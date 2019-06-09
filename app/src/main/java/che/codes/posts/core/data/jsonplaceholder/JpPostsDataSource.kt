package che.codes.posts.core.data.jsonplaceholder

import che.codes.posts.core.data.PostsDataSource
import che.codes.posts.core.data.jsonplaceholder.models.JpComment
import che.codes.posts.core.data.jsonplaceholder.models.JpPost
import che.codes.posts.core.data.jsonplaceholder.models.JpUser
import che.codes.posts.core.data.models.Comment
import che.codes.posts.core.data.models.Post
import che.codes.posts.core.data.models.User
import io.reactivex.Single

class JpPostsDataSource(private val service: JpPostsService) : PostsDataSource {

    override fun fetchPosts(): Single<List<Post>> {
        return service.getPosts()
            .flatMap { jpPosts ->
                service.getUsers(jpPosts.map { it.userId }.distinct())
                    .map { jpUsers ->
                        createPosts(jpPosts, jpUsers)
                    }
            }
    }

    override fun fetchComments(postId: Long): Single<List<Comment>> {
        return service.getComments(postId).map { jpComments -> jpComments.map { createComment(it) } }
    }

    private fun createPosts(jpPosts: List<JpPost>, jpUsers: List<JpUser>): List<Post> {
        return jpPosts.map { jpPost ->
            Post(jpPost.id, createUser(jpPost.userId, jpUsers), jpPost.title, jpPost.body)
        }
    }

    private fun createUser(userId: Long, jpUsers: List<JpUser>): User {
        val jpUser: JpUser? = jpUsers.find { it.id == userId }

        jpUser ?: throw IllegalStateException("User from post not found")

        return User(jpUser.id, jpUser.username, jpUser.email)
    }

    private fun createComment(jpComment: JpComment): Comment {
        return Comment(jpComment.id, jpComment.email, jpComment.body)
    }
}