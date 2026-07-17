package secret.news.club.domain.service

import secret.news.club.domain.repository.ArticleDao
import java.security.MessageDigest
import javax.inject.Inject

/**
 * Resolves a broadcast-push article URL to a local Article id when the recipient has
 * already synced it, since a push can't reference a device-specific Room row id.
 */
class ArticleUrlResolver @Inject constructor(
    private val articleDao: ArticleDao,
    private val accountService: AccountService,
) {
    suspend fun findLocalArticleIdByUrl(url: String): String? =
        articleDao.queryIdByLink(url, accountService.getCurrentAccountId())

    /** Synthetic id for an article that isn't in Room, never collides with a real "$accountId$uuid" id. */
    fun transientArticleId(url: String): String {
        val digest = MessageDigest.getInstance("SHA-256").digest(url.toByteArray())
        return "push$" + digest.joinToString("") { "%02x".format(it) }
    }

    companion object {
        const val TRANSIENT_FEED_ID = "push_transient_feed"
    }
}
