package assasingh.nearmev2.Model;

import android.widget.ImageView;

/**
 * Created by Assa Chana on 23/11/2016.
 */

public class DayPlan {

    private String title;
    private String description;
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
