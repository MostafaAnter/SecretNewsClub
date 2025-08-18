package secret.news.club.infrastructure.rss

import secret.news.club.domain.model.rss.RssCategory
import secret.news.club.domain.model.rss.RssService

private fun getUSRssServices(): List<RssService> = listOf(
    RssService("NBC News Top Stories", "https://feeds.nbcnews.com/nbcnews/public/news", RssCategory.NEWS, "en", "US"),
    RssService("Fox News Latest", "http://feeds.foxnews.com/foxnews/latest", RssCategory.NEWS, "en", "US"),
    RssService("NPR", "https://www.npr.org/rss/rss.php?id=1001", RssCategory.NEWS, "en", "US"),
    RssService("NYT Top Stories", "https://rss.nytimes.com/services/xml/rss/nyt/HomePage.xml", RssCategory.NEWS, "en-us", "US"),
    RssService("NBC News Top Stories", "https://nbcnews.com/rss", RssCategory.NEWS, "en-US", "US"),
    RssService("NBC News Top Stories", "https://nbcnews.com/feed", RssCategory.NEWS, "en-US", "US"),
    RssService("Latest Breaking News on Fox News", "https://foxnews.com/rss", RssCategory.NEWS, "en-us", "US"),
    RssService("Latest Breaking News Fox News", "https://foxnews.com/rss.xml", RssCategory.NEWS, "en-us", "US"),
    RssService("Latest Breaking News on Fox News", "https://moxie.foxnews.com/google-publisher/latest.xml", RssCategory.NEWS, "en-us", "US"),
    RssService("The Hill News", "https://thehill.com/rss", RssCategory.NEWS, "en-US", "US"),
    RssService("The Hill News", "https://thehill.com/feed", RssCategory.NEWS, "en-US", "US"),
    RssService("Just In News", "https://thehill.com/rss.xml", RssCategory.NEWS, "en-US", "US"),
    RssService("ESPN Top Headlines", "https://www.espn.com/espn/rss/news", RssCategory.SPORTS, "en", "US"),
    RssService("CBS Sports", "https://www.cbssports.com/rss/headlines/", RssCategory.SPORTS, "en", "US"),
    RssService("Yahoo Sports Top Headlines", "https://sports.yahoo.com/top/rss.xml", RssCategory.SPORTS, "en", "US"),
    RssService("CNN", "https://www.youtube.com/feeds/videos.xml?channel_id=UCupvZG-5ko_eiXAupbDfxWw", RssCategory.NEWS, "en", "US"),
    RssService("Fox News", "https://www.youtube.com/feeds/videos.xml?channel_id=UCXIJgqnII2ZOINSWNOGFThA", RssCategory.NEWS, "en", "US"),
    RssService("NBC News", "https://www.youtube.com/feeds/videos.xml?channel_id=UCeY0bbntWzzVIaj2z3QigXg", RssCategory.NEWS, "en", "US"),
    RssService("ABC News", "https://www.youtube.com/feeds/videos.xml?channel_id=UCBi2mrWuNuyYy4gbM6fU18Q", RssCategory.NEWS, "en", "US"),
    RssService("NPR Topics Home Page Top Stories", "https://feeds.npr.org/1002/rss.xml", RssCategory.NEWS, "en", "US"),
    RssService("NPR Topics News", "https://feeds.npr.org/1001/rss.xml", RssCategory.NEWS, "en", "US"),
    RssService("NPR Topics Music", "https://feeds.npr.org/1039/rss.xml", RssCategory.NEWS, "en", "US"),
    RssService("Home - CBSNewscom", "https://www.cbsnews.com/latest/rss/main", RssCategory.NEWS, "en", "US")
)

