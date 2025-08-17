package secret.news.club.infrastructure.rss

import secret.news.club.domain.model.rss.RssCategory
import secret.news.club.domain.model.rss.RssService

private fun getUSRssServices(): List<RssService> = listOf(
    // News
    RssService("CNN Top Stories", "http://rss.cnn.com/rss/edition.rss", RssCategory.NEWS, "en", "US"),
    RssService("New York Times Home Page", "https://rss.nytimes.com/services/xml/rss/nyt/HomePage.xml", RssCategory.NEWS, "en", "US"),
    RssService("Washington Post World", "http://feeds.washingtonpost.com/rss/world", RssCategory.NEWS, "en", "US"),
    RssService("NBC News Top Stories", "https://feeds.nbcnews.com/nbcnews/public/news", RssCategory.NEWS, "en", "US"),
    RssService("Fox News Latest", "http://feeds.foxnews.com/foxnews/latest", RssCategory.NEWS, "en", "US"),
    // Sports
    RssService("ESPN Top Headlines", "https://www.espn.com/espn/rss/news", RssCategory.SPORTS, "en", "US"),
    RssService("CBS Sports", "https://www.cbssports.com/rss/headlines/", RssCategory.SPORTS, "en", "US"),
    RssService("Yahoo Sports Top Headlines", "https://sports.yahoo.com/top/rss.xml", RssCategory.SPORTS, "en", "US"),
)

private fun getUKRssServices(): List<RssService> = listOf(
    // News
    RssService("BBC News", "http://feeds.bbci.co.uk/news/rss.xml", RssCategory.NEWS, "en", "GB"),
    RssService("The Guardian Top Stories", "https://www.theguardian.com/uk/rss", RssCategory.NEWS, "en", "GB"),
    RssService("Independent UK", "http://www.independent.co.uk/news/uk/rss", RssCategory.NEWS, "en", "GB"),
    RssService("Sky News Top Stories", "http://feeds.skynews.com/feeds/rss/home.xml", RssCategory.NEWS, "en", "GB"),
    // Sports
    RssService("BBC Sport", "http://feeds.bbci.co.uk/sport/rss.xml", RssCategory.SPORTS, "en", "GB"),
    RssService("Sky Sports News", "http://www.skysports.com/rss/12040", RssCategory.SPORTS, "en", "GB"),
    RssService("The Guardian Sport", "https://www.theguardian.com/uk/sport/rss", RssCategory.SPORTS, "en", "GB"),
    RssService("Independent Sport", "http://www.independent.co.uk/sport/rss", RssCategory.SPORTS, "en", "GB")
)

private fun getCanadaRssServices(): List<RssService> = listOf(
    // News
    RssService("National Post", "https://nationalpost.com/feed", RssCategory.NEWS, "en", "CA"),
    RssService("CBC News", "https://www.cbc.ca/cmlink/rss-topstories", RssCategory.NEWS, "en", "CA"),
    RssService("The Globe and Mail", "https://www.theglobeandmail.com/rss/", RssCategory.NEWS, "en", "CA"),
    RssService("Toronto Star", "https://www.thestar.com/content/thestar/feed.RSSManagerServlet.topstories.rss", RssCategory.NEWS, "en", "CA"),
    RssService("CTV News", "https://www.ctvnews.ca/rss/ctvnews-ca-top-stories-public-rss-1.822009", RssCategory.NEWS, "en", "CA"),
    RssService("Le Devoir", "https://www.ledevoir.com/rss/actualites.xml", RssCategory.NEWS, "fr", "CA"),
    RssService("La Presse", "https://www.lapresse.ca/rss/actualites.xml", RssCategory.NEWS, "fr", "CA"),
    // Sports
    RssService("TSN Sports", "https://www.tsn.ca/rss", RssCategory.SPORTS, "en", "CA"),
    RssService("Sportsnet", "https://www.sportsnet.ca/feed/", RssCategory.SPORTS, "en", "CA"),
    RssService("CBC Sports", "https://www.cbc.ca/cmlink/rss-sports", RssCategory.SPORTS, "en", "CA")
)

