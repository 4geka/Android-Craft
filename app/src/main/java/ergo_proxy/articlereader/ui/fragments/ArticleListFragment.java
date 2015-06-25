package ergo_proxy.articlereader.ui.fragments;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import ergo_proxy.articlereader.ui.utils.ArticleReaderAppController;
import ergo_proxy.articlereader.R;
import ergo_proxy.articlereader.ui.adapters.ListExAdapter;
import ergo_proxy.articlereader.ui.db.ArticleItem;
import ergo_proxy.articlereader.ui.db.ArticlesGroupItems;
import ergo_proxy.articlereader.ui.utils.IActtivityInteractionListener;
import ergo_proxy.articlereader.ui.utils.IDetailsFragmentInteraction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ergo_proxy.articlereader.ui.db.AppContentProvider.CONTENT_URI_ARTICLES;
import static ergo_proxy.articlereader.ui.db.AppSQLiteOpenHelper.ARTICLES_COLUMN_CATEGORY_ID;
import static ergo_proxy.articlereader.ui.db.AppSQLiteOpenHelper.ARTICLES_COLUMN_DESCRIPTION;
import static ergo_proxy.articlereader.ui.db.AppSQLiteOpenHelper.ARTICLES_COLUMN_OWN;
import static ergo_proxy.articlereader.ui.db.AppSQLiteOpenHelper.ARTICLES_COLUMN_PHOTO;
import static ergo_proxy.articlereader.ui.db.AppSQLiteOpenHelper.ARTICLES_COLUMN_PUBLISHED;
import static ergo_proxy.articlereader.ui.db.AppSQLiteOpenHelper.ARTICLES_COLUMN_UPDATE_AT;
import static ergo_proxy.articlereader.ui.db.AppSQLiteOpenHelper.COLUMN_ID;
import static ergo_proxy.articlereader.ui.db.AppSQLiteOpenHelper.COLUMN_TITLE;
import static ergo_proxy.articlereader.ui.db.AppSQLiteOpenHelper.TABLE_ARTICLES;

/**
 * Created by ergo_proxy on 19.06.15.
 */

