package che.codes.posts.ui.util

import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class TestPropertyModule(private val baseApiUrl: String) {

    @Provides
    @Named("base.api.url")
    fun provideBaseUrl(): String {
        return baseApiUrl
    }

    @Provides
    @Named("base.avatar.url")
    fun provideBaseAvatarUrl(): String {
        return "https://api.adorable.io/avatars/200/"
    }
}