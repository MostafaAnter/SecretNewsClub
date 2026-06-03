package secret.news.club.infrastructure.rss.discovery

import secret.news.club.domain.model.rss.RssCategory
import secret.news.club.domain.model.rss.RssCategory.BUSINESS
import secret.news.club.domain.model.rss.RssCategory.ENTERTAINMENT
import secret.news.club.domain.model.rss.RssCategory.NEWS
import secret.news.club.domain.model.rss.RssCategory.SPORTS
import secret.news.club.domain.model.rss.RssCategory.TECHNOLOGY

/**
 * Per-country discovery inputs: native-language search keywords, generic feed terms,
 * and any extra seed domains beyond what's already in RssData.kt.
 *
 * The discovery engine combines these with domains auto-extracted from RssData.kt
 * for the same country, so the static curated list and the discovery seed list never
 * drift apart — every static entry's host automatically becomes a probe target.
 *
 * Stage A validation findings (see project_rss_discovery memory) drove three rules
 * across every config:
 *   1. Native-language phrases beat English phrases on DuckDuckGo.
 *   2. Country name in the query helps the result page be country-specific.
 *   3. URL hints ("RSS", "feed", "feed.xml") bias the result page toward sites
 *      that actually expose `<link rel="alternate" type="application/rss+xml">`.
 *
 * Keyword authoring is best-effort — entries written by an LLM, not a native
 * speaker. Spot-checks welcome via the TroubleshootingPage debug runner.
 */
data class CountryDiscoveryConfig(
    val countryCode: String,
    val language: String,
    /** Native-language search phrases per category. Used as DuckDuckGo HTML queries. */
    val nativeKeywords: Map<RssCategory, List<String>>,
    /** Feed-format hints appended to native queries to bias toward result pages
     *  that actually expose `<link rel="alternate" type="application/rss+xml">`. */
    val genericTerms: List<String> = DEFAULT_GENERIC_TERMS,
    /** Domains worth probing that are NOT already in RssData.kt for this country. */
    val extraDomains: List<String> = emptyList(),
) {
    companion object {
        /** Generic RSS/feed terms appended to native queries. Latin set works
         *  even on Arabic/CJK queries because the feed URLs themselves are ASCII. */
        val DEFAULT_GENERIC_TERMS = listOf("RSS", "feed", "feed.xml", "rss.xml", "atom.xml")
    }
}

object CountryDiscoveryRegistry {

    operator fun get(countryCode: String): CountryDiscoveryConfig? =
        configs[countryCode.uppercase()]

    fun isSupported(countryCode: String): Boolean =
        configs.containsKey(countryCode.uppercase())

    /** Used by diagnostics — count and country list of supported entries. */
    val supportedCountries: Set<String> get() = configs.keys

    private val configs: Map<String, CountryDiscoveryConfig> = listOf(
        // Validation target first; then alphabetical by ISO code.
        EG,
        AE, AR, AT, AU, BE, BR, CA, CH, CN, CZ, DE, DK, ES, FI, FR, GB, GR, HU,
        ID, IN, IT, JP, KE, KR, MX, MY, NG, NL, NO, PH, PL, RU, SA, SE, TH, TR,
        US, VN, ZA,
    ).associateBy { it.countryCode }
}

// ─── Validation target ─────────────────────────────────────────────────────────
// Egypt — first country we hardened. Stage A confirmed Arabic queries surface
// eldyar.net, akhbarelyom.com, operamisr.com, presidency.eg that domain-probing
// alone never finds.

private val EG = CountryDiscoveryConfig(
    countryCode = "EG",
    language = "ar",
    nativeKeywords = mapOf(
        NEWS to listOf(
            "أخبار مصر RSS",
            "موقع أخبار مصري RSS",
            "آخر أخبار مصر تغذية",
            "صحف مصرية RSS feed",
        ),
        SPORTS to listOf(
            "أخبار رياضة مصر RSS",
            "كرة القدم المصرية RSS",
            "الدوري المصري RSS",
            "موقع رياضي مصري RSS feed",
        ),
        TECHNOLOGY to listOf(
            "أخبار تقنية مصر RSS",
            "التكنولوجيا والإنترنت RSS",
            "أخبار تقنية عربية RSS feed",
        ),
        ENTERTAINMENT to listOf(
            "أخبار فن مصر RSS",
            "السينما المصرية RSS",
            "فن وترفيه RSS مصر",
            "أخبار المشاهير RSS مصر",
        ),
        BUSINESS to listOf(
            "أخبار اقتصاد مصر RSS",
            "البورصة المصرية RSS",
            "أعمال ومال مصر RSS feed",
        ),
    ),
    extraDomains = listOf(
        "eldyar.net", "akhbarelyom.com", "operamisr.com", "ahlmasrnews.com",
        "presidency.eg", "ahram.org.eg", "filgoal.com", "yallakora.com",
    ),
)

// ─── Arabic-speaking ───────────────────────────────────────────────────────────