private fun getUKRssServices(): List<RssService> = listOf(
    RssService("The Guardian Top Stories", "https://www.theguardian.com/uk/rss", RssCategory.NEWS, "en", "GB"),
    RssService("Independent UK", "http://www.independent.co.uk/news/uk/rss", RssCategory.NEWS, "en", "GB"),
    RssService("Sky News Top Stories", "http://feeds.skynews.com/feeds/rss/home.xml", RssCategory.NEWS, "en", "GB"),
    RssService("The Sun", "https://www.thesun.co.uk/feed/", RssCategory.NEWS, "en", "GB"),
    RssService("Daily Mail", "https://www.dailymail.co.uk/home/index.rss", RssCategory.NEWS, "en", "GB"),
    RssService("The Guardian", "https://theguardian.com/rss", RssCategory.NEWS, "en-gb", "GB"),
    RssService("The Guardian", "https://theguardian.com/rss/news", RssCategory.NEWS, "en-gb", "GB"),
    RssService("The Guardian", "https://www.theguardian.com/europe/rss", RssCategory.NEWS, "en-gb", "GB"),
    RssService("The Independent", "https://independent.co.uk/rss", RssCategory.NEWS, "en-gb", "GB"),
    RssService("The Independent", "https://www.independent.co.uk/rss", RssCategory.NEWS, "en-gb", "GB"),
    RssService("Mirror - mirror frontpage", "https://mirror.co.uk/rss.xml", RssCategory.NEWS, "en-gb", "GB"),
    RssService("Home Mail Online", "https://www.dailymail.co.uk/home/index.rss", RssCategory.NEWS, "en-gb", "GB"),
    RssService("Articles Mail Online", "https://www.dailymail.co.uk/articles.rss", RssCategory.NEWS, "en-gb", "GB"),
    RssService("The Sun", "https://thesun.co.uk/rss", RssCategory.NEWS, "en-GB", "GB"),
    RssService("The Sun", "https://thesun.co.uk/feed", RssCategory.NEWS, "en-GB", "GB"),
    RssService("The Sun", "https://www.thesun.co.uk/feed/", RssCategory.NEWS, "en-GB", "GB"),
    RssService("The Standard", "https://standard.co.uk/rss", RssCategory.NEWS, "en-gb", "GB"),
    RssService("The Standard", "https://www.standard.co.uk/rss", RssCategory.NEWS, "en-gb", "GB"),
    RssService("Metro", "https://metro.co.uk/rss", RssCategory.NEWS, "en-US", "GB"),
    RssService("Metro", "https://metro.co.uk/feed", RssCategory.NEWS, "en-US", "GB"),
    RssService("Metro", "https://metro.co.uk/feed/", RssCategory.NEWS, "en-US", "GB"),
    RssService("Entertainment Metro", "https://metro.co.uk/entertainment/feed/", RssCategory.NEWS, "en-US", "GB"),
    RssService("Lifestyle Metro", "https://metro.co.uk/lifestyle/feed/", RssCategory.NEWS, "en-US", "GB"),
    RssService("News Feed", "https://ft.com/rss/news", RssCategory.NEWS, "en", "GB"),
    RssService("International homepage", "https://www.ft.com/rss/home", RssCategory.NEWS, "en", "GB"),
    RssService("International homepage", "https://www.ft.com/rss/home/international", RssCategory.NEWS, "en", "GB"),
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
    RssService("Guardian News", "https://www.youtube.com/feeds/videos.xml?channel_id=UCIRYBXDze5krPDzAEOxFGVA", RssCategory.NEWS, "en", "GB"),
    // Other
)

private fun getCanadaRssServices(): List<RssService> = listOf(
    // News
    RssService("National Post", "https://nationalpost.com/feed", RssCategory.NEWS, "en", "CA"),
    RssService("", "https://globalnews.ca/rss", RssCategory.NEWS, "en-CA", "CA"),
    RssService("", "https://globalnews.ca/feed", RssCategory.NEWS, "en-CA", "CA"),
    RssService("", "https://globalnews.ca/feed/", RssCategory.NEWS, "en-CA", "CA"),
    RssService("National Post", "https://nationalpost.com/rss", RssCategory.NEWS, "en-CA", "CA"),
    RssService("National Post", "https://nationalpost.com/feed", RssCategory.NEWS, "en-CA", "CA"),
    RssService("National Post", "https://nationalpost.com/feed/atom", RssCategory.NEWS, "en-CA", "CA"),
    RssService("The Decibel", "https://feeds.simplecast.com/TGclX98p", RssCategory.NEWS, "en-CA", "CA"),
    RssService("Vancouver Sun", "https://vancouversun.com/rss", RssCategory.NEWS, "en-CA", "CA"),
    RssService("Vancouver Sun", "https://vancouversun.com/feed", RssCategory.NEWS, "en-CA", "CA"),
    RssService("Vancouver Sun", "https://vancouversun.com/feed/atom", RssCategory.NEWS, "en-CA", "CA"),
    RssService("Calgary Herald", "https://calgaryherald.com/rss", RssCategory.NEWS, "en-CA", "CA"),
    RssService("Calgary Herald", "https://calgaryherald.com/feed", RssCategory.NEWS, "en-CA", "CA"),
    RssService("Calgary Herald", "https://calgaryherald.com/feed/atom", RssCategory.NEWS, "en-CA", "CA"),
    // Sports
    // YouTube
)

