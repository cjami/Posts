package che.codes.posts.core.di.modules

import che.codes.posts.core.PostsApplication
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
class NetworkModule {
    private val cacheSize: Long = 5 * 1024 * 1024

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().cache(Cache(PostsApplication.instance.cacheDir, cacheSize)).build()
    }
}