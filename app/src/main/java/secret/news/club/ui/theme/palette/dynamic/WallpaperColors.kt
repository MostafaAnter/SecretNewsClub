/**
 * Copyright (C) 2021 Kyant0
 *
 * @link https://github.com/Kyant0/MusicYou
 * @author Kyant0
 * @modifier Ashinch
 */

package secret.news.club.ui.theme.palette.dynamic

import android.app.WallpaperManager
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import secret.news.club.infrastructure.preference.LocalCustomPrimaryColor
import secret.news.club.ui.theme.palette.TonalPalettes
import secret.news.club.ui.theme.palette.TonalPalettes.Companion.getSystemTonalPalettes
import secret.news.club.ui.theme.palette.TonalPalettes.Companion.toTonalPalettes
import secret.news.club.ui.theme.palette.safeHexToColor

object PresetColor {

    val blue = Color(0xFF80BBFF)
    val pink = Color(0xFFFFD8E4)
    val purple = Color(0xFF62539f)
    val yellow = Color(0xFFE9B666)
}

@Composable
@Stable
fun extractTonalPalettesFromUserWallpaper(): List<TonalPalettes> {
    val context = LocalContext.current
    val customPrimaryColor = LocalCustomPrimaryColor.current

    val preset = mutableListOf(
        PresetColor.blue.toTonalPalettes(),
        PresetColor.pink.toTonalPalettes(),
        PresetColor.purple.toTonalPalettes(),
        PresetColor.yellow.toTonalPalettes(),
        customPrimaryColor.safeHexToColor().toTonalPalettes()
    )

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1 && !LocalView.current.isInEditMode) {
        val colors = WallpaperManager.getInstance(LocalContext.current)
            .getWallpaperColors(WallpaperManager.FLAG_SYSTEM)
        val primary = colors?.primaryColor?.toArgb()
        val secondary = colors?.secondaryColor?.toArgb()
        val tertiary = colors?.tertiaryColor?.toArgb()
        if (primary != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                preset.add(context.getSystemTonalPalettes())
            } else {
                preset.add(Color(primary).toTonalPalettes())
            }
        }
        if (secondary != null) preset.add(Color(secondary).toTonalPalettes())
        if (tertiary != null) preset.add(Color(tertiary).toTonalPalettes())
    }
    return preset
}
