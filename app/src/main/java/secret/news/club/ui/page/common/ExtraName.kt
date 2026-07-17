package secret.news.club.ui.page.common

object ExtraName {

    const val ARTICLE_ID: String = "article.id"
    const val ARTICLE_URL: String = "article.url"

    /**
     * Raw FCM data payload key for the article URL (see PushMessagingService). Android's
     * FCM SDK auto-builds and launches the notification itself — bypassing PushMessagingService
     * — whenever a push carries a "notification" payload and the app is backgrounded/killed; the
     * intent it launches carries this raw wire-format key instead of [ARTICLE_URL].
     */
    const val FCM_DATA_ARTICLE_URL: String = "article_url"
}
