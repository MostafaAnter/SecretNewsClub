package secret.news.club.infrastructure.rss

import secret.news.club.domain.model.rss.RssCategory
import secret.news.club.domain.model.rss.RssService

private fun getUSRssServices(): List<RssService> = listOf(
    // News
    RssService("CNN Top Stories", "http://rss.cnn.com/rss/edition.rss", RssCategory.NEWS, "en", "US"),
    RssService("New York Times Home Page", "https://rss.nytimes.com/services/xml/rss/nyt/HomePage.xml", RssCategory.NEWS, "en", "US"),
    RssService("Washington Post World", "http://feeds.washingtonpost.com/rss/world", RssCategory.NEWS, "en", "US"),
    RssService("NBC News Top Stories", "https://feeds.nbcnews.com/nbcnews/public/news", RssCategory.NEWS, "en", "US"),
    RssService("Reuters Top News", "http://feeds.reuters.com/reuters/topNews", RssCategory.NEWS, "en", "US"),
    RssService("Fox News Latest", "http://feeds.foxnews.com/foxnews/latest", RssCategory.NEWS, "en", "US"),
    RssService("USA Today Top Stories", "http://rssfeeds.usatoday.com/usatoday-NewsTopStories", RssCategory.NEWS, "en", "US"),
    // Sports
    RssService("ESPN Top Headlines", "https://www.espn.com/espn/rss/news", RssCategory.SPORTS, "en", "US"),
    RssService("CBS Sports", "https://www.cbssports.com/rss/headlines/", RssCategory.SPORTS, "en", "US"),
    RssService("Yahoo Sports Top Headlines", "https://sports.yahoo.com/top/rss.xml", RssCategory.SPORTS, "en", "US"),
    RssService("Sports Illustrated", "https://www.si.com/rss/si_topstories.rss", RssCategory.SPORTS, "en", "US")
)

private fun getUKRssServices(): List<RssService> = listOf(
    // News
    RssService("BBC News", "http://feeds.bbci.co.uk/news/rss.xml", RssCategory.NEWS, "en", "GB"),
    RssService("The Guardian Top Stories", "https://www.theguardian.com/uk/rss", RssCategory.NEWS, "en", "GB"),
    RssService("The Telegraph UK", "https://www.telegraph.co.uk/news/rss.xml", RssCategory.NEWS, "en", "GB"),
    RssService("Independent UK", "http://www.independent.co.uk/news/uk/rss", RssCategory.NEWS, "en", "GB"),
    RssService("Sky News Top Stories", "http://feeds.skynews.com/feeds/rss/home.xml", RssCategory.NEWS, "en", "GB"),
    // Sports
    RssService("BBC Sport", "http://feeds.bbci.co.uk/sport/rss.xml", RssCategory.SPORTS, "en", "GB"),
    RssService("Sky Sports News", "http://www.skysports.com/rss/12040", RssCategory.SPORTS, "en", "GB"),
    RssService("The Guardian Sport", "https://www.theguardian.com/uk/sport/rss", RssCategory.SPORTS, "en", "GB"),
    RssService("Independent Sport", "http://www.independent.co.uk/sport/rss", RssCategory.SPORTS, "en", "GB")
)

private fun getCanadaRssServices(): List<RssService> = listOf(
    RssService("CBC News", "https://www.cbc.ca/cmlink/rss-topstories", RssCategory.NEWS, "en", "CA"),
    RssService("The Globe and Mail", "https://www.theglobeandmail.com/rss", RssCategory.NEWS, "en", "CA"),
    RssService("National Post", "https://nationalpost.com/feed", RssCategory.NEWS, "en", "CA"),
    RssService("TSN Sports", "https://www.tsn.ca/rss", RssCategory.SPORTS, "en", "CA"),
    RssService("Sportsnet", "https://www.sportsnet.ca/feed/", RssCategory.SPORTS, "en", "CA")
)