private fun getIndiaRssServices(): List<RssService> = listOf(
    RssService("Aaj Tak", "https://www.youtube.com/feeds/videos.xml?channel_id=UCt4t-jeY85JegMlZ-E5UWtA", RssCategory.NEWS, "hi", "IN"),
    RssService("The Times of India", "https://timesofindia.indiatimes.com/rssfeeds/-2128936835.cms", RssCategory.NEWS, "en", "IN"),
    RssService("Hindustan Times", "https://www.hindustantimes.com/feeds/rss/india-news/rssfeed.xml", RssCategory.NEWS, "en", "IN"),
    RssService("The Hindu", "https://www.thehindu.com/feeder/default.rss", RssCategory.NEWS, "en", "IN"),
    RssService("NDTV", "http://feeds.feedburner.com/ndtvnews-top-stories", RssCategory.NEWS, "en", "IN"),
    RssService("ESPN Cricinfo", "https://www.espncricinfo.com/rss/content/story/feeds/0.xml", RssCategory.SPORTS, "en", "IN"),
    RssService("Zee News Sports", "http://zeenews.india.com/rss/sports-news.xml", RssCategory.SPORTS, "en", "IN"),
    RssService("Sportskeeda", "https://www.sportskeeda.com/feed", RssCategory.SPORTS, "en", "IN"),
    // YouTube
    RssService("NDTV", "https://www.youtube.com/feeds/videos.xml?channel_id=UCZFMm1mMw0F81Z37aaEzTUA", RssCategory.NEWS, "en", "IN"),
    RssService("India Today", "https://www.youtube.com/feeds/videos.xml?channel_id=UCYPvAwZP8pZhSMW8qs7cVCw", RssCategory.NEWS, "en", "IN"),
    RssService("WION", "https://www.youtube.com/feeds/videos.xml?channel_id=UC_gUM8rL-Lrg6O3adPW9K1g", RssCategory.NEWS, "en", "IN")
)

private fun getAustraliaRssServices(): List<RssService> = listOf(
    RssService("ABC News Australia", "https://www.abc.net.au/news/feed/51120/rss.xml", RssCategory.NEWS, "en", "AU"),
    RssService("The Sydney Morning Herald", "https://www.smh.com.au/rss/feed.xml", RssCategory.NEWS, "en", "AU"),
    RssService("The Age", "https://www.theage.com.au/rss/feed.xml", RssCategory.NEWS, "en", "AU"),
    RssService("news.com.au", "https://www.news.com.au/content-feeds/latest-news-national/", RssCategory.NEWS, "en", "AU"),
    RssService("ESPN Australia", "https://www.espn.com.au/espn/rss/news", RssCategory.SPORTS, "en", "AU"),
    RssService("Sydney Morning Herald - Latest News", "https://smh.com.au/rss/feed.xml", RssCategory.NEWS, "en-AU", "AU"),
    RssService("Sydney Morning Herald - Politics / Federal", "https://smh.com.au/rss/politics/federal.xml", RssCategory.NEWS, "en-AU", "AU"),
    RssService("Sydney Morning Herald - National / Nsw", "https://smh.com.au/rss/national/nsw.xml", RssCategory.NEWS, "en-AU", "AU"),
    RssService("The Age - Latest News", "https://theage.com.au/rss/feed.xml", RssCategory.NEWS, "en-AU", "AU"),
    RssService("The Age - Politics / Federal", "https://theage.com.au/rss/politics/federal.xml", RssCategory.NEWS, "en-AU", "AU"),
    RssService("The Age - National / Victoria", "https://theage.com.au/rss/national/victoria.xml", RssCategory.NEWS, "en-AU", "AU"),
    RssService("7NEWS", "https://7news.com.au/rss", RssCategory.NEWS, "en-AU", "AU"),
    RssService("7NEWS", "https://7news.com.au/feed", RssCategory.NEWS, "en-AU", "AU"),
    // YouTube
)

