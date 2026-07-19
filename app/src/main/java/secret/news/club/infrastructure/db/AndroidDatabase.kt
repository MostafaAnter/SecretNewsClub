package secret.news.club.infrastructure.db

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import secret.news.club.domain.model.account.*
import secret.news.club.domain.model.account.security.DESUtils
import secret.news.club.domain.model.article.ArchivedArticle
import secret.news.club.domain.model.article.Article
import secret.news.club.domain.model.feed.Feed
import secret.news.club.domain.model.group.Group
import secret.news.club.domain.model.rss.DiscoveredFeedEntity
import secret.news.club.domain.repository.AccountDao
import secret.news.club.domain.repository.ArticleDao
import secret.news.club.domain.repository.DiscoveredFeedDao
import secret.news.club.domain.repository.FeedDao
import secret.news.club.domain.repository.GroupDao
import secret.news.club.infrastructure.preference.*
import secret.news.club.ui.ext.toInt
import java.util.*

@Database(
    entities = [
        Account::class,
        Feed::class,
        Article::class,
        Group::class,
        ArchivedArticle::class,
        DiscoveredFeedEntity::class,
    ],
    version = 9,
    autoMigrations = [
        AutoMigration(from = 5, to = 6),
        AutoMigration(from = 5, to = 7),
        AutoMigration(from = 6, to = 7),
    ]
)
@TypeConverters(
    AndroidDatabase.DateConverters::class,
    AccountTypeConverters::class,
    SyncIntervalConverters::class,
    SyncOnStartConverters::class,
    SyncOnlyOnWiFiConverters::class,
    SyncOnlyWhenChargingConverters::class,
    KeepArchivedConverters::class,
    SyncBlockListConverters::class,
)
abstract class AndroidDatabase : RoomDatabase() {

    abstract fun accountDao(): AccountDao
    abstract fun feedDao(): FeedDao
    abstract fun articleDao(): ArticleDao
    abstract fun groupDao(): GroupDao
    abstract fun discoveredFeedDao(): DiscoveredFeedDao

    companion object {

        private var instance: AndroidDatabase? = null

        fun getInstance(context: Context): AndroidDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AndroidDatabase::class.java,
                    "Reader"
                ).addMigrations(*allMigrations).build().also {
                    instance = it
                }
            }
        }
    }

    class DateConverters {

        @TypeConverter
        fun toDate(dateLong: Long?): Date? {
            return dateLong?.let { Date(it) }
        }

        @TypeConverter
        fun fromDate(date: Date?): Long? {
            return date?.time
        }
    }
}

val allMigrations = arrayOf(
    MIGRATION_1_2,
    MIGRATION_2_3,
    MIGRATION_3_4,
    MIGRATION_4_5,
    MIGRATION_7_8,
    MIGRATION_8_9,
)

@Suppress("ClassName")
object MIGRATION_1_2 : Migration(1, 2) {

    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            """
            ALTER TABLE article ADD COLUMN img TEXT DEFAULT NULL
            """.trimIndent()
        )
    }
}

@Suppress("ClassName")
object MIGRATION_2_3 : Migration(2, 3) {

    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            """
            ALTER TABLE article ADD COLUMN updateAt INTEGER DEFAULT ${System.currentTimeMillis()}
            """.trimIndent()
        )
        database.execSQL(
            """
            ALTER TABLE account ADD COLUMN syncInterval INTEGER NOT NULL DEFAULT ${SyncIntervalPreference.default.value}
            """.trimIndent()
        )
        database.execSQL(
            """
            ALTER TABLE account ADD COLUMN syncOnStart INTEGER NOT NULL DEFAULT ${SyncOnStartPreference.default.value.toInt()}
            """.trimIndent()
        )
        database.execSQL(
            """
            ALTER TABLE account ADD COLUMN syncOnlyOnWiFi INTEGER NOT NULL DEFAULT ${SyncOnlyOnWiFiPreference.default.value.toInt()}
            """.trimIndent()
        )
        database.execSQL(
            """
            ALTER TABLE account ADD COLUMN syncOnlyWhenCharging INTEGER NOT NULL DEFAULT ${SyncOnlyWhenChargingPreference.default.value.toInt()}
            """.trimIndent()
        )
        database.execSQL(
            """
            ALTER TABLE account ADD COLUMN keepArchived INTEGER NOT NULL DEFAULT ${KeepArchivedPreference.default.value}
            """.trimIndent()
        )
        database.execSQL(
            """
            ALTER TABLE account ADD COLUMN syncBlockList TEXT NOT NULL DEFAULT ''
            """.trimIndent()
        )
    }
}

@Suppress("ClassName")
object MIGRATION_3_4 : Migration(3, 4) {

    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            """
            ALTER TABLE account ADD COLUMN securityKey TEXT DEFAULT '${DESUtils.empty}'
            """.trimIndent()
        )
    }
}

@Suppress("ClassName")
object MIGRATION_4_5 : Migration(4, 5) {

    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            """
            ALTER TABLE account ADD COLUMN lastArticleId TEXT DEFAULT NULL
            """.trimIndent()
        )
    }
}

/**
 * Adds the [DiscoveredFeedEntity] table only. Existing tables untouched —
 * this is purely additive, so the article/feed/account paths are unaffected
 * even if the migration is later rolled back.
 *
 * Statements lifted from Room's generated schema for v8 to guarantee the
 * column types and indices match what Room expects on first open.
 */
@Suppress("ClassName")
object MIGRATION_7_8 : Migration(7, 8) {

    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `discovered_feed` (
                `url` TEXT NOT NULL,
                `name` TEXT NOT NULL,
                `category` TEXT NOT NULL,
                `language` TEXT NOT NULL,
                `countryCode` TEXT NOT NULL,
                `source` TEXT NOT NULL,
                `contentFingerprint` TEXT,
                `discoveredAt` INTEGER NOT NULL,
                `lastValidatedAt` INTEGER NOT NULL,
                PRIMARY KEY(`url`)
            )
            """.trimIndent()
        )
        database.execSQL(
            "CREATE INDEX IF NOT EXISTS `index_discovered_feed_countryCode` ON `discovered_feed` (`countryCode`)"
        )
        database.execSQL(
            "CREATE INDEX IF NOT EXISTS `index_discovered_feed_contentFingerprint` ON `discovered_feed` (`contentFingerprint`)"
        )
    }
}

/**
 * Adds composite indices on `article` matching the filtered-count and paged-article-list
 * query shapes (accountId+isUnread/isStarred, feedId+isUnread/isStarred). Those queries were
 * doing a full table scan of `article` on every Feeds-page load and every Flow-page paging
 * fetch — the more articles a user has cached, the slower every scroll/refresh got. Purely
 * additive (CREATE INDEX only), so existing data/tables are untouched.
 */
@Suppress("ClassName")
object MIGRATION_8_9 : Migration(8, 9) {

    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "CREATE INDEX IF NOT EXISTS `index_article_accountId_isUnread` ON `article` (`accountId`, `isUnread`)"
        )
        database.execSQL(
            "CREATE INDEX IF NOT EXISTS `index_article_accountId_isStarred` ON `article` (`accountId`, `isStarred`)"
        )
        database.execSQL(
            "CREATE INDEX IF NOT EXISTS `index_article_feedId_isUnread` ON `article` (`feedId`, `isUnread`)"
        )
        database.execSQL(
            "CREATE INDEX IF NOT EXISTS `index_article_feedId_isStarred` ON `article` (`feedId`, `isStarred`)"
        )
    }
}
