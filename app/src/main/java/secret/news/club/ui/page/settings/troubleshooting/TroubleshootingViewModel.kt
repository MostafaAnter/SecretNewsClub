package secret.news.club.ui.page.settings.troubleshooting

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import secret.news.club.domain.model.rss.RssCategory
import secret.news.club.domain.service.AccountService
import secret.news.club.domain.service.OpmlService
import secret.news.club.domain.service.RssService
import secret.news.club.domain.service.discovery.DiscoveredFeed
import secret.news.club.domain.service.discovery.RssDiscoveryEngine
import secret.news.club.infrastructure.di.ApplicationScope
import secret.news.club.infrastructure.di.DefaultDispatcher
import secret.news.club.infrastructure.di.IODispatcher
import secret.news.club.infrastructure.di.MainDispatcher
import secret.news.club.ui.ext.fromDataStoreToJSONString
import secret.news.club.ui.ext.fromJSONStringToDataStore
import secret.news.club.ui.ext.isProbableProtobuf
import javax.inject.Inject

@HiltViewModel
class TroubleshootingViewModel @Inject constructor(
    private val accountService: AccountService,
    private val rssService: RssService,
    private val opmlService: OpmlService,
    private val rssDiscoveryEngine: RssDiscoveryEngine,
    @IODispatcher
    private val ioDispatcher: CoroutineDispatcher,
    @DefaultDispatcher
    private val defaultDispatcher: CoroutineDispatcher,
    @MainDispatcher
    private val mainDispatcher: CoroutineDispatcher,
    @ApplicationScope
    private val applicationScope: CoroutineScope,
) : ViewModel() {

    private var discoveryJob: Job? = null

    private val _troubleshootingUiState = MutableStateFlow(TroubleshootingUiState())
    val troubleshootingUiState: StateFlow<TroubleshootingUiState> =
        _troubleshootingUiState.asStateFlow()

    fun showWarningDialog() {
        _troubleshootingUiState.update { it.copy(warningDialogVisible = true) }
    }

    fun hideWarningDialog() {
        _troubleshootingUiState.update { it.copy(warningDialogVisible = false) }
    }

    fun tryImport(context: Context, byteArray: ByteArray) {
        if (!byteArray.isProbableProtobuf()) {
            showWarningDialog()
        } else {
            importPreferencesFromJSON(context, byteArray)
        }
    }

    fun importPreferencesFromJSON(context: Context, byteArray: ByteArray) {
        viewModelScope.launch(ioDispatcher) {
            String(byteArray).fromJSONStringToDataStore(context)
        }
    }

    fun exportPreferencesAsJSON(context: Context, callback: (ByteArray) -> Unit = {}) {
        viewModelScope.launch(ioDispatcher) {
            callback(context.fromDataStoreToJSONString().toByteArray())
        }
    }

    /**
     * Stage B-3 debug runner — invokes the discovery engine for [countryCode]
     * across all categories and streams hits into the UI state as they arrive.
     * Reentrant: starting a new run cancels the previous one and clears the list.
     */
    fun runRssDiscovery(countryCode: String) {
        discoveryJob?.cancel()
        _troubleshootingUiState.update {
            it.copy(discoveryRunning = true, discoveryCountry = countryCode, discoveredFeeds = emptyList())
        }
        discoveryJob = viewModelScope.launch(ioDispatcher) {
            rssDiscoveryEngine.discover(
                countryCode = countryCode,
                categories = RssCategory.entries.toList(),
            )
                .onEach { feed ->
                    _troubleshootingUiState.update { state ->
                        state.copy(discoveredFeeds = state.discoveredFeeds + feed)
                    }
                }
                .onCompletion {
                    _troubleshootingUiState.update { it.copy(discoveryRunning = false) }
                }
                .collect { /* state already updated in onEach */ }
        }
    }

    fun cancelRssDiscovery() {
        discoveryJob?.cancel()
        _troubleshootingUiState.update { it.copy(discoveryRunning = false) }
    }
}

data class TroubleshootingUiState(
    val isLoading: Boolean = false,
    val warningDialogVisible: Boolean = false,
    val discoveryRunning: Boolean = false,
    val discoveryCountry: String = "",
    val discoveredFeeds: List<DiscoveredFeed> = emptyList(),
)
