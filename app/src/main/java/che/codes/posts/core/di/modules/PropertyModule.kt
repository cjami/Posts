package che.codes.posts.core.di.modules

import dagger.Module
import dagger.Provides
import javax.inject.Named

private const val BASE_API_URL = "https://jsonplaceholder.typicode.com/"
private const val BASE_AVATAR_URL = "https://api.adorable.io/avatars/200/"

@Module
class PropertyModule {

    @Provides
    @Named("base.api.url")
    fun provideBaseApiUrl(): String {
        return BASE_API_URL
    }

    @Provides
    @Named("base.avatar.url")
    fun provideBaseAvatarUrl():String{
        return BASE_AVATAR_URL
    }
}