package assasingh.nearmev2.Adaptors;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import assasingh.nearmev2.Model.SimpleGooglePlace;
import assasingh.nearmev2.R;
import assasingh.nearmev2.View.NearMe;

/**
 * Created by Assa on 08/03/2017.
 */

public class NearMeListResultAdaptor extends CursorAdapter {

    ImageView iv;
    double lat;
    double lng;
    TextView name;
    TextView distanceFromUser;
    TextView rating;
    int distance;
    boolean open;
    String n;
    String r;
    String photoRef;


    public NearMeListResultAdaptor(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.nearme_list_row, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        iv = (ImageView) view.findViewById(R.id.placeImage);
        name = (TextView) view.findViewById(R.id.name);
        distanceFromUser = (TextView) view.findViewById(R.id.distance);
        rating = (TextView) view.findViewById(R.id.rating);

        while(cursor.moveToNext()) {

            n = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String o = cursor.getString(cursor.getColumnIndexOrThrow("open_now"));
            r = cursor.getString(cursor.getColumnIndexOrThrow("rating"));
            photoRef = cursor.getString(cursor.getColumnIndexOrThrow("photo_ref"));

            lat = Double.parseDouble(cursor.getString(cursor.getColumnIndexOrThrow("lat")));
            lng = Double.parseDouble(cursor.getString(cursor.getColumnIndexOrThrow("lng")));

            double userLat = ((NearMe) context).getLatitude();
            double userLng = ((NearMe) context).getLongitude();

            distance = (int) distance(lat, lng, userLat, userLng, 'M');

            open = Boolean.valueOf(o);

            name.setText(n);

            if (!open) {
                //distanceFromUser.setVisibility(View.GONE);
            }
            distanceFromUser.setText(String.valueOf(distance) + " meters away!");

            rating.setText(r + " stars");
        }


        GetImageFromUrl get = new GetImageFromUrl();

        String url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + photoRef + "&key=" + context.getResources().getString(R.string.places_key);

        //get.execute(url);


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

            iv.setImageDrawable(d);

        }


    }

}
