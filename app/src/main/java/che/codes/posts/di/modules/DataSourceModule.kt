package che.codes.posts.di.modules

import che.codes.posts.data.PostsDataSource
import che.codes.posts.data.jsonplaceholder.JpPostsDataSource
import che.codes.posts.data.jsonplaceholder.JpPostsDummyService
import che.codes.posts.data.jsonplaceholder.JpPostsService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
open class DataSourceModule {

    @Singleton
    @Provides
    fun providePostsDataSource(okHttpClient: OkHttpClient, @Named("base.api.url") baseUrl: String): PostsDataSource {
        val retrofit = Retrofit.Builder().baseUrl(baseUrl)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return JpPostsDataSource(retrofit.create(JpPostsService::class.java))
//        return JpPostsDataSource(JpPostsDummyService())
    }
}