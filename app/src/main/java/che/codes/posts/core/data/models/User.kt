package che.codes.posts.core.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val id: Long,
    val username: String,
    val email: String
) : Parcelable