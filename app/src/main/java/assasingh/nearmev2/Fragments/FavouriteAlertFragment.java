package assasingh.nearmev2.Fragments;


import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import assasingh.nearmev2.Adaptors.FavActionDialogAdapter;
import assasingh.nearmev2.R;
import assasingh.nearmev2.View.FavouritePlaces;
import assasingh.nearmev2.View.Postcard;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavouriteAlertFragment extends DialogFragment {

    private static ListView lv;
    private static Integer[] actionIcons = {R.drawable.postcard, R.drawable.share, R.drawable.cross};
    private static String[] menu = {"Create Postcard", "Share this place", "Remove from favourites"};


    public FavouriteAlertFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_list_view, container, false);

        final String picked = getArguments().getString("selected");

        lv = (ListView) rootView.findViewById(R.id.dialogListView);

        getDialog().setTitle("What would you like to do?");

        FavActionDialogAdapter adapter = new FavActionDialogAdapter(getActivity(), menu, actionIcons);

        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String t = String.valueOf(parent.getItemAtPosition(position));

                switch( position )
                {
                    case 0:
                        createPostCardIntent();
                        break;
                    case 1:
                        share(picked,"birmingham"); //will need to pass name of place and location from FavouritePlaces activity
                        break;
                    case 2:
                        Toast.makeText(getActivity(), t, Toast.LENGTH_SHORT).show();
                        break;
                }


            }
        });

        return rootView;
    }

    public void createPostCardIntent(){
        Intent intent = new Intent(getActivity(), Postcard.class);
        startActivity(intent);
    }

    public void share(String name, String location){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, name);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

}