private fun getIndiaRssServices(): List<RssService> = listOf(
    RssService("The Times of India", "https://timesofindia.indiatimes.com/rssfeeds/-2128936835.cms", RssCategory.NEWS, "en", "IN"),
    RssService("Hindustan Times", "https://www.hindustantimes.com/feeds/rss/india-news/rssfeed.xml", RssCategory.NEWS, "en", "IN"),
    RssService("The Hindu", "https://www.thehindu.com/feeder/default.rss", RssCategory.NEWS, "en", "IN"),
    RssService("ESPN Cricinfo", "https://www.espncricinfo.com/rss/content/story/feeds/0.xml", RssCategory.SPORTS, "en", "IN"),
    RssService("Sportstar", "https://sportstar.thehindu.com/rss/sportstar.xml", RssCategory.SPORTS, "en", "IN")
)

private fun getAustraliaRssServices(): List<RssService> = listOf(
    RssService("ABC News Australia", "https://www.abc.net.au/news/feed/51120/rss.xml", RssCategory.NEWS, "en", "AU"),
    RssService("The Sydney Morning Herald", "https://www.smh.com.au/rss/feed.xml", RssCategory.NEWS, "en", "AU"),
    RssService("The Age", "https://www.theage.com.au/rss/feed.xml", RssCategory.NEWS, "en", "AU"),
    RssService("Fox Sports Australia", "https://www.foxsports.com.au/feed", RssCategory.SPORTS, "en", "AU"),
    RssService("ESPN Australia", "https://www.espn.com.au/espn/rss/news", RssCategory.SPORTS, "en", "AU")
)

private fun getEgyptRssServices(): List<RssService> = listOf(
    // News
    RssService("Al Ahram", "https://gate.ahram.org.eg/rss/97/", RssCategory.NEWS, "ar", "EG"),
    RssService("Egypt Independent", "https://www.egyptindependent.com/feed/", RssCategory.NEWS, "en", "EG"),
    RssService("Daily News Egypt", "https://dailynewsegypt.com/feed/", RssCategory.NEWS, "en", "EG"),
    RssService("Youm7", "https://www.youm7.com/rss/SectionRss?SectionID=65", RssCategory.NEWS, "ar", "EG"),
    RssService("Masrawy", "https://www.masrawy.com/rss/rss_topstories.aspx", RssCategory.NEWS, "ar", "EG"),
    // Sports
    RssService("KingFut", "https://www.kingfut.com/feed/", RssCategory.SPORTS, "en", "EG"),
    RssService("FilGoal", "https://www.filgoal.com/rss/", RssCategory.SPORTS, "ar", "EG")
)

private fun getFranceRssServices(): List<RssService> = listOf(
    RssService("Le Monde", "https://www.lemonde.fr/rss/une.xml", RssCategory.NEWS, "fr", "FR"),
    RssService("Le Figaro", "https://www.lefigaro.fr/rss/figaro_actualites.xml", RssCategory.NEWS, "fr", "FR"),
    RssService("France24", "https://www.france24.com/fr/rss", RssCategory.NEWS, "fr", "FR"),
    RssService("L'Équipe - Sports", "https://www.lequipe.fr/rss/actu_rss.xml", RssCategory.SPORTS, "fr", "FR"),
    RssService("Eurosport France", "https://www.eurosport.fr/rss.xml", RssCategory.SPORTS, "fr", "FR")
)

private fun getGermanyRssServices(): List<RssService> = listOf(
    RssService("Der Spiegel", "https://www.spiegel.de/schlagzeilen/tops/index.rss", RssCategory.NEWS, "de", "DE"),
    RssService("Die Welt", "https://www.welt.de/feeds/topnews.rss", RssCategory.NEWS, "de", "DE"),
    RssService("Tagesschau", "https://www.tagesschau.de/xml/rss2", RssCategory.NEWS, "de", "DE"),
    RssService("Kicker - Sports", "https://www.kicker.de/news.rss", RssCategory.SPORTS, "de", "DE"),
    RssService("Bild Sport", "https://www.bild.de/rssfeeds/vw-alles/vw-alles-26970192,sort=1,view=rss2.bild.xml", RssCategory.SPORTS, "de", "DE")
)

