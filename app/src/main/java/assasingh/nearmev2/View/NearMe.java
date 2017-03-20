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
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
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
import assasingh.nearmev2.Services.NetworkChecker;

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

    MyPagerAdapter adapterViewPager;
    String query;

    ProgressDialog progress;
    volatile String progressText;

    static List<SimpleGooglePlace> places;


    public static DatabaseHelper db;

    public static ClusterManager<SimpleGooglePlace> mClusterManager;

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
        if (b.getString("query") != null) {
            query = b.getString("query");
            query = query.replace("something", "");
            query.trim();
            query = query.replaceAll("\\s+", "+");

        }
        radius = 10;
        activity = "fun";

        LatLng ll = new LatLng(latitude, longitude);

        String type = URLEncoder.encode(getActivity());


        if (query != "") {
            placesRequest =
                    "https://maps.googleapis.com/maps/api/place/textsearch/json?query=" + query + "&location=" + latitude + "," + longitude + "&radius=" + radius + "&key=" +
                            getResources().getString(R.string.places_key);
        }else{
            placesRequest = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" +
                    latitude + "," + longitude + "&radius=" + radius + "&key=" + getResources().getString(R.string.places_key);
        }

        Log.d("URL_REQ", placesRequest);

        setTitle(activity + " : " + convertRadiusToMiles(radius) + latitude + ": " + longitude);

        /*SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (!prefs.getBoolean("firstTime", false)) {
            // <---- run your one time code here

            // mark first time has runned.
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", true);
            editor.commit();
        }*/


        NearMeAsync nearMeAsync = new NearMeAsync();
        nearMeAsync.execute(); //new thread

        progressText = "Entering the void";

        progress = ProgressDialog.show(this, "Hold on tight!",
                progressText, true);

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
            switch (position) {
                case 0: //List results
                    //myDrawable = getResources().getDrawable(R.drawable.ic_place_white_24dp);
                    return "Places on a list!";
                case 1: //map results
                    //myDrawable = getResources().getDrawable(R.drawable.img_section2);
                    return "Places on a map!";
                default:
                    break;
            }

            return "Something went wrong :S";
        }
    }

    private class NearMeAsync extends AsyncTask<Double, Integer, List<SimpleGooglePlace>> {


        @Override
        protected List<SimpleGooglePlace> doInBackground(Double... params) {
            //call which will return list of google places that are near a lat and long

            GooglePlacesUtility util = new GooglePlacesUtility(getApplicationContext());
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
            progress.setMessage("Creating the cluster manager");

            places = googlePlaces;

            progress.setMessage("places = googlePlaces");

            int counter = 0;

            progress.setMessage("int counter = 0;");

            for (SimpleGooglePlace place : googlePlaces) {

                LatLng pos = new LatLng(place.getLatitude(), place.getLongitude());
                String rating = String.valueOf(place.getRating());

                counter++;
                if (bounds == null) {
                    bounds = new LatLngBounds(pos, pos);
                } else {
                    bounds = bounds.including(pos);
                }

                //googleMap.addMarker(new MarkerOptions().position(pos).title(place.getName()).snippet(rating));

                SimpleGooglePlace offsetItem = new SimpleGooglePlace(place.getLatitude(), place.getLongitude());
                mClusterManager.addItem(offsetItem);
                progress.setMessage("There are " + counter + " places around you!");

            }

            if (googleMap != null) {
                googleMap.setOnCameraIdleListener(mClusterManager);
                googleMap.setOnMarkerClickListener(mClusterManager);
                googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150), null);
            }

            progress.setMessage("nearly there, phew");

            adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());

            viewPager = (ViewPager) findViewById(R.id.viewpager);
            viewPager.setAdapter(adapterViewPager);

            tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(viewPager);

            adapterViewPager.notifyDataSetChanged();

            progress.setMessage("All done (☞ﾟ∀ﾟ)☞");

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


        return super.onOptionsItemSelected(item);
    }

}
