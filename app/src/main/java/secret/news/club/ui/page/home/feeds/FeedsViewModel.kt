package secret.news.club.ui.page.home.feeds

import android.content.Context
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import secret.news.club.R
import secret.news.club.domain.model.account.Account
import secret.news.club.domain.model.general.Filter
import secret.news.club.domain.model.group.GroupWithFeed
import secret.news.club.domain.service.AccountService
import secret.news.club.domain.service.RssService
import secret.news.club.infrastructure.android.AndroidStringsHelper
import secret.news.club.domain.data.DiffMapHolder
import secret.news.club.domain.data.FilterState
import secret.news.club.domain.data.FilterStateUseCase
import secret.news.club.domain.data.GroupWithFeedsListUseCase
import secret.news.club.domain.service.SyncWorker
import secret.news.club.infrastructure.di.ApplicationScope
import secret.news.club.infrastructure.di.DefaultDispatcher
import secret.news.club.infrastructure.di.IODispatcher
import secret.news.club.infrastructure.preference.SettingsProvider
import secret.news.club.infrastructure.rss.NativeLanguageKeywords
import secret.news.club.infrastructure.rss.hasNativeScript
import secret.news.club.ui.ext.DataStoreKey
import secret.news.club.ui.ext.dataStore
import secret.news.club.ui.ext.put
import javax.inject.Inject

