package com.alphakiwi.projet_7;

import android.content.Context;


import androidx.test.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;


@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest  {


    private HungryActivity mActivity;

    @Rule
    public ActivityTestRule<HungryActivity> mActivityRule =
            new ActivityTestRule(HungryActivity.class);


    @Before
    public void setUp()  {
        mActivity = mActivityRule.getActivity();
        assertThat(mActivity, notNullValue());

    }


    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.alphakiwi.projet_7", appContext.getPackageName());

    }

    @Test
    public void drawer_layout_shouldNotBeEmpty() {
        onView(withId(R.id.drawer_layout))
                .check(matches(hasMinimumChildCount(1)));
    }

    @Test
    public void  toolbar_shouldNotBeEmpty() {


        onView(withId(R.id.toolbar))
                .check(matches(hasMinimumChildCount(1)));


    }

    @Test
    public void  navigation_shouldNotBeEmpty() {


        onView(withId(R.id.navigation))
                .check(matches(hasMinimumChildCount(1)));


    }


    @Test
    public void  frame_shouldNotBeEmpty() {


        onView(withId(R.id.content_frame))
                .check(matches(hasMinimumChildCount(1)));


    }

}
