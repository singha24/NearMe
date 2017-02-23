package assasingh.nearmev2.Fragments;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.net.URLEncoder;

import assasingh.nearmev2.Model.GooglePlaceList;
import assasingh.nearmev2.Model.GooglePlacesUtility;
import assasingh.nearmev2.R;
import assasingh.nearmev2.Services.LocationService;
import assasingh.nearmev2.View.NearMe;

public class FragmentNearMeListView extends Fragment {


    private GooglePlaceList nearby;
    private ListView list;

    public FragmentNearMeListView() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_near_me_list_view, container, false);

        list = (ListView) v.findViewById(R.id.the_list);
        nearby = null;


        //Toast.makeText(getActivity(), "activity: " + ((NearMe)getActivity()).getActivity() + " radius: " + ((NearMe)getActivity()).getRadius() + " lat: " + ((NearMe)getActivity()).getLatitude() + " lon: " + ((NearMe)getActivity()).getLongitude(), Toast.LENGTH_LONG).show();

        Log.e("PLACES_EXAMPLE", getString(R.string.places_key));

        String type = URLEncoder.encode(((NearMe)getActivity()).getActivity());
        String placesRequest = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" +
                ((NearMe)getActivity()).getLatitude() + "," + ((NearMe)getActivity()).getLongitude() + "&radius=" + ((NearMe)getActivity()).getRadius() + "&key=" + getString(R.string.places_key);
        PlacesReadFeed process = new PlacesReadFeed();
        try {
            process.execute(new String[]{placesRequest});
        } catch (Exception e) {
            Log.e("NearMe Async Task Error", e.toString());
        }

        return v;


    }

    protected void reportBack(GooglePlaceList nearby) {

        if (this.nearby == null) {
            this.nearby = nearby;

        } else {
            this.nearby.getResults().addAll(nearby.getResults());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, this.nearby.getPlaceNames());
        list.setAdapter(adapter);
    }


    private class PlacesReadFeed extends AsyncTask<String, Void, GooglePlaceList> {
        private final ProgressDialog dialog = new ProgressDialog(getActivity());

        @Override
        protected GooglePlaceList doInBackground(String... urls) {
            try {
                String referer = null;
                //dialog.setMessage("Fetching Places Data");
                if (urls.length == 1) {
                    referer = null;
                } else {
                    referer = urls[1];
                }
                String input = GooglePlacesUtility.readGooglePlaces(urls[0], referer);
                Gson gson = new Gson();
                GooglePlaceList places = gson.fromJson(input, GooglePlaceList.class);
                Log.i("PLACES_EXAMPLE", "Number of places found is " + places.getResults().size());
                return places;
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("PLACES_EXAMPLE", "PLACES RESULT ERROR");
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            try {
                this.dialog.setMessage("Getting nearby places...");

                this.dialog.show();
            } catch (Exception e) {
                Log.e("Dialog error", "unable to display dialog box on getting places");
            }
        }

        @Override
        protected void onPostExecute(GooglePlaceList places) {
            try {
                this.dialog.dismiss();
                reportBack(places);
            } catch (Exception e) {
                Toast.makeText(getActivity(), "Well, err. This is embarrassing.", Toast.LENGTH_SHORT).show();
                Log.e("REPORT BACK ERROR", "OnPostExecute");
            }
        }
    }
}

