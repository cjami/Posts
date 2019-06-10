package che.codes.posts.features.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import che.codes.posts.R
import che.codes.posts.core.PostsApplication
import che.codes.posts.features.details.POST_KEY
import che.codes.posts.features.details.PostDetailsActivity
import che.codes.posts.util.DaggerTestAppComponent
import che.codes.posts.util.IdlingSchedulerRule
import che.codes.posts.util.IdlingSchedulerRule.Companion.clearIdlingScheduler
import che.codes.posts.util.MatcherUtils.Companion.hasRecyclerItemCount
import che.codes.posts.util.TestPropertyModule
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PostListActivityTest {

    @get:Rule
    val activityRule = IntentsTestRule(PostListActivity::class.java, false, false)

    @get:Rule
    val schedulerRule = IdlingSchedulerRule()

    private lateinit var mockServer: MockWebServer

    @Before
    fun setUp() {
        mockServer = MockWebServer()
        mockServer.start()

        val app = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as PostsApplication

        val component = DaggerTestAppComponent.builder()
            .testPropertyModule(TestPropertyModule(mockServer.url("").toString()))
            .build()

        app.component = component
    }

    @After
    fun tearDown() {
        mockServer.shutdown()
    }

    @Test
    fun beforeFetching_fetchingTextDisplayed() {
        clearIdlingScheduler()
        activityRule.launchActivity(null)

        onView(withId(R.id.status_text)).check(matches(isDisplayed()))
        onView(withId(R.id.status_text)).check(matches(withText(R.string.fetching)))
    }

    @Test
    fun afterFetching_onSuccess_postsListed() {
        success()

        activityRule.launchActivity(null)

        onView(withId(R.id.post_list)).check(matches(hasRecyclerItemCount(100 + 1))) // Including Header
    }

    @Test
    fun afterFetching_onSuccess_statusTextNotDisplayed() {
        success()

        activityRule.launchActivity(null)

        onView(withId(R.id.status_text)).check(matches(not(isDisplayed())))
    }

    @Test
    fun afterFetching_onError_noPostsListed() {
        serverError()

        activityRule.launchActivity(null)

        onView(withId(R.id.post_list)).check(matches(hasRecyclerItemCount(0 + 1))) // Only Header
    }

    @Test
    fun afterFetching_onError_errorTextDisplayed() {
        serverError()

        activityRule.launchActivity(null)

        onView(withId(R.id.status_text)).check(matches(isDisplayed()))
        onView(withId(R.id.status_text)).check(matches(withText(R.string.error)))
    }

    @Test
    fun itemClicked_postDetailsLaunched() {
        success()

        activityRule.launchActivity(null)
        onView(withId(R.id.post_list)).perform(actionOnItem(1, click()))

        intended(allOf(hasExtraWithKey(POST_KEY), hasComponent(PostDetailsActivity::class.java.name)))
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

    private fun getJsonFromFile(filename: String): String {
        val inputStream = InstrumentationRegistry.getInstrumentation().context.assets.open(filename)
        return inputStream.bufferedReader().use { it.readText() }
    }

    private fun actionOnItem(position: Int, action: ViewAction): ViewAction {
        return object : ViewAction {
            val viewAction = actionOnItemAtPosition<RecyclerView.ViewHolder>(position, action)

            override fun getDescription(): String {
                return viewAction.description
            }

            override fun getConstraints(): Matcher<View> {
                return viewAction.constraints
            }

            override fun perform(uiController: UiController?, view: View?) {
                clearIdlingScheduler()
                viewAction.perform(uiController, view)
            }
        }
    }

    //endregion
}