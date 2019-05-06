package che.codes.posts.features.details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import che.codes.posts.core.data.PostsDataSource
import che.codes.posts.core.data.models.Comment
import che.codes.posts.core.data.models.Post
import che.codes.posts.core.data.models.User
import che.codes.posts.util.TrampolineSchedulerRule
import che.codes.posts.features.details.PostDetailsViewModel.*
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import java.lang.IllegalStateException


class PostDetailsViewModelTest {
    private lateinit var sut: PostDetailsViewModel
    private val dataSourceMock: PostsDataSource = mock()
    private val observerMock: Observer<FetchResult> = mock()
    private val testPost = Post(1, User(1, "username1", "email1"), "title1", "body1")
    private val testComments = listOf(Comment(1, "email2", "body2"), Comment(2, "email3", "body3"))

    @get:Rule
    val executorRule = InstantTaskExecutorRule()

    @get:Rule
    val schedulerRule = TrampolineSchedulerRule()

    @get:Rule
    var thrown: ExpectedException = ExpectedException.none()

    @Before
    fun setUp() {
        sut = PostDetailsViewModel(dataSourceMock)
        sut.post = testPost
    }

    @Test(expected = IllegalStateException::class)
    fun fetchDetails_noPost_throwsIllegalStateException() {
        sut.post = null

        sut.fetchDetails()
    }

    @Test
    fun fetchDetails_dataSourceCalledWithCorrectId() {
        success()

        sut.fetchDetails()

        verify(dataSourceMock).fetchComments(1)
    }

    @Test
    fun fetchDetails_onSuccess_noComments_resultSuccessNoComments() {
        successEmpty()
        sut.result.observeForever(observerMock)

        sut.fetchDetails()

        verify(observerMock).onChanged(FetchResult.Success(emptyList()))
    }

    @Test
    fun fetchDetails_onSuccess_resultSuccessCorrectComments() {
        success()
        sut.result.observeForever(observerMock)

        sut.fetchDetails()

        verify(observerMock).onChanged(FetchResult.Success(testComments))
    }

    @Test
    fun fetchDetails_onError_resultError() {
        error()
        sut.result.observeForever(observerMock)

        sut.fetchDetails()

        verify(observerMock).onChanged(any<FetchResult.Error>())
    }

    @Test
    fun onCleared_afterFetching_disposablesCleared() {
        success()

        sut.fetchDetails()
        invokeOnCleared()

        MatcherAssert.assertThat(sut.disposables.size(), CoreMatchers.equalTo(0))
    }

    @Test
    fun onCleared_beforeFetching_disposablesCleared() {
        invokeOnCleared()

        MatcherAssert.assertThat(sut.disposables.size(), CoreMatchers.equalTo(0))
    }

    //region Helper methods

    private fun success(){
        whenever(dataSourceMock.fetchComments(any())).thenReturn(Single.just(testComments))
    }

    private fun successEmpty(){
        whenever(dataSourceMock.fetchComments(any())).thenReturn(Single.just(emptyList()))
    }

    private fun error() {
        whenever(dataSourceMock.fetchComments(any())).thenReturn(Single.error(Exception("Test Exception")))
    }

    private fun invokeOnCleared() {
        val onClearedMethod = sut.javaClass.getDeclaredMethod("onCleared")
        onClearedMethod.isAccessible = true
        onClearedMethod.invoke(sut)
    }

    //endregion
}