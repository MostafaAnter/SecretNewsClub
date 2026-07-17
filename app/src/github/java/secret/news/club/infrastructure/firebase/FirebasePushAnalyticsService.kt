package secret.news.club.infrastructure.firebase

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import secret.news.club.domain.service.PushAnalyticsService
import secret.news.club.infrastructure.di.ApplicationScope
import secret.news.club.ui.ext.DataStoreKey
import secret.news.club.ui.ext.dataStore
import secret.news.club.ui.ext.get
import secret.news.club.ui.ext.put
import javax.inject.Inject

class FirebasePushAnalyticsService @Inject constructor(
    @ApplicationContext private val context: Context,
    @ApplicationScope private val applicationScope: CoroutineScope,
) : PushAnalyticsService {

    private val messaging = FirebaseMessaging.getInstance()
    private val analytics = FirebaseAnalytics.getInstance(context)

    override fun subscribeToBroadcastTopic() {
        messaging.subscribeToTopic(BROADCAST_TOPIC)
    }

    override fun unsubscribeFromBroadcastTopic() {
        messaging.unsubscribeFromTopic(BROADCAST_TOPIC)
    }

    override fun updateCountryTopicSubscription(countryCode: String) {
        val newTopic = countryTopic(countryCode)
        applicationScope.launch {
            val previousTopic = context.dataStore.get<String>(DataStoreKey.pushSubscribedCountryTopic)
            if (previousTopic == newTopic) return@launch
            if (!previousTopic.isNullOrBlank()) {
                messaging.unsubscribeFromTopic(previousTopic)
            }
            messaging.subscribeToTopic(newTopic)
            context.dataStore.put(DataStoreKey.pushSubscribedCountryTopic, newTopic)
        }
    }

    override fun clearCountryTopicSubscription() {
        applicationScope.launch {
            context.dataStore.get<String>(DataStoreKey.pushSubscribedCountryTopic)
                ?.takeIf { it.isNotBlank() }
                ?.let { messaging.unsubscribeFromTopic(it) }
            context.dataStore.put(DataStoreKey.pushSubscribedCountryTopic, "")
        }
    }

    override fun logPushReceived(articleUrl: String) {
        analytics.logEvent("push_received", Bundle().apply { putString("article_url", articleUrl) })
    }

    override fun logPushOpened(articleUrl: String) {
        analytics.logEvent("push_opened", Bundle().apply { putString("article_url", articleUrl) })
    }

    companion object {
        const val BROADCAST_TOPIC = "broadcast_all"

        /** FCM topic names allow only [a-zA-Z0-9-_.~%]; country codes are already safe. */
        fun countryTopic(countryCode: String) = "broadcast_country_${countryCode.trim().uppercase()}"
    }
}
