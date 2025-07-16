package secret.news.club.infrastructure.preference

import android.content.Context
import androidx.compose.runtime.compositionLocalOf
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import secret.news.club.ui.ext.DataStoreKey
import secret.news.club.ui.ext.DataStoreKey.Companion.flowArticleListDateStickyHeader
import secret.news.club.ui.ext.dataStore
import secret.news.club.ui.ext.put

val LocalFlowArticleListDateStickyHeader =
    compositionLocalOf<FlowArticleListDateStickyHeaderPreference> { FlowArticleListDateStickyHeaderPreference.default }

sealed class FlowArticleListDateStickyHeaderPreference(val value: Boolean) : Preference() {
    object ON : FlowArticleListDateStickyHeaderPreference(true)
    object OFF : FlowArticleListDateStickyHeaderPreference(false)

    override fun put(context: Context, scope: CoroutineScope) {
        scope.launch {
            context.dataStore.put(
                DataStoreKey.flowArticleListDateStickyHeader,
                value
            )
        }
    }

    companion object {

        val default = OFF
        val values = listOf(ON, OFF)

        fun fromPreferences(preferences: Preferences) =
            when (preferences[DataStoreKey.keys[flowArticleListDateStickyHeader]?.key as Preferences.Key<Boolean>]) {
                true -> ON
                false -> OFF
                else -> default
            }
    }
}

operator fun FlowArticleListDateStickyHeaderPreference.not(): FlowArticleListDateStickyHeaderPreference =
    when (value) {
        true -> FlowArticleListDateStickyHeaderPreference.OFF
        false -> FlowArticleListDateStickyHeaderPreference.ON
    }
