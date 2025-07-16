package secret.news.club.infrastructure.preference

import android.content.Context
import androidx.compose.runtime.compositionLocalOf
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import secret.news.club.domain.model.constant.ElevationTokens
import secret.news.club.ui.ext.DataStoreKey
import secret.news.club.ui.ext.DataStoreKey.Companion.flowTopBarTonalElevation
import secret.news.club.ui.ext.dataStore
import secret.news.club.ui.ext.put

val LocalFlowTopBarTonalElevation =
    compositionLocalOf<FlowTopBarTonalElevationPreference> { FlowTopBarTonalElevationPreference.default }

sealed class FlowTopBarTonalElevationPreference(val value: Int) : Preference() {
    object None : FlowTopBarTonalElevationPreference(ElevationTokens.Level0)
    object Elevated : FlowTopBarTonalElevationPreference(ElevationTokens.Level2)

    override fun put(context: Context, scope: CoroutineScope) {
        scope.launch {
            context.dataStore.put(flowTopBarTonalElevation, value)
        }
    }

    fun toDesc(context: Context): String =
        when (this) {
            None -> "Level 0 (${ElevationTokens.Level0}dp)"
            Elevated -> "Level 2 (${ElevationTokens.Level2}dp)"
        }

    companion object {

        val default = Elevated
        val values = listOf(None, Elevated)

        fun fromPreferences(preferences: Preferences) =
            when (preferences[DataStoreKey.keys[flowTopBarTonalElevation]?.key as Preferences.Key<Int>]) {
                ElevationTokens.Level0 -> None
                ElevationTokens.Level2 -> Elevated
                else -> default
            }
    }
}
