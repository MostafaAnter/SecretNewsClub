package secret.news.club.domain.service

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import secret.news.club.domain.util.detectUserCountry
import secret.news.club.infrastructure.di.ApplicationScope
import secret.news.club.infrastructure.rss.RssHelper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultSubscriptionManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val rssService: RssService,
    private val rssHelper: RssHelper,
    private val rssServiceManager: RssServiceManager,
    @ApplicationScope private val applicationScope: CoroutineScope
) {

    fun addDefaultSubscriptions() {
        applicationScope.launch {
            val countryCode = detectUserCountry(context)
            val defaultFeeds = rssServiceManager.getRssServices(context, countryCode)

            val groups = rssService.get().pullGroups().first()
            val firstGroupId = groups.firstOrNull()?.id ?: rssService.get().addGroup(null, "Default")

            defaultFeeds.forEach { feed ->
                if (!rssService.get().isFeedExist(feed.url)) {
                    runCatching {
                        rssHelper.searchFeed(feed.url)
                    }.onSuccess { searchedFeed ->
                        rssService.get().subscribe(
                            searchedFeed = searchedFeed,
                            feedLink = feed.url,
                            groupId = firstGroupId,
                            isNotification = feed.category == secret.news.club.domain.model.rss.RssCategory.NEWS,
                            isFullContent = true,
                            isBrowser = false,
                        )
                    }
                }
            }
        }
    }
}