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
    RssService("Reuters", "http://feeds.reuters.com/reuters/topNews", RssCategory.NEWS, "en", "US"),
    RssService("Associated Press", "http://hosted.ap.org/lineups/TOPHEADS-rss_2.0.xml?SITE=AP&SECTION=HOME", RssCategory.NEWS, "en", "US"),
    RssService("NPR", "https://www.npr.org/rss/rss.php?id=1001", RssCategory.NEWS, "en", "US"),
    // Sports
    RssService("ESPN Top Headlines", "https://www.espn.com/espn/rss/news", RssCategory.SPORTS, "en", "US"),
    RssService("CBS Sports", "https://www.cbssports.com/rss/headlines/", RssCategory.SPORTS, "en", "US"),
    RssService("Yahoo Sports Top Headlines", "https://sports.yahoo.com/top/rss.xml", RssCategory.SPORTS, "en", "US"),
    RssService("Bleacher Report", "https://bleacherreport.com/articles/rss.xml", RssCategory.SPORTS, "en", "US"),
    // YouTube
    RssService("CNN", "https://www.youtube.com/feeds/videos.xml?channel_id=UCupvZG-5ko_eiXAupbDfxWw", RssCategory.NEWS, "en", "US"),
    RssService("Fox News", "https://www.youtube.com/feeds/videos.xml?channel_id=UCXIJgqnII2ZOINSWNOGFThA", RssCategory.NEWS, "en", "US"),
    RssService("MSNBC", "https://www.youtube.com/feeds/videos.xml?channel_id=UCaXkIU1QidjPwiAiafroGxw", RssCategory.NEWS, "en", "US"),
    RssService("NBC News", "https://www.youtube.com/feeds/videos.xml?channel_id=UCeY0bbntWzzVIaj2z3QigXg", RssCategory.NEWS, "en", "US"),
    RssService("ABC News", "https://www.youtube.com/feeds/videos.xml?channel_id=UCBi2mrWuNuyYy4gbM6fU18Q", RssCategory.NEWS, "en", "US")
)

private fun getUKRssServices(): List<RssService> = listOf(
    // News
    RssService("BBC News", "http://feeds.bbci.co.uk/news/rss.xml", RssCategory.NEWS, "en", "GB"),
    RssService("The Guardian Top Stories", "https://www.theguardian.com/uk/rss", RssCategory.NEWS, "en", "GB"),
    RssService("Independent UK", "http://www.independent.co.uk/news/uk/rss", RssCategory.NEWS, "en", "GB"),
    RssService("Sky News Top Stories", "http://feeds.skynews.com/feeds/rss/home.xml", RssCategory.NEWS, "en", "GB"),
    RssService("Reuters UK", "http://feeds.reuters.com/reuters/UKTopNews", RssCategory.NEWS, "en", "GB"),
    RssService("The Telegraph", "https://www.telegraph.co.uk/rss.xml", RssCategory.NEWS, "en", "GB"),
    RssService("The Sun", "https://www.thesun.co.uk/feed/", RssCategory.NEWS, "en", "GB"),
    RssService("Daily Mail", "https://www.dailymail.co.uk/home/index.rss", RssCategory.NEWS, "en", "GB"),
    // Sports
    RssService("BBC Sport", "http://feeds.bbci.co.uk/sport/rss.xml", RssCategory.SPORTS, "en", "GB"),
    RssService("Sky Sports News", "http://www.skysports.com/rss/12040", RssCategory.SPORTS, "en", "GB"),
    RssService("The Guardian Sport", "https://www.theguardian.com/uk/sport/rss", RssCategory.SPORTS, "en", "GB"),
    RssService("Independent Sport", "http://www.independent.co.uk/sport/rss", RssCategory.SPORTS, "en", "GB"),
    RssService("talkSPORT", "https://talksport.com/rss/football/feed", RssCategory.SPORTS, "en", "GB"),
    // YouTube
    RssService("BBC News", "https://www.youtube.com/feeds/videos.xml?channel_id=UC16niRr50-MSBwiO3YDb3RA", RssCategory.NEWS, "en", "GB"),
    RssService("Sky News", "https://www.youtube.com/feeds/videos.xml?channel_id=UCoMdktPbSTixAyNGwb-UYkQ", RssCategory.NEWS, "en", "GB"),
    RssService("The Guardian", "https://www.youtube.com/feeds/videos.xml?channel_id=UCIRYBXDze5krPDzAEOxFGVA", RssCategory.NEWS, "en", "GB"),
    RssService("ITV News", "https://www.youtube.com/feeds/videos.xml?channel_id=UC9-RFgzMIFmIeS1y1-b9Wpw", RssCategory.NEWS, "en", "GB")
)

private fun getCanadaRssServices(): List<RssService> = listOf(
    // News
    RssService("National Post", "https://nationalpost.com/feed", RssCategory.NEWS, "en", "CA"),
    RssService("CBC News", "https://www.cbc.ca/cmlink/rss-topstories", RssCategory.NEWS, "en", "CA"),
    RssService("CTV News", "https://www.ctvnews.ca/rss/ctvnews-ca-top-stories-public-rss-1.822009", RssCategory.NEWS, "en", "CA"),
    RssService("The Globe and Mail", "https://www.theglobeandmail.com/arc/outboundfeeds/rss/category/news/", RssCategory.NEWS, "en", "CA"),
    RssService("Toronto Star", "https://www.thestar.com/content/rss/topstories.xml", RssCategory.NEWS, "en", "CA"),
    // Sports
    RssService("Sportsnet", "https://www.sportsnet.ca/feed/", RssCategory.SPORTS, "en", "CA"),
    RssService("TSN", "https://www.tsn.ca/rss", RssCategory.SPORTS, "en", "CA"),
    // YouTube
    RssService("CBC News", "https://www.youtube.com/feeds/videos.xml?channel_id=UCuFFtG20a-I1_p2L4tJ7p4w", RssCategory.NEWS, "en", "CA"),
    RssService("Global News", "https://www.youtube.com/feeds/videos.xml?channel_id=UChLtXXpo4Ge1Rebe_4wBUAw", RssCategory.NEWS, "en", "CA"),
    RssService("CTV News", "https://www.youtube.com/feeds/videos.xml?channel_id=UCi7Zk9baY1tvdlgxIML8l4A", RssCategory.NEWS, "en", "CA")
)

