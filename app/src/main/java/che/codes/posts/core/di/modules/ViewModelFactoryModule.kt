package che.codes.posts.di.modules

import che.codes.posts.data.PostsDataSource
import che.codes.posts.viewmodels.PostsViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class ViewModelFactoryModule {

    @Provides
    fun providePostsViewModelFactory(dataSource: PostsDataSource): PostsViewModelFactory {
        return PostsViewModelFactory(dataSource)
    }
}