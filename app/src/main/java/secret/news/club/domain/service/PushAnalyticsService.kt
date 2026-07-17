package secret.news.club.domain.service

/**
 * Abstracts broadcast-push subscription and analytics logging so that shared code
 * (MainActivity, AndroidApp, NotificationHelper) never references Firebase directly.
 * The fdroid flavor binds a no-op implementation, since F-Droid's inclusion policy
 * forbids proprietary/tracking libraries.
 */
interface PushAnalyticsService {
    fun subscribeToBroadcastTopic()
    fun unsubscribeFromBroadcastTopic()

    /** Subscribes to the topic for [countryCode], unsubscribing from any previously tracked country topic. */
    fun updateCountryTopicSubscription(countryCode: String)

    /** Unsubscribes from whichever country topic is currently tracked, if any. */
    fun clearCountryTopicSubscription()

    fun logPushReceived(articleUrl: String)
    fun logPushOpened(articleUrl: String)
}
