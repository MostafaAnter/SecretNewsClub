package secret.news.club.infrastructure.preference

import android.content.Context
import androidx.compose.runtime.compositionLocalOf
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import secret.news.club.ui.ext.DataStoreKey
import secret.news.club.ui.ext.DataStoreKey.Companion.feedsGroupListExpand
import secret.news.club.ui.ext.dataStore
import secret.news.club.ui.ext.put

val LocalFeedsGroupListExpand =
    compositionLocalOf<FeedsGroupListExpandPreference> { FeedsGroupListExpandPreference.default }

sealed class FeedsGroupListExpandPreference(val value: Boolean) : Preference() {
    object ON : FeedsGroupListExpandPreference(true)
    object OFF : FeedsGroupListExpandPreference(false)

    override fun put(context: Context, scope: CoroutineScope) {
        scope.launch {
            context.dataStore.put(
                DataStoreKey.feedsGroupListExpand,
                value
            )
        }
    }

    companion object {

        val default = ON
        val values = listOf(ON, OFF)

        fun fromPreferences(preferences: Preferences) =
            when (preferences[DataStoreKey.keys[feedsGroupListExpand]?.key as Preferences.Key<Boolean>]) {
                true -> ON
                false -> OFF
                else -> default
            }
    }
}

operator fun FeedsGroupListExpandPreference.not(): FeedsGroupListExpandPreference =
    when (value) {
        true -> FeedsGroupListExpandPreference.OFF
        false -> FeedsGroupListExpandPreference.ON
    }
