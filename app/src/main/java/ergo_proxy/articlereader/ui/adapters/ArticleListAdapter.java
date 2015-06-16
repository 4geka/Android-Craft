package ergo_proxy.articlereader.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import ergo_proxy.articlereader.R;
import ergo_proxy.articlereader.ui.fragments.ItemsArticleFilter;
import ergo_proxy.articlereader.prog.Collection;
import ergo_proxy.articlereader.prog.ItemCon;

/**
 * Created by ergo_proxy on 12.06.15.
 */

public class ArticleListAdapter extends BaseAdapter
{

	private Context context;
	private Collection collection;
	private LayoutInflater inflater;
	private List<ItemCon> items;
	private ItemsArticleFilter filter;

	public ArticleListAdapter(Context context, Collection collection)
	{
		this.collection = collection;
		this.context = context;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		items = collection.getItems();
	}

	@Override
	public int getCount()
	{
		return collection.getItemsCount();
	}

	@Override
	public Object getItem(int location)
	{
		return items.get(location);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder;

		if (convertView == null)
		{
			convertView = inflater.inflate(R.layout.listview_view, parent, false);
			holder  = new ViewHolder();
			holder.tvTitle  = (TextView) convertView.findViewById(R.id.listview_title);
			holder.cbFavoretes	= (CheckBox) convertView.findViewById(R.id.listview_checkbox);
			convertView.setTag(holder);
		} else
		{
			holder  = (ViewHolder)  convertView.getTag();
		}

		ItemCon currentItem = (ItemCon)getItem(position);
		holder.tvTitle.setText(currentItem.getTitle());
		holder.cbFavoretes.setChecked(currentItem.isFavorite());

		return convertView;
	}

	public ItemsArticleFilter getFilter()
	{
		if( filter == null)
		{
			filter = new ItemsArticleFilter(collection.getItems(), new ItemsArticleFilter.IOnItemFilterListener()
			{
				@Override
				public void onResult(List<ItemCon> result)
				{
					items	= result;
					notifyDataSetChanged();
				}
			});
		}
		return filter;
	}

	private static class ViewHolder
	{
		TextView    tvTitle;
		CheckBox 	cbFavoretes;
	}
}