private val SA = CountryDiscoveryConfig(
    countryCode = "SA",
    language = "ar",
    nativeKeywords = mapOf(
        NEWS to listOf(
            "أخبار السعودية RSS",
            "صحف سعودية RSS feed",
            "آخر أخبار المملكة العربية السعودية RSS",
            "موقع إخباري سعودي RSS",
        ),
        SPORTS to listOf(
            "أخبار رياضة السعودية RSS",
            "الدوري السعودي للمحترفين RSS",
            "كرة القدم السعودية RSS feed",
        ),
        TECHNOLOGY to listOf(
            "أخبار تقنية السعودية RSS",
            "تقنية الخليج RSS feed",
        ),
        ENTERTAINMENT to listOf(
            "أخبار فن السعودية RSS",
            "ترفيه ومنوعات السعودية RSS",
        ),
        BUSINESS to listOf(
            "أخبار اقتصاد السعودية RSS",
            "السوق المالية السعودية تداول RSS",
            "أعمال ومال السعودية RSS feed",
        ),
    ),
    extraDomains = listOf("alarabiya.net", "aawsat.com", "asharqnews.com"),
)

private val AE = CountryDiscoveryConfig(
    countryCode = "AE",
    language = "ar",
    nativeKeywords = mapOf(
        NEWS to listOf(
            "أخبار الإمارات RSS",
            "موقع إخباري إماراتي RSS",
            "صحف الإمارات RSS feed",
        ),
        SPORTS to listOf(
            "أخبار رياضة الإمارات RSS",
            "دوري الخليج العربي RSS feed",
        ),
        TECHNOLOGY to listOf(
            "أخبار تقنية الإمارات RSS",
            "تقنية ودبي RSS feed",
        ),
        ENTERTAINMENT to listOf(
            "أخبار فن الإمارات RSS",
            "ترفيه الإمارات RSS",
        ),
        BUSINESS to listOf(
            "أخبار اقتصاد الإمارات RSS",
            "أعمال دبي وأبوظبي RSS feed",
        ),
    ),
    extraDomains = listOf("gulfnews.com", "khaleejtimes.com", "thenationalnews.com"),
)

// ─── Spanish-speaking ──────────────────────────────────────────────────────────

private val ES = CountryDiscoveryConfig(
    countryCode = "ES",
    language = "es",
    nativeKeywords = mapOf(
        NEWS to listOf(
            "noticias España RSS",
            "España noticias RSS feed",
            "periódico España RSS",
        ),
        SPORTS to listOf(
            "deportes España RSS",
            "fútbol La Liga RSS feed",
            "noticias deportivas España RSS",
        ),
        TECHNOLOGY to listOf(
            "tecnología España RSS",
            "noticias tecnología RSS feed",
        ),
        ENTERTAINMENT to listOf(
            "espectáculos España RSS",
            "famosos España RSS feed",
        ),
        BUSINESS to listOf(
            "economía España RSS",
            "negocios España RSS feed",
        ),
    ),
    extraDomains = listOf("marca.com", "rtve.es"),
)

private val MX = CountryDiscoveryConfig(
    countryCode = "MX",
    language = "es",
    nativeKeywords = mapOf(
        NEWS to listOf(
            "noticias México RSS",
            "México noticias RSS feed",
            "periódico mexicano RSS",
        ),
        SPORTS to listOf(
            "deportes México RSS",
            "Liga MX fútbol RSS feed",
            "noticias deportivas México RSS",
        ),
        TECHNOLOGY to listOf(
            "tecnología México RSS",
            "noticias tecnología México RSS feed",
        ),
        ENTERTAINMENT to listOf(
            "espectáculos México RSS",
            "farándula mexicana RSS feed",
        ),
        BUSINESS to listOf(
            "economía México RSS",
            "negocios México RSS feed",
        ),
    ),
    extraDomains = listOf("mediotiempo.com", "jornada.com.mx", "animalpolitico.com"),
)

private val AR = CountryDiscoveryConfig(
    countryCode = "AR",
    language = "es",
    nativeKeywords = mapOf(
        NEWS to listOf(
            "noticias Argentina RSS",
            "Argentina noticias RSS feed",
            "diario argentino RSS",
        ),
        SPORTS to listOf(
            "deportes Argentina RSS",
            "fútbol argentino RSS feed",
            "Primera División Argentina RSS",
        ),
        TECHNOLOGY to listOf(
            "tecnología Argentina RSS",
            "noticias tecnología Argentina RSS feed",
        ),
        ENTERTAINMENT to listOf(
            "espectáculos Argentina RSS",
            "farándula argentina RSS feed",
        ),
        BUSINESS to listOf(
            "economía Argentina RSS",
            "negocios Argentina RSS feed",
        ),
    ),
    extraDomains = listOf("ole.com.ar", "tycsports.com", "ambito.com"),
)

// ─── German-speaking ───────────────────────────────────────────────────────────

private val DE = CountryDiscoveryConfig(
    countryCode = "DE",
    language = "de",
    nativeKeywords = mapOf(
        NEWS to listOf(
            "Deutschland Nachrichten RSS",
            "Deutsche Nachrichten RSS feed",
            "deutsche Zeitung RSS",
        ),
        SPORTS to listOf(
            "Sport Deutschland RSS",
            "Bundesliga Fußball RSS feed",
            "Sportnachrichten Deutschland RSS",
        ),
        TECHNOLOGY to listOf(
            "Technik Nachrichten Deutschland RSS",
            "IT News Deutschland RSS feed",
        ),
        ENTERTAINMENT to listOf(
            "Unterhaltung Deutschland RSS",
            "Promi Nachrichten Deutschland RSS feed",
        ),
        BUSINESS to listOf(
            "Wirtschaftsnachrichten Deutschland RSS",
            "Business Deutschland RSS feed",
        ),
    ),
    extraDomains = listOf("heise.de", "tagesschau.de", "ndr.de", "mdr.de"),
)

