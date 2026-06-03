package secret.news.club.infrastructure.rss.discovery

import android.util.Log
import java.net.URLEncoder
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.executeAsync
import org.jsoup.Jsoup
import secret.news.club.infrastructure.di.IODispatcher

/**
 * Fallback search backend (priority 1). Used when DuckDuckGo returns empty.
 * Bing tolerates Arabic queries and is less aggressive than Google about
 * CAPTCHAing mobile/datacenter IPs.
 *
 * Endpoint: bing.com/search. Result anchors live under `li.b_algo h2 a`.
 */
@Singleton
class BingHtmlSearchClient @Inject constructor(
    private val okHttpClient: OkHttpClient,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
) : SearchEngineClient {

    override val priority: Int = 1
    override val name: String = "Bing"

    override suspend fun search(query: String, language: String, max: Int): List<String> =
        withContext(ioDispatcher) {
            val url = "https://www.bing.com/search?q=" +
                URLEncoder.encode(query, "UTF-8") + "&count=$max"
            val request = Request.Builder()
                .url(url)
                .header("User-Agent", BROWSER_UA)
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                .header("Accept-Language", "$language,en;q=0.5")
                .build()
            try {
                val response = okHttpClient.newCall(request).executeAsync()
                if (!response.isSuccessful) {
                    response.close()
                    return@withContext emptyList()
                }
                val html = response.body.string()
                parseResults(html, max)
            } catch (t: Throwable) {
                Log.d("RssDiscovery", "Bing search failed for '$query': ${t.message}")
                emptyList()
            }
        }

    private fun parseResults(html: String, max: Int): List<String> {
        val doc = runCatching { Jsoup.parse(html) }.getOrNull() ?: return emptyList()
        val urls = LinkedHashSet<String>()
        for (a in doc.select("li.b_algo h2 a, li.b_algo a.tilk")) {
            if (urls.size >= max) break
            val href = a.attr("href").ifBlank { continue }
            if (href.startsWith("http") && "bing.com" !in href) {
                urls += href
            }
        }
        return urls.toList()
    }

    companion object {
        private const val BROWSER_UA =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
                "(KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36"
    }
}