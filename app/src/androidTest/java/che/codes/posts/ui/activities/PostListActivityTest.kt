package che.codes.posts.ui.activities

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import che.codes.posts.PostsApplication
import che.codes.posts.R
import che.codes.posts.ui.util.DaggerTestAppComponent
import che.codes.posts.ui.util.MatcherUtils.Companion.hasRecyclerItemCount
import che.codes.posts.ui.util.TestPropertyModule
import com.squareup.rx2.idler.Rx2Idler
import io.reactivex.android.schedulers.AndroidSchedulers
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PostListActivityTest {

    @get:Rule
    val activityRule = ActivityTestRule(PostListActivity::class.java, false, false)

    private lateinit var mockServer: MockWebServer
    private lateinit var observerIdling: IdlingResource

    @Before
    fun setUp() {
        mockServer = MockWebServer()
        mockServer.start()

        val app = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as PostsApplication

        val component = DaggerTestAppComponent.builder()
            .testPropertyModule(TestPropertyModule(mockServer.url("").toString()))
            .build()

        app.component = component

        // We assume that any returning network call is called back on the main thread scheduler
        observerIdling = Rx2Idler.wrap(AndroidSchedulers.mainThread(), "Android Main Thread Scheduler")
    }

    @After
    fun tearDown() {
        mockServer.shutdown()
    }

    @Test
    fun beforeFetching_fetchingTextDisplayed() {
        activityRule.launchActivity(null)

        onView(withId(R.id.status_text)).check(matches(isDisplayed()))
        onView(withId(R.id.status_text)).check(matches(withText(R.string.fetching)))
    }

    @Test
    fun afterFetching_onSuccess_postsListed() {
        success()
        registerObserverIdling()

        activityRule.launchActivity(null)

        onView(withId(R.id.post_list)).check(matches(hasRecyclerItemCount(100 + 1))) // Including Header

        unregisterObserverIdling()
    }

    @Test
    fun afterFetching_onSuccess_errorTextNotDisplayed() {
        success()
        registerObserverIdling()

        activityRule.launchActivity(null)

        onView(withId(R.id.status_text)).check(matches(not(isDisplayed())))

        unregisterObserverIdling()
    }

    @Test
    fun afterFetching_onError_noPostsListed() {
        serverError()
        registerObserverIdling()

        activityRule.launchActivity(null)

        onView(withId(R.id.post_list)).check(matches(hasRecyclerItemCount(0 + 1))) // Only Header

        unregisterObserverIdling()
    }

    @Test
    fun afterFetching_onError_errorTextDisplayed() {
        serverError()
        registerObserverIdling()

        activityRule.launchActivity(null)

        onView(withId(R.id.status_text)).check(matches(isDisplayed()))
        onView(withId(R.id.status_text)).check(matches(withText(R.string.error)))

        unregisterObserverIdling()
    }

    //region Helper Classes and Methods

    private fun success() {
        mockServer.enqueue(createJsonResponse(200, getJsonFromFile("jp_posts.json")))
        mockServer.enqueue(createJsonResponse(200, getJsonFromFile("jp_users.json")))
    }

    private fun serverError() {
        mockServer.enqueue(createJsonResponse(500))
    }

    private fun createJsonResponse(code: Int, body: String = "{}"): MockResponse {
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