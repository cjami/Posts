package che.codes.posts.core.di.components

import che.codes.posts.core.di.modules.*
import che.codes.posts.features.details.PostDetailsActivity
import che.codes.posts.features.list.PostListActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        NetworkModule::class,
        DataSourceModule::class,
        ViewModelFactoryModule::class,
        AdapterModule::class,
        PropertyModule::class
    ]
)
interface AppComponent {
    fun inject(activity: PostListActivity)
    fun inject(activity: PostDetailsActivity)
}