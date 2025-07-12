package secret.news.club.domain.model.feed

import androidx.room.Embedded
import androidx.room.Relation
import secret.news.club.domain.model.article.Article

/**
 * A [feed] contains many [articles].
 */
data class FeedWithArticle(
    @Embedded
    var feed: Feed,
    @Relation(parentColumn = "id", entityColumn = "feedId")
    var articles: List<Article>,
)
