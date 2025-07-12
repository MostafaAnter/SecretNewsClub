package secret.news.club.ui.page.home.feeds.drawer.group

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Article
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
fun AllParseFullContentDialog(
    groupName: String,
    groupOptionViewModel: GroupOptionViewModel = hiltViewModel(),
    onConfirm: () -> Unit,
) {
    val context = LocalContext.current
    val groupOptionUiState = groupOptionViewModel.groupOptionUiState.collectAsStateValue()
    val scope = rememberCoroutineScope()
    val allowToastString = stringResource(R.string.all_parse_full_content_toast, groupName)
    val denyToastString = stringResource(R.string.all_deny_parse_full_content_toast, groupName)

    RYDialog(
        visible = groupOptionUiState.allParseFullContentDialogVisible,
        onDismissRequest = {
            groupOptionViewModel.hideAllParseFullContentDialog()
        },
        icon = {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.Article,
                contentDescription = stringResource(R.string.parse_full_content),
            )
        },
        title = {
            Text(text = stringResource(R.string.parse_full_content))
        },
        text = {
            Text(text = stringResource(R.string.all_parse_full_content_tips, groupName))
        },
        confirmButton = {
            TextButton(
                onClick = {
                    groupOptionViewModel.allParseFullContent(true) {
                        groupOptionViewModel.hideAllParseFullContentDialog()
                        onConfirm()
                        context.showToast(allowToastString)
                    }
                }
            ) {
                Text(
                    text = stringResource(R.string.allow),
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    groupOptionViewModel.allParseFullContent(false) {
                        groupOptionViewModel.hideAllParseFullContentDialog()
                        onConfirm()
                        context.showToast(denyToastString)
                    }
                }
            ) {
                Text(
                    text = stringResource(R.string.deny),
                )
            }
        },
    )
}