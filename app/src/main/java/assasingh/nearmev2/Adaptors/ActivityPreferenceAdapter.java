package assasingh.nearmev2.Adaptors;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import assasingh.nearmev2.R;

/**
 * Created by Assa on 27/02/2017.
 */

public class ActivityPreferenceAdapter extends ArrayAdapter<ActivityPreference> {

    private Activity myContext;


    public ActivityPreferenceAdapter(Activity context, int textViewResourceId, ActivityPreference[] objects) {
        super(context, textViewResourceId, objects);
        myContext = context;
    }

    static class ViewHolder {
        protected ActivityPreference data;
        protected TextView text;
        protected Spinner spin;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        // Check to see if this row has already been painted once.
        if (convertView == null) {

            // If it hasn't, set up everything:
            LayoutInflater inflator = myContext.getLayoutInflater();
            view = inflator.inflate(R.layout.preferences_single_row, null);

            // Make a new ViewHolder for this row, and modify its data and spinner:
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.text = (TextView) view.findViewById(R.id.pref_textView);
            viewHolder.data = new ActivityPreference(myContext);
            viewHolder.spin = (Spinner) view.findViewById(R.id.pref_spinner);
            viewHolder.spin.setAdapter(viewHolder.data.getAdapter());

            // Used to handle events when the user changes the Spinner selection:
            viewHolder.spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    viewHolder.data.setSelected(arg2);
                    viewHolder.text.setText(viewHolder.data.getText());
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }

            });

            // Update the TextView to reflect what's in the Spinner
            viewHolder.text.setText(viewHolder.data.getText());

            view.setTag(viewHolder);

            Log.d("SETTINGS_ADAPT", viewHolder.text.getText() + "");
        } else {
            view = convertView;
        }

        // This is what gets called every time the ListView refreshes
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.text.setText(getItem(position).getText());
        holder.spin.setSelection(getItem(position).getSelected());

        return view;
    }
}