private fun getEgyptRssServices(): List<RssService> = listOf(
    // News
    RssService("Youm7", "https://www.youm7.com/rss/SectionRss?SectionID=65", RssCategory.NEWS, "ar", "EG"),
    RssService("Sada Elbalad", "https://see.news/rss", RssCategory.NEWS, "ar", "EG"),
    RssService("alwafd.news", "https://alwafd.news/rss.aspx", RssCategory.NEWS, "ar", "EG"),
    // Sports
    // YouTube
    RssService("Al Jazeera Mubasher", "https://www.youtube.com/feeds/videos.xml?channel_id=UCfiwzLy-8yKzIbsmZTzxDgw", RssCategory.NEWS, "ar", "EG"),
)

private fun getFranceRssServices(): List<RssService> = listOf(
    // News
    RssService("Le Monde", "https://www.lemonde.fr/rss/une.xml", RssCategory.NEWS, "fr", "FR"),
    RssService("Le Figaro", "https://www.lefigaro.fr/rss/figaro_actualites.xml", RssCategory.NEWS, "fr", "FR"),
    RssService("France 24", "https://www.france24.com/fr/rss", RssCategory.NEWS, "fr", "FR"),
    RssService("20 Minutes", "https://www.20minutes.fr/feeds/rss-une.xml", RssCategory.NEWS, "fr", "FR"),
    RssService("L'Express", "https://www.lexpress.fr/rss/alaune.xml", RssCategory.NEWS, "fr", "FR"),
    RssService("Le Mondefr - Actualités et Infos en France et da...", "https://www.lemonde.fr/rss/une.xml", RssCategory.NEWS, "fr-FR", "FR"),
    RssService("20Minutes - Une", "https://www.20minutes.fr/feeds/rss-une.xml", RssCategory.NEWS, "fr-FR", "FR"),
    RssService("Le Parisien - undefined", "https://feeds.leparisien.fr/leparisien/rss", RssCategory.NEWS, "fr-FR", "FR"),
    RssService("Europe1 — politique", "https://europe1.fr/rss.xml", RssCategory.NEWS, "fr-FR", "FR"),
    // Sports
    // YouTube
    RssService("FRANCE 24", "https://www.youtube.com/feeds/videos.xml?channel_id=UCCCPCZNChQdGa9EkATeye4g", RssCategory.NEWS, "fr", "FR"),
)

private fun getGermanyRssServices(): List<RssService> = listOf(
    RssService("Der Spiegel", "https://www.spiegel.de/schlagzeilen/tops/index.rss", RssCategory.NEWS, "de", "DE"),
    RssService("Die Welt", "https://www.welt.de/feeds/topnews.rss", RssCategory.NEWS, "de", "DE"),
    RssService("Tagesschau", "https://www.tagesschau.de/xml/rss2", RssCategory.NEWS, "de", "DE"),
    RssService("Süddeutsche Zeitung", "https://rss.sueddeutsche.de/rss/Topthemen", RssCategory.NEWS, "de", "DE"),
    RssService("Frankfurter Allgemeine Zeitung", "https://www.faz.net/rss/aktuell/", RssCategory.NEWS, "de", "DE"),
    RssService("kicker.de", "http://rss.kicker.de/news/fussball", RssCategory.SPORTS, "de", "DE"),
    RssService("Sportschau ARD", "http://www.sportschau.de/sendung/sportschausendungindex100.feed", RssCategory.SPORTS, "de", "DE"),
    RssService("DER SPIEGEL - Schlagzeilen", "https://www.spiegel.de/schlagzeilen/index.rss", RssCategory.NEWS, "de-DE", "DE"),
    RssService("DER SPIEGEL - Nachrichten", "https://www.spiegel.de/index.rss", RssCategory.NEWS, "de-DE", "DE"),
    RssService("BILD - Home", "http://www.bild.de/rss-feeds/rss-16725492,feed=home.bild.html", RssCategory.NEWS, "de-DE", "DE"),
    RssService("DIE ZEIT | Nachrichten News Hintergründe und Debatten", "https://newsfeed.zeit.de/index", RssCategory.NEWS, "de-DE", "DE"),
    RssService("Aktuell - FAZNET", "https://www.faz.net/rss/aktuell/", RssCategory.NEWS, "de-DE", "DE"),
    RssService("tagesschaude - die erste Adresse für Nachrichten...", "https://www.tagesschau.de/index~atom.xml", RssCategory.NEWS, "de-DE", "DE"),
    RssService("tagesschaude - die erste Adresse für Nachrichten...", "https://www.tagesschau.de/index~rss2.xml", RssCategory.NEWS, "de-DE", "DE"),
    RssService("tagesschaude - die erste Adresse für Nachrichten...", "https://www.tagesschau.de/index~rdf.xml", RssCategory.NEWS, "de-DE", "DE"),
    RssService("FOCUS online - Aktuelle Nachrichten", "https://focus.de/rss", RssCategory.NEWS, "de-DE", "DE"),
    RssService("kicker News", "https://newsfeed.kicker.de/news/aktuell", RssCategory.NEWS, "de-DE", "DE"),
    RssService("n-tvde - Startseite", "https://n-tv.de/rss", RssCategory.NEWS, "de-DE", "DE"),
    RssService("n-tvde - Startseite", "https://www.n-tv.de/rss", RssCategory.NEWS, "de-DE", "DE"),
    // YouTube
    RssService("tagesschau", "https://www.youtube.com/feeds/videos.xml?channel_id=UC5NOEUbkLheQcaaRldYW5GA", RssCategory.NEWS, "de", "DE"),
)

