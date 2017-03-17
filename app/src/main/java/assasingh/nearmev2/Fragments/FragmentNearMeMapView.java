package assasingh.nearmev2.Fragments;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.maps.android.clustering.ClusterManager;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import assasingh.nearmev2.Model.ClusterMarkerLocation;
import assasingh.nearmev2.Model.GooglePlaceList;
import assasingh.nearmev2.Model.GooglePlacesUtility;
import assasingh.nearmev2.Model.SimpleGooglePlace;
import assasingh.nearmev2.R;
import assasingh.nearmev2.Services.DatabaseHelper;
import assasingh.nearmev2.View.NearMe;
import assasingh.nearmev2.View.NearMeCard;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentNearMeMapView extends Fragment {

    private MapView mapView;
    private GooglePlaceList nearby;
    private final String TAG = "MAP_DEBUG";
    private String placesRequest;
    LatLngBounds bounds;
    List<SimpleGooglePlace> googlePlaces;



    public FragmentNearMeMapView() {
        // Required empty public constructor

    }


    public static FragmentNearMeMapView newInstance(int page, String title) {
        FragmentNearMeMapView mapViewFrag = new FragmentNearMeMapView();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        mapViewFrag.setArguments(args);
        return mapViewFrag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_near_me_map_view, container, false);

        mapView = (MapView) v.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);


        googlePlaces = NearMe.getPlaces();


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



//                /NearMe.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, 10));
                //NearMe.googleMap.setOnCameraIdleListener(NearMe.clusterManager);

                NearMe.googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {



                        String m = marker.getId().replace("m", "");
                        long pos = Integer.valueOf(m);

                        Intent intent = new Intent(getActivity(), NearMeCard.class);
                        intent.putExtra("id", pos);
                        startActivity(intent);
                    }
                });

                for(SimpleGooglePlace place: googlePlaces){
                    LatLng pos = new LatLng(place.getLatitude(), place.getLongitude());
                    NearMe.googleMap.addMarker(new MarkerOptions().position(pos).title(place.getName()).snippet(String.valueOf(place.getRating())));
                }




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

    class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View myContentsView;

        MyInfoWindowAdapter(){
            myContentsView = getActivity().getLayoutInflater().inflate(R.layout.info_window, null);
        }

        @Override
        public View getInfoContents(Marker marker) {

            TextView placeName = ((TextView)myContentsView.findViewById(R.id.infowindow_name));
            placeName.setText(marker.getTitle());
            TextView rating = ((TextView)myContentsView.findViewById(R.id.infowindow_rating));
            rating.setText(marker.getSnippet());

            ImageView icon = ((ImageView)myContentsView.findViewById(R.id.infowindow_icon));

            return myContentsView;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            // TODO Auto-generated method stub
            return null;
        }

    }



    public String getPlacesRequestURL() {
        return placesRequest;
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

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            Log.d(TAG, ((Object) this).getClass().getSimpleName() + " is NOT on screen");
        }
        else
        {
            Log.d(TAG, ((Object) this).getClass().getSimpleName() + " is on screen");
        }


    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //NearMe.googleMap.setOnCameraIdleListener(NearMe.mClusterManager);

            /*final CameraPosition[] mPreviousCameraPosition = {null};
            NearMe.googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                @Override
                public void onCameraIdle() {
                    CameraPosition position = NearMe.googleMap.getCameraPosition();
                    if(mPreviousCameraPosition[0] == null || mPreviousCameraPosition[0].zoom != position.zoom) {
                        mPreviousCameraPosition[0] = NearMe.googleMap.getCameraPosition();
                        NearMe.mClusterManager.cluster();
                    }
                }
            });*/

            NearMe.googleMap.setOnMarkerClickListener(NearMe.mClusterManager);
            NearMe.googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(NearMe.bounds, 150), null);
        } else {
            // Do your Work
        }
    }

}
