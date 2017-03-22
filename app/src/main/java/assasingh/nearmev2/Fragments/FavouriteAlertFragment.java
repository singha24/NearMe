package assasingh.nearmev2.Fragments;


import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import assasingh.nearmev2.Adaptors.FavActionDialogAdapter;
import assasingh.nearmev2.R;
import assasingh.nearmev2.Services.DatabaseHelper;
import assasingh.nearmev2.View.PostCard;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavouriteAlertFragment extends DialogFragment {

    private static ListView lv;
    private static Integer[] actionIcons = {R.drawable.postcard, R.drawable.share, R.drawable.visited, R.drawable.cross};
    private static String[] menu = {"Create Postcard", "Share this place", "Mark Visited", "Remove from list"};
    private static long placeID;
    private String name;
    private LatLng pos;
    private int posInList;
    private String photoRef;
    public String rating;
    public String favtable;
    public String dayPlanTable;


    public FavouriteAlertFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_list_view, container, false);

        try {

            placeID = getArguments().getLong("id");
            name = getArguments().getString("name");
            pos = getArguments().getParcelable("latlng");
            posInList = getArguments().getInt("posInList");
            photoRef = getArguments().getString("photoRef");
            rating = getArguments().getString("rating");
            favtable = getArguments().getString("favourite");
            dayPlanTable = getArguments().getString("dayPlan");

        } catch (NullPointerException e) {
            Log.e("NullPointer", e.toString());
        }

        lv = (ListView) rootView.findViewById(R.id.dialogListView);

        getDialog().setTitle("What would you like to do?");

        final FavActionDialogAdapter adapter = new FavActionDialogAdapter(getActivity(), menu, actionIcons);

        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        Log.d("FAVALERT", name + rating + photoRef);
                        createPostCardIntent(name, rating, photoRef);
                        break;
                    case 1:

                        share(pos.latitude,pos.longitude, name); //will need to pass name of place and location from FavouritePlaces activity

                        break;
                    case 2:

                        int res = markVisited(name, 1);

                        if (res == 0) {
                            Snackbar.make(view, name + " has been marked as visited", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }

                        break;
                    case 3:

                        boolean success = deleteFromTable(placeID, getTable());

                        if (success) {
                            Snackbar.make(view, name + " has been removed from this list", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }

                        break;
                }


            }
        });

        return rootView;
    }

    public String getFavtable() {
        return favtable;
    }

    public String getDayPlanTable() {
        return dayPlanTable;
    }

    public String getTable() {
        if (getDayPlanTable() == null) {
            return getFavtable();
        } else {
            return getDayPlanTable();
        }
    }

    public int markVisited(String name, int val) {
        DatabaseHelper db = new DatabaseHelper(getActivity());
        return db.updatePlaceVisited(name, val);
    }

    public boolean deleteFromTable(long id, String tableName) {
        DatabaseHelper db = new DatabaseHelper(getActivity());
        return db.removeFromTable(id, tableName);
    }

    public void createPostCardIntent(String name, String rating, String photoRef) {
        Intent intent = new Intent(getActivity(), PostCard.class);
        intent.putExtra("name", name);
        intent.putExtra("rating", rating);
        intent.putExtra("photoRef", photoRef);
        startActivity(intent);
    }

    public void share(double latitude, double longitude, String name) {

        String uri = "http://maps.google.com/maps?saddr=" +latitude+","+longitude;

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String ShareSub = "Recommendation";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, ShareSub);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, uri + "\n\n" + "It's a cool place called " + name + ", thought you might want to check it out!");
        startActivity(Intent.createChooser(sharingIntent, "Share via"));

    }

}
