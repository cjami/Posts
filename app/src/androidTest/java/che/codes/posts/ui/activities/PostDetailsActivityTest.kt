package che.codes.posts.ui.activities

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import che.codes.posts.PostsApplication
import che.codes.posts.R
import che.codes.posts.data.models.Post
import che.codes.posts.data.models.User
import che.codes.posts.ui.util.DaggerTestAppComponent
import che.codes.posts.ui.util.MatcherUtils.Companion.hasRecyclerItemCount
import che.codes.posts.ui.util.TestPropertyModule
import com.squareup.rx2.idler.Rx2Idler
import io.reactivex.android.schedulers.AndroidSchedulers
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PostDetailsActivityTest {

    @get:Rule
    val activityRule = ActivityTestRule(PostDetailsActivity::class.java, false, false)

    private lateinit var mockServer: MockWebServer
    private lateinit var observerIdling: IdlingResource

    private val testPost = Post(1, User(1, "username1", "email1"), "title1", "body1")

    @Before
    fun setUp() {
        mockServer = MockWebServer()
        mockServer.start()

        val app = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as PostsApplication

        val component = DaggerTestAppComponent.builder()
            .testPropertyModule(TestPropertyModule(mockServer.url("").toString()))
            .build()

        app.component = component

        observerIdling = Rx2Idler.wrap(AndroidSchedulers.mainThread(), "Android Main Thread Scheduler")
    }

    @Test
    fun beforeFetchingComments_postDetailsDisplayed() {
        activityRule.launchActivity(createPostIntent())

        onView(withText(testPost.title)).check(matches(isDisplayed()))
        onView(withText(testPost.body)).check(matches(isDisplayed()))
        onView(withText(testPost.user.username.toUpperCase())).check(matches(isDisplayed()))
    }

    @Test
    fun afterFetchingComments_onSuccess_postDetailsDisplayed() {
        success()
        registerObserverIdling()

        activityRule.launchActivity(createPostIntent())

        onView(withText(testPost.title)).check(matches(isDisplayed()))
        onView(withText(testPost.body)).check(matches(isDisplayed()))
        onView(withText(testPost.user.username.toUpperCase())).check(matches(isDisplayed()))

        unregisterObserverIdling()
    }

    @Test
    fun afterFetchingComments_onError_postDetailsDisplayed() {
        error()
        registerObserverIdling()

        activityRule.launchActivity(createPostIntent())

        onView(withText(testPost.title)).check(matches(isDisplayed()))
        onView(withText(testPost.body)).check(matches(isDisplayed()))
        onView(withText(testPost.user.username.toUpperCase())).check(matches(isDisplayed()))

        unregisterObserverIdling()
    }

    @Test
    fun beforeFetchingComments_fetchingTextDisplayed() {
        activityRule.launchActivity(createPostIntent())

        onView(withText(R.string.fetching)).check(matches(isDisplayed()))
    }

    @Test
    fun afterFetchingComments_onSuccessEmpty_noCommentsTextDisplayed() {
        successEmpty()
        registerObserverIdling()

        activityRule.launchActivity(createPostIntent())

        onView(withText(R.string.no_comments)).check(matches(isDisplayed()))

        unregisterObserverIdling()
    }

    @Test
    fun afterFetchingComments_onError_errorTextDisplayed() {
        error()
        registerObserverIdling()

        activityRule.launchActivity(createPostIntent())

        onView(withText(R.string.error)).check(matches(isDisplayed()))

        unregisterObserverIdling()
    }

    @Test
    fun afterFetchingComments_onSuccess_commentsListed() {
        success()
        registerObserverIdling()

        activityRule.launchActivity(createPostIntent())

        onView(withId(R.id.comment_list)).check(matches(hasRecyclerItemCount(5 + 2)))

        unregisterObserverIdling()
    }

    @Test
    fun afterFetchingComments_onError_noCommentsListed() {
        error()
        registerObserverIdling()

        activityRule.launchActivity(createPostIntent())

        onView(withId(R.id.comment_list)).check(matches(hasRecyclerItemCount(0 + 2)))

        unregisterObserverIdling()
    }

    @After
    fun tearDown() {
        mockServer.shutdown()
    }

    //region Helper methods

    private fun createPostIntent(): Intent {
        return Intent().apply {
            putExtra(POST_KEY, testPost)
        }
    }

    private fun success() {
        mockServer.enqueue(createJsonResponse(200, getJsonFromFile("jp_comments.json")))
    }

    private fun successEmpty() {
        mockServer.enqueue(createJsonResponse(200))
    }

    private fun error() {
        mockServer.enqueue(createJsonResponse(500))
    }

    private fun createJsonResponse(code: Int, body: String = "[]"): MockResponse {
        return MockResponse().addHeader("Content-Type", "application/json; charset=utf-8")
            .addHeader("Cache-Control", "no-cache")
            .setBody(body)
            .setResponseCode(code)
    }

    private fun registerObserverIdling() {
        IdlingRegistry.getInstance().register(observerIdling)
    }

    private fun unregisterObserverIdling() {
        IdlingRegistry.getInstance().unregister(observerIdling)
    }

    private fun getJsonFromFile(filename: String): String {
        val inputStream = InstrumentationRegistry.getInstrumentation().context.assets.open(filename)
        return inputStream.bufferedReader().use { it.readText() }
    }

    //endregion
}