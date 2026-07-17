package secret.news.club.infrastructure.android

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import secret.news.club.domain.service.PushAnalyticsService
import secret.news.club.ui.page.common.ExtraName
import javax.inject.Inject

/**
 * Handles data-only broadcast pushes (see app/google-services.json + Firebase Console ->
 * Cloud Messaging -> send to the "broadcast_all" topic). Data-only, not a "notification"
 * message, so onMessageReceived always runs here, foreground/background/killed alike, and
 * the notification is built consistently via NotificationHelper regardless of app state.
 *
 * Expected data payload keys: title (required), body (optional), article_url (required),
 * image_url (optional).
 */
@AndroidEntryPoint
class PushMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var notificationHelper: NotificationHelper

    @Inject
    lateinit var pushAnalyticsService: PushAnalyticsService

    override fun onMessageReceived(message: RemoteMessage) {
        val data = message.data
        val title = data["title"] ?: return
        val articleUrl = data[ExtraName.FCM_DATA_ARTICLE_URL] ?: return

        pushAnalyticsService.logPushReceived(articleUrl)
        notificationHelper.notifyPush(
            title = title,
            body = data["body"].orEmpty(),
            articleUrl = articleUrl,
            imageUrl = data["image_url"],
        )
    }
}
