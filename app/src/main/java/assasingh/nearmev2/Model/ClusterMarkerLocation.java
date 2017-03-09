package assasingh.nearmev2.Model;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by Assa on 09/03/2017.
 */

public class ClusterMarkerLocation implements ClusterItem {

    private final LatLng mPosition;
    private final String mTitle;
    private final String mSnippet;

    public ClusterMarkerLocation(double lat, double lng) {
        mPosition = new LatLng(lat, lng);
        mTitle = "test";
        mSnippet = "test";
    }

    public ClusterMarkerLocation(double lat, double lng, String title, String snippet) {
        mPosition = new LatLng(lat, lng);
        mTitle = title;
        mSnippet = snippet;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public String getSnippet() {
        return mSnippet;
    }

}
