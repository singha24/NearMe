package assasingh.nearmev2.Model;

import java.util.ArrayList;

/**
 * Created by Assa Chana on 08/12/2016.
 */

public class Place {

    private double locationLat;
    private double locationLng;
    private String photoUrl;
    private ArrayList<String> type; //bar, lodging, POI, establishment
    private String placeId;
    private double rating;
    private String icon; //url
    private boolean openNow;
    private String weekdayText;


    public double getLocationLat() {
        return locationLat;
    }

    public void setLocationLat(double locationLat) {
        this.locationLat = locationLat;
    }

    public double getLocationLng() {
        return locationLng;
    }

    public void setLocationLng(double locationLng) {
        this.locationLng = locationLng;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public ArrayList<String> getType() {
        return type;
    }

    public void setType(ArrayList<String> type) {
        this.type = type;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public boolean isOpenNow() {
        return openNow;
    }

    public void setOpenNow(boolean openNow) {
        this.openNow = openNow;
    }

    public String getWeekdayText() {
        return weekdayText;
    }

    public void setWeekdayText(String weekdayText) {
        this.weekdayText = weekdayText;
    }
}
