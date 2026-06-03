package secret.news.club.domain.service.discovery

import secret.news.club.domain.model.rss.RssCategory

/**
 * A feed candidate produced by [RssDiscoveryEngine]. Distinct from the user's
 * subscribed [secret.news.club.domain.model.feed.Feed] — these live in their
 * own table and feed into the Add-Subscription picker, not the article list.
 */
data class DiscoveredFeed(
    val name: String,
    val url: String,
    val category: RssCategory,
    val language: String,
    val countryCode: String,
    val source: DiscoverySource,
    /** Content fingerprint for deduplication across aliased URLs that serve the
     *  same feed (e.g. `/rss`, `/feed`, `/rss.xml` returning identical content).
     *  Built from feed title + first-entry-link hash by [FeedProber]. */
    val contentFingerprint: String? = null,
)

enum class DiscoverySource {
    /** Hit on COMMON_RSS_PATHS against a curated/static domain. */
    DOMAIN_PROBE,
    /** Found via `<link rel="alternate" type="rss|atom">` on a homepage HTML page. */
    HOMEPAGE_HTML,
    /** Result of a search engine query, after walking the result page's HTML
     *  to extract its `<link>` tags. */
    SEARCH_RESULT,
}