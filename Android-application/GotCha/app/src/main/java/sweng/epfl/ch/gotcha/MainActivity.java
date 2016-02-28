package sweng.epfl.ch.gotcha;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import java.net.CookieHandler;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.net.URI;
import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    public static java.net.CookieManager msCookieManager = new java.net.CookieManager();
    public static Context contextOfApplication;
    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        contextOfApplication = getApplicationContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CookieHandler.setDefault(msCookieManager);
        msCookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String cookie = prefs.getString("Cookie", null);
        if (!(cookie == null)) {
            try {
                msCookieManager.getCookieStore().add(new URI(NetworkGotchaClient.mServerUrl), HttpCookie.parse(cookie).get(0));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));


    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        Fragment fragemnt;
        Log.d("MAIN_ACTIVITY", "itemSelected position: " + position);
        FragmentManager fragmentManager = getSupportFragmentManager();
        Intent intent = new Intent(this, ListActivity.class);
        switch (position) {
            case 0:
                fragmentManager.beginTransaction().replace(R.id.container, CategoryFragment.newInstance(position + 1))
                        .commit();
                break;
            case 1:
                fragmentManager.beginTransaction().replace(R.id.container, LoginFragment.newInstance(position + 1))
                        .commit();
                break;
            case 2:
                fragmentManager.beginTransaction().replace(R.id.container, SellItemFragment.newInstance(position + 1))
                        .commit();
                break;
            case 3:
                fragmentManager.beginTransaction().replace(R.id.container, MyMessageFragment.newInstance(position + 1))
                        .commit();
                break;
            case 4:
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                int userid = prefs.getInt("UserId", 0);
                Log.d("UserId", String.valueOf(userid)) ;
                if (userid != 0) {
                    intent.putExtra("title", getString(R.string.myitems));
                    //Start List activity
                    startActivity(intent);
                }
                else {
                    Toast.makeText(this, "You must log in to view this", Toast.LENGTH_SHORT).show();
                }
                break;
            case 5:
                fragmentManager.beginTransaction().replace(R.id.container, WishListFragment.newInstance(position + 1)).commit();
                break;
        }
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.getContextOfApplication());
                String cookie = prefs.getString("Cookie", "");
                if (cookie.isEmpty()) {
                    mTitle = getString(R.string.title_section2a);
                } else {
                    mTitle = getString(R.string.title_section2b);
                }
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
            case 4:
                mTitle = getString(R.string.title_section4);
                break;
            case 5:
                mTitle = getString(R.string.title_section5);
                break;
            case 6:
                mTitle = getString(R.string.title_section6);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            this.menu = menu;

            // Associate searchable configuration with the SearchView
            SearchManager searchManager =
                    (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            SearchView searchView =
                    (SearchView) menu.findItem(R.id.search).getActionView();
            searchView.setSearchableInfo(
                    searchManager.getSearchableInfo(getComponentName()));

            SearchView.OnQueryTextListener textChangeListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {
                    return true;
                }

                @Override
                public boolean onQueryTextSubmit(String query) {
                    if(query.contains(" ")) {
                        new AlertDialog.Builder(MainActivity.this)
                                .setMessage("Sorry, it's only possible to make a search of one word.")
                                .setTitle("Search")
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        //close dialog
                                    }
                                })
                                .show();
                    }else {
                        Intent i = new Intent(MainActivity.this, ListActivity.class);
                        i.putExtra("keyword", query);
                        i.putExtra("title", "Search Results");
                        startActivity(i);
                    }
                    return true;

                }
            };
            searchView.setOnQueryTextListener(textChangeListener);

            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                Log.d("searcheding!!", "");
                SearchView searchView =
                        (SearchView) menu.findItem(R.id.search).getActionView();
                String keyword = searchView.getQuery().toString();
                Log.d("searched for: ", keyword);
                search(keyword);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

    }

    private void search(String keyword) {
        Intent i = new Intent(MainActivity.this, ListActivity.class);
        i.putExtra("keyword", keyword);
        startActivity(i);
    }

    public boolean onSearchRequested() {
        Intent i = new Intent(MainActivity.this, ListActivity.class);
        i.putExtra("keyword", "hello");
        startActivity(i);
        return super.onSearchRequested();
    }

    public static Context getContextOfApplication() {
        return contextOfApplication;
    }


}
