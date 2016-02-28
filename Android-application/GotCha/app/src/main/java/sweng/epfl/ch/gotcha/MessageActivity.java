package sweng.epfl.ch.gotcha;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.*;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MessageActivity extends AppCompatActivity {

    Button sendMessageButton;
    public static Context mContext;
    private long itemId;
    private String messageText;
    private String seller;
    private long senderId ;
    private long receiverId;
    private int response;
    NetworkGotchaClient networkGotchaClient = new NetworkGotchaClient(new DefaultNetworkProvider());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        mContext = this;
        seller=getIntent().getExtras().getString("receiverName");
        itemId=getIntent().getExtras().getLong("itemIdLong");
        receiverId=getIntent().getExtras().getLong("receiverId");
        TextView sellerName = (TextView) findViewById(R.id.sellerName);
        sellerName.setText(seller);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        int userId=prefs.getInt("UserId", 0);
        Log.d("userId", String.valueOf(userId));
        senderId = userId;

        //Display toast to show whether the message is sent
        sendMessageButton = (Button) findViewById(R.id.sendButton);
        sendMessageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText editText = (EditText) findViewById(R.id.messageText);
                messageText = editText.getText().toString();

                // Check if no view has focus:
                // Hide keyboard
                View view = getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }


                if (messageText.length()==0)
                {
                    showToast(getApplicationContext(),"Message cannot be empty. Please try again."); //Toast to show failure
                }
                else {

                    //Post messages to server

                    new sendMsgAsyncTask().execute();

                }
                editText.setText("");

            }
        });

    }

    class sendMsgAsyncTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            response=-1;

            try {
                Uri builtUri = Uri.parse("").buildUpon().appendQueryParameter("senderId", String.valueOf(senderId)).appendQueryParameter("receiverId", String.valueOf(receiverId)).appendQueryParameter("itemId", String.valueOf(itemId)).appendQueryParameter("text", messageText).build();
                response = networkGotchaClient.postMessage(builtUri.toString().substring(1));

            } catch (IOException e) {
                e.printStackTrace();
            } catch (sweng.epfl.ch.gotcha.ClientException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result)
        {
            Log.d("SSSSSS", String.valueOf(response));
            if (response == 201)
                showToast(getApplicationContext(),"Your message is successfully sent to " + seller + ".");  //Toast to show success
            else
                showToast(getApplicationContext(),"Failed to send message to " + seller + ", please try again later."); //Toast to show failure
        }
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

    public void showToast(Context context, String toastText) {

        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, toastText, duration);
        toast.show();

    }

}