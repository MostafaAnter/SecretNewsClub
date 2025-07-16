package secret.news.club.ui.page.home

import androidx.lifecycle.ViewModel
import androidx.work.WorkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import secret.news.club.domain.data.ArticlePagingListUseCase
import secret.news.club.domain.service.RssService
import secret.news.club.domain.service.SyncWorker
import secret.news.club.domain.data.DiffMapHolder
import secret.news.club.domain.data.FilterState
import secret.news.club.domain.data.FilterStateUseCase
import secret.news.club.domain.data.PagerData
import secret.news.club.infrastructure.di.ApplicationScope
import secret.news.club.infrastructure.di.IODispatcher
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val rssService: RssService,
    @ApplicationScope
    private val applicationScope: CoroutineScope,
    private val workManager: WorkManager,
    @IODispatcher
    private val ioDispatcher: CoroutineDispatcher,
    private val pagingListUseCase: ArticlePagingListUseCase,
    private val filterStateUseCase: FilterStateUseCase,
    val diffMapHolder: DiffMapHolder,
) : ViewModel() {

    val filterStateFlow = filterStateUseCase.filterStateFlow
    val pagerFlow: StateFlow<PagerData> = pagingListUseCase.pagerFlow

    val syncWorkLiveData = workManager.getWorkInfosByTagLiveData(SyncWorker.WORK_TAG)

    fun sync() {
        applicationScope.launch(ioDispatcher) {
            rssService.get().doSyncOneTime()
        }
    }

    fun changeFilter(filterState: FilterState) {
        filterStateUseCase.updateFilterState(
            filterState.feed,
            filterState.group,
            filterState.filter
        )
    }

    fun inputSearchContent(content: String? = null) {
        filterStateUseCase.updateFilterState(searchContent = content)
    }

    fun commitDiffs() = diffMapHolder.commitDiffsToDb()
}