private fun getItalyRssServices(): List<RssService> = listOf(
    RssService("Corriere della Sera", "https://xml2.corriereobjects.it/rss/homepage.xml", RssCategory.NEWS, "it", "IT"),
    RssService("La Repubblica", "https://www.repubblica.it/rss/homepage/rss2.0.xml", RssCategory.NEWS, "it", "IT"),
    RssService("ANSA", "https://www.ansa.it/sito/ansait_rss.xml", RssCategory.NEWS, "it", "IT"),
    RssService("Gazzetta dello Sport", "https://www.gazzetta.it/rss/home.xml", RssCategory.SPORTS, "it", "IT"),
    RssService("Corriere dello Sport", "https://www.corrieredellosport.it/rss/", RssCategory.SPORTS, "it", "IT")
)

private fun getSpainRssServices(): List<RssService> = listOf(
    RssService("El País", "https://feeds.elpais.com/mrss-s/pages/ep/site/elpais.com/portada", RssCategory.NEWS, "es", "ES"),
    RssService("El Mundo", "https://e00-elmundo.uecdn.es/elmundo/rss/espana.xml", RssCategory.NEWS, "es", "ES"),
    RssService("ABC España", "https://www.abc.es/rss/feeds/abcPortada.xml", RssCategory.NEWS, "es", "ES"),
    RssService("Marca - Sports", "https://e00-marca.uecdn.es/rss/portada.xml", RssCategory.SPORTS, "es", "ES"),
    RssService("AS - Sports", "https://as.com/rss/tags/ultima_hora.xml", RssCategory.SPORTS, "es", "ES")
)

private fun getBrazilRssServices(): List<RssService> = listOf(
    RssService("Folha de S.Paulo", "https://feeds.folha.uol.com.br/emcimadahora/rss091.xml", RssCategory.NEWS, "pt", "BR"),
    RssService("O Globo", "https://oglobo.globo.com/rss.xml", RssCategory.NEWS, "pt", "BR"),
    RssService("Estadão", "https://feeds.folha.uol.com.br/poder/rss091.xml", RssCategory.NEWS, "pt", "BR"),
    RssService("Globo Esporte", "https://ge.globo.com/rss/feeds/home.xml", RssCategory.SPORTS, "pt", "BR"),
    RssService("Lance!", "https://www.lance.com.br/rss", RssCategory.SPORTS, "pt", "BR")
)

private fun getJapanRssServices(): List<RssService> = listOf(
    RssService("NHK News", "https://www3.nhk.or.jp/rss/news/cat0.xml", RssCategory.NEWS, "ja", "JP"),
    RssService("The Japan Times", "https://www.japantimes.co.jp/feed/", RssCategory.NEWS, "en", "JP"),
    RssService("Mainichi Shimbun", "https://mainichi.jp/rss/etc/flash.rss", RssCategory.NEWS, "ja", "JP"),
    RssService("Nikkan Sports", "https://www.nikkansports.com/rss/general.xml", RssCategory.SPORTS, "ja", "JP"),
    RssService("Sponichi Annex - Sports", "https://www.sponichi.co.jp/rss/index.html", RssCategory.SPORTS, "ja", "JP")
)

