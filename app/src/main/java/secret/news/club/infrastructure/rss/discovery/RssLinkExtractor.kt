package secret.news.club.infrastructure.rss.discovery

import javax.inject.Inject
import org.jsoup.Jsoup

/**
 * Pulls candidate RSS/Atom URLs out of an HTML page. Mirrors
 * `_rss_links_from_html` in scripts/validate_rss.py.
 *
 * Two extraction strategies, deduped while preserving order:
 *  1. `<link rel="alternate" type="application/rss+xml | atom | rdf">` — the
 *     reliable declaration. Almost every news site has one.
 *  2. `<a href="…">` heuristic — hrefs whose path ends in `.rss`, `.xml`,
 *     `.atom`, `/feed`, `/rss`, etc. Catches sites that link to their feed
 *     in the footer/sidebar but don't declare it in `<head>`.
 */
class RssLinkExtractor @Inject constructor() {

    /**
     * @param html     Raw HTML bytes of the page.
     * @param baseUrl  URL of the page itself, used to resolve relative hrefs.
     * @return Ordered, deduped list of absolute candidate URLs.
     */
    fun extract(html: String, baseUrl: String): List<String> {
        val doc = runCatching { Jsoup.parse(html, baseUrl) }.getOrNull() ?: return emptyList()
        val urls = LinkedHashSet<String>()

        // (1) Reliable: <link rel="alternate" type="rss|atom|rdf">
        for (link in doc.select("link[type]")) {
            val type = link.attr("type").lowercase()
            if (FEED_MIME_REGEX.containsMatchIn(type)) {
                val href = link.absUrl("href").ifBlank { link.attr("href") }
                if (href.isNotBlank()) urls += href
            }
        }

        // (2) Heuristic: <a href> whose path ends in a feed-ish suffix.
        for (a in doc.select("a[href]")) {
            val href = a.absUrl("href").ifBlank { a.attr("href") }
            if (href.isBlank() || !href.startsWith("http")) continue
            val path = href.substringBefore('?').substringBefore('#').lowercase()
            if (FEED_PATH_SUFFIXES.any { path.endsWith(it) }) {
                urls += href
            }
        }
        return urls.toList()
    }

    companion object {
        private val FEED_MIME_REGEX = Regex("rss|atom|rdf", RegexOption.IGNORE_CASE)

        private val FEED_PATH_SUFFIXES = listOf(
            ".rss", ".xml", ".atom",
            "/feed", "/rss", "/atom",
            "/feed.xml", "/rss.xml", "/atom.xml",
            "/feeds",
        )
    }
}