package assasingh.nearmev2.Model;

import android.widget.ImageView;

/**
 * Created by Assa Chana on 23/11/2016.
 */

public class FavouritePlace {

    private String title;
    private String date;
    private ImageView image;
    private String time;
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ImageView getImage() {
        return image;
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