private fun getChinaRssServices(): List<RssService> = listOf(
    RssService("Xinhua News", "http://www.xinhuanet.com/english/rss/worldrss.xml", RssCategory.NEWS, "en", "CN"),
    RssService("China Daily", "https://www.chinadaily.com.cn/rss/china_rss.xml", RssCategory.NEWS, "en", "CN"),
    RssService("Global Times", "https://www.globaltimes.cn/rss/china.xml", RssCategory.NEWS, "en", "CN"),
    RssService("Sina Sports", "https://rss.sina.com.cn/sports/global/focus.xml", RssCategory.SPORTS, "zh", "CN"),
    RssService("Tencent Sports", "https://sports.qq.com/isocce/rss_isocce.xml", RssCategory.SPORTS, "zh", "CN")
)

private fun getRussiaRssServices(): List<RssService> = listOf(
    RssService("RT News", "https://www.rt.com/rss/news/", RssCategory.NEWS, "en", "RU"),
    RssService("TASS", "https://tass.com/rss/v2.xml", RssCategory.NEWS, "en", "RU"),
    RssService("Moscow Times", "https://www.themoscowtimes.com/rss/news", RssCategory.NEWS, "en", "RU"),
    RssService("Sport Express", "https://www.sport-express.ru/services/materials/news/rss/", RssCategory.SPORTS, "ru", "RU"),
    RssService("Championat", "https://www.championat.com/rss/news/", RssCategory.SPORTS, "ru", "RU")
)

private fun getSouthAfricaRssServices(): List<RssService> = listOf(
    RssService("News24", "https://feeds.24.com/articles/news24/TopStories/rss", RssCategory.NEWS, "en", "ZA"),
    RssService("IOL News", "https://www.iol.co.za/cmlink/1.640", RssCategory.NEWS, "en", "ZA"),
    RssService("TimesLIVE", "https://www.timeslive.co.za/rss", RssCategory.NEWS, "en", "ZA"),
    RssService("SuperSport", "https://www.supersport.com/rss/football", RssCategory.SPORTS, "en", "ZA"),
    RssService("KickOff", "https://www.kickoff.com/rss", RssCategory.SPORTS, "en", "ZA")
)

private fun getSaudiArabiaRssServices(): List<RssService> = listOf(
    RssService("Arab News", "https://www.arabnews.com/rss.xml", RssCategory.NEWS, "en", "SA"),
    RssService("Saudi Gazette", "https://saudigazette.com.sa/rss", RssCategory.NEWS, "en", "SA"),
    RssService("Al Riyadh", "https://www.alriyadh.com/section.main.xml", RssCategory.NEWS, "ar", "SA"),
    RssService("Kooora Saudi", "https://www.kooora.com/default.aspx?region=-10&rss=true", RssCategory.SPORTS, "ar", "SA"),
    RssService("Arriyadiyah", "https://arriyadiyah.com/rss", RssCategory.SPORTS, "ar", "SA")
)

private fun getUaeRssServices(): List<RssService> = listOf(
    RssService("The National", "https://www.thenationalnews.com/rss", RssCategory.NEWS, "en", "AE"),
    RssService("Gulf News", "https://gulfnews.com/rss", RssCategory.NEWS, "en", "AE"),
    RssService("Khaleej Times", "https://www.khaleejtimes.com/rss", RssCategory.NEWS, "en", "AE"),
    RssService("Kooora UAE", "https://www.kooora.com/default.aspx?region=-6&rss=true", RssCategory.SPORTS, "ar", "AE"),
    RssService("Sport360", "https://sport360.com/feed", RssCategory.SPORTS, "en", "AE")
)

private fun getTurkeyRssServices(): List<RssService> = listOf(
    RssService("Hürriyet", "https://www.hurriyet.com.tr/rss/anasayfa", RssCategory.NEWS, "tr", "TR"),
    RssService("Sabah", "https://www.sabah.com.tr/rss/gundem.xml", RssCategory.NEWS, "tr", "TR"),
    RssService("Daily Sabah", "https://www.dailysabah.com/rssFeed/1", RssCategory.NEWS, "en", "TR"),
    RssService("Fanatik - Sports", "https://www.fanatik.com.tr/rss", RssCategory.SPORTS, "tr", "TR"),
    RssService("NTV Spor", "https://www.ntvspor.net/rss", RssCategory.SPORTS, "tr", "TR")
)

