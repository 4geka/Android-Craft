package ergo_proxy.example_four.fragments;

import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

import ergo_proxy.example_four.func.ItemCon;

/**
 * Created by ergo_proxy on 12.06.15.
 */

public class ItemFilter extends Filter
{

	public interface IOnItemFilterListener
	{
		void onResult(List<ItemCon> result);
	}
	private IOnItemFilterListener listener;
	private List<ItemCon> sourceList;

	public ItemFilter(List<ItemCon> sourceList, IOnItemFilterListener listener)
	{
		this.listener	= listener;
		this.sourceList	= sourceList;
	}

	@Override
	protected FilterResults performFiltering(CharSequence constraint)
	{
		FilterResults results = new FilterResults();
		if(constraint != null && constraint.length() > 0 )
		{
			ArrayList<ItemCon> filterList = new ArrayList<ItemCon>();

			for(int i = 0; i < sourceList.size();i++)
			{
				if((sourceList.get(i).getTitle().toUpperCase())
						.contains(constraint.toString().toUpperCase()))
				{
					filterList.add(sourceList.get(i));
				}
			}
			results.count	= filterList.size();
			results.values	= filterList;
		}else{
			results.count	= sourceList.size();
			results.values	= sourceList;
		}
		return results;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void publishResults(CharSequence constraint,
								  FilterResults results)
	{
		if(listener != null)
		{
			listener.onResult( (List<ItemCon>)results.values );
		}
	}
}
