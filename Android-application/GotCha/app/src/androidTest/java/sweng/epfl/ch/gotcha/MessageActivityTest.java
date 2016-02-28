package sweng.epfl.ch.gotcha;

import android.support.test.InstrumentationRegistry;
import android.test.ActivityInstrumentationTestCase2;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;



/**
 * Created by kongchingyiii on 28/10/15.
 */
public class MessageActivityTest extends ActivityInstrumentationTestCase2<MessageActivity> {

    private MessageActivity mActivity;

    public MessageActivityTest() {
        super(MessageActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mActivity = getActivity();
    }

    public void testSellerNameIsDisplayed() {
        onView(withId(R.id.toText)).check(matches(isDisplayed()));
        onView(withId(R.id.sellerName)).check(matches(isDisplayed()));
    }

    public void testMessageFieldIsDisplayed() {
        onView(withId(R.id.messageText)).check(matches(isDisplayed()));
    }

    public void testSendButtonIsDisplayed() {
        onView(withId(R.id.sendButton)).check(matches(isDisplayed()));
    }

//    public void testCanSendMessage() {
//        onView(withId(R.id.messageText)).perform(typeText("Hello from my unit test!"));
//        onView(withId(R.id.sendButton)).perform(click());
//        onView(withId(R.id.popup_element)).check(matches(isDisplayed()));
//        onView(withId(R.id.successMsg)).check(matches(withText("Your message has been successfully sent to the seller.")));
//    }
//
//    public void testCannotSendEmptyMessage() {
//        onView(withId(R.id.messageText)).perform(typeText(""));
//        onView(withId(R.id.sendButton)).perform(click());
//        onView(withId(R.id.error_text)).inRoot(withDecorView(not(mActivity.getWindow().getDecorView()))).check(matches(isDisplayed()));
//        onView(withText("Message cannot be empty. Please try again.")).inRoot(withDecorView(not(mActivity.getWindow().getDecorView()))).check(matches(isDisplayed()));
//    }


}