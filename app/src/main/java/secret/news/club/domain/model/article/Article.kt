package secret.news.club.domain.model.article

import androidx.room.*
import secret.news.club.domain.model.feed.Feed
import java.util.*

/**
 * TODO: Add class description
 */
@Entity(
    tableName = "article",
    foreignKeys = [ForeignKey(
        entity = Feed::class,
        parentColumns = ["id"],
        childColumns = ["feedId"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )],
    // accountId/feedId already have single-column indices below via @ColumnInfo(index = true).
    // These composite ones match the actual WHERE-clause shapes in ArticleDao (unread/starred
    // counts + paged article lists, both account-wide and per-feed) so those queries hit an
    // index instead of a full table scan of the article table.
    indices = [
        Index(value = ["accountId", "isUnread"]),
        Index(value = ["accountId", "isStarred"]),
        Index(value = ["feedId", "isUnread"]),
        Index(value = ["feedId", "isStarred"]),
    ]
)
data class Article(
    @PrimaryKey
    var id: String,
    @ColumnInfo
    var date: Date,
    @ColumnInfo
    var title: String,
    @ColumnInfo
    var author: String? = null,
    @ColumnInfo
    var rawDescription: String,
    @ColumnInfo
    var shortDescription: String,
    @ColumnInfo
    @Deprecated("fullContent is the same as rawDescription")
    var fullContent: String? = null,
    @ColumnInfo
    var img: String? = null,
    @ColumnInfo
    var link: String,
    @ColumnInfo(index = true)
    var feedId: String,
    @ColumnInfo(index = true)
    var accountId: Int,
    @ColumnInfo
    var isUnread: Boolean = true,
    @ColumnInfo
    var isStarred: Boolean = false,
    @ColumnInfo
    var isReadLater: Boolean = false,
    @ColumnInfo
    var updateAt: Date? = null,
) {

    @Ignore
    var dateString: String? = null
}
