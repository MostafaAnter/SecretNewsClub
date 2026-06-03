package secret.news.club.infrastructure.rss.discovery

import android.util.Log
import com.rometools.rome.feed.synd.SyndFeed
import com.rometools.rome.io.SyndFeedInput
import com.rometools.rome.io.XmlReader
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.executeAsync
import secret.news.club.infrastructure.di.IODispatcher

/**
 * Verifies a candidate URL is actually a parseable RSS/Atom feed, and produces
 * a content fingerprint for dedup across URL aliases (the yallakora.com problem
 * surfaced in Stage A — same feed served at 9 different paths).
 *
 * Uses Rome (already a project dep, see [secret.news.club.infrastructure.rss.RssHelper]).
 */
class FeedProber @Inject constructor(
    private val okHttpClient: OkHttpClient,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
) {

    data class ProbeResult(
        val title: String?,
        val entryCount: Int,
        /** Stable hash to detect aliased feeds. Null when feed has no entries. */
        val contentFingerprint: String?,
    )

    /** Returns null if the URL doesn't resolve to a valid feed, or on any error. */
    suspend fun probe(url: String): ProbeResult? = withContext(ioDispatcher) {
        try {
            val response = okHttpClient.newCall(Request.Builder().url(url).build()).executeAsync()
            if (!response.isSuccessful) {
                response.close()
                return@withContext null
            }
            val contentType = response.header("Content-Type")
            response.body.byteStream().use { stream ->
                val feed: SyndFeed = SyndFeedInput().build(XmlReader(stream, contentType))
                buildResult(feed)
            }
        } catch (t: Throwable) {
            Log.d("RssDiscovery", "probe failed for $url: ${t.message}")
            null
        }
    }

    private fun buildResult(feed: SyndFeed): ProbeResult {
        val title = feed.title?.trim()?.takeIf { it.isNotBlank() }
        val entries = feed.entries.orEmpty()
        val fingerprint = if (entries.isNotEmpty()) {
            // title + first-entry link is enough to detect aliased feeds. Hash to
            // keep DB column size small and to avoid storing raw URLs from foreign feeds.
            val firstLink = entries.first().link?.trim().orEmpty()
            (title.orEmpty() + "|" + firstLink).hashCode().toString(16)
        } else null
        return ProbeResult(title = title, entryCount = entries.size, contentFingerprint = fingerprint)
    }
}