private fun getArgentinaRssServices(): List<RssService> = listOf(
    RssService("Clarín", "https://www.clarin.com/rss/lo-ultimo/", RssCategory.NEWS, "es", "AR"),
    RssService("La Nación", "https://www.lanacion.com.ar/rss/canal/lo-ultimo/", RssCategory.NEWS, "es", "AR"),
    RssService("Infobae", "https://www.infobae.com/america/rss/", RssCategory.NEWS, "es", "AR"),
    RssService("Olé - Sports", "https://www.ole.com.ar/rss/ultimas-noticias/", RssCategory.SPORTS, "es", "AR"),
    RssService("TyC Sports", "https://www.tycsports.com/rss.html", RssCategory.SPORTS, "es", "AR")
)

private fun getMexicoRssServices(): List<RssService> = listOf(
    RssService("El Universal", "https://www.eluniversal.com.mx/rss", RssCategory.NEWS, "es", "MX"),
    RssService("Milenio", "https://www.milenio.com/rss", RssCategory.NEWS, "es", "MX"),
    RssService("Excélsior", "https://www.excelsior.com.mx/rss", RssCategory.NEWS, "es", "MX"),
    RssService("Record - Sports", "https://www.record.com.mx/rss.xml", RssCategory.SPORTS, "es", "MX"),
    RssService("MedioTiempo", "https://www.mediotiempo.com/rss", RssCategory.SPORTS, "es", "MX")
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
    RssService("Dagbladet", "https://www.dagbladet.no/rss.xml", RssCategory.NEWS, "no", "NO"),
    RssService("VG Sporten", "https://www.vg.no/rss/feed/?categories=sport", RssCategory.SPORTS, "no", "NO"),
    RssService("NRK Sport", "https://www.nrk.no/toppsaker.rss", RssCategory.SPORTS, "no", "NO")
)

private fun getDenmarkRssServices(): List<RssService> = listOf(
    RssService("Politiken", "https://politiken.dk/rss/senestenyt.rss", RssCategory.NEWS, "da", "DK"),
    RssService("Berlingske", "https://www.berlingske.dk/rss", RssCategory.NEWS, "da", "DK"),
    RssService("Jyllands-Posten", "https://jyllands-posten.dk/?service=rss", RssCategory.NEWS, "da", "DK"),
    RssService("Tipsbladet", "https://www.tipsbladet.dk/rss/alle", RssCategory.SPORTS, "da", "DK"),
    RssService("Ekstra Bladet Sport", "https://ekstrabladet.dk/rss/sport/", RssCategory.SPORTS, "da", "DK")
)

private fun getFinlandRssServices(): List<RssService> = listOf(
    RssService("Helsingin Sanomat", "https://www.hs.fi/rss/teasers/etusivu.xml", RssCategory.NEWS, "fi", "FI"),
    RssService("Ilta-Sanomat", "https://www.is.fi/rss/tuoreimmat.xml", RssCategory.NEWS, "fi", "FI"),
    RssService("Yle Uutiset", "https://feeds.yle.fi/uutiset/v1/recent.rss?publisherIds=YLE_UUTISET", RssCategory.NEWS, "fi", "FI"),
    RssService("Yle Urheilu", "https://feeds.yle.fi/urheilu/v1/recent.rss?publisherIds=YLE_URHEILU", RssCategory.SPORTS, "fi", "FI"),
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
    RssService("Le Soir", "https://www.lesoir.be/rss/section/actualite.xml", RssCategory.NEWS, "fr", "BE"),
    RssService("De Standaard", "https://www.standaard.be/rss/section/algemeen", RssCategory.NEWS, "nl", "BE"),
    RssService("Het Laatste Nieuws", "https://www.hln.be/rss.xml", RssCategory.NEWS, "nl", "BE"),
    RssService("Sporza", "https://sporza.be/rss.xml", RssCategory.SPORTS, "nl", "BE"),
    RssService("Het Nieuwsblad Sport", "https://www.nieuwsblad.be/rss/sport", RssCategory.SPORTS, "nl", "BE")
)

