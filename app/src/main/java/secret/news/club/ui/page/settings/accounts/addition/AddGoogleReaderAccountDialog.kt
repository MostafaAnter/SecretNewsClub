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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.RssFeed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
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
import secret.news.club.domain.model.account.security.GoogleReaderSecurityKey
import secret.news.club.ui.component.base.RYDialog
import secret.news.club.ui.component.base.RYOutlineTextField
import secret.news.club.ui.ext.collectAsStateValue
import secret.news.club.ui.ext.showToast
import secret.news.club.ui.page.common.RouteName
import secret.news.club.ui.page.settings.accounts.AccountViewModel

@OptIn(androidx.compose.ui.ExperimentalComposeUiApi::class)
@Composable
fun AddGoogleReaderAccountDialog(
    navController: NavHostController,
    viewModel: AdditionViewModel = hiltViewModel(),
    accountViewModel: AccountViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val uiState = viewModel.additionUiState.collectAsStateValue()
    val accountUiState = accountViewModel.accountUiState.collectAsStateValue()

    var googleReaderServerUrl by rememberSaveable { mutableStateOf("") }
    var googleReaderUsername by rememberSaveable { mutableStateOf("") }
    var googleReaderPassword by rememberSaveable { mutableStateOf("") }
    var googleReaderClientCertificateAlias by rememberSaveable { mutableStateOf("") }

    RYDialog(
        modifier = Modifier.padding(horizontal = 44.dp),
        visible = uiState.addGoogleReaderAccountDialogVisible,
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = {
            focusManager.clearFocus()
            accountViewModel.cancelAdd()
            viewModel.hideAddGoogleReaderAccountDialog()
        },
        icon = {
            if (accountUiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onSurface,
                )
            } else {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = Icons.Rounded.RssFeed,
                    contentDescription = stringResource(R.string.google_reader),
                )
            }
        },
        title = {
            Text(
                text = stringResource(R.string.google_reader),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Spacer(modifier = Modifier.height(10.dp))
                RYOutlineTextField(
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = accountUiState.isLoading,
                    value = googleReaderServerUrl,
                    onValueChange = { googleReaderServerUrl = it },
                    label = stringResource(R.string.server_url),
                    placeholder = "https://demo.freshrss.org/api/greader.php",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
                )
                Spacer(modifier = Modifier.height(10.dp))
                RYOutlineTextField(
                    modifier = Modifier.fillMaxWidth(),
                    requestFocus = false,
                    readOnly = accountUiState.isLoading,
                    value = googleReaderUsername,
                    onValueChange = { googleReaderUsername = it },
                    label = stringResource(R.string.username),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                )
                Spacer(modifier = Modifier.height(10.dp))
                RYOutlineTextField(
                    modifier = Modifier.fillMaxWidth(),
                    requestFocus = false,
                    readOnly = accountUiState.isLoading,
                    value = googleReaderPassword,
                    onValueChange = { googleReaderPassword = it },
                    isPassword = true,
                    label = stringResource(R.string.password),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                )
                Spacer(modifier = Modifier.height(10.dp))
                CertificateSelector(
                    value = googleReaderClientCertificateAlias,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    googleReaderClientCertificateAlias = it
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        },
        confirmButton = {
            TextButton(
                enabled =
                    !accountUiState.isLoading &&
                        googleReaderServerUrl.isNotBlank() &&
                        googleReaderUsername.isNotEmpty() &&
                        googleReaderPassword.isNotEmpty(),
                onClick = {
                    focusManager.clearFocus()
                    if (!googleReaderServerUrl.endsWith("/")) {
                        googleReaderServerUrl += "/"
                    }
                    accountViewModel.addAccount(
                        Account(
                            type = AccountType.GoogleReader,
                            name = context.getString(R.string.google_reader),
                            securityKey =
                                GoogleReaderSecurityKey(
                                        serverUrl = googleReaderServerUrl,
                                        username = googleReaderUsername,
                                        password = googleReaderPassword,
                                        clientCertificateAlias =
                                            googleReaderClientCertificateAlias.takeIf {
                                                it.isNotEmpty()
                                            },
                                    )
                                    .toString(),
                        )
                    ) { account, exception ->
                        if (account == null) {
                            context.showToast(exception?.message ?: "Not valid credentials")
                        } else {
                            viewModel.hideAddGoogleReaderAccountDialog()
                            navController.popBackStack()
                            navController.navigate("${RouteName.ACCOUNT_DETAILS}/${account.id}") {
                                launchSingleTop = true
                            }
                        }
                    }
                },
            ) {
                Text(stringResource(R.string.add))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    focusManager.clearFocus()
                    accountViewModel.cancelAdd()
                    viewModel.hideAddGoogleReaderAccountDialog()
                }
            ) {
                Text(stringResource(R.string.cancel))
            }
        },
    )
}
