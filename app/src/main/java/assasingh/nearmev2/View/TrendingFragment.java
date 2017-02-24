package assasingh.nearmev2.View;


import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
    private ArrayList<Drawable> drawables;
    private ArrayList<String> names;


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

        //gi.execute(URL);



        return view;
    }

    public String getPlacesRequestURL() {
        String s = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" +
                getLat() + "," + getLng() + "&radius=" + getRadius() + "&key=" + getResources().getString(R.string.places_key);

        return s;
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

            GooglePlacesUtility util = new GooglePlacesUtility();
            List<SimpleGooglePlace> places = new ArrayList<SimpleGooglePlace>();

            drawables = new ArrayList<>();
            names = new ArrayList<>();

            try {
                places = util.networkCall(getPlacesRequestURL());

                for (SimpleGooglePlace p : places) {
                    String photoRefUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=2000&photoreference=" + p.getPhotoRef() + "&key=" + getResources().getString(R.string.places_key);
                    //Log.d(TAG, photoRefUrl);
                    InputStream is = (InputStream) new URL(photoRefUrl).getContent();
                    Drawable d = Drawable.createFromStream(is, "src name");

                    drawables.add(d);
                    names.add(p.getName());

                }
            }catch (Exception e) {
                e.printStackTrace();
            }


            return places;
        }

        @Override
        protected void onPostExecute(List<SimpleGooglePlace> places) {

            /*for(int i = 0; i<drawables.size(); i++){
                image.setImageDrawable(drawables.get(i));
            }

            for(int j = 0; j<names.size(); j++){
                count.setText(names.get(j));
            }*/
        }


    }
}