private fun getAustriaRssServices(): List<RssService> = listOf(
    // News
    RssService("Der Standard", "https://www.derstandard.at/rss", RssCategory.NEWS, "de", "AT"),
    RssService("Die Presse", "https://www.diepresse.com/rss/alles", RssCategory.NEWS, "de", "AT"),
    // Sports
    RssService("Sky Sport Austria", "https://sport.sky.at/rss", RssCategory.SPORTS, "de", "AT")
)

private fun getSwitzerlandRssServices(): List<RssService> = listOf(
    // News
    RssService("Swissinfo", "https://www.swissinfo.ch/rss?type=general", RssCategory.NEWS, "en", "CH"),
    RssService("Neue Zürcher Zeitung", "https://www.nzz.ch/rss", RssCategory.NEWS, "de", "CH"),
    // Sports
    RssService("SRF Sport", "https://www.srf.ch/rss/sport", RssCategory.SPORTS, "de", "CH")
)

private fun getPolandRssServices(): List<RssService> = listOf(
    // News
    RssService("Gazeta Wyborcza", "https://wyborcza.pl/0,0.rss", RssCategory.NEWS, "pl", "PL"),
    RssService("TVN24", "https://tvn24.pl/najnowsze.rss", RssCategory.NEWS, "pl", "PL"),
    // Sports
    RssService("Przegląd Sportowy", "https://www.przegladsportowy.pl/rss.xml", RssCategory.SPORTS, "pl", "PL")
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
    RssService("Origo", "https://www.origo.hu/rss", RssCategory.NEWS, "hu", "HU"),
    // Sports
    RssService("Nemzeti Sport", "https://www.nemzetisport.hu/rss", RssCategory.SPORTS, "hu", "HU")
)

private fun getGreeceRssServices(): List<RssService> = listOf(
    // News
    RssService("Kathimerini", "https://www.kathimerini.gr/rss/feed/", RssCategory.NEWS, "el", "GR"),
    RssService("Ekathimerini", "https://www.ekathimerini.com/rss", RssCategory.NEWS, "en", "GR"),
    // Sports
    RssService("Sport24.gr", "https://www.sport24.gr/rss", RssCategory.SPORTS, "el", "GR")
)

private fun getNigeriaRssServices(): List<RssService> = listOf(
    // News
    RssService("Punch", "https://punchng.com/feed/", RssCategory.NEWS, "en", "NG"),
    RssService("The Guardian Nigeria", "https://guardian.ng/feed/", RssCategory.NEWS, "en", "NG"),
    RssService("Vanguard News", "https://www.vanguardngr.com/feed/", RssCategory.NEWS, "en", "NG"),
    // Sports
    RssService("Complete Sports Nigeria", "https://www.completesports.com/feed/", RssCategory.SPORTS, "en", "NG")
)

private fun getKenyaRssServices(): List<RssService> = listOf(
    RssService("The Star - Kenya", "https://www.the-star.co.ke/rss.xml", RssCategory.NEWS, "en", "KE"),
    RssService("Nation - Kenya", "https://nation.africa/kenya/rss", RssCategory.NEWS, "en", "KE"),
    RssService("Standard Media - Kenya", "https://www.standardmedia.co.ke/rss/headlines.php", RssCategory.NEWS, "en", "KE"),
    RssService("Capital Sports - Kenya", "https://www.capitalfm.co.ke/sports/feed/", RssCategory.SPORTS, "en", "KE"),
    RssService("Nation Sports - Kenya", "https://nation.africa/kenya/sports/rss", RssCategory.SPORTS, "en", "KE")
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
