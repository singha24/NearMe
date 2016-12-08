package assasingh.nearmev2.View;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import assasingh.nearmev2.Adaptors.TrendingAdapter;
import assasingh.nearmev2.Fragments.FirstFragment;
import assasingh.nearmev2.Fragments.SecondFragment;
import assasingh.nearmev2.Fragments.ThirdFragment;
import assasingh.nearmev2.Adaptors.ListViewAdapter;
import assasingh.nearmev2.Model.DatabaseOperations;
import assasingh.nearmev2.R;

import android.view.MotionEvent;
import android.gesture.Gesture;
import static android.view.GestureDetector.*;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnGestureListener, OnDoubleTapListener {

    private static String [] menu = {"Your Day Plan", "Near Me", "Favourite Places", "More"};
    private static Integer[] menuImages = {R.drawable.list, R.drawable.location, R.drawable.heart, R.drawable.more};

    private String speechInput = "";

    private final int REQ_CODE_SPEECH_INPUT = 100;

    private EditText userTextInput;

    private GestureDetectorCompat gestureDetectorCompat;

    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.trending);

        TrendingAdapter trendingAdapter = new TrendingAdapter (getSupportFragmentManager());
        viewPager.setAdapter(trendingAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                fabLongPress();
                //autoCompleteTextView();
                return false;
            }
        });

        fab.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3399cc"))); //no working man
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                promptSpeechInput();
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        //.setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        gestureDetectorCompat = new GestureDetectorCompat(MainActivity.this, MainActivity.this);
        gestureDetectorCompat.setOnDoubleTapListener(MainActivity.this);


        ListAdapter theAdapter = new ListViewAdapter(this, menu, menuImages);

        ListView theListView = (ListView) findViewById(R.id.theListView);

        theListView.setAdapter(theAdapter);

        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch( position )
                {
                    case 0:
                        dayPlanIntent();
                        break;
                    case 1:
                        nearMeIntent();
                        break;
                    case 2:
                        favPlacesIntent();
                        break;
                    case 3:
                        //TODO
                        break;
                }

            }
        });

    }

    public void favPlacesIntent(){
        Intent intent = new Intent(this, FavouritePlaces.class);
        startActivity(intent);
    }

    public void nearMeIntent(){
        Intent intent = new Intent(this, NearMe.class);
        startActivity(intent);
    }

    public void settingsIntent(){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void dayPlanIntent(){
        Intent intent = new Intent(this, DayPlan.class);
        startActivity(intent);
    }

    public void fabLongPress(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("It's a bit noisy isn't it?");
        builder.setIcon(ResourcesCompat.getDrawable(getResources(), android.R.drawable.toast_frame, null));

// Set up the input
        userTextInput = new EditText(this);
        userTextInput.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(userTextInput, InputMethodManager.SHOW_IMPLICIT);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        userTextInput.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(userTextInput);

// Set up the buttons
        builder.setPositiveButton("Search", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                speechInput = userTextInput.getText().toString();

                DatabaseOperations dbo = new DatabaseOperations(getApplicationContext());
                dbo.insertQuery(dbo,getTextFromSpeech());

                Toast.makeText(getApplicationContext(), getTextFromSpeech(), Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void autoCompleteTextView(){
        final Dialog dialog = new Dialog(this);
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View post = inflater.inflate(R.layout.autocompleteview, null);
        AutoCompleteTextView  textView = (AutoCompleteTextView)post.findViewById((R.id.history));

        String[] suggestions = {"Test", "Adventure", "Fun"};
        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_dropdown_item_1line, suggestions);
        textView.setAdapter(adapter);
////Autocomplete

//textView.setThreshold(2);
        dialog.setContentView(post);
        dialog.setTitle("Post");
        dialog.show();
    }

    //Speech Input
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void setSpeechText(String text) {
        this.speechInput = text;
    }

    /**
     * Receiving speech input
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    setSpeechText(result.get(0));
                    Toast.makeText(getApplicationContext(), getTextFromSpeech(),
                            Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }
    }

    public String getTextFromSpeech() {
        return speechInput;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            settingsIntent();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentManager manager = getSupportFragmentManager();


        if (id == R.id.nav_first_layout) {
            FirstFragment firstFragment = new FirstFragment();

            manager.beginTransaction().replace(R.id.content_main, firstFragment, firstFragment.getTag()).commit();

        } else if (id == R.id.nav_second_layout) {

            SecondFragment secondFragment = new SecondFragment();
            manager.beginTransaction().replace(R.id.content_main, secondFragment, secondFragment.getTag()).commit();

        } else if (id == R.id.nav_third_layout) {

            ThirdFragment thirdFragment = new ThirdFragment();
            manager.beginTransaction().replace(R.id.content_main, thirdFragment, thirdFragment.getTag()).commit();

        }  else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        enableGPS();
    }

    public void enableGPS() {
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = service
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

// check if enabled and if not send user to the GSP settings
// Better solution would be to display a dialog and suggesting to
// go to the settings
        if (!enabled) {

            new AlertDialog.Builder(this)
                    .setTitle("uh Houston, we have a problem...")
                    .setCancelable(false)
                    .setMessage("In order to use this app, please consider turning on location services")
                    .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Another Time", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            quit();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();



        }
    }

    public void createAlert(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void quit() {
        int pid = android.os.Process.myPid();
        android.os.Process.killProcess(pid);
        System.exit(0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetectorCompat.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        autoCompleteTextView();
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        autoCompleteTextView();
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        autoCompleteTextView();
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        autoCompleteTextView();
        return false;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        autoCompleteTextView();
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        autoCompleteTextView();
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        autoCompleteTextView();
        return false;
    }
}
