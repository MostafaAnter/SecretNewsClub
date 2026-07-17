package secret.news.club.infrastructure.preference

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import secret.news.club.ui.ext.DataStoreKey
import secret.news.club.ui.ext.dataStore
import secret.news.club.ui.ext.get
import secret.news.club.ui.ext.put
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton

private const val MIN_GAP_MILLIS = 90 * 60 * 1000L
private const val MAX_NOTIFICATIONS_PER_DAY = 3

/**
 * Paces the "auto-notify top feed" engagement notifications to at most
 * [MAX_NOTIFICATIONS_PER_DAY], at least [MIN_GAP_MILLIS] apart, so a multi-day
 * sync gap surfaces a few spaced-out pings instead of a flood. Single source of
 * truth for this state — consumed by [secret.news.club.domain.service.TopFeedEngagementWorker].
 */
@Singleton
class TopFeedEngagementPacer @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    suspend fun shouldFireNow(now: Long = System.currentTimeMillis()): Boolean {
        rollDailyCounterIfNeeded()
        val lastFired = context.dataStore.get<Long>(DataStoreKey.topFeedLastNotifiedAt) ?: 0L
        val countToday = context.dataStore.get<Int>(DataStoreKey.topFeedDailyNotifyCount) ?: 0
        return countToday < MAX_NOTIFICATIONS_PER_DAY && (now - lastFired) >= MIN_GAP_MILLIS
    }

    suspend fun recordFired(now: Long = System.currentTimeMillis()) {
        rollDailyCounterIfNeeded()
        val countToday = context.dataStore.get<Int>(DataStoreKey.topFeedDailyNotifyCount) ?: 0
        context.dataStore.put(DataStoreKey.topFeedLastNotifiedAt, now)
        context.dataStore.put(DataStoreKey.topFeedDailyNotifyCount, countToday + 1)
    }

    private suspend fun rollDailyCounterIfNeeded() {
        val today = LocalDate.now(ZoneId.systemDefault()).toEpochDay()
        val storedDay = context.dataStore.get<Long>(DataStoreKey.topFeedDailyCounterEpochDay) ?: -1L
        if (storedDay != today) {
            context.dataStore.put(DataStoreKey.topFeedDailyCounterEpochDay, today)
            context.dataStore.put(DataStoreKey.topFeedDailyNotifyCount, 0)
        }
    }
}
