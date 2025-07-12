package secret.news.club.infrastructure.preference

import android.content.Context
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.compositionLocalOf
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import secret.news.club.domain.model.general.Version
import secret.news.club.domain.model.general.toVersion
import secret.news.club.ui.ext.DataStoreKey
import secret.news.club.ui.ext.DataStoreKey.Companion.newVersionNumber
import secret.news.club.ui.ext.dataStore
import secret.news.club.ui.ext.put

val LocalNewVersionNumber = compositionLocalOf { NewVersionNumberPreference.default }

object NewVersionNumberPreference {

    val provide: (Settings) -> ProvidedValue<Version> =
        fun(settings: Settings) = LocalNewVersionNumber provides settings.newVersionNumber

    val default = Version()

    fun put(context: Context, scope: CoroutineScope, value: String) {
        scope.launch(Dispatchers.IO) {
            context.dataStore.put(newVersionNumber, value)
        }
    }

    fun fromPreferences(preferences: Preferences) =
        preferences[DataStoreKey.keys[newVersionNumber]?.key as Preferences.Key<String>].toVersion()
}
