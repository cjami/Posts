package che.codes.posts.di.modules

import che.codes.posts.data.PostsDataSource
import che.codes.posts.data.jsonplaceholder.JpPostsDataSource
import che.codes.posts.data.jsonplaceholder.JpPostsDummyService
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