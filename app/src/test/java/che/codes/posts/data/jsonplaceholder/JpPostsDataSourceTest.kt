package che.codes.posts.data.jsonplaceholder

import che.codes.posts.data.jsonplaceholder.models.JpComment
import che.codes.posts.data.jsonplaceholder.models.JpPost
import che.codes.posts.data.jsonplaceholder.models.JpUser
import com.google.gson.Gson
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import okhttp3.ResponseBody
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

class JpPostsDataSourceTest {
    private lateinit var sut: JpPostsDataSource
    private val jpPostsServiceMock: JpPostsService = mock()

    @Before
    fun setUp() {
        sut = JpPostsDataSource(jpPostsServiceMock)
    }

    @Test
    fun fetchPosts_serviceCalled() {
        postsSuccess()

        sut.fetchPosts()

        verify(jpPostsServiceMock).getPosts()
    }

    @Test
    fun fetchPosts_onSuccessEmpty_noPostsReturned() {
        postsSuccessEmpty()

        val single = sut.fetchPosts()
        val testObserver = single.test()
        testObserver.awaitTerminalEvent()
        val result = testObserver.values()[0]

        assert(result.isEmpty())
    }

    @Test
    fun fetchPosts_onSuccess_postsReturned() {
        postsSuccess()

        val single = sut.fetchPosts()
        val testObserver = single.test()
        testObserver.awaitTerminalEvent()
        val result = testObserver.values()[0]

        assertEquals(100, result.size)
    }

    @Test
    fun fetchPosts_onError_errorReturned() {
        postsHttpError(500) // Would be better to cycle through all HTTP error codes

        val single = sut.fetchPosts()
        val testObserver = single.test()
        testObserver.awaitTerminalEvent()

        assertThat(testObserver.errors()[0], instanceOf(HttpException::class.java))
    }

    @Test
    fun fetchComments_serviceCalledWithCorrectId() {
        commentsSuccess()

        sut.fetchComments(3)

        verify(jpPostsServiceMock).getComments(3)
    }

    @Test
    fun fetchComments_onSuccessEmpty_noCommentsReturned() {
        commentsSuccessEmpty()

        val single = sut.fetchComments(1)
        val testObserver = single.test()
        testObserver.awaitTerminalEvent()
        val result = testObserver.values()[0]

        assert(result.isEmpty())
    }

    @Test
    fun fetchComments_onSuccess_commentsReturned() {
        commentsSuccess()

        val single = sut.fetchComments(1)
        val testObserver = single.test()
        testObserver.awaitTerminalEvent()
        val result = testObserver.values()[0]

        assertEquals(5, result.size)
    }

    @Test
    fun fetchComments_onError_errorReturned() {
        commentsHttpError(500)

        val single = sut.fetchComments(1)
        val testObserver = single.test()
        testObserver.awaitTerminalEvent()

        assertThat(testObserver.errors()[0], instanceOf(HttpException::class.java))
    }

    //region Helper Methods

    private fun postsSuccess() {
        val jpPosts = getListFromFile("jp_posts.json", Array<JpPost>::class.java)
        val jpUsers = getListFromFile("jp_users.json", Array<JpUser>::class.java)
        whenever(jpPostsServiceMock.getPosts()).thenReturn(Single.just(jpPosts))
        whenever(jpPostsServiceMock.getUsers(any())).thenReturn(Single.just(jpUsers))
    }

    private fun postsSuccessEmpty(){
        whenever(jpPostsServiceMock.getPosts()).thenReturn(Single.just(emptyList()))
        whenever(jpPostsServiceMock.getUsers(any())).thenReturn(Single.just(emptyList()))
    }

    private fun postsHttpError(errorCode: Int) {
        val httpException = HttpException(Response.error<Any>(errorCode, ResponseBody.create(null, "")))
        whenever(jpPostsServiceMock.getPosts()).thenReturn(Single.error(httpException))
    }

    private fun commentsSuccess() {
        val jpComments = getListFromFile("jp_comments.json", Array<JpComment>::class.java)
        whenever(jpPostsServiceMock.getComments(any())).thenReturn(Single.just(jpComments))
    }

    private fun commentsSuccessEmpty() {
        whenever(jpPostsServiceMock.getComments(any())).thenReturn(Single.just(emptyList()))
    }

    private fun commentsHttpError(errorCode: Int) {
        val httpException = HttpException(Response.error<Any>(errorCode, ResponseBody.create(null, "")))
        whenever(jpPostsServiceMock.getComments(any())).thenReturn(Single.error(httpException))
    }

    private fun <T : Any> getListFromFile(filename: String, type: Class<Array<T>>): List<T> {
        return Gson().fromJson(getTextFromFile(filename), type).toList()
    }

    private fun getTextFromFile(filename: String): String {
        return this::class.java.getResource("/$filename")!!.readText()
    }

    //endregion
}