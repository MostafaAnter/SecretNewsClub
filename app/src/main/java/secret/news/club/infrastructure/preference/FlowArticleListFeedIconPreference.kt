package secret.news.club.infrastructure.preference

import android.content.Context
import androidx.compose.runtime.compositionLocalOf
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import secret.news.club.ui.ext.DataStoreKey
import secret.news.club.ui.ext.DataStoreKey.Companion.flowArticleListFeedIcon
import secret.news.club.ui.ext.dataStore
import secret.news.club.ui.ext.put

val LocalFlowArticleListFeedIcon =
    compositionLocalOf<FlowArticleListFeedIconPreference> { FlowArticleListFeedIconPreference.default }

sealed class FlowArticleListFeedIconPreference(val value: Boolean) : Preference() {
    object ON : FlowArticleListFeedIconPreference(true)
    object OFF : FlowArticleListFeedIconPreference(false)

    override fun put(context: Context, scope: CoroutineScope) {
        scope.launch {
            context.dataStore.put(
                DataStoreKey.flowArticleListFeedIcon,
                value
            )
        }
    }

    companion object {

        val default = ON
        val values = listOf(ON, OFF)

        fun fromPreferences(preferences: Preferences) =
            when (preferences[DataStoreKey.keys[flowArticleListFeedIcon]?.key as Preferences.Key<Boolean>]) {
                true -> ON
                false -> OFF
                else -> default
            }
    }
}

operator fun FlowArticleListFeedIconPreference.not(): FlowArticleListFeedIconPreference =
    when (value) {
        true -> FlowArticleListFeedIconPreference.OFF
        false -> FlowArticleListFeedIconPreference.ON
    }
