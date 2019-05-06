package che.codes.posts.core

import android.app.Application
import che.codes.posts.core.di.components.AppComponent
import che.codes.posts.core.di.components.DaggerAppComponent

class PostsApplication : Application() {
    companion object {
        lateinit var instance: PostsApplication
            private set
    }

    lateinit var component: AppComponent

    override fun onCreate() {
        super.onCreate()
        instance = this
        component = DaggerAppComponent.builder().build()
    }
}