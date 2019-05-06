package che.codes.posts.core.di.modules

import che.codes.posts.features.details.CommentsAdapter
import che.codes.posts.features.list.PostsAdapter
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