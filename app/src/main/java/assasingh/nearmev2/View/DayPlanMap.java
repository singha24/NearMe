package assasingh.nearmev2.View;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import assasingh.nearmev2.Model.DayPlanModel;
import assasingh.nearmev2.Model.DirectionModel;
import assasingh.nearmev2.Model.GooglePlacesUtility;
import assasingh.nearmev2.R;
import assasingh.nearmev2.Services.LocationService;

public class DayPlanMap extends AppCompatActivity {

    private String polyLine;

    private ArrayList<DayPlanModel> dayPlan;
    private GoogleMap googleMap;
    private MapView mapView;
    Uri.Builder builder;

    TextView startAddress;
    TextView endAddress;
    TextView duration;
    TextView distance;

    String url;

    LatLng userPos;
    String desting;
    String sUserPos;
    String travelMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_plan_map);

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        startAddress = (TextView) findViewById(R.id.startAddress);
        endAddress = (TextView) findViewById(R.id.endAddress);
        duration = (TextView) findViewById(R.id.duration);
        distance = (TextView) findViewById(R.id.distance);

        travelMode = sharedPrefs.getString("travel_mode", "walking");

        dayPlan = DayPlan.dayPlan;

        userPos = new LatLng(LocationService.getLatitude(), LocationService.getLongitude());

        final LatLng destinationLatLng = dayPlan.get(dayPlan.size() - 1).getLatlng();

        desting = destinationLatLng.latitude + "," + destinationLatLng.longitude;
        sUserPos = "52.487144,-1.886977";

        mapView = (MapView) findViewById(R.id.dayPlanMap);
        mapView.onCreate(savedInstanceState);

        mapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;


                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                googleMap.setMyLocationEnabled(true);


                builder = new Uri.Builder();
                builder.scheme("https")
                        .authority("maps.googleapis.com")
                        .appendPath("maps")
                        .appendPath("api")
                        .appendPath("directions")
                        .appendPath("json")
                        .appendQueryParameter("origin", sUserPos)
                        .appendQueryParameter("destination", desting)
                        .appendQueryParameter("mode", travelMode);

                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userPos, 12));


                googleMap.addMarker(new MarkerOptions().position(getUserPos()).title("Start").snippet(""));
                googleMap.addMarker(new MarkerOptions().position(destinationLatLng).title("Finish").snippet(""));

                String via = "";

                for (DayPlanModel place : dayPlan) {
                    LatLng ll = place.getLatlng();

                    googleMap.addMarker(new MarkerOptions().position(ll).title(place.getTitle()).snippet(place.getDescription()));

                    via += "via:" + ll.latitude + "," + ll.longitude + "|";


                }

                builder.appendQueryParameter("waypoints", "optimize:true|" + via);
                builder.appendQueryParameter("key", getResources().getString(R.string.places_key));

                url = builder.build().toString();

                Log.d("DAYPLAN_URL", getUrl());

                DayPlanAsync async = new DayPlanAsync();
                async.execute(getUrl());


            }
        });


    }

    public LatLng getUserPos() {
        LatLng dummy = new LatLng(52.487144, -1.886977);
        if (LocationService.locationAvailable()) {
            return userPos;
        } else {
            Toast.makeText(this, "Hold on a mo, trying to figure out your location", Toast.LENGTH_LONG).show();
        }
        return dummy;
    }

    public String getUrl() {
        return url;
    }

    private class DayPlanAsync extends AsyncTask<String, Void, ArrayList<DirectionModel>> {


        @Override
        protected ArrayList<DirectionModel> doInBackground(String... params) {
            //call which will return list of google places that are near a lat and long

            GooglePlacesUtility get = new GooglePlacesUtility(getApplicationContext());


            ArrayList<DirectionModel> result = new ArrayList<DirectionModel>();

            try {
                result = (ArrayList<DirectionModel>) get.getPolyLine(params[0]);
                polyLine = result.get(0).getPolyLine();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(ArrayList<DirectionModel> result) {

            List<LatLng> decodedPath = null;

            startAddress.setText("Starting @: " + result.get(0).getStartAddress());
            endAddress.setText("Finishing @: " + result.get(0).getEndAddress());
            duration.setText("This route will take :" + result.get(0).getDuration());
            distance.setText("You'll cover: " + result.get(0).getDistance());

            decodedPath = PolyUtil.decode(result.get(0).getPolyLine());
            googleMap.addPolyline(new PolylineOptions().addAll(decodedPath));
            //googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userPos, 15));


        }
    }
}
