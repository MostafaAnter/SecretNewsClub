package secret.news.club.infrastructure.preference

import android.content.Context
import androidx.compose.runtime.compositionLocalOf
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import secret.news.club.ui.ext.DataStoreKey
import secret.news.club.ui.ext.DataStoreKey.Companion.readingAutoHideToolbar
import secret.news.club.ui.ext.dataStore
import secret.news.club.ui.ext.put

val LocalReadingAutoHideToolbar =
    compositionLocalOf<ReadingAutoHideToolbarPreference> { ReadingAutoHideToolbarPreference.default }

sealed class ReadingAutoHideToolbarPreference(val value: Boolean) : Preference() {
    object ON : ReadingAutoHideToolbarPreference(true)
    object OFF : ReadingAutoHideToolbarPreference(false)

    override fun put(context: Context, scope: CoroutineScope) {
        scope.launch {
            context.dataStore.put(DataStoreKey.readingAutoHideToolbar, value)
        }
    }

    companion object {

        val default = ON
        val values = listOf(ON, OFF)

        fun fromPreferences(preferences: Preferences) =
            when (preferences[DataStoreKey.keys[readingAutoHideToolbar]?.key as Preferences.Key<Boolean>]) {
                true -> ON
                false -> OFF
                else -> default
            }
    }
}

operator fun ReadingAutoHideToolbarPreference.not(): ReadingAutoHideToolbarPreference =
    when (value) {
        true -> ReadingAutoHideToolbarPreference.OFF
        false -> ReadingAutoHideToolbarPreference.ON
    }
