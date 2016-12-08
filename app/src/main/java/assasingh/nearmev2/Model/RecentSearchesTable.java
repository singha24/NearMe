package assasingh.nearmev2.Model;

import android.provider.BaseColumns;

/**
 * Created by Assa Chana on 08/12/2016.
 */

public class RecentSearchesTable {

    public RecentSearchesTable(){

    }

    public static abstract class TableInfo implements BaseColumns{

        public static final String SEARCH_TEXT = "search_text"; //first column

        public static final String RECENT_SEARCHES = "recent_searches"; //DB NAME
        public static final String RECENT_SEARCHES_TABLE = "recent_searches_table"; //table name

    }



}
