package assasingh.nearmev2.Adaptors;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import assasingh.nearmev2.Model.SimpleGooglePlace;
import assasingh.nearmev2.R;
import assasingh.nearmev2.View.NearMe;

/**
 * Created by Assa on 08/03/2017.
 */

public class NearMeListResultAdapter extends ArrayAdapter<SimpleGooglePlace> {

    ImageView iv;
    double lat;
    double lng;
    TextView name;
    TextView distanceFromUser;
    TextView rating;
    int distance;
    boolean open;
    String n;
    String photoRef;

    ProgressBar progressBar;

    private boolean mNotifyOnChange = true;

    Context context;
    ArrayList<SimpleGooglePlace> places;
    private LayoutInflater mInflater;


    public NearMeListResultAdapter(Context context, ArrayList<SimpleGooglePlace> places) {
        super(context, 0, places);
        this.context = context;
        this.places = places;
        this.mInflater = LayoutInflater.from(context);
        notifyDataSetChanged();

    }


    @Override
    public View getView(int position, View view, ViewGroup parent) {

        int type = getItemViewType(position);

        if (view == null) {
            view = mInflater.inflate(R.layout.nearme_list_row, parent, false);

        }

        SimpleGooglePlace place = getItem(position);

        view = LayoutInflater.from(getContext()).inflate(R.layout.nearme_list_row, parent, false);


        iv = (ImageView) view.findViewById(R.id.placeImage);
        name = (TextView) view.findViewById(R.id.name);
        distanceFromUser = (TextView) view.findViewById(R.id.distance);
        rating = (TextView) view.findViewById(R.id.rating);

        progressBar = (ProgressBar) view.findViewById(R.id.listImageProgress);



        name.setText(place.getName());


        String o = place.getOpenNow();

        photoRef = place.getPhotoRef();

        lat = place.getLatitude();
        lng = place.getLongitude();

        double userLat = ((NearMe) getContext()).getLatitude();
        double userLng = ((NearMe) getContext()).getLongitude();

        distance = (int) distance(lat, lng, userLat, userLng, 'M');

        open = Boolean.valueOf(o);

        if (!open) {
            //distanceFromUser.setVisibility(View.GONE);
        }
        distanceFromUser.setText(String.valueOf(distance) + " meters away!");

        rating.setText(place.getRating() + " stars");

        iv.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        GetImageFromUrl getImageFromUrl = new GetImageFromUrl();

        String url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + photoRef + "&key=" + getContext().getResources().getString(R.string.places_key);

        notifyDataSetChanged();

        getImageFromUrl.execute(url);
        return view;

    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        mNotifyOnChange = true;
    }

    public double getLatLocation() {
        return lat;
    }

    public double getLngLocation() {
        return lng;
    }

    private double distance(double lat1, double lon1, double lat2, double lon2, char unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == 'K') {
            dist = dist * 1.609344;
        } else if (unit == 'N') {
            dist = dist * 0.8684;
        } else if (unit == 'M') {
            dist = dist * 1609.34; //miles to meters
        }
        return (dist);
    }

    @Override
    public int getCount() {
        return places.size();
    }

    @Override
    public SimpleGooglePlace getItem(int position) {
        return places.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public int getPosition(SimpleGooglePlace item) {
        return places.indexOf(item);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts decimal degrees to radians             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts radians to decimal degrees             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
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

            iv.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);

            if(d != null) {
                iv.setImageDrawable(d);
            }

        }


    }

}
