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
import java.util.*
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

    fun notify(feedWithArticle: FeedWithArticle) {
        notificationManager.createNotificationChannelGroup(
            NotificationChannelGroup(
                feedWithArticle.feed.id,
                feedWithArticle.feed.name
            )
        )
        feedWithArticle.articles.forEach { article ->
            val builder = NotificationCompat.Builder(context, NotificationGroupName.ARTICLE_UPDATE)
                .setSmallIcon(R.drawable.ic_launcher_round)
                .setLargeIcon(
                    (BitmapFactory.decodeResource(
                        context.resources,
                        R.drawable.ic_launcher_round
                    ))
                )
                .setContentTitle(article.title)
                .setContentIntent(
                    PendingIntent.getActivity(
                        context,
                        Random().nextInt() + article.id.hashCode(),
                        Intent(context, MainActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK
                            putExtra(
                                ExtraName.ARTICLE_ID,
                                article.id
                            )
                        },
                        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                    )
                )
                .setGroup(feedWithArticle.feed.id)
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText(article.shortDescription)
                        .setSummaryText(feedWithArticle.feed.name)
                )

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            notificationManager.notify(
                Random().nextInt() + article.id.hashCode(),
                builder.build().apply {
                    flags = Notification.FLAG_AUTO_CANCEL
                }
            )
        }

        if (feedWithArticle.articles.size > 1) {
            notificationManager.notify(
                Random().nextInt() + feedWithArticle.feed.id.hashCode(),
                NotificationCompat.Builder(context, NotificationGroupName.ARTICLE_UPDATE)
                    .setSmallIcon(R.drawable.ic_launcher_round)
                    .setLargeIcon(
                        (BitmapFactory.decodeResource(
                            context.resources,
                            R.drawable.ic_launcher_round
                        ))
                    )
                    .setStyle(
                        NotificationCompat.InboxStyle()
                            .setSummaryText(feedWithArticle.feed.name)
                    )
                    .setGroup(feedWithArticle.feed.id)
                    .setGroupSummary(true)
                    .build()
            )
        }
    }
}
