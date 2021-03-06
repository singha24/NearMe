package assasingh.nearmev2.View;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import assasingh.nearmev2.R;
import assasingh.nearmev2.Services.DatabaseHelper;

public class NearMeCard extends AppCompatActivity {

    private ImageView placeImage;
    private Button love;
    Button beenHereBefore;

    TextView openNow;
    TextView description;
    TextView types;
    ImageView call;
    ImageView share;
    ImageView website;
    TextView name;
    Button addToDayPlan;

    String sOpenNow;
    String sDescription;
    String sTypes;
    String sCall;
    String sShare;
    String sWebsite;
    String sName;
    Double sRating;
    String photoRef;
    Double lat;
    Double lng;
    String address;
    boolean clicked;

    ProgressBar progressBar;

    Cursor cursor;

    int id;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_me_card);

        id = (int) getIntent().getLongExtra("id", 0);

        db = new DatabaseHelper(this);


        openNow = (TextView) findViewById(R.id.open_now);
        description = (TextView) findViewById(R.id.description);
        types = (TextView) findViewById(R.id.types);
        call = (ImageView) findViewById(R.id.callPlace);
        share = (ImageView) findViewById(R.id.sharePlace);
        website = (ImageView) findViewById(R.id.website);
        name = (TextView) findViewById(R.id.name);
        love = (Button) findViewById(R.id.heartLove);
        placeImage = (ImageView) findViewById(R.id.near_me_card_image);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        beenHereBefore = (Button) findViewById(R.id.done);
        addToDayPlan = (Button) findViewById(R.id.dayPlan);

        addToDayPlan.setBackgroundColor(Color.parseColor("#b2bcbc"));

        sOpenNow = NearMe.places.get(id).getOpenNow();
        sDescription = NearMe.places.get(id).getWeekdayText();
        sRating = NearMe.places.get(id).getRating();
        sName = NearMe.places.get(id).getName();
        sTypes = NearMe.places.get(id).getTypes();
        photoRef = NearMe.places.get(id).getPhotoRef();
        lat = NearMe.places.get(id).getLatitude();
        lng = NearMe.places.get(id).getLongitude();
        address = NearMe.places.get(id).getAddress();
        sCall = NearMe.places.get(id).getPhone();
        sWebsite = NearMe.places.get(id).getWebsite();


        boolean s = Boolean.valueOf(sOpenNow);

        if (s)
            openNow.setText("Open");
        else
            openNow.setText(sDescription);

        description.setText(sRating + " stars \n\n" + address);

        name.setText(sName);
        String cursorString = sTypes;
        String outputText = "";
        String[] cursorSplitString = cursorString.split("\\+");
        for (int i = 0; i < cursorSplitString.length; i++) {
            outputText += cursorSplitString[i] + "\n";
        }

        outputText = outputText.replace("point_of_interest", "POI");
        outputText = outputText.trim();


        types.setText("\n" + outputText);


        GetImageFromUrl get = new GetImageFromUrl();

        String url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + photoRef + "&key=" + getResources().getString(R.string.places_key);

        get.execute(url);

        placeImage.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        love.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*
                Error codes for insertion
                0 = no error
                1 = error with insertion
                2 = duplicate value
                 */
                int success = db.insertPlaceToFavs(lat, lng, sName, photoRef, sRating,
                        sOpenNow, sDescription, sTypes, getDate(), 0); //0 for place visited

                if (success == 0) {
                    Snackbar.make(view, sName + " has been added to your loved places", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    love.setBackgroundResource(R.drawable.heartred);
                } else {
                    Snackbar.make(view, "Unable to love this place :(", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        beenHereBefore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int result = db.updatePlaceVisited(sName, 1);
                if (result == 0) {
                    beenHereBefore.setBackgroundResource(R.drawable.tick);
                    Snackbar.make(view, sName + " has been added to your visited places", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    clicked = true;
                } else {
                    Snackbar.make(view, "Unable to set this place as visited :(", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

            }
        });

        addToDayPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int success = db.insertIntoDayPlan(lat, lng, sName, photoRef, sDescription);
                if (success == 0) {
                    Snackbar.make(view, sName + " has been added to Day Plan", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    Snackbar.make(view, "Unable to do that boss :(", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share(lat, lng, sName);
            }
        });


        website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(sWebsite));
                startActivity(i);
            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!sCall.equals("000000")) {
                    call(sCall);
                } else {
                    Snackbar.make(view, "They don't seem to have a phone number I'm afraid.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });


    }

    void call(String number) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + number));

        startActivity(callIntent);
    }

    public void share(double latitude, double longitude, String name) {

        String uri = "http://maps.google.com/maps?saddr=" + latitude + "," + longitude;

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String ShareSub = "Recommendation";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, ShareSub);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, uri + "\n\n" + "It's a cool place called " + name + ", thought you might want to check it out!");
        startActivity(Intent.createChooser(sharingIntent, "Share via"));

    }


    public static String getDate() {
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDateTime = dateFormat.format(new Date()); // Find todays date

            return currentDateTime;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    public void vibrate() {
        Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        v.vibrate(50);
    }

    private class GetImageFromUrl extends AsyncTask<String, Void, Drawable> {

        @Override
        protected Drawable doInBackground(String... params) {

            try {
                InputStream is = (InputStream) new URL(params[0]).getContent();
                Drawable d = Drawable.createFromStream(is, "src name");
                return d;
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Drawable d) {

            if (d != null) {
                placeImage.setImageDrawable(d);
            } else {
                placeImage.setImageResource(R.drawable.no_image);
            }
            placeImage.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);


        }


    }


}
