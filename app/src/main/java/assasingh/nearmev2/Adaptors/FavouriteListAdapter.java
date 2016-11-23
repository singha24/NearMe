package assasingh.nearmev2.Adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import assasingh.nearmev2.Object.FavouritePlaceObject;
import assasingh.nearmev2.R;

public class FavouriteListAdapter extends BaseAdapter {
    private ArrayList<FavouritePlaceObject> listData;
    private LayoutInflater layoutInflater;

    public FavouriteListAdapter(Context aContext, ArrayList<FavouritePlaceObject> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.favourite_places_row, null);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.title);
            //holder.image = (ImageView) convertView.findViewById(R.id.favImage);
            holder.date = (TextView) convertView.findViewById(R.id.date);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.title.setText(listData.get(position).getTitle());
        holder.date.setText(listData.get(position).getDate());
        return convertView;
    }

    static class ViewHolder {
        TextView title;
        TextView image;
        TextView date;
    }
}
