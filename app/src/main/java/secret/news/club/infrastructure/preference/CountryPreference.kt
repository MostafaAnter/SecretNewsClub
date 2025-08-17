package secret.news.club.infrastructure.preference

import android.content.Context
import androidx.compose.runtime.compositionLocalOf
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import secret.news.club.domain.util.detectUserCountry
import secret.news.club.ui.ext.DataStoreKey
import secret.news.club.ui.ext.dataStore
import secret.news.club.ui.ext.put


open class CountryPreference(val value: String) : Preference() {
    class UseDeviceCountry(context: Context) : CountryPreference(detectUserCountry(context))
    class Country(val countryCode: String) : CountryPreference(countryCode)

    override fun put(context: Context, scope: CoroutineScope) {
        scope.launch {
            context.dataStore.put(DataStoreKey.country, value)
        }
    }

    companion object {
        fun default(context: Context) = UseDeviceCountry(context)
        fun fromPreferences(preferences: Preferences, context: Context): CountryPreference {
            val countryCode = preferences[DataStoreKey.keys[DataStoreKey.country]?.key as Preferences.Key<String>]
            return if (countryCode.isNullOrEmpty()) {
                default(context)
            } else {
                Country(countryCode)
            }
        }
    }
}

val LocalCountry = compositionLocalOf<CountryPreference?> { error("CountryPreference not provided") }
