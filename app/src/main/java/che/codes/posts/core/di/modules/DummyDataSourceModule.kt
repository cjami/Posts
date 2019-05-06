package che.codes.posts.core.di.modules

import che.codes.posts.core.data.PostsDataSource
import che.codes.posts.core.data.jsonplaceholder.JpPostsDataSource
import che.codes.posts.core.data.jsonplaceholder.JpPostsDummyService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DummyDataSourceModule {

    @Singleton
    @Provides
    fun providePostsDataSource(): PostsDataSource {
        return JpPostsDataSource(JpPostsDummyService())
    }
}