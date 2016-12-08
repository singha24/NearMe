package assasingh.nearmev2.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.R.attr.version;

/**
 * Created by Assa Chana on 08/12/2016.
 */

public class DatabaseOperations extends SQLiteOpenHelper {

    public static final int databaseVersion = 1;
    public String CREATE_QUERY = "create table " + RecentSearchesTable.TableInfo.RECENT_SEARCHES_TABLE + "(" + RecentSearchesTable.TableInfo.SEARCH_TEXT + " TEXT);";


    public DatabaseOperations(Context context){
        super(context, RecentSearchesTable.TableInfo.RECENT_SEARCHES, null, databaseVersion);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertQuery(DatabaseOperations dbo, String searchText){
        SQLiteDatabase sqldb = dbo.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(RecentSearchesTable.TableInfo.SEARCH_TEXT, searchText);

        long success = sqldb.insert(RecentSearchesTable.TableInfo.RECENT_SEARCHES_TABLE, null, cv); //inserting into db

        Log.d("Database operations", String.valueOf(success));

    }

    public Cursor getQuery(DatabaseOperations dbo){
        SQLiteDatabase sqldb = dbo.getReadableDatabase();

        String[] columns = {RecentSearchesTable.TableInfo.SEARCH_TEXT};

        Cursor cursor = sqldb.query(RecentSearchesTable.TableInfo.RECENT_SEARCHES_TABLE, columns, null, null, null, null, null);

        return cursor;
    }

}
