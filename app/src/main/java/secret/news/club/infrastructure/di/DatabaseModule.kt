package secret.news.club.infrastructure.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import secret.news.club.domain.repository.AccountDao
import secret.news.club.domain.repository.ArticleDao
import secret.news.club.domain.repository.FeedDao
import secret.news.club.domain.repository.GroupDao
import secret.news.club.infrastructure.db.AndroidDatabase
import javax.inject.Singleton

/**
 * Provides Data Access Objects for database.
 *
 * - [ArticleDao]
 * - [FeedDao]
 * - [GroupDao]
 * - [AccountDao]
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideArticleDao(androidDatabase: AndroidDatabase): ArticleDao =
        androidDatabase.articleDao()

    @Provides
    @Singleton
    fun provideFeedDao(androidDatabase: AndroidDatabase): FeedDao =
        androidDatabase.feedDao()

    @Provides
    @Singleton
    fun provideGroupDao(androidDatabase: AndroidDatabase): GroupDao =
        androidDatabase.groupDao()

    @Provides
    @Singleton
    fun provideAccountDao(androidDatabase: AndroidDatabase): AccountDao =
        androidDatabase.accountDao()

    @Provides
    @Singleton
    fun provideReaderDatabase(@ApplicationContext context: Context): AndroidDatabase =
        AndroidDatabase.getInstance(context)
}
