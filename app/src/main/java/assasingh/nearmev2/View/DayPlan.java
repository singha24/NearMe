package assasingh.nearmev2.View;

import android.app.ActionBar;
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
import android.widget.Toast;

import java.util.ArrayList;

import assasingh.nearmev2.Adaptors.DayPlanListAdapter;
import assasingh.nearmev2.Fragments.FavouriteAlertFragment;
import assasingh.nearmev2.Object.DayPlanObject;
import assasingh.nearmev2.Object.FavouritePlaceObject;
import assasingh.nearmev2.R;

public class DayPlan extends AppCompatActivity {

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


        ArrayList image_details = getListData();
        final ListView lv1 = (ListView) findViewById(R.id.dayPlan);
        lv1.setAdapter(new DayPlanListAdapter(this, image_details));

        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String picked = "you selected " + String.valueOf(parent.getItemAtPosition(position));

                final android.app.FragmentManager fm = getFragmentManager();
                final FavouriteAlertFragment favFrag = new FavouriteAlertFragment();

                favFrag.show(fm,"Alert");
            }
        });

    }

    private ArrayList getListData() {
        ArrayList<DayPlanObject> results = new ArrayList<DayPlanObject>();
        DayPlanObject favObj = new DayPlanObject();

        favObj.setTitle("Start location");
        favObj.setDescription("Restaurant, great food, great atmosphere and great company");
        favObj.setTime("15:38");
        results.add(favObj);

        DayPlanObject favObj1 = new DayPlanObject();

        favObj1.setTitle("Boston Tea Party");
        favObj1.setDescription("Best blend of tea's and coffee's");
        favObj1.setTime("16:15");
        results.add(favObj1);

        DayPlanObject favObj2 = new DayPlanObject();

        favObj2.setTitle("Aston Woodcock Sports");
        favObj2.setDescription("Aston woodcock sports center offers");
        favObj2.setTime("16:57");
        results.add(favObj2);

        DayPlanObject obj3 = new DayPlanObject();

        obj3.setTitle("Home");
        obj3.setTime("18:02");
        results.add(obj3);

        // Add some more dummy data for testing
        return results;
    }

}
