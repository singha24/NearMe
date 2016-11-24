package assasingh.nearmev2.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import assasingh.nearmev2.Adaptors.FavouriteListAdapter;
import assasingh.nearmev2.Fragments.FavouriteAlertFragment;
import assasingh.nearmev2.Object.FavouritePlaceObject;
import assasingh.nearmev2.R;

public class FavouritePlaces extends AppCompatActivity {




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fav_content);



        ArrayList image_details = getListData();
        final ListView lv1 = (ListView) findViewById(R.id.favListView);
        lv1.setAdapter(new FavouriteListAdapter(this, image_details));

        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String picked = "you selected " + String.valueOf(parent.getItemAtPosition(position));

                final android.app.FragmentManager fm = getFragmentManager();
                final  FavouriteAlertFragment favFrag = new FavouriteAlertFragment();

                favFrag.show(fm,"Alert");

                Toast.makeText(FavouritePlaces.this, picked, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private ArrayList getListData() {
        ArrayList<FavouritePlaceObject> results = new ArrayList<FavouritePlaceObject>();
        FavouritePlaceObject favObj = new FavouritePlaceObject();

        favObj.setTitle("Boston Tea Party");
        favObj.setTime("May 26, 2016, 13:35");
        results.add(favObj);

        FavouritePlaceObject favObj1 = new FavouritePlaceObject();

        favObj1.setTitle("Mr Singhs Pizza");
        favObj1.setTime("May 15, 2016, 10:35");
        results.add(favObj1);

        FavouritePlaceObject favObj2 = new FavouritePlaceObject();

        favObj2.setTitle("Rock Climbing");
        favObj2.setTime("May 20, 2016, 10:10");
        results.add(favObj2);

        // Add some more dummy data for testing
        return results;
    }
}