private fun getIndiaRssServices(): List<RssService> = listOf(
    RssService("The Times of India", "https://timesofindia.indiatimes.com/rssfeeds/-2128936835.cms", RssCategory.NEWS, "en", "IN"),
    RssService("Hindustan Times", "https://www.hindustantimes.com/feeds/rss/india-news/rssfeed.xml", RssCategory.NEWS, "en", "IN"),
    RssService("The Hindu", "https://www.thehindu.com/feeder/default.rss", RssCategory.NEWS, "en", "IN"),
    RssService("ESPN Cricinfo", "https://www.espncricinfo.com/rss/content/story/feeds/0.xml", RssCategory.SPORTS, "en", "IN"),
)

private fun getAustraliaRssServices(): List<RssService> = listOf(
    RssService("ABC News Australia", "https://www.abc.net.au/news/feed/51120/rss.xml", RssCategory.NEWS, "en", "AU"),
    RssService("The Sydney Morning Herald", "https://www.smh.com.au/rss/feed.xml", RssCategory.NEWS, "en", "AU"),
    RssService("The Age", "https://www.theage.com.au/rss/feed.xml", RssCategory.NEWS, "en", "AU"),
    RssService("ESPN Australia", "https://www.espn.com.au/espn/rss/news", RssCategory.SPORTS, "en", "AU")
)

private fun getEgyptRssServices(): List<RssService> = listOf(
    // News
    RssService("Daily News Egypt", "https://dailynewsegypt.com/feed/", RssCategory.NEWS, "en", "EG"),
    RssService("Youm7", "https://www.youm7.com/rss/SectionRss?SectionID=65", RssCategory.NEWS, "ar", "EG"),
    RssService("Al-Ahram", "https://gate.ahram.org.eg/rss/0/0.aspx", RssCategory.NEWS, "ar", "EG"),
    RssService("Al-Masry Al-Youm", "https://www.almasryalyoum.com/rss/rssfeeds", RssCategory.NEWS, "ar", "EG"),
    RssService("Egypt Independent", "https://www.egyptindependent.com/feed/", RssCategory.NEWS, "en", "EG"),
    RssService("Sada Elbalad", "https://see.news/rss", RssCategory.NEWS, "ar", "EG"),
    // Sports
    RssService("FilGoal", "https://www.filgoal.com/rss/news", RssCategory.SPORTS, "ar", "EG"),
    RssService("YallaKora", "https://www.yallakora.com/rss", RssCategory.SPORTS, "ar", "EG"),
    RssService("KingFut", "https://www.kingfut.com/feed/", RssCategory.SPORTS, "en", "EG")
)

private fun getFranceRssServices(): List<RssService> = listOf(
    // News
    RssService("Le Monde", "https://www.lemonde.fr/rss/une.xml", RssCategory.NEWS, "fr", "FR"),
    RssService("Le Figaro", "https://www.lefigaro.fr/rss/figaro_actualites.xml", RssCategory.NEWS, "fr", "FR"),
    RssService("France 24", "https://www.france24.com/fr/rss", RssCategory.NEWS, "fr", "FR"),
    RssService("Libération", "https://www.liberation.fr/rss/latest/", RssCategory.NEWS, "fr", "FR"),
    RssService("Le Parisien", "https://www.leparisien.fr/feeds/rss.xml", RssCategory.NEWS, "fr", "FR"),
    RssService("20 Minutes", "https://www.20minutes.fr/feeds/rss-une.xml", RssCategory.NEWS, "fr", "FR"),
    RssService("L'Express", "https://www.lexpress.fr/rss/alaune.xml", RssCategory.NEWS, "fr", "FR"),
    // Sports
    RssService("L'Équipe", "https://www.lequipe.fr/rss/actu_rss.xml", RssCategory.SPORTS, "fr", "FR"),
    RssService("Eurosport France", "https://www.eurosport.fr/rss.xml", RssCategory.SPORTS, "fr", "FR"),
    RssService("RMC Sport", "https://rmcsport.bfmtv.com/rss/info/flux-rmc-sport-home/", RssCategory.SPORTS, "fr", "FR")
)

