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
import java.util.ArrayList;
import java.util.List;

import assasingh.nearmev2.Model.GooglePlacesUtility;
import assasingh.nearmev2.Model.SimpleGooglePlace;
import assasingh.nearmev2.R;

/**
 * Created by Assa on 08/03/2017.
 */

public class NearMeListResultAdaptor extends CursorAdapter {

    ImageView iv;


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
        TextView name = (TextView) view.findViewById(R.id.name);
        TextView openNow = (TextView) view.findViewById(R.id.openNow);
        TextView rating = (TextView) view.findViewById(R.id.rating);

        String n = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        Boolean o = Boolean.valueOf(cursor.getString(cursor.getColumnIndexOrThrow("open_now")));
        String r = cursor.getString(cursor.getColumnIndexOrThrow("rating"));
        String weekDayText = cursor.getString(cursor.getColumnIndexOrThrow("weekday_text"));

        name.setText(n);
        if (o)
            openNow.setText("They're open!");
        else
            openNow.setText("Open again tomorrow @ " + weekDayText);
        rating.setText(r + " stars");


    }


    private class GetImageFromUrl extends AsyncTask<String, Void, List<SimpleGooglePlace>> {

        @Override
        protected List<SimpleGooglePlace> doInBackground(String... params) {

            return null;
        }

        @Override
        protected void onPostExecute(List<SimpleGooglePlace> places) {

        }


    }

}
