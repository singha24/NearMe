package assasingh.nearmev2.Fragments;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import assasingh.nearmev2.Model.GooglePlaceList;
import assasingh.nearmev2.Model.GooglePlacesUtility;
import assasingh.nearmev2.Model.SimpleGooglePlace;
import assasingh.nearmev2.R;
import assasingh.nearmev2.Services.DatabaseHelper;
import assasingh.nearmev2.View.NearMe;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentNearMeMapView extends Fragment {

    private MapView mapView;
    private GooglePlaceList nearby;
    private final String TAG = "MAP_DEBUG";
    private String placesRequest;
    LatLngBounds bounds;




    public FragmentNearMeMapView() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_near_me_map_view, container, false);

        mapView = (MapView) v.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);





        mapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }







        //
        //NearMe.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(52.5009146, -1.9371191), 100));




        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                NearMe.googleMap = mMap;

                // For showing a move to my location button
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    return;
                }
                NearMe.googleMap.setMyLocationEnabled(true);

                CameraPosition position = CameraPosition.builder()
                        .target( new LatLng(52.5009146, -1.9371191) )
                        .zoom( 12.0f )
                        .build();

                NearMe.googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), null);


//                /NearMe.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, 10));
                //NearMe.googleMap.setOnCameraIdleListener(NearMe.clusterManager);
                NearMe.googleMap.setOnMarkerClickListener(NearMe.clusterManager);



                /*bounds = null;
                for(int i =0; i< NearMe.markers.size(); i++){
                    bounds.including(NearMe.markers.get(i));
                }

                NearMe.googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds,100));*/


                // For dropping a marker at a point on the Map
                LatLng userPos = new LatLng(((NearMe) getActivity()).getLatitude(), ((NearMe) getActivity()).getLongitude());


            }
        });

        return v;


    }

    public String getPlacesRequestURL() {
        return placesRequest;
    }

    private class NearMeAsync extends AsyncTask<Double, Integer, List<SimpleGooglePlace>> {


        @Override
        protected List<SimpleGooglePlace> doInBackground(Double... params) {
            //call which will return list of google places that are near a lat and long

            GooglePlacesUtility util = new GooglePlacesUtility();
            List<SimpleGooglePlace> places = new ArrayList<SimpleGooglePlace>();
            try {
                places = util.networkCall(getPlacesRequestURL());
            } catch (Exception e) {
                e.printStackTrace();
            }

            return places;
        }

        @Override
        protected void onPostExecute(List<SimpleGooglePlace> googlePlaces) {

            for (SimpleGooglePlace place : googlePlaces) {


                String rating = String.valueOf(place.getRating());


            }

        }
    }


    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {

    }

}
