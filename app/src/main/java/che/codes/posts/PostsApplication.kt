package che.codes.posts

import android.app.Application
import che.codes.posts.di.components.AppComponent
import che.codes.posts.di.components.DaggerAppComponent

class PostsApplication : Application() {
    lateinit var component: AppComponent

    override fun onCreate() {
        super.onCreate()
        component = DaggerAppComponent.builder().build()
    }
}