package com.rmaproject.storyapp.ui.auth

import android.content.Context
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.rmaproject.storyapp.R
import com.rmaproject.storyapp.data.remote.api.ApiConfig
import com.rmaproject.storyapp.ui.main.MainActivity
import com.rmaproject.storyapp.ui.main.utils.JsonConverter
import com.rmaproject.storyapp.utils.IdlingResource
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito

@RunWith(AndroidJUnit4::class)
@MediumTest
class RegisterFragmentTest {

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    private val navController = Mockito.mock(NavController::class.java)
    private val mockWebServer = MockWebServer()

    @Before
    fun setUp() {
        mockWebServer.start(5000)
        ApiConfig.BASE_URL = "http://127.0.0.1:5000/"
        IdlingRegistry.getInstance().register(IdlingResource.countingIdlingResource)
        val scenario = launchFragmentInContainer<RegisterFragment>(themeResId = R.style.AppTheme)
        scenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), navController)
        }
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
        IdlingRegistry.getInstance().unregister(IdlingResource.countingIdlingResource)
    }

    @Test
    fun checkUi() {
        onView(withId(R.id.ed_register_name)).check(matches(isDisplayed()))
        onView(withId(R.id.ed_register_email)).check(matches(isDisplayed()))
        onView(withId(R.id.ed_register_password)).check(matches(isDisplayed()))
    }

    @Test
    fun register_Success() {
        checkUi()
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(JsonConverter.readStringFromFile("success_register.json"))
        mockWebServer.enqueue(mockResponse)

        onView(withId(R.id.ed_register_name)).perform(
            click(),
            typeText("mamangtest 12345"),
            closeSoftKeyboard()
        )
        onView(withId(R.id.ed_register_email)).perform(
            click(),
            typeText("mamangtest12345@gmail.com"),
            closeSoftKeyboard()
        )

        onView(withHint(ApplicationProvider.getApplicationContext<Context>().getString(R.string.hint_new_password))).perform(
            click(),
            typeText("12345678"),
            closeSoftKeyboard()
        )

        onView(withId(R.id.btn_register)).perform(click())

        onView(withText(ApplicationProvider.getApplicationContext<Context>().getString(R.string.msg_success_register)))
    }
}