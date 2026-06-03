package secret.news.club.domain.service.discovery

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.toList
import secret.news.club.domain.model.rss.DiscoveredFeedEntity
import secret.news.club.domain.model.rss.RssCategory
import secret.news.club.domain.repository.DiscoveredFeedDao

/**
 * Background discovery for the active country. Three entry points (see companion):
 *   • [enqueueOnFirstLaunch]  — fired once from AndroidApp.onCreate, KEEP policy.
 *   • [enqueueOnCountryChange] — fired from FeedsViewModel when CountryPreference
 *     changes, REPLACE policy so a freshly-switched country cancels stale work.
 *   • [enqueuePeriodic] — weekly PeriodicWorkRequest, UNMETERED, KEEP policy.
 *
 * All three share a unique-work name keyed by country, so a periodic tick that
 * lands while a one-time run is in-flight is a no-op. Discovery results are
 * written to `discovered_feed` with INSERT OR IGNORE — multiple runs converge.
 */
@HiltWorker
class RssDiscoveryWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val engine: RssDiscoveryEngine,
    private val discoveredFeedDao: DiscoveredFeedDao,
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val countryCode = inputData.getString(KEY_COUNTRY)?.uppercase()
            ?: return Result.failure()

        Log.i(TAG, "Discovery start: $countryCode")

        return try {
            val collected = engine.discover(
                countryCode = countryCode,
                categories = RssCategory.entries.toList(),
            )
                .onEach { feed ->
                    // Stream-insert as feeds validate — the Subscribe UI updates
                    // reactively the moment each row lands, instead of waiting
                    // for the whole pipeline to finish.
                    discoveredFeedDao.insert(feed.toEntity())
                }
                .toList()

            // Final dedup pass — kills aliased entries that share a content
            // fingerprint with rows added by either THIS run or a previous one.
            discoveredFeedDao.dedupAliasedFor(countryCode)

            // GC: drop entries older than 30d that haven't been re-validated.
            val cutoff = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(30)
            discoveredFeedDao.deleteStaleOlderThan(cutoff)

            Log.i(TAG, "Discovery done: $countryCode (${collected.size} candidates emitted)")
            Result.success()
        } catch (t: Throwable) {
            Log.w(TAG, "Discovery failed for $countryCode: ${t.message}", t)
            // Network errors / soft blocks are retryable; permanent errors will
            // hit the WorkManager attempt cap and stop.
            if (runAttemptCount < 3) Result.retry() else Result.failure()
        }
    }

    private fun DiscoveredFeed.toEntity() = DiscoveredFeedEntity(
        url = url,
        name = name,
        category = category.name,
        language = language,
        countryCode = countryCode,
        source = source.name,
        contentFingerprint = contentFingerprint,
    )

    companion object {
        private const val TAG = "RssDiscoveryWorker"
        private const val KEY_COUNTRY = "countryCode"
        private const val PERIODIC_WORK_NAME = "rss-discovery-periodic"

        /** Unique-work name keyed per country so concurrent runs collapse. */
        private fun oneTimeName(countryCode: String) = "rss-discovery:${countryCode.uppercase()}"

        private val networkConstraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        private val unmeteredConstraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .build()

        /** App startup — once per install, KEEP avoids retrigger on every cold boot. */
        fun enqueueOnFirstLaunch(workManager: WorkManager, countryCode: String) {
            workManager.enqueueUniqueWork(
                oneTimeName(countryCode),
                ExistingWorkPolicy.KEEP,
                OneTimeWorkRequestBuilder<RssDiscoveryWorker>()
                    .setConstraints(networkConstraints)
                    .setInputData(workDataOf(KEY_COUNTRY to countryCode.uppercase()))
                    .build()
            )
        }

        /** User switched country — REPLACE so the old country's run is cancelled. */
        fun enqueueOnCountryChange(workManager: WorkManager, countryCode: String) {
            workManager.enqueueUniqueWork(
                oneTimeName(countryCode),
                ExistingWorkPolicy.REPLACE,
                OneTimeWorkRequestBuilder<RssDiscoveryWorker>()
                    .setConstraints(networkConstraints)
                    .setInputData(workDataOf(KEY_COUNTRY to countryCode.uppercase()))
                    .build()
            )
        }

        /** Weekly refresh on unmetered networks. KEEP so app startup is idempotent. */
        fun enqueuePeriodic(workManager: WorkManager, countryCode: String) {
            workManager.enqueueUniquePeriodicWork(
                PERIODIC_WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                PeriodicWorkRequestBuilder<RssDiscoveryWorker>(7, TimeUnit.DAYS)
                    .setConstraints(unmeteredConstraints)
                    .setInputData(workDataOf(KEY_COUNTRY to countryCode.uppercase()))
                    .build()
            )
        }

        /**
         * Re-arm the periodic worker with the new country whenever the country
         * changes (the periodic work's inputData is sticky, so we have to
         * REPLACE it, not KEEP). Call from CountryPreference observer.
         */
        fun replacePeriodicCountry(workManager: WorkManager, countryCode: String) {
            workManager.enqueueUniquePeriodicWork(
                PERIODIC_WORK_NAME,
                ExistingPeriodicWorkPolicy.UPDATE,
                PeriodicWorkRequestBuilder<RssDiscoveryWorker>(7, TimeUnit.DAYS)
                    .setConstraints(unmeteredConstraints)
                    .setInputData(workDataOf(KEY_COUNTRY to countryCode.uppercase()))
                    .build()
            )
        }
    }
}