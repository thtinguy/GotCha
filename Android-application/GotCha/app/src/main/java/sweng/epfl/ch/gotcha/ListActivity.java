package sweng.epfl.ch.gotcha;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    SwipeRefreshLayout swipeRefreshLayout;
    NetworkGotchaClient networkGotchaClient = new NetworkGotchaClient(new DefaultNetworkProvider());
    private ListView mainListView;
    private List<Item> itemList;
    private ItemAdapter itemAdapter;
    private Item.Category cat;
    private TextView noItemsTextView;
    private String keyword;
    private String cookie;
    private Context context;
    private boolean noNetworkConn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.context = this;
        setCurrentCategory();
        Intent intent = getIntent();
        String message = intent.getStringExtra("title");
        getSupportActionBar().setTitle(message);
        keyword = intent.getStringExtra("keyword");
        itemList = new ArrayList<>();

        // get the cookie
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.getContextOfApplication());
        cookie = prefs.getString("Cookie", "");

        // Create the text view
        setContentView(R.layout.activity_list);

        //Find the text view that displays a message if there are no items it the category
        noItemsTextView = (TextView) findViewById(R.id.no_items_text_view);
        displayNoItemsMessage();
        noItemsTextView.setVisibility(View.INVISIBLE);

        if (cat.equals(Item.Category.MYITEMS)) {
            if (cookie.isEmpty()) {
                new AlertDialog.Builder(this)
                        .setTitle(getResources().getString(R.string.loginAlert))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                            }
                        })
                        .show();
            }
        }


        int SDK_INT = android.os.Build.VERSION.SDK_INT;

        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);


            boolean hasButtons = false;
            if (getCurrentCategory() == Item.Category.MYITEMS) hasButtons = true;

            itemAdapter = new ItemAdapter(this, R.layout.item_row, itemList, hasButtons);
            mainListView = (ListView) findViewById(R.id.mainListView);
            mainListView.setAdapter(itemAdapter);

            swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
            swipeRefreshLayout.setOnRefreshListener(this);
            swipeRefreshLayout.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            itemList.clear();

                                        }
                                    }
            );
        }
        fetchItems();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void addItem(Item item) {
        itemList.add(item);
    }


    // Add new item to the list
    public void openItem(View view) {
        Log.d("BBBBBB","AA");
        Intent intent = new Intent(view.getContext(), ItemActivity.class);
        intent.putExtra("item_id", view.getTag().toString());
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        itemList.clear();
        fetchItems();
    }


    // Help method for testing that the correct category is displayed
    public void setCategory(Item.Category cat) {
        this.cat = cat;
    }

    // get the chosen category from the previous activity
    private void setCurrentCategory() {
        Intent intent = getIntent();
        String message = intent.getStringExtra("title");

        cat = Item.Category.OTHER;
        if (message != null) {
            if (message.equals("Books")) cat = Item.Category.BOOK;
            else if (message.equals("Clothes")) cat = Item.Category.CLOTHING;
            else if (message.equals("Household")) cat = Item.Category.HOUSE;
            else if (message.equals("Electronics")) cat = Item.Category.ELECTRONIC;
            else if (message.equals("Sport equipment")) cat = Item.Category.SPORT;
            else if (message.equals("Search Results")) cat = Item.Category.SEARCH;
            else if (message.equals("MyItems")) {
                cat = Item.Category.MYITEMS;
                setTitle(getString(R.string.title_section5));
            } else if (message.equals("My wish list")) {
                cat = Item.Category.WISHLIST;
                setTitle(getString(R.string.title_section6));
            }

        }


    }


    public Item.Category getCurrentCategory() {
        return this.cat;
    }

    private void fetchItems() {
        swipeRefreshLayout.setRefreshing(true);
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            try {
                new GetItemsByCatTask().execute();

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
        swipeRefreshLayout.setRefreshing(false);
    }

    public List<Item> getItemList() {
        return this.itemList;
    }


    private void displayNoItemsMessage() {
        if (cat.equals(Item.Category.SEARCH)) {
            noItemsTextView.setText(R.string.no_search_results_message);
        } else if (cat.equals(Item.Category.MYITEMS)) {
            if (!cookie.isEmpty()) {
                noItemsTextView.setText(R.string.no_myitems_message);
            } else {
                noItemsTextView.setText(R.string.loginAlert);
            }
        } else if(noNetworkConn){
            noItemsTextView.setText(R.string.alert_failure_network);
        }else {
            noItemsTextView.setText(R.string.no_items_message);
        }
        noItemsTextView.setVisibility(View.VISIBLE);
    }


    public void deleteItem(View view) throws IOException {
        Integer itemId = Integer.parseInt((((RelativeLayout) view.getParent().getParent()).getTag()).toString());
        NetworkGotchaClient networkGotchaClient = new NetworkGotchaClient(new DefaultNetworkProvider());
        try {
            networkGotchaClient.deleteItem(itemId);
            onRefresh();
        } catch (ClientException e) {
            Toast.makeText(getApplicationContext(), R.string.deleteError, Toast.LENGTH_LONG);
        }
    }


    private class GetItemsByCatTask extends AsyncTask<Void, Void, List<Item>> {
        private final ProgressDialog dialog = new ProgressDialog(ListActivity.this);

        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Loading items from server");
            dialog.show();
        }

        //prepare a toast if the network conection fails
        CharSequence text = "No network connection";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);

        @Override
        protected List<Item> doInBackground(Void... voids) {
            if (cat.equals(Item.Category.SEARCH)) {
                try {
                    List<Item> itemList = networkGotchaClient.getSearchResult(keyword);
                    noNetworkConn = false;
                    return itemList;
                } catch (Exception e) {
                    //show error message network failure
                    noNetworkConn = true;
                    toast.show();
                    e.printStackTrace();
                }
            } else {
                try {
                    Log.d("FFFFFF: ", cat.toString());
                    List<Item> itemList = networkGotchaClient.getItemsByCat(cat);
                    noNetworkConn = false;
                    return itemList;
                } catch (Exception e) {
                    //show error message network failure
                    noNetworkConn = true;
                    toast.show();
                    e.printStackTrace();
                }
            }
            return null;
        }


        protected void onPostExecute(List<Item> result) {
            dialog.dismiss();
            if (result == null) {
                Log.d("listActivity, ", "null respose");
                displayNoItemsMessage();
            } else {
                noItemsTextView.setVisibility(View.INVISIBLE);
                for (Item element : result) {
                    addItem(element);
                }
                itemAdapter.notifyDataSetChanged();
            }
        }

    }


}
