package che.codes.posts.di.modules

import che.codes.posts.ui.adapters.CommentsAdapter
import che.codes.posts.ui.adapters.PostsAdapter
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class AdapterModule {

    @Provides
    fun providePostsAdapter(@Named("base.avatar.url") baseAvatarUrl: String): PostsAdapter {
        return PostsAdapter(baseAvatarUrl)
    }

    @Provides
    fun provideCommentsAdapter(@Named("base.avatar.url") baseAvatarUrl: String): CommentsAdapter {
        return CommentsAdapter(baseAvatarUrl)
    }
}