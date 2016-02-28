package sweng.epfl.ch.gotcha;

import android.support.test.espresso.NoMatchingViewException;
import android.test.ActivityInstrumentationTestCase2;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import junit.framework.Assert;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressKey;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerActions.closeDrawer;
import static android.support.test.espresso.contrib.DrawerActions.openDrawer;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.contrib.DrawerMatchers.isOpen;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.StringStartsWith.startsWith;
import static org.hamcrest.object.HasToString.hasToString;

public class InterfaceTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private MainActivity mFirstTestActivity;

    public InterfaceTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mFirstTestActivity = getActivity();
    }

    public void testNavigationDrawer() {
        openDrawer(R.id.drawer_layout);
        onView(withId(R.id.drawer_layout)).check(matches(isOpen()));
        onData(hasToString(startsWith("Categories"))).inAdapterView(withId(R.id.navigation_drawer)).perform(click());
        onView(withId(R.id.drawer_layout)).check(matches(isClosed()));
    }

    //Test if search bar is displayed
    //TODO complete the test once the search is implemented
    public void testSearchBar() {
        closeDrawer(R.id.drawer_layout);
        onView(withId(R.id.search)).perform(click());
        onView(isAssignableFrom(EditText.class)).perform(typeText("test"), pressKey(66));
    }

    //Test if all categories are displayed
    public void testCategories() {
        closeDrawer(R.id.drawer_layout);
        onView(withText("Books")).check(matches(isDisplayed()));
        onView(withText("Clothes")).check(matches(isDisplayed()));
        onView(withText("Household")).check(matches(isDisplayed()));
        onView(withText("Electronics")).check(matches(isDisplayed()));
        onView(withText("Sport equipment")).check(matches(isDisplayed()));
        onView(withText("Others")).check(matches(isDisplayed()));
    }


    //Test if list of items of a specified category are displayed
    //TODO check if all items are books
    public void testListOfItems() {
        closeDrawer(R.id.drawer_layout);
        onData(anything()).inAdapterView(withId(R.id.gridView)).atPosition(0).perform(click());
        onData(anything()).inAdapterView(withId(R.id.mainListView)).atPosition(0).check(matches(isDisplayed()));
    }

    //Test if all necessary details about the items are displayed
    //TODO check that detail match with actual data from DB
    public void testItemDetails() {
        closeDrawer(R.id.drawer_layout);
        View view = LayoutInflater.from(mFirstTestActivity).inflate(R.layout.activity_item, null);

        TextView itemName = (TextView) view.findViewById(R.id.t_ItemName);
        String name = (String) itemName.getText();

        TextView itemDesc = (TextView) view.findViewById(R.id.t_itemDescription);
        String desc = (String) itemDesc.getText();

        TextView itemPrice = (TextView) view.findViewById(R.id.t_itemPrice);
        String price = (String) itemPrice.getText();

        TextView seller = (TextView) view.findViewById(R.id.sellerEmail);
        String contact = (String) seller.getText();
        assertTrue(name != null && desc != null && price != null && contact != null);
    }

    //Test if "send message to seller" works
    public void testSendMessage(){
        closeDrawer(R.id.drawer_layout);
        onData(anything()).inAdapterView(withId(R.id.gridView)).atPosition(0).perform(click());
        onData(anything()).inAdapterView(withId(R.id.mainListView)).atPosition(0).perform(click());
        onView(withId(R.id.sendMessageButton)).perform(click());
        onView(withId(R.id.messageActivity)).check(matches(isDisplayed()));
    }

    //Test back button
    public void testBackButton() {
        closeDrawer(R.id.drawer_layout);
        onData(anything()).inAdapterView(withId(R.id.gridView)).atPosition(0).perform(click());
        onData(anything()).inAdapterView(withId(R.id.mainListView)).atPosition(0).perform(click());

        pressBack();
        try {
            onView(withId(R.id.container)).check(matches(not(isDisplayed())));
        } catch (NoMatchingViewException e) {
            assertTrue(true);
        }

        pressBack();
        try {
            onView(withId(R.id.container)).check(matches(isDisplayed()));
        } catch (NoMatchingViewException e) {
            Assert.fail();
        }
    }


    public void testLoginInput() {
        openDrawer(R.id.drawer_layout);
        onView(withId(R.id.drawer_layout)).check(matches(isOpen()));
        onData(hasToString(startsWith("Login"))).inAdapterView(withId(R.id.navigation_drawer)).perform(click());

        // invalid email
        onView(withId(R.id.email)).perform(click());
        onView(withId(R.id.email)).perform(typeText("test"), pressKey(66));
        onView(withId(R.id.password)).perform(click());
        onView(withId(R.id.password)).perform(typeText("password"), pressKey(66));
        onView(withId(R.id.email_sign_in_button)).perform(click());
        onView(withId(R.id.login_fragment)).check(matches(isDisplayed()));

        //clear view
        onView(withId(R.id.email)).perform(click());
        onView(withId(R.id.email)).perform(clearText(), pressKey(66));
        onView(withId(R.id.password)).perform(click());
        onView(withId(R.id.password)).perform(clearText(), pressKey(66));

        // to short password
        onView(withId(R.id.email)).perform(click());
        onView(withId(R.id.email)).perform(typeText("test@epfl.ch"), pressKey(66));
        onView(withId(R.id.password)).perform(click());
        onView(withId(R.id.password)).perform(typeText("aaa"), pressKey(66));
        onView(withId(R.id.email_sign_in_button)).perform(click());
        onView(withId(R.id.login_fragment)).check(matches(isDisplayed()));
        
        //clear view
        onView(withId(R.id.email)).perform(click());
        onView(withId(R.id.email)).perform(clearText(), pressKey(66));
        onView(withId(R.id.password)).perform(click());
        onView(withId(R.id.password)).perform(clearText(), pressKey(66));

        // to short password
        onView(withId(R.id.email)).perform(click());
        onView(withId(R.id.email)).perform(typeText("test@epfl.ch"), pressKey(66));
        onView(withId(R.id.password)).perform(click());
        onView(withId(R.id.password)).perform(typeText("aaa"), pressKey(66));
        onView(withId(R.id.email_sign_in_button)).perform(click());
        onView(withId(R.id.login_fragment)).check(matches(isDisplayed()));

        //clear view
        onView(withId(R.id.email)).perform(click());
        onView(withId(R.id.email)).perform(clearText(), pressKey(66));
        onView(withId(R.id.password)).perform(click());
        onView(withId(R.id.password)).perform(clearText(), pressKey(66));

        // everything works as it should
        try {
            onView(withId(R.id.email)).perform(click());
            onView(withId(R.id.email)).perform(typeText("test@epf.ch"), pressKey(66));
            onView(withId(R.id.password)).perform(click());
            onView(withId(R.id.password)).perform(typeText("password"), pressKey(66));
            onView(withId(R.id.email_sign_in_button)).perform(click());
            onView(withId(R.id.login_fragment)).check(matches(isDisplayed()));
        } catch (NoMatchingViewException e) {
            assertTrue(true);
        }


    }


}