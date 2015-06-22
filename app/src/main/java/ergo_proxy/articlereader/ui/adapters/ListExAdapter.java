package ergo_proxy.articlereader.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import ergo_proxy.articlereader.R;
import ergo_proxy.articlereader.ui.db.ArticleItem;
import ergo_proxy.articlereader.ui.db.ArticlesGroupItems;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ergo_proxy on 19.06.15.
 */

public class ListExAdapter extends BaseExpandableListAdapter
{
    private List<ArticlesGroupItems> articlesGroupItemsList = new ArrayList<>();
    private List<ArticleItem> articleItemList = new ArrayList<>();
    private List<List<ArticleItem>> arrayGroupsAndArticles=new ArrayList<>();
    private LayoutInflater inflater;
    private Context mContext;

    public ListExAdapter(Context context, List<ArticlesGroupItems> groups, List<ArticleItem> articles)
    {
        mContext = context;
        articlesGroupItemsList = groups;
        articleItemList = articles;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        connectGroupsWithArticles(groups, articles);
    }

    @Override
    public int getGroupCount()
    {
        return articlesGroupItemsList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition)
    {
        return arrayGroupsAndArticles.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition)
    {
        return articlesGroupItemsList.get(groupPosition);
    }

    @Override
    public ArticleItem getChild(int groupPosition, int childPosition)
    {
        return arrayGroupsAndArticles.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition)
    {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition)
    {
        return childPosition;
    }

    @Override
    public boolean hasStableIds()
    {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                             ViewGroup parent)
    {

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.group_view, null);
        }

        TextView textGroup = (TextView) convertView.findViewById(R.id.textGroup);
        textGroup.setText(articlesGroupItemsList.get(groupPosition).getmTitle());

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.child_view, null);
        }
        TextView textChild = (TextView) convertView.findViewById(R.id.textChild);
        textChild.setText(arrayGroupsAndArticles.get(groupPosition).get(childPosition).getmTitle());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        return true;
    }

    private void connectGroupsWithArticles(List<ArticlesGroupItems> groups, List<ArticleItem> articles)
    {
        for (int i=0;i<groups.size();i++)
        {
            List<ArticleItem> currentList=new ArrayList<>();
            for (int j=0;j<articles.size();j++)
            {
                if (groups.get(i).get_id()==articles.get(j).getmCategory_id())
                {
                    currentList.add(articles.get(j));
                }
            }
            if (currentList.size()>0)
            {
                arrayGroupsAndArticles.add(currentList);
            }
        }
    }
}
