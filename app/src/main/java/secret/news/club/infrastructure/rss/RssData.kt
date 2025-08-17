package secret.news.club.infrastructure.rss

import secret.news.club.domain.model.rss.RssCategory
import secret.news.club.domain.model.rss.RssService

// A more comprehensive and verified list of RSS services.

private fun getUSRssServices(): List<RssService> = listOf(
    RssService("The New York Times", "https://rss.nytimes.com/services/xml/rss/nyt/HomePage.xml", RssCategory.NEWS, "en", "US", "NYT Home Page"),
    RssService("NPR", "https://feeds.npr.org/1001/rss.xml", RssCategory.NEWS, "en", "US", "NPR News"),
    RssService("The Verge", "https://www.theverge.com/rss/index.xml", RssCategory.TECHNOLOGY, "en", "US", "Tech and Science News"),
    RssService("TechCrunch", "https://techcrunch.com/feed/", RssCategory.TECHNOLOGY, "en", "US", "Startup and Tech News"),
    RssService("ESPN", "https://www.espn.com/espn/rss/news", RssCategory.SPORTS, "en", "US", "Sports News"),
    RssService("Variety", "https://variety.com/feed/", RssCategory.ENTERTAINMENT, "en", "US", "Entertainment News")
)

private fun getUKRssServices(): List<RssService> = listOf(
    RssService("BBC News", "http://feeds.bbci.co.uk/news/rss.xml", RssCategory.NEWS, "en", "GB", "BBC News UK"),
    RssService("The Guardian", "https://www.theguardian.com/world/rss", RssCategory.NEWS, "en", "GB", "The Guardian World News"),
    RssService("Financial Times", "https://www.ft.com/rss/world", RssCategory.BUSINESS, "en", "GB", "FT World News"),
    RssService("Sky Sports", "https://www.skysports.com/rss/12040", RssCategory.SPORTS, "en", "GB", "Sky Sports News")
)

private fun getCanadaRssServices(): List<RssService> = listOf(
    RssService("Global News", "https://globalnews.ca/feed/", RssCategory.NEWS, "en", "CA", "Global News"),
    RssService("National Post", "https://nationalpost.com/feed", RssCategory.NEWS, "en", "CA", "National Post"),
)

private fun getAustraliaRssServices(): List<RssService> = listOf(
    RssService("ABC News", "https://www.abc.net.au/news/feed/51120/rss.xml", RssCategory.NEWS, "en", "AU", "ABC News Australia"),
    RssService("Sydney Morning Herald", "https://www.smh.com.au/rss/feed.xml", RssCategory.NEWS, "en", "AU", "SMH News"),
    RssService("The Roar", "https://www.theroar.com.au/feed/", RssCategory.SPORTS, "en", "AU", "The Roar")
)

private fun getNewZealandRssServices(): List<RssService> = listOf(
    RssService("Stuff.co.nz", "https://www.stuff.co.nz/rss", RssCategory.NEWS, "en", "NZ", "Stuff.co.nz - National"),
    RssService("Radio New Zealand", "https://www.rnz.co.nz/rss/national.xml", RssCategory.NEWS, "en", "NZ", "RNZ National")
)

private fun getIrelandRssServices(): List<RssService> = listOf(
    RssService("RTÉ News", "https://www.rte.ie/news/rss/news-headlines.xml", RssCategory.NEWS, "en", "IE", "RTÉ News"),
    RssService("Irish Independent", "https://www.independent.ie/breaking-news/rss", RssCategory.NEWS, "en", "IE", "Irish Independent")
)

private fun getSouthAfricaRssServices(): List<RssService> = listOf(
    RssService("News24", "http://feeds.news24.com/articles/news24/TopStories/rss", RssCategory.NEWS, "en", "ZA", "News24 Top Stories"),
    RssService("Mail & Guardian", "https://mg.co.za/feed/", RssCategory.NEWS, "en", "ZA", "Mail & Guardian")
)

private fun getNetherlandsRssServices(): List<RssService> = listOf(
    RssService("NOS", "https://feeds.nos.nl/nosnieuwsalgemeen", RssCategory.NEWS, "nl", "NL", "NOS Nieuws"),
    RssService("De Telegraaf", "https://www.telegraaf.nl/rss", RssCategory.NEWS, "nl", "NL", "De Telegraaf")
)

private fun getSwedenRssServices(): List<RssService> = listOf(
    RssService("SVT Nyheter", "https://www.svt.se/nyheter/rss.xml", RssCategory.NEWS, "sv", "SE", "SVT News"),
    RssService("Dagens Nyheter", "https://www.dn.se/rss/", RssCategory.NEWS, "sv", "SE", "Dagens Nyheter")
)

private fun getNorwayRssServices(): List<RssService> = listOf(
    RssService("NRK", "https://www.nrk.no/nyheter/siste.rss", RssCategory.NEWS, "no", "NO", "NRK Siste Nytt"),
    RssService("Aftenposten", "https://www.aftenposten.no/rss", RssCategory.NEWS, "no", "NO", "Aftenposten")
)

