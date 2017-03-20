package assasingh.nearmev2.Model;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by Assa on 23/02/2017.
 */

public class SimpleGooglePlace implements ClusterItem {

    private double latitude;
    private double longitude;
    private String name;
    private String photoRef;
    private double rating;
    private String openNow;
    private String exceptionalDates;
    private String weekdayText;
    private String types;
    String phone;
    String address;

    String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    private final LatLng mPosition;

    public SimpleGooglePlace(double lat, double lng) {
        mPosition = new LatLng(lat,lng);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public String getExceptionalDates() {
        return exceptionalDates;
    }

    public void setExceptionalDates(String exceptionalDates) {
        this.exceptionalDates = exceptionalDates;
    }

    public String getWeekdayText() {
        return weekdayText;
    }

    public void setWeekdayText(String weekdayText) {
        this.weekdayText = weekdayText;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setOpenNow(String openNow) {
        this.openNow = openNow;
    }

    public String getOpenNow(){
        return openNow;
    }

    public String getPhotoRef() {
        return photoRef;
    }

    public void setPhotoRef(String photoRef) {
        this.photoRef = photoRef;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getName() {
        return name;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return name;
    }

    @Override
    public String getSnippet() {
        return String.valueOf(rating);
    }
}
