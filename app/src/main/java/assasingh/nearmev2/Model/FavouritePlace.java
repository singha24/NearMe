package assasingh.nearmev2.Model;

import android.widget.ImageView;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Assa Chana on 23/11/2016.
 */

public class FavouritePlace {

    private String title;
    private String date;
    private String photoRef;
    private String time;
    private String rating;
    private long id;
    private LatLng pos;


    public LatLng getLatLng(){
        return pos;
    }

    public void setLatLng(double lat, double lng){
        pos = new LatLng(lat, lng);
    }

    public void setPhotoRef(String photoRef) {
        this.photoRef = photoRef;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
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

    public String getDate(){
        return date;
    }

    public void setDate(String date){
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


}
