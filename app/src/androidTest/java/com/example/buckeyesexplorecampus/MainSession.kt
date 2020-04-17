package com.example.buckeyesexplorecampus


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainSession {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(SplashActivity::class.java)

    @Test
    fun mainSession() {
        val textInputEditText = onView(
            allOf(
                withId(R.id.password),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.password_layout),
                        0
                    ),
                    0
                )
            )
        )
        textInputEditText.perform(scrollTo(), replaceText("test1234"), closeSoftKeyboard())

        val appCompatButton = onView(
            allOf(
                withId(R.id.button_done), withText("Sign in"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    4
                )
            )
        )
        appCompatButton.perform(scrollTo(), click())

        val imageView = onView(
            allOf(
                withId(R.id.locationPreview), withContentDescription("location image"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.recyclerView),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        imageView.perform(click())

        val button = onView(
            allOf(
                withId(R.id.factsBackButton), withText("Back"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.LinearLayout")),
                        3
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        button.perform(click())

        val imageView2 = onView(
            allOf(
                withId(R.id.locationPreview), withContentDescription("location image"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.recyclerView),
                        2
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        imageView2.perform(click())

        val button2 = onView(
            allOf(
                withId(R.id.factsBackButton), withText("Back"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.LinearLayout")),
                        3
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        button2.perform(click())

        val imageView3 = onView(
            allOf(
                withId(R.id.locationPreview), withContentDescription("location image"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.recyclerView),
                        4
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        imageView3.perform(click())

        val button3 = onView(
            allOf(
                withId(R.id.factsBackButton), withText("Back"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.LinearLayout")),
                        3
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        button3.perform(click())

        val imageView4 = onView(
            allOf(
                withId(R.id.locationPreview), withContentDescription("location image"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.recyclerView),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        imageView4.perform(click())

        val button4 = onView(
            allOf(
                withId(R.id.factsBackButton), withText("Back"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.LinearLayout")),
                        3
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        button4.perform(click())

        val button5 = onView(
            allOf(
                withId(R.id.logoutSubmit), withText("Log Out"),
                childAtPosition(
                    allOf(
                        withId(R.id.buttonSubmit),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            3
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        button5.perform(click())
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