private fun getItalyRssServices(): List<RssService> = listOf(
    RssService("La Repubblica", "https://www.repubblica.it/rss/homepage/rss2.0.xml", RssCategory.NEWS, "it", "IT"),
    RssService("ANSA", "https://www.ansa.it/sito/ansait_rss.xml", RssCategory.NEWS, "it", "IT"),
    RssService("Corriere dello Sport", "https://www.corrieredellosport.it/rss/", RssCategory.SPORTS, "it", "IT"),
    RssService("La Stampa - News inchieste e approfondimenti La ...", "https://www.lastampa.it/rss/copertina.xml", RssCategory.NEWS, "it-IT", "IT"),
    RssService("Primo piano ANSA - ANSAit", "https://ansa.it/rss.xml", RssCategory.NEWS, "it-IT", "IT"),
    RssService("Tutte le notizie di  oggi - Quotidianonet", "https://quotidiano.net/rss", RssCategory.NEWS, "it-IT", "IT"),
    RssService("Tutte le notizie di  oggi - Quotidianonet", "https://quotidiano.net/feed", RssCategory.NEWS, "it-IT", "IT"),
    RssService("Tutte le notizie di  oggi - Quotidianonet", "https://quotidiano.net/rss/news", RssCategory.NEWS, "it-IT", "IT"),
    // YouTube
)

private fun getSpainRssServices(): List<RssService> = listOf(
    RssService("El País", "https://feeds.elpais.com/mrss-s/pages/ep/site/elpais.com/portada", RssCategory.NEWS, "es", "ES"),
    RssService("El Mundo", "https://e00-elmundo.uecdn.es/elmundo/rss/espana.xml", RssCategory.NEWS, "es", "ES"),
    RssService("ABC", "http://www.abc.es/rss/feeds/abc_EspanaEspana.xml", RssCategory.NEWS, "es", "ES"),
    RssService("La Vanguardia", "http://www.lavanguardia.com/rss/home.xml", RssCategory.NEWS, "es", "ES"),
    RssService("Mundo Deportivo", "https://www.mundodeportivo.com/rss/portada.xml", RssCategory.SPORTS, "es", "ES"),
    RssService("EL PAÍS el periódico global", "https://feeds.elpais.com/mrss-s/pages/ep/site/elpais.com/portada", RssCategory.NEWS, "es-ES", "ES"),
    RssService("Portada // elmundo", "https://e00-elmundo.uecdn.es/rss/portada.xml", RssCategory.NEWS, "es-ES", "ES"),
    RssService("Portada", "https://www.elmundo.es/rss/googlenews/portada.xml", RssCategory.NEWS, "es-ES", "ES"),
    RssService("RSS de noticias de portada", "https://www.abc.es/rss/2.0/portada/", RssCategory.NEWS, "es-ES", "ES"),
    RssService("RSS de noticias de ultima-hora", "https://www.abc.es/rss/2.0/ultima-hora/", RssCategory.NEWS, "es-ES", "ES"),
    RssService("Portada", "https://www.lavanguardia.com/rss/home.xml", RssCategory.NEWS, "es-ES", "ES"),
    RssService("Portada", "https://www.mundodeportivo.com/feed/rss/home/", RssCategory.NEWS, "es-ES", "ES"),
    RssService("20MINUTOSES - Lo último", "https://20minutos.es/rss", RssCategory.NEWS, "es-ES", "ES"),
    RssService("20MINUTOSES - Lo último", "https://www.20minutos.es/rss/", RssCategory.NEWS, "es-ES", "ES"),
    // YouTube
)

