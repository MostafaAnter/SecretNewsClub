package secret.news.club.domain.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import secret.news.club.domain.model.rss.DiscoveredFeedEntity

@Dao
interface DiscoveredFeedDao {

    /** Subscribe-picker live read for the current country. */
    @Query("SELECT * FROM discovered_feed WHERE countryCode = :countryCode ORDER BY discoveredAt DESC")
    fun flowByCountry(countryCode: String): Flow<List<DiscoveredFeedEntity>>

    @Query("SELECT * FROM discovered_feed WHERE countryCode = :countryCode")
    suspend fun queryByCountry(countryCode: String): List<DiscoveredFeedEntity>

    /** Insert-or-ignore by URL primary key. Worker can re-run safely. */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(feeds: List<DiscoveredFeedEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(feed: DiscoveredFeedEntity)

    /**
     * Drop entries that share a content fingerprint with a row already in
     * `discovered_feed` OR with any URL in the user-curated `feed` table.
     * Closes both holes from Stage A: yallakora aliases, and discovered
     * URLs that are just synonyms for something the user already trusts.
     */
    @Query(
        "DELETE FROM discovered_feed " +
            "WHERE countryCode = :countryCode " +
            "AND contentFingerprint IS NOT NULL " +
            "AND url NOT IN (" +
            "  SELECT MIN(url) FROM discovered_feed " +
            "  WHERE countryCode = :countryCode AND contentFingerprint IS NOT NULL " +
            "  GROUP BY contentFingerprint" +
            ")"
    )
    suspend fun dedupAliasedFor(countryCode: String)

    /** Clear all entries for a country. Used by the worker before a full refresh. */
    @Query("DELETE FROM discovered_feed WHERE countryCode = :countryCode")
    suspend fun clearCountry(countryCode: String)

    /** Garbage collection of entries that haven't been re-validated in a long while. */
    @Query("DELETE FROM discovered_feed WHERE lastValidatedAt < :olderThanMillis")
    suspend fun deleteStaleOlderThan(olderThanMillis: Long)
}