private fun getIndiaRssServices(): List<RssService> = listOf(
    RssService("The Times of India", "https://timesofindia.indiatimes.com/rssfeeds/-2128936835.cms", RssCategory.NEWS, "en", "IN"),
    RssService("Hindustan Times", "https://www.hindustantimes.com/feeds/rss/india-news/rssfeed.xml", RssCategory.NEWS, "en", "IN"),
    RssService("The Hindu", "https://www.thehindu.com/feeder/default.rss", RssCategory.NEWS, "en", "IN"),
    RssService("NDTV", "http://feeds.feedburner.com/ndtvnews-top-stories", RssCategory.NEWS, "en", "IN"),
    RssService("The Indian Express", "https://indianexpress.com/feed/", RssCategory.NEWS, "en", "IN"),
    RssService("Firstpost", "https://www.firstpost.com/rss/india.xml", RssCategory.NEWS, "en", "IN"),
    RssService("ESPN Cricinfo", "https://www.espncricinfo.com/rss/content/story/feeds/0.xml", RssCategory.SPORTS, "en", "IN"),
    RssService("Zee News Sports", "http://zeenews.india.com/rss/sports-news.xml", RssCategory.SPORTS, "en", "IN"),
    RssService("Cricbuzz", "http://www.cricbuzz.com/cricket-rss/cricket-news.xml", RssCategory.SPORTS, "en", "IN"),
    RssService("Sportskeeda", "https://www.sportskeeda.com/feed", RssCategory.SPORTS, "en", "IN"),
    // YouTube
    RssService("NDTV", "https://www.youtube.com/feeds/videos.xml?channel_id=UCZFMm1mMw0F81Z37aaEzTUA", RssCategory.NEWS, "en", "IN"),
    RssService("India Today", "https://www.youtube.com/feeds/videos.xml?channel_id=UCYPvAwZP8pZhSMW8qs7cVCw", RssCategory.NEWS, "en", "IN"),
    RssService("WION", "https://www.youtube.com/feeds/videos.xml?channel_id=UC_gUM8rL-Lrg6O3adPW9K1g", RssCategory.NEWS, "en", "IN"),
    RssService("Aaj Tak", "https://www.youtube.com/feeds/videos.xml?channel_id=UCt4t-jeY85JegMlZ-E5UWtA", RssCategory.NEWS, "hi", "IN")
)

private fun getAustraliaRssServices(): List<RssService> = listOf(
    RssService("ABC News Australia", "https://www.abc.net.au/news/feed/51120/rss.xml", RssCategory.NEWS, "en", "AU"),
    RssService("The Sydney Morning Herald", "https://www.smh.com.au/rss/feed.xml", RssCategory.NEWS, "en", "AU"),
    RssService("The Age", "https://www.theage.com.au/rss/feed.xml", RssCategory.NEWS, "en", "AU"),
    RssService("news.com.au", "https://www.news.com.au/content-feeds/latest-news-national/", RssCategory.NEWS, "en", "AU"),
    RssService("ESPN Australia", "https://www.espn.com.au/espn/rss/news", RssCategory.SPORTS, "en", "AU"),
    RssService("Fox Sports Australia", "https://www.foxsports.com.au/fs/rss/news/all", RssCategory.SPORTS, "en", "AU"),
    // YouTube
    RssService("ABC News (Australia)", "https://www.youtube.com/feeds/videos.xml?channel_id=UCVgA3pr9Qi4B502iG_2gLSA", RssCategory.NEWS, "en", "AU"),
    RssService("7NEWS Australia", "https://www.youtube.com/feeds/videos.xml?channel_id=UC5T7D-Dh1eDGtsAFCuD84rA", RssCategory.NEWS, "en", "AU"),
    RssService("9 News Australia", "https://www.youtube.com/feeds/videos.xml?channel_id=UCp33-V_T1aB5o9L4Ym_b4rg", RssCategory.NEWS, "en", "AU")
)

private fun getEgyptRssServices(): List<RssService> = listOf(
    // News
    RssService("Youm7", "https://www.youm7.com/rss/SectionRss?SectionID=65", RssCategory.NEWS, "ar", "EG"),
    RssService("Sada Elbalad", "https://see.news/rss", RssCategory.NEWS, "ar", "EG"),
    RssService("Al-Ahram", "http://www.ahram.org.eg/Rss/2.aspx", RssCategory.NEWS, "ar", "EG"),
    RssService("Al-Masry Al-Youm", "https://www.almasryalyoum.com/rss/news", RssCategory.NEWS, "ar", "EG"),
    RssService("masrawy.com", "https://masrawy.com/rss", RssCategory.NEWS, "ar", "EG"),
    RssService("shorouknews.com", "https://shorouknews.com/rss", RssCategory.NEWS, "ar", "EG"),
    RssService("alwafd.news", "https://alwafd.news/rss.aspx", RssCategory.NEWS, "ar", "EG"),
    // Sports
    RssService("FilGoal", "https://www.filgoal.com/rss", RssCategory.SPORTS, "ar", "EG"),
    RssService("Yallakora", "https://www.yallakora.com/rss/news", RssCategory.SPORTS, "ar", "EG"),
    RssService("filgoal.com", "https://filgoal.com/feeds/news", RssCategory.SPORTS, "ar", "EG"),
    RssService("yallakora.com", "https://yallakora.com/rss", RssCategory.SPORTS, "ar", "EG"),
    // YouTube
    RssService("Al Jazeera Mubasher", "https://www.youtube.com/feeds/videos.xml?channel_id=UCfiwzLy-8yKzIbsmZTzxDgw", RssCategory.NEWS, "ar", "EG"),
    RssService("BBC News عربي", "https://www.youtube.com/feeds/videos.xml?channel_id=UCeTA_Al5_1SCSdqb_J0H2vA", RssCategory.NEWS, "ar", "EG"),
    RssService("Sky News Arabia", "https://www.youtube.com/feeds/videos.xml?channel_id=UCBPb3f1Qf0P6g_j1i0vL62A", RssCategory.NEWS, "ar", "EG")
)

