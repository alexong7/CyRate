package com.example.cyrate;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;




import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;


import android.content.Intent;
import android.os.Handler;
import android.view.View;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.filters.LargeTest;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.rule.ActivityTestRule;

import com.example.cyrate.activities.BusinessListActivity;
import com.example.cyrate.activities.BusinessPostFeed;
import com.example.cyrate.activities.FindUsActivity;
import com.example.cyrate.activities.IndividualBusinessActivity;
import com.example.cyrate.activities.IntroActivity;
import com.example.cyrate.activities.LoginActivity;
import com.example.cyrate.activities.ReviewListActivity;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


// Mock the RequestServerForService class
@RunWith(AndroidJUnit4ClassRunner.class)
@LargeTest   // large execution time
public class BusinessListActivityTest {

    @Rule   // needed to launch the activity
    public ActivityTestRule<BusinessListActivity> activityRule = new ActivityTestRule<>(BusinessListActivity.class);

    @Before
    public void setUp() {
        Intents.init();
    }

    @After
    public void tearDown(){
        Intents.release();
    }

    @Test
    public void selectingBusinessNavigatesToIndividualBusiness(){
        activityRule.launchActivity(new Intent());

        onView(withId(R.id.restaurantList_recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // Verify we navigate to the next activity
        intended(hasComponent(IndividualBusinessActivity.class.getName()));

        // Our first restaurant is The Cafe, verify that the name matches
        onView(withId(R.id.restaurant_name)).check(matches(withText("The Cafe")));
    }

    @Test
    public void openNavMenu(){
        // Open Nav menu and verify we have it open
        onView(withId(R.id.open_menu_icon)).perform(click());
        onView(withId(R.id.nav_menu_header)).check(matches(isDisplayed()));
    }

    @Test
    public void navMenuSignInSelect(){
        activityRule.launchActivity(new Intent());

        // Open the Nav Menu and click Sign In
        onView(withId(R.id.open_menu_icon)).perform(click());
        onView(withId(R.id.nav_menu_header)).check(matches(isDisplayed()));
        onView(withId(R.id.nav_sign_in)).perform(click());

        // Verify we navigate back to the Login Activity
        intended(hasComponent(LoginActivity.class.getName()));
    }

    // For these Individual Business tests, I'm keeping them in this file so that we start from
    // the BusinessListActivity, otherwise we don't have the data of the Businesses which will
    // give us exceptions when trying to get info like Business Name and such.
    //
    // This works because we call the server on this activity, populate the list, and then the data
    // works and passes info to IndividualBusinessActivity like normal
    @Test
    public void selectIndividualBusinessClickReviews(){
        activityRule.launchActivity(new Intent());

        onView(withId(R.id.restaurantList_recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // Verify we navigate to the next activity
        intended(hasComponent(IndividualBusinessActivity.class.getName()));

        // Our first restaurant is The Cafe, verify that the name matches
        onView(withId(R.id.restaurant_name)).check(matches(withText("The Cafe")));

        // Click Reviews and verify we navigate to the Reviews Activity
        onView(withId(R.id.reviews_btn)).perform(click());
        intended(hasComponent(ReviewListActivity.class.getName()));
    }

    @Test
    public void selectIndividualBusinessClickPosts(){
        activityRule.launchActivity(new Intent());

        onView(withId(R.id.restaurantList_recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // Verify we navigate to the next activity
        intended(hasComponent(IndividualBusinessActivity.class.getName()));

        // Our first restaurant is The Cafe, verify that the name matches
        onView(withId(R.id.restaurant_name)).check(matches(withText("The Cafe")));

        // Click Reviews and verify we navigate to the Reviews Activity
        onView(withId(R.id.busPosts_btn)).perform(click());
        intended(hasComponent(BusinessPostFeed.class.getName()));
    }

    @Test
    public void selectIndividualBusinessClickFindUs(){
        activityRule.launchActivity(new Intent());

        onView(withId(R.id.restaurantList_recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // Verify we navigate to the next activity
        intended(hasComponent(IndividualBusinessActivity.class.getName()));

        // Our first restaurant is The Cafe, verify that the name matches
        onView(withId(R.id.restaurant_name)).check(matches(withText("The Cafe")));

        // Click Reviews and verify we navigate to the Reviews Activity
        onView(withId(R.id.find_us_btn)).perform(click());
        intended(hasComponent(FindUsActivity.class.getName()));
    }



}