private fun getBrazilRssServices(): List<RssService> = listOf(
    RssService("Folha de S.Paulo", "https://feeds.folha.uol.com.br/emcimadahora/rss091.xml", RssCategory.NEWS, "pt", "BR"),
    RssService("Estadão", "https://feeds.folha.uol.com.br/poder/rss091.xml", RssCategory.NEWS, "pt", "BR"),
    RssService("O Globo", "https://oglobo.globo.com/rss/oglobo", RssCategory.NEWS, "pt", "BR"),
    RssService("G1", "https://g1.globo.com/rss/g1/", RssCategory.NEWS, "pt", "BR"),
    RssService("globoesporte.com", "https://ge.globo.com/rss/ge", RssCategory.SPORTS, "pt", "BR"),
    // YouTube
)

private fun getJapanRssServices(): List<RssService> = listOf(
    RssService("NHK News", "https://www3.nhk.or.jp/rss/news/cat0.xml", RssCategory.NEWS, "ja", "JP"),
    RssService("Mainichi Shimbun", "https://mainichi.jp/rss/etc/flash.rss", RssCategory.NEWS, "ja", "JP"),
    RssService("Asahi Shimbun", "http://www.asahi.com/rss/asahi/newsheadlines.rdf", RssCategory.NEWS, "ja", "JP"),
    RssService("The Japan Times", "https://www.japantimes.co.jp/feed/", RssCategory.NEWS, "en", "JP"),
    // YouTube
)

private fun getChinaRssServices(): List<RssService> = listOf(
    // News
    // Sports
    // YouTube
)

private fun getRussiaRssServices(): List<RssService> = listOf(
    RssService("Championat", "https://www.championat.com/rss/news/", RssCategory.SPORTS, "ru", "RU"),
    RssService("RT News", "https://www.rt.com/rss/news/", RssCategory.NEWS, "en", "RU"),
    RssService("Moscow Times", "https://www.themoscowtimes.com/rss/news", RssCategory.NEWS, "en", "RU"),
    RssService("TASS", "http://tass.com/rss/v2.xml", RssCategory.NEWS, "en", "RU"),
    // YouTube
)

private fun getSouthAfricaRssServices(): List<RssService> = listOf(
    RssService("News24", "https://feeds.24.com/articles/news24/TopStories/rss", RssCategory.NEWS, "en", "ZA"),
    RssService("TimesLIVE", "https://www.timeslive.co.za/rss", RssCategory.NEWS, "en", "ZA"),
    RssService("KickOff", "https://www.kickoff.com/rss", RssCategory.SPORTS, "en", "ZA"),
    // YouTube
    RssService("SABC News", "https://www.youtube.com/feeds/videos.xml?channel_id=UC8yH-uI81UUtEMDsowQyx1g", RssCategory.NEWS, "en", "ZA"),
)

private fun getSaudiArabiaRssServices(): List<RssService> = listOf(
    // News
    RssService("Arriyadiyah", "https://saudigazette.com.sa/rssFeed/0", RssCategory.SPORTS, "ar", "SA"),
    RssService("Arriyadiyah", "https://saudigazette.com.sa/rssFeed/45", RssCategory.SPORTS, "ar", "SA"),
    RssService("Arriyadiyah", "https://saudigazette.com.sa/rssFeed/74", RssCategory.SPORTS, "ar", "SA"),
    RssService("Arriyadiyah", "https://arriyadiyah.com/rss", RssCategory.SPORTS, "ar", "SA"),
    RssService("Al Jazeera", "https://www.aljazeera.com/xml/rss/all.xml", RssCategory.NEWS, "en", "SA"),
    RssService("Al Arabiya", "https://www.youm7.com/rss/SectionRss?SectionID=88", RssCategory.NEWS, "en", "SA"),
    // Sports
    // YouTube
)

