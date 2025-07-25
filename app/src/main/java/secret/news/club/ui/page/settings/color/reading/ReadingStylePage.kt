package secret.news.club.ui.page.settings.color.reading

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.Segment
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.rounded.Title
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import secret.news.club.R
import secret.news.club.infrastructure.preference.LocalPullToSwitchArticle
import secret.news.club.infrastructure.preference.LocalReadingAutoHideToolbar
import secret.news.club.infrastructure.preference.LocalReadingBoldCharacters
import secret.news.club.infrastructure.preference.LocalReadingFonts
import secret.news.club.infrastructure.preference.LocalReadingPageTonalElevation
import secret.news.club.infrastructure.preference.LocalReadingRenderer
import secret.news.club.infrastructure.preference.LocalReadingTheme
import secret.news.club.infrastructure.preference.ReadingFontsPreference
import secret.news.club.infrastructure.preference.ReadingPageTonalElevationPreference
import secret.news.club.infrastructure.preference.ReadingRendererPreference
import secret.news.club.infrastructure.preference.ReadingThemePreference
import secret.news.club.infrastructure.preference.not
import secret.news.club.ui.component.ReadingThemePrev
import secret.news.club.ui.component.base.DisplayText
import secret.news.club.ui.component.base.FeedbackIconButton
import secret.news.club.ui.component.base.RYScaffold
import secret.news.club.ui.component.base.RYSwitch
import secret.news.club.ui.component.base.RadioDialog
import secret.news.club.ui.component.base.RadioDialogOption
import secret.news.club.ui.component.base.Subtitle
import secret.news.club.ui.ext.ExternalFonts
import secret.news.club.ui.ext.MimeType
import secret.news.club.ui.ext.showToast
import secret.news.club.ui.page.common.RouteName
import secret.news.club.ui.page.settings.SettingItem
import secret.news.club.ui.theme.palette.onLight

