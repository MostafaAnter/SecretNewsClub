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
            "https://www.youm7.com/rss/SectionRss?SectionID=203",
            "https://www.youm7.com/rss/SectionRss?SectionID=319",
            "https://www.youm7.com/rss/SectionRss?SectionID=97",
            "https://www.youm7.com/rss/SectionRss?SectionID=297",
            "https://www.youm7.com/rss/SectionRss?SectionID=298",
            "https://www.youm7.com/rss/SectionRss?SectionID=88",
            "https://www.youm7.com/rss/SectionRss?SectionID=286",
            "https://www.youm7.com/rss/SectionRss?SectionID=94",
            "https://www.masrawy.com/rss/feed/25/%D8%A3%D8%AE%D8%A8%D8%A7%D8%B1",
            "https://www.masrawy.com/rss/feed/27/%D8%B1%D9%8A%D8%A7%D8%B6%D8%A9",
            "https://www.almasryalyoum.com/rss/rssfeed",
            "https://www.almasryalyoum.com/rss/rssfeed?homePage=true",
            "https://www.almasryalyoum.com/rss/rssfeed?typeId=2&homePage=false",
            "https://www.egyptindependent.com/rss_feed/",
            "https://www.dailynewsegypt.com/feed/",
            "https://www.egyptianstreets.com/feed/",
            "https://www.masress.com/en/rss",
            "https://www.bbc.com/arabic/rss.xml",
            "https://www.aljazeera.net/aljazeerarss",
            "https://gate.ahram.org.eg/rss",
            "https://english.ahram.org.eg/rss/",
            "https://www.presidency.eg/EN/rss/",
            "https://elbalad.news/rss.aspx",
            "https://egyptoil-gas.com/news/feed/",
            "https://www.egypttoday.com/english/rss.xml",
            "https://feeds.feedburner.com/KingFut",
            "https://nilesports.com/feed/"
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
                            isNotification = false,
                            isFullContent = false,
                            isBrowser = false,
                        )
                    }
                }
            }
        }
    }
}