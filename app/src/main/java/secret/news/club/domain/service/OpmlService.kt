package secret.news.club.domain.service

import android.content.Context
import be.ceau.opml.OpmlWriter
import be.ceau.opml.entity.Body
import be.ceau.opml.entity.Head
import be.ceau.opml.entity.Opml
import be.ceau.opml.entity.Outline
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import secret.news.club.domain.model.feed.Feed
import secret.news.club.domain.repository.FeedDao
import secret.news.club.domain.repository.GroupDao
import secret.news.club.infrastructure.di.IODispatcher
import secret.news.club.infrastructure.rss.OPMLDataSource
import secret.news.club.ui.ext.currentAccountId
import secret.news.club.ui.ext.getDefaultGroupId
import java.io.InputStream
import java.util.*
import javax.inject.Inject

/**
 * Supports import and export from OPML files.
 */
class OpmlService @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val groupDao: GroupDao,
    private val feedDao: FeedDao,
    private val accountService: AccountService,
    private val rssService: RssService,
    private val OPMLDataSource: OPMLDataSource,
    @IODispatcher
    private val ioDispatcher: CoroutineDispatcher,
) {

    /**
     * Imports OPML file.
     *
     * @param [inputStream] input stream of OPML file
     */
    @Throws(Exception::class)
    suspend fun saveToDatabase(inputStream: InputStream) {
        withContext(ioDispatcher) {
            val defaultGroup = groupDao.queryById(getDefaultGroupId(context.currentAccountId))!!
            val groupWithFeedList =
                OPMLDataSource.parseFileInputStream(inputStream, defaultGroup, context.currentAccountId)
            groupWithFeedList.forEach { groupWithFeed ->
                if (groupWithFeed.group != defaultGroup) {
                    groupDao.insert(groupWithFeed.group)
                }
                val repeatList = mutableListOf<Feed>()
                groupWithFeed.feeds.forEach {
                    it.groupId = groupWithFeed.group.id
                    if (rssService.get().isFeedExist(it.url)) {
                        repeatList.add(it)
                    }
                }
                feedDao.insertList((groupWithFeed.feeds subtract repeatList.toSet()).toList())
            }
        }
    }

    /**
     * Exports OPML file.
     */
    @Throws(Exception::class)
    suspend fun saveToString(accountId: Int, attachInfo: Boolean): String {
        val defaultGroup = groupDao.queryById(getDefaultGroupId(accountId))
        return OpmlWriter().write(
            Opml(
                "2.0",
                Head(
                    accountService.getCurrentAccount().name,
                    Date().toString(), null, null, null,
                    null, null, null, null,
                    null, null, null, null,
                ),
                Body(groupDao.queryAllGroupWithFeed(accountId).map {
                    Outline(
                        mutableMapOf(
                            "text" to it.group.name,
                            "title" to it.group.name,
                        ).apply {
                            if (attachInfo) {
                                put("isDefault", (it.group.id == defaultGroup?.id).toString())
                            }
                        },
                        it.feeds.map { feed ->
                            Outline(
                                mutableMapOf(
                                    "text" to feed.name,
                                    "title" to feed.name,
                                    "xmlUrl" to feed.url,
                                    "htmlUrl" to feed.url
                                ).apply {
                                    if (attachInfo) {
                                        put("isNotification", feed.isNotification.toString())
                                        put("isFullContent", feed.isFullContent.toString())
                                        put("isBrowser", feed.isBrowser.toString())
                                    }
                                },
                                listOf()
                            )
                        }
                    )
                })
            )
        )!!
    }

    private fun getDefaultGroupId(accountId: Int): String = accountId.getDefaultGroupId()
}