private fun getFranceRssServices(): List<RssService> = listOf(
    // News
    RssService("Le Monde", "https://www.lemonde.fr/rss/une.xml", RssCategory.NEWS, "fr", "FR"),
    RssService("Le Figaro", "https://www.lefigaro.fr/rss/figaro_actualites.xml", RssCategory.NEWS, "fr", "FR"),
    RssService("France 24", "https://www.france24.com/fr/rss", RssCategory.NEWS, "fr", "FR"),
    RssService("20 Minutes", "https://www.20minutes.fr/feeds/rss-une.xml", RssCategory.NEWS, "fr", "FR"),
    RssService("L'Express", "https://www.lexpress.fr/rss/alaune.xml", RssCategory.NEWS, "fr", "FR"),
    // Sports
    RssService("L'Équipe", "https://www.lequipe.fr/rss/actu_rss.xml", RssCategory.SPORTS, "fr", "FR"),
    RssService("RMC Sport", "https://rmcsport.bfmtv.com/rss/info/flux-rss/flux-toutes-les-actualites/", RssCategory.SPORTS, "fr", "FR"),
    // YouTube
    RssService("FRANCE 24", "https://www.youtube.com/feeds/videos.xml?channel_id=UCCCPCZNChQdGa9EkATeye4g", RssCategory.NEWS, "fr", "FR"),
    RssService("Le Monde", "https://www.youtube.com/feeds/videos.xml?channel_id=UCYpRD2-t73_9E0cq5M-OO4A", RssCategory.NEWS, "fr", "FR"),
    RssService("Brut", "https://www.youtube.com/feeds/videos.xml?channel_id=UCK7WvGZ3uMyqB5d2-b2rYpA", RssCategory.NEWS, "fr", "FR"),
    RssService("L'ÉQUIPE", "https://www.youtube.com/feeds/videos.xml?channel_id=UCp1_Ld8vC-I4W2v2s2gQc_w", RssCategory.SPORTS, "fr", "FR")
)

private fun getGermanyRssServices(): List<RssService> = listOf(
    RssService("Der Spiegel", "https://www.spiegel.de/schlagzeilen/tops/index.rss", RssCategory.NEWS, "de", "DE"),
    RssService("Die Welt", "https://www.welt.de/feeds/topnews.rss", RssCategory.NEWS, "de", "DE"),
    RssService("Tagesschau", "https://www.tagesschau.de/xml/rss2", RssCategory.NEWS, "de", "DE"),
    RssService("Süddeutsche Zeitung", "https://rss.sueddeutsche.de/rss/Topthemen", RssCategory.NEWS, "de", "DE"),
    RssService("Frankfurter Allgemeine Zeitung", "https://www.faz.net/rss/aktuell/", RssCategory.NEWS, "de", "DE"),
    RssService("kicker.de", "http://rss.kicker.de/news/fussball", RssCategory.SPORTS, "de", "DE"),
    RssService("Sportschau ARD", "http://www.sportschau.de/sendung/sportschausendungindex100.feed", RssCategory.SPORTS, "de", "DE"),
    RssService("Sport1", "https://www.sport1.de/news.rss", RssCategory.SPORTS, "de", "DE"),
    // YouTube
    RssService("tagesschau", "https://www.youtube.com/feeds/videos.xml?channel_id=UC5NOEUbkLheQcaaRldYW5GA", RssCategory.NEWS, "de", "DE"),
    RssService("DER SPIEGEL", "https://www.youtube.com/feeds/videos.xml?channel_id=UC1JTaVpQhG1L1P4_K2Wde2g", RssCategory.NEWS, "de", "DE"),
    RssService("BILD", "https://www.youtube.com/feeds/videos.xml?channel_id=UCb_9d2hGGu1b2a_NEg3wXAg", RssCategory.NEWS, "de", "DE"),
    RssService("sportstudio", "https://www.youtube.com/feeds/videos.xml?channel_id=UC26AfgC31Jg_G_cTj2pY-Xg", RssCategory.SPORTS, "de", "DE")
)

private fun getItalyRssServices(): List<RssService> = listOf(
    RssService("La Repubblica", "https://www.repubblica.it/rss/homepage/rss2.0.xml", RssCategory.NEWS, "it", "IT"),
    RssService("ANSA", "https://www.ansa.it/sito/ansait_rss.xml", RssCategory.NEWS, "it", "IT"),
    RssService("Corriere dello Sport", "https://www.corrieredellosport.it/rss/", RssCategory.SPORTS, "it", "IT"),
    RssService("La Gazzetta dello Sport", "https://www.gazzetta.it/rss/", RssCategory.SPORTS, "it", "IT"),
    RssService("Tuttosport", "https://www.tuttosport.com/rss", RssCategory.SPORTS, "it", "IT"),
    // YouTube
    RssService("Sky Sport", "https://www.youtube.com/feeds/videos.xml?channel_id=UCy0n6j52lt4K2cI_5B_0p_g", RssCategory.SPORTS, "it", "IT"),
    RssService("Rai", "https://www.youtube.com/feeds/videos.xml?channel_id=UC44UaY-4c85z_H2_bLwXo-w", RssCategory.NEWS, "it", "IT"),
    RssService("la Repubblica", "https://www.youtube.com/feeds/videos.xml?channel_id=UCd25X4VcwBE5o5p6I4T_n4g", RssCategory.NEWS, "it", "IT")
)

private fun getSpainRssServices(): List<RssService> = listOf(
    RssService("El País", "https://feeds.elpais.com/mrss-s/pages/ep/site/elpais.com/portada", RssCategory.NEWS, "es", "ES"),
    RssService("El Mundo", "https://e00-elmundo.uecdn.es/elmundo/rss/espana.xml", RssCategory.NEWS, "es", "ES"),
    RssService("ABC", "http://www.abc.es/rss/feeds/abc_EspanaEspana.xml", RssCategory.NEWS, "es", "ES"),
    RssService("La Vanguardia", "http://www.lavanguardia.com/rss/home.xml", RssCategory.NEWS, "es", "ES"),
    RssService("Marca", "https://www.marca.com/rss/portada.xml", RssCategory.SPORTS, "es", "ES"),
    RssService("AS", "https://as.com/rss/tags/ultimas_noticias.xml", RssCategory.SPORTS, "es", "ES"),
    RssService("Mundo Deportivo", "https://www.mundodeportivo.com/rss/portada.xml", RssCategory.SPORTS, "es", "ES"),
    // YouTube
    RssService("EL PAÍS", "https://www.youtube.com/feeds/videos.xml?channel_id=UCnsvjht6-iP3S8d1m7wG2yA", RssCategory.NEWS, "es", "ES"),
    RssService("RTVE", "https://www.youtube.com/feeds/videos.xml?channel_id=UC7QZIf0dta-XPX2i9XU_Y-A", RssCategory.NEWS, "es", "ES"),
    RssService("MARCA", "https://www.youtube.com/feeds/videos.xml?channel_id=UCi-gD2ev15n_iAnj_m2W_Zw", RssCategory.SPORTS, "es", "ES")
)

