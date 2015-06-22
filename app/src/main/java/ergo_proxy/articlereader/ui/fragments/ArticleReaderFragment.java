package ergo_proxy.articlereader.ui.fragments;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import ergo_proxy.articlereader.ui.utils.ArticleReaderAppController;
import ergo_proxy.articlereader.R;
import ergo_proxy.articlereader.ui.activity.ArticleReaderActivity;
import ergo_proxy.articlereader.ui.db.ArticleItem;
import ergo_proxy.articlereader.ui.db.ArticlesGroupItems;
import ergo_proxy.articlereader.ui.utils.IActtivityInteractionListener;
import ergo_proxy.articlereader.ui.utils.IDetailsFragmentInteraction;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ergo_proxy.articlereader.ui.db.AppContentProvider.CONTENT_URI_ARTICLES;
import static ergo_proxy.articlereader.ui.db.AppSQLiteOpenHelper.ARTICLES_COLUMN_CATEGORY_ID;
import static ergo_proxy.articlereader.ui.db.AppSQLiteOpenHelper.ARTICLES_COLUMN_CREATE_AT;
import static ergo_proxy.articlereader.ui.db.AppSQLiteOpenHelper.ARTICLES_COLUMN_DESCRIPTION;
import static ergo_proxy.articlereader.ui.db.AppSQLiteOpenHelper.ARTICLES_COLUMN_OWN;
import static ergo_proxy.articlereader.ui.db.AppSQLiteOpenHelper.ARTICLES_COLUMN_PUBLISHED;
import static ergo_proxy.articlereader.ui.db.AppSQLiteOpenHelper.ARTICLES_COLUMN_UPDATE_AT;
import static ergo_proxy.articlereader.ui.db.AppSQLiteOpenHelper.COLUMN_ID;
import static ergo_proxy.articlereader.ui.db.AppSQLiteOpenHelper.COLUMN_TITLE;

/**
 * Created by ergo_proxy on 19.06.15.
 */

