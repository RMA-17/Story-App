package com.rmaproject.storyapp.ui.addstory

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
import org.mockito.Mockito

@RunWith(AndroidJUnit4::class)
@MediumTest
class AddStoryFragmentTest {

    @get:Rule
    val activity = ActivityScenarioRule(MainActivity::class.java)

    private val navController = Mockito.mock(NavController::class.java)

    @Before
    fun setUp() {
        val scenario = launchFragmentInContainer<AddStoryFragment>(themeResId = R.style.AppTheme)
        scenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), navController)
        }
        IdlingRegistry.getInstance().register(IdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(IdlingResource.countingIdlingResource)
    }

    @Test
    fun checkUi() {
        onView(withId(R.id.switch_location)).check(matches(isDisplayed()))
        onView(withId(R.id.container_new_story)).check(matches(isDisplayed()))
        onView(withId(R.id.ed_add_description)).check(matches(isDisplayed()))
        onView(withId(R.id.button_add)).perform(scrollTo()).check(matches(isDisplayed()))
    }
}