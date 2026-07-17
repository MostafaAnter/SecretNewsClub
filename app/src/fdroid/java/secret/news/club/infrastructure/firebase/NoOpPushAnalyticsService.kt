package secret.news.club.infrastructure.firebase

import secret.news.club.domain.service.PushAnalyticsService
import javax.inject.Inject

/**
 * No-op binding for the fdroid flavor, which ships no Firebase/Play Services
 * dependency at all per F-Droid's anti-tracking inclusion policy.
 */
class NoOpPushAnalyticsService @Inject constructor() : PushAnalyticsService {
    override fun subscribeToBroadcastTopic() = Unit
    override fun unsubscribeFromBroadcastTopic() = Unit
    override fun updateCountryTopicSubscription(countryCode: String) = Unit
    override fun clearCountryTopicSubscription() = Unit
    override fun logPushReceived(articleUrl: String) = Unit
    override fun logPushOpened(articleUrl: String) = Unit
}
