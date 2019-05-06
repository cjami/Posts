package che.codes.posts.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Post(
    val id: Long,
    val user: User,
    val title: String,
    val body: String
) : Parcelable