package che.codes.posts.util

import che.codes.posts.core.di.components.AppComponent
import che.codes.posts.core.di.modules.AdapterModule
import che.codes.posts.core.di.modules.DataSourceModule
import che.codes.posts.core.di.modules.NetworkModule
import che.codes.posts.core.di.modules.ViewModelFactoryModule
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