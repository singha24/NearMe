package assasingh.nearmev2.Fragments;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
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
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
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

import static assasingh.nearmev2.View.NearMe.circle;

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
    private ClusterManager<SimpleGooglePlace> mClusterManager;
    private GoogleMap googleMap;


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


        mClusterManager = new ClusterManager<SimpleGooglePlace>(getActivity(), googleMap);


        //googleMap.setOnCameraChangeListener(mClusterManager);


        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    return;
                }
                googleMap.setMyLocationEnabled(true);


//                /NearMe.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, 10));
                //NearMe.googleMap.setOnCameraIdleListener(NearMe.clusterManager);

                googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {

                        String m = marker.getId().replace("m", "");
                        long pos = Integer.valueOf(m);

                        Intent intent = new Intent(getActivity(), NearMeCard.class);
                        intent.putExtra("id", pos);
                        startActivity(intent);
                    }
                });


                for (SimpleGooglePlace place : googlePlaces) {
                    LatLng pos = new LatLng(place.getLatitude(), place.getLongitude());
                    googleMap.addMarker(new MarkerOptions().position(pos).title(place.getName()).snippet(String.valueOf(place.getRating())));

                    mClusterManager.addItem(place);

                    circle = googleMap.addCircle(new CircleOptions()
                            .center(place.getPosition())
                            .radius(1000)
                            .strokeWidth(5)
                            .strokeColor(Color.BLACK)
                            .fillColor(Color.parseColor("#FFF859"))
                            .clickable(true));

                    googleMap.setOnCircleClickListener(new GoogleMap.OnCircleClickListener() {
                        @Override
                        public void onCircleClick(Circle circle) {

                            //circle.setFillColor(Color.parseColor("#C740FF"));
                        }
                    });

                }


                // For dropping a marker at a point on the Map
                LatLng userPos = new LatLng(((NearMe) getActivity()).getLatitude(), ((NearMe) getActivity()).getLongitude());


            }
        });

        return v;


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
        } else {
            Log.d(TAG, ((Object) this).getClass().getSimpleName() + " is on screen");
        }


    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

            googleMap.setOnMarkerClickListener(mClusterManager);
            //googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(NearMe.bounds, 150), null);

            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(NearMe.getLatLng(), 15));
        } else {
            // Do your Work
        }
    }

}
