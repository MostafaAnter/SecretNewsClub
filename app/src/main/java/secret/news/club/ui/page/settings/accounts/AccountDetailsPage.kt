package secret.news.club.ui.page.settings.accounts

import android.content.Context
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.DeleteSweep
import androidx.compose.material.icons.outlined.PersonOff
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import java.util.Date
import secret.news.club.R
import secret.news.club.infrastructure.preference.KeepArchivedPreference
import secret.news.club.infrastructure.preference.SyncBlockListPreference
import secret.news.club.infrastructure.preference.SyncIntervalPreference
import secret.news.club.infrastructure.preference.not
import secret.news.club.ui.component.base.DisplayText
import secret.news.club.ui.component.base.FeedbackIconButton
import secret.news.club.ui.component.base.RYDialog
import secret.news.club.ui.component.base.RYScaffold
import secret.news.club.ui.component.base.RYSwitch
import secret.news.club.ui.component.base.RadioDialog
import secret.news.club.ui.component.base.RadioDialogOption
import secret.news.club.ui.component.base.Subtitle
import secret.news.club.ui.component.base.TextFieldDialog
import secret.news.club.ui.component.base.Tips
import secret.news.club.ui.ext.DateFormat
import secret.news.club.ui.ext.MimeType
import secret.news.club.ui.ext.collectAsStateValue
import secret.news.club.ui.ext.getCurrentVersion
import secret.news.club.ui.ext.showToast
import secret.news.club.ui.ext.showToastLong
import secret.news.club.ui.ext.toString
import secret.news.club.ui.page.common.RouteName
import secret.news.club.ui.page.settings.SettingItem
import secret.news.club.ui.page.settings.accounts.connection.AccountConnection
import secret.news.club.ui.theme.palette.onLight

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AccountDetailsPage(
    navController: NavHostController = rememberNavController(),
    viewModel: AccountViewModel = hiltViewModel(),
) {
    val uiState = viewModel.accountUiState.collectAsStateValue()
    val context = LocalContext.current

    val selectedAccount = uiState.selectedAccount.collectAsStateValue(initial = null)

    var nameValue by remember { mutableStateOf(selectedAccount?.name) }
    var nameDialogVisible by remember { mutableStateOf(false) }
    var blockListValue by remember {
        mutableStateOf(
            SyncBlockListPreference.toString(
                selectedAccount?.syncBlockList ?: SyncBlockListPreference.default
            )
        )
    }
    var blockListDialogVisible by remember { mutableStateOf(false) }
    var syncIntervalDialogVisible by remember { mutableStateOf(false) }
    var keepArchivedDialogVisible by remember { mutableStateOf(false) }
    var exportOPMLModeDialogVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        navController.currentBackStackEntryFlow.collect {
            it.arguments?.getString("accountId")?.let { viewModel.initData(it.toInt()) }
        }
    }

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument(MimeType.ANY)) {
            result ->
            viewModel.exportAsOPML(selectedAccount!!.id!!) { string ->
                result?.let { uri ->
                    context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                        outputStream.write(string.toByteArray())
                    }
                }
            }
        }

    RYScaffold(
        containerColor =
            MaterialTheme.colorScheme.surface onLight MaterialTheme.colorScheme.inverseOnSurface,
        navigationIcon = {
            FeedbackIconButton(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = stringResource(R.string.back),
                tint = MaterialTheme.colorScheme.onSurface,
            ) {
                navController.popBackStack()
            }
        },
        actions = {
            FeedbackIconButton(
                imageVector = Icons.Rounded.Close,
                contentDescription = stringResource(R.string.close),
                tint = MaterialTheme.colorScheme.onSurface,
            ) {
                navController.navigate(RouteName.FEEDS) { launchSingleTop = true }
            }
        },
        content = {
            LazyColumn {
                item {
                    DisplayText(text = selectedAccount?.type?.toDesc(context) ?: "", desc = "")
                    Spacer(modifier = Modifier.height(16.dp))
                }
                item {
                    Subtitle(
                        modifier = Modifier.padding(horizontal = 24.dp),
                        text = stringResource(R.string.display),
                    )
                    SettingItem(
                        title = stringResource(R.string.name),
                        desc = selectedAccount?.name ?: "",
                        onClick = {
                            nameValue = selectedAccount?.name
                            nameDialogVisible = true
                        },
                    ) {}
                    Spacer(modifier = Modifier.height(24.dp))
                }
                if (selectedAccount != null) {
                    item { AccountConnection(account = selectedAccount) }
                }
                item {
                    Subtitle(
                        modifier = Modifier.padding(horizontal = 24.dp),
                        text = stringResource(R.string.synchronous),
                    )
                    SettingItem(
                        title = stringResource(R.string.sync_interval),
                        desc = selectedAccount?.syncInterval?.toDesc(context),
                        onClick = { syncIntervalDialogVisible = true },
                    ) {}
                    SettingItem(
                        title = stringResource(R.string.sync_once_on_start),
                        onClick = {
                            selectedAccount?.id?.let {
                                (!selectedAccount.syncOnStart).put(it, viewModel)
                            }
                        },
                    ) {
                        RYSwitch(activated = selectedAccount?.syncOnStart?.value == true) {
                            selectedAccount?.id?.let {
                                (!selectedAccount.syncOnStart).put(it, viewModel)
                            }
                        }
                    }
                    SettingItem(
                        title = stringResource(R.string.only_on_wifi),
                        onClick = {
                            selectedAccount?.id?.let {
                                (!selectedAccount.syncOnlyOnWiFi).put(it, viewModel)
                            }
                        },
                    ) {
                        RYSwitch(activated = selectedAccount?.syncOnlyOnWiFi?.value == true) {
                            selectedAccount?.id?.let {
                                (!selectedAccount.syncOnlyOnWiFi).put(it, viewModel)
                            }
                        }
                    }
                    SettingItem(
                        title = stringResource(R.string.only_when_charging),
                        onClick = {
                            selectedAccount?.id?.let {
                                (!selectedAccount.syncOnlyWhenCharging).put(it, viewModel)
                            }
                        },
                    ) {
                        RYSwitch(activated = selectedAccount?.syncOnlyWhenCharging?.value == true) {
                            selectedAccount?.id?.let {
                                (!selectedAccount.syncOnlyWhenCharging).put(it, viewModel)
                            }
                        }
                    }
                    SettingItem(
                        title = stringResource(R.string.keep_archived_articles),
                        desc = selectedAccount?.keepArchived?.toDesc(context),
                        onClick = { keepArchivedDialogVisible = true },
                    ) {}
                    // SettingItem(
                    //     title = stringResource(R.string.block_list),
                    //     onClick = { blockListDialogVisible = true },
                    // ) {}
                    Tips(
                        text =
                            stringResource(R.string.synchronous_tips) +
                                "\n\n" +
                                stringResource(R.string.keep_archived_tips)
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }
                item {
                    Subtitle(
                        modifier = Modifier.padding(horizontal = 24.dp),
                        text = stringResource(R.string.advanced),
                    )
                    SettingItem(
                        title = stringResource(R.string.export_as_opml),
                        onClick = { exportOPMLModeDialogVisible = true },
                    ) {}
                    SettingItem(
                        title = stringResource(R.string.clear_all_articles),
                        onClick = { viewModel.showClearDialog() },
                    ) {}
                    SettingItem(
                        title = stringResource(R.string.delete_account),
                        onClick = { viewModel.showDeleteDialog() },
                    ) {}
                    Spacer(modifier = Modifier.height(24.dp))
                }
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    Spacer(
                        modifier = Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars)
                    )
                }
            }
        },
    )

    if (nameDialogVisible) {
        val textFieldState = rememberTextFieldState(nameValue ?: "")
        TextFieldDialog(
            title = stringResource(R.string.name),
            textFieldState = textFieldState,
            onDismissRequest = { nameDialogVisible = false },
            onConfirm = {
                if (textFieldState.text.isNotBlank() == true) {
                    selectedAccount?.id?.let {
                        viewModel.update(it) { copy(name = textFieldState.text.toString()) }
                    }
                    nameDialogVisible = false
                }
            },
        )
    }

    RadioDialog(
        visible = syncIntervalDialogVisible,
        title = stringResource(R.string.sync_interval),
        options =
            SyncIntervalPreference.values.map {
                RadioDialogOption(
                    text = it.toDesc(context),
                    selected = it == selectedAccount?.syncInterval,
                ) {
                    selectedAccount?.id?.let { accountId -> it.put(accountId, viewModel) }
                }
            },
    ) {
        syncIntervalDialogVisible = false
    }

    RadioDialog(
        visible = keepArchivedDialogVisible,
        title = stringResource(R.string.keep_archived_articles),
        options =
            KeepArchivedPreference.values.map {
                RadioDialogOption(
                    text = it.toDesc(context),
                    selected = it == selectedAccount?.keepArchived,
                ) {
                    selectedAccount?.id?.let { accountId -> it.put(accountId, viewModel) }
                }
            },
    ) {
        keepArchivedDialogVisible = false
    }

    TextFieldDialog(
        visible = blockListDialogVisible,
        title = stringResource(R.string.block_list),
        value = blockListValue,
        singleLine = false,
        onValueChange = { blockListValue = it },
        onDismissRequest = { blockListDialogVisible = false },
        onConfirm = {
            selectedAccount?.id?.let {
                SyncBlockListPreference.put(it, viewModel, selectedAccount.syncBlockList)
                blockListDialogVisible = false
                context.showToast(selectedAccount.syncBlockList.toString())
            }
        },
    )

    RYDialog(
        visible = uiState.clearDialogVisible,
        onDismissRequest = { viewModel.hideClearDialog() },
        icon = {
            Icon(
                imageVector = Icons.Outlined.DeleteSweep,
                contentDescription = stringResource(R.string.clear_all_articles),
            )
        },
        title = { Text(text = stringResource(R.string.clear_all_articles)) },
        text = { Text(text = stringResource(R.string.clear_all_articles_tips)) },
        confirmButton = {
            TextButton(
                onClick = {
                    selectedAccount?.let {
                        viewModel.clear(it) {
                            viewModel.hideClearDialog()
                            context.showToastLong(
                                context.getString(R.string.clear_all_articles_toast)
                            )
                        }
                    }
                }
            ) {
                Text(text = stringResource(R.string.clear))
            }
        },
        dismissButton = {
            TextButton(onClick = { viewModel.hideClearDialog() }) {
                Text(text = stringResource(R.string.cancel))
            }
        },
    )

    RYDialog(
        visible = uiState.deleteDialogVisible,
        onDismissRequest = { viewModel.hideDeleteDialog() },
        icon = {
            Icon(
                imageVector = Icons.Outlined.PersonOff,
                contentDescription = stringResource(R.string.delete_account),
            )
        },
        title = { Text(text = stringResource(R.string.delete_account)) },
        text = { Text(text = stringResource(R.string.delete_account_tips)) },
        confirmButton = {
            TextButton(
                onClick = {
                    selectedAccount?.id?.let {
                        viewModel.delete(it) {
                            navController.popBackStack()
                            context.showToastLong(context.getString(R.string.delete_account_toast))
                        }
                    }
                }
            ) {
                Text(text = stringResource(R.string.delete))
            }
        },
        dismissButton = {
            TextButton(onClick = { viewModel.hideDeleteDialog() }) {
                Text(text = stringResource(R.string.cancel))
            }
        },
    )

    RYDialog(
        visible = exportOPMLModeDialogVisible,
        title = {
            Text(
                text = stringResource(R.string.export_as_opml),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleLarge,
            )
        },
        text = {
            LazyColumn {
                item {
                    Text(text = stringResource(R.string.additional_info_desc))
                    Spacer(modifier = Modifier.height(16.dp))
                }
                items(
                    listOf(
                        RadioDialogOption(
                            text = context.getString(R.string.include_additional_info),
                            selected = uiState.exportOPMLMode == ExportOPMLMode.ATTACH_INFO,
                        ) {
                            viewModel.changeExportOPMLMode(ExportOPMLMode.ATTACH_INFO)
                        },
                        RadioDialogOption(
                            text = context.getString(R.string.exclude),
                            selected = uiState.exportOPMLMode == ExportOPMLMode.NO_ATTACH,
                        ) {
                            viewModel.changeExportOPMLMode(ExportOPMLMode.NO_ATTACH)
                        },
                    )
                ) { option ->
                    Row(
                        modifier =
                            Modifier.fillMaxWidth().clip(CircleShape).clickable {
                                option.onClick()
                            },
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RadioButton(selected = option.selected, onClick = { option.onClick() })
                        Text(
                            modifier = Modifier.padding(start = 6.dp),
                            text = option.text,
                            style =
                                MaterialTheme.typography.bodyLarge
                                    .copy(baselineShift = BaselineShift.None)
                                    .merge(other = option.style),
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    exportOPMLModeDialogVisible = false
                    subscriptionOPMLFileLauncher(context, launcher)
                }
            ) {
                Text(stringResource(R.string.export))
            }
        },
        dismissButton = {
            TextButton(onClick = { exportOPMLModeDialogVisible = false }) {
                Text(stringResource(R.string.cancel))
            }
        },
        onDismissRequest = { exportOPMLModeDialogVisible = false },
    )
}

private fun subscriptionOPMLFileLauncher(
    context: Context,
    launcher: ManagedActivityResultLauncher<String, Uri?>,
) {
    launcher.launch(
        "News-Club-" +
            "${context.getCurrentVersion()}-subscription-" +
            "${Date().toString(DateFormat.YYYY_MM_DD_DASH_HH_MM_SS_DASH)}.opml"
    )
}
