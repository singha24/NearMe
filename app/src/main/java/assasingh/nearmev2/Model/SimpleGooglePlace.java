package assasingh.nearmev2.Model;

/**
 * Created by Assa on 23/02/2017.
 */

public class SimpleGooglePlace {

    private double latitude;
    private double longitude;
    private String name;
    private String photoRef;
    private double rating;
    private boolean openNow;
    private String exceptionalDates;
    private String weekdayText;

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

    public void setOpenNow(boolean openNow) {
        this.openNow = openNow;
    }

    public boolean getOpenNow(){
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
}
