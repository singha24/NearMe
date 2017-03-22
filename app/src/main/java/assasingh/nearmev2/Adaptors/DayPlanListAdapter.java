package assasingh.nearmev2.Adaptors;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import assasingh.nearmev2.Model.DayPlanModel;
import assasingh.nearmev2.R;

public class DayPlanListAdapter extends BaseAdapter {
    private ArrayList<DayPlanModel> listData;
    private LayoutInflater layoutInflater;

    public DayPlanListAdapter(Context aContext, ArrayList<DayPlanModel> listData) {
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
            holder = new ViewHolder(convertView, listData.get(position).getPhotoRef());

            holder.image = (ImageView) convertView.findViewById(R.id.dayPlanImage);
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
        View view;
        String photoRef;
        ImageView image;

        public ViewHolder(View view, String photoRef) {
            this.view = view;
            this.photoRef = photoRef;

            GetImageFromUrl get = new GetImageFromUrl();
            String url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + this.photoRef + "&key=AIzaSyB3Qirj2H1pL_63c7yXcMIMCjcQUinyHS4";

            get.execute(url);
        }


        private class GetImageFromUrl extends AsyncTask<String, Void, Drawable> {

            @Override
            protected Drawable doInBackground(String... params) {

                try {
                    InputStream is = (InputStream) new URL(params[0]).getContent();
                    Drawable d = Drawable.createFromStream(is, "src name");
                    return d;
                } catch (Exception e) {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Drawable d) {

                if (d != null) {
                    image.setImageDrawable(d);
                } else {
                    image.setImageResource(R.drawable.no_image);
                }


            }


        }

    }
}
