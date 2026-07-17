package secret.news.club.infrastructure.preference

import android.content.Context
import androidx.compose.runtime.compositionLocalOf
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import secret.news.club.ui.ext.DataStoreKey
import secret.news.club.ui.ext.DataStoreKey.Companion.pushNotificationsEnabled
import secret.news.club.ui.ext.dataStore
import secret.news.club.ui.ext.put

val LocalPushNotificationsEnabled =
    compositionLocalOf<PushNotificationsEnabledPreference> { PushNotificationsEnabledPreference.default }

sealed class PushNotificationsEnabledPreference(val value: Boolean) : Preference() {
    data object ON : PushNotificationsEnabledPreference(true)
    data object OFF : PushNotificationsEnabledPreference(false)

    override fun put(context: Context, scope: CoroutineScope) {
        scope.launch {
            context.dataStore.put(pushNotificationsEnabled, value)
        }
    }

    fun toggle(context: Context, scope: CoroutineScope) = scope.launch {
        context.dataStore.put(pushNotificationsEnabled, !value)
    }

    companion object {
        val default = ON
        val values = listOf(ON, OFF)

        fun fromPreferences(preferences: Preferences) =
            when (preferences[DataStoreKey.keys[pushNotificationsEnabled]?.key as Preferences.Key<Boolean>]) {
                true -> ON
                false -> OFF
                else -> default
            }
    }
}
