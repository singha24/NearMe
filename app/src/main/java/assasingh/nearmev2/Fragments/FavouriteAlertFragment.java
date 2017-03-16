package assasingh.nearmev2.Fragments;


import android.app.DialogFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import assasingh.nearmev2.Adaptors.FavActionDialogAdapter;
import assasingh.nearmev2.Adaptors.FavouriteListAdapter;
import assasingh.nearmev2.Model.FavouritePlace;
import assasingh.nearmev2.R;
import assasingh.nearmev2.Services.DatabaseHelper;
import assasingh.nearmev2.View.PostCard;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavouriteAlertFragment extends DialogFragment {

    private static ListView lv;
    private static Integer[] actionIcons = {R.drawable.postcard, R.drawable.share, R.drawable.cross};
    private static String[] menu = {"Create Postcard", "Share this place", "Remove from favourites"};
    private static long placeID;
    private String name;
    private LatLng pos;
    private int posInList;
    private String photoRef;


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
                        createPostCardIntent();
                        break;
                    case 1:

                        share(pos.latitude,pos.longitude, name); //will need to pass name of place and location from FavouritePlaces activity

                        break;
                    case 2:

                        boolean success = deleteFromFavourites(placeID);

                        Toast.makeText(getActivity(), placeID + " : " + String.valueOf(success), Toast.LENGTH_SHORT).show();

                        break;
                }


            }
        });

        return rootView;
    }

    public boolean deleteFromFavourites(long id){
        DatabaseHelper db = new DatabaseHelper(getActivity());
        return db.removeFromFavs(id);
    }

    public void createPostCardIntent() {
        Intent intent = new Intent(getActivity(), PostCard.class);
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
