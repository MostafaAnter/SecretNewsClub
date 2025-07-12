package secret.news.club.infrastructure.preference

import android.content.Context
import androidx.compose.runtime.compositionLocalOf
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import secret.news.club.ui.ext.DataStoreKey
import secret.news.club.ui.ext.DataStoreKey.Companion.flowFilterBarPadding
import secret.news.club.ui.ext.dataStore
import secret.news.club.ui.ext.put

val LocalFlowFilterBarPadding =
    compositionLocalOf { FlowFilterBarPaddingPreference.default }

object FlowFilterBarPaddingPreference {

    const val default = 60

    fun put(context: Context, scope: CoroutineScope, value: Int) {
        scope.launch {
            context.dataStore.put(DataStoreKey.flowFilterBarPadding, value)
        }
    }

    fun fromPreferences(preferences: Preferences) =
        preferences[DataStoreKey.keys[flowFilterBarPadding]?.key as Preferences.Key<Int>] ?: default
}
