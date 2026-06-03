package secret.news.club.domain.service.discovery

import kotlinx.coroutines.flow.Flow
import secret.news.club.domain.model.rss.RssCategory

/**
 * Discovers RSS/Atom feed URLs for a given country across multiple categories.
 *
 * Implementations should emit feeds incrementally as they're validated so the
 * caller (worker or debug screen) can react as results come in rather than wait
 * for the entire scan to complete.
 *
 * Ports the discovery pipeline from `scripts/validate_rss.py` — see Stage A
 * findings in the project memory for what worked and what didn't.
 */
interface RssDiscoveryEngine {

    /**
     * @param countryCode ISO 3166-1 alpha-2 (e.g. "EG").
     * @param categories  Categories to search for. Native-language keywords are
     *                    sourced from [CountryDiscoveryRegistry].
     * @return Flow of validated feeds. Caller decides what to do with them
     *         (persist, render, ignore).
     */
    fun discover(
        countryCode: String,
        categories: List<RssCategory>,
    ): Flow<DiscoveredFeed>
}