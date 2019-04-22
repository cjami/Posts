package che.codes.posts.data.jsonplaceholder.models

data class JpComment(
    val postId: Long,
    val id: Long,
    val name: String,
    val email: String,
    val body: String
)