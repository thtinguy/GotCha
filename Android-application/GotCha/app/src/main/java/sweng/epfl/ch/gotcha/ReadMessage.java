package sweng.epfl.ch.gotcha;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


public class ReadMessage extends AppCompatActivity {

    private String senderOfMessage;
    private String messageReceived;
    private long senderId;
    private long receiverId;
    private long messageId;
    private long itemId;
    private String itemName;
    NetworkGotchaClient networkGotchaClient = new NetworkGotchaClient(new DefaultNetworkProvider());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_message);

        senderOfMessage=getIntent().getExtras().getString("sender");
        TextView sender= (TextView) findViewById(R.id.sender);
        sender.setText(senderOfMessage);

        messageId =getIntent().getExtras().getLong("messageId");

        messageReceived =getIntent().getExtras().getString("messageText");
        TextView message_details= (TextView) findViewById(R.id.message_details);
        message_details.setText(messageReceived);

        senderId=getIntent().getExtras().getLong("senderId");
        receiverId=getIntent().getExtras().getLong("receiverId");
        itemId=getIntent().getExtras().getLong("itemId");
        itemName=getIntent().getExtras().getString("itemName");

        if (getIntent().getExtras().getBoolean("isOpened")==false) {
            new openMsgAsyncTask().execute();
        }

        if (itemName!=null) {
            TextView item = (TextView) findViewById(R.id.item_name);
            item.setText(itemName);
        } else {
            TextView item = (TextView) findViewById(R.id.textView3);
            item.setText("Item does not exist");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_read_message, menu);
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

    public void replyMessage(View view) {
        Intent intent = new Intent(this, ReplyMessageActivity.class);
        intent.putExtra("itemId", itemId);
        intent.putExtra("sender", senderOfMessage);
        intent.putExtra("senderId", senderId);
        intent.putExtra("receiverId", receiverId);
        intent.putExtra("messageReceived", messageReceived);
        startActivity(intent);
    }

    class openMsgAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                networkGotchaClient.openedMessage(messageId);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

}
