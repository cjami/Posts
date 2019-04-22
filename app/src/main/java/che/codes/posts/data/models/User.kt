package che.codes.posts.data.models

import java.io.Serializable

data class User(
    val id: Long,
    val username: String,
    val email: String
) : Serializable