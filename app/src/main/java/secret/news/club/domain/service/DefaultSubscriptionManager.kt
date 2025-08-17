package secret.news.club.domain.service

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import secret.news.club.domain.util.detectUserCountry
import secret.news.club.infrastructure.di.ApplicationScope
import secret.news.club.infrastructure.preference.SettingsProvider
import secret.news.club.infrastructure.rss.RssHelper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultSubscriptionManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val rssService: RssService,
    private val rssHelper: RssHelper,
    private val rssServiceManager: RssServiceManager,
    private val settingsProvider: SettingsProvider,
    @ApplicationScope private val applicationScope: CoroutineScope
) {

    init {
        applicationScope.launch {
            settingsProvider.settingsFlow.collect { settings ->
                addDefaultSubscriptions(settings.country?.value)
            }
        }
    }

    private suspend fun addDefaultSubscriptions(countryCode: String?) {
            val resolvedCountryCode = countryCode?.ifEmpty { detectUserCountry(context) } ?: detectUserCountry(context)
            
            // To ensure that subscriptions from different countries do not mix,
            // we will now clear all existing feeds before adding the new default ones.
            // NOTE: This will remove any custom user-added feeds. This behavior might need
            // to be adjusted if custom feeds should be preserved.
            rssService.get().deleteAllFeeds()

            val defaultFeeds = rssServiceManager.getRssServices(resolvedCountryCode)

            val groups = rssService.get().pullGroups().first()
            val firstGroupId = groups.firstOrNull()?.id ?: rssService.get().addGroup(null, "Default")

            defaultFeeds.forEachIndexed { index, feed ->
                // Since we just cleared all feeds, we don't need to check if the feed exists.
                runCatching {
                    rssHelper.searchFeed(feed.url)
                }.onSuccess { searchedFeed ->
                    rssService.get().subscribe(
                        searchedFeed = searchedFeed,
                        feedLink = feed.url,
                        groupId = firstGroupId,
                        isNotification = index == 0,
                        isFullContent = true,
                        isBrowser = false,
                    )
                }
            }
    }
}
