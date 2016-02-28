package sweng.epfl.ch.gotcha;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by nguyenthaitinh on 07/11/15.
 */
// Create an adatper for itemlist
public class ItemAdapter extends ArrayAdapter {
    private Context context;
    private int layoutResourceId;

    boolean hasButtons;

    private List<Item> itemList;

    public ItemAdapter(Context context,
                       int layoutResourceId,
                       List<Item> itemList,boolean hasButtons) {
        super(context, layoutResourceId, itemList);
        //this.notifyDataSetChanged();
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.itemList = itemList;
        this.hasButtons=hasButtons;
    }

    @Override
    public View getView(int position, View contentView, ViewGroup parent) {
        View row = contentView;
        ItemHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ItemHolder();
            holder.image = (ImageView) row.findViewById(R.id.image);
            holder.title = (TextView) row.findViewById(R.id.title);
            holder.price = (TextView) row.findViewById(R.id.price);
            holder.description = (TextView) row.findViewById(R.id.description);
            holder.id = (RelativeLayout) row.findViewById(R.id.itemID);
            holder.button2= (Button) row.findViewById(R.id.button2);
            row.setTag(holder);

        } else {
            holder = (ItemHolder) row.getTag();
        }
        Item item = itemList.get(position);
        holder.title.setText(item.getName());
        holder.price.setText(item.getPrice() + " CHF");
        holder.description.setText(item.getDescription());

        String completeImageData = item.getImage();
        if (!completeImageData.equals("null")) {
            String imageDataBytes = completeImageData.substring(completeImageData.indexOf(",") + 1);
            byte[] decodedString = Base64.decode(imageDataBytes, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.image.setImageBitmap(decodedByte);
        }else{
            holder.image.setImageResource(R.drawable.no_image);
        }
        holder.id.setTag(item.getId());
        if(hasButtons) holder.button2.setVisibility(View.VISIBLE);
        return row;
    }
    static class ItemHolder {
        ImageView image;
        TextView title;
        TextView price;
        TextView description;
        RelativeLayout id;
        Button button2;
    }

    public int getCount() {
        if (itemList != null)
            return itemList.size();
        return 0;
    }

    public Item getItem(int position) {
        if (itemList != null)
            return itemList.get(position);
        return null;
    }





}