private val AT = CountryDiscoveryConfig(
    countryCode = "AT",
    language = "de",
    nativeKeywords = mapOf(
        NEWS to listOf(
            "Österreich Nachrichten RSS",
            "Austria news RSS feed",
            "österreichische Zeitung RSS",
        ),
        SPORTS to listOf(
            "Sport Österreich RSS",
            "Fußball Österreich Bundesliga RSS feed",
        ),
        TECHNOLOGY to listOf(
            "Technik Nachrichten Österreich RSS",
            "IT News Austria RSS feed",
        ),
        ENTERTAINMENT to listOf(
            "Unterhaltung Österreich RSS",
            "Promi Nachrichten Austria RSS feed",
        ),
        BUSINESS to listOf(
            "Wirtschaft Österreich RSS",
            "Business Austria RSS feed",
        ),
    ),
    extraDomains = listOf("orf.at", "derstandard.at", "kleinezeitung.at"),
)

private val CH = CountryDiscoveryConfig(
    countryCode = "CH",
    language = "de",  // Switzerland — DE, FR, IT, RM all official. NativeLanguageKeywords picks de.
    nativeKeywords = mapOf(
        NEWS to listOf(
            "Schweiz Nachrichten RSS",
            "Schweizer News RSS feed",
            "actualité Suisse RSS",  // FR speakers too
        ),
        SPORTS to listOf(
            "Sport Schweiz RSS",
            "Schweizer Fußball RSS feed",
        ),
        TECHNOLOGY to listOf(
            "Technik Schweiz RSS",
            "IT News Schweiz RSS feed",
        ),
        ENTERTAINMENT to listOf(
            "Unterhaltung Schweiz RSS",
            "Promi Schweiz RSS feed",
        ),
        BUSINESS to listOf(
            "Wirtschaft Schweiz RSS",
            "Business Schweiz RSS feed",
        ),
    ),
    extraDomains = listOf("srf.ch"),
)

// ─── French ────────────────────────────────────────────────────────────────────

private val FR = CountryDiscoveryConfig(
    countryCode = "FR",
    language = "fr",
    nativeKeywords = mapOf(
        NEWS to listOf(
            "actualités France RSS",
            "France info RSS feed",
            "journal français flux RSS",
        ),
        SPORTS to listOf(
            "actualités sport France RSS",
            "football français Ligue 1 RSS feed",
            "L'Équipe flux RSS",
        ),
        TECHNOLOGY to listOf(
            "actualités tech France RSS",
            "high-tech France flux RSS feed",
        ),
        ENTERTAINMENT to listOf(
            "actualités people France RSS",
            "divertissement France RSS feed",
        ),
        BUSINESS to listOf(
            "actualité économique France RSS",
            "business France RSS feed",
        ),
    ),
    extraDomains = listOf("lequipe.fr", "bfmtv.com"),
)

private val BE = CountryDiscoveryConfig(
    countryCode = "BE",
    language = "fr",  // NativeLanguageKeywords maps BE → fr. Flemish queries below as supplement.
    nativeKeywords = mapOf(
        NEWS to listOf(
            "actualités Belgique RSS",
            "Belgique info RSS feed",
            "Belgisch nieuws RSS",        // Dutch/Flemish
            "Vlaams nieuws RSS feed",
        ),
        SPORTS to listOf(
            "sport Belgique RSS",
            "football belge Jupiler Pro League RSS feed",
            "Belgisch voetbal RSS",
        ),
        TECHNOLOGY to listOf(
            "tech Belgique RSS",
            "technologie Belgique flux RSS feed",
        ),
        ENTERTAINMENT to listOf(
            "divertissement Belgique RSS",
            "people Belgique RSS feed",
        ),
        BUSINESS to listOf(
            "économie Belgique RSS",
            "business Belgique RSS feed",
        ),
    ),
    extraDomains = listOf("hln.be", "nieuwsblad.be", "lesoir.be"),
)

// ─── Italian ───────────────────────────────────────────────────────────────────

private val IT = CountryDiscoveryConfig(
    countryCode = "IT",
    language = "it",
    nativeKeywords = mapOf(
        NEWS to listOf(
            "notizie Italia RSS",
            "Italia news RSS feed",
            "giornale italiano feed RSS",
        ),
        SPORTS to listOf(
            "sport Italia RSS",
            "calcio italiano Serie A RSS feed",
            "Gazzetta dello Sport RSS",
        ),
        TECHNOLOGY to listOf(
            "tecnologia Italia RSS",
            "notizie tech Italia RSS feed",
        ),
        ENTERTAINMENT to listOf(
            "spettacolo Italia RSS",
            "gossip Italia RSS feed",
        ),
        BUSINESS to listOf(
            "economia Italia RSS",
            "business Italia RSS feed",
        ),
    ),
    extraDomains = listOf("gazzetta.it", "rai.it"),
)

