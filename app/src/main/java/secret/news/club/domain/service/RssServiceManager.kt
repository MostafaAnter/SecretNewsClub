package secret.news.club.domain.service

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import secret.news.club.domain.model.rss.RssService
import secret.news.club.domain.util.detectUserCountry
import secret.news.club.infrastructure.preference.SettingsProvider
import secret.news.club.infrastructure.rss.getLanguageForCountry
import secret.news.club.infrastructure.rss.getRssServicesByCountry
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RssServiceManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val settingsProvider: SettingsProvider
) {
    private val rssCache = mutableMapOf<String, List<RssService>>()

    fun getRssServices(
        countryCode: String,
        preferNativeLanguage: Boolean = true,
        forceRefresh: Boolean = false
    ): List<RssService> {
        val cacheKey = "${countryCode}_${preferNativeLanguage}"

        if (!forceRefresh && rssCache.containsKey(cacheKey)) {
            return rssCache[cacheKey] ?: emptyList()
        }

        val services = getRssServicesByCountry(countryCode, preferNativeLanguage)
        rssCache[cacheKey] = services
        return services
    }

    fun getRssServicesByLanguage(language: String): List<RssService> {
        val countryCode = settingsProvider.settings.country?.value?.ifEmpty { detectUserCountry(context) } ?: detectUserCountry(context)
        return getRssServices(countryCode, false).filter {
            it.language.equals(language, ignoreCase = true)
        }
    }
}