private fun getDenmarkRssServices(): List<RssService> = listOf(
    RssService("DR", "https://www.dr.dk/nyheder/service/feeds/allenyheder", RssCategory.NEWS, "da", "DK", "DR Nyheder"),
)

private fun getFinlandRssServices(): List<RssService> = listOf(
    RssService("Yle", "https://feeds.yle.fi/uutiset/v1/majorHeadlines/YLE_UUTISET.rss", RssCategory.NEWS, "fi", "FI", "Yle Uutiset"),
    RssService("Helsingin Sanomat", "https://www.hs.fi/rss/teasers/etusivu.xml", RssCategory.NEWS, "fi", "FI", "Helsingin Sanomat")
)

private fun getPolandRssServices(): List<RssService> = listOf(
    RssService("Gazeta.pl", "https://www.gazeta.pl/pub/rss/wiadomosci.xml", RssCategory.NEWS, "pl", "PL", "Gazeta.pl Wiadomości"),
    RssService("Wirtualna Polska", "https://wiadomosci.wp.pl/rss.xml", RssCategory.NEWS, "pl", "PL", "Wirtualna Polska")
)

private fun getTurkeyRssServices(): List<RssService> = listOf(
    RssService("Hürriyet", "https://www.hurriyet.com.tr/rss/anasayfa", RssCategory.NEWS, "tr", "TR", "Hürriyet")
)

private fun getMexicoRssServices(): List<RssService> = listOf(
    RssService("Milenio", "https://www.milenio.com/rss", RssCategory.NEWS, "es", "MX", "Milenio")
)

private fun getArgentinaRssServices(): List<RssService> = listOf(
    RssService("Clarín", "https://www.clarin.com/rss/lo-ultimo/", RssCategory.NEWS, "es", "AR", "Clarín"),
)

private fun getSouthKoreaRssServices(): List<RssService> = listOf(
    RssService("Yonhap News", "https://en.yna.co.kr/RSS/news.xml", RssCategory.NEWS, "en", "KR", "Yonhap News Agency", isNativeLanguage = false),
    RssService("The Korea Times", "https://www.koreatimes.co.kr/www/rss/rss.xml", RssCategory.NEWS, "en", "KR", "The Korea Times", isNativeLanguage = false)
)

private fun getGermanyRssServices(): List<RssService> = listOf(
    RssService("Der Spiegel", "https://www.spiegel.de/schlagzeilen/index.rss", RssCategory.NEWS, "de", "DE", "Nachrichten und aktuelle Berichte"),
    RssService("Tagesschau", "https://www.tagesschau.de/xml/rss2/", RssCategory.NEWS, "de", "DE", "ARD Tagesschau"),
    RssService("Süddeutsche Zeitung", "https://rss.sueddeutsche.de/rss/Topthemen", RssCategory.NEWS, "de", "DE", "SZ Top-Themen"),
)

private fun getFranceRssServices(): List<RssService> = listOf(
    RssService("Le Monde", "https://www.lemonde.fr/rss/une.xml", RssCategory.NEWS, "fr", "FR", "Actualités et informations"),
    RssService("France 24", "https://www.france24.com/fr/rss", RssCategory.NEWS, "fr", "FR", "International News"),
)

private fun getSpainRssServices(): List<RssService> = listOf(
    RssService("El País", "https://feeds.elpais.com/mrss-s/pages/ep/site/elpais.com/portada", RssCategory.NEWS, "es", "ES", "Noticias de España y el mundo"),
    RssService("El Mundo", "https://e00-elmundo.uecdn.es/elmundo/rss/portada.xml", RssCategory.NEWS, "es", "ES", "Noticias de última hora"),
)

private fun getItalyRssServices(): List<RssService> = listOf(
    RssService("La Repubblica", "https://www.repubblica.it/rss/homepage/rss2.0.xml", RssCategory.NEWS, "it", "IT", "Homepage"),
)

private fun getJapanRssServices(): List<RssService> = listOf(
    RssService("NHK News", "https://www3.nhk.or.jp/rss/news/cat0.xml", RssCategory.NEWS, "ja", "JP", "日本の最新ニュース"),
)

private fun getIndiaRssServices(): List<RssService> = listOf(
    RssService("Times of India", "https://timesofindia.indiatimes.com/rssfeedstopstories.cms", RssCategory.NEWS, "en", "IN", "Top Stories", isNativeLanguage = false),
    RssService("The Hindu", "https://www.thehindu.com/feeder/default.rss", RssCategory.NEWS, "en", "IN", "Latest News", isNativeLanguage = false),
    RssService("NDTV", "https://feeds.feedburner.com/ndtvnews-top-stories", RssCategory.NEWS, "en", "IN", "Top Stories", isNativeLanguage = false),
    RssService("ESPN Cricinfo", "https://www.espncricinfo.com/rss/content/story/feeds/0.xml", RssCategory.SPORTS, "en", "IN", "ESPN Cricinfo", isNativeLanguage = false)
)

