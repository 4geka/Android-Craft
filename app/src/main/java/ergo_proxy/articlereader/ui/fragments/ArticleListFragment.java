package ergo_proxy.articlereader.ui.fragments;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import ergo_proxy.articlereader.ui.utils.ArticleReaderAppController;
import ergo_proxy.articlereader.R;
import ergo_proxy.articlereader.ui.activity.ArticleReaderActivity;
import ergo_proxy.articlereader.ui.adapters.ListExAdapter;
import ergo_proxy.articlereader.ui.db.AppSQLiteOpenHelper;
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
import static ergo_proxy.articlereader.ui.db.AppSQLiteOpenHelper.COLUMN_ID;
import static ergo_proxy.articlereader.ui.db.AppSQLiteOpenHelper.COLUMN_TITLE;

/**
 * Created by ergo_proxy on 19.06.15.
 */

public class ArticleListFragment extends Fragment implements Spinner.OnItemSelectedListener,
        ExpandableListView.OnChildClickListener, ListView.OnItemClickListener,
        IDetailsFragmentInteraction, LoaderManager.LoaderCallbacks<Cursor>, Button.OnClickListener
{
    private ExpandableListView expandableListView;
    private ListView customListView;
    private Spinner mSpinner;
    private ListExAdapter expandableAdapter;
    private TextView mTextView;
    private List<ArticlesGroupItems> articlesGroupItemsList = new ArrayList<>();
    private List<ArticleItem> articleItemList = new ArrayList<>();
    private SimpleCursorAdapter cursorAdapter;
    private EditText mEitTextFilter;
    private String jsonResponse;
    private static String TAG = ArticleReaderActivity.class.getSimpleName();
    private final static String urlJsonArray = "http://editors.yozhik.sibext.ru/";
    private final static String apiKey="7e56b73c3cbb6f63ae70041a4e592e87";

    public ArticleListFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        LinearLayout inflateView = (LinearLayout) inflater.inflate(R.layout.fragment_article_list,
                container, false);
        expandableListView = (ExpandableListView) inflateView.findViewById(R.id.exp_list);
        mSpinner = (Spinner) inflateView.findViewById(R.id.list_spinner);
        customListView = (ListView) inflateView.findViewById(R.id.def_list);
        mEitTextFilter = (EditText) inflateView.findViewById(R.id.filter);
        mEitTextFilter.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override public void beforeTextChanged(CharSequence s, int start, int count,
                                                    int after) { }

            @Override public void afterTextChanged(Editable s) { }
        });
        ImageButton imageButtonRefresh= (ImageButton) inflateView.findViewById(R.id.refresh);
        imageButtonRefresh.setOnClickListener(this);
        Button addNewArticleButton= (Button) inflateView.findViewById(R.id.add_new_article);

        expandableAdapter = new ListExAdapter(getActivity(), articlesGroupItemsList, articleItemList);
        expandableListView.setAdapter(expandableAdapter);
        expandableListView.setOnChildClickListener(this);

        String[] from = new String[]{AppSQLiteOpenHelper.COLUMN_TITLE};
        int[] to = new int[]{R.id.textChild};

        getLoaderManager().initLoader(0, null, this);

        cursorAdapter = new SimpleCursorAdapter(getActivity(), R.layout.child_view, null,
                from, to, 0);
        customListView.setAdapter(cursorAdapter);
        request();
        mSpinner.setOnItemSelectedListener(this);
        customListView.setOnItemClickListener(this);

        registerForContextMenu(customListView);
        return inflateView;
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
                                int childPosition, long id)
    {
        IActtivityInteractionListener listener = (IActtivityInteractionListener) getActivity();
        ArticleItem articleItem = expandableAdapter.getChild(groupPosition, childPosition);
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        switch (parent.getId()) {
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
    public void onNothingSelected(AdapterView<?> parent)
    {
    }

    private void request()
    {
        getActivity().getContentResolver().delete(CONTENT_URI_ARTICLES, null, null);
        StringRequest req = new StringRequest(urlJsonArray+"articles.json", new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArrays= jsonObject.getJSONArray("articles");
                    for (int i = 0; i < jsonArrays.length(); i++)
                    {
                        JSONObject articles = (JSONObject) jsonArrays.get(i);

                        int id = articles.getInt("id");
                        String title = articles.getString("title");
                        String description = articles.getString("description");
                        int category_id=articles.getInt("category_id");
                        boolean own=articles.getBoolean("own");

                        ContentValues values = new ContentValues();
                        values.put(COLUMN_ID, id);
                        values.put(COLUMN_TITLE, title);
                        values.put(ARTICLES_COLUMN_DESCRIPTION, description);
                        values.put(ARTICLES_COLUMN_CATEGORY_ID, category_id);
                        values.put(ARTICLES_COLUMN_OWN, own ? 1:0);

                        getActivity().getContentResolver().insert(CONTENT_URI_ARTICLES, values);
                    }


                    VolleyLog.v("Response:%n %s", response);


                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                VolleyLog.e("Error: ", error.getMessage());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/json");
                params.put("Authorization","Token token="+ apiKey);
                return params;
            }
        };

        ArticleReaderAppController.getInstance().addToRequestQueue(req);
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
    public void deleteArticleItem(ArticleItem articleItem)
    {

    }

    @Override
    public void updateArticleItem(ArticleItem articleItem)
    {

    }

    @Override
    public void addArticleItem(Uri articleItem)
    {

    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        String[] projection = { COLUMN_ID, COLUMN_TITLE };
        CursorLoader cursorLoader = new CursorLoader(getActivity(),
                CONTENT_URI_ARTICLES,
                projection, null, null, null);
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
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                        .getMenuInfo();
                Uri uri = Uri.parse(CONTENT_URI_ARTICLES + "/" + info.id);
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.DELETE,
                urlJsonArray+"articles/"+info.id+".json",null, new Response.Listener<JSONObject>(

        ) {
            @Override
            public void onResponse(JSONObject response)
            {
                VolleyLog.v("Response:%n %s", response);
                request();
            }
        },new Response.ErrorListener()
        {

            @Override
            public void onErrorResponse(VolleyError error)
            {
                VolleyLog.e("Error: ", error.getMessage());

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/json");
                params.put("Authorization","Token token="+ apiKey);
                return params;
            }
        };
        ArticleReaderAppController.getInstance().addToRequestQueue(jsonObjectRequest);
        return true;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        cursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
        cursorAdapter.swapCursor(null);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.add_new_article:

                break;
            case R.id.refresh:
                mEitTextFilter.setText("");
                request();
                break;
        }
    }
}