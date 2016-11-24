package assasingh.nearmev2.Adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import assasingh.nearmev2.Object.DayPlanObject;
import assasingh.nearmev2.Object.FavouritePlaceObject;
import assasingh.nearmev2.R;

public class DayPlanListAdapter extends BaseAdapter {
    private ArrayList<DayPlanObject> listData;
    private LayoutInflater layoutInflater;

    public DayPlanListAdapter(Context aContext, ArrayList<DayPlanObject> listData) {
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
            convertView = layoutInflater.inflate(R.layout.day_plan_row, null);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.dayPlanTitle);
            holder.time = (TextView) convertView.findViewById(R.id.dayPlanTime);
            holder.description = (TextView) convertView.findViewById(R.id.dayPlanText);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.title.setText(listData.get(position).getTitle());
        holder.description.setText(listData.get(position).getDescription());
        holder.time.setText(listData.get(position).getTime());
        return convertView;
    }

    static class ViewHolder {
        TextView time;
        TextView title;
        TextView description;
    }
}
