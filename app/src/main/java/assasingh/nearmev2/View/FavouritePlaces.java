package assasingh.nearmev2.View;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import assasingh.nearmev2.Adaptors.FavouriteListAdapter;
import assasingh.nearmev2.Fragments.FavouriteAlertFragment;
import assasingh.nearmev2.Model.FavouritePlace;
import assasingh.nearmev2.R;
import assasingh.nearmev2.Services.DatabaseHelper;

public class FavouritePlaces extends AppCompatActivity {


    FavouritePlace favPlace;
    String name;
    String rating;
    String photoRef;
    String date;
    long id;
    double lat;
    double lng;

    public static FavouriteListAdapter adap;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fav_content);

        final ListView favListview = (ListView) findViewById(R.id.favListView);

        Toolbar toolbar = (Toolbar) findViewById(R.id.favToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DatabaseHelper db = new DatabaseHelper(this);



        ArrayList<FavouritePlace> favs = new ArrayList<FavouritePlace>();

        Cursor c = db.getAllFromFavPlacesTable();



        while (c.moveToNext()){
            name = c.getString(c.getColumnIndexOrThrow("name"));
            rating = c.getString(c.getColumnIndexOrThrow("rating"));
            photoRef = c.getString(c.getColumnIndexOrThrow("photo_ref"));
            date = c.getString(c.getColumnIndexOrThrow("date"));
            id = c.getLong(c.getColumnIndexOrThrow("_id"));

            lat = c.getDouble(c.getColumnIndexOrThrow("lat"));
            lng = c.getDouble(c.getColumnIndexOrThrow("lng"));



            favPlace = new FavouritePlace();

            favPlace.setTitle(name);
            favPlace.setDate(date);
            favPlace.setRating(rating);
            favPlace.setId(id);
            favPlace.setLatLng(lat,lng);
            favPlace.setPhotoRef(photoRef);


            favs.add(favPlace);



        }

        c.close();
        db.close();




        final ListView lv1 = (ListView) findViewById(R.id.favListView);

         adap = new FavouriteListAdapter(this, favs);

        lv1.setAdapter(adap);

        GetImageFromUrl get = new GetImageFromUrl();
        String url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + photoRef + "&key=AIzaSyB3Qirj2H1pL_63c7yXcMIMCjcQUinyHS4";

        //get.execute(url);



        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                FavouritePlace picked = (FavouritePlace) parent.getItemAtPosition(position);

                final android.app.FragmentManager fm = getFragmentManager();
                final  FavouriteAlertFragment favFrag = new FavouriteAlertFragment();

                Bundle bundle = new Bundle();
                bundle.putLong("id", picked.getId());
                bundle.putString("name", picked.getTitle());
                bundle.putParcelable("latlng", picked.getLatLng());
                bundle.putInt("posInList", position);
                bundle.putString("photoRef", picked.getPhotoRef());
                bundle.putString("rating", picked.getRating());
                favFrag.setArguments(bundle);

                favFrag.show(fm,"Alert");


            }
        });

    }

    private class GetImageFromUrl extends AsyncTask<String, Void, Drawable> {

        @Override
        protected Drawable doInBackground(String... params) {

            try {
                InputStream is = (InputStream) new URL(params[0]).getContent();
                Drawable d = Drawable.createFromStream(is, "src name");
                return d;
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Drawable d) {

            if (d != null) {
                //FavouriteListAdapter.ViewHolder.image.setImageDrawable(d);
            } else {
                //FavouriteListAdapter.ViewHolder.image.setImageResource(R.drawable.no_image);
            }


        }


    }


}
