package assasingh.nearmev2.View;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.maps.android.clustering.ClusterManager;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import assasingh.nearmev2.Fragments.FragmentNearMeListView;
import assasingh.nearmev2.Fragments.FragmentNearMeMapView;
import assasingh.nearmev2.Model.ClusterMarkerLocation;
import assasingh.nearmev2.Model.GooglePlaceList;
import assasingh.nearmev2.Model.GooglePlacesUtility;
import assasingh.nearmev2.Model.SimpleGooglePlace;
import assasingh.nearmev2.R;
import assasingh.nearmev2.Services.DatabaseHelper;

import static android.R.attr.radius;

public class NearMe extends AppCompatActivity {


    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int radius;
    private String activity;
    private double latitude;
    private double longitude;
    private String placesRequest;
    public static GoogleMap googleMap;
    public static ArrayList<LatLng> markers;
    ProgressDialog progress;


    public static ClusterManager<ClusterMarkerLocation> clusterManager;

    public static DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_me);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        db = new DatabaseHelper(this);

        Bundle b = getIntent().getExtras();
        latitude = b.getDouble("lat");
        longitude = b.getDouble("lon");
        radius = 1000;
        activity = "fun";

        markers = new ArrayList<>();

        LatLng ll = new LatLng(latitude,longitude);

        String type = URLEncoder.encode(getActivity());
        placesRequest = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" +
                latitude + "," + longitude + "&radius=" + radius + "&key=" + getResources().getString(R.string.places_key);

        Log.d("URL_REQ", placesRequest);

        setTitle(activity + " : " + convertRadiusToMiles(radius));


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (!prefs.getBoolean("firstTime", false)) {
            // <---- run your one time code here
            databaseSetup();

            // mark first time has runned.
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", true);
            editor.commit();
        }

        markers();

        clusterManager = new ClusterManager<ClusterMarkerLocation>(this, googleMap);


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

    }

    void databaseSetup(){

        NearMeAsync nearMeAsync = new NearMeAsync();
        nearMeAsync.execute(); //new thread

        progress = ProgressDialog.show(this, "dialog title",
                "dialog message", true);
    }

    void markers(){
        Markers nearMeAsync = new Markers();
        nearMeAsync.execute(); //new thread
    }

    public String getPlacesRequestURL() {
        return placesRequest;
    }

    public double convertRadiusToMiles(double r){
        return Math.ceil(r * 0.00062137);
    }

    public String getActivity(){return activity; }

    public int getRadius(){
        return radius;
    }

    public double getLongitude(){
        return longitude;
    }

    public double getLatitude(){
        return latitude;
    }

    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentNearMeMapView(), "Map");
        adapter.addFragment(new FragmentNearMeListView(), "List");

        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private class NearMeAsync extends AsyncTask<Double, Integer, List<SimpleGooglePlace>> {


        @Override
        protected List<SimpleGooglePlace> doInBackground(Double... params) {
            //call which will return list of google places that are near a lat and long

            GooglePlacesUtility util = new GooglePlacesUtility();
            List<SimpleGooglePlace> places = new ArrayList<SimpleGooglePlace>();
            try {
                places = util.networkCall(getPlacesRequestURL());
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

                markers.add(pos);
                googleMap.addMarker(new MarkerOptions().position(pos).title(place.getName()).snippet(rating));


                ClusterMarkerLocation markerLocation = new ClusterMarkerLocation(pos.latitude, pos.longitude);
                clusterManager.addItem(markerLocation);

                boolean success = db.insertPlace(place.getLatitude(),place.getLongitude(),place.getName(),place.getPhotoRef(),place.getRating(), place.getOpenNow(), place.getWeekdayText(), place.getTypes());


            }

            progress.dismiss();

        }
    }

    private class Markers extends AsyncTask<Double, Integer, List<SimpleGooglePlace>> {


        @Override
        protected List<SimpleGooglePlace> doInBackground(Double... params) {
            //call which will return list of google places that are near a lat and long

            GooglePlacesUtility util = new GooglePlacesUtility();
            List<SimpleGooglePlace> places = new ArrayList<SimpleGooglePlace>();
            try {
                places = util.networkCall(getPlacesRequestURL());
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

                markers.add(pos);
                googleMap.addMarker(new MarkerOptions().position(pos).title(place.getName()).snippet(rating));


                ClusterMarkerLocation markerLocation = new ClusterMarkerLocation(pos.latitude, pos.longitude);
                clusterManager.addItem(markerLocation);

            }
        }
    }
}
