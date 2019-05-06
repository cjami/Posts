package che.codes.posts.core.data

import che.codes.posts.core.data.models.Comment
import che.codes.posts.core.data.models.Post
import io.reactivex.Single

interface PostsDataSource {
    fun fetchPosts(): Single<List<Post>>
    fun fetchComments(postId: Long): Single<List<Comment>>
}