package secret.news.club.domain.service

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import secret.news.club.infrastructure.di.ApplicationScope
import secret.news.club.infrastructure.rss.RssHelper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultSubscriptionManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val rssService: RssService,
    private val rssHelper: RssHelper,
    @ApplicationScope private val applicationScope: CoroutineScope
) {

    fun addDefaultSubscriptions() {
        applicationScope.launch {
            val defaultFeeds = listOf(
                "https://www.youm7.com/rss/SectionRss?SectionID=97",
                "https://www.youm7.com/rss/SectionRss?SectionID=319",
                "https://www.youm7.com/rss/SectionRss?SectionID=297",
                "https://www.almasryalyoum.com/rss/rssfeeds",
                "https://www.elbalad.news/rss.aspx",
            )

            val groups = rssService.get().pullGroups().first()
            val firstGroupId = groups.firstOrNull()?.id ?: rssService.get().addGroup(null, "Default")

            defaultFeeds.forEach { feedUrl ->
                if (!rssService.get().isFeedExist(feedUrl)) {
                    runCatching {
                        rssHelper.searchFeed(feedUrl)
                    }.onSuccess { searchedFeed ->
                        rssService.get().subscribe(
                            searchedFeed = searchedFeed,
                            feedLink = feedUrl,
                            groupId = firstGroupId,
                            isNotification = feedUrl.endsWith("97", true),
                            isFullContent = true,
                            isBrowser = false,
                        )
                    }
                }
            }
        }
    }
}