public class ArticleListFragment extends Fragment implements Spinner.OnItemSelectedListener,
        ExpandableListView.OnChildClickListener, ListView.OnItemClickListener,
        IDetailsFragmentInteraction, LoaderManager.LoaderCallbacks<Cursor>, Button.OnClickListener
{
    private static final String ARGS_SELECTION = "args_selection";
    private static final String ARGS_SELECTION_ARGUMENTS = "args_selection_arguments";
    private static final int ARTICLES_LOADER = 1;
    public static final int INIT_LOADER = 0;
    private ExpandableListView expandableListView;
    private ListView customListView;
    private Spinner mSpinner;
    private ListExAdapter expandableAdapter;
    private List<ArticlesGroupItems> groupItemList = new ArrayList<>();
    private List<ArticleItem> articleItemList = new ArrayList<>();
    private SimpleCursorAdapter cursorAdapter;
    private EditText mEitTextFilter;
    private ImageButton imageButtonRefresh;
    private ImageButton imageButtonFilter;
    private Switch mSwitchOnlyMy;

    private final static String urlJsonArray = "http://editors.yozhik.sibext.ru/";
    private final static String apiKey = "7e56b73c3cbb6f63ae70041a4e592e87";

    public ArticleListFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        LinearLayout inflateView = (LinearLayout) inflater.inflate(R.layout.fragment_article_list,
                container, false);
        expandableListView = (ExpandableListView) inflateView.findViewById(R.id.exp_list);
        mSpinner = (Spinner) inflateView.findViewById(R.id.list_spinner);
        customListView = (ListView) inflateView.findViewById(R.id.def_list);
        mEitTextFilter = (EditText) inflateView.findViewById(R.id.filter);

        mSwitchOnlyMy = (Switch) inflateView.findViewById(R.id.switch_only_my);

        imageButtonFilter= (ImageButton) inflateView.findViewById(R.id.start_filter);
        imageButtonFilter.setOnClickListener(this);
        imageButtonRefresh = (ImageButton) inflateView.findViewById(R.id.refresh);
        imageButtonRefresh.setOnClickListener(this);
        Button addNewArticleButton = (Button) inflateView.findViewById(R.id.add_new_article);
        addNewArticleButton.setOnClickListener(this);

        expandableAdapter = new ListExAdapter(getActivity(), groupItemList, articleItemList);
        expandableListView.setAdapter(expandableAdapter);
        expandableListView.setOnChildClickListener(this);

        String[] from = new String[]{COLUMN_TITLE};
        int[] to = new int[]{R.id.textChild};

        getLoaderManager().initLoader(INIT_LOADER, null, this);
        cursorAdapter = new SimpleCursorAdapter(getActivity(), R.layout.child_view, null, from, to, 0);
        customListView.setAdapter(cursorAdapter);

        receiveArticlesFromServer();
        mSpinner.setOnItemSelectedListener(this);

        customListView.setOnItemClickListener(this);

        registerForContextMenu(customListView);
        return inflateView;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        switch (parent.getId())
        {
            case R.id.list_spinner:
                if (position == 1)
                {
                    customListView.setVisibility(View.VISIBLE);
                    expandableListView.setVisibility(View.GONE);
                } else
                {
                    customListView.setVisibility(View.GONE);
                    expandableListView.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        IActtivityInteractionListener listener = (IActtivityInteractionListener) getActivity();
        Uri uri = Uri.parse(CONTENT_URI_ARTICLES + "/"
                + id);
        listener.getArticleToAnotherFragment(uri);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        String[] projection;
        CursorLoader cursorLoader;
        String selection=null;
        String[] selectionArgs=null;

        switch (id)
        {
            case INIT_LOADER:
                break;
            case ARTICLES_LOADER:
                if (args!=null)
                {
                    selectionArgs= args.getStringArray(ARGS_SELECTION_ARGUMENTS);
                    selection=args.getString(ARGS_SELECTION);
                }
        }
        projection = new String[]{COLUMN_ID, COLUMN_TITLE, ARTICLES_COLUMN_UPDATE_AT};

        cursorLoader = new CursorLoader(getActivity(), CONTENT_URI_ARTICLES,
                projection, selection, selectionArgs, ARTICLES_COLUMN_UPDATE_AT);

        return cursorLoader;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.def_list)
        {
            MenuInflater inflater = getActivity().getMenuInflater();
            inflater.inflate(R.menu.menu_article_reader, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();
        final Uri uri = Uri.parse(CONTENT_URI_ARTICLES + "/" + info.id);

        ArticleReaderAppController.getInstance().addToRequestQueue(getJsonObjectRequest(info, uri));
        return true;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        switch (loader.getId())
        {
            case INIT_LOADER:
                cursorAdapter.swapCursor(data);
                break;
            case ARTICLES_LOADER:
                cursorAdapter.swapCursor(data);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
        switch (loader.getId())
        {
            case INIT_LOADER:
                cursorAdapter.swapCursor(null);
                break;
            case ARTICLES_LOADER:
                cursorAdapter.swapCursor(null);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_new_article:
                IActtivityInteractionListener listener = (IActtivityInteractionListener) getActivity();
                listener.getArticleToAnotherFragment(null);
                break;
            case R.id.refresh:
                mEitTextFilter.setText("");
                receiveArticlesFromServer();
                break;
            case R.id.start_filter:
                initFilter();
                break;
        }
    }

    private JsonObjectRequest getJsonObjectRequest(final AdapterView.AdapterContextMenuInfo info,
                                                   final Uri uri)
    {
        return new JsonObjectRequest(Request.Method.DELETE,
                urlJsonArray + "articles/" + info.id + ".json", null,
                getResponseForDeleteFromDB(uri), getErrorListenerForDeleteArticles())
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                return getStringStringMap();
            }
        };
    }

    private void receiveArticlesFromServer()
    {
        getActivity().getContentResolver().delete(CONTENT_URI_ARTICLES, null, null);

        StringRequest req = new StringRequest(urlJsonArray + "articles.json",
                getResponseForReceiveArticlesFormServer(), getErrorListener()) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getStringStringMap();
            }
        };

        ArticleReaderAppController.getInstance().addToRequestQueue(req);
    }

    private Response.ErrorListener getErrorListenerForDeleteArticles()
    {
        return new Response.ErrorListener()
        {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), getResources().getString(R.string.warning_delete),
                        Toast.LENGTH_SHORT).show();
                VolleyLog.e("Error: ", error.getMessage());

            }
        };
    }

    private Response.ErrorListener getErrorListener()
    {
        return new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                VolleyLog.e("Error: ", error.getMessage());
            }
        };
    }

    private Response.Listener<String> getResponseForReceiveArticlesFormServer()
    {
        return new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArrays = jsonObject.getJSONArray(TABLE_ARTICLES);
                    for (int i = 0; i < jsonArrays.length(); i++) {

                        JSONObject articles = (JSONObject) jsonArrays.get(i);

                        int id = articles.getInt("id");
                        String title = articles.getString(COLUMN_TITLE);
                        String description = articles.getString(ARTICLES_COLUMN_DESCRIPTION);
                        int category_id = articles.getInt(ARTICLES_COLUMN_CATEGORY_ID);
                        boolean own = articles.getBoolean(ARTICLES_COLUMN_OWN);
                        boolean isPhotoExists=articles.isNull(ARTICLES_COLUMN_PHOTO);
                        JSONObject jsonPhoto=null;
                        String uri="";

                        if (!isPhotoExists)
                        {
                            jsonPhoto = articles.getJSONObject(ARTICLES_COLUMN_PHOTO);
                            uri = jsonPhoto.getString("url");
                        }

                        ContentValues values = new ContentValues();
                        values.put(COLUMN_ID, id);
                        values.put(COLUMN_TITLE, title);
                        values.put(ARTICLES_COLUMN_DESCRIPTION, description);
                        values.put(ARTICLES_COLUMN_CATEGORY_ID, category_id);
                        values.put(ARTICLES_COLUMN_OWN, own ? 1 : 0);
                        values.put(ARTICLES_COLUMN_PHOTO,uri);

                        getActivity().getContentResolver().insert(CONTENT_URI_ARTICLES, values);
                    }
                    VolleyLog.v("Response:%n %s", response);
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        };
    }

    private Response.Listener<JSONObject> getResponseForDeleteFromDB(final Uri uri)
    {
        return new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                VolleyLog.v("Response:%n %s", response);
                getActivity().getContentResolver().delete(uri, null, null);
            }
        };
    }

    private Map<String, String> getStringStringMap()
    {
        Map<String, String> params = new HashMap<String, String>();
        params.put("Content-Type", "application/json");
        params.put("Authorization", "Token token=" + apiKey);
        return params;
    }

    private void initFilter()
    {
        List<String> selectionArgs = new ArrayList<>();
        StringBuilder filterSelection = new StringBuilder();
        if (mSwitchOnlyMy.isChecked()) {
            filterSelection.append(ARTICLES_COLUMN_OWN);
            filterSelection.append("= ? ");
            selectionArgs.add("1");
        }
        if (false)
        {
            if (filterSelection.length() > 0)
            {
                filterSelection.append(" AND ");
            }
            filterSelection.append(ARTICLES_COLUMN_PUBLISHED);
            filterSelection.append("= ?");
            selectionArgs.add("1");
        }
        if (!TextUtils.isEmpty(mEitTextFilter.getText()))
        {
            if (filterSelection.length() > 0)
            {
                filterSelection.append(" AND ");
            }
            filterSelection.append(COLUMN_TITLE);
            filterSelection.append(" LIKE ?");
            selectionArgs.add(mEitTextFilter.getText()+ "%");

        }

        Bundle args = null;
        if (filterSelection.length() > 0)
        {
            args = new Bundle();
            args.putString(ARGS_SELECTION, filterSelection.toString());
            args.putStringArray(ARGS_SELECTION_ARGUMENTS
                    , selectionArgs.toArray(new String[selectionArgs.size()]));
        }

        getLoaderManager().restartLoader(ARTICLES_LOADER, args, this);
    }
    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
                                int childPosition, long id)
    {
        return false;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {

    }

    @Override
    public void deleteArticleItem(ArticleItem articleItem)
    {


    }

    @Override
    public void updateArticleItem(ArticleItem articleItem)
    {

    }

    @Override
    public void addArticleItem()
    {
        receiveArticlesFromServer();
    }

}