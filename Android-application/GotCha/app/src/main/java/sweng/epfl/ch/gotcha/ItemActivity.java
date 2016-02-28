package sweng.epfl.ch.gotcha;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class ItemActivity extends AppCompatActivity {

    private User seller ;
    private long itemIdLong;
    private NetworkGotchaClient networkGotchaClient;
    private Integer itemIDInt;
    private ProgressDialog dialog;
    private Context context;
    ImageView imageView ;
    TextView itemName ;
    TextView itemDesc ;
    TextView itemPrice ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_item);

        imageView = (ImageView) findViewById(R.id.item_picture);
        itemName = (TextView) findViewById(R.id.t_ItemName);
        itemPrice = (TextView) findViewById(R.id.t_itemPrice);
        itemDesc = (TextView) findViewById(R.id.t_itemDescription);
        Intent intent = getIntent();
        itemIdLong = Long.parseLong(intent.getStringExtra("item_id"));
        this.context = this;
        networkGotchaClient = new NetworkGotchaClient(new DefaultNetworkProvider());
        dialog = new ProgressDialog(ItemActivity.this);

        new LoadItemDataAsyncTask().execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_item, menu);
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
                //NavUtils.navigateUpFromSameTask(this);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void writeMessage(View view) {

        if (seller==null)
        {
            Context context = getApplicationContext();
            CharSequence text = "Seller does not exist, cannot proceed to send message.";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        else
        {
            Intent intent = new Intent(this, MessageActivity.class);
            intent.putExtra("receiverName", seller.getFirstName() + " " + seller.getLastName());
            intent.putExtra("receiverId", seller.getUserId());
            intent.putExtra("itemIdLong", itemIdLong);
            startActivity(intent);
        }
    }

    class LoadItemDataAsyncTask extends AsyncTask<Void, Void, Item> {
        @Override
        protected Item doInBackground(Void... voids) {
            try {
                Item item = networkGotchaClient.getItemById(itemIdLong);
                return item;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }

        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Loading item details");
            dialog.show();
        }


        protected void onPostExecute(Item item) {

            super.onPostExecute(item);
            if (item == null) {
                // network connection failed, alert user
                new AlertDialog.Builder(context)
                        .setTitle("Network failure")
                        .setMessage(getResources().getString(R.string.alert_failure_network))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
            } else {
                itemName.setText(item.getName());
                itemDesc.setText(item.getDescription());
                itemPrice.setText(item.getPrice().toString() + " CHF");

                Uri builtUri = Uri.parse("").buildUpon().appendQueryParameter("ownerID", item.getOwner() + "").build();

                new LoadUserDataAsyncTask().execute(builtUri.toString().substring(1));

                String completeImageData = item.getImage();
                String imageDataBytes = completeImageData.substring(completeImageData.indexOf(",") + 1);
                byte[] decodedString = Base64.decode(imageDataBytes, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageView.setImageBitmap(decodedByte);

            }
        }

    }

    class LoadUserDataAsyncTask extends AsyncTask<String, Void, User> {

        @Override
        protected User doInBackground(String... ownerID) {
            try {
                String id = ownerID[0].substring(8, ownerID[0].length());
                long idLong = Long.parseLong(id);
                User user = networkGotchaClient.getUserById(idLong);
                return user;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(User result) {
            super.onPostExecute(result);
            dialog.dismiss();
            if (result != null) {
                User user = result;
                seller = result ;

                TextView itemSeller = (TextView) findViewById(R.id.sellerEmail);
                itemSeller.setText(user.getEmail().toString());
            } else {
                Context context = getApplicationContext();
                CharSequence text = "No seller information from server";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

            }
        }
        protected void onResume(){

        }

    }


}

