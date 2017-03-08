package assasingh.nearmev2.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import assasingh.nearmev2.Adaptors.ActivityPreferenceAdaptor;
import assasingh.nearmev2.Adaptors.ActivityPreference;
import assasingh.nearmev2.R;

public class SettingsActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.settingsToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = (ListView) findViewById(R.id.prefList);

        ActivityPreference activityPreference = new ActivityPreference(this);

        ActivityPreferenceAdaptor s = new ActivityPreferenceAdaptor(this, R.layout.preferences_single_row, new ActivityPreference[]{activityPreference });

        listView.setAdapter(s);



    }
}
