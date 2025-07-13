package secret.news.club.ui.page.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.TipsAndUpdates
import androidx.compose.material.icons.outlined.TouchApp
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import secret.news.club.R
import secret.news.club.infrastructure.preference.LocalNewVersionNumber
import secret.news.club.infrastructure.preference.LocalSkipVersionNumber
import secret.news.club.infrastructure.preference.toDisplayName
import secret.news.club.ui.component.base.Banner
import secret.news.club.ui.component.base.DisplayText
import secret.news.club.ui.component.base.FeedbackIconButton
import secret.news.club.ui.component.base.RYScaffold
import secret.news.club.ui.ext.getCurrentVersion
import secret.news.club.ui.page.common.RouteName
import secret.news.club.ui.theme.palette.onLight
import java.util.Locale

@Composable
fun SettingsPage(
    navController: NavHostController,
) {
    val context = LocalContext.current
    val newVersion = LocalNewVersionNumber.current
    val skipVersion = LocalSkipVersionNumber.current
    val currentVersion by remember { mutableStateOf(context.getCurrentVersion()) }

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
                    DisplayText(text = stringResource(R.string.settings), desc = "")
                }
                item {
                    SelectableSettingGroupItem(
                        title = stringResource(R.string.accounts),
                        desc = stringResource(R.string.accounts_desc),
                        icon = Icons.Outlined.AccountCircle,
                    ) {
                        navController.navigate(RouteName.ACCOUNTS) {
                            launchSingleTop = true
                        }
                    }
                }
                item {
                    SelectableSettingGroupItem(
                        title = stringResource(R.string.color_and_style),
                        desc = stringResource(R.string.color_and_style_desc),
                        icon = Icons.Outlined.Palette,
                    ) {
                        navController.navigate(RouteName.COLOR_AND_STYLE) {
                            launchSingleTop = true
                        }
                    }
                }
                item {
                    SelectableSettingGroupItem(
                        title = stringResource(R.string.interaction),
                        desc = stringResource(R.string.interaction_desc),
                        icon = Icons.Outlined.TouchApp,
                    ) {
                        navController.navigate(RouteName.INTERACTION) {
                            launchSingleTop = true
                        }
                    }
                }
                item {
                    SelectableSettingGroupItem(
                        title = stringResource(R.string.languages),
                        desc = Locale.getDefault().toDisplayName(),
                        icon = Icons.Outlined.Language,
                    ) {
                        navController.navigate(RouteName.LANGUAGES) {
                            launchSingleTop = true
                        }
                    }
                }
                item {
                    SelectableSettingGroupItem(
                        title = stringResource(R.string.troubleshooting),
                        desc = stringResource(R.string.troubleshooting_desc),
                        icon = Icons.Outlined.BugReport,
                    ) {
                        navController.navigate(RouteName.TROUBLESHOOTING) {
                            launchSingleTop = true
                        }
                    }
                }
                item {
                    SelectableSettingGroupItem(
                        title = stringResource(R.string.tips_and_support),
                        desc = stringResource(R.string.tips_and_support_desc),
                        icon = Icons.Outlined.TipsAndUpdates,
                    ) {
                        navController.navigate(RouteName.TIPS_AND_SUPPORT) {
                            launchSingleTop = true
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
                }
            }
        }
    )

}