private fun getBrazilRssServices(): List<RssService> = listOf(
    RssService("Folha de S.Paulo", "https://feeds.folha.uol.com.br/emcimadahora/rss091.xml", RssCategory.NEWS, "pt", "BR"),
    RssService("Estadão", "https://feeds.folha.uol.com.br/poder/rss091.xml", RssCategory.NEWS, "pt", "BR"),
    RssService("O Globo", "https://oglobo.globo.com/rss/oglobo", RssCategory.NEWS, "pt", "BR"),
    RssService("G1", "https://g1.globo.com/rss/g1/", RssCategory.NEWS, "pt", "BR"),
    RssService("globoesporte.com", "https://ge.globo.com/rss/ge", RssCategory.SPORTS, "pt", "BR"),
    RssService("UOL Esporte", "https://rss.uol.com.br/esporte/ultimas/index.xml", RssCategory.SPORTS, "pt", "BR"),
    RssService("Lance!", "https://www.lance.com.br/feed.xml", RssCategory.SPORTS, "pt", "BR"),
    // YouTube
    RssService("CNN Brasil", "https://www.youtube.com/feeds/videos.xml?channel_id=UCvdwhh_fDyG8LMR4a2rIQEw", RssCategory.NEWS, "pt", "BR"),
    RssService("Jovem Pan News", "https://www.youtube.com/feeds/videos.xml?channel_id=UC-S-9B2s0aJ_iASLgY_4g2Q", RssCategory.NEWS, "pt", "BR"),
    RssService("Band Jornalismo", "https://www.youtube.com/feeds/videos.xml?channel_id=UCoa-D_VfMkFr_4-7f8o_1gA", RssCategory.NEWS, "pt", "BR")
)

private fun getJapanRssServices(): List<RssService> = listOf(
    RssService("NHK News", "https://www3.nhk.or.jp/rss/news/cat0.xml", RssCategory.NEWS, "ja", "JP"),
    RssService("The Japan Times", "https://www.japantimes.co.jp/feed/", RssCategory.NEWS, "en", "JP"),
    RssService("Mainichi Shimbun", "https://mainichi.jp/rss/etc/flash.rss", RssCategory.NEWS, "ja", "JP"),
    RssService("Asahi Shimbun", "http://www.asahi.com/rss/asahi/newsheadlines.rdf", RssCategory.NEWS, "ja", "JP"),
    RssService("Nikkei", "https://www.nikkei.com/rss/index.xml", RssCategory.NEWS, "ja", "JP"),
    RssService("Yomiuri Shimbun", "https://www.yomiuri.co.jp/rss/yol/topstories", RssCategory.NEWS, "ja", "JP"),
    RssService("Sports Hochi", "https://hochi.news/rss/all/feed.xml", RssCategory.SPORTS, "ja", "JP"),
    // YouTube
    RssService("ANNnewsCH", "https://www.youtube.com/feeds/videos.xml?channel_id=UCuTAXTexrhetbOe3U5s67GA", RssCategory.NEWS, "ja", "JP"),
    RssService("FNNプライムオンライン", "https://www.youtube.com/feeds/videos.xml?channel_id=UCoQBJMzcwmC_I-sK29_ClsA", RssCategory.NEWS, "ja", "JP")
)

private fun getChinaRssServices(): List<RssService> = listOf(
    // News
    RssService("Xinhua News", "http://www.xinhuanet.com/english/rss/worldrss.xml", RssCategory.NEWS, "en", "CN"),
    RssService("People's Daily", "http://en.people.cn/rss/world.xml", RssCategory.NEWS, "en", "CN"),
    // Sports
    RssService("Sina Sports", "http://rss.sina.com.cn/sports/global/focus.xml", RssCategory.SPORTS, "zh", "CN"),
    // YouTube
    RssService("CGTN", "https://www.youtube.com/feeds/videos.xml?channel_id=UC-3_a_5G3z-kZ_cbhT6h_Gg", RssCategory.NEWS, "en", "CN"),
    RssService("New China TV", "https://www.youtube.com/feeds/videos.xml?channel_id=UCi5Qv6s_mK4i1e52Jb_0s9g", RssCategory.NEWS, "en", "CN")
)

private fun getRussiaRssServices(): List<RssService> = listOf(
    RssService("RT News", "https://www.rt.com/rss/news/", RssCategory.NEWS, "en", "RU"),
    RssService("Moscow Times", "https://www.themoscowtimes.com/rss/news", RssCategory.NEWS, "en", "RU"),
    RssService("TASS", "http://tass.com/rss/v2.xml", RssCategory.NEWS, "en", "RU"),
    RssService("Izvestia", "https://iz.ru/xml/rss/all.xml", RssCategory.NEWS, "ru", "RU"),
    RssService("Championat", "https://www.championat.com/rss/news/", RssCategory.SPORTS, "ru", "RU"),
    RssService("Sport-Express", "https://www.sport-express.ru/rss/", RssCategory.SPORTS, "ru", "RU"),
    // YouTube
    RssService("RT", "https://www.youtube.com/feeds/videos.xml?channel_id=UCpwvZwUam-URkxB7g4USKpg", RssCategory.NEWS, "en", "RU"),
    RssService("Россия 24", "https://www.youtube.com/feeds/videos.xml?channel_id=UC_IEcnNeHc_bwd92aa1m3rA", RssCategory.NEWS, "ru", "RU")
)

private fun getSouthAfricaRssServices(): List<RssService> = listOf(
    RssService("News24", "https://feeds.24.com/articles/news24/TopStories/rss", RssCategory.NEWS, "en", "ZA"),
    RssService("TimesLIVE", "https://www.timeslive.co.za/rss", RssCategory.NEWS, "en", "ZA"),
    RssService("KickOff", "https://www.kickoff.com/rss", RssCategory.SPORTS, "en", "ZA"),
    RssService("SuperSport", "https://www.supersport.com/rss", RssCategory.SPORTS, "en", "ZA"),
    RssService("IOL Sport", "https://www.iol.co.za/sport?service=rss", RssCategory.SPORTS, "en", "ZA"),
    // YouTube
    RssService("SABC News", "https://www.youtube.com/feeds/videos.xml?channel_id=UC8yH-uI81UUtEMDsowQyx1g", RssCategory.NEWS, "en", "ZA"),
    RssService("eNCA", "https://www.youtube.com/feeds/videos.xml?channel_id=UCp-y1-d_ea_g-e29B40_3pQ", RssCategory.NEWS, "en", "ZA")
)

