package assasingh.nearmev2.View;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import assasingh.nearmev2.Fragments.FragmentNearMeListView;
import assasingh.nearmev2.Fragments.FragmentNearMeMapView;
import assasingh.nearmev2.Model.GooglePlacesUtility;
import assasingh.nearmev2.Model.SimpleGooglePlace;
import assasingh.nearmev2.R;
import assasingh.nearmev2.Services.DatabaseHelper;

public class NearMe extends AppCompatActivity {


    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int radius;
    private String activity;
    private double latitude;
    private double longitude;
    public static String placesRequest;

    public static GoogleMap googleMap;
    public static ArrayList<LatLng> markers;

    MyPagerAdapter adapterViewPager;

    ProgressDialog progress;

    static List<SimpleGooglePlace> places;


    public static DatabaseHelper db;
    private ClusterManager<SimpleGooglePlace> mClusterManager;
    LatLngBounds.Builder boundsBuilder;

    public static LatLngBounds bounds;

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
        radius = 100;
        activity = "fun";

        LatLng ll = new LatLng(latitude, longitude);

        String type = URLEncoder.encode(getActivity());
        placesRequest = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" +
                latitude + "," + longitude + "&radius=" + radius + "&key=" + getResources().getString(R.string.places_key);

        Log.d("URL_REQ", placesRequest);

        setTitle(activity + " : " + convertRadiusToMiles(radius));

        /*SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (!prefs.getBoolean("firstTime", false)) {
            // <---- run your one time code here

            // mark first time has runned.
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", true);
            editor.commit();
        }*/

        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(adapterViewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


        NearMeAsync nearMeAsync = new NearMeAsync();
        nearMeAsync.execute(); //new thread

        progress = ProgressDialog.show(this, "Hold on tight!",
                "Doing some very technical stuff in the background right now..", true);

    }

    public static String getPlacesRequestURL() {
        return placesRequest;
    }

    public double convertRadiusToMiles(double r) {
        return Math.ceil(r * 0.00062137);
    }

    public String getActivity() {
        return activity;
    }

    public int getRadius() {
        return radius;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public static List<SimpleGooglePlace> getPlaces() {
        return places;
    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {

        private static int NUM_ITEMS = 2;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return FragmentNearMeListView.newInstance(0, "List");
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return FragmentNearMeMapView.newInstance(1, "Map");

                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }


    }

    private class NearMeAsync extends AsyncTask<Double, Integer, List<SimpleGooglePlace>> {


        @Override
        protected List<SimpleGooglePlace> doInBackground(Double... params) {
            //call which will return list of google places that are near a lat and long

            GooglePlacesUtility util = new GooglePlacesUtility();
            places = new ArrayList<SimpleGooglePlace>();
            try {
                places = util.networkCall(getPlacesRequestURL());
            } catch (Exception e) {
                e.printStackTrace();
            }

            return places;
        }

        @Override
        protected void onPostExecute(List<SimpleGooglePlace> googlePlaces) {

            mClusterManager = new ClusterManager<SimpleGooglePlace>(getApplication(), googleMap);
            boundsBuilder = new LatLngBounds.Builder();

            for (SimpleGooglePlace place : googlePlaces) {

                LatLng pos = new LatLng(place.getLatitude(), place.getLongitude());
                String rating = String.valueOf(place.getRating());

                boundsBuilder.include(pos);



                markers.add(pos);
                googleMap.addMarker(new MarkerOptions().position(pos).title(place.getName()).snippet(rating));

                SimpleGooglePlace offsetItem = new SimpleGooglePlace(place.getLatitude(), place.getLongitude());
                mClusterManager.addItem(offsetItem);

                //boolean success = db.insertPlaceToFavs(place.getLatitude(),place.getLongitude(),place.getName(),place.getPhotoRef(),place.getRating(), place.getOpenNow(), place.getWeekdayText(), place.getTypes());
                //Log.d("INSER", String.valueOf(success));

            }

            bounds = boundsBuilder.build();

            googleMap.setOnCameraIdleListener(mClusterManager);
            googleMap.setOnMarkerClickListener(mClusterManager);
            googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(NearMe.bounds,150), null);

            progress.dismiss();

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nearme_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            //refresh();
        }

        return super.onOptionsItemSelected(item);
    }

    void refresh() {
        try {
            db.refreshPlacesTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
