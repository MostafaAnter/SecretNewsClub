package secret.news.club.ui.page.home.feeds

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.UnfoldLess
import androidx.compose.material.icons.rounded.UnfoldMore
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastAny
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.eventFlow
import androidx.navigation.NavHostController
import androidx.work.WorkInfo
import kotlinx.coroutines.launch
import secret.news.club.R
import secret.news.club.domain.data.FilterState
import secret.news.club.infrastructure.preference.LocalFeedsFilterBarPadding
import secret.news.club.infrastructure.preference.LocalFeedsFilterBarStyle
import secret.news.club.infrastructure.preference.LocalFeedsFilterBarTonalElevation
import secret.news.club.infrastructure.preference.LocalFeedsGroupListExpand
import secret.news.club.infrastructure.preference.LocalFeedsGroupListTonalElevation
import secret.news.club.infrastructure.preference.LocalFeedsTopBarTonalElevation
import secret.news.club.infrastructure.preference.LocalNewVersionNumber
import secret.news.club.infrastructure.preference.LocalSkipVersionNumber
import secret.news.club.ui.component.FilterBar
import secret.news.club.ui.component.base.AdBanner
import secret.news.club.ui.component.base.DisplayText
import secret.news.club.ui.component.base.FeedbackIconButton
import secret.news.club.ui.component.base.RYScaffold
import secret.news.club.ui.ext.collectAsStateValue
import secret.news.club.ui.ext.currentAccountId
import secret.news.club.ui.ext.findActivity
import secret.news.club.ui.ext.getCurrentVersion
import secret.news.club.ui.ext.surfaceColorAtElevation
import secret.news.club.ui.page.common.RouteName
import secret.news.club.ui.page.home.feeds.accounts.AccountsTab
import secret.news.club.ui.page.home.feeds.drawer.feed.FeedOptionDrawer
import secret.news.club.ui.page.home.feeds.drawer.group.GroupOptionDrawer
import secret.news.club.ui.page.home.feeds.subscribe.SubscribeDialog
import secret.news.club.ui.page.home.feeds.subscribe.SubscribeViewModel
import secret.news.club.ui.page.settings.accounts.AccountViewModel
import kotlin.collections.set

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class
)
@Composable
fun FeedsPage(
    navController: NavHostController,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    accountViewModel: AccountViewModel = hiltViewModel(),
    feedsViewModel: FeedsViewModel = hiltViewModel(),
    subscribeViewModel: SubscribeViewModel = hiltViewModel(),
) {
    var accountTabVisible by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val hapticFeedback = LocalHapticFeedback.current
    val topBarTonalElevation = LocalFeedsTopBarTonalElevation.current
    val groupListTonalElevation = LocalFeedsGroupListTonalElevation.current
    val groupListExpand = LocalFeedsGroupListExpand.current
    val filterBarStyle = LocalFeedsFilterBarStyle.current
    val filterBarPadding = LocalFeedsFilterBarPadding.current
    val filterBarTonalElevation = LocalFeedsFilterBarTonalElevation.current

    val accounts = accountViewModel.accounts.collectAsStateValue(initial = emptyList())

    val feedsUiState = feedsViewModel.feedsUiState.collectAsStateValue()
    val filterState = feedsViewModel.filterStateFlow.collectAsStateValue()
    val importantSum = feedsUiState.importantSum
    val groupWithFeedList = feedsViewModel.groupWithFeedsListFlow.collectAsStateValue()
    val groupsVisible: SnapshotStateMap<String, Boolean> = feedsUiState.groupsVisible
    val hasGroupVisible by remember(groupWithFeedList) { derivedStateOf { groupWithFeedList.fastAny { groupsVisible[it.group.id] == true } } }

    val newVersion = LocalNewVersionNumber.current
    val skipVersion = LocalSkipVersionNumber.current
    val currentVersion = remember { context.getCurrentVersion() }
    val listState =
        if (groupWithFeedList.isNotEmpty()) feedsUiState.listState else rememberLazyListState()

    val owner = LocalLifecycleOwner.current

    var isSyncing by remember { mutableStateOf(false) }
    val syncingState = rememberPullToRefreshState()
    val syncingScope = rememberCoroutineScope()
    val doSync: () -> Unit = {
        isSyncing = true
        syncingScope.launch {
            feedsViewModel.sync()
        }
    }

    DisposableEffect(owner) {
        scope.launch {
            owner.lifecycle.eventFlow.collect {
                when (it) {
                    Lifecycle.Event.ON_RESUME, Lifecycle.Event.ON_PAUSE -> {
                        feedsViewModel.commitDiffs()
                    }

                    else -> {/* no-op */
                    }
                }
            }
        }
        feedsViewModel.syncWorkLiveData.observe(owner) { workInfoList ->
            workInfoList.let {
                isSyncing = it.any { workInfo -> workInfo.state == WorkInfo.State.RUNNING }
            }
        }
        onDispose { feedsViewModel.syncWorkLiveData.removeObservers(owner) }
    }

    fun expandAllGroups() {
        groupWithFeedList.forEach { groupWithFeed ->
            groupsVisible[groupWithFeed.group.id] = true
        }
    }

    fun collapseAllGroups() {
        groupWithFeedList.forEach { groupWithFeed ->
            groupsVisible[groupWithFeed.group.id] = false
        }
    }

    val groupDrawerState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val feedDrawerState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    BackHandler(true) {
        context.findActivity()?.moveTaskToBack(false)
    }

    RYScaffold(
        topBarTonalElevation = topBarTonalElevation.value.dp,
//        containerTonalElevation = groupListTonalElevation.value.dp,
        topBar = {
            TopAppBar(
                modifier = Modifier.clickable(
                    onClick = {
                        scope.launch {
                            if (listState.firstVisibleItemIndex != 0) {
                                listState.animateScrollToItem(0)
                            }
                        }
                    },
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }),
                title = {},
                navigationIcon = {
                    FeedbackIconButton(
                        modifier = Modifier.size(20.dp),
                        imageVector = Icons.Outlined.Settings,
                        contentDescription = stringResource(R.string.settings),
                        tint = MaterialTheme.colorScheme.onSurface,
                    ) {
                        navController.navigate(RouteName.SETTINGS) {
                            launchSingleTop = true
                        }
                    }
                },
                actions = {
                    if (subscribeViewModel.rssService.get().addSubscription) {
                        FeedbackIconButton(
                            imageVector = Icons.Rounded.Add,
                            contentDescription = stringResource(R.string.subscribe),
                            tint = MaterialTheme.colorScheme.onSurface,
                        ) {
                            subscribeViewModel.showDrawer()
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(
                        topBarTonalElevation.value.dp
                    ),
                )
            )
        }, content = {
            PullToRefreshBox(
                state = syncingState, isRefreshing = isSyncing, onRefresh = doSync
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(), state = listState
                ) {
                    item {
                        DisplayText(
                            text = feedsUiState.account?.name ?: "",
                            desc = "",
                        ) {
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.ContextClick)
                            accountTabVisible = true
                        }
                    }
                    item {
                        FeedsBanner(
                            filter = filterState.filter,
                            desc = importantSum.ifEmpty { stringResource(R.string.loading) },
                        ) {
                            filterChange(
                                navController = navController,
                                feedsViewModel = feedsViewModel,
                                filterState = filterState.copy(
                                    group = null,
                                    feed = null,
                                )
                            )
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 26.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = stringResource(R.string.feeds),
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.labelLarge,
                            )
                            IconButton(
                                onClick = {
                                    if (hasGroupVisible) collapseAllGroups() else expandAllGroups()
                                }, modifier = Modifier
                                    .padding(end = 8.dp)
                                    .size(28.dp)
                            ) {
                                Icon(
                                    imageVector = if (hasGroupVisible) Icons.Rounded.UnfoldLess else Icons.Rounded.UnfoldMore,
                                    contentDescription = stringResource(R.string.unfold_less),
                                    tint = MaterialTheme.colorScheme.primary,
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    itemsIndexed(groupWithFeedList) { _, (group, feeds) ->

                        GroupWithFeedsContainer {
                            GroupItem(isExpanded = {
                                groupsVisible.getOrPut(
                                    group.id, groupListExpand::value
                                )
                            }, group = group, onExpanded = {
                                groupsVisible[group.id] = groupsVisible.getOrPut(
                                    group.id, groupListExpand::value
                                ).not()
                            }, onLongClick = {
                                scope.launch {
                                    groupDrawerState.show()
                                }
                            }) {
                                filterChange(
                                    navController = navController,
                                    feedsViewModel = feedsViewModel,
                                    filterState = filterState.copy(
                                        group = group,
                                        feed = null,
                                    )
                                )
                            }

                            feeds.forEachIndexed { index, feed ->

                                if ((index == 5 || index == 10) && groupsVisible.getOrPut(group.id, groupListExpand::value)) {
                                    AdBanner()
                                }

                                FeedItem(
                                    feed = feed,
                                    isLastItem = { index == feeds.lastIndex },
                                    isExpanded = {
                                        groupsVisible.getOrPut(
                                            feed.groupId, groupListExpand::value
                                        )
                                    },
                                    onClick = {
                                        filterChange(
                                            navController = navController,
                                            feedsViewModel = feedsViewModel,
                                            filterState = filterState.copy(
                                                group = null,
                                                feed = feed,
                                            )
                                        )
                                    },
                                    onLongClick = {
                                        scope.launch {
                                            feedDrawerState.show()
                                        }
                                    })
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(128.dp))
                        Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
                    }
                }
            }
        }, bottomBar = {
            FilterBar(
                modifier = with(sharedTransitionScope) {
                    Modifier.sharedElement(
                        sharedContentState = rememberSharedContentState(
                            "filterBar"
                        ), animatedVisibilityScope = animatedVisibilityScope
                    )
                },
                filter = filterState.filter,
                filterBarStyle = filterBarStyle.value,
                filterBarFilled = true,
                filterBarPadding = filterBarPadding.dp,
                filterBarTonalElevation = filterBarTonalElevation.value.dp,
            ) {
                filterChange(
                    navController = navController,
                    feedsViewModel = feedsViewModel,
                    filterState = filterState.copy(filter = it),
                    isNavigate = false,
                )
            }
        })

    SubscribeDialog(subscribeViewModel = subscribeViewModel)

    GroupOptionDrawer(drawerState = groupDrawerState)
    FeedOptionDrawer(drawerState = feedDrawerState)

    val currentAccountId = feedsUiState.account?.id

    AccountsTab(
        visible = accountTabVisible,
        accounts = accounts,
        currentAccountId = currentAccountId,
        onAccountSwitch = {
            accountViewModel.switchAccount(it) {
                accountTabVisible = false
            }
        },
        onClickSettings = {
            accountTabVisible = false
            navController.navigate("${RouteName.ACCOUNT_DETAILS}/${currentAccountId}")
        },
        onClickManage = {
            accountTabVisible = false
            navController.navigate(RouteName.ACCOUNTS)
        },
        onDismissRequest = {
            accountTabVisible = false
        },
    )
}

private fun filterChange(
    navController: NavHostController,
    feedsViewModel: FeedsViewModel,
    filterState: FilterState,
    isNavigate: Boolean = true,
) {
    feedsViewModel.changeFilter(filterState)
    if (isNavigate) {
        navController.navigate(RouteName.FLOW) {
            launchSingleTop = true
        }
    }
}
