package secret.news.club.domain.data

import android.content.Context
import androidx.core.app.NotificationManagerCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapLatest
import secret.news.club.domain.model.group.GroupWithFeed
import secret.news.club.domain.service.RssService
import secret.news.club.infrastructure.preference.SettingsProvider
import secret.news.club.infrastructure.rss.NativeLanguageKeywords
import secret.news.club.infrastructure.rss.hasNativeScript
import secret.news.club.ui.ext.DataStoreKey
import secret.news.club.ui.ext.dataStore
import secret.news.club.ui.ext.put
import javax.inject.Inject

/**
 * Single source of truth for "top feed": native-script-aware sort of the current
 * account's feed list, and the reconciliation that flips `isNotification` on
 * whichever feed lands at the top. Shared by [secret.news.club.ui.page.home.feeds.FeedsViewModel]
 * (runs while the app is foregrounded) and [secret.news.club.domain.service.TopFeedEngagementWorker]
 * (runs in the background, so the subscription stays current even while the app is killed).
 */
@OptIn(ExperimentalCoroutinesApi::class)
class TopFeedUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val settingsProvider: SettingsProvider,
    private val rssService: RssService,
    groupWithFeedsListUseCase: GroupWithFeedsListUseCase,
) {

    val sortedGroupWithFeedsListFlow: Flow<List<GroupWithFeed>> =
        combine(
            groupWithFeedsListUseCase.groupWithFeedListFlow,
            settingsProvider.settingsFlow
                .mapLatest { it.country?.value.orEmpty() }
                .distinctUntilChanged()
        ) { list, countryCode ->
            sortForNativeLanguage(list, countryCode)
        }

    fun topFeedOf(sortedList: List<GroupWithFeed>) =
        sortedList.firstOrNull { it.feeds.isNotEmpty() }?.feeds?.firstOrNull()

    suspend fun reconcileAutoSubscribedFeed(
        sortedList: List<GroupWithFeed>,
        enabled: Boolean,
    ) {
        val previousId = settingsProvider
            .get<String>(DataStoreKey.autoSubscribedFeedId)
            .orEmpty()

        val topFeed = topFeedOf(sortedList)

        // Feature off: undo our previous subscription if we still own it, then exit.
        if (!enabled) {
            if (previousId.isNotEmpty()) {
                unsubscribePreviousIfStillOurs(previousId)
                context.dataStore.put(DataStoreKey.autoSubscribedFeedId, "")
            }
            return
        }

        // Nothing to subscribe to yet (initial load).
        if (topFeed == null) return

        // Already subscribed — nothing to do.
        if (topFeed.id == previousId) return

        // OS-level notification permission gate: don't flip flags the user
        // can't actually receive notifications from anyway.
        if (!NotificationManagerCompat.from(context).areNotificationsEnabled()) return

        if (previousId.isNotEmpty()) {
            unsubscribePreviousIfStillOurs(previousId)
        }

        if (!topFeed.isNotification) {
            rssService.get().updateFeed(topFeed.copy(isNotification = true))
        }
        context.dataStore.put(DataStoreKey.autoSubscribedFeedId, topFeed.id)
    }

    /**
     * Only undo the auto-subscription if the feed's flag is still `true` — i.e. the
     * user hasn't manually turned it off since we set it. Avoids fighting user intent.
     */
    private suspend fun unsubscribePreviousIfStillOurs(previousFeedId: String) {
        val previousFeed = rssService.get().findFeedById(previousFeedId) ?: return
        if (previousFeed.isNotification) {
            rssService.get().updateFeed(previousFeed.copy(isNotification = false))
        }
    }

    private fun sortForNativeLanguage(
        list: List<GroupWithFeed>,
        countryCode: String,
    ): List<GroupWithFeed> {
        if (countryCode.isEmpty()) return list
        val nativeLang = NativeLanguageKeywords.languageForCountry(countryCode)
        if (nativeLang.isEmpty()) return list
        return list.map { gwf ->
            gwf.copy(
                feeds = gwf.feeds
                    .sortedByDescending { feed ->
                        feed.name.hasNativeScript(nativeLang) || feed.language == nativeLang
                    }
                    .toMutableList()
            )
        }
    }
}