private fun getBrazilRssServices(): List<RssService> = listOf(
    RssService("Folha de S.Paulo", "https://feeds.folha.uol.com.br/poder/rss091.xml", RssCategory.NEWS, "pt", "BR", "News and Politics"),
    RssService("Globo Esporte", "https://ge.globo.com/rss/ge", RssCategory.SPORTS, "pt", "BR", "Globo Esporte")
)

private fun getRussiaRssServices(): List<RssService> = listOf(
    RssService("TASS", "https://tass.ru/rss/v2.xml", RssCategory.NEWS, "ru", "RU", "Russian News Agency"),
    RssService("Interfax", "https://www.interfax.ru/rss.asp", RssCategory.NEWS, "ru", "RU", "News Agency")
)

private fun getChinaRssServices(): List<RssService> = listOf(
    RssService("South China Morning Post", "https://www.scmp.com/rss/91/feed", RssCategory.NEWS, "en", "CN", "SCMP", isNativeLanguage = false),
)

private fun getArabCountriesRssServices(countryCode: String): List<RssService> {
    val services = mutableListOf<RssService>()
    when (countryCode.uppercase()) {
        "EG" -> {
            services.add(RssService("Mada Masr", "https://www.madamasr.com/en/feed", RssCategory.NEWS, "en", "EG", "Mada Masr", isNativeLanguage = false))
        }
        "SA" -> {
        }
        "AE" -> {
            services.add(RssService("Gulf News", "https://gulfnews.com/feed", RssCategory.NEWS, "en", "AE", "Gulf News", isNativeLanguage = false))
        }
    }
    services.addAll(listOf(
        RssService("Al Jazeera", "https://www.aljazeera.com/xml/rss/all.xml", RssCategory.NEWS, "en", countryCode, "Al Jazeera English", isNativeLanguage = false),
        RssService("BBC Arabic", "https://feeds.bbci.co.uk/arabic/rss.xml", RssCategory.NEWS, "ar", countryCode, "بي بي سي عربي"),
    ))
    return services
}

private fun getDefaultRssServices(): List<RssService> = listOf(
)

fun getRssServicesByCountry(countryCode: String, preferNativeLanguage: Boolean = true): List<RssService> {
    val services = when (countryCode.uppercase()) {
        "US" -> getUSRssServices()
        "GB", "UK" -> getUKRssServices()
        "CA" -> getCanadaRssServices()
        "AU" -> getAustraliaRssServices()
        "NZ" -> getNewZealandRssServices()
        "IE" -> getIrelandRssServices()
        "ZA" -> getSouthAfricaRssServices()
        "DE" -> getGermanyRssServices()
        "FR" -> getFranceRssServices()
        "IT" -> getItalyRssServices()
        "ES" -> getSpainRssServices()
        "NL" -> getNetherlandsRssServices()
        "SE" -> getSwedenRssServices()
        "NO" -> getNorwayRssServices()
        "DK" -> getDenmarkRssServices()
        "FI" -> getFinlandRssServices()
        "PL" -> getPolandRssServices()
        "TR" -> getTurkeyRssServices()
        "JP" -> getJapanRssServices()
        "CN" -> getChinaRssServices()
        "IN" -> getIndiaRssServices()
        "BR" -> getBrazilRssServices()
        "MX" -> getMexicoRssServices()
        "AR" -> getArgentinaRssServices()
        "RU" -> getRussiaRssServices()
        "KR" -> getSouthKoreaRssServices()
        // Arab countries - Add more as needed
        "SA", "AE", "EG", "QA", "JO", "LB", "KW", "BH", "OM" -> getArabCountriesRssServices(countryCode)
        else -> getDefaultRssServices()
    }

    return if (preferNativeLanguage) {
        services.sortedByDescending { it.isNativeLanguage }
    } else {
        services
    }
}

fun getLanguageForCountry(countryCode: String): String {
    return when (countryCode.uppercase()) {
        "US", "GB", "UK", "CA", "AU", "NZ", "IE", "ZA" -> "en"
        "DE", "AT", "CH" -> "de"
        "FR", "BE", "CH" -> "fr"
        "ES", "MX", "AR", "CO", "PE", "VE", "CL", "EC", "GT", "CU", "BO", "DO", "HN", "PY", "SV", "NI", "CR", "PA", "UY" -> "es"
        "IT", "SM", "VA" -> "it"
        "JP" -> "ja"
        "CN", "TW", "HK", "MO", "SG" -> "zh"
        "RU", "BY", "KZ", "KG", "TJ", "UZ" -> "ru"
        "KR" -> "ko"
        "BR", "PT" -> "pt"
        "IN" -> "hi" // Primary, but English is also widely used
        "SA", "AE", "EG", "JO", "LB", "SY", "IQ", "KW", "QA", "BH", "OM", "YE", "MA", "TN", "DZ", "LY", "SD" -> "ar"
        "NL" -> "nl"
        "SE" -> "sv"
        "NO" -> "no"
        "DK" -> "da"
        "FI" -> "fi"
        "PL" -> "pl"
        "TR" -> "tr"
        else -> "en" // Default to English
    }
}
