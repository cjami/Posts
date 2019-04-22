package che.codes.posts.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import che.codes.posts.util.TrampolineSchedulerRule
import che.codes.posts.data.models.Post
import che.codes.posts.data.PostsDataSource
import che.codes.posts.data.models.User
import che.codes.posts.viewmodels.PostListViewModel.FetchResult
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PostListViewModelTest {
    private lateinit var sut: PostListViewModel
    private val dataSourceMock: PostsDataSource = mock()
    private val observerMock: Observer<FetchResult> = mock()
    private val testPost1 = Post(1, User(1, "username1", "email1"), "title1", "body1")
    private val testPost2 = Post(2, User(2, "username2", "email2"), "title2", "body2")

    @get:Rule
    val executorRule = InstantTaskExecutorRule()

    @get:Rule
    val schedulerRule = TrampolineSchedulerRule()

    @Before
    fun setUp() {
        sut = PostListViewModel(dataSourceMock)
    }

    @Test
    fun fetchPosts_dataSourceCalled() {
        success()

        sut.fetchPosts()

        verify(dataSourceMock).fetchPosts()
    }

    @Test
    fun fetchPosts_onSuccess_noPosts_resultSuccessNoPosts() {
        successEmpty()
        sut.result.observeForever(observerMock)

        sut.fetchPosts()

        verify(observerMock).onChanged(FetchResult.Success(listOf()))
    }

    @Test
    fun fetchPosts_onSuccess_resultSuccessCorrectPosts() {
        success()
        sut.result.observeForever(observerMock)

        sut.fetchPosts()

        verify(observerMock).onChanged(FetchResult.Success(listOf(testPost1, testPost2)))
    }

    @Test
    fun fetchPosts_onError_resultError() {
        error()
        sut.result.observeForever(observerMock)

        sut.fetchPosts()

        verify(observerMock).onChanged(any<FetchResult.Error>())
    }

    @Test
    fun onCleared_afterFetching_disposablesCleared() {
        success()

        sut.fetchPosts()
        invokeOnCleared()

        assertThat(sut.disposables.size(), equalTo(0))
    }

    @Test
    fun onCleared_beforeFetching_disposablesCleared() {
        invokeOnCleared()

        assertThat(sut.disposables.size(), equalTo(0))
    }

    //region Helper methods

    private fun success() {
        whenever(dataSourceMock.fetchPosts()).thenReturn(Single.just(listOf(testPost1, testPost2)))
    }

    private fun successEmpty() {
        whenever(dataSourceMock.fetchPosts()).thenReturn(Single.just(listOf()))
    }

    private fun error() {
        whenever(dataSourceMock.fetchPosts()).thenReturn(Single.error(Exception("Test Exception")))
    }

    private fun invokeOnCleared() {
        val onClearedMethod = sut.javaClass.getDeclaredMethod("onCleared")
        onClearedMethod.isAccessible = true
        onClearedMethod.invoke(sut)
    }

    //endregion
}