// ─── Portuguese ────────────────────────────────────────────────────────────────

private val BR = CountryDiscoveryConfig(
    countryCode = "BR",
    language = "pt",
    nativeKeywords = mapOf(
        NEWS to listOf(
            "notícias Brasil RSS",
            "Brasil notícias RSS feed",
            "jornal brasileiro RSS",
        ),
        SPORTS to listOf(
            "esporte Brasil RSS",
            "futebol brasileiro Brasileirão RSS feed",
            "notícias esportivas Brasil RSS",
        ),
        TECHNOLOGY to listOf(
            "tecnologia Brasil RSS",
            "TI notícias Brasil RSS feed",
        ),
        ENTERTAINMENT to listOf(
            "entretenimento Brasil RSS",
            "celebridades Brasil RSS feed",
        ),
        BUSINESS to listOf(
            "economia Brasil RSS",
            "negócios Brasil RSS feed",
        ),
    ),
    extraDomains = listOf("metropoles.com", "agenciabrasil.ebc.com.br", "exame.com"),
)

// ─── East Asian ────────────────────────────────────────────────────────────────

private val JP = CountryDiscoveryConfig(
    countryCode = "JP",
    language = "ja",
    nativeKeywords = mapOf(
        NEWS to listOf(
            "日本 ニュース RSS フィード",
            "日本のニュース RSS",
            "新聞 RSS feed 日本",
        ),
        SPORTS to listOf(
            "スポーツ ニュース 日本 RSS",
            "野球 サッカー RSS フィード",
            "Jリーグ プロ野球 RSS",
        ),
        TECHNOLOGY to listOf(
            "テクノロジー ニュース 日本 RSS",
            "IT ニュース 日本 RSS feed",
        ),
        ENTERTAINMENT to listOf(
            "エンタメ ニュース 日本 RSS",
            "芸能 ニュース RSS フィード",
        ),
        BUSINESS to listOf(
            "ビジネス ニュース 日本 RSS",
            "経済 ニュース RSS feed",
        ),
    ),
    extraDomains = listOf("kyodonews.net", "the-japan-news.com"),
)

private val CN = CountryDiscoveryConfig(
    countryCode = "CN",
    language = "zh",
    nativeKeywords = mapOf(
        NEWS to listOf(
            "中国 新闻 RSS 订阅",
            "中国新闻 RSS",
            "中文 新闻 RSS feed",
        ),
        SPORTS to listOf(
            "中国 体育新闻 RSS",
            "中超 足球 RSS feed",
        ),
        TECHNOLOGY to listOf(
            "中国 科技新闻 RSS",
            "IT 新闻 中国 RSS feed",
        ),
        ENTERTAINMENT to listOf(
            "中国 娱乐新闻 RSS",
            "影视 综艺 RSS feed",
        ),
        BUSINESS to listOf(
            "中国 财经新闻 RSS",
            "经济 新闻 RSS feed",
        ),
    ),
    extraDomains = listOf("xinhuanet.com", "cgtn.com", "sixthtone.com"),
)

private val KR = CountryDiscoveryConfig(
    countryCode = "KR",
    language = "ko",
    nativeKeywords = mapOf(
        NEWS to listOf(
            "한국 뉴스 RSS",
            "한국 신문 RSS feed",
            "대한민국 뉴스 RSS",
        ),
        SPORTS to listOf(
            "한국 스포츠 뉴스 RSS",
            "축구 야구 RSS feed",
            "K리그 KBO RSS",
        ),
        TECHNOLOGY to listOf(
            "한국 IT 뉴스 RSS",
            "기술 뉴스 RSS feed 한국",
        ),
        ENTERTAINMENT to listOf(
            "한국 연예 뉴스 RSS",
            "K-pop 뉴스 RSS feed",
        ),
        BUSINESS to listOf(
            "한국 경제 뉴스 RSS",
            "비즈니스 뉴스 RSS feed",
        ),
    ),
    extraDomains = emptyList(),
)

// ─── South Asian / SEA ─────────────────────────────────────────────────────────

private val IN = CountryDiscoveryConfig(
    countryCode = "IN",
    // NativeLanguageKeywords.kt maps IN → "en"; Hindi keywords below are
    // supplemental to surface devanagari sites.
    language = "en",
    nativeKeywords = mapOf(
        NEWS to listOf(
            "India news RSS feed",
            "Indian news RSS",
            "भारत समाचार RSS",
            "हिंदी समाचार RSS feed",
        ),
        SPORTS to listOf(
            "India sports RSS feed",
            "cricket news IPL RSS",
            "क्रिकेट समाचार RSS feed",
        ),
        TECHNOLOGY to listOf(
            "India tech news RSS",
            "Indian technology RSS feed",
            "तकनीक समाचार RSS",
        ),
        ENTERTAINMENT to listOf(
            "India entertainment RSS",
            "Bollywood news RSS feed",
            "बॉलीवुड समाचार RSS",
        ),
        BUSINESS to listOf(
            "India business news RSS",
            "Indian economy RSS feed",
            "भारत व्यापार RSS",
        ),
    ),
    extraDomains = listOf("ndtv.com", "firstpost.com", "news18.com"),
)