private fun getSaudiArabiaRssServices(): List<RssService> = listOf(
    // News
    RssService("Al Jazeera", "https://www.aljazeera.com/xml/rss/all.xml", RssCategory.NEWS, "en", "SA"),
    RssService("Al Arabiya", "https://english.alarabiya.net/tools/rss/all_news.xml", RssCategory.NEWS, "en", "SA"),
    RssService("Al Arabiya", "https://www.youm7.com/rss/SectionRss?SectionID=88", RssCategory.NEWS, "en", "SA"),
    // Sports
    RssService("Arriyadiyah", "https://saudigazette.com.sa/rssFeed/0", RssCategory.SPORTS, "ar", "SA"),
    RssService("Arriyadiyah", "https://saudigazette.com.sa/rssFeed/212", RssCategory.SPORTS, "ar", "SA"),
    RssService("Arriyadiyah", "https://saudigazette.com.sa/rssFeed/45", RssCategory.SPORTS, "ar", "SA"),
    RssService("Arriyadiyah", "https://saudigazette.com.sa/rssFeed/74", RssCategory.SPORTS, "ar", "SA"),
    RssService("Arriyadiyah", "https://arriyadiyah.com/rss", RssCategory.SPORTS, "ar", "SA"),
    // YouTube
    RssService("Al Arabiya", "https://www.youtube.com/feeds/videos.xml?channel_id=UC4k1j_h8-15d5_2A826p4wQ", RssCategory.NEWS, "ar", "SA"),
    RssService("Al Ekhbariya", "https://www.youtube.com/feeds/videos.xml?channel_id=UCnB4aK_4-Q-9sWgxxJ42P2g", RssCategory.NEWS, "ar", "SA")
)

private fun getUaeRssServices(): List<RssService> = listOf(
    // News
    RssService("Lovin Dubai", "https://lovin.co/dubai/en/news/feed/", RssCategory.NEWS, "en", "AE"),
    RssService("What's On", "https://whatson.ae/feed/", RssCategory.NEWS, "en", "AE"),
    RssService("What's On", "https://www.youm7.com/rss/SectionRss?SectionID=88", RssCategory.NEWS, "en", "AE"),
    RssService("Gulf News", "https://gulfnews.com/feed", RssCategory.NEWS, "en", "AE"),
    RssService("Khaleej Times", "https://www.khaleejtimes.com/rss", RssCategory.NEWS, "en", "AE"),
    // Sports
    RssService("Dubai Sports", "https://www.dubaisports.ae/rss", RssCategory.SPORTS, "ar", "AE"),
    // YouTube
    RssService("Dubai TV", "https://www.youtube.com/feeds/videos.xml?channel_id=UCaT4E-Qa_v-12hE-e2YgA6A", RssCategory.NEWS, "ar", "AE"),
    RssService("Abu Dhabi TV", "https://www.youtube.com/feeds/videos.xml?channel_id=UCzt4-25-26a4GkQW_GjU-DA", RssCategory.NEWS, "ar", "AE")
)

private fun getTurkeyRssServices(): List<RssService> = listOf(
    RssService("Hürriyet", "https://www.hurriyet.com.tr/rss/anasayfa", RssCategory.NEWS, "tr", "TR"),
    RssService("Sabah", "https://www.sabah.com.tr/rss/gundem.xml", RssCategory.NEWS, "tr", "TR"),
    RssService("Daily Sabah", "https://www.dailysabah.com/rssFeed/1", RssCategory.NEWS, "en", "TR"),
    RssService("Haber Turk", "http://www.haberturk.com/haberturk.xml", RssCategory.NEWS, "tr", "TR"),
    RssService("FotoMac", "http://www.fotomac.com.tr/rss/besiktas.xml", RssCategory.SPORTS, "tr", "TR"),
    RssService("NTV Spor", "https://www.ntvspor.net/rss", RssCategory.SPORTS, "tr", "TR"),
    RssService("Fanatik", "https://www.fanatik.com.tr/rss", RssCategory.SPORTS, "tr", "TR"),
    // YouTube
    RssService("NTV", "https://www.youtube.com/feeds/videos.xml?channel_id=UCn-p_03se_G3O5_P_J4_cSA", RssCategory.NEWS, "tr", "TR"),
    RssService("Habertürk", "https://www.youtube.com/feeds/videos.xml?channel_id=UCc_34VLE62Tdo-wI5A23a-A", RssCategory.NEWS, "tr", "TR"),
    RssService("TRT Haber", "https://www.youtube.com/feeds/videos.xml?channel_id=UC7-x8hJ3_f9DRd4-P8o_HAQ", RssCategory.NEWS, "tr", "TR")
)

private fun getArgentinaRssServices(): List<RssService> = listOf(
    RssService("Clarín", "https://www.clarin.com/rss/lo-ultimo/", RssCategory.NEWS, "es", "AR"),
    RssService("La Nacion", "https://www.lanacion.com.ar/arc/outboundfeeds/rss/?outputType=xml", RssCategory.NEWS, "es", "AR"),
    RssService("Infobae", "https://www.infobae.com/arc/outboundfeeds/rss/", RssCategory.NEWS, "es", "AR"),
    RssService("Pagina/12", "https://www.pagina12.com.ar/rss/portada", RssCategory.NEWS, "es", "AR"),
    RssService("Olé - Sports", "https://www.ole.com.ar/rss/ultimas-noticias/", RssCategory.SPORTS, "es", "AR"),
    RssService("TyC Sports", "https://www.tycsports.com/rss.xml", RssCategory.SPORTS, "es", "AR"),
    RssService("La Capital", "https://www.lacapital.com.ar/rss/home.xml", RssCategory.NEWS, "es", "AR"),
    // YouTube
    RssService("TELEFE NOTICIAS", "https://www.youtube.com/feeds/videos.xml?channel_id=UC8ELFH_YkI-XGj8Yd9yB_yQ", RssCategory.NEWS, "es", "AR"),
    RssService("TN", "https://www.youtube.com/feeds/videos.xml?channel_id=UCj6PcyLvpnIRT_2W_mwa9Aw", RssCategory.NEWS, "es", "AR")
)

