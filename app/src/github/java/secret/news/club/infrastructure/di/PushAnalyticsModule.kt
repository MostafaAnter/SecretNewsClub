package secret.news.club.infrastructure.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import secret.news.club.domain.service.PushAnalyticsService
import secret.news.club.infrastructure.firebase.FirebasePushAnalyticsService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PushAnalyticsModule {

    @Binds
    @Singleton
    abstract fun bindPushAnalyticsService(impl: FirebasePushAnalyticsService): PushAnalyticsService
}
