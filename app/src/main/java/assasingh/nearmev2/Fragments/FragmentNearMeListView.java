package assasingh.nearmev2.Fragments;


import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.net.URLEncoder;

import assasingh.nearmev2.Adaptors.NearMeListResultAdaptor;
import assasingh.nearmev2.Model.GooglePlaceList;
import assasingh.nearmev2.Model.GooglePlacesUtility;
import assasingh.nearmev2.R;
import assasingh.nearmev2.Services.LocationService;
import assasingh.nearmev2.View.NearMe;

public class FragmentNearMeListView extends Fragment {


    private GooglePlaceList nearby;
    private ListView list;

    public FragmentNearMeListView() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_near_me_list_view, container, false);

        list = (ListView) v.findViewById(R.id.the_list);

        Cursor c = NearMe.db.getAllFromPlacesTable();

        if(c.getCount() == 0){
            Toast.makeText(getActivity(),"Oops", Toast.LENGTH_LONG).show();
        }


        NearMeListResultAdaptor adap = new NearMeListResultAdaptor(getActivity(), c);

        list.setAdapter(adap);

        nearby = null;

        return v;


    }
}

