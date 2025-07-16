package secret.news.club.infrastructure.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import secret.news.club.domain.data.ArticlePagingListUseCase
import secret.news.club.domain.data.DiffMapHolder
import secret.news.club.domain.data.FilterStateUseCase
import secret.news.club.domain.data.GroupWithFeedsListUseCase
import secret.news.club.domain.service.AccountService
import secret.news.club.domain.service.RssService
import secret.news.club.infrastructure.android.AndroidStringsHelper
import secret.news.club.infrastructure.preference.SettingsProvider

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun providesArticlePagingList(
        rssService: RssService,
        androidStringsHelper: AndroidStringsHelper,
        @ApplicationScope applicationScope: CoroutineScope,
        @IODispatcher ioDispatcher: CoroutineDispatcher,
        settingsProvider: SettingsProvider,
        filterStateUseCase: FilterStateUseCase,
        accountService: AccountService,
    ): ArticlePagingListUseCase {
        return ArticlePagingListUseCase(
            rssService,
            androidStringsHelper,
            applicationScope,
            ioDispatcher,
            settingsProvider,
            filterStateUseCase,
            accountService,
        )
    }

    @Provides
    @Singleton
    fun providesFilterState(settingsProvider: SettingsProvider): FilterStateUseCase {
        return FilterStateUseCase(settingsProvider)
    }

    @Provides
    @Singleton
    fun providesGroupWithFeedsList(
        @ApplicationScope applicationScope: CoroutineScope,
        @IODispatcher ioDispatcher: CoroutineDispatcher,
        settingsProvider: SettingsProvider,
        rssService: RssService,
        filterStateUseCase: FilterStateUseCase,
        diffMapHolder: DiffMapHolder,
        accountService: AccountService,
    ): GroupWithFeedsListUseCase {
        return GroupWithFeedsListUseCase(
            applicationScope,
            ioDispatcher,
            settingsProvider,
            rssService,
            filterStateUseCase,
            diffMapHolder,
            accountService,
        )
    }
}
