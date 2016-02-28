package sweng.epfl.ch.gotcha;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

// Create an adatper for messagelist
public class MessageAdapter extends ArrayAdapter {
    private Context context;
    private int layoutResourceId;
    private List<MyMessage> data = new ArrayList<>();

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;
    private static final int TYPE_MAX_COUNT = TYPE_SEPARATOR + 1;

    private LayoutInflater mInflater;

    private TreeSet<Integer> mSeparatorsSet = new TreeSet<Integer>();

    public MessageAdapter(Context context,
                          int layoutResourceId) {
        super(context, layoutResourceId);
        this.context = context;
        this.layoutResourceId = layoutResourceId;

    }

    public void addItem(final MyMessage item) {
        data.add(item);
    }

    public void removeItem(final int pos) {
        data.remove(pos);
    }

    public void addSeparatorItem(final MyMessage item) {
        data.add(item);
        // save separator position
        mSeparatorsSet.add(data.size() - 1);
    }

    @Override
    public int getItemViewType(int position) {
        return mSeparatorsSet.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_MAX_COUNT;
    }

    public int getCount() {
        return data.size();
    }

    public MyMessage getItem(int position) {
        return data.get(position);
    }

    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View contentView, ViewGroup parent) {
        View row = contentView;
        MessageHolder holder = null;
        int type = getItemViewType(position);


        if (row == null) {
            mInflater = ((Activity) context).getLayoutInflater();
            holder = new MessageHolder();
            holder.item = null;
            holder.sender = null;
            holder.message = null;


            switch (type) {
                case TYPE_ITEM:
                    row = mInflater.inflate(layoutResourceId, null);
                    Message message = data.get(position).getMessageInMyMessage();
                    String userName = data.get(position).getUserNameInMyMessage();
                    holder.sender = (TextView) row.findViewById(R.id.Sender);
                    holder.message = (TextView) row.findViewById(R.id.Message);
                    holder.sender.setText(userName);
                    holder.message.setText(message.getMessage());
                    if (message.isOpened()==false) {
                        holder.message.setTextColor(Color.RED);
                    }
                    else {
                        holder.message.setTextColor(Color.GRAY);
                    }
                    System.out.println(message.getMessage());

                    break;
                case TYPE_SEPARATOR:
                    row = mInflater.inflate(R.layout.section_header, null);
                    System.out.println(data.get(position+1).getItemNameInMyMessage());
                    String itemName = data.get(position+1).getItemNameInMyMessage();
                    holder.item = (TextView)row.findViewById(R.id.textSeparator);
                    holder.item.setText("Item: " + itemName);
                    break;
            }

            row.setTag(holder);

        } else {
            holder = (MessageHolder) row.getTag();
        }


        return row;
    }

    static class MessageHolder {
        TextView item;
        TextView sender;
        TextView message;
    }


}
