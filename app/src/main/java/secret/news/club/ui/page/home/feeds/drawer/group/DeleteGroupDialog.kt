package secret.news.club.ui.page.home.feeds.drawer.group

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeleteForever
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import secret.news.club.R
import secret.news.club.ui.component.base.RYDialog
import secret.news.club.ui.ext.collectAsStateValue
import secret.news.club.ui.ext.showToast

@Composable
fun DeleteGroupDialog(
    groupName: String,
    groupOptionViewModel: GroupOptionViewModel = hiltViewModel(),
    onConfirm: () -> Unit,
) {
    val context = LocalContext.current
    val groupOptionUiState = groupOptionViewModel.groupOptionUiState.collectAsStateValue()
    val scope = rememberCoroutineScope()
    val toastString = stringResource(R.string.delete_toast, groupName)

    RYDialog(
        visible = groupOptionUiState.deleteDialogVisible,
        onDismissRequest = {
            groupOptionViewModel.hideDeleteDialog()
        },
        icon = {
            Icon(
                imageVector = Icons.Outlined.DeleteForever,
                contentDescription = stringResource(R.string.delete_group),
            )
        },
        title = {
            Text(text = stringResource(R.string.delete_group))
        },
        text = {
            Text(text = stringResource(R.string.delete_group_tips, groupName))
        },
        confirmButton = {
            TextButton(
                onClick = {
                    groupOptionViewModel.delete {
                        groupOptionViewModel.hideDeleteDialog()
                        onConfirm()
                        context.showToast(toastString)
                    }
                }
            ) {
                Text(
                    text = stringResource(R.string.delete),
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    groupOptionViewModel.hideDeleteDialog()
                }
            ) {
                Text(
                    text = stringResource(R.string.cancel),
                )
            }
        },
    )
}