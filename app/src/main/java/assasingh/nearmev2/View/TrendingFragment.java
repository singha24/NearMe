package assasingh.nearmev2.View;


import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import assasingh.nearmev2.Model.GooglePlacesUtility;
import assasingh.nearmev2.Model.SimpleGooglePlace;
import assasingh.nearmev2.R;
import assasingh.nearmev2.Services.LocationService;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrendingFragment extends Fragment {

    private ImageView image;
    private TextView count;
    private final String TAG = "TRENDING_FRAGMENT";

    private String URL = "";

    private LocationService ls;
    private Drawable drawable;
    private String name = "";


    public TrendingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_trending, container, false);

        image = (ImageView) view.findViewById(R.id.trendingImageFrag);
        count = (TextView) view.findViewById(R.id.trendingCount);

        Bundle bundle = getArguments();
        String message = Integer.toString(bundle.getInt("count"));

        GetImageFromUrl gi = new GetImageFromUrl();

        ls = new LocationService();

        image.setImageResource(R.drawable.astonhome);
        count.setText("Trending now!");

        //gi.execute(URL);


        return view;
    }


    public double getLat() {
        return ls.getLatitude();
    }

    public double getLng() {
        return ls.getLongitude();
    }

    public double getRadius() {
        return 500; //TODO
    }


    private class GetImageFromUrl extends AsyncTask<String, Void, List<SimpleGooglePlace>> {

        @Override
        protected List<SimpleGooglePlace> doInBackground(String... params) {

            Cursor ref = MainActivity.db.getImages();

            String r = "";

            r = ref.getString(ref.getColumnIndexOrThrow("photo_ref"));
            name = ref.getString(ref.getColumnIndexOrThrow("name"));


            Log.d("TRENDING", r);

            try {

                String photoRefUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=2000&photoreference=" + r + "&key=" + getResources().getString(R.string.places_key);
                //Log.d(TAG, photoRefUrl);
                InputStream is = (InputStream) new URL(photoRefUrl).getContent();
                Drawable d = Drawable.createFromStream(is, "src name");

                drawable = d;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<SimpleGooglePlace> places) {


            image.setImageDrawable(drawable);
            count.setText(name);

        }


    }
}