private fun getGermanyRssServices(): List<RssService> = listOf(
    RssService("Der Spiegel", "https://www.spiegel.de/schlagzeilen/tops/index.rss", RssCategory.NEWS, "de", "DE"),
    RssService("Die Welt", "https://www.welt.de/feeds/topnews.rss", RssCategory.NEWS, "de", "DE"),
    RssService("Tagesschau", "https://www.tagesschau.de/xml/rss2", RssCategory.NEWS, "de", "DE"),
)

private fun getItalyRssServices(): List<RssService> = listOf(
    RssService("La Repubblica", "https://www.repubblica.it/rss/homepage/rss2.0.xml", RssCategory.NEWS, "it", "IT"),
    RssService("ANSA", "https://www.ansa.it/sito/ansait_rss.xml", RssCategory.NEWS, "it", "IT"),
    RssService("Corriere dello Sport", "https://www.corrieredellosport.it/rss/", RssCategory.SPORTS, "it", "IT")
)

private fun getSpainRssServices(): List<RssService> = listOf(
    RssService("El País", "https://feeds.elpais.com/mrss-s/pages/ep/site/elpais.com/portada", RssCategory.NEWS, "es", "ES"),
    RssService("El Mundo", "https://e00-elmundo.uecdn.es/elmundo/rss/espana.xml", RssCategory.NEWS, "es", "ES"),
)

private fun getBrazilRssServices(): List<RssService> = listOf(
    RssService("Folha de S.Paulo", "https://feeds.folha.uol.com.br/emcimadahora/rss091.xml", RssCategory.NEWS, "pt", "BR"),
    RssService("Estadão", "https://feeds.folha.uol.com.br/poder/rss091.xml", RssCategory.NEWS, "pt", "BR"),
)

private fun getJapanRssServices(): List<RssService> = listOf(
    RssService("NHK News", "https://www3.nhk.or.jp/rss/news/cat0.xml", RssCategory.NEWS, "ja", "JP"),
    RssService("The Japan Times", "https://www.japantimes.co.jp/feed/", RssCategory.NEWS, "en", "JP"),
    RssService("Mainichi Shimbun", "https://mainichi.jp/rss/etc/flash.rss", RssCategory.NEWS, "ja", "JP"),
)

private fun getChinaRssServices(): List<RssService> = listOf(
    // News
    RssService("Xinhua News", "http://www.xinhuanet.com/english/rss/worldrss.xml", RssCategory.NEWS, "zh", "CN"),
    RssService("China Daily", "http://www.chinadaily.com.cn/rss/china_rss.xml", RssCategory.NEWS, "en", "CN"),
    RssService("Global Times", "https://www.globaltimes.cn/rss/China.xml", RssCategory.NEWS, "en", "CN"),
    RssService("People's Daily Online", "http://en.people.cn/rss/english.xml", RssCategory.NEWS, "en", "CN"),
    // Sports
    RssService("Xinhua Sports", "http://www.xinhuanet.com/english/rss/sportsrss.xml", RssCategory.SPORTS, "zh", "CN"),
    RssService("China Daily Sports", "http://www.chinadaily.com.cn/rss/sports_rss.xml", RssCategory.SPORTS, "en", "CN")
)

private fun getRussiaRssServices(): List<RssService> = listOf(
    RssService("RT News", "https://www.rt.com/rss/news/", RssCategory.NEWS, "en", "RU"),
    RssService("Moscow Times", "https://www.themoscowtimes.com/rss/news", RssCategory.NEWS, "en", "RU"),
    RssService("Championat", "https://www.championat.com/rss/news/", RssCategory.SPORTS, "ru", "RU")
)