@Composable
fun ReadingStylePage(
    navController: NavHostController,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val readingTheme = LocalReadingTheme.current
    val tonalElevation = LocalReadingPageTonalElevation.current
    val fonts = LocalReadingFonts.current
    val autoHideToolbar = LocalReadingAutoHideToolbar.current
    val pullToSwitchArticle = LocalPullToSwitchArticle.current
    val renderer = LocalReadingRenderer.current
    val boldCharacters = LocalReadingBoldCharacters.current

    var tonalElevationDialogVisible by remember { mutableStateOf(false) }
    var rendererDialogVisible by remember { mutableStateOf(false) }
    var fontsDialogVisible by remember { mutableStateOf(false) }

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            uri?.let {
                ExternalFonts(
                    context,
                    it,
                    ExternalFonts.FontType.ReadingFont
                ).copyToInternalStorage()
                ReadingFontsPreference.External.put(context, scope)
            } ?: context.showToast("Cannot get activity result with launcher")
        }

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
                    DisplayText(text = stringResource(R.string.reading_page), desc = "")
                }

                // Preview
                item {
                    Row(
                        modifier = Modifier.horizontalScroll(rememberScrollState())
                    ) {
                        Spacer(modifier = Modifier.width(24.dp))
                        ReadingThemePreference.values.map {
                            if (readingTheme == ReadingThemePreference.Custom || it != ReadingThemePreference.Custom) {
                                ReadingThemePrev(selected = readingTheme, theme = it) {
                                    it.put(context, scope)
                                    it.applyTheme(context, scope)
                                }
                            } else {
                                Spacer(modifier = Modifier.width(150.dp))
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        Spacer(modifier = Modifier.width((24 - 8).dp))
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(
                                MaterialTheme.colorScheme.inverseOnSurface
                                        onLight MaterialTheme.colorScheme.surface.copy(0.7f)
                            )
                            .clickable { },
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }

                // General
                item {
                    Subtitle(
                        modifier = Modifier.padding(horizontal = 24.dp),
                        text = stringResource(R.string.general)
                    )
                    SettingItem(
                        title = stringResource(R.string.content_renderer),
                        desc = renderer.toDesc(context),
                        onClick = { rendererDialogVisible = true },
                    ) {}
                    SettingItem(
                        title = stringResource(R.string.bold_characters),
                        separatedActions = renderer == ReadingRendererPreference.WebView,
                        enabled = renderer == ReadingRendererPreference.WebView,
                        desc = if (renderer == ReadingRendererPreference.WebView) null
                        else stringResource(R.string.only_available_on_webview),
                        onClick = {
                            navController.navigate(RouteName.READING_BOLD_CHARACTERS) {
                                launchSingleTop = true
                            }
                        },
                    ) {
                        if (renderer == ReadingRendererPreference.WebView) {
                            RYSwitch(
                                enable = renderer == ReadingRendererPreference.WebView,
                                activated = boldCharacters.value,
                            ) {
                                (!boldCharacters).put(context, scope)
                            }
                        }
                    }
                    SettingItem(
                        title = stringResource(R.string.reading_fonts),
                        desc = fonts.toDesc(context),
                        onClick = { fontsDialogVisible = true },
                    ) {}
                    SettingItem(
                        title = stringResource(R.string.auto_hide_toolbars),
                        onClick = {
                            (!autoHideToolbar).put(context, scope)
                        },
                    ) {
                        RYSwitch(activated = autoHideToolbar.value) {
                            (!autoHideToolbar).put(context, scope)
                        }
                    }
                    SettingItem(
                        title = stringResource(R.string.rearrange_buttons),
                        enabled = false,
                        onClick = {},
                    ) {}

                    SettingItem(
                        title = stringResource(id = R.string.pull_to_switch_article),
                        onClick = { pullToSwitchArticle.toggle(context, scope) }) {
                        RYSwitch(activated = pullToSwitchArticle.value, onClick = {
                            pullToSwitchArticle.toggle(context, scope)
                        })
                    }
                    Subtitle(
                        modifier = Modifier.padding(horizontal = 24.dp),
                        text = stringResource(R.string.toolbars)
                    )
                    SettingItem(
                        title = stringResource(R.string.tonal_elevation),
                        desc = "${tonalElevation.value}dp",
                        onClick = {
                            tonalElevationDialogVisible = true
                        },
                    ) {}

                    Spacer(modifier = Modifier.height(24.dp))
                }

                // Advanced
                item {
                    Subtitle(
                        modifier = Modifier.padding(horizontal = 24.dp),
                        text = stringResource(R.string.advanced)
                    )
                    SettingItem(
                        title = stringResource(R.string.title),
                        desc = stringResource(R.string.title_desc),
                        icon = Icons.Rounded.Title,
                        onClick = {
                            navController.navigate(RouteName.READING_PAGE_TITLE) {
                                launchSingleTop = true
                            }
                        },
                    ) {}
                    SettingItem(
                        title = stringResource(R.string.text),
                        desc = stringResource(R.string.text_desc),
                        icon = Icons.AutoMirrored.Rounded.Segment,
                        onClick = {
                            navController.navigate(RouteName.READING_PAGE_TEXT) {
                                launchSingleTop = true
                            }
                        },
                    ) {}
                    SettingItem(
                        title = stringResource(R.string.images),
                        desc = stringResource(R.string.images_desc),
                        icon = Icons.Outlined.Image,
                        onClick = {
                            navController.navigate(RouteName.READING_PAGE_IMAGE) {
                                launchSingleTop = true
                            }
                        },
                    ) {}
                    SettingItem(
                        title = stringResource(R.string.videos),
                        desc = stringResource(R.string.videos_desc),
                        icon = Icons.Outlined.Movie,
                        enabled = false,
                        onClick = {
//                            navController.navigate(RouteName.READING_PAGE_VIDEO) {
//                                launchSingleTop = true
//                            }
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

    RadioDialog(
        visible = tonalElevationDialogVisible,
        title = stringResource(R.string.tonal_elevation),
        options = ReadingPageTonalElevationPreference.values.map {
            RadioDialogOption(
                text = it.toDesc(context),
                selected = it == tonalElevation,
            ) {
                it.put(context, scope)
            }
        }
    ) {
        tonalElevationDialogVisible = false
    }

    RadioDialog(
        visible = rendererDialogVisible,
        title = stringResource(R.string.content_renderer),
        options = ReadingRendererPreference.values.map {
            RadioDialogOption(
                text = it.toDesc(context),
                selected = it == renderer,
            ) {
                it.put(context, scope)
            }
        }
    ) {
        rendererDialogVisible = false
    }

    RadioDialog(
        visible = fontsDialogVisible,
        title = stringResource(R.string.reading_fonts),
        options = ReadingFontsPreference.values.map {
            RadioDialogOption(
                text = it.toDesc(context),
                style = TextStyle(fontFamily = it.asFontFamily(context)),
                selected = it == fonts,
            ) {
                if (it.value == ReadingFontsPreference.External.value) {
                    launcher.launch(arrayOf(MimeType.FONT))
                } else {
                    it.put(context, scope)
                }
            }
        }
    ) {
        fontsDialogVisible = false
    }
}