private fun getMexicoRssServices(): List<RssService> = listOf(
    RssService("Milenio", "https://www.milenio.com/rss", RssCategory.NEWS, "es", "MX"),
    RssService("Excélsior", "https://www.excelsior.com.mx/rss", RssCategory.NEWS, "es", "MX"),
    RssService("Record - Sports", "https://www.record.com.mx/rss.xml", RssCategory.SPORTS, "es", "MX"),
    RssService("MedioTiempo", "https://www.mediotiempo.com/rss.xml", RssCategory.SPORTS, "es", "MX"),
    RssService("TUDN", "https://www.tudn.com/rss", RssCategory.SPORTS, "es", "MX"),
    // YouTube
    RssService("MILENIO", "https://www.youtube.com/feeds/videos.xml?channel_id=UCFxHplbCo4LCR3-CCBr-iVw", RssCategory.NEWS, "es", "MX"),
    RssService("Noticieros Televisa", "https://www.youtube.com/feeds/videos.xml?channel_id=UC_Rk1hD-19h8s2i4G9-d_UA", RssCategory.NEWS, "es", "MX")
)

private fun getSwedenRssServices(): List<RssService> = listOf(
    RssService("Dagens Nyheter", "https://www.dn.se/rss/", RssCategory.NEWS, "sv", "SE"),
    RssService("Svenska Dagbladet", "https://www.svd.se/?service=rss", RssCategory.NEWS, "sv", "SE"),
    RssService("Aftonbladet", "https://rss.aftonbladet.se/rss2/small/pages/sections/senastenytt/", RssCategory.NEWS, "sv", "SE"),
    RssService("Expressen", "http://www.expressen.se/rss/nyheter", RssCategory.NEWS, "sv", "SE"),
    RssService("SVT Nyheter", "https://www.svt.se/nyheter/rss.xml", RssCategory.NEWS, "sv", "SE"),
    RssService("Fotbollskanalen", "https://www.fotbollskanalen.se/rss/", RssCategory.SPORTS, "sv", "SE"),
    RssService("Sportbladet", "https://rss.aftonbladet.se/rss2/small/pages/sections/sportbladet/", RssCategory.SPORTS, "sv", "SE"),
    RssService("SVT Sport", "https://www.svt.se/sport/rss.xml", RssCategory.SPORTS, "sv", "SE"),
    // YouTube
    RssService("SVT Nyheter", "https://www.youtube.com/feeds/videos.xml?channel_id=UC7-wI33JE8IC3d-1v5D_p-w", RssCategory.NEWS, "sv", "SE"),
    RssService("TV4Nyheterna", "https://www.youtube.com/feeds/videos.xml?channel_id=UCC6-3Xy3jG_Y-3J-8SgHjlw", RssCategory.NEWS, "sv", "SE")
)

private fun getNorwayRssServices(): List<RssService> = listOf(
    RssService("Aftenposten", "https://www.aftenposten.no/rss", RssCategory.NEWS, "no", "NO"),
    RssService("VG", "https://www.vg.no/rss/feed/", RssCategory.NEWS, "no", "NO"),
    RssService("Dagbladet", "http://www.dagbladet.no/rss/nyheter/", RssCategory.NEWS, "no", "NO"),
    RssService("NRK Nyheter", "https://www.nrk.no/nyheter/siste.rss", RssCategory.NEWS, "no", "NO"),
    RssService("TV2 Nyheter", "http://www.tv2.no/rss/nyheter/", RssCategory.NEWS, "no", "NO"),
    RssService("VG Sporten", "https://www.vg.no/rss/feed/?categories=sport", RssCategory.SPORTS, "no", "NO"),
    RssService("NRK Sport", "https://www.nrk.no/toppsaker.rss", RssCategory.SPORTS, "no", "NO"),
    RssService("TV 2 Sporten", "http://www.tv2.no/rss/sport/", RssCategory.SPORTS, "no", "NO"),
    // YouTube
    RssService("NRK", "https://www.youtube.com/feeds/videos.xml?channel_id=UC2_so_2F8nJeG3e32K3eT5g", RssCategory.NEWS, "no", "NO"),
    RssService("TV 2", "https://www.youtube.com/feeds/videos.xml?channel_id=UCQ-8Lw2iYyB_6a_s-FvD_tg", RssCategory.NEWS, "no", "NO")
)

private fun getDenmarkRssServices(): List<RssService> = listOf(
    RssService("Politiken", "https://politiken.dk/rss/senestenyt.rss", RssCategory.NEWS, "da", "DK"),
    RssService("Ekstra Bladet", "http://ekstrabladet.dk/rssfeed/all/", RssCategory.NEWS, "da", "DK"),
    RssService("BT", "http://www.bt.dk/bt/seneste/rss", RssCategory.NEWS, "da", "DK"),
    RssService("DR Nyheder", "https://www.dr.dk/nyheder/service/feeds/allenyheder", RssCategory.NEWS, "da", "DK"),
    RssService("TV 2 Sport", "https://sport.tv2.dk/rss", RssCategory.SPORTS, "da", "DK"),
    // YouTube
    RssService("DR Nyheder", "https://www.youtube.com/feeds/videos.xml?channel_id=UCYpcjIaV5Bv5D0y0i_wA-eg", RssCategory.NEWS, "da", "DK"),
    RssService("TV 2 NYHEDERNE", "https://www.youtube.com/feeds/videos.xml?channel_id=UCcPrr22-rG_x49d5I65o-3g", RssCategory.NEWS, "da", "DK")
)

private fun getFinlandRssServices(): List<RssService> = listOf(
    RssService("Helsingin Sanomat", "https://www.hs.fi/rss/teasers/etusivu.xml", RssCategory.NEWS, "fi", "FI"),
    RssService("Ilta-Sanomat", "https://www.is.fi/rss/tuoreimmat.xml", RssCategory.NEWS, "fi", "FI"),
    RssService("Yle Uutiset", "https://feeds.yle.fi/uutiset/v1/recent.rss?publisherIds=YLE_UUTISET", RssCategory.NEWS, "fi", "FI"),
    RssService("Ilta-Sanomat Sport", "https://www.is.fi/rss/urheilu.xml", RssCategory.SPORTS, "fi", "FI"),
    RssService("Yle Urheilu", "https://feeds.yle.fi/urheilu/v1/recent.rss?publisherIds=YLE_URHEILU", RssCategory.SPORTS, "fi", "FI"),
    RssService("MTV Uutiset", "https://www.mtvuutiset.fi/rss/uutiset.xml", RssCategory.NEWS, "fi", "FI"),
    // YouTube
    RssService("Yle Uutiset", "https://www.youtube.com/feeds/videos.xml?channel_id=UCVL2W-8p8dJp_1g_jI4i_Lg", RssCategory.NEWS, "fi", "FI"),
    RssService("MTV Uutiset", "https://www.youtube.com/feeds/videos.xml?channel_id=UCk-L23Gg63Q_S_d_A4c_vBw", RssCategory.NEWS, "fi", "FI")
)

