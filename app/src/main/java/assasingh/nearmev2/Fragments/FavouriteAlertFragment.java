package assasingh.nearmev2.Fragments;


import android.app.DialogFragment;
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

        lv = (ListView) rootView.findViewById(R.id.dialogListView);

        getDialog().setTitle("what would you like to do?");

        FavActionDialogAdapter adapter = new FavActionDialogAdapter(getActivity(), menu, actionIcons);

        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String t = String.valueOf(parent.getItemAtPosition(position));

                switch( position )
                {
                    case 0:
                        Toast.makeText(getActivity(), t, Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(getActivity(), t, Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(getActivity(), t, Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(getActivity(), t, Toast.LENGTH_SHORT).show();
                        break;
                }


            }
        });

        return rootView;
    }

}
