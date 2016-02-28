package sweng.epfl.ch.gotcha;

        import android.app.Activity;
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
        import android.support.v4.app.Fragment;
        import android.support.v4.widget.SwipeRefreshLayout;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.AdapterView;
        import android.widget.ListView;
        import android.widget.TextView;
        import android.widget.Toast;

        import java.util.ArrayList;
        import java.util.Collections;
        import java.util.Comparator;
        import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyMessageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyMessageFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    SwipeRefreshLayout swipeRefreshLayout;
    private static final String ARG_SECTION_NUMBER = "section_number";
    private ListView msgListView;
    private long receiverId ;
    private List<MyMessage> myMessageList = new ArrayList<>();
    MessageAdapter messageAdapter;
    private View myMessageView;
    NetworkGotchaClient networkGotchaClient = new NetworkGotchaClient(new DefaultNetworkProvider());

    public MyMessageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param sectionNumber shows the section selected
     * @return A new instance of fragment MyMessageFragment.
     */

    public static MyMessageFragment newInstance(int sectionNumber) {
        MyMessageFragment fragment = new MyMessageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        myMessageView = inflater.inflate(R.layout.fragment_my_message, container, false);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        int userId=prefs.getInt("UserId", 0);
        Log.d("userId", String.valueOf(userId));
        receiverId = userId;
        if (userId == 0)
        {
            new AlertDialog.Builder(getActivity())
                    .setMessage("Please log in first and try again.")
                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

        }
        else {
            swipeRefreshLayout = (SwipeRefreshLayout) myMessageView.findViewById(R.id.swipe_refresh_layout);
            swipeRefreshLayout.setOnRefreshListener(this);
            swipeRefreshLayout.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            swipeRefreshLayout.setRefreshing(true);
                                            myMessageList.clear();
                                            fetchMessages();
                                        }
                                    }
            );
        }

        return myMessageView;
    }

    @Override
    public void onRefresh() {
        myMessageList.clear();
        fetchMessages();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }


    private void fetchMessages() {
        swipeRefreshLayout.setRefreshing(true);
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            try {
                new getMyMessagesTask().execute();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void addMyMessage(MyMessage myMessage) {
        myMessageList.add(myMessage);
    }

    public void openMessage(Message message, String sender, Item item) {
        Intent intent = new Intent(getActivity().getApplicationContext(), ReadMessage.class);
        intent.putExtra("messageText", message.getMessage());
        intent.putExtra("messageId", message.getMessageId());
        intent.putExtra("isOpened", message.isOpened());
        intent.putExtra("itemId", item.getId());
        intent.putExtra("sender", sender);
        intent.putExtra("senderId", message.getSenderId());
        intent.putExtra("receiverId", message.getReceiverId());
        intent.putExtra("itemName", item.getName());
        startActivity(intent);
    }

    private class getMyMessagesTask extends AsyncTask<Void, Void, List<MyMessage>> {

        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            // Showing progress dialog before sending http request
            this.pDialog  = new ProgressDialog(getContext());
            this.pDialog.setMessage("Please wait while retrieving your messages...");
            this.pDialog.setIndeterminate(true);
            this.pDialog.setCancelable(false);
            this.pDialog.show();
        }

        @Override
        protected List<MyMessage> doInBackground(Void ... voids) {
            // params comes from the execute() call: params[0] is the url.
            List<Message> messageList = null;
            MyMessage myMessage = null;

            try {
                messageList = networkGotchaClient.getMessages(receiverId);
                Log.d("no. of msg:", String.valueOf(messageList.size()));
                for (Message element : messageList) {
                    Log.d("add message:", String.valueOf(element.getItemId()));
                    String user= null;
                    Item item=null;

                    try {
                        user = networkGotchaClient.getUserNameById(element.getSenderId());
                        if (element.getItemId()!=0)
                        {item= networkGotchaClient.getItemById(element.getItemId());}
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (item==null)
                    {
                        item = new Item(Long.parseLong(String.valueOf(0)), "Item does not exist", "Item does not exist", Item.Category.OTHER ,0, Long.parseLong(String.valueOf(0)), null);

                    }
                    myMessage = new MyMessage(item, element, user);
                        addMyMessage(myMessage);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return myMessageList;

        }

        @Override
        protected void onPostExecute(List<MyMessage> MessageList)
        {

            super.onPostExecute(MessageList);

            this.pDialog.dismiss();

            if (myMessageList.size() !=0) {


                final Comparator<MyMessage> myComparator = new Comparator<MyMessage>() {
                    public int compare(MyMessage obj1, MyMessage obj2) {
                        return obj1.getItemIdInMyMessage().compareTo(obj2.getItemIdInMyMessage());
                    }
                };

                Collections.sort(myMessageList, myComparator);


                messageAdapter = new MessageAdapter(getActivity(), R.layout.message_row);


                for (int i = 0; i < myMessageList.size(); i++) {

                    if(  i!=0 ) {
                        if (myMessageList.get(i).getItemIdInMyMessage() != myMessageList.get(i - 1).getItemIdInMyMessage()) {
                            messageAdapter.addSeparatorItem(null);
                        }
                    }
                    else if (i == 0) {
                        messageAdapter.addSeparatorItem(null);
                    }

                    messageAdapter.addItem(myMessageList.get(i));
                }


                msgListView = (ListView) myMessageView.findViewById(R.id.msgListView);
                msgListView.setAdapter(messageAdapter);

                msgListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> a, View v, int pos, long id) {

                        MyMessage myMessages = messageAdapter.getItem(pos);
                        openMessage(myMessages.getMessageInMyMessage(), myMessages.getUserNameInMyMessage(), myMessages.getItemInMyMessage());
                    }
                });

                msgListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> av, View v, final int pos, final long id) {

                        final AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
                        b.setIcon(android.R.drawable.ic_dialog_alert);
                        b.setMessage("Do you want to delete this message?");

                        b.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        });
                        b.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                int response = -1;

                                try {
                                    response = networkGotchaClient.deleteMessage(messageAdapter.getItem(pos).getMessageInMyMessage());
                                    messageAdapter.removeItem(pos);
                                    onRefresh();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                if (response == 200) {
                                    showToast(getActivity(),"You deleted the message.");
                                } else {
                                    showToast(getActivity(),"The message cannot be deleted at the moment, please try again later.");
                                }
                            }
                        });

                        b.show();
                        return true;
                    }
                });

            }
            else
            {

                new AlertDialog.Builder(getActivity())
                        .setMessage("You have no message at the moment.")
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        }
    }

    public void showToast(Context context, String toastText) {

        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, toastText, duration);
        toast.show();

    }

}
