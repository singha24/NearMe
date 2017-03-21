package assasingh.nearmev2.View;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import assasingh.nearmev2.Adaptors.DayPlanListAdapter;
import assasingh.nearmev2.Adaptors.FavouriteListAdapter;
import assasingh.nearmev2.Fragments.FavouriteAlertFragment;
import assasingh.nearmev2.Model.FavouritePlace;
import assasingh.nearmev2.R;
import assasingh.nearmev2.Services.DatabaseHelper;

public class DayPlan extends AppCompatActivity {

    assasingh.nearmev2.Model.DayPlan dayPlan;
    String name;
    String photoRef;
    long id;
    double lat;
    double lng;
    String description;

    public static DayPlanListAdapter adap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_plan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        android.support.v7.app.ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#cc3333")));

        DatabaseHelper db = new DatabaseHelper(this);


        ArrayList<assasingh.nearmev2.Model.DayPlan> dayPlan = new ArrayList<assasingh.nearmev2.Model.DayPlan>();

        Cursor c = db.getAllFromDayPlanTable();


        while (c.moveToNext()) {
            name = c.getString(c.getColumnIndexOrThrow("name"));
            photoRef = c.getString(c.getColumnIndexOrThrow("photo_ref"));
            description = c.getString(c.getColumnIndexOrThrow("description"));
            id = c.getLong(c.getColumnIndexOrThrow("_id"));

            lat = c.getDouble(c.getColumnIndexOrThrow("lat"));
            lng = c.getDouble(c.getColumnIndexOrThrow("lng"));


            this.dayPlan = new assasingh.nearmev2.Model.DayPlan();

            this.dayPlan.setTitle(name);
            this.dayPlan.setId(id);
            this.dayPlan.setLatlng(lat, lng);
            this.dayPlan.setPhotoRef(photoRef);


            dayPlan.add(this.dayPlan);


        }

        c.close();
        db.close();

        final ListView lv1 = (ListView) findViewById(R.id.dayPlan);

        adap = new DayPlanListAdapter(this, dayPlan);

        lv1.setAdapter(adap);

        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                assasingh.nearmev2.Model.DayPlan picked = (assasingh.nearmev2.Model.DayPlan) parent.getItemAtPosition(position);

                final android.app.FragmentManager fm = getFragmentManager();
                final FavouriteAlertFragment favFrag = new FavouriteAlertFragment();

                Bundle bundle = new Bundle();
                bundle.putLong("id", picked.getId());
                bundle.putString("name", picked.getTitle());
                bundle.putParcelable("latlng", picked.getLatlng());
                bundle.putInt("posInList", position);
                bundle.putString("photoRef", picked.getPhotoRef());
                bundle.putString("description", picked.getDescription());
                bundle.putString("dayPlan", "dayPlan");
                favFrag.setArguments(bundle);

                favFrag.show(fm,"Alert");


            }
        });

    }

}
