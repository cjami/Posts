package che.codes.posts.data

import che.codes.posts.data.models.Comment
import che.codes.posts.data.models.Post
import io.reactivex.Single

interface PostsDataSource {
    fun fetchPosts(): Single<List<Post>>
    fun fetchComments(postId: Long): Single<List<Comment>>
}