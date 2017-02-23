package assasingh.nearmev2.Services;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.location.LocationListener;

public class LocationService extends Service implements android.location.LocationListener {


    private static final String TAG = "MyLocationService";
    private LocationManager mLocationManager;
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 10f;
    private static double latitude;
    private static double longitude;
    private static Location location;
    LocationListener[] mLocationListeners;
    private boolean isNetworkEnabled = false;

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    public LocationService() {

    }

    static void setLongitude(Double lon) {
        longitude = lon;
    }

    static void setLatitude(Double lat) {
        latitude = lat;
    }

    @Override
    public void onLocationChanged(Location location) {
        setLatitude(location.getLatitude());
        setLongitude(location.getLongitude());

        Log.e(TAG, "onLocationChanged: " + location);
        Log.e(TAG, "lat:" + String.valueOf(location.getLatitude()) + " lon: " + String.valueOf(location.getLongitude()));
        this.location.set(location);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.e(TAG, "onProviderDisabled: " + provider);
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.e(TAG, "onProviderEnabled: " + provider);
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.e(TAG, "onStatusChanged: " + provider);
    }


    public static Double getLongitude() {
        return longitude;
    }

    public static Double getLatitude() {
        return latitude;
    }
    /*
    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };
    */

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);

        //Toast.makeText(this, location.getLatitude() + " : " + location.getLongitude(), Toast.LENGTH_LONG).show();
        Log.e(TAG, getLongitude().toString() + " : " + getLatitude().toString());
        return START_STICKY;
    }

    public static boolean locationAvailable() {
        if (location != null) {
            return true;
        }

        return false;
    }

    public void stopUsingGPS() {
        if (mLocationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                return;
            }
            mLocationManager.removeUpdates(LocationService.this);
        }
    }


    @Override
    public void onCreate() {


        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        location = new Location(LocationManager.PASSIVE_PROVIDER);


        getNetworkLocation();
        getGPSLocation();

        mLocationListeners = new LocationListener[]{
                new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        if(isNetworkEnabled) {
                            setLatitude(getNetworkLocation().getLatitude());
                            setLongitude(getNetworkLocation().getLongitude());
                        }

                        setLatitude(getGPSLocation().getLatitude());
                        setLongitude(getGPSLocation().getLongitude());

                    }
                }
        };

    }

    public Location getNetworkLocation() {

        isNetworkEnabled = mLocationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if(isNetworkEnabled) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                return location;
            }

            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
            Log.d("Network", "Network");

            if (mLocationManager != null) {
                location = mLocationManager
                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }
            }
        }
        return location;
    }

    public Location getGPSLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return location;
        }
        mLocationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                MIN_TIME_BW_UPDATES,
                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
        Log.d("GPS Enabled", "GPS Enabled");

        if(mLocationManager != null){
            location = mLocationManager
                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if(location != null){
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }
        }

        return location;
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    mLocationManager.removeUpdates((android.location.LocationListener) mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listener, ignore", ex);
                }
            }
        }
    }


}
