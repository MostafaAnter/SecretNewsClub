package secret.news.club.infrastructure.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import javax.inject.Singleton
import secret.news.club.domain.service.discovery.RssDiscoveryEngine
import secret.news.club.infrastructure.rss.discovery.BingHtmlSearchClient
import secret.news.club.infrastructure.rss.discovery.DuckDuckGoHtmlSearchClient
import secret.news.club.infrastructure.rss.discovery.RssDiscoveryEngineImpl
import secret.news.club.infrastructure.rss.discovery.SearchEngineClient

@Module
@InstallIn(SingletonComponent::class)
abstract class DiscoveryModule {

    @Binds
    @Singleton
    abstract fun bindRssDiscoveryEngine(impl: RssDiscoveryEngineImpl): RssDiscoveryEngine

    /**
     * Multibinding — discovery engine asks for `Set<SearchEngineClient>` and
     * tries them in priority order. Adding a new search backend = add a new
     * @Binds @IntoSet here, no engine changes required.
     */
    @Binds
    @IntoSet
    abstract fun bindDuckDuckGoSearchClient(impl: DuckDuckGoHtmlSearchClient): SearchEngineClient

    @Binds
    @IntoSet
    abstract fun bindBingSearchClient(impl: BingHtmlSearchClient): SearchEngineClient
}