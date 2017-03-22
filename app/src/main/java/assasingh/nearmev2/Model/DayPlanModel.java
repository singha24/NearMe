package assasingh.nearmev2.Model;

import android.widget.ImageView;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by Assa Chana on 23/11/2016.
 */

public class DayPlanModel implements Serializable {

    private String title;
    private String description;
    private String photoRef;
    private String time;
    private long id;
    private LatLng latlng;
    private String placeId;

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public void setLatlng(double lat, double lng) {
        LatLng ll = new LatLng(lat, lng);
        this.latlng = ll;
    }

    public LatLng getLatlng() {
        return latlng;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setPhotoRef(String photoRef) {
        this.photoRef = photoRef;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhotoRef() {
        return photoRef;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


}
