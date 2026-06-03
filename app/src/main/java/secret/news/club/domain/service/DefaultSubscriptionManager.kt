package secret.news.club.domain.service

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import secret.news.club.R
import secret.news.club.domain.model.rss.DiscoveredFeedEntity
import secret.news.club.domain.model.rss.RssCategory
import secret.news.club.domain.model.rss.RssService as PredefinedRssService
import secret.news.club.domain.repository.DiscoveredFeedDao
import secret.news.club.domain.util.detectUserCountry
import secret.news.club.infrastructure.di.ApplicationScope
import secret.news.club.infrastructure.preference.SettingsProvider
import secret.news.club.infrastructure.rss.RssHelper
import secret.news.club.infrastructure.rss.hasNativeScript
import secret.news.club.infrastructure.rss.NativeLanguageKeywords
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(ExperimentalCoroutinesApi::class)
@Singleton
class DefaultSubscriptionManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val rssService: RssService,
    private val rssHelper: RssHelper,
    private val rssServiceManager: RssServiceManager,
    private val discoveredFeedDao: DiscoveredFeedDao,
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
            // Re-trigger on country change OR on discovered_feed updates for the
            // current country. flatMapLatest on the country flow keeps a single
            // active discovered_feed subscription at any time.
            settingsProvider.settingsFlow
                .map { it.country?.value.orEmpty() }
                .distinctUntilChanged()
                .flatMapLatest { country ->
                    val resolved = country.ifEmpty { detectUserCountry(context) }
                    discoveredFeedDao.flowByCountry(resolved)
                        .map { discovered -> resolved to discovered }
                }
                .collect { (resolved, discovered) ->
                    addDefaultSubscriptions(resolved, discovered)
                }
        }
    }

    private fun addDefaultSubscriptions(
        resolvedCountryCode: String,
        discoveredForCountry: List<DiscoveredFeedEntity>,
    ) {
        subscriptionJob?.cancel()
        subscriptionJob = applicationScope.launch {

            val countryChanged = resolvedCountryCode != lastCountryCode
            if (countryChanged) {
                lastCountryCode = resolvedCountryCode
                _message.value = context.getString(R.string.updating_feeds)
                // Existing destructive behavior: switching country wipes feeds.
                // Don't fire on mere discovered-feed arrivals — only on actual
                // country changes — or we'd kill the user's current subscriptions
                // every time the worker emits a row.
                rssService.get().deleteAllFeeds()
            }

            val staticFeeds = rssServiceManager.getRssServices(resolvedCountryCode)

            // Promote native-script discovered feeds so the top one (which gets
            // `isNotification = true`) prefers native content over Latin fallbacks.
            val nativeLang = NativeLanguageKeywords.languageForCountry(resolvedCountryCode)
            val sortedDiscovered: List<PredefinedRssService> = discoveredForCountry
                .map {
                    PredefinedRssService(
                        name = it.name,
                        url = it.url,
                        category = runCatching { RssCategory.valueOf(it.category) }
                            .getOrDefault(RssCategory.NEWS),
                        language = it.language,
                        countryCode = it.countryCode,
                        isNativeLanguage = it.language.equals(nativeLang, ignoreCase = true) ||
                            it.name.hasNativeScript(nativeLang),
                    )
                }
                .sortedByDescending { it.isNativeLanguage }

            // Static first (curated), then discovered (machine-found). URL dedup
            // across both sets — static wins on collision. The worker's
            // `dedupAliasedFor` already handled fingerprint dedup within discovered.
            val seenUrls = mutableSetOf<String>()
            val merged = (staticFeeds + sortedDiscovered).filter { seenUrls.add(it.url) }

            if (merged.isEmpty()) {
                _message.value = context.getString(R.string.no_feeds_for_country)
                return@launch
            }

            // Use a one-shot suspend query instead of pullGroups()/pullFeeds().first().
            // Those return Room Flows whose initial emission can be a stale cached snapshot
            // taken before deleteAllFeeds() committed — which would hand us a non-existent
            // groupId and crash the subsequent feedDao.insert with a FOREIGN KEY violation.
            val groupsWithFeeds = rssService.get().queryAllGroupWithFeeds()
            val existingFeeds = groupsWithFeeds.flatMap { it.feeds }.map { it.url }.toSet()
            val firstGroupId = groupsWithFeeds.firstOrNull()?.group?.id
                ?: rssService.get().addGroup(null, "Default")

            var newFeedsAdded = false
            merged.forEachIndexed { index, feed ->
                if (!existingFeeds.contains(feed.url)) {
                    newFeedsAdded = true
                    runCatching {
                        withContext(Dispatchers.IO) {
                            rssHelper.searchFeed(feed.url)
                        }
                    }.onSuccess { searchedFeed ->
                        // Discovered feeds often have entries but no <title> in
                        // their XML. AbstractRssRepository.subscribe force-unwraps
                        // searchedFeed.title.decodeHTML()!! and NPEs in that case.
                        // Use the curated/discovered name as a fallback so the
                        // user always sees a meaningful feed label.
                        if (searchedFeed.title.isNullOrBlank()) {
                            searchedFeed.title = feed.name
                        }
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
