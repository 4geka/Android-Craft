package ergo_proxy.articlereader.ui.db;

import android.content.ContentValues;
import android.database.Cursor;

import static ergo_proxy.articlereader.ui.db.AppSQLiteOpenHelper.COLUMN_ID;
import static ergo_proxy.articlereader.ui.db.AppSQLiteOpenHelper.COLUMN_TITLE;

/**
 * Created by ergo_proxy on 19.06.15.
 */

public class ArticlesGroupItems
{
    private int _id;
    private String mTitle;


    public ArticlesGroupItems(int id, String title)
    {
        this._id = id;
        this.mTitle = title;
    }

    public ArticlesGroupItems(String title)
    {
        this (-1,title);
    }
    public ContentValues buildContentValues()
    {
        ContentValues cv = new ContentValues();
        if (_id >=0 )
        {
            cv.put(COLUMN_ID,_id);
        }
        cv.put(COLUMN_TITLE, mTitle);

        return cv;
    }

    public static ArticlesGroupItems fromCursor(Cursor c)
    {
        int idColId=c.getColumnIndex(COLUMN_ID);
        int titleColId=c.getColumnIndex(COLUMN_TITLE);

        return new ArticlesGroupItems(
                c.getInt(idColId),
                c.getString(titleColId));
    }

    public int get_id()
    {
        return _id;
    }

    public void set_id(int _id)
    {
        this._id = _id;
    }

    public String getmTitle()
    {
        return mTitle;
    }

    public void setmTitle(String mTitle)
    {
        this.mTitle = mTitle;
    }
}
