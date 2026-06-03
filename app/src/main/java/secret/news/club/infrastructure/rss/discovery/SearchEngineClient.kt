package secret.news.club.infrastructure.rss.discovery

/**
 * Search engine adapter for RSS discovery.
 *
 * Implementations scrape public HTML endpoints (no API keys). Each instance
 * declares its [priority]; the engine queries them in ascending order and
 * falls back to the next when one returns empty results — the Stage A finding
 * was that Google is consistently CAPTCHAed, so DuckDuckGo (lowest priority
 * number = first tried) is the default primary.
 */
interface SearchEngineClient {

    /** Lower = tried earlier. DuckDuckGo: 0, Bing: 1, Google: 2 (last resort). */
    val priority: Int

    /** For diagnostics — appears in debug screen / logs only. */
    val name: String

    /**
     * @param query Search query (UTF-8, may contain Arabic/CJK).
     * @param language Preferred result language (BCP-47, e.g. "ar"). Honored
     *                 best-effort via Accept-Language and engine-specific params.
     * @param max Maximum number of result URLs to return. Implementations
     *            should cap their own output at this even if more are available.
     * @return Absolute URLs of search-result pages, deduped, in result order.
     *         Empty list = no results / blocked / network error (engine decides
     *         not to expose the distinction to the caller).
     */
    suspend fun search(query: String, language: String, max: Int = 8): List<String>
}