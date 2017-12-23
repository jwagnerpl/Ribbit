package com.teamtreehouse.ribbit;

import android.content.Intent;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.teamtreehouse.ribbit.ui.LoginActivity;
import com.teamtreehouse.ribbit.ui.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class LoginActivityUITest {



    @Rule
    public ActivityTestRule<LoginActivity> activityTestRule= new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void correctCredentialsPassesToMainActivity() throws Exception {

        // Arrange
        Intents.init();
        String userName = "ben";
        String password = "test";
        onView(withId(R.id.usernameField)).perform(typeText(userName));
        onView(withId(R.id.passwordField)).perform(typeText(password));

        // Act
        onView(withId(R.id.loginButton)).perform(click());

        // Assert

        intended(hasComponent(MainActivity.class.getName()));
        Intents.release();
    }

    @Test
    public void incorrectCredentialsDisplaysDialog() throws Exception {

        // Arrange

        String userName = "ben";
        String password = "wrongtest";
        onView(withId(R.id.usernameField)).perform(typeText(userName));
        onView(withId(R.id.passwordField)).perform(typeText(password));

        // Act
        onView(withId(R.id.loginButton)).perform(click());

        // Assert

        onView(withText("Try entering the correct login details to proceed.")).check(matches(isDisplayed()));
    }

    @Test
    public void missingUsernameDisplaysDialog() throws Exception {

        // Arrange

        String password = "test";
        onView(withId(R.id.passwordField)).perform(typeText(password));

        // Act
        onView(withId(R.id.loginButton)).perform(click());

        // Assert

        onView(withText(R.string.login_error_message)).check(matches(isDisplayed()));
    }

    @Test
    public void missingPasswordDisplaysDialog() throws Exception {

        // Arrange

        String userName = "ben";
        onView(withId(R.id.usernameField)).perform(typeText(userName));

        // Act
        onView(withId(R.id.loginButton)).perform(click());

        // Assert

        onView(withText("Try entering the correct login details to proceed.")).check(matches(isDisplayed()));
    }

}
