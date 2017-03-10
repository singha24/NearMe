package assasingh.nearmev2.View;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.net.URL;

import assasingh.nearmev2.R;
import assasingh.nearmev2.Services.DatabaseHelper;

public class NearMeCard extends AppCompatActivity {

    private ImageView placeImage;
    private ImageView love;
    ImageView beenHereBefore;

    TextView openNow;
    TextView description;
    TextView types;
    ImageView call;
    ImageView share;
    ImageView website;
    TextView name;
    Button addToDayPlan;

    ProgressBar progressBar;

    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_me_card);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        long id = getIntent().getLongExtra("id", 0);

        DatabaseHelper db = new DatabaseHelper(this);

        cursor = db.getAllFromPlacesWhereID(id);


        openNow = (TextView) findViewById(R.id.open_now);
        description = (TextView) findViewById(R.id.description);
        types = (TextView) findViewById(R.id.types);
        call = (ImageView) findViewById(R.id.call);
        share = (ImageView) findViewById(R.id.sharePlace);
        website = (ImageView) findViewById(R.id.website);
        name = (TextView) findViewById(R.id.name);
        love = (ImageView) findViewById(R.id.heart);
        placeImage = (ImageView) findViewById(R.id.near_me_card_image);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        beenHereBefore = (ImageView) findViewById(R.id.done);
        addToDayPlan = (Button) findViewById(R.id.dayPlan);

        addToDayPlan.setBackgroundColor(Color.parseColor("#b2bcbc"));


        String photoRef = "";

        if (cursor.getCount() != 0) {

            while (cursor.moveToNext()) {

                boolean s = Boolean.valueOf(cursor.getString(cursor.getColumnIndexOrThrow("open_now")));

                if (s)
                    openNow.setText("Open");
                else
                    openNow.setText(":( look's like they're closed Jim");

                description.setText(cursor.getString(cursor.getColumnIndexOrThrow("rating")) + " stars (this is meant to be the description)");

                name.setText(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                String cursorString = cursor.getString(cursor.getColumnIndexOrThrow("type"));
                String outputText = "";
                String[] cursorSplitString = cursorString.split("\\+");
                for (int i = 0; i < cursorSplitString.length; i++) {
                    outputText += cursorSplitString[i] + "\n";
                }

                outputText = outputText.replace("point_of_interest", "POI");
                outputText = outputText.trim();

                photoRef = cursor.getString(cursor.getColumnIndexOrThrow("photo_ref"));

                types.setText("\n" + outputText);
            }

        } else {
            Toast.makeText(this, "nope", Toast.LENGTH_LONG).show();
        }

        GetImageFromUrl get = new GetImageFromUrl();

        String url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + photoRef + "&key=" + getResources().getString(R.string.places_key);

        get.execute(url);

        placeImage.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        love.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrate();
            }
        });



    }

    public void vibrate(){
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

            if(d != null) {
                placeImage.setImageDrawable(d);
            }else{
                placeImage.setImageResource(R.drawable.no_image);
            }
            placeImage.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);


        }


    }


}