private fun getUaeRssServices(): List<RssService> = listOf(
    // News
    RssService("Lovin Dubai", "https://lovin.co/dubai/en/news/feed/", RssCategory.NEWS, "en", "AE"),
    RssService("What's On", "https://whatson.ae/feed/", RssCategory.NEWS, "en", "AE"),
    RssService("What's On", "https://www.youm7.com/rss/SectionRss?SectionID=88", RssCategory.NEWS, "en", "AE"),
    RssService("Gulf News", "https://gulfnews.com/feed", RssCategory.NEWS, "en", "AE"),
    // Sports
    // YouTube
)

private fun getTurkeyRssServices(): List<RssService> = listOf(
    RssService("Hürriyet", "https://www.hurriyet.com.tr/rss/anasayfa", RssCategory.NEWS, "tr", "TR"),
    RssService("Sabah", "https://www.sabah.com.tr/rss/gundem.xml", RssCategory.NEWS, "tr", "TR"),
    RssService("Haber Turk", "http://www.haberturk.com/haberturk.xml", RssCategory.NEWS, "tr", "TR"),
    RssService("FotoMac", "http://www.fotomac.com.tr/rss/besiktas.xml", RssCategory.SPORTS, "tr", "TR"),
    RssService("Daily Sabah", "https://www.dailysabah.com/rssFeed/1", RssCategory.NEWS, "en", "TR"),
    // YouTube
)

private fun getArgentinaRssServices(): List<RssService> = listOf(
    RssService("Clarín", "https://www.clarin.com/rss/lo-ultimo/", RssCategory.NEWS, "es", "AR"),
    RssService("La Nacion", "https://www.lanacion.com.ar/arc/outboundfeeds/rss/?outputType=xml", RssCategory.NEWS, "es", "AR"),
    RssService("Infobae", "https://www.infobae.com/arc/outboundfeeds/rss/", RssCategory.NEWS, "es", "AR"),
    RssService("Pagina/12", "https://www.pagina12.com.ar/rss/portada", RssCategory.NEWS, "es", "AR"),
    RssService("Olé - Sports", "https://www.ole.com.ar/rss/ultimas-noticias/", RssCategory.SPORTS, "es", "AR"),
    RssService("La Capital", "https://www.lacapital.com.ar/rss/home.xml", RssCategory.NEWS, "es", "AR"),
    // YouTube
    RssService("TN", "https://www.youtube.com/feeds/videos.xml?channel_id=UCj6PcyLvpnIRT_2W_mwa9Aw", RssCategory.NEWS, "es", "AR")
)

private fun getMexicoRssServices(): List<RssService> = listOf(
    RssService("Milenio", "https://www.milenio.com/rss", RssCategory.NEWS, "es", "MX"),
    RssService("Excélsior", "https://www.excelsior.com.mx/rss", RssCategory.NEWS, "es", "MX"),
    RssService("Record - Sports", "https://www.record.com.mx/rss.xml", RssCategory.SPORTS, "es", "MX"),
    // YouTube
)

private fun getSwedenRssServices(): List<RssService> = listOf(
    RssService("Dagens Nyheter", "https://www.dn.se/rss/", RssCategory.NEWS, "sv", "SE"),
    RssService("Svenska Dagbladet", "https://www.svd.se/?service=rss", RssCategory.NEWS, "sv", "SE"),
    RssService("Aftonbladet", "https://rss.aftonbladet.se/rss2/small/pages/sections/senastenytt/", RssCategory.NEWS, "sv", "SE"),
    RssService("Expressen", "http://www.expressen.se/rss/nyheter", RssCategory.NEWS, "sv", "SE"),
    RssService("SVT Nyheter", "https://www.svt.se/nyheter/rss.xml", RssCategory.NEWS, "sv", "SE"),
    RssService("Fotbollskanalen", "https://www.fotbollskanalen.se/rss/", RssCategory.SPORTS, "sv", "SE"),
    RssService("SVT Sport", "https://www.svt.se/sport/rss.xml", RssCategory.SPORTS, "sv", "SE"),
    // YouTube
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
)

private fun getDenmarkRssServices(): List<RssService> = listOf(
    RssService("Politiken", "https://politiken.dk/rss/senestenyt.rss", RssCategory.NEWS, "da", "DK"),
    RssService("Ekstra Bladet", "http://ekstrabladet.dk/rssfeed/all/", RssCategory.NEWS, "da", "DK"),
    RssService("BT", "http://www.bt.dk/bt/seneste/rss", RssCategory.NEWS, "da", "DK"),
    RssService("DR Nyheder", "https://www.dr.dk/nyheder/service/feeds/allenyheder", RssCategory.NEWS, "da", "DK"),
    // YouTube
)