private val TH = CountryDiscoveryConfig(
    countryCode = "TH",
    language = "th",
    nativeKeywords = mapOf(
        NEWS to listOf(
            "ข่าว ประเทศไทย RSS",
            "ข่าวไทย RSS feed",
            "หนังสือพิมพ์ไทย RSS",
        ),
        SPORTS to listOf(
            "ข่าวกีฬาไทย RSS",
            "ฟุตบอลไทยลีก RSS feed",
        ),
        TECHNOLOGY to listOf(
            "ข่าวเทคโนโลยี ไทย RSS",
            "ไอที ข่าว RSS feed",
        ),
        ENTERTAINMENT to listOf(
            "ข่าวบันเทิงไทย RSS",
            "ดารา RSS feed",
        ),
        BUSINESS to listOf(
            "ข่าวเศรษฐกิจไทย RSS",
            "ธุรกิจ ข่าว RSS feed",
        ),
    ),
    extraDomains = emptyList(),
)

private val VN = CountryDiscoveryConfig(
    countryCode = "VN",
    language = "vi",
    nativeKeywords = mapOf(
        NEWS to listOf(
            "tin tức Việt Nam RSS",
            "báo Việt Nam RSS feed",
            "tin nóng RSS",
        ),
        SPORTS to listOf(
            "thể thao Việt Nam RSS",
            "bóng đá Việt Nam V-League RSS feed",
        ),
        TECHNOLOGY to listOf(
            "công nghệ Việt Nam RSS",
            "tin IT RSS feed",
        ),
        ENTERTAINMENT to listOf(
            "giải trí Việt Nam RSS",
            "showbiz Việt Nam RSS feed",
        ),
        BUSINESS to listOf(
            "kinh tế Việt Nam RSS",
            "kinh doanh Việt Nam RSS feed",
        ),
    ),
    extraDomains = emptyList(),
)

private val ID = CountryDiscoveryConfig(
    countryCode = "ID",
    language = "id",
    nativeKeywords = mapOf(
        NEWS to listOf(
            "berita Indonesia RSS",
            "Indonesia news RSS feed",
            "koran Indonesia RSS",
        ),
        SPORTS to listOf(
            "berita olahraga Indonesia RSS",
            "sepak bola Liga 1 RSS feed",
        ),
        TECHNOLOGY to listOf(
            "berita teknologi Indonesia RSS",
            "IT Indonesia RSS feed",
        ),
        ENTERTAINMENT to listOf(
            "berita hiburan Indonesia RSS",
            "selebriti Indonesia RSS feed",
        ),
        BUSINESS to listOf(
            "berita ekonomi Indonesia RSS",
            "bisnis Indonesia RSS feed",
        ),
    ),
    extraDomains = emptyList(),
)

private val MY = CountryDiscoveryConfig(
    countryCode = "MY",
    language = "ms",
    nativeKeywords = mapOf(
        NEWS to listOf(
            "berita Malaysia RSS",
            "Malaysia news RSS feed",
            "akhbar Malaysia RSS",
        ),
        SPORTS to listOf(
            "berita sukan Malaysia RSS",
            "bola sepak Liga Super RSS feed",
        ),
        TECHNOLOGY to listOf(
            "berita teknologi Malaysia RSS",
            "tech Malaysia RSS feed",
        ),
        ENTERTAINMENT to listOf(
            "berita hiburan Malaysia RSS",
            "selebriti Malaysia RSS feed",
        ),
        BUSINESS to listOf(
            "berita ekonomi Malaysia RSS",
            "perniagaan Malaysia RSS feed",
        ),
    ),
    extraDomains = emptyList(),
)

private val PH = CountryDiscoveryConfig(
    countryCode = "PH",
    language = "tl",
    nativeKeywords = mapOf(
        NEWS to listOf(
            "Philippines news RSS feed",
            "balita Pilipinas RSS",
            "Filipino news RSS feed",
        ),
        SPORTS to listOf(
            "Philippines sports RSS feed",
            "PBA basketball RSS",
            "balitang sports Pilipinas RSS",
        ),
        TECHNOLOGY to listOf(
            "Philippines tech news RSS",
            "Filipino technology RSS feed",
        ),
        ENTERTAINMENT to listOf(
            "Philippines entertainment RSS",
            "showbiz Pilipinas RSS feed",
        ),
        BUSINESS to listOf(
            "Philippines business news RSS",
            "negosyo Pilipinas RSS feed",
        ),
    ),
    extraDomains = emptyList(),
)

// ─── Slavic / Eastern European ─────────────────────────────────────────────────

private val RU = CountryDiscoveryConfig(
    countryCode = "RU",
    language = "ru",
    nativeKeywords = mapOf(
        NEWS to listOf(
            "новости России RSS",
            "RSS лента новостей Россия",
            "российская газета RSS feed",
        ),
        SPORTS to listOf(
            "спорт Россия RSS",
            "футбол России РПЛ RSS feed",
            "спортивные новости RSS",
        ),
        TECHNOLOGY to listOf(
            "технологии Россия RSS",
            "ИТ новости RSS feed",
        ),
        ENTERTAINMENT to listOf(
            "развлечения Россия RSS",
            "шоу-бизнес RSS feed",
        ),
        BUSINESS to listOf(
            "экономика России RSS",
            "бизнес Россия RSS feed",
        ),
    ),
    extraDomains = listOf("kommersant.ru", "iz.ru", "lenta.ru"),
)