private fun getSouthAfricaRssServices(): List<RssService> = listOf(
    RssService("News24", "https://feeds.24.com/articles/news24/TopStories/rss", RssCategory.NEWS, "en", "ZA"),
    RssService("TimesLIVE", "https://www.timeslive.co.za/rss", RssCategory.NEWS, "en", "ZA"),
    RssService("KickOff", "https://www.kickoff.com/rss", RssCategory.SPORTS, "en", "ZA")
)

private fun getSaudiArabiaRssServices(): List<RssService> = listOf(
    // News
    RssService("Al Riyadh Newspaper", "https://www.alriyadh.com/rss", RssCategory.NEWS, "ar", "SA"),
    RssService("Okaz", "https://www.okaz.com.sa/rss", RssCategory.NEWS, "ar", "SA"),
    RssService("Al Eqtisadiah", "https://www.aleqt.com/rss", RssCategory.NEWS, "ar", "SA"),
    RssService("Saudi Gazette", "https://saudigazette.com.sa/rss", RssCategory.NEWS, "en", "SA"),
    RssService("Arab News", "https://www.arabnews.com/rss", RssCategory.NEWS, "en", "SA"),
    // Sports
    RssService("Arriyadiyah", "https://arriyadiyah.com/rss", RssCategory.SPORTS, "ar", "SA"),
    RssService("Kooora", "https://www.kooora.com/?n=0&o=rss", RssCategory.SPORTS, "ar", "SA")
)

private fun getUaeRssServices(): List<RssService> = listOf(
    // News
    RssService("The National", "https://www.thenationalnews.com/rss", RssCategory.NEWS, "en", "AE"),
    RssService("Gulf News", "https://gulfnews.com/rss?generatorName=top-stories", RssCategory.NEWS, "en", "AE"),
    RssService("Khaleej Times", "https://www.khaleejtimes.com/rss", RssCategory.NEWS, "en", "AE"),
    RssService("Al Bayan", "https://www.albayan.ae/polopoly_fs/1.2018576.1508838572!/rss/rss.xml", RssCategory.NEWS, "ar", "AE"),
    RssService("Emirates News Agency (WAM)", "https://wam.ae/en/rss.xml", RssCategory.NEWS, "en", "AE"),
    // Sports
    RssService("Sport360", "https://sport360.com/feed", RssCategory.SPORTS, "en", "AE"),
    RssService("Al-Ittihad Sports", "https://www.alittihad.ae/rss/sports", RssCategory.SPORTS, "ar", "AE")
)

private fun getTurkeyRssServices(): List<RssService> = listOf(
    RssService("Hürriyet", "https://www.hurriyet.com.tr/rss/anasayfa", RssCategory.NEWS, "tr", "TR"),
    RssService("Sabah", "https://www.sabah.com.tr/rss/gundem.xml", RssCategory.NEWS, "tr", "TR"),
    RssService("Daily Sabah", "https://www.dailysabah.com/rssFeed/1", RssCategory.NEWS, "en", "TR"),
)

private fun getArgentinaRssServices(): List<RssService> = listOf(
    RssService("Clarín", "https://www.clarin.com/rss/lo-ultimo/", RssCategory.NEWS, "es", "AR"),
    RssService("Olé - Sports", "https://www.ole.com.ar/rss/ultimas-noticias/", RssCategory.SPORTS, "es", "AR"),
)

private fun getMexicoRssServices(): List<RssService> = listOf(
    RssService("Milenio", "https://www.milenio.com/rss", RssCategory.NEWS, "es", "MX"),
    RssService("Excélsior", "https://www.excelsior.com.mx/rss", RssCategory.NEWS, "es", "MX"),
    RssService("Record - Sports", "https://www.record.com.mx/rss.xml", RssCategory.SPORTS, "es", "MX"),
)

