package secret.news.club.domain.util

import android.content.Context
import android.os.Build
import android.telephony.TelephonyManager
import java.util.Locale
import secret.news.club.BuildConfig

fun detectUserCountry(context: Context): String {
    // Priority 1: Debug override
    if (BuildConfig.DEBUG) {
        getDebugCountryCode()?.let {
            return it.uppercase(Locale.US)
        }
    }

    // Priority 2: TelephonyManager (SIM Country) - Often most reliable
    try {
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val simCountry = telephonyManager.simCountryIso
        if (simCountry != null && simCountry.length == 2) {
            return simCountry.uppercase(Locale.US)
        }
    } catch (e: Exception) {
        // Ignore and proceed to the next method
    }

    // 2. Try TelephonyManager (Network Country)
    try {
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val networkCountry = telephonyManager.networkCountryIso
        if (networkCountry != null && networkCountry.length == 2) {
            return networkCountry.uppercase(Locale.US)
        }
    } catch (e: Exception) {
        // Ignore and proceed to the next method
    }

    // 3. Try Configuration Locale - User's selected locale in device settings
    try {
        val config = context.resources.configuration
        val locale: Locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.locales.get(0)
        } else {
            @Suppress("DEPRECATION")
            config.locale
        }
        val country = locale.country
        if (country != null && country.length == 2) {
            return country.uppercase(Locale.US)
        }
    } catch (e: Exception) {
        // Ignore and proceed to the next method
    }

    // 4. Fallback to default JVM locale
    try {
        val country = Locale.getDefault().country
        if (country != null && country.length == 2) {
            return country.uppercase(Locale.US)
        }
    } catch (e: Exception) {
        // Ignore
    }

    // 5. Final fallback
    return "US"
}
