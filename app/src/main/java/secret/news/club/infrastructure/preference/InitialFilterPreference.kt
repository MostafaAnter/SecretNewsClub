package secret.news.club.infrastructure.preference

import android.content.Context
import androidx.compose.runtime.compositionLocalOf
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import secret.news.club.R
import secret.news.club.domain.model.general.Filter
import secret.news.club.ui.ext.DataStoreKey
import secret.news.club.ui.ext.DataStoreKey.Companion.initialFilter
import secret.news.club.ui.ext.dataStore
import secret.news.club.ui.ext.put

val LocalInitialFilter =
    compositionLocalOf<InitialFilterPreference> { InitialFilterPreference.default }

sealed class InitialFilterPreference(val value: Int) : Preference() {
    object Starred : InitialFilterPreference(0)
    object Unread : InitialFilterPreference(1)
    object All : InitialFilterPreference(2)

    override fun put(context: Context, scope: CoroutineScope) {
        scope.launch {
            context.dataStore.put(
                DataStoreKey.initialFilter,
                value
            )
        }
    }

    fun toDesc(context: Context): String =
        when (this) {
            Starred -> context.getString(R.string.starred)
            Unread -> context.getString(R.string.unread)
            All -> context.getString(R.string.all)
        }

    fun toFilter(): Filter {
        return when (this) {
            Starred -> Filter.Starred
            Unread -> Filter.Unread
            All -> Filter.All
        }
    }

    companion object {

        val default = All
        val values = listOf(Starred, Unread, All)

        fun fromPreferences(preferences: Preferences) =
            when (preferences[DataStoreKey.keys[initialFilter]?.key as Preferences.Key<Int>]) {
                0 -> Starred
                1 -> Unread
                2 -> All
                else -> default
            }
    }
}