public class ArticleReaderFragment extends Fragment implements IActtivityInteractionListener,
        View.OnClickListener,
        Spinner.OnItemClickListener,
        IDetailsFragmentInteraction
{
    private TextView textTitle;
    private TextView textDescription;
    private Spinner spinnerCategory;
    private ImageView imagePhoto;
    private Switch switchPublished;
    private Button buttonView;
    private Button buttonEdit;
    private Button buttonSave;
    private Uri todoUri;
    private final static String urlJsonArray = "http://editors.yozhik.sibext.ru/categories.json";
    private final static String urlJsonArrayInsert = "http://editors.yozhik.sibext.ru/articles.json";
    private final static String apiKey="7e56b73c3cbb6f63ae70041a4e592e87";
    private String jsonResponse;
    private List<ArticlesGroupItems> listCategoty_id;
    private JSONObject mArticleView;
    private static String TAG = ArticleReaderActivity.class.getSimpleName();


    public ArticleReaderFragment()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        LinearLayout inflateView = (LinearLayout) inflater.inflate(R.layout.fragment_article_detail,
                container, false);

        textTitle = (TextView) inflateView.findViewById(R.id.editText);
        textDescription = (TextView) inflateView.findViewById(R.id.editText2);
        spinnerCategory = (Spinner) inflateView.findViewById(R.id.spinner);
        imagePhoto = (ImageView) inflateView.findViewById(R.id.imageViewThumb);
        switchPublished = (Switch) inflateView.findViewById(R.id.switch1);
        buttonView = (Button) inflateView.findViewById(R.id.button_view);
        buttonEdit = (Button) inflateView.findViewById(R.id.button_edit);
        buttonSave = (Button) inflateView.findViewById(R.id.button_save);

        buttonView.setOnClickListener(this);
        buttonEdit.setOnClickListener(this);
        buttonSave.setOnClickListener(this);

        if (listCategoty_id==null)
        {
            listCategoty_id=new ArrayList<>();
            getCategory_ids();
        }

        return inflateView;
    }

    private void request()
    {
        JsonObjectRequest req = new JsonObjectRequest(urlJsonArrayInsert, mArticleView,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        int id = 0;
                        String title = "";
                        String description = "";
                        int category_id = 0;
                        try
                        {
                                JSONObject jsonObject = response.getJSONObject("article");
                                id = jsonObject.getInt("id");
                                title = jsonObject.getString("title");
                                description = jsonObject.getString("description");
                                category_id = jsonObject.getInt("category_id");

                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                        ContentValues values = new ContentValues();
                        values.put(COLUMN_ID, id);
                        values.put(COLUMN_TITLE, title);
                        values.put(ARTICLES_COLUMN_DESCRIPTION, description);
                        values.put(ARTICLES_COLUMN_CATEGORY_ID, category_id);
                        values.put(ARTICLES_COLUMN_CREATE_AT, "");
                        values.put(ARTICLES_COLUMN_UPDATE_AT, "");
                        values.put(ARTICLES_COLUMN_PUBLISHED, true);
                        getActivity().getContentResolver().insert(CONTENT_URI_ARTICLES, values);
                    }
                }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                VolleyLog.e("Error: ", error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Token token=" + apiKey);
                return params;
            }
        };
        ArticleReaderAppController.getInstance().addToRequestQueue(req);
    }

    @Override
    public void getArticleToAnotherFragment(Uri uri)
    {
        // Deprecated methods

        HttpClient hc = new DefaultHttpClient();
        String message;

        HttpPost p = new HttpPost(urlJsonArrayInsert);
        mArticleView = new JSONObject();
        try
        {

            mArticleView.put("title", textTitle.getText().toString());
            mArticleView.put("description", textDescription.getText().toString());
            mArticleView.put("published", true);
            mArticleView.put("category_id", spinnerCategory.getSelectedItemId());

        } catch (Exception ex)
        {

        }
        todoUri=uri;
        fillData(todoUri);

    }

    private void fillData(Uri uri)
    {
        String[] projection = { COLUMN_TITLE,
                ARTICLES_COLUMN_DESCRIPTION,
                ARTICLES_COLUMN_OWN};
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null,
                null);
        if (cursor != null)
        {
            cursor.moveToFirst();

            textTitle.setText(cursor.getString(cursor
                    .getColumnIndexOrThrow(COLUMN_TITLE)));
            textDescription.setText(cursor.getString(cursor
                    .getColumnIndexOrThrow(ARTICLES_COLUMN_DESCRIPTION)));
            int publ=cursor.getInt(cursor
                    .getColumnIndexOrThrow(ARTICLES_COLUMN_OWN));
            switchPublished.setChecked(publ>0);


            cursor.close();
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.button_view:

                buttonSave.setVisibility(View.INVISIBLE);
                spinnerCategory.setVisibility(View.INVISIBLE);
                textTitle.setEnabled(false);
                textDescription.setEnabled(false);
                spinnerCategory.setEnabled(false);
                break;
            case R.id.button_edit:
                spinnerCategory.setVisibility(View.VISIBLE);
                buttonSave.setVisibility(View.VISIBLE);
                textTitle.setEnabled(true);
                textDescription.setEnabled(true);
                spinnerCategory.setEnabled(true);
                break;
            case R.id.button_save:

                if (mArticleView==null)
                {
                    request();
                } else
                {

                }

                spinnerCategory.setVisibility(View.INVISIBLE);
                buttonSave.setVisibility(View.INVISIBLE);
                textTitle.setEnabled(false);
                textDescription.setEnabled(false);
                spinnerCategory.setEnabled(false);
                break;
        }
    }

    private void getCategory_ids()
    {
        StringRequest req = new StringRequest(urlJsonArray, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArrays = jsonObject.getJSONArray("categories");
                    for (int i = 0; i < jsonArrays.length(); i++)
                    {
                        JSONObject category = (JSONObject) jsonArrays.get(i);

                        int id = category.getInt("id");
                        String title = category.getString("title");
                        ArticlesGroupItems articlesGroupItems = new ArticlesGroupItems(id,title);
                        listCategoty_id.add(articlesGroupItems);
                    }
                    String[] strings=new String[listCategoty_id.size()];
                    for (int i=0;i<listCategoty_id.size();i++)
                    {
                        strings[i]=listCategoty_id.get(i).getmTitle();
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                            android.R.layout.simple_spinner_item
                            , strings);
                    spinnerCategory.setAdapter(adapter);
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
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Token token=" + apiKey);
                return params;
            }
        };
        ArticleReaderAppController.getInstance().addToRequestQueue(req);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        switch (parent.getId())
        {
            case R.id.spinner:

                break;
        }
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
}
