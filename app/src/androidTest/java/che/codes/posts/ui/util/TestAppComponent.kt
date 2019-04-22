package che.codes.posts.ui.util

import che.codes.posts.di.components.AppComponent
import che.codes.posts.di.modules.AdapterModule
import che.codes.posts.di.modules.DataSourceModule
import che.codes.posts.di.modules.NetworkModule
import che.codes.posts.di.modules.ViewModelFactoryModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ViewModelFactoryModule::class,
        NetworkModule::class,
        DataSourceModule::class,
        AdapterModule::class,
        TestPropertyModule::class
    ]
)
interface TestAppComponent : AppComponent