private fun getNetherlandsRssServices(): List<RssService> = listOf(
    RssService("NOS Nieuws", "https://feeds.nos.nl/nosnieuwsalgemeen", RssCategory.NEWS, "nl", "NL"),
    RssService("De Telegraaf", "https://www.telegraaf.nl/rss", RssCategory.NEWS, "nl", "NL"),
    RssService("NRC", "https://www.nrc.nl/rss/", RssCategory.NEWS, "nl", "NL"),
    RssService("AD.nl", "http://www.ad.nl/rss.xml", RssCategory.NEWS, "nl", "NL"),
    RssService("NU.nl", "http://www.nu.nl/rss/Algemeen", RssCategory.NEWS, "nl", "NL"),
    RssService("Voetbal International", "https://www.vi.nl/rss", RssCategory.SPORTS, "nl", "NL"),
    RssService("AD Sportwereld", "https://www.ad.nl/sport/rss.xml", RssCategory.SPORTS, "nl", "NL"),
    RssService("NOS Sport", "https://feeds.nos.nl/nossportalgemeen", RssCategory.SPORTS, "nl", "NL"),
    RssService("De Telegraaf Sport", "https://www.telegraaf.nl/sport/rss", RssCategory.SPORTS, "nl", "NL"),
    // YouTube
    RssService("NOS", "https://www.youtube.com/feeds/videos.xml?channel_id=UC5gx_1c9bS3g0a-2hS-n-YQ", RssCategory.NEWS, "nl", "NL"),
    RssService("RTL Nieuws", "https://www.youtube.com/feeds/videos.xml?channel_id=UCr5_R_5seA3b3b-1n4vYwBg", RssCategory.NEWS, "nl", "NL")
)

private fun getBelgiumRssServices(): List<RssService> = listOf(
    RssService("Het Laatste Nieuws", "https://www.hln.be/rss.xml", RssCategory.NEWS, "nl", "BE"),
    RssService("De Standaard", "https://www.standaard.be/rss", RssCategory.NEWS, "nl", "BE"),
    RssService("De Morgen", "https://demorgen.be/in-het-nieuws/rss.xml", RssCategory.NEWS, "nl", "BE"),
    RssService("La Libre", "https://www.lalibre.be/rss", RssCategory.NEWS, "fr", "BE"),
    RssService("Sporza", "https://sporza.be/rss", RssCategory.SPORTS, "nl", "BE"),
    RssService("DH Les Sports+", "https://www.dhnet.be/rss/section/sports.xml", RssCategory.SPORTS, "fr", "BE"),
    // YouTube
    RssService("VRT NWS", "https://www.youtube.com/feeds/videos.xml?channel_id=UCV_2Dwb_V5Qge3d63E7p0qA", RssCategory.NEWS, "nl", "BE"),
    RssService("RTL info", "https://www.youtube.com/feeds/videos.xml?channel_id=UCwG3sWyl3aH-aIIi_1o2-XA", RssCategory.NEWS, "fr", "BE")
)

private fun getAustriaRssServices(): List<RssService> = listOf(
    // News
    RssService("Der Standard", "https://www.derstandard.at/rss", RssCategory.NEWS, "de", "AT"),
    RssService("Krone.at", "http://www.krone.at/Nachrichten/rss.html", RssCategory.NEWS, "de", "AT"),
    RssService("Kurier", "http://kurier.at/xml/rss", RssCategory.NEWS, "de", "AT"),
    RssService("ORF.at", "http://rss.orf.at/news.xml", RssCategory.NEWS, "de", "AT"),
    // Sports
    RssService("LAOLA1.at", "https://www.laola1.at/templates/generated/1/xml/rss/newsRSS.xml", RssCategory.SPORTS, "de", "AT"),
    RssService("ORF Sport Plus", "http://rss.orf.at/sport.xml", RssCategory.SPORTS, "de", "AT"),
    RssService("Kronen Zeitung Sport", "http://www.krone.at/Sport/rss.html", RssCategory.SPORTS, "de", "AT"),
    // YouTube
    RssService("ORF", "https://www.youtube.com/feeds/videos.xml?channel_id=UCX_U1s1o_n0R0b408i_b-mA", RssCategory.NEWS, "de", "AT"),
    RssService("krone.tv", "https://www.youtube.com/feeds/videos.xml?channel_id=UCY-f2yVz6a_y_fS_f_s_f_w", RssCategory.NEWS, "de", "AT")
)

private fun getSwitzerlandRssServices(): List<RssService> = listOf(
    // News
    RssService("Tages Anzeiger", "http://www.tagesanzeiger.ch/rss.html", RssCategory.NEWS, "de", "CH"),
    RssService("Neue Zuercher Zeitung", "http://www.nzz.ch/startseite.rss", RssCategory.NEWS, "de", "CH"),
    RssService("Blick", "https://www.blick.ch/rss.xml", RssCategory.NEWS, "de", "CH"),
    // Sports
    RssService("SRF Sport", "http://www.srf.ch/sport/bnf/rss/718", RssCategory.SPORTS, "de", "CH"),
    RssService("RTS Sport", "https://www.rts.ch/sport/rss", RssCategory.SPORTS, "fr", "CH"),
    // YouTube
    RssService("SRF News", "https://www.youtube.com/feeds/videos.xml?channel_id=UCY_Q_h-32Z3b_y-a_Xg_X_w", RssCategory.NEWS, "de", "CH"),
    RssService("RTS Info", "https://www.youtube.com/feeds/videos.xml?channel_id=UCYv3i53a_8sU_g54j2x2g3g", RssCategory.NEWS, "fr", "CH")
)

private fun getPolandRssServices(): List<RssService> = listOf(
    // News
    RssService("Onet Wiadomosci", "http://wiadomosci.onet.pl/.feed", RssCategory.NEWS, "pl", "PL"),
    RssService("WP.pl Wiadomosci", "http://wiadomosci.wp.pl/rss.xml", RssCategory.NEWS, "pl", "PL"),
    RssService("Gazeta.pl", "http://rss.gazeta.pl/pub/rss/wiadomosci.xml", RssCategory.NEWS, "pl", "PL"),
    // Sports
    RssService("Przegląd Sportowy", "https://www.przegladsportowy.pl/rss", RssCategory.SPORTS, "pl", "PL"),
    RssService("Sport.pl", "http://www.sport.pl/rss/sport.xml", RssCategory.SPORTS, "pl", "PL"),
    // YouTube
    RssService("TVN24", "https://www.youtube.com/feeds/videos.xml?channel_id=UC_j-t_1P9_f_Yg9-3g_t_g", RssCategory.NEWS, "pl", "PL"),
    RssService("TVP INFO", "https://www.youtube.com/feeds/videos.xml?channel_id=UC_j-t_1P9_f_Yg9-3g_t_g", RssCategory.NEWS, "pl", "PL")
)