private fun getSwedenRssServices(): List<RssService> = listOf(
    RssService("Dagens Nyheter", "https://www.dn.se/rss/", RssCategory.NEWS, "sv", "SE"),
    RssService("Svenska Dagbladet", "https://www.svd.se/?service=rss", RssCategory.NEWS, "sv", "SE"),
    RssService("Aftonbladet", "https://rss.aftonbladet.se/rss2/small/pages/sections/senastenytt/", RssCategory.NEWS, "sv", "SE"),
    RssService("Fotbollskanalen", "https://www.fotbollskanalen.se/rss/", RssCategory.SPORTS, "sv", "SE"),
    RssService("Sportbladet", "https://rss.aftonbladet.se/rss2/small/pages/sections/sportbladet/", RssCategory.SPORTS, "sv", "SE")
)

private fun getNorwayRssServices(): List<RssService> = listOf(
    RssService("Aftenposten", "https://www.aftenposten.no/rss", RssCategory.NEWS, "no", "NO"),
    RssService("VG", "https://www.vg.no/rss/feed/", RssCategory.NEWS, "no", "NO"),
    RssService("VG Sporten", "https://www.vg.no/rss/feed/?categories=sport", RssCategory.SPORTS, "no", "NO"),
    RssService("NRK Sport", "https://www.nrk.no/toppsaker.rss", RssCategory.SPORTS, "no", "NO")
)

private fun getDenmarkRssServices(): List<RssService> = listOf(
    RssService("Politiken", "https://politiken.dk/rss/senestenyt.rss", RssCategory.NEWS, "da", "DK"),
)

private fun getFinlandRssServices(): List<RssService> = listOf(
    RssService("Helsingin Sanomat", "https://www.hs.fi/rss/teasers/etusivu.xml", RssCategory.NEWS, "fi", "FI"),
    RssService("Ilta-Sanomat", "https://www.is.fi/rss/tuoreimmat.xml", RssCategory.NEWS, "fi", "FI"),
    RssService("Yle Uutiset", "https://feeds.yle.fi/uutiset/v1/recent.rss?publisherIds=YLE_UUTISET", RssCategory.NEWS, "fi", "FI"),
    RssService("Ilta-Sanomat Sport", "https://www.is.fi/rss/urheilu.xml", RssCategory.SPORTS, "fi", "FI")
)

private fun getNetherlandsRssServices(): List<RssService> = listOf(
    RssService("NOS Nieuws", "https://feeds.nos.nl/nosnieuwsalgemeen", RssCategory.NEWS, "nl", "NL"),
    RssService("De Telegraaf", "https://www.telegraaf.nl/rss", RssCategory.NEWS, "nl", "NL"),
    RssService("NRC", "https://www.nrc.nl/rss/", RssCategory.NEWS, "nl", "NL"),
    RssService("Voetbal International", "https://www.vi.nl/rss", RssCategory.SPORTS, "nl", "NL"),
    RssService("AD Sportwereld", "https://www.ad.nl/sport/rss.xml", RssCategory.SPORTS, "nl", "NL")
)

private fun getBelgiumRssServices(): List<RssService> = listOf(
    RssService("Het Laatste Nieuws", "https://www.hln.be/rss.xml", RssCategory.NEWS, "nl", "BE"),
)

private fun getAustriaRssServices(): List<RssService> = listOf(
    // News
    RssService("Der Standard", "https://www.derstandard.at/rss", RssCategory.NEWS, "de", "AT"),
    // Sports
)

private fun getSwitzerlandRssServices(): List<RssService> = listOf(
    // News
    // Sports
)

private fun getPolandRssServices(): List<RssService> = listOf(
    // News
    // Sports
)

private fun getCzechRepublicRssServices(): List<RssService> = listOf(
    // News
    RssService("ČT24", "https://ct24.ceskatelevize.cz/rss", RssCategory.NEWS, "cs", "CZ"),
    RssService("Právo", "https://www.novinky.cz/rss/", RssCategory.NEWS, "cs", "CZ"),
    // Sports
    RssService("iSport.cz", "https://isport.blesk.cz/rss", RssCategory.SPORTS, "cs", "CZ")
)

