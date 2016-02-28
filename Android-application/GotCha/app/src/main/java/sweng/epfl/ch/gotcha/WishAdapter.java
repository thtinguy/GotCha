package sweng.epfl.ch.gotcha;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

// Create an adatper for messagelist
public class WishAdapter extends ArrayAdapter {
    private Context context;
    private int layoutResourceId;
    private List<Wish> data;

    public WishAdapter(Context context,
                       int layoutResourceId,
                       List<Wish> data) {
        super(context, layoutResourceId, data);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
    }

    @Override
    public View getView(int position, View contentView, ViewGroup parent) {
        View row = contentView;
        WishHolder holder = null;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new WishHolder();
            holder.keyword = (TextView) row.findViewById(R.id.keyword);
            holder.button1 = (Button) row.findViewById(R.id.removeWish);
            row.setTag(holder);
        }else{
           holder=(WishHolder)row.getTag();
        }
        Wish wish = data.get(position);
        holder.keyword.setText(String.valueOf(wish.getKeyword()));
        holder.keyword.setTag(wish.getWishId());
        holder.button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TextView textView = null;
                ViewGroup row = (ViewGroup) v.getParent();
                for (int itemPos = 0; itemPos < row.getChildCount(); itemPos++) {
                    View view = row.getChildAt(itemPos);
                    if (view instanceof TextView) {
                        textView = (TextView) view; //Found it!
                        break;
                    }
                }
                Integer itemId = Integer.parseInt((textView.getTag()).toString());
                NetworkGotchaClient networkGotchaClient = new NetworkGotchaClient(new DefaultNetworkProvider());
                try {
                    networkGotchaClient.removeWish(itemId);
                } catch (ClientException e) {
                    Toast.makeText(getContext(), R.string.deleteError, Toast.LENGTH_LONG);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
       return row;
    }


        static class WishHolder {
            TextView keyword;
            Button button1;
            RelativeLayout id;
        }


    }


