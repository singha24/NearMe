package assasingh.nearmev2.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import assasingh.nearmev2.Adaptors.NearMeListResultAdapter;
import assasingh.nearmev2.Model.SimpleGooglePlace;
import assasingh.nearmev2.R;
import assasingh.nearmev2.View.NearMe;

public class FragmentNearMeListView extends Fragment {


    NearMeListResultAdapter adap;
    private ListView list;
    List<SimpleGooglePlace> places = new ArrayList<>();

    public FragmentNearMeListView() {
        // Required empty public constructor
        places = NearMe.getPlaces();
    }


    public static FragmentNearMeListView newInstance(int page, String title) {
        FragmentNearMeListView listViewFrag = new FragmentNearMeListView();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        listViewFrag.setArguments(args);
        return listViewFrag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        places = NearMe.getPlaces();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_near_me_list_view, container, false);

        list = (ListView) v.findViewById(R.id.the_list);

        places = NearMe.getPlaces();



         adap = new NearMeListResultAdapter(getContext(), (ArrayList<SimpleGooglePlace>) places);

        list.setAdapter(adap);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getContext(), String.valueOf(l), Toast.LENGTH_LONG).show();

            }
        });


        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        list.setAdapter(adap);
        adap.notifyDataSetChanged();
    }


}

