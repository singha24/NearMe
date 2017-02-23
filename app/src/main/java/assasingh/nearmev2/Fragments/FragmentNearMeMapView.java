package assasingh.nearmev2.Fragments;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.StringBuilderPrinter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import assasingh.nearmev2.Model.GooglePlace;
import assasingh.nearmev2.Model.GooglePlaceList;
import assasingh.nearmev2.Model.GooglePlacesUtility;
import assasingh.nearmev2.Model.SimpleGooglePlace;
import assasingh.nearmev2.R;
import assasingh.nearmev2.View.NearMe;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentNearMeMapView extends Fragment {

    private MapView mapView;
    private GoogleMap googleMap;
    private GooglePlaceList nearby;
    private final String TAG = "MAP_DEBUG";
    private String placesRequest;


    public FragmentNearMeMapView() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_near_me_map_view, container, false);

        mapView = (MapView) v.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);

        mapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        double lat = ((NearMe) getActivity()).getLatitude();
        double lng = ((NearMe) getActivity()).getLongitude();
        double radius = ((NearMe) getActivity()).getRadius();


        String type = URLEncoder.encode(((NearMe) getActivity()).getActivity());
        placesRequest = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" +
                lat + "," + lng + "&radius=" + radius + "&key=" + getString(R.string.places_key);

        NearMeAsync nearMeAsync = new NearMeAsync();

        nearMeAsync.execute(); //new thread

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    return;
                }
                googleMap.setMyLocationEnabled(true);


                // For dropping a marker at a point on the Map
                LatLng userPos = new LatLng(((NearMe) getActivity()).getLatitude(), ((NearMe) getActivity()).getLongitude());

                //googleMap.addMarker(new MarkerOptions().position(userPos).title("").snippet("Marker Description"));

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(userPos).zoom(10).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });

        return v;


    }

    public String getPlacesRequestURL() {
        return placesRequest;
    }

    private class NearMeAsync extends AsyncTask<Double, Integer, List<SimpleGooglePlace>> {


        @Override
        protected List<SimpleGooglePlace> doInBackground(Double... params) {
            //call which will return list of google places that are near a lat and long

            List<SimpleGooglePlace> places = new ArrayList<SimpleGooglePlace>();
            try {
                places = networkCall();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return places;
        }

        @Override
        protected void onPostExecute(List<SimpleGooglePlace> googlePlaces) {
            for (SimpleGooglePlace place : googlePlaces) {
                LatLng pos = new LatLng(place.getLatitude(), place.getLongitude());


                String rating = String.valueOf(place.getRating());
                googleMap.addMarker(new MarkerOptions().position(pos).title(place.getName()).snippet(rating));

            }

        }
    }

    public List<SimpleGooglePlace> networkCall() throws Exception {
        ArrayList<SimpleGooglePlace> result = new ArrayList<SimpleGooglePlace>();

        String jsonData = urlRequest(getPlacesRequestURL());

        JSONObject jsonObject = new JSONObject(jsonData);
        JSONArray jsonArray = jsonObject.getJSONArray("results");


        for (int i = 0; i < jsonArray.length(); i++) {
            SimpleGooglePlace place = new SimpleGooglePlace();

            double lat = jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getDouble("lat");
            double lng = jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getDouble("lng");
            String name = jsonArray.getJSONObject(i).getString("name");
            String photoRef = jsonArray.getJSONObject(i).getString("reference");

            boolean openNow = false;
            String exceptionalDates = "";
            String weekdayText = "";
            if(jsonArray.getJSONObject(i).has("opening_hours")) {
                openNow = jsonArray.getJSONObject(i).getJSONObject("opening_hours").getBoolean("open_now");
                exceptionalDates = jsonArray.getJSONObject(i).getJSONObject("opening_hours").getJSONArray("exceptional_date").toString();
                weekdayText = jsonArray.getJSONObject(i).getJSONObject("opening_hours").getJSONArray("weekday_text").toString();
            }

            double rating = 0.0;

            if(jsonArray.getJSONObject(i).has("rating")){
                rating = jsonArray.getJSONObject(i).getDouble("rating");
            }

            place.setLatitude(lat);
            place.setLongitude(lng);
            place.setName(name);
            place.setPhotoRef(photoRef);
            place.setOpenNow(openNow);
            place.setRating(rating);
            place.setExceptionalDates(exceptionalDates);
            place.setWeekdayText(weekdayText);

            result.add(place);
        }


        return result;
    }

    private String urlRequest(String s) throws Exception {
        String request = getPlacesRequestURL();

        StringBuilder sb = new StringBuilder();

        URL url = new URL(s);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        try {

            InputStream in = new BufferedInputStream(conn.getInputStream());

            BufferedReader bin = new BufferedReader(new InputStreamReader(in));

            String inputLine;

            while ((inputLine = bin.readLine()) != null) {
                sb.append(inputLine);
            }

        } finally {
            conn.disconnect();
        }

        return sb.toString();
    }


    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {

    }

}
