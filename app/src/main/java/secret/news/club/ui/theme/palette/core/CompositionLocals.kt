/**
 * Copyright (C) 2021 Kyant0
 *
 * @link https://github.com/Kyant0/MusicYou
 * @author Kyant0
 */

package secret.news.club.ui.theme.palette.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import secret.news.club.ui.theme.palette.colorspace.cielab.CieLab
import secret.news.club.ui.theme.palette.colorspace.ciexyz.CieXyz
import secret.news.club.ui.theme.palette.colorspace.rgb.RgbColorSpace
import secret.news.club.ui.theme.palette.colorspace.zcam.Zcam
import secret.news.club.ui.theme.palette.data.Illuminant

val LocalWhitePoint = staticCompositionLocalOf {
    Illuminant.D65
}

val LocalLuminance = staticCompositionLocalOf {
    1.0
}

val LocalRgbColorSpace = staticCompositionLocalOf {
    RgbColorSpace.Srgb
}

val LocalZcamViewingConditions = staticCompositionLocalOf {
    createZcamViewingConditions()
}

@Composable
fun ProvideZcamViewingConditions(
    whitePoint: CieXyz = Illuminant.D65,
    luminance: Double = 203.0, // BT.2408-4, HDR white luminance
    surroundFactor: Double = 0.69, // average surround
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalWhitePoint provides whitePoint,
        LocalLuminance provides luminance,
        LocalZcamViewingConditions provides createZcamViewingConditions(
            whitePoint = whitePoint,
            luminance = luminance,
            surroundFactor = surroundFactor,
        )
    ) {
        content()
    }
}

fun createZcamViewingConditions(
    whitePoint: CieXyz = Illuminant.D65,
    luminance: Double = 203.0,
    surroundFactor: Double = 0.69,
): Zcam.Companion.ViewingConditions = Zcam.Companion.ViewingConditions(
    whitePoint = whitePoint,
    luminance = luminance,
    F_s = surroundFactor,
    L_a = 0.4 * luminance,
    Y_b = CieLab(50.0, 0.0, 0.0).toXyz(whitePoint, luminance).luminance,
)
