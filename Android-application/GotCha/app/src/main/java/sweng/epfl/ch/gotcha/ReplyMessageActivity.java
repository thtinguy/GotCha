package sweng.epfl.ch.gotcha;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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

/**
 * Created by kongchingyiii on 26/11/15.
 */
public class ReplyMessageActivity extends AppCompatActivity {

    Button replyMessageButton;
    public static Context mContext;
    private long itemId;
    private String messageText;
    private String senderOfMessage;
    private String messageReceived;
    private long senderId;
    private long receiverId;
    private int response;
    NetworkGotchaClient networkGotchaClient = new NetworkGotchaClient(new DefaultNetworkProvider());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply_message2);
        mContext = this;

        senderId=getIntent().getExtras().getLong("receiverId");
        receiverId=getIntent().getExtras().getLong("senderId");
        itemId=getIntent().getExtras().getLong("itemId");

        senderOfMessage=getIntent().getExtras().getString("sender");
        TextView sender= (TextView) findViewById(R.id.senderName);
        sender.setText(senderOfMessage);

        messageReceived =getIntent().getExtras().getString("messageReceived");
        TextView message = (TextView) findViewById(R.id.messageReceived);
        message.setText(messageReceived);

        //Display toast to show whether the message is sent
        replyMessageButton = (Button) findViewById(R.id.sendButton);
        replyMessageButton.setOnClickListener(new View.OnClickListener() {

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

                if (messageText.length() == 0) {
                    showToast(getApplicationContext(),"Message cannot be empty. Please try again."); //Toast to show failure
                } else {

                    //Post messages to server
                    new replyMsgAsyncTask().execute();

                }


            }
        });

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

    class replyMsgAsyncTask extends AsyncTask<Void, Void, String> {

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
            if (response == 201)
                showToast(getApplicationContext(),"Your message is successfully sent to " + senderOfMessage + "."); //Toast to show success
            else
                showToast(getApplicationContext(),"Failed to send message to " + senderOfMessage + ", please try again later."); //Toast to show failure

            EditText editText = (EditText) findViewById(R.id.messageText);
            editText.setText("");
        }
    }


}