package assasingh.nearmev2.Services;

import android.net.ConnectivityManager;
import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Created by Assa on 20/03/2017.
 */

public class NetworkChecker {


    public static boolean isNetworkConnected(
            final ConnectivityManager connectivityManager) {
        boolean val = false;

        Log.d(TAG, "Checking for Mobile Internet Network");
        final android.net.NetworkInfo mobile = connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobile.isAvailable() && mobile.isConnected()) {
            Log.i(TAG, "Found Mobile Internet Network");
            val = true;
        } else {
            Log.e(TAG, "Mobile Internet Network not Found");
        }

        Log.d(TAG, "Checking for WI-FI Network");
        final android.net.NetworkInfo wifi = connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifi.isAvailable() && wifi.isConnected()) {
            Log.i(TAG, "Found WI-FI Network");
            val = true;
        } else {
            Log.e(TAG, "WI-FI Network not Found");
        }

        return val;
    }
}
