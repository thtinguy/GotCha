package sweng.epfl.ch.gotcha;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by nguyenthaitinh on 12/11/15.
 */
public class SpinnerAdapter extends BaseAdapter {

    private Context context;
    private int layoutResourceId;
    private List<SpinnerLine> categories;
    public SpinnerAdapter(Context context, int layoutResourceId, List<SpinnerLine> categories) {
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.categories = categories;
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        CategoryHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new CategoryHolder();
            holder.icon = (ImageView) row.findViewById(R.id.icon_category);
            holder.categoryName = (TextView) row.findViewById(R.id.single_category);
            row.setTag(holder);

        } else {
            holder = (CategoryHolder) row.getTag();
        }
        SpinnerLine item = categories.get(position);
        holder.categoryName.setText(item.getName());
        holder.icon.setImageResource(item.getIconId());
        return row;
    }

    static class CategoryHolder {
        ImageView icon;
        TextView categoryName;
    }

}
