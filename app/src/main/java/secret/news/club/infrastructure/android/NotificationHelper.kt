package secret.news.club.infrastructure.android

import android.Manifest
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import secret.news.club.R
import secret.news.club.domain.model.feed.FeedWithArticle
import secret.news.club.ui.page.common.ExtraName
import secret.news.club.ui.page.common.NotificationGroupName
import javax.inject.Inject

class NotificationHelper @Inject constructor(
    @ApplicationContext
    private val context: Context,
) {

    private val notificationManager: NotificationManagerCompat =
        NotificationManagerCompat.from(context).apply {
            createNotificationChannel(
                NotificationChannel(
                    NotificationGroupName.ARTICLE_UPDATE,
                    NotificationGroupName.ARTICLE_UPDATE,
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

        // Stable per-feed id: re-notifying the same feed replaces the previous
        // notification instead of stacking a new one, on top of the coalescing below.
        val notificationId = feedWithArticle.feed.id.hashCode()
        val latestArticle = articles.first()
        val contentIntent = PendingIntent.getActivity(
            context,
            notificationId,
            Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                putExtra(ExtraName.ARTICLE_ID, latestArticle.id)
            },
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(context, NotificationGroupName.ARTICLE_UPDATE)
            .setSmallIcon(R.drawable.ic_launcher_round)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.ic_launcher_round))
            .setContentIntent(contentIntent)
            .setGroup(feedWithArticle.feed.id)
            .setAutoCancel(true)

        if (articles.size == 1) {
            builder
                .setContentTitle(latestArticle.title)
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText(latestArticle.shortDescription)
                        .setSummaryText(feedWithArticle.feed.name)
                )
        } else {
            builder
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
                        .also { style -> articles.take(5).forEach { style.addLine(it.title) } }
                )
        }

        notificationManager.notify(notificationId, builder.build())
    }
}
