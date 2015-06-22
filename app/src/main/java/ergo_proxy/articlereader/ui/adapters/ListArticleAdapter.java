package ergo_proxy.articlereader.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import ergo_proxy.articlereader.R;
import ergo_proxy.articlereader.ui.db.ArticleItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ergo_proxy on 19.06.15.
 */

public class ListArticleAdapter extends ArrayAdapter<ArticleItem>
{
    private List<ArticleItem> articleItemList = new ArrayList<>();
    private Context mContext;
    private LayoutInflater inflater;
    public ListArticleAdapter(Context context, List<ArticleItem> articles)
    {
        super(context, R.layout.group_view,articles);
        mContext = context;
        articleItemList = articles;
        inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public ArticleItem getItem(int position)
    {
        return articleItemList.get(position);
    }

    @Override
    public int getCount()
    {
        return articleItemList.size();
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    public void setArticleItemList(List<ArticleItem> articleItemList)
    {
        this.articleItemList = articleItemList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {

            convertView = inflater.inflate(R.layout.child_view, null);
        }

        TextView textChild = (TextView) convertView.findViewById(R.id.textChild);
        textChild.setText(articleItemList.get(position).getmTitle());

        return convertView;
    }

}