private val PL = CountryDiscoveryConfig(
    countryCode = "PL",
    language = "pl",
    nativeKeywords = mapOf(
        NEWS to listOf(
            "wiadomości Polska RSS",
            "Polska news RSS feed",
            "gazeta polska RSS",
        ),
        SPORTS to listOf(
            "sport Polska RSS",
            "piłka nożna Ekstraklasa RSS feed",
        ),
        TECHNOLOGY to listOf(
            "technologia Polska RSS",
            "IT wiadomości RSS feed",
        ),
        ENTERTAINMENT to listOf(
            "rozrywka Polska RSS",
            "showbiznes RSS feed",
        ),
        BUSINESS to listOf(
            "biznes Polska RSS",
            "ekonomia Polska RSS feed",
        ),
    ),
    extraDomains = listOf("tvn24.pl", "polsatnews.pl"),
)

private val CZ = CountryDiscoveryConfig(
    countryCode = "CZ",
    language = "cs",
    nativeKeywords = mapOf(
        NEWS to listOf(
            "zprávy Česko RSS",
            "ČR zprávy RSS feed",
            "česká média RSS",
        ),
        SPORTS to listOf(
            "sport Česko RSS",
            "fotbal Fortuna liga RSS feed",
        ),
        TECHNOLOGY to listOf(
            "technologie Česko RSS",
            "IT zprávy RSS feed",
        ),
        ENTERTAINMENT to listOf(
            "zábava Česko RSS",
            "showbyznys RSS feed",
        ),
        BUSINESS to listOf(
            "byznys Česko RSS",
            "ekonomika Česko RSS feed",
        ),
    ),
    extraDomains = listOf("ct24.ceskatelevize.cz", "novinky.cz", "irozhlas.cz"),
)

private val HU = CountryDiscoveryConfig(
    countryCode = "HU",
    language = "hu",
    nativeKeywords = mapOf(
        NEWS to listOf(
            "Magyarország hírek RSS",
            "magyar hírek RSS feed",
            "magyar sajtó RSS",
        ),
        SPORTS to listOf(
            "magyar sport hírek RSS",
            "foci NB I RSS feed",
        ),
        TECHNOLOGY to listOf(
            "technológia Magyarország RSS",
            "IT hírek RSS feed",
        ),
        ENTERTAINMENT to listOf(
            "szórakozás magyar RSS",
            "bulvár RSS feed",
        ),
        BUSINESS to listOf(
            "gazdaság Magyarország RSS",
            "üzleti hírek RSS feed",
        ),
    ),
    extraDomains = listOf("hvg.hu", "444.hu"),
)

private val GR = CountryDiscoveryConfig(
    countryCode = "GR",
    language = "el",
    nativeKeywords = mapOf(
        NEWS to listOf(
            "ειδήσεις Ελλάδα RSS",
            "Ελλάδα νέα RSS feed",
            "ελληνική εφημερίδα RSS",
        ),
        SPORTS to listOf(
            "αθλητικά Ελλάδα RSS",
            "ποδόσφαιρο Super League RSS feed",
        ),
        TECHNOLOGY to listOf(
            "τεχνολογία Ελλάδα RSS",
            "IT ειδήσεις RSS feed",
        ),
        ENTERTAINMENT to listOf(
            "ψυχαγωγία Ελλάδα RSS",
            "celebrity Ελλάδα RSS feed",
        ),
        BUSINESS to listOf(
            "οικονομία Ελλάδα RSS",
            "επιχειρήσεις Ελλάδα RSS feed",
        ),
    ),
    extraDomains = listOf("sport24.gr"),
)

private val TR = CountryDiscoveryConfig(
    countryCode = "TR",
    language = "tr",
    nativeKeywords = mapOf(
        NEWS to listOf(
            "Türkiye haber RSS",
            "haber sitesi Türkiye RSS feed",
            "Türkçe gazete RSS",
        ),
        SPORTS to listOf(
            "Türkiye spor haber RSS",
            "Süper Lig futbol RSS feed",
        ),
        TECHNOLOGY to listOf(
            "Türkiye teknoloji haber RSS",
            "bilim teknoloji RSS feed",
        ),
        ENTERTAINMENT to listOf(
            "Türkiye magazin haber RSS",
            "dizi haber RSS feed",
        ),
        BUSINESS to listOf(
            "Türkiye ekonomi haber RSS",
            "iş dünyası RSS feed",
        ),
    ),
    extraDomains = emptyList(),
)

// ─── Nordic ────────────────────────────────────────────────────────────────────

