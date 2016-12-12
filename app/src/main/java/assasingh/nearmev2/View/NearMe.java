package assasingh.nearmev2.View;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.net.URLEncoder;

import assasingh.nearmev2.Model.GooglePlaceList;
import assasingh.nearmev2.Model.GooglePlacesUtility;
import assasingh.nearmev2.R;

import static android.R.attr.radius;

public class NearMe extends AppCompatActivity {

    private GooglePlaceList nearby;
    private ListView list;
    private String placesKey;
    private double latitude = 52.487144;
    private double longitude = -1.886977;
    private int radius;
    private String activity = "fun";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_me);

        list = (ListView) findViewById(R.id.the_list);
        nearby = null;
        radius = 100; //default



        Toast.makeText(getBaseContext(), "activity: " + activity + " radius: " + radius + " lat: " + latitude + " lon: " + longitude, Toast.LENGTH_LONG).show();

        placesKey = this.getResources().getString(R.string.places_key);

        String type = URLEncoder.encode(activity);
        String placesRequest = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" +
                latitude + "," + longitude + "&radius=" + radius + "&key=" + placesKey;
        PlacesReadFeed process = new PlacesReadFeed();
        try {
            process.execute(new String[]{placesRequest});
        }catch(Exception e){
            Log.e("NearMe Async Task execute Error", e.toString());
        }
    }

    protected void reportBack(GooglePlaceList nearby) {

        if (this.nearby == null) {
            this.nearby = nearby;

        } else {
            this.nearby.getResults().addAll(nearby.getResults());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, this.nearby.getPlaceNames());
        list.setAdapter(adapter);
    }

    private class PlacesReadFeed extends AsyncTask<String, Void, GooglePlaceList> {
        private final ProgressDialog dialog = new ProgressDialog(NearMe.this);

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
            }catch(Exception e){
                Log.e("Dialog error", "unable to display dialog box on getting places");
            }
        }

        @Override
        protected void onPostExecute(GooglePlaceList places) {
            try{
            this.dialog.dismiss();
            reportBack(places);
        }catch (Exception e){
                Toast.makeText(NearMe.this, "Well, err. This is embarrassing.", Toast.LENGTH_SHORT).show();
                Log.e("REPORT BACK ERROR", "OnPostExecute");
            }
        }


    }
}
