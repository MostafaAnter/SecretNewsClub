package secret.news.club.domain.model.rss

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Background-discovered RSS feed. Distinct from the user's subscribed
 * [secret.news.club.domain.model.feed.Feed] — these are *candidates* surfaced
 * by [secret.news.club.domain.service.discovery.RssDiscoveryEngine] for the
 * Add-Subscription picker, not part of any account's feed graph.
 *
 * Why a separate table:
 *  - Discovery doesn't belong to any account; one table serves all accounts.
 *  - Bulk-replacing on country refresh is safe without touching `feed` rows.
 *  - `feed` schema stays untouched → no migration risk on the article path.
 *
 * Primary key is the URL (already deduped by URL upstream). Fingerprint column
 * is indexed so the dedup query that drops aliased entries is cheap.
 */
@Entity(
    tableName = "discovered_feed",
    indices = [
        Index(value = ["countryCode"]),
        Index(value = ["contentFingerprint"]),
    ],
)
data class DiscoveredFeedEntity(
    @PrimaryKey
    val url: String,
    @ColumnInfo val name: String,
    @ColumnInfo val category: String,
    @ColumnInfo val language: String,
    @ColumnInfo val countryCode: String,
    /** [secret.news.club.domain.service.discovery.DiscoverySource] name. */
    @ColumnInfo val source: String,
    @ColumnInfo val contentFingerprint: String? = null,
    @ColumnInfo val discoveredAt: Long = System.currentTimeMillis(),
    @ColumnInfo val lastValidatedAt: Long = System.currentTimeMillis(),
)