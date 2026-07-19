package secret.news.club.infrastructure.android

import android.Manifest
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.Request
import secret.news.club.R
import secret.news.club.domain.model.article.Article
import secret.news.club.domain.model.feed.FeedWithArticle
import secret.news.club.ui.page.common.ExtraName
import secret.news.club.ui.page.common.NotificationGroupName
import javax.inject.Inject

class NotificationHelper @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val okHttpClient: OkHttpClient,
) {
    companion object {
        private const val MAX_NOTIFICATIONS_PER_FEED = 3
    }

    private val notificationManager: NotificationManagerCompat =
        NotificationManagerCompat.from(context).apply {
            createNotificationChannel(
                NotificationChannel(
                    NotificationGroupName.ARTICLE_UPDATE,
                    NotificationGroupName.ARTICLE_UPDATE,
                    NotificationManager.IMPORTANCE_DEFAULT
                )
            )
            createNotificationChannel(
                NotificationChannel(
                    NotificationGroupName.PUSH_BROADCAST,
                    context.getString(R.string.push_broadcast_channel_name),
                    NotificationManager.IMPORTANCE_DEFAULT
                )
            )
        }

    fun cancelAllNotifications() {
        notificationManager.notificationChannelGroups.forEach { group ->
            notificationManager.deleteNotificationChannelGroup(group.id)
        }
        notificationManager.cancelAll()
    }

    fun notify(feedWithArticle: FeedWithArticle) {
        val articles = feedWithArticle.articles
        if (articles.isEmpty()) return

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        notificationManager.createNotificationChannelGroup(
            NotificationChannelGroup(
                feedWithArticle.feed.id,
                feedWithArticle.feed.name
            )
        )

        if (articles.size == 1) {
            val article = articles.first()
            notificationManager.notify(
                article.id.hashCode(),
                articleNotificationBuilder(feedWithArticle, article, groupKey = null)
                    .setContentTitle(article.title)
                    .setStyle(
                        NotificationCompat.BigTextStyle()
                            .bigText(article.shortDescription)
                            .setSummaryText(feedWithArticle.feed.name)
                    )
                    .build()
            )
            return
        }

        // Multiple new articles from the same feed: post one notification per article
        // (each individually expandable to its own full description via BigTextStyle),
        // grouped so the shade still collapses them into a single card by default —
        // expanding that group reveals each article's own, separately-expandable entry.
        // Capped to match the summary's own line cap below.
        val groupKey = feedWithArticle.feed.id
        articles.take(MAX_NOTIFICATIONS_PER_FEED).forEach { article ->
            notificationManager.notify(
                article.id.hashCode(),
                articleNotificationBuilder(feedWithArticle, article, groupKey = groupKey)
                    .setContentTitle(article.title)
                    .setStyle(
                        NotificationCompat.BigTextStyle()
                            .bigText(article.shortDescription)
                            .setSummaryText(feedWithArticle.feed.name)
                    )
                    .build()
            )
        }

        // Stable per-feed id: re-notifying the same feed replaces the previous summary
        // instead of stacking a new one.
        val summaryBuilder = articleNotificationBuilder(feedWithArticle, articles.first(), groupKey = groupKey)
            .setGroupSummary(true)
            .setContentTitle(feedWithArticle.feed.name)
            .setContentText(
                context.resources.getQuantityString(
                    R.plurals.new_articles_notification,
                    articles.size,
                    articles.size
                )
            )
            .setStyle(
                // Note: InboxStyle declares its own member fun named `apply`, which
                // shadows kotlin.apply via SAM conversion — build it via plain chaining.
                NotificationCompat.InboxStyle()
                    .setBigContentTitle(feedWithArticle.feed.name)
                    .setSummaryText(feedWithArticle.feed.name)
                    .also { style -> articles.take(MAX_NOTIFICATIONS_PER_FEED).forEach { style.addLine(it.title) } }
            )

        notificationManager.notify(feedWithArticle.feed.id.hashCode(), summaryBuilder.build())
    }

    private fun articleNotificationBuilder(
        feedWithArticle: FeedWithArticle,
        article: Article,
        groupKey: String?,
    ): NotificationCompat.Builder {
        val contentIntent = PendingIntent.getActivity(
            context,
            article.id.hashCode(),
            Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                putExtra(ExtraName.ARTICLE_ID, article.id)
            },
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        return NotificationCompat.Builder(context, NotificationGroupName.ARTICLE_UPDATE)
            .setSmallIcon(R.drawable.ic_launcher_round)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.ic_launcher_round))
            .setContentIntent(contentIntent)
            .setAutoCancel(true)
            .apply { groupKey?.let { setGroup(it) } }
    }

    /**
     * Notification for a broadcast push (see PushMessagingService), keyed by article URL.
     * Called from FCM's onMessageReceived, which already runs off the main thread, so the
     * synchronous image fetch below is safe.
     */
    fun notifyPush(title: String, body: String, articleUrl: String, imageUrl: String?) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        val notificationId = articleUrl.hashCode()
        val contentIntent = PendingIntent.getActivity(
            context,
            notificationId,
            Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                putExtra(ExtraName.ARTICLE_URL, articleUrl)
            },
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val largeIcon = imageUrl?.let { fetchBitmap(it) }
            ?: BitmapFactory.decodeResource(context.resources, R.drawable.ic_launcher_round)

        val builder = NotificationCompat.Builder(context, NotificationGroupName.PUSH_BROADCAST)
            .setSmallIcon(R.drawable.ic_launcher_round)
            .setLargeIcon(largeIcon)
            .setContentIntent(contentIntent)
            .setAutoCancel(true)
            .setContentTitle(title)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))

        if (body.isNotBlank()) {
            builder.setContentText(body)
        }

        notificationManager.notify(notificationId, builder.build())
    }

    private fun fetchBitmap(url: String): Bitmap? = runCatching {
        okHttpClient.newCall(Request.Builder().url(url).build()).execute().use { response ->
            response.body.byteStream().use { BitmapFactory.decodeStream(it) }
        }
    }.getOrNull()
}
