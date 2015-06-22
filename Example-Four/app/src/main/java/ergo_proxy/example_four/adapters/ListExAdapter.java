package ergo_proxy.example_four.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import ergo_proxy.example_four.R;
import ergo_proxy.example_four.func.Collection;
import ergo_proxy.example_four.func.ItemCon;

/**
 * Created by ergo_proxy on 12.06.15.
 */

public class ListExAdapter extends BaseExpandableListAdapter
{

	private Context context;
	private Collection collection;
	private LayoutInflater inflater;

	public ListExAdapter(Context context, Collection collection)
	{
		this.collection = collection;
		this.context = context;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getGroupCount()
	{
		return collection.getGroupsCount();
	}

	@Override
	public int getChildrenCount(int groupPosition)
	{
		return collection.getGroupItemsCount(groupPosition);
	}

	@Override
	public Object getGroup(int groupPosition)
	{
		return collection.getGroup(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition)
	{
		return collection.getItem(groupPosition, childPosition);
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
		return true;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
	{
		GroupViewHolder holder = null;

		if (convertView == null)
		{
			convertView = inflater.inflate(R.layout.listview_view, parent, false);
			holder  = new GroupViewHolder();
			holder.tvTitle = (TextView) convertView.findViewById(R.id.listview_title);
			holder.cbFavoretes = (CheckBox) convertView.findViewById(R.id.listview_checkbox);
			convertView.setTag(holder);
		} else
		{
			holder  = (GroupViewHolder)  convertView.getTag();
		}

		if (isExpanded)
		{

		}
		else
		{

		}

		ItemCon currentItem = (ItemCon)getGroup(groupPosition);
		holder.tvTitle.setText(currentItem.getTitle());
		holder.cbFavoretes.setChecked(currentItem.isFavorite());

		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
	{
		ItemViewHolder holder = null;

		if (convertView == null)
		{
			convertView = inflater.inflate(R.layout.listview_view, parent, false);
			holder = new ItemViewHolder();
			holder.tvTitle = (TextView) convertView.findViewById(R.id.listview_title);
			holder.cbFavoretes = (CheckBox) convertView.findViewById(R.id.listview_checkbox);
			convertView.setTag(holder);
		} else
		{
			holder  = (ItemViewHolder)  convertView.getTag();
		}

		ItemCon currentItem = (ItemCon)getChild(groupPosition, childPosition);
		holder.tvTitle.setText(currentItem.getTitle());
		holder.cbFavoretes.setChecked(currentItem.isFavorite());

		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition)
	{
		return false;
	}

	private static class GroupViewHolder
	{
		TextView tvTitle;
		CheckBox cbFavoretes;
	}

	private static class ItemViewHolder
	{
		TextView tvTitle;
		CheckBox cbFavoretes;
	}

}