private const val TAG = "FeedsViewModel"

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class FeedsViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val accountService: AccountService,
    private val rssService: RssService,
    private val workManager: WorkManager,
    private val androidStringsHelper: AndroidStringsHelper,
    @DefaultDispatcher
    private val defaultDispatcher: CoroutineDispatcher,
    @IODispatcher
    private val ioDispatcher: CoroutineDispatcher,
    @ApplicationScope
    private val applicationScope: CoroutineScope,
    private val settingsProvider: SettingsProvider,
    private val diffMapHolder: DiffMapHolder,
    private val filterStateUseCase: FilterStateUseCase,
    private val groupWithFeedsListUseCase: GroupWithFeedsListUseCase,
) : ViewModel() {

    private val _feedsUiState =
        MutableStateFlow(FeedsUiState())
    val feedsUiState: StateFlow<FeedsUiState> = _feedsUiState.asStateFlow()

    val syncWorkLiveData = workManager.getWorkInfosByTagLiveData(SyncWorker.WORK_TAG)

    val filterStateFlow = filterStateUseCase.filterStateFlow
    val groupWithFeedsListFlow = groupWithFeedsListUseCase.groupWithFeedListFlow

    /**
     * Native-script-aware sort of [groupWithFeedsListFlow] keyed off the selected country.
     * This is the single source of truth for "top feed" — anything that needs the sorted
     * order (UI rendering, auto-notify reconciliation) should read this flow.
     */
    val sortedGroupWithFeedsListFlow: StateFlow<List<GroupWithFeed>> =
        combine(
            groupWithFeedsListFlow,
            settingsProvider.settingsFlow
                .mapLatest { it.country?.value.orEmpty() }
                .distinctUntilChanged()
        ) { list, countryCode ->
            sortForNativeLanguage(list, countryCode)
        }
            .flowOn(defaultDispatcher)
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    var currentJob: Job? = null

    fun sync() {
        applicationScope.launch(ioDispatcher) {
            rssService.get().doSyncOneTime()
        }
    }

    fun commitDiffs() = diffMapHolder.commitDiffsToDb()

    fun changeFilter(filterState: FilterState) {
        filterStateUseCase.updateFilterState(filterState)
    }

    init {
        val accountFlow = accountService.currentAccountFlow
        viewModelScope.launch {
            accountFlow.collect { account ->
                _feedsUiState.update { it.copy(account = account) }
            }
        }
        viewModelScope.launch {
            filterStateUseCase.filterStateFlow.mapLatest { it.filter }
                .combine(accountFlow) { filter, account ->
                    filter
                }
                .collect {
                    currentJob?.cancel()
                    currentJob = when (it) {
                        Filter.Unread -> pullUnreadFeeds()
                        Filter.Starred -> pullStarredFeeds()
                        else -> pullAllFeeds()
                    }
                }
        }

        // Auto-subscribe the top feed for notifications so users get something to
        // come back to. Reacts to country / sort changes and the user-facing setting.
        viewModelScope.launch(ioDispatcher) {
            combine(
                sortedGroupWithFeedsListFlow,
                settingsProvider.settingsFlow
                    .mapLatest { it.autoNotifyTopFeed.value }
                    .distinctUntilChanged(),
                accountFlow
            ) { sortedList, enabled, _ ->
                Triple(sortedList, enabled, Unit)
            }.collect { (sortedList, enabled, _) ->
                reconcileAutoSubscribedFeed(sortedList, enabled)
            }
        }
    }

    private suspend fun reconcileAutoSubscribedFeed(
        sortedList: List<GroupWithFeed>,
        enabled: Boolean,
    ) {
        val previousId = settingsProvider
            .get<String>(DataStoreKey.autoSubscribedFeedId)
            .orEmpty()

        val topFeed = sortedList
            .firstOrNull { it.feeds.isNotEmpty() }
            ?.feeds
            ?.firstOrNull()

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

    private fun pullAllFeeds(): Job {
        val articleCountMapFlow =
            rssService.get().pullImportant(isStarred = false, isUnread = false)

        return viewModelScope.launch {
            launch {
                articleCountMapFlow.mapLatest {
                    val sum = it.values.sum()
                    androidStringsHelper.getQuantityString(R.plurals.all_desc, sum, sum)
                }.flowOn(defaultDispatcher).collect { text ->
                    _feedsUiState.update { it.copy(importantSum = text) }
                }
            }
        }
    }

    private fun pullStarredFeeds(): Job {
        val starredCountMap = rssService.get().pullImportant(isStarred = true, isUnread = false)

        return viewModelScope.launch {
            starredCountMap.mapLatest {
                val sum = it.values.sum()
                androidStringsHelper.getQuantityString(R.plurals.starred_desc, sum, sum)
            }.flowOn(defaultDispatcher).collect { text ->
                _feedsUiState.update { it.copy(importantSum = text) }
            }
        }
    }

    @OptIn(FlowPreview::class)
    private fun pullUnreadFeeds(): Job {
        val unreadCountMapFlow = rssService.get().pullImportant(isStarred = false, isUnread = true)

        return viewModelScope.launch {
            diffMapHolder.diffMapSnapshotFlow
                .combine(
                    unreadCountMapFlow
                ) { diffMap, unreadCountMap ->
                    val sum = unreadCountMap.values.sum()
                    val combinedSum =
                        sum + diffMap.values.sumOf { if (it.isUnread) 1.toInt() else -1 } // KT-46360
                    androidStringsHelper.getQuantityString(
                        R.plurals.unread_desc,
                        combinedSum,
                        combinedSum
                    )
                }.debounce(200L).flowOn(defaultDispatcher).collect { text ->
                    _feedsUiState.update { it.copy(importantSum = text) }
                }
        }
    }

//    @OptIn(ExperimentalCoroutinesApi::class)
//    fun pullFeeds(filterState: FilterState, hideEmptyGroups: Boolean) {
//        val isStarred = filterState.filter.isStarred()
//        val isUnread = filterState.filter.isUnread()
//        _feedsUiState.update {
//            val important = rssService.get().pullImportant(isStarred, isUnread)
//            it.copy(
////                importantSum = important
////                    .mapLatest {
////                        (it["sum"] ?: 0).run {
////                            androidStringsHelper.getQuantityString(
////                                when {
////                                    isStarred -> R.plurals.starred_desc
////                                    isUnread -> R.plurals.unread_desc
////                                    else -> R.plurals.all_desc
////                                },
////                                this,
////                                this
////                            )
////                        }
////                    }.flowOn(defaultDispatcher),
//                groupWithFeedList = combine(
//                    important,
//                    rssService.get().pullFeeds()
//                ) { importantMap, groupWithFeedList ->
//                    val groupIterator = groupWithFeedList.iterator()
//                    while (groupIterator.hasNext()) {
//                        val groupWithFeed = groupIterator.next()
//                        val groupImportant = importantMap[groupWithFeed.group.id] ?: 0
//                        if (hideEmptyGroups && (isStarred || isUnread) && groupImportant == 0) {
//                            groupIterator.remove()
//                            continue
//                        }
//                        groupWithFeed.group.important = groupImportant
//                        val feedIterator = groupWithFeed.feeds.iterator()
//                        while (feedIterator.hasNext()) {
//                            val feed = feedIterator.next()
//                            val feedImportant = importantMap[feed.id] ?: 0
//                            groupWithFeed.group.feeds++
//                            if (hideEmptyGroups && (isStarred || isUnread) && feedImportant == 0) {
//                                feedIterator.remove()
//                                continue
//                            }
//                            feed.important = feedImportant
//                        }
//                    }
//                    groupWithFeedList
//                }.mapLatest { list ->
//                    list.filter { (group, feeds) ->
//                        group.id != feedsUiState.value.account?.id?.getDefaultGroupId() || feeds.isNotEmpty()
//                    }
//                }.flowOn(defaultDispatcher),
//            )
//        }
//    }
}

data class FeedsUiState(
    val account: Account? = null,
    val importantSum: String = "",
    val listState: LazyListState = LazyListState(),
    val groupsVisible: SnapshotStateMap<String, Boolean> = mutableStateMapOf(),
)
