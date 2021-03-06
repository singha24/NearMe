package assasingh.nearmev2.Services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Assa on 08/03/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Places.db";
    private static final String FAV_PLACES_TABLE = "loved";
    private static final String LAT_LNG_TABLE = "lat_lng_table";
    private static final String DAY_PLAN_TABLE = "dayPlan";

    private static final String LATLNG_ID = "_id";
    private static final String LATLNG_LAT = "lat";
    private static final String LATLNG_LNG = "lng";

    private static final String PLACES_ID = "_id";
    private static final String LAT = "lat";
    private static final String LNG = "lng";
    private static final String NAME = "name";
    private static final String PHOTO_REF = "photo_ref";
    private static final String RATING = "rating";
    private static final String OPEN_NOW = "open_now";
    private static final String WEEKDAY_TEXT = "weekday_text";
    private static final String TYPE = "type";
    private static final String DATE = "date";
    private static final String VISITED = "visited";

    private static int VERSION = 1;

    private Context context;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);

        this.context = context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + FAV_PLACES_TABLE + " ( " + PLACES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + LAT + "," + LNG + ", " + NAME + ", " + PHOTO_REF + "," + RATING + ", " +
                OPEN_NOW + ", " + WEEKDAY_TEXT + "," + TYPE + "," + DATE + "," + VISITED + ")");
        db.execSQL("create table " + LAT_LNG_TABLE + "( " + LATLNG_ID + "," + LATLNG_LAT + "," + LATLNG_LNG + ")");
        db.execSQL("create table " + DAY_PLAN_TABLE + "(" + PLACES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME + ", " + LAT + ", " + LNG + ", " + PHOTO_REF + ", description)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists" + FAV_PLACES_TABLE);
        db.execSQL("drop table if exists" + LAT_LNG_TABLE);
        db.execSQL("drop table if exists" + DAY_PLAN_TABLE);
        onCreate(db);

    }

    public int updatePlaceVisited(String name, int val) {

        long result;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(VISITED, 1);

        result = db.update(FAV_PLACES_TABLE, cv, NAME + '=' + "'" + name + "'", null);

        if (result == -1)
            return 1;
        else
            return 0;
    }

    public int insertIntoDayPlan(Double lat, Double lng, String name, String photo_ref, String description) {
        SQLiteDatabase db = this.getWritableDatabase();

        long result;
        ContentValues cv = new ContentValues();

        cv.put(LAT, lat);
        cv.put(LNG, lng);
        cv.put(NAME, name);
        cv.put(PHOTO_REF, photo_ref);
        cv.put("description", description);


        result = db.insert(DAY_PLAN_TABLE, null, cv);


        if (result == -1)
            return 1;
        else
            return 0;

    }

    public boolean removeFromDayPlan(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(DAY_PLAN_TABLE, PLACES_ID + " = " + id, null) > 0;
    }

    public boolean insertLatLng(Double lat, Double lng) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(LAT, lat);
        cv.put(LNG, lng);

        long result = db.insert(LAT_LNG_TABLE, null, cv);

        if (result == -1)
            return false;
        else
            return true;

    }

    public Cursor getLastKnownLocation() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT  * FROM " + LAT_LNG_TABLE + " ORDER BY " + LATLNG_ID + " DESC LIMIT 1", null); //get last inserted record
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public boolean removeFromTable(long id, String table) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(table, PLACES_ID + " = " + id, null) > 0;
    }


    public int insertPlaceToFavs(Double lat, Double lng, String name, String photo_ref, Double rating, String open, String weekdayText, String type, String date, int visited) {
        SQLiteDatabase db = this.getWritableDatabase();

        long result;
        ContentValues cv = new ContentValues();

        cv.put(LAT, lat);
        cv.put(LNG, lng);
        cv.put(NAME, name);
        cv.put(PHOTO_REF, photo_ref);
        cv.put(RATING, rating);
        cv.put(OPEN_NOW, open);
        cv.put(WEEKDAY_TEXT, weekdayText);
        cv.put(TYPE, type);
        cv.put(DATE, date);
        cv.put(VISITED, visited);

        result = db.insert(FAV_PLACES_TABLE, null, cv);


        if (result == -1)
            return 1;
        else
            return 0;

    }

    public Cursor getAllFromDayPlanTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + DAY_PLAN_TABLE, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public Cursor getAllFromFavPlacesTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + FAV_PLACES_TABLE, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public Cursor getAllFromFavPlacesWhereID(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT  * FROM " + FAV_PLACES_TABLE + " where _id = " + id, null);
        return c;
    }

    public Cursor getImages() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT photo_ref, name FROM " + FAV_PLACES_TABLE + " where rating > 4.0", null);
        return c;
    }

    public void refreshPlacesTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + FAV_PLACES_TABLE);
    }
}
