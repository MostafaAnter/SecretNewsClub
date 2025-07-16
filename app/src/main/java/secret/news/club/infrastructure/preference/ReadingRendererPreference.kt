package secret.news.club.infrastructure.preference

import android.content.Context
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import secret.news.club.R
import secret.news.club.ui.ext.DataStoreKey
import secret.news.club.ui.ext.DataStoreKey.Companion.readingRenderer
import secret.news.club.ui.ext.dataStore
import secret.news.club.ui.ext.put

val LocalReadingRenderer =
    compositionLocalOf<ReadingRendererPreference> { ReadingRendererPreference.default }

sealed class ReadingRendererPreference(val value: Int) : Preference() {
    object WebView : ReadingRendererPreference(0)
    object NativeComponent : ReadingRendererPreference(1)

    override fun put(context: Context, scope: CoroutineScope) {
        scope.launch {
            context.dataStore.put(DataStoreKey.readingRenderer, value)
        }
    }

    @Stable
    fun toDesc(context: Context): String =
        when (this) {
            WebView -> context.getString(R.string.webview)
            NativeComponent -> context.getString(R.string.native_component)
        }

    companion object {

        val default = WebView
        val values = listOf(WebView, NativeComponent)

        fun fromPreferences(preferences: Preferences) =
            when (preferences[DataStoreKey.keys[readingRenderer]?.key as Preferences.Key<Int>]) {
                0 -> WebView
                1 -> NativeComponent
                else -> default
            }
    }
}
