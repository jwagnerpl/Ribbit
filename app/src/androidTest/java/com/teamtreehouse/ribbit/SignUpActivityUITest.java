package com.teamtreehouse.ribbit;

import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.teamtreehouse.ribbit.ui.MainActivity;
import com.teamtreehouse.ribbit.ui.SignUpActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class SignUpActivityUITest {

    @Before
    public void initialize() {
        String userName = "testUser";
        String password = "password";
        String email = "email@email.com";
    }

    @Rule
    public ActivityTestRule<SignUpActivity> activityTestRule = new ActivityTestRule<SignUpActivity>(SignUpActivity.class);


    @Test
    public void completeFormSendstToMainActivity() throws Exception {

        Intents.init();


        onView(withId(R.id.usernameField)).perform(typeText("testUser"));
        onView(withId(R.id.passwordField)).perform(typeText("testPassword"));
        onView(withId(R.id.emailField)).perform(typeText("testEmail@email.com"));

        onView(withId(R.id.signupButton)).perform(click());

        intended(hasComponent(MainActivity.class.getName()));
        Intents.release();
    }

    @Test
    public void missingPasswordPromptsError() throws Exception {

        onView(withId(R.id.usernameField)).perform(typeText("testUser"));
        onView(withId(R.id.emailField)).perform(typeText("testEmail@email.com"));
        onView(withId(R.id.signupButton)).perform(click());

        onView(withText(R.string.signup_error_message)).check(matches(isDisplayed()));
    }

}
