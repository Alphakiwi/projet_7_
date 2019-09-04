package com.alphakiwi.projet_7;

import android.content.Context;

import androidx.fragment.app.ListFragment;
import androidx.test.InstrumentationRegistry;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.alphakiwi.projet_7.api.UserHelper;
import com.alphakiwi.projet_7.chat.ChatActivity;
import com.alphakiwi.projet_7.fragment.FirstFragment;
import com.alphakiwi.projet_7.fragment.ThirdFragment;
import com.alphakiwi.projet_7.model.User;
import com.android21buttons.fragmenttestrule.FragmentTestRule;
import com.bumptech.glide.RequestManager;
import com.google.firebase.auth.FirebaseUser;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static com.alphakiwi.projet_7.api.UserHelper.getUserCurrent;
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
