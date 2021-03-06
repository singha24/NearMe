package assasingh.nearmev2.View;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Set;

import assasingh.nearmev2.R;

public class Test extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        Set<String> activitySelections = sharedPrefs.getStringSet("activity_types", null);

        Set<String> defaultActivities = new HashSet<String>();
        defaultActivities.add("cafe");
        defaultActivities.add("zoo");

        StringBuilder builder = new StringBuilder();

        builder.append("\n" + "opne now:\t" + sharedPrefs.getBoolean("openNowPref", false));
        builder.append("\n" + "Max walk:\t" + sharedPrefs.getString("walk_max", "-1"));
        builder.append("\n" + "cost:\t" + sharedPrefs.getString("cost_max", "-1"));
        builder.append("\n" + "Activity Pref:\t" + sharedPrefs.getStringSet("activity_types", activitySelections));
        builder.append("\n" + "Customized Notification Ringtone:\t" + sharedPrefs.getString("notification_ringtone", ""));
        builder.append("\n" + "language:\t" + sharedPrefs.getString("language_pref", "en"));
        builder.append("\n" + "travel method:\t" + sharedPrefs.getString("travel_mode", "walking"));

        TextView settingsTextView = (TextView) findViewById(R.id.settingsContent);
        settingsTextView.setText(builder.toString());
    }
}
