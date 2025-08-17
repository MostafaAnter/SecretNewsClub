package secret.news.club.domain.service

import android.content.Context
import secret.news.club.domain.model.rss.RssService
import secret.news.club.domain.util.detectUserCountry
import secret.news.club.infrastructure.rss.getLanguageForCountry
import secret.news.club.infrastructure.rss.getRssServicesByCountry
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RssServiceManager @Inject constructor() {
    private val rssCache = mutableMapOf<String, List<RssService>>()

    fun getRssServices(
        context: Context,
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

    fun getRssServicesByLanguage(context: Context, language: String): List<RssService> {
        val countryCode = detectUserCountry(context)
        return getRssServices(context, countryCode, false).filter {
            it.language.equals(language, ignoreCase = true)
        }
    }

    fun getNativeLanguageServices(context: Context): List<RssService> {
        val countryCode = detectUserCountry(context)
        val nativeLanguage = getLanguageForCountry(countryCode)
        return getRssServicesByLanguage(context, nativeLanguage)
    }
}
