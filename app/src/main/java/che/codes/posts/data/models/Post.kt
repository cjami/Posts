package che.codes.posts.data.models

import java.io.Serializable

data class Post(
    val id: Long,
    val user: User,
    val title: String,
    val body: String
) : Serializable