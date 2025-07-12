package secret.news.club.infrastructure.preference

import android.content.Context
import androidx.compose.runtime.compositionLocalOf
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import secret.news.club.ui.ext.DataStoreKey
import secret.news.club.ui.ext.DataStoreKey.Companion.newVersionDownloadUrl
import secret.news.club.ui.ext.dataStore
import secret.news.club.ui.ext.put

val LocalNewVersionDownloadUrl = compositionLocalOf { NewVersionDownloadUrlPreference.default }

object NewVersionDownloadUrlPreference {

    const val default = ""

    fun put(context: Context, scope: CoroutineScope, value: String) {
        scope.launch(Dispatchers.IO) {
            context.dataStore.put(DataStoreKey.newVersionDownloadUrl, value)
        }
    }

    fun fromPreferences(preferences: Preferences) =
        preferences[DataStoreKey.keys[newVersionDownloadUrl]?.key as Preferences.Key<String>] ?: default
}
