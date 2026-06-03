package secret.news.club.infrastructure.rss.discovery

import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.executeAsync
import secret.news.club.domain.model.rss.RssCategory
import secret.news.club.domain.service.discovery.DiscoveredFeed
import secret.news.club.domain.service.discovery.DiscoverySource
import secret.news.club.domain.service.discovery.RssDiscoveryEngine
import secret.news.club.infrastructure.di.IODispatcher
import secret.news.club.infrastructure.rss.getRssServicesByCountry

/**
 * Orchestrates the discovery pipeline. Mirrors `discover_country` in
 * scripts/validate_rss.py, hardened with the Stage A learnings:
 *
 *  • Search engines tried by priority order. DuckDuckGo first, Bing fallback.
 *  • Native-language keywords drive the search phase; English never queried
 *    in the primary loop (Stage A: English DDG queries returned 0 hits).
 *  • Per-host concurrency limit so we don't hammer any one server.
 *  • Feed-content fingerprint dedup, not URL-only — yallakora.com aliased
 *    the same feed under 9 paths in Stage A.
 *  • Auto-seeds domains from the static RssData.kt entries for the country,
 *    so the curated list and the discovery seed list never drift apart.
 */
@Singleton
class RssDiscoveryEngineImpl @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val linkExtractor: RssLinkExtractor,
    private val feedProber: FeedProber,
    private val searchClients: Set<@JvmSuppressWildcards SearchEngineClient>,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
) : RssDiscoveryEngine {

    /** Aliased feeds (yallakora) → single-domain bursts hammering us. Per-host gate. */
    private val perHostGate = HostSemaphoreMap(maxPerHost = 2)

    /** Per Stage A: DDG soft-rate-limits — sleep briefly between queries. */
    private val searchQueryDelayMs = 1_200L

    override fun discover(
        countryCode: String,
        categories: List<RssCategory>,
    ): Flow<DiscoveredFeed> = channelFlow {
        val cc = countryCode.uppercase()
        val cfg = CountryDiscoveryRegistry[cc]
        if (cfg == null) {
            Log.w("RssDiscovery", "No CountryDiscoveryConfig for $cc — discovery skipped")
            return@channelFlow
        }

        // (1) Build the seed domain set: extra domains in config + every host
        //     already in RssData.kt for this country. Auto-keeps the two in sync.
        val staticDomains = getRssServicesByCountry(cc, preferNativeLanguage = false)
            .mapNotNull { it.url.toHost() }
            .toSet()
        val seedDomains: Set<String> = (cfg.extraDomains.map { it.removePrefix("www.") } + staticDomains)
            .toSortedSet()

        // Dedup ledgers shared across all branches of the pipeline.
        val seenUrls = mutableSetOf<String>()
        val seenFingerprints = mutableSetOf<String>()

        suspend fun emitIfNew(candidate: DiscoveredFeed) {
            synchronized(seenUrls) {
                if (!seenUrls.add(candidate.url)) return
            }
            candidate.contentFingerprint?.let {
                synchronized(seenFingerprints) {
                    if (!seenFingerprints.add(it)) return
                }
            }
            send(candidate)
        }

        coroutineScope {
            // (2) Domain probe — for each seed, try COMMON_RSS_PATHS in parallel.
            seedDomains.map { domain ->
                async(ioDispatcher) {
                    probeDomain(domain, cc, cfg.language, RssCategory.NEWS)
                        .forEach { emitIfNew(it) }
                }
            }.awaitAll()

            // (3) Homepage HTML scan — find <link rel="alternate"> on each seed.
            seedDomains.map { domain ->
                async(ioDispatcher) {
                    scrapeHomepage(domain, cc, cfg.language, RssCategory.NEWS)
                        .forEach { emitIfNew(it) }
                }
            }.awaitAll()

            // (4) Native-language search — per category, per keyword.
            //     This is where new domains beyond RssData.kt come from.
            for ((category, keywords) in cfg.nativeKeywords) {
                for (keyword in keywords) {
                    val query = "$keyword ${cfg.genericTerms.first()}"  // append "RSS"
                    val pageUrls = searchAcrossEngines(query, cfg.language)
                    delay(searchQueryDelayMs)
                    if (pageUrls.isEmpty()) continue

                    pageUrls.take(MAX_SEARCH_RESULT_PAGES).map { pageUrl ->
                        async(ioDispatcher) {
                            walkSearchResultPage(pageUrl, cc, cfg.language, category)
                                .forEach { emitIfNew(it) }
                        }
                    }.awaitAll()
                }
            }
        }
    }

    // ── Pipeline steps ────────────────────────────────────────────────────────

    private suspend fun probeDomain(
        domain: String, country: String, language: String, category: RssCategory,
    ): List<DiscoveredFeed> = coroutineScope {
        val base = "https://$domain"
        COMMON_RSS_PATHS.map { path ->
            async(ioDispatcher) {
                val url = "$base$path"
                perHostGate.withHost(domain) {
                    val probe = feedProber.probe(url) ?: return@withHost null
                    DiscoveredFeed(
                        name = probe.title ?: domain,
                        url = url,
                        category = category,
                        language = language,
                        countryCode = country,
                        source = DiscoverySource.DOMAIN_PROBE,
                        contentFingerprint = probe.contentFingerprint,
                    )
                }
            }
        }.awaitAll().filterNotNull()
    }

    private suspend fun scrapeHomepage(
        domain: String, country: String, language: String, category: RssCategory,
    ): List<DiscoveredFeed> {
        val base = "https://$domain"
        val html = fetchHtml(base) ?: return emptyList()
        val candidates = linkExtractor.extract(html, base).take(MAX_LINKS_PER_PAGE)
        return validateCandidates(candidates, country, language, category, DiscoverySource.HOMEPAGE_HTML, fallbackName = domain)
    }

    private suspend fun searchAcrossEngines(query: String, language: String): List<String> {
        val sorted = searchClients.sortedBy { it.priority }
        for (client in sorted) {
            val results = client.search(query, language, max = MAX_SEARCH_RESULTS_PER_QUERY)
            if (results.isNotEmpty()) {
                Log.d("RssDiscovery", "[${client.name}] '$query' → ${results.size} results")
                return results
            }
        }
        return emptyList()
    }

    private suspend fun walkSearchResultPage(
        pageUrl: String, country: String, language: String, category: RssCategory,
    ): List<DiscoveredFeed> {
        val html = fetchHtml(pageUrl) ?: return emptyList()
        val candidates = linkExtractor.extract(html, pageUrl).take(MAX_LINKS_PER_PAGE)
        val name = pageUrl.toHost() ?: pageUrl
        return validateCandidates(candidates, country, language, category, DiscoverySource.SEARCH_RESULT, fallbackName = name)
    }

    private suspend fun validateCandidates(
        urls: List<String>, country: String, language: String, category: RssCategory,
        source: DiscoverySource, fallbackName: String,
    ): List<DiscoveredFeed> = coroutineScope {
        urls.map { url ->
            async(ioDispatcher) {
                val host = url.toHost() ?: return@async null
                perHostGate.withHost(host) {
                    val probe = feedProber.probe(url) ?: return@withHost null
                    DiscoveredFeed(
                        name = probe.title?.takeIf { it.isNotBlank() } ?: fallbackName,
                        url = url,
                        category = category,
                        language = language,
                        countryCode = country,
                        source = source,
                        contentFingerprint = probe.contentFingerprint,
                    )
                }
            }
        }.awaitAll().filterNotNull()
    }

    // ── Plumbing ──────────────────────────────────────────────────────────────

    private suspend fun fetchHtml(url: String): String? = try {
        val response = okHttpClient.newCall(
            Request.Builder().url(url).header("Accept", "text/html,*/*").build()
        ).executeAsync()
        if (response.isSuccessful) response.body.string() else { response.close(); null }
    } catch (t: Throwable) {
        Log.d("RssDiscovery", "fetchHtml failed for $url: ${t.message}")
        null
    }

    private fun String.toHost(): String? = runCatching {
        val host = java.net.URI(this).host ?: return null
        if (host.startsWith("www.")) host.substring(4) else host
    }.getOrNull()

    companion object {
        private val COMMON_RSS_PATHS = listOf(
            "/rss", "/feed", "/rss.xml", "/feed.xml", "/atom.xml",
            "/feeds/all.atom.xml", "/rss/news", "/feeds/news",
            "/index.rss", "/news.rss",
        )
        private const val MAX_LINKS_PER_PAGE = 6
        private const val MAX_SEARCH_RESULTS_PER_QUERY = 8
        private const val MAX_SEARCH_RESULT_PAGES = 5
    }
}

/** Per-host semaphore pool — at most [maxPerHost] concurrent ops per host. */
private class HostSemaphoreMap(private val maxPerHost: Int) {
    private val gates = mutableMapOf<String, Semaphore>()
    private fun gate(host: String) = synchronized(gates) {
        gates.getOrPut(host) { Semaphore(maxPerHost) }
    }
    suspend inline fun <T> withHost(host: String, block: () -> T): T =
        gate(host).withPermit { block() }
}