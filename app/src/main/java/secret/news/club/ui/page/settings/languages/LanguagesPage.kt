package secret.news.club.ui.page.settings.languages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import secret.news.club.infrastructure.preference.LanguagesPreference
import secret.news.club.infrastructure.preference.LocalLanguages
import secret.news.club.infrastructure.preference.OpenLinkPreference
import secret.news.club.ui.component.base.Banner
import secret.news.club.ui.component.base.DisplayText
import secret.news.club.ui.component.base.FeedbackIconButton
import secret.news.club.ui.component.base.RYScaffold
import secret.news.club.ui.ext.openURL
import secret.news.club.ui.page.settings.SettingItem
import secret.news.club.ui.theme.palette.onLight
import java.util.Locale
import secret.news.club.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguagesPage(
    navController: NavHostController,
) {
    val context = LocalContext.current
    val currentLocale = Locale.getDefault()

    val languages = LocalLanguages.current.run {
        if (toLocale() == currentLocale) this
        else LanguagesPreference.default
    }

    val scope = rememberCoroutineScope()

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
                item(key = languages.value) {
                    DisplayText(text = stringResource(R.string.languages), desc = "")
                    Spacer(modifier = Modifier.height(16.dp))
                    Banner(
                        title = stringResource(R.string.help_translate),
                        icon = Icons.Outlined.Lightbulb,
                        action = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                                contentDescription = stringResource(R.string.go_to),
                            )
                        },
                    ) {
                        context.openURL(
                            context.getString(R.string.translatable_url),
                            OpenLinkPreference.AutoPreferCustomTabs
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
                item {
                    LanguagesPreference.values.map {
                        SettingItem(
                            title = it.toDesc(),
                            onClick = {
                                it.put(context, scope)
                            },
                        ) {
                            RadioButton(selected = it == languages, onClick = {
                                it.put(context, scope)
                            })
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
