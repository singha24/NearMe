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

import assasingh.nearmev2.Model.FavouritePlace;
import assasingh.nearmev2.R;
import assasingh.nearmev2.View.FavouritePlaces;

public class FavouriteListAdapter extends BaseAdapter {
    private ArrayList<FavouritePlace> listData;
    private LayoutInflater layoutInflater;

    public FavouriteListAdapter(Context aContext, ArrayList<FavouritePlace> listData) {
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
            holder = new ViewHolder(convertView, listData.get(position).getPhotoRef());
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.image = (ImageView) convertView.findViewById(R.id.favPlaceImage);
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
        ImageView image;
        TextView date;
        View view;
        String photoRef;

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
