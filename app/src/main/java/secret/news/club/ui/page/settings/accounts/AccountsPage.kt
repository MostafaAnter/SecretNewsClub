package secret.news.club.ui.page.settings.accounts

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import secret.news.club.ui.component.base.*
import secret.news.club.ui.ext.collectAsStateValue
import secret.news.club.ui.page.common.RouteName
import secret.news.club.ui.page.settings.SettingItem
import secret.news.club.ui.theme.palette.onLight
import secret.news.club.R

@Composable
fun AccountsPage(
    navController: NavHostController = rememberNavController(),
    viewModel: AccountViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val uiState = viewModel.accountUiState.collectAsStateValue()
    val accounts = viewModel.accounts.collectAsStateValue(initial = emptyList())

    RYScaffold(
        containerColor = MaterialTheme.colorScheme.surface onLight MaterialTheme.colorScheme.inverseOnSurface,
        navigationIcon = {
            FeedbackIconButton(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = stringResource(R.string.back),
                tint = MaterialTheme.colorScheme.onSurface
            ) {
                navController.popBackStack()
            }
        },
        content = {
            LazyColumn {
                item {
                    DisplayText(text = stringResource(R.string.accounts), desc = "")
                    Spacer(modifier = Modifier.height(16.dp))
                    Subtitle(
                        modifier = Modifier.padding(horizontal = 24.dp),
                        text = stringResource(R.string.list),
                    )
                }
                accounts.forEach {
                    item {
                        SettingItem(
                            title = it.name,
                            desc = it.type.toDesc(context),
                            icon = it.type.toIcon().takeIf { it is ImageVector }?.let { it as ImageVector },
                            iconPainter = it.type.toIcon().takeIf { it is Painter }?.let { it as Painter },
                            onClick = {
                                navController.navigate("${RouteName.ACCOUNT_DETAILS}/${it.id}") {
                                    launchSingleTop = true
                                }
                            },
                        ) {}
                    }
                }
                item {
                    Tips(text = stringResource(R.string.accounts_tips))
                    Spacer(modifier = Modifier.height(24.dp))
                }
                item {
                    Subtitle(
                        modifier = Modifier.padding(horizontal = 24.dp),
                        text = stringResource(R.string.more),
                    )
                    SettingItem(
                        title = stringResource(R.string.add_accounts),
                        desc = stringResource(R.string.add_accounts_desc),
                        icon = Icons.Outlined.PersonAdd,
                        onClick = {
                            navController.navigate(RouteName.ADD_ACCOUNTS) {
                                launchSingleTop = true
                            }
                        },
                    ) {}
                }
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
                }
            }
        }
    )
}

@Preview
@Composable
fun AccountsPreview() {
    AccountsPage()
}
