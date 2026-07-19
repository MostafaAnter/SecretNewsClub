package secret.news.club.infrastructure.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import secret.news.club.domain.service.CrashReportingService
import secret.news.club.infrastructure.firebase.NoOpCrashReportingService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CrashReportingModule {

    @Binds
    @Singleton
    abstract fun bindCrashReportingService(impl: NoOpCrashReportingService): CrashReportingService
}
