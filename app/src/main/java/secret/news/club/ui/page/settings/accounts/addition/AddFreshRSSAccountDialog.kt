package secret.news.club.ui.page.settings.accounts.addition

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import secret.news.club.R
import secret.news.club.domain.model.account.Account
import secret.news.club.domain.model.account.AccountType
import secret.news.club.domain.model.account.security.FreshRSSSecurityKey
import secret.news.club.ui.component.base.RYDialog
import secret.news.club.ui.component.base.RYOutlineTextField
import secret.news.club.ui.ext.collectAsStateValue
import secret.news.club.ui.ext.showToast
import secret.news.club.ui.page.common.RouteName
import secret.news.club.ui.page.settings.accounts.AccountViewModel

@OptIn(androidx.compose.ui.ExperimentalComposeUiApi::class)
@Composable
fun AddFreshRSSAccountDialog(
    navController: NavHostController,
    viewModel: AdditionViewModel = hiltViewModel(),
    accountViewModel: AccountViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val uiState = viewModel.additionUiState.collectAsStateValue()
    val accountUiState = accountViewModel.accountUiState.collectAsStateValue()
    val scope = rememberCoroutineScope()

    var freshRSSServerUrl by rememberSaveable { mutableStateOf("") }
    var freshRSSUsername by rememberSaveable { mutableStateOf("") }
    var freshRSSPassword by rememberSaveable { mutableStateOf("") }
    var freshRSSClientCertificateAlias by rememberSaveable { mutableStateOf("") }

    RYDialog(
        modifier = Modifier.padding(horizontal = 44.dp),
        visible = uiState.addFreshRSSAccountDialogVisible,
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = {
            focusManager.clearFocus()
            accountViewModel.cancelAdd()
            viewModel.hideAddFreshRSSAccountDialog()
        },
        icon = {
            if (accountUiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(24.dp),
                    color = MaterialTheme.colorScheme.onSurface,
                )
            } else {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(id = R.drawable.ic_freshrss),
                    contentDescription = stringResource(R.string.fresh_rss),
                )
            }
        },
        title = {
            Text(
                text = stringResource(R.string.fresh_rss),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(10.dp))
                RYOutlineTextField(
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = accountUiState.isLoading,
                    value = freshRSSServerUrl,
                    onValueChange = { freshRSSServerUrl = it },
                    label = stringResource(R.string.server_url),
                    placeholder = "https://demo.freshrss.org/api/greader.php",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
                )
                Spacer(modifier = Modifier.height(10.dp))
                RYOutlineTextField(
                    modifier = Modifier.fillMaxWidth(),
                    requestFocus = false,
                    readOnly = accountUiState.isLoading,
                    value = freshRSSUsername,
                    onValueChange = { freshRSSUsername = it },
                    label = stringResource(R.string.username),
                    
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                )
                Spacer(modifier = Modifier.height(10.dp))
                RYOutlineTextField(
                    modifier = Modifier.fillMaxWidth(),
                    requestFocus = false,
                    readOnly = accountUiState.isLoading,
                    value = freshRSSPassword,
                    onValueChange = { freshRSSPassword = it },
                    isPassword = true,
                    label = stringResource(R.string.password),
                    
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                )
                Spacer(modifier = Modifier.height(10.dp))
                CertificateSelector(
                    value = freshRSSClientCertificateAlias,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    freshRSSClientCertificateAlias = it
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        },
        confirmButton = {
            TextButton(
                enabled = !accountUiState.isLoading
                        && freshRSSServerUrl.isNotBlank()
                        && freshRSSUsername.isNotEmpty()
                        && freshRSSPassword.isNotEmpty(),
                onClick = {
                    focusManager.clearFocus()
                    if (!freshRSSServerUrl.endsWith("/")) {
                        freshRSSServerUrl += "/"
                    }
                    accountViewModel.addAccount(
                        Account(
                            type = AccountType.FreshRSS,
                            name = context.getString(R.string.fresh_rss),
                            securityKey = FreshRSSSecurityKey(
                                serverUrl = freshRSSServerUrl,
                                username = freshRSSUsername,
                                password = freshRSSPassword,
                                clientCertificateAlias = freshRSSClientCertificateAlias,
                            ).toString(),
                        )
                    ) { account, exception ->
                        if (account == null) {
                            context.showToast(exception?.message ?: "Not valid credentials")
                        } else {
                            viewModel.hideAddFreshRSSAccountDialog()
                            navController.popBackStack()
                            navController.navigate("${RouteName.ACCOUNT_DETAILS}/${account.id}") {
                                launchSingleTop = true
                            }
                        }
                    }
                }
            ) {
                Text(stringResource(R.string.add))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    accountViewModel.cancelAdd()
                    focusManager.clearFocus()
                    viewModel.hideAddFreshRSSAccountDialog()
                }
            ) {
                Text(stringResource(R.string.cancel))
            }
        },
    )
}