private fun getCzechRepublicRssServices(): List<RssService> = listOf(
    // News
    RssService("ČT24", "https://ct24.ceskatelevize.cz/rss", RssCategory.NEWS, "cs", "CZ"),
    RssService("Právo", "https://www.novinky.cz/rss/", RssCategory.NEWS, "cs", "CZ"),
    RssService("iDNES.cz", "http://servis.idnes.cz/rss.aspx?c=zpravodaj", RssCategory.NEWS, "cs", "CZ"),
    RssService("Aktualne.cz", "http://zpravy.aktualne.cz/rss/", RssCategory.NEWS, "cs", "CZ"),
    RssService("Blesk.cz", "http://www.blesk.cz/rss/", RssCategory.NEWS, "cs", "CZ"),
    // Sports
    RssService("iSport.cz", "https://isport.blesk.cz/rss", RssCategory.SPORTS, "cs", "CZ"),
    RssService("Sport.cz", "http://www.sport.cz/rss2/", RssCategory.SPORTS, "cs", "CZ"),
    RssService("ČT Sport", "https://sport.ceskatelevize.cz/rss", RssCategory.SPORTS, "cs", "CZ"),
    // YouTube
    RssService("ČT24", "https://www.youtube.com/feeds/videos.xml?channel_id=UCTr_D0f6M5h_j-l_x_g-f_w", RssCategory.NEWS, "cs", "CZ"),
    RssService("Seznam Zprávy", "https://www.youtube.com/feeds/videos.xml?channel_id=UCpC-2Yn_gI-v2-oQf_w-g_w", RssCategory.NEWS, "cs", "CZ")
)

private fun getHungaryRssServices(): List<RssService> = listOf(
    // News
    RssService("Index.hu", "https://index.hu/24ora/rss", RssCategory.NEWS, "hu", "HU"),
    RssService("hvg.hu", "http://hvg.hu/rss", RssCategory.NEWS, "hu", "HU"),
    RssService("Blikk", "http://www.blikk.hu/rss/blikk", RssCategory.NEWS, "hu", "HU"),
    // Sports
    RssService("Nemzeti Sport", "https://www.nemzetisport.hu/rss", RssCategory.SPORTS, "hu", "HU"),
    RssService("Origo Sport", "https://www.origo.hu/sport/rss", RssCategory.SPORTS, "hu", "HU"),
    // YouTube
    RssService("ATV Magyarország", "https://www.youtube.com/feeds/videos.xml?channel_id=UC-3_a_5G3z-kZ_cbhT6h_Gg", RssCategory.NEWS, "hu", "HU"),
    RssService("HírTV", "https://www.youtube.com/feeds/videos.xml?channel_id=UCi5Qv6s_mK4i1e52Jb_0s9g", RssCategory.NEWS, "hu", "HU")
)

private fun getGreeceRssServices(): List<RssService> = listOf(
    // News
    RssService("in.gr", "http://rss.in.gr/Netvolution.Site.Engine.PageHandler.axd?rid=2&pid=250&la=1&si=1", RssCategory.NEWS, "el", "GR"),
    RssService("Newsbomb.gr", "http://feeds.feedburner.com/newsbombgr", RssCategory.NEWS, "el", "GR"),
    RssService("NewsIt.gr", "http://www.newsit.gr/rss/artrss.php", RssCategory.NEWS, "el", "GR"),
    RssService("Proto Thema", "http://www.protothema.gr/rss/news/general/", RssCategory.NEWS, "el", "GR"),
    // Sports
    RssService("Sport24", "https://www.sport24.gr/rss", RssCategory.SPORTS, "el", "GR"),
    RssService("Gazzetta.gr", "https://www.gazzetta.gr/rss", RssCategory.SPORTS, "el", "GR"),
    // YouTube
    RssService("OPEN TV", "https://www.youtube.com/feeds/videos.xml?channel_id=UC_j-t_1P9_f_Yg9-3g_t_g", RssCategory.NEWS, "el", "GR"),
    RssService("ERT", "https://www.youtube.com/feeds/videos.xml?channel_id=UC_j-t_1P9_f_Yg9-3g_t_g", RssCategory.NEWS, "el", "GR")
)

private fun getNigeriaRssServices(): List<RssService> = listOf(
    // News
    RssService("The Guardian Nigeria", "https://guardian.ng/feed/", RssCategory.NEWS, "en", "NG"),
    RssService("Vanguard News", "https://www.vanguardngr.com/feed/", RssCategory.NEWS, "en", "NG"),
    // Sports
    RssService("Complete Sports Nigeria", "https://www.completesports.com/feed/", RssCategory.SPORTS, "en", "NG"),
    RssService("Brila FM", "https://www.brila.net/feed/", RssCategory.SPORTS, "en", "NG"),
    RssService("Sporting Life", "https://sportinglife.ng/feed/", RssCategory.SPORTS, "en", "NG"),
    // YouTube
    RssService("Channels Television", "https://www.youtube.com/feeds/videos.xml?channel_id=UCAbjB_O3-d-d3_d_d_d_d_d", RssCategory.NEWS, "en", "NG"),
    RssService("TVC News Nigeria", "https://www.youtube.com/feeds/videos.xml?channel_id=UCgp4A6I8_O3-d_d_d_d_d_d", RssCategory.NEWS, "en", "NG")
)

private fun getKenyaRssServices(): List<RssService> = listOf(
    RssService("Standard Media - Kenya", "https://www.standardmedia.co.ke/rss/headlines.php", RssCategory.NEWS, "en", "KE"),
    RssService("Citizen TV Kenya", "https://citizentv.co.ke/rss", RssCategory.NEWS, "en", "KE"),
    RssService("NTV Kenya", "https://ntv.nation.co.ke/rss", RssCategory.NEWS, "en", "KE"),
    // YouTube
    RssService("Citizen TV Kenya", "https://www.youtube.com/feeds/videos.xml?channel_id=UC8-f4_d_d_d_d_d_d_d_d_d", RssCategory.NEWS, "en", "KE"),
    RssService("NTV Kenya", "https://www.youtube.com/feeds/videos.xml?channel_id=UCp-y1-d_ea_g-e29B40_3pQ", RssCategory.NEWS, "en", "KE")
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
