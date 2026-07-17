package secret.news.club.domain.service

import android.content.Context
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.time.Duration
import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.flow.first
import secret.news.club.domain.data.TopFeedUseCase
import secret.news.club.infrastructure.preference.SettingsProvider
import secret.news.club.infrastructure.preference.TopFeedEngagementPacer

/**
 * Keeps the "auto-notify top feed" subscription current and syncs it even while
 * the app is killed — [TopFeedUseCase.reconcileAutoSubscribedFeed] otherwise only
 * runs from the (foreground-only) FeedsViewModel. Notifications are paced by
 * [TopFeedEngagementPacer] so a multi-day gap surfaces a few spaced-out pings
 * instead of a flood; the actual coalescing/dedup of what gets shown per sync
 * lives in [NotificationHelper][secret.news.club.infrastructure.android.NotificationHelper].
 */
@HiltWorker
class TopFeedEngagementWorker
@AssistedInject
constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val topFeedUseCase: TopFeedUseCase,
    private val rssService: RssService,
    private val pacer: TopFeedEngagementPacer,
    private val settingsProvider: SettingsProvider,
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        if (!settingsProvider.settings.autoNotifyTopFeed.value) return Result.success()
        if (!NotificationManagerCompat.from(applicationContext).areNotificationsEnabled()) {
            return Result.success()
        }

        val sortedList = topFeedUseCase.sortedGroupWithFeedsListFlow.first()
        topFeedUseCase.reconcileAutoSubscribedFeed(sortedList, enabled = true)

        if (!pacer.shouldFireNow()) return Result.success()

        val topFeed = topFeedUseCase.topFeedOf(sortedList) ?: return Result.success()
        val result = rssService.get().sync(feedId = topFeed.id, groupId = null)
        pacer.recordFired()
        return result
    }

    companion object {
        private const val WORK_NAME_PERIODIC = "TOP_FEED_ENGAGEMENT_PERIODIC"
        private const val CHECK_INTERVAL_HOURS = 4L
        private const val MORNING_ANCHOR_HOUR = 8

        /**
         * Uses KEEP, not UPDATE: this is called on every app start, and UPDATE preserves the
         * *original* lastEnqueueTime while taking the freshly-computed initialDelay from this
         * call, so `lastEnqueueTime + initialDelay` silently drifts away from the intended next
         * 8 AM anchor on every relaunch. KEEP no-ops while a schedule is already pending and only
         * inserts (with a correctly "now"-anchored delay) when none exists — first enable, or
         * after [cancel].
         */
        fun enqueuePeriodic(workManager: WorkManager) {
            workManager.enqueueUniquePeriodicWork(
                WORK_NAME_PERIODIC,
                ExistingPeriodicWorkPolicy.KEEP,
                PeriodicWorkRequestBuilder<TopFeedEngagementWorker>(
                    CHECK_INTERVAL_HOURS,
                    TimeUnit.HOURS
                )
                    .setConstraints(
                        Constraints.Builder()
                            .setRequiredNetworkType(NetworkType.CONNECTED)
                            .build()
                    )
                    .setInitialDelay(initialDelayMinutes(), TimeUnit.MINUTES)
                    .build(),
            )
        }

        fun cancel(workManager: WorkManager) {
            workManager.cancelUniqueWork(WORK_NAME_PERIODIC)
        }

        /** Minutes until the next [MORNING_ANCHOR_HOUR]:00 local time. */
        private fun initialDelayMinutes(): Long {
            val now = ZonedDateTime.now()
            var target = now.withHour(MORNING_ANCHOR_HOUR).withMinute(0).withSecond(0).withNano(0)
            if (!target.isAfter(now)) {
                target = target.plusDays(1)
            }
            return Duration.between(now, target).toMinutes()
        }
    }
}
