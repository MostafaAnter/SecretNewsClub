package secret.news.club.domain.model.rss

data class RssService(
    val name: String,
    val url: String,
    val category: RssCategory,
    val language: String,
    val countryCode: String,
    val description: String = "",
    val isNativeLanguage: Boolean = true
)
