package ergo_proxy.articlereader.ui.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import static ergo_proxy.articlereader.ui.db.AppSQLiteOpenHelper.*;

public class AppContentProvider extends ContentProvider
{

    private static final String AUTHORITY = "ergo_proxy.content_provider";

    public static final Uri CONTENT_URI_ARTICLES = Uri.parse("content://" + AUTHORITY + "/" + TABLE_ARTICLES);
    public static final Uri CONTENT_URI_CATEGORIES = Uri.parse("content://" + AUTHORITY + "/" + TABLE_CATEGORIES);
    private static final int CODE_ONE_ARTICLE = 0;
    private static final int CODE_ALL_ARTICLES = 1;
    private static final int CODE_CATEGORIES = 2;

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static
    {
        URI_MATCHER.addURI(AUTHORITY, TABLE_ARTICLES+"/#", CODE_ONE_ARTICLE);
        URI_MATCHER.addURI(AUTHORITY, TABLE_ARTICLES, CODE_ALL_ARTICLES);
        URI_MATCHER.addURI(AUTHORITY, TABLE_CATEGORIES, CODE_CATEGORIES);
    }

    private static AppSQLiteOpenHelper dbHelper;

    public synchronized static AppSQLiteOpenHelper getDbHelper(Context context)
    {
        if (null == dbHelper)
        {
            dbHelper = new AppSQLiteOpenHelper(context);
        }
        return dbHelper;
    }

    @Override
    public boolean onCreate()
    {
        getDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder)
    {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        queryBuilder.setTables(TABLE_ARTICLES);
        int uriType = URI_MATCHER.match(uri);
        switch (uriType)
        {
            case CODE_ALL_ARTICLES:
                break;
            case CODE_ONE_ARTICLE:
                queryBuilder.appendWhere(COLUMN_ID + "="
                        + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values)
    {
        int uriType = URI_MATCHER.match(uri);
        SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();
        long id = 0;
        switch (uriType)
        {
            case CODE_ALL_ARTICLES:
                id = sqlDB.insert(TABLE_ARTICLES, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(TABLE_ARTICLES + "/" + id);

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs)
    {
        int uriType = URI_MATCHER.match(uri);
        SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();
        int rowsDeleted = 0;
        switch (uriType) {
            case CODE_ALL_ARTICLES:
                rowsDeleted = sqlDB.delete(TABLE_ARTICLES, selection,
                        selectionArgs);
                break;
            case CODE_ONE_ARTICLE:
                String id = uri.getLastPathSegment();

                rowsDeleted = sqlDB.delete(TABLE_ARTICLES,
                        COLUMN_ID + "=" + id
                                + " and " + selection,
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
    {
        int uriType = URI_MATCHER.match(uri);
        SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();
        int rowsDeleted = 0;
        switch (uriType)
        {
            case CODE_ALL_ARTICLES:
                rowsDeleted = sqlDB.delete(TABLE_ARTICLES, selection,
                        selectionArgs);
                break;
            case CODE_ONE_ARTICLE:
                String id = uri.getLastPathSegment();

                rowsDeleted = sqlDB.delete(TABLE_ARTICLES,
                        COLUMN_ID + "=" + id
                                + " and " + selection,
                        selectionArgs);

                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri)
    {
        return null;
    }
}
