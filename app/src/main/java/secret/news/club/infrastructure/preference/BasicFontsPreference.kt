package secret.news.club.infrastructure.preference

import android.content.Context
import androidx.compose.material3.Typography
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.text.font.FontFamily
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import secret.news.club.R
import secret.news.club.ui.ext.DataStoreKey
import secret.news.club.ui.ext.DataStoreKey.Companion.basicFonts
import secret.news.club.ui.ext.ExternalFonts
import secret.news.club.ui.ext.dataStore
import secret.news.club.ui.ext.put
import secret.news.club.ui.ext.restart
import secret.news.club.ui.theme.SystemTypography

val LocalBasicFonts = compositionLocalOf<BasicFontsPreference> { BasicFontsPreference.default }

sealed class BasicFontsPreference(val value: Int) : Preference() {
    object System : BasicFontsPreference(0)
    object External : BasicFontsPreference(5)

    override fun put(context: Context, scope: CoroutineScope) {
        scope.launch {
            context.dataStore.put(DataStoreKey.basicFonts, value)
            if (this@BasicFontsPreference == External) {
                context.restart()
            }
        }
    }

    fun toDesc(context: Context): String =
        when (this) {
            System -> context.getString(R.string.system_default)
            External -> context.getString(R.string.external_fonts)
        }

    fun asFontFamily(context: Context): FontFamily =
        when (this) {
            System -> FontFamily.Default
            External -> ExternalFonts.loadBasicTypography(context).displayLarge.fontFamily ?: FontFamily.Default
        }

    fun asTypography(context: Context): Typography =
        when (this) {
            System -> SystemTypography
            External -> ExternalFonts.loadBasicTypography(context)
        }

    companion object {

        val default = System
        val values = listOf(System, External)

        fun fromPreferences(preferences: Preferences): BasicFontsPreference =
            when (preferences[DataStoreKey.keys[basicFonts]?.key as Preferences.Key<Int>]) {
                0 -> System
                5 -> External
                else -> default
            }
    }
}
