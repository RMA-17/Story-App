package com.rmaproject.storyapp.ui.auth

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.rmaproject.storyapp.R
import com.rmaproject.storyapp.ui.main.MainActivity
import com.rmaproject.storyapp.utils.IdlingResource
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*

@RunWith(AndroidJUnit4::class)
@MediumTest
class LoginFragmentTest {

    @get:Rule
    val activity = ActivityScenarioRule(MainActivity::class.java)

    private val navController = mock(NavController::class.java)

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(IdlingResource.countingIdlingResource)
        val scenario = launchFragmentInContainer<LoginFragment>(themeResId = R.style.AppTheme)
        scenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), navController)
        }
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(IdlingResource.countingIdlingResource)
    }

    @Test
    fun checkUi() {
        onView(withId(R.id.ed_login_email)).check(matches(isDisplayed()))
        onView(withId(R.id.ed_login_password)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_login)).check(matches(isDisplayed()))
    }

    @Test
    fun login_Success() {
        checkUi()
        onView(withId(R.id.ed_login_email)).perform(
            click(),
            typeText("mamangtest@gmail.com"),
            closeSoftKeyboard()
        )
        onView(withId(R.id.ed_login_password)).perform(
            click(),
            typeText("12345678"),
            closeSoftKeyboard()
        )
        onView(withId(R.id.btn_login)).perform(
            click()
        )
    }
}