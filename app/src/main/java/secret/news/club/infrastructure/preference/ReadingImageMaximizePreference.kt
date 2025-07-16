package secret.news.club.infrastructure.preference

import android.content.Context
import androidx.compose.runtime.compositionLocalOf
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import secret.news.club.ui.ext.DataStoreKey
import secret.news.club.ui.ext.DataStoreKey.Companion.readingImageMaximize
import secret.news.club.ui.ext.dataStore
import secret.news.club.ui.ext.put

val LocalReadingImageMaximize =
    compositionLocalOf<ReadingImageMaximizePreference> { ReadingImageMaximizePreference.default }

sealed class ReadingImageMaximizePreference(val value: Boolean) : Preference() {
    object ON : ReadingImageMaximizePreference(true)
    object OFF : ReadingImageMaximizePreference(false)

    override fun put(context: Context, scope: CoroutineScope) {
        scope.launch {
            context.dataStore.put(DataStoreKey.readingImageMaximize, value)
        }
    }

    companion object {

        val default = ON
        val values = listOf(ON, OFF)

        fun fromPreferences(preferences: Preferences) =
            when (preferences[DataStoreKey.keys[readingImageMaximize]?.key as Preferences.Key<Boolean>]) {
                true -> ON
                false -> OFF
                else -> default
            }
    }
}

operator fun ReadingImageMaximizePreference.not(): ReadingImageMaximizePreference =
    when (value) {
        true -> ReadingImageMaximizePreference.OFF
        false -> ReadingImageMaximizePreference.ON
    }
