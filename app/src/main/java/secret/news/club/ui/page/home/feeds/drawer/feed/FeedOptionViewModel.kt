package secret.news.club.ui.page.home.feeds.drawer.feed

import androidx.compose.material.ExperimentalMaterialApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import secret.news.club.domain.model.feed.Feed
import secret.news.club.domain.model.group.Group
import secret.news.club.domain.repository.FeedDao
import secret.news.club.domain.service.RssService
import secret.news.club.infrastructure.di.ApplicationScope
import secret.news.club.infrastructure.di.IODispatcher
import secret.news.club.infrastructure.di.MainDispatcher
import secret.news.club.infrastructure.rss.RssHelper

@OptIn(ExperimentalMaterialApi::class)
@HiltViewModel
class FeedOptionViewModel
@Inject
constructor(
    val rssService: RssService,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    @ApplicationScope private val applicationScope: CoroutineScope,
    private val rssHelper: RssHelper,
    private val feedDao: FeedDao,
) : ViewModel() {

    private val _feedOptionUiState = MutableStateFlow(FeedOptionUiState())
    val feedOptionUiState: StateFlow<FeedOptionUiState> = _feedOptionUiState.asStateFlow()

    init {
        viewModelScope.launch(ioDispatcher) {
            rssService.flow().collectLatest {
                it.pullGroups().collectLatest { groups ->
                    _feedOptionUiState.update { it.copy(groups = groups) }
                }
            }
        }
    }

    suspend fun fetchFeed(feedId: String) {
        val feed = rssService.get().findFeedById(feedId)
        _feedOptionUiState.update { it.copy(feed = feed, selectedGroupId = feed?.groupId ?: "") }
    }

    fun showNewGroupDialog() {
        _feedOptionUiState.update { it.copy(newGroupDialogVisible = true, newGroupContent = "") }
    }

    fun hideNewGroupDialog() {
        _feedOptionUiState.update { it.copy(newGroupDialogVisible = false, newGroupContent = "") }
    }

    fun inputNewGroup(content: String) {
        _feedOptionUiState.update { it.copy(newGroupContent = content) }
    }

    fun addNewGroup() {
        if (_feedOptionUiState.value.newGroupContent.isNotBlank()) {
            applicationScope.launch {
                selectedGroup(
                    rssService
                        .get()
                        .addGroup(
                            destFeed = _feedOptionUiState.value.feed,
                            newGroupName = _feedOptionUiState.value.newGroupContent,
                        )
                )
                hideNewGroupDialog()
            }
        }
    }

    fun selectedGroup(groupId: String) {
        applicationScope.launch(ioDispatcher) {
            _feedOptionUiState.value.feed?.let {
                rssService
                    .get()
                    .moveFeed(originGroupId = it.groupId, feed = it.copy(groupId = groupId))
                fetchFeed(it.id)
            }
        }
    }

    fun changeParseFullContentPreset() {
        viewModelScope.launch(ioDispatcher) {
            _feedOptionUiState.value.feed?.let {
                val isFullContent = !it.isFullContent
                val isBrowser = if (isFullContent) false else it.isBrowser
                rssService
                    .get()
                    .updateFeed(it.copy(isFullContent = isFullContent, isBrowser = isBrowser))
                fetchFeed(it.id)
            }
        }
    }

    fun changeOpenInBrowserPreset() {
        viewModelScope.launch(ioDispatcher) {
            _feedOptionUiState.value.feed?.let {
                val isBrowser = !it.isBrowser
                val isFullContent = if (isBrowser) false else it.isFullContent
                rssService
                    .get()
                    .updateFeed(it.copy(isBrowser = isBrowser, isFullContent = isFullContent))
                fetchFeed(it.id)
            }
        }
    }

    fun changeAllowNotificationPreset() {
        viewModelScope.launch(ioDispatcher) {
            _feedOptionUiState.value.feed?.let {
                rssService.get().updateFeed(it.copy(isNotification = !it.isNotification))
                fetchFeed(it.id)
            }
        }
    }

    fun delete(callback: () -> Unit = {}) {
        _feedOptionUiState.value.feed?.let {
            applicationScope.launch(ioDispatcher) {
                rssService.get().deleteFeed(it)
                withContext(mainDispatcher) { callback() }
            }
        }
    }

    fun hideDeleteDialog() {
        _feedOptionUiState.update { it.copy(deleteDialogVisible = false) }
    }

    fun showDeleteDialog() {
        _feedOptionUiState.update { it.copy(deleteDialogVisible = true) }
    }

    fun showClearDialog() {
        _feedOptionUiState.update { it.copy(clearDialogVisible = true) }
    }

    fun hideClearDialog() {
        _feedOptionUiState.update { it.copy(clearDialogVisible = false) }
    }

    fun clearFeed(callback: () -> Unit = {}) {
        _feedOptionUiState.value.feed?.let {
            viewModelScope.launch(ioDispatcher) {
                rssService.get().deleteArticles(feed = it)
                withContext(mainDispatcher) { callback() }
            }
        }
    }

    fun renameFeed() {
        _feedOptionUiState.value.feed?.let {
            applicationScope.launch {
                rssService.get().renameFeed(it.copy(name = _feedOptionUiState.value.newName))
                _feedOptionUiState.update { it.copy(renameDialogVisible = false) }
            }
        }
    }

    fun showRenameDialog() {
        _feedOptionUiState.update {
            it.copy(renameDialogVisible = true, newName = _feedOptionUiState.value.feed?.name ?: "")
        }
    }

    fun hideRenameDialog() {
        _feedOptionUiState.update { it.copy(renameDialogVisible = false, newName = "") }
    }

    fun inputNewName(content: String) {
        _feedOptionUiState.update { it.copy(newName = content) }
    }

    fun showFeedUrlDialog() {
        _feedOptionUiState.update {
            it.copy(
                changeUrlDialogVisible = true,
                newUrl = _feedOptionUiState.value.feed?.url ?: "",
            )
        }
    }

    fun hideFeedUrlDialog() {
        _feedOptionUiState.update { it.copy(changeUrlDialogVisible = false, newUrl = "") }
    }

    fun inputNewUrl(content: String) {
        _feedOptionUiState.update { it.copy(newUrl = content) }
    }

    fun changeFeedUrl() {
        _feedOptionUiState.value.feed?.let {
            applicationScope.launch {
                rssService.get().changeFeedUrl(it.copy(url = _feedOptionUiState.value.newUrl))
                _feedOptionUiState.update { it.copy(changeUrlDialogVisible = false) }
            }
        }
    }

    fun reloadIcon() {
        _feedOptionUiState.value.feed?.let { feed ->
            viewModelScope.launch(ioDispatcher) {
                val icon = rssHelper.queryRssIconLink(feed.url) ?: return@launch
                feedDao.update(feed.copy(icon = icon))
                fetchFeed(feed.id)
            }
        }
    }
}

data class FeedOptionUiState(
    val feed: Feed? = null,
    val selectedGroupId: String = "",
    val newGroupContent: String = "",
    val newGroupDialogVisible: Boolean = false,
    val groups: List<Group> = emptyList(),
    val deleteDialogVisible: Boolean = false,
    val clearDialogVisible: Boolean = false,
    val newName: String = "",
    val renameDialogVisible: Boolean = false,
    val newUrl: String = "",
    val changeUrlDialogVisible: Boolean = false,
)
