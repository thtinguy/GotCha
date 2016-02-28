package sweng.epfl.ch.gotcha;

import android.net.Uri;
import android.os.SystemClock;
import android.test.ActivityInstrumentationTestCase2;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressKey;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerActions.closeDrawer;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.not;

/**
 * Created by emmawestman on 09/12/15.
 */
public class SearchTest  extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity mFirstTestActivity;
    private final String testServerURL = "https://powerful-spire-4471.herokuapp.com/";
    private NetworkGotchaClient networkGotchaClient;

    public SearchTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mFirstTestActivity = getActivity();
        networkGotchaClient = new NetworkGotchaClient(new DefaultNetworkProvider());

        // Set the URL for the test server
        networkGotchaClient.setmServerUrl(testServerURL);

        // create a dummy info about item to search for w

        String title_name = "Biology";
        String item_price = "50";
        String item_description = "This is a very interesting book";

        // Build the URI for the item
        final String NAME = "name";
        final String DESCRIPTION = "description";
        final String PRICE = "price";
        final String USERID = "userId";
        final String CATEGORY = "category";
        final String IMAGE = "image";
        Uri builtUri1 = Uri.parse("").buildUpon().appendQueryParameter(NAME, title_name).appendQueryParameter(DESCRIPTION, item_description)
                .appendQueryParameter(PRICE, item_price).appendQueryParameter(USERID, String.valueOf(1))
                .appendQueryParameter(CATEGORY, "BOOK").appendQueryParameter(IMAGE, null).build();


        //post the item to test server
        networkGotchaClient.postItem(builtUri1.toString().substring(1));

    }

    /*
        test search on key work of two words
     */
    public void testTwoWordMatch() {
        String searchInput = "Advanced Biology";


        //Search for an item where the title two words
        closeDrawer(R.id.drawer_layout);
        onView(withId(R.id.search)).perform(click());
        onView(isAssignableFrom(EditText.class)).perform(typeText(searchInput), pressKey(66));

        //verify that we got an error message
        onView(withText("Search")).check(matches(isDisplayed()));
    }

    /*
        tests search on exact match of an item
     */
    public void testExactMatch() {
        String searchInput = "Biology";

        //Search for an item where the title matches exactly
        closeDrawer(R.id.drawer_layout);
        onView(withId(R.id.search)).perform(click());
        onView(isAssignableFrom(EditText.class)).perform(typeText(searchInput), pressKey(66));

        //verify that we got some response from the server
        onData(anything()).inAdapterView(withId(R.id.mainListView)).atPosition(0).check(matches(isDisplayed()));
        onView(withText("Biology")).check(matches(isDisplayed()));
    }

    /*
        tests search on partial match of an item
     */
    public void testPartialMatch() {
        String searchInput = "B";

        //Search for an item where the title matches partially
        closeDrawer(R.id.drawer_layout);
        onView(withId(R.id.search)).perform(click());
        onView(isAssignableFrom(EditText.class)).perform(typeText(searchInput), pressKey(66));

        //verify that we got some response from the server
        onData(anything()).inAdapterView(withId(R.id.mainListView)).atPosition(0).check(matches(isDisplayed()));
        onView(withText("Biology")).check(matches(isDisplayed()));
    }

    /*
        Verify that the layout is correct when there is no match for the search
     */
    public void testNoMatch() {
        //search for an item that doesn't exist
        closeDrawer(R.id.drawer_layout);
        onView(withId(R.id.search)).perform(click());
        onView(isAssignableFrom(EditText.class)).perform(typeText("Chemistry"), pressKey(66));

        //verify there is an error message
        onView(withId(R.id.no_items_text_view)).check(matches(isDisplayed()));
    }


    protected void tearDown () throws IOException, ClientException {
        String url = networkGotchaClient.mServerUrl + "application/reset";
        URL urlObject = new URL(url);
        networkGotchaClient.connection(urlObject , NetworkGotchaClient.Request.GET);
    }

}