private fun getFinlandRssServices(): List<RssService> = listOf(
    RssService("Helsingin Sanomat", "https://www.hs.fi/rss/teasers/etusivu.xml", RssCategory.NEWS, "fi", "FI"),
    RssService("Ilta-Sanomat", "https://www.is.fi/rss/tuoreimmat.xml", RssCategory.NEWS, "fi", "FI"),
    RssService("Yle Uutiset", "https://feeds.yle.fi/uutiset/v1/recent.rss?publisherIds=YLE_UUTISET", RssCategory.NEWS, "fi", "FI"),
    RssService("Ilta-Sanomat Sport", "https://www.is.fi/rss/urheilu.xml", RssCategory.SPORTS, "fi", "FI"),
    // YouTube
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
)

private fun getBelgiumRssServices(): List<RssService> = listOf(
    RssService("Het Laatste Nieuws", "https://www.hln.be/rss.xml", RssCategory.NEWS, "nl", "BE"),
    RssService("De Standaard", "https://www.standaard.be/rss", RssCategory.NEWS, "nl", "BE"),
    RssService("De Morgen", "https://demorgen.be/in-het-nieuws/rss.xml", RssCategory.NEWS, "nl", "BE"),
    RssService("La Libre", "https://www.lalibre.be/rss", RssCategory.NEWS, "fr", "BE"),
    RssService("DH Les Sports+", "https://www.dhnet.be/rss/section/sports.xml", RssCategory.SPORTS, "fr", "BE"),
    // YouTube
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
)

private fun getSwitzerlandRssServices(): List<RssService> = listOf(
    // News
    RssService("Neue Zuercher Zeitung", "http://www.nzz.ch/startseite.rss", RssCategory.NEWS, "de", "CH"),
    // Sports
    RssService("SRF Sport", "http://www.srf.ch/sport/bnf/rss/718", RssCategory.SPORTS, "de", "CH"),
    // YouTube
)

private fun getPolandRssServices(): List<RssService> = listOf(
    // News
    RssService("Onet Wiadomosci", "http://wiadomosci.onet.pl/.feed", RssCategory.NEWS, "pl", "PL"),
    RssService("WP.pl Wiadomosci", "http://wiadomosci.wp.pl/rss.xml", RssCategory.NEWS, "pl", "PL"),
    RssService("Gazeta.pl", "http://rss.gazeta.pl/pub/rss/wiadomosci.xml", RssCategory.NEWS, "pl", "PL"),
    // Sports
    // YouTube
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
    // YouTube
)

private fun getHungaryRssServices(): List<RssService> = listOf(
    // News
    RssService("Index.hu", "https://index.hu/24ora/rss", RssCategory.NEWS, "hu", "HU"),
    RssService("hvg.hu", "http://hvg.hu/rss", RssCategory.NEWS, "hu", "HU"),
    RssService("Blikk", "http://www.blikk.hu/rss/blikk", RssCategory.NEWS, "hu", "HU"),
    // Sports
    // YouTube
)

private fun getGreeceRssServices(): List<RssService> = listOf(
    // News
    RssService("in.gr", "http://rss.in.gr/Netvolution.Site.Engine.PageHandler.axd?rid=2&pid=250&la=1&si=1", RssCategory.NEWS, "el", "GR"),
    RssService("Newsbomb.gr", "http://feeds.feedburner.com/newsbombgr", RssCategory.NEWS, "el", "GR"),
    RssService("NewsIt.gr", "http://www.newsit.gr/rss/artrss.php", RssCategory.NEWS, "el", "GR"),
    RssService("Proto Thema", "http://www.protothema.gr/rss/news/general/", RssCategory.NEWS, "el", "GR"),
    // Sports
    RssService("Gazzetta.gr", "https://www.gazzetta.gr/rss", RssCategory.SPORTS, "el", "GR"),
    // YouTube
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
)

private fun getKenyaRssServices(): List<RssService> = listOf(
    RssService("Standard Media - Kenya", "https://www.standardmedia.co.ke/rss/headlines.php", RssCategory.NEWS, "en", "KE"),
    // YouTube
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