private val SE = CountryDiscoveryConfig(
    countryCode = "SE",
    language = "sv",
    nativeKeywords = mapOf(
        NEWS to listOf(
            "svenska nyheter RSS",
            "Sverige nyheter RSS feed",
            "svensk dagstidning RSS",
        ),
        SPORTS to listOf(
            "svensk sport RSS",
            "fotboll Allsvenskan RSS feed",
        ),
        TECHNOLOGY to listOf(
            "teknik Sverige RSS",
            "IT-nyheter RSS feed",
        ),
        ENTERTAINMENT to listOf(
            "nöje Sverige RSS",
            "underhållning RSS feed",
        ),
        BUSINESS to listOf(
            "ekonomi Sverige RSS",
            "näringsliv RSS feed",
        ),
    ),
    extraDomains = listOf("svt.se"),
)

private val NO = CountryDiscoveryConfig(
    countryCode = "NO",
    language = "no",
    nativeKeywords = mapOf(
        NEWS to listOf(
            "norske nyheter RSS",
            "Norge nyheter RSS feed",
            "norsk avis RSS",
        ),
        SPORTS to listOf(
            "norsk sport RSS",
            "fotball Eliteserien RSS feed",
        ),
        TECHNOLOGY to listOf(
            "teknologi Norge RSS",
            "IT-nyheter Norge RSS feed",
        ),
        ENTERTAINMENT to listOf(
            "underholdning Norge RSS",
            "kjendis Norge RSS feed",
        ),
        BUSINESS to listOf(
            "økonomi Norge RSS",
            "næringsliv Norge RSS feed",
        ),
    ),
    extraDomains = listOf("nrk.no", "aftenposten.no"),
)

private val DK = CountryDiscoveryConfig(
    countryCode = "DK",
    language = "da",
    nativeKeywords = mapOf(
        NEWS to listOf(
            "dansk nyheder RSS",
            "Danmark nyheder RSS feed",
            "dansk avis RSS",
        ),
        SPORTS to listOf(
            "dansk sport RSS",
            "fodbold Superliga RSS feed",
        ),
        TECHNOLOGY to listOf(
            "teknologi Danmark RSS",
            "IT-nyheder RSS feed",
        ),
        ENTERTAINMENT to listOf(
            "underholdning Danmark RSS",
            "kendis nyheder RSS feed",
        ),
        BUSINESS to listOf(
            "økonomi Danmark RSS",
            "erhverv Danmark RSS feed",
        ),
    ),
    extraDomains = listOf("tv2.dk", "altinget.dk", "zetland.dk"),
)

private val FI = CountryDiscoveryConfig(
    countryCode = "FI",
    language = "fi",
    nativeKeywords = mapOf(
        NEWS to listOf(
            "suomalaiset uutiset RSS",
            "Suomi uutiset RSS feed",
            "Suomi sanomalehti RSS",
        ),
        SPORTS to listOf(
            "Suomen urheilu uutiset RSS",
            "jalkapallo Veikkausliiga RSS feed",
        ),
        TECHNOLOGY to listOf(
            "teknologia uutiset Suomi RSS",
            "IT uutiset RSS feed",
        ),
        ENTERTAINMENT to listOf(
            "viihde uutiset Suomi RSS",
            "Suomi julkkis RSS feed",
        ),
        BUSINESS to listOf(
            "talous Suomi RSS",
            "yritysuutiset RSS feed",
        ),
    ),
    extraDomains = listOf("kauppalehti.fi", "verkkouutiset.fi"),
)

// ─── Dutch ─────────────────────────────────────────────────────────────────────

private val NL = CountryDiscoveryConfig(
    countryCode = "NL",
    language = "nl",
    nativeKeywords = mapOf(
        NEWS to listOf(
            "Nederlands nieuws RSS",
            "Nederland nieuws RSS feed",
            "Nederlandse krant RSS",
        ),
        SPORTS to listOf(
            "Nederlands sport nieuws RSS",
            "voetbal Eredivisie RSS feed",
        ),
        TECHNOLOGY to listOf(
            "technologie Nederland RSS",
            "IT nieuws Nederland RSS feed",
        ),
        ENTERTAINMENT to listOf(
            "entertainment Nederland RSS",
            "showbizz Nederland RSS feed",
        ),
        BUSINESS to listOf(
            "economie Nederland RSS",
            "zakelijk nieuws RSS feed",
        ),
    ),
    extraDomains = listOf("nu.nl", "nos.nl"),
)

// ─── English-speaking ──────────────────────────────────────────────────────────
// For English-native countries, the "native language" advantage from Stage A
// doesn't apply — we differentiate via country-specific terms and league names.

private val US = CountryDiscoveryConfig(
    countryCode = "US",
    language = "en",
    nativeKeywords = mapOf(
        NEWS to listOf(
            "USA news RSS feed",
            "American news RSS",
            "United States news RSS feed",
        ),
        SPORTS to listOf(
            "USA sports RSS feed",
            "NFL NBA MLB NHL RSS",
            "American sports news RSS feed",
        ),
        TECHNOLOGY to listOf(
            "USA tech news RSS feed",
            "Silicon Valley news RSS",
        ),
        ENTERTAINMENT to listOf(
            "USA entertainment RSS feed",
            "Hollywood news RSS",
        ),
        BUSINESS to listOf(
            "USA business news RSS feed",
            "Wall Street RSS",
        ),
    ),
    extraDomains = listOf("apnews.com", "bloomberg.com"),
)

