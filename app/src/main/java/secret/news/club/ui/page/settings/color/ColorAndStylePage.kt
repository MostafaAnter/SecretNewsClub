package secret.news.club.ui.page.settings.color

import android.content.Context
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import secret.news.club.infrastructure.preference.BasicFontsPreference
import secret.news.club.infrastructure.preference.CustomPrimaryColorPreference
import secret.news.club.infrastructure.preference.LocalBasicFonts
import secret.news.club.infrastructure.preference.LocalCustomPrimaryColor
import secret.news.club.infrastructure.preference.LocalDarkTheme
import secret.news.club.infrastructure.preference.LocalThemeIndex
import secret.news.club.infrastructure.preference.ThemeIndexPreference
import secret.news.club.infrastructure.preference.not
import secret.news.club.ui.component.base.BlockRadioButton
import secret.news.club.ui.component.base.BlockRadioGroupButtonItem
import secret.news.club.ui.component.base.DisplayText
import secret.news.club.ui.component.base.DynamicSVGImage
import secret.news.club.ui.component.base.FeedbackIconButton
import secret.news.club.ui.component.base.RYScaffold
import secret.news.club.ui.component.base.RYSwitch
import secret.news.club.ui.component.base.RadioDialog
import secret.news.club.ui.component.base.RadioDialogOption
import secret.news.club.ui.component.base.Subtitle
import secret.news.club.ui.component.base.TextFieldDialog
import secret.news.club.ui.ext.ExternalFonts
import secret.news.club.ui.ext.MimeType
import secret.news.club.ui.ext.showToast
import secret.news.club.ui.page.common.RouteName
import secret.news.club.ui.page.settings.SettingItem
import secret.news.club.ui.svg.PALETTE
import secret.news.club.ui.svg.SVGString
import secret.news.club.ui.theme.palette.TonalPalettes
import secret.news.club.ui.theme.palette.TonalPalettes.Companion.toTonalPalettes
import secret.news.club.ui.theme.palette.checkColorHex
import secret.news.club.ui.theme.palette.dynamic.extractTonalPalettesFromUserWallpaper
import secret.news.club.ui.theme.palette.onDark
import secret.news.club.ui.theme.palette.onLight
import secret.news.club.ui.theme.palette.safeHexToColor

@Composable
fun ColorAndStylePage(
    navController: NavHostController,
) {
    val context = LocalContext.current
    val darkTheme = LocalDarkTheme.current
    val darkThemeNot = !darkTheme
    val themeIndex = LocalThemeIndex.current
    val customPrimaryColor = LocalCustomPrimaryColor.current
    val fonts = LocalBasicFonts.current
    val scope = rememberCoroutineScope()

    val wallpaperTonalPalettes = extractTonalPalettesFromUserWallpaper()
    var radioButtonSelected by remember { mutableStateOf(if (themeIndex > 4) 0 else 1) }
    var fontsDialogVisible by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        uri?.let {
            ExternalFonts(context, it, ExternalFonts.FontType.BasicFont).copyToInternalStorage()
            BasicFontsPreference.External.put(context, scope)
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
                    DisplayText(text = stringResource(R.string.color_and_style), desc = "")
                }
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .aspectRatio(1.38f)
                            .clip(RoundedCornerShape(24.dp))
                            .background(
                                MaterialTheme.colorScheme.surfaceContainerLow
                            )
                            .clickable { },
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        DynamicSVGImage(
                            modifier = Modifier.padding(60.dp),
                            svgImageString = SVGString.PALETTE,
                            contentDescription = stringResource(R.string.color_and_style),
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
                item {
                    BlockRadioButton(
                        selected = radioButtonSelected,
                        onSelected = { radioButtonSelected = it },
                        itemRadioGroups = listOf(
                            BlockRadioGroupButtonItem(
                                text = stringResource(R.string.wallpaper_colors),
                                onClick = {},
                            ) {
                                Palettes(
                                    context = context,
                                    palettes = wallpaperTonalPalettes.run {
                                        if (this.size > 5) {
                                            this.subList(5, wallpaperTonalPalettes.size)
                                        } else {
                                            emptyList()
                                        }
                                    },
                                    themeIndex = themeIndex,
                                    themeIndexPrefix = 5,
                                    customPrimaryColor = customPrimaryColor,
                                )
                            },
                            BlockRadioGroupButtonItem(
                                text = stringResource(R.string.basic_colors),
                                onClick = {},
                            ) {
                                Palettes(
                                    context = context,
                                    themeIndex = themeIndex,
                                    palettes = wallpaperTonalPalettes.subList(0, 5),
                                    customPrimaryColor = customPrimaryColor,
                                )
                            },
                        ),
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }
                item {
                    Subtitle(
                        modifier = Modifier.padding(horizontal = 24.dp),
                        text = stringResource(R.string.appearance),
                    )
                    SettingItem(
                        title = stringResource(R.string.dark_theme),
                        desc = darkTheme.toDesc(context),
                        separatedActions = true,
                        onClick = {
                            navController.navigate(RouteName.DARK_THEME) {
                                launchSingleTop = true
                            }
                        },
                    ) {
                        RYSwitch(
                            activated = darkTheme.isDarkTheme()
                        ) {
                            darkThemeNot.put(context, scope)
                        }
                    }
                    SettingItem(
                        title = stringResource(R.string.basic_fonts),
                        desc = fonts.toDesc(context),
                        onClick = { fontsDialogVisible = true },
                    ) {}
                    Spacer(modifier = Modifier.height(24.dp))
                }
                item {
                    Subtitle(
                        modifier = Modifier.padding(horizontal = 24.dp),
                        text = stringResource(R.string.style)
                    )
                    SettingItem(
                        title = stringResource(R.string.feeds_page),
                        onClick = {
                            navController.navigate(RouteName.FEEDS_PAGE_STYLE) {
                                launchSingleTop = true
                            }
                        },
                    ) {}
                    SettingItem(
                        title = stringResource(R.string.flow_page),
                        onClick = {
                            navController.navigate(RouteName.FLOW_PAGE_STYLE) {
                                launchSingleTop = true
                            }
                        },
                    ) {}
                    SettingItem(
                        title = stringResource(R.string.reading_page),
                        onClick = {
                            navController.navigate(RouteName.READING_PAGE_STYLE) {
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

    RadioDialog(
        visible = fontsDialogVisible,
        title = stringResource(R.string.basic_fonts),
        options = BasicFontsPreference.values.map {
            RadioDialogOption(
                text = it.toDesc(context),
                style = TextStyle(fontFamily = it.asFontFamily(context)),
                selected = it == fonts,
            ) {
                if (it.value == BasicFontsPreference.External.value) {
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

@Composable
fun Palettes(
    context: Context,
    palettes: List<TonalPalettes>,
    themeIndex: Int = 0,
    themeIndexPrefix: Int = 0,
    customPrimaryColor: String = "",
) {
    val scope = rememberCoroutineScope()
    val tonalPalettes = customPrimaryColor.safeHexToColor().toTonalPalettes()
    var addDialogVisible by remember { mutableStateOf(false) }
    var customColorValue by remember { mutableStateOf(customPrimaryColor) }

    if (palettes.isEmpty()) {
        Row(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
                .height(80.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    MaterialTheme.colorScheme.inverseOnSurface
                            onLight MaterialTheme.colorScheme.surfaceContainer,
                )
                .clickable {},
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1)
                    stringResource(R.string.no_palettes)
                else stringResource(R.string.only_android_8_1_plus),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.inverseSurface,
            )
        }
    } else {
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            palettes.forEachIndexed { index, palette ->
                val isCustom = index == palettes.lastIndex && themeIndexPrefix == 0
                val i = themeIndex - themeIndexPrefix
                SelectableMiniPalette(
                    selected = if (i >= palettes.size) i == 0 else i == index,
                    isCustom = isCustom,
                    onClick = {
                        if (isCustom) {
                            customColorValue = customPrimaryColor
                            addDialogVisible = true
                        } else {
                            ThemeIndexPreference.put(context, scope, themeIndexPrefix + index)
                        }
                    },
                    palette = if (isCustom) tonalPalettes else palette
                )
            }
        }
    }

    TextFieldDialog(
        visible = addDialogVisible,
        title = stringResource(R.string.primary_color),
        icon = Icons.Outlined.Palette,
        value = customColorValue,
        placeholder = stringResource(R.string.primary_color_hint),
        onValueChange = {
            customColorValue = it
        },
        onDismissRequest = {
            addDialogVisible = false
        },
        onConfirm = {
            it.checkColorHex()?.let {
                CustomPrimaryColorPreference.put(context, scope, it)
                ThemeIndexPreference.put(context, scope, 4)
                addDialogVisible = false
            }
        }
    )
}

@Composable
fun SelectableMiniPalette(
    modifier: Modifier = Modifier,
    selected: Boolean,
    isCustom: Boolean = false,
    onClick: () -> Unit,
    palette: TonalPalettes,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = if (isCustom) {
            MaterialTheme.colorScheme.primaryContainer
                .copy(0.5f) onDark MaterialTheme.colorScheme.onPrimaryContainer.copy(0.3f)
        } else {
            MaterialTheme.colorScheme
                .inverseOnSurface onLight MaterialTheme.colorScheme.surfaceContainer
        },
    ) {
        Surface(
            modifier = Modifier
                .clickable { onClick() }
                .padding(16.dp)
                .size(48.dp),
            shape = CircleShape,
            color = palette primary 90,
        ) {
            Box {
                Surface(
                    modifier = Modifier
                        .size(48.dp)
                        .offset((-24).dp, 24.dp),
                    color = palette tertiary 90,
                ) {}
                Surface(
                    modifier = Modifier
                        .size(48.dp)
                        .offset(24.dp, 24.dp),
                    color = palette secondary 60,
                ) {}
                AnimatedVisibility(
                    visible = selected,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    enter = fadeIn() + expandIn(expandFrom = Alignment.Center),
                    exit = shrinkOut(shrinkTowards = Alignment.Center) + fadeOut()
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Check,
                        contentDescription = "Checked",
                        modifier = Modifier
                            .padding(8.dp)
                            .size(16.dp),
                        tint = MaterialTheme.colorScheme.surface
                    )
                }
            }
        }
    }
}
