package che.codes.posts.core.di.modules

import che.codes.posts.core.data.PostsDataSource
import che.codes.posts.core.viewmodel.PostsViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class ViewModelFactoryModule {

    @Provides
    fun providePostsViewModelFactory(dataSource: PostsDataSource): PostsViewModelFactory {
        return PostsViewModelFactory(dataSource)
    }
}