private val GB = CountryDiscoveryConfig(
    countryCode = "GB",
    language = "en",
    nativeKeywords = mapOf(
        NEWS to listOf(
            "UK news RSS feed",
            "British news RSS",
            "Britain news RSS feed",
        ),
        SPORTS to listOf(
            "UK sports RSS feed",
            "Premier League football RSS",
            "British sports news RSS",
        ),
        TECHNOLOGY to listOf(
            "UK tech news RSS feed",
            "British technology RSS",
        ),
        ENTERTAINMENT to listOf(
            "UK entertainment RSS feed",
            "British showbiz RSS",
        ),
        BUSINESS to listOf(
            "UK business news RSS feed",
            "London Stock Exchange RSS",
        ),
    ),
    extraDomains = listOf("ft.com"),
)

private val CA = CountryDiscoveryConfig(
    countryCode = "CA",
    language = "en",
    nativeKeywords = mapOf(
        NEWS to listOf(
            "Canada news RSS feed",
            "Canadian news RSS",
            "actualités Canada RSS feed",  // FR-CA
        ),
        SPORTS to listOf(
            "Canada sports RSS feed",
            "NHL hockey CFL RSS",
            "Canadian sports news RSS",
        ),
        TECHNOLOGY to listOf(
            "Canada tech news RSS feed",
            "Canadian technology RSS",
        ),
        ENTERTAINMENT to listOf(
            "Canada entertainment RSS feed",
            "Canadian celebrities RSS",
        ),
        BUSINESS to listOf(
            "Canada business news RSS feed",
            "Canadian economy RSS",
        ),
    ),
    extraDomains = listOf("nationalpost.com", "macleans.ca"),
)

private val AU = CountryDiscoveryConfig(
    countryCode = "AU",
    language = "en",
    nativeKeywords = mapOf(
        NEWS to listOf(
            "Australia news RSS feed",
            "Australian news RSS",
            "Aussie news RSS feed",
        ),
        SPORTS to listOf(
            "Australia sports RSS feed",
            "AFL NRL cricket RSS",
            "Australian sport news RSS",
        ),
        TECHNOLOGY to listOf(
            "Australia tech news RSS feed",
            "Australian technology RSS",
        ),
        ENTERTAINMENT to listOf(
            "Australia entertainment RSS feed",
            "Aussie celebrities RSS",
        ),
        BUSINESS to listOf(
            "Australia business news RSS feed",
            "ASX Australian economy RSS",
        ),
    ),
    extraDomains = listOf("smh.com.au", "sbs.com.au", "7news.com.au"),
)

private val ZA = CountryDiscoveryConfig(
    countryCode = "ZA",
    language = "en",
    nativeKeywords = mapOf(
        NEWS to listOf(
            "South Africa news RSS feed",
            "SA news RSS",
            "South African news RSS feed",
        ),
        SPORTS to listOf(
            "South Africa sports RSS feed",
            "rugby cricket Springboks RSS",
            "PSL football South Africa RSS",
        ),
        TECHNOLOGY to listOf(
            "South Africa tech news RSS",
            "SA technology RSS feed",
        ),
        ENTERTAINMENT to listOf(
            "South Africa entertainment RSS feed",
            "SA celebrities RSS",
        ),
        BUSINESS to listOf(
            "South Africa business news RSS feed",
            "JSE economy RSS",
        ),
    ),
    extraDomains = listOf("dailymaverick.co.za", "groundup.org.za", "businesslive.co.za", "mg.co.za"),
)

private val NG = CountryDiscoveryConfig(
    countryCode = "NG",
    language = "en",
    nativeKeywords = mapOf(
        NEWS to listOf(
            "Nigeria news RSS feed",
            "Nigerian news RSS",
            "Naija news RSS feed",
        ),
        SPORTS to listOf(
            "Nigeria sports RSS feed",
            "Super Eagles football RSS",
            "Nigerian football NPFL RSS",
        ),
        TECHNOLOGY to listOf(
            "Nigeria tech news RSS feed",
            "Nigerian technology RSS",
        ),
        ENTERTAINMENT to listOf(
            "Nigeria entertainment RSS feed",
            "Nollywood news RSS",
        ),
        BUSINESS to listOf(
            "Nigeria business news RSS feed",
            "Naira economy RSS",
        ),
    ),
    extraDomains = listOf("premiumtimesng.com"),
)

private val KE = CountryDiscoveryConfig(
    countryCode = "KE",
    language = "en",
    nativeKeywords = mapOf(
        NEWS to listOf(
            "Kenya news RSS feed",
            "Kenyan news RSS",
            "habari Kenya RSS feed",  // Swahili
        ),
        SPORTS to listOf(
            "Kenya sports RSS feed",
            "Harambee Stars football RSS",
            "Kenyan athletics RSS",
        ),
        TECHNOLOGY to listOf(
            "Kenya tech news RSS feed",
            "Kenyan technology RSS",
        ),
        ENTERTAINMENT to listOf(
            "Kenya entertainment RSS feed",
            "Kenyan celebrities RSS",
        ),
        BUSINESS to listOf(
            "Kenya business news RSS feed",
            "NSE economy Kenya RSS",
        ),
    ),
    extraDomains = listOf("citizen.digital", "businessdailyafrica.com", "kenyans.co.ke", "tuko.co.ke"),
)