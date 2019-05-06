package che.codes.posts.core.data.jsonplaceholder

import che.codes.posts.core.PostsApplication
import che.codes.posts.core.data.jsonplaceholder.models.JpComment
import che.codes.posts.core.data.jsonplaceholder.models.JpPost
import che.codes.posts.core.data.jsonplaceholder.models.JpUser
import com.google.gson.Gson
import io.reactivex.Single

class JpPostsDummyService : JpPostsService {

    override fun getPosts(): Single<List<JpPost>> {
        return Single.just(getListFromFile("jp_posts.json", Array<JpPost>::class.java))
    }

    override fun getComments(postId: Long): Single<List<JpComment>> {
        return Single.just(getListFromFile("jp_comments.json", Array<JpComment>::class.java))
    }

    override fun getUsers(ids: List<Long>): Single<List<JpUser>> {
        return Single.just(getListFromFile("jp_users.json", Array<JpUser>::class.java))
    }

    private fun <T : Any> getListFromFile(filename: String, type: Class<Array<T>>): List<T> {
        return Gson().fromJson(getJsonFromFile(filename), type).toList()
    }

    private fun getJsonFromFile(filename: String): String {
        val inputStream = PostsApplication.instance.assets.open(filename)
        return inputStream.bufferedReader().use { it.readText() }
    }
}