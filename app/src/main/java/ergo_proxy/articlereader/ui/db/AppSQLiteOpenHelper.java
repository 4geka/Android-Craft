package ergo_proxy.articlereader.ui.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AppSQLiteOpenHelper extends SQLiteOpenHelper
{

    private static final String DB_NAME = "app.db";
    private static final int DB_VERSION = 6;

    public static final String COLUMN_ID = "_id";

    public static final String TABLE_ARTICLES = "articles";
    public static final String TABLE_CATEGORIES = "categories";

    public static final String COLUMN_TITLE = "title";
    public static final String ARTICLES_COLUMN_DESCRIPTION = "description";
    public static final String ARTICLES_COLUMN_PUBLISHED = "published";
    public static final String ARTICLES_COLUMN_CATEGORY_ID = "category_id";
    public static final String ARTICLES_COLUMN_CREATE_AT = "create_at";
    public static final String ARTICLES_COLUMN_UPDATE_AT= "update_at";
    public static final String ARTICLES_COLUMN_OWN = "own";
    public static final String ARTICLES_COLUMN_PHOTO = "photo";

    private static final String CREATE_TABLE_ARTICLES = "" +
            "CREATE TABLE " + TABLE_ARTICLES + "(" +
            COLUMN_ID + " integer primary key," +
            COLUMN_TITLE   + " text," +
            ARTICLES_COLUMN_DESCRIPTION  + " text," +
            ARTICLES_COLUMN_PUBLISHED  + " integer," +
            ARTICLES_COLUMN_CATEGORY_ID  + " integer," +
            ARTICLES_COLUMN_CREATE_AT  + " text," +
            ARTICLES_COLUMN_UPDATE_AT  + " text," +
            ARTICLES_COLUMN_OWN  + " integer," +
            ARTICLES_COLUMN_PHOTO  + " text" +
            ");";

    private static final String CREATE_TABLE_CATEGORIES = "" +
            "CREATE TABLE " + TABLE_CATEGORIES + "(" +
            COLUMN_ID + " integer primary key," +
            COLUMN_TITLE   + " text" +
            ");";

    public AppSQLiteOpenHelper(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String[] CREATES =
                {
                CREATE_TABLE_ARTICLES,
                CREATE_TABLE_CATEGORIES
        };
        for (final String table : CREATES)
        {
            db.execSQL(table);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        String[] TABLES =
                {
                TABLE_ARTICLES,
                TABLE_CATEGORIES
        };
        for (final String table : TABLES)
        {
            db.execSQL("DROP TABLE IF EXISTS " + table);
        }
        onCreate(db);
    }
}
