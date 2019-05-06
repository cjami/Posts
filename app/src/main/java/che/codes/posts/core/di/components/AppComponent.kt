package che.codes.posts.di.components

import che.codes.posts.di.modules.*
import che.codes.posts.ui.activities.PostDetailsActivity
import che.codes.posts.ui.activities.PostListActivity
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