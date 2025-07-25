package secret.news.club.infrastructure.preference

import android.content.Context
import androidx.compose.runtime.compositionLocalOf
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import secret.news.club.ui.ext.DataStoreKey
import secret.news.club.ui.ext.DataStoreKey.Companion.newVersionSizeString
import secret.news.club.ui.ext.dataStore
import secret.news.club.ui.ext.put

val LocalNewVersionSize = compositionLocalOf { NewVersionSizePreference.default }

object NewVersionSizePreference {

    const val default = ""

    fun Int.formatSize(): String =
        (this / 1024f / 1024f)
            .takeIf { it > 0f }
            ?.run { " ${String.format("%.2f", this)} MB" } ?: ""

    fun put(context: Context, scope: CoroutineScope, value: String) {
        scope.launch(Dispatchers.IO) {
            context.dataStore.put(newVersionSizeString, value)
        }
    }

    fun fromPreferences(preferences: Preferences) =
        preferences[DataStoreKey.keys[newVersionSizeString]?.key as Preferences.Key<String>] ?: default
}
