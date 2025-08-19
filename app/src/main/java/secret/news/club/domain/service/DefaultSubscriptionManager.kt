package secret.news.club.domain.service

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import secret.news.club.R
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

    private val _message = MutableStateFlow<String?>(null)
    val message = _message.asStateFlow()

    private var lastCountryCode : String? = null
    private var subscriptionJob: Job? = null

    init {
        applicationScope.launch {
            lastCountryCode = settingsProvider.settingsFlow.first().country?.value
            settingsProvider.settingsFlow.collect { settings ->
                addDefaultSubscriptions(settings.country?.value)
            }
        }
    }

    private fun addDefaultSubscriptions(countryCode: String?) {
        subscriptionJob?.cancel()
        subscriptionJob = applicationScope.launch {
            val resolvedCountryCode =
                countryCode?.ifEmpty { detectUserCountry(context) } ?: detectUserCountry(context)


            if (resolvedCountryCode != lastCountryCode) {
                lastCountryCode = resolvedCountryCode
                _message.value = context.getString(R.string.updating_feeds)
                rssService.get().deleteAllFeeds()
            }

            val defaultFeeds = rssServiceManager.getRssServices(resolvedCountryCode)
            val existingFeeds =
                rssService.get().pullFeeds().first().flatMap { it.feeds }.map { it.url }.toSet()

            if (defaultFeeds.isEmpty()) {
                _message.value = context.getString(R.string.no_feeds_for_country)
                return@launch
            }

            val groups = rssService.get().pullGroups().first()
            val firstGroupId = groups.firstOrNull()?.id ?: rssService.get().addGroup(null, "Default")

            var newFeedsAdded = false
            defaultFeeds.forEachIndexed { index, feed ->
                if (!existingFeeds.contains(feed.url)) {
                    newFeedsAdded = true
                    runCatching {
                        withContext(Dispatchers.IO) {
                            rssHelper.searchFeed(feed.url)
                        }
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

            if (newFeedsAdded) {
                _message.value = context.getString(R.string.feeds_updated)
            } else {
                _message.value = null
            }
        }
    }
}