private fun getHungaryRssServices(): List<RssService> = listOf(
    // News
    RssService("Index.hu", "https://index.hu/24ora/rss", RssCategory.NEWS, "hu", "HU"),
    // Sports
)

private fun getGreeceRssServices(): List<RssService> = listOf(
    // News
    // Sports
)

private fun getNigeriaRssServices(): List<RssService> = listOf(
    // News
    RssService("The Guardian Nigeria", "https://guardian.ng/feed/", RssCategory.NEWS, "en", "NG"),
    RssService("Vanguard News", "https://www.vanguardngr.com/feed/", RssCategory.NEWS, "en", "NG"),
    // Sports
    RssService("Complete Sports Nigeria", "https://www.completesports.com/feed/", RssCategory.SPORTS, "en", "NG")
)

private fun getKenyaRssServices(): List<RssService> = listOf(
    RssService("Standard Media - Kenya", "https://www.standardmedia.co.ke/rss/headlines.php", RssCategory.NEWS, "en", "KE"),
)

private fun getDefaultRssServices(): List<RssService> = emptyList()

fun getRssServicesByCountry(countryCode: String, preferNativeLanguage: Boolean = true): List<RssService> {
    val services = when (countryCode.uppercase()) {
        "US" -> getUSRssServices()
        "GB", "UK" -> getUKRssServices()
        "CA" -> getCanadaRssServices()
        "AU" -> getAustraliaRssServices()
        "DE" -> getGermanyRssServices()
        "FR" -> getFranceRssServices()
        "IT" -> getItalyRssServices()
        "ES" -> getSpainRssServices()
        "JP" -> getJapanRssServices()
        "CN" -> getChinaRssServices()
        "IN" -> getIndiaRssServices()
        "BR" -> getBrazilRssServices()
        "MX" -> getMexicoRssServices()
        "RU" -> getRussiaRssServices()
        "ZA" -> getSouthAfricaRssServices()
        "AE" -> getUaeRssServices()
        "SA" -> getSaudiArabiaRssServices()
        "TR" -> getTurkeyRssServices()
        "AR" -> getArgentinaRssServices()
        "NG" -> getNigeriaRssServices()
        "KE" -> getKenyaRssServices()
        "SE" -> getSwedenRssServices()
        "NO" -> getNorwayRssServices()
        "DK" -> getDenmarkRssServices()
        "FI" -> getFinlandRssServices()
        "NL" -> getNetherlandsRssServices()
        "BE" -> getBelgiumRssServices()
        "AT" -> getAustriaRssServices()
        "CH" -> getSwitzerlandRssServices()
        "PL" -> getPolandRssServices()
        "CZ" -> getCzechRepublicRssServices()
        "HU" -> getHungaryRssServices()
        "GR" -> getGreeceRssServices()
        "EG" -> getEgyptRssServices()
        else -> getDefaultRssServices()
    }
    
    return if (preferNativeLanguage) {
        // Prioritize native language sources
        services.sortedByDescending { it.isNativeLanguage }
    } else {
        services
    }
}

fun getLanguageForCountry(countryCode: String): String {
    return when (countryCode.uppercase()) {
        "US", "GB", "UK", "CA", "AU", "NZ", "IE", "ZA", "KE", "NG" -> "en"
        "DE", "AT", "CH" -> "de"
        "FR", "BE" -> "fr"
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
        "TH" -> "th"
        "VN" -> "vi"
        "ID" -> "id"
        "MY" -> "ms"
        "PH" -> "tl" // Filipino/Tagalog, but English is also official
        "CZ" -> "cs"
        "HU" -> "hu"
        "GR" -> "el"
        else -> "en" // Default to English
    }
}
