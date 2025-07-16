package secret.news.club.infrastructure.preference

import android.content.Context
import androidx.compose.runtime.compositionLocalOf
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import secret.news.club.ui.ext.DataStoreKey
import secret.news.club.ui.ext.DataStoreKey.Companion.themeIndex
import secret.news.club.ui.ext.dataStore
import secret.news.club.ui.ext.put

val LocalThemeIndex =
    compositionLocalOf { ThemeIndexPreference.default }

object ThemeIndexPreference {

    const val default = 5

    fun put(context: Context, scope: CoroutineScope, value: Int) {
        scope.launch(Dispatchers.IO) {
            context.dataStore.put(DataStoreKey.themeIndex, value)
        }
    }

    fun fromPreferences(preferences: Preferences) =
        preferences[DataStoreKey.keys[themeIndex]?.key as Preferences.Key<Int>] ?: default
}
