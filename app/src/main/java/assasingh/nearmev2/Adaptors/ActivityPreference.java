package assasingh.nearmev2.Adaptors;

import android.content.Context;
import android.widget.ArrayAdapter;

import assasingh.nearmev2.R;

/**
 * Created by Assa on 27/02/2017.
 */

public class ActivityPreference {

    private int selected;
    private ArrayAdapter<CharSequence> adapter;

    public ActivityPreference(Context parent){

        adapter = ArrayAdapter.createFromResource(parent, R.array.activityPreference, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    }

    public ArrayAdapter<CharSequence> getAdapter() {
        return adapter;
    }

    public String getText() {
        return (String) adapter.getItem(selected);
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

}
