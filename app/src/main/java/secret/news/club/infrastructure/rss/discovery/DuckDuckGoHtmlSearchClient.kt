package secret.news.club.infrastructure.rss.discovery

import android.util.Log
import java.net.URLDecoder
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
 * Primary search backend (priority 0). Stage A confirmed DDG HTML returns
 * usable Arabic results where Google was 100% CAPTCHAed.
 *
 * Endpoint: html.duckduckgo.com/html — a no-JS variant intended for screen
 * readers. Result links are wrapped in `//duckduckgo.com/l/?uddg=<encoded>`
 * which we unwrap. Selector: `a.result__a` (the result-title anchor).
 */
@Singleton
class DuckDuckGoHtmlSearchClient @Inject constructor(
    private val okHttpClient: OkHttpClient,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
) : SearchEngineClient {

    override val priority: Int = 0
    override val name: String = "DuckDuckGo"

    override suspend fun search(query: String, language: String, max: Int): List<String> =
        withContext(ioDispatcher) {
            val url = "https://html.duckduckgo.com/html/?q=" +
                URLEncoder.encode(query, "UTF-8")
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
                Log.d("RssDiscovery", "DDG search failed for '$query': ${t.message}")
                emptyList()
            }
        }

    private fun parseResults(html: String, max: Int): List<String> {
        val doc = runCatching { Jsoup.parse(html) }.getOrNull() ?: return emptyList()
        val urls = LinkedHashSet<String>()
        for (a in doc.select("a.result__a, a.result__url")) {
            if (urls.size >= max) break
            val href = a.attr("href").ifBlank { continue }
            val unwrapped = unwrapRedirect(href)
            if (unwrapped.startsWith("http") && "duckduckgo.com" !in unwrapped) {
                urls += unwrapped
            }
        }
        return urls.toList()
    }

    private fun unwrapRedirect(href: String): String {
        // DDG result hrefs look like:  //duckduckgo.com/l/?uddg=<encoded>&rut=...
        val marker = "uddg="
        val i = href.indexOf(marker)
        if (i < 0) return href
        val encoded = href.substring(i + marker.length).substringBefore('&')
        return runCatching { URLDecoder.decode(encoded, "UTF-8") }.getOrDefault(href)
    }

    companion object {
        private const val BROWSER_UA =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
                "(KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36"
    }
}