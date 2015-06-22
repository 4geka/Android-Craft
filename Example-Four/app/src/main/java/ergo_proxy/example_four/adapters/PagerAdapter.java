package ergo_proxy.example_four.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import ergo_proxy.example_four.fragments.DetFragment;
import ergo_proxy.example_four.func.Collection;
import ergo_proxy.example_four.func.ItemCon;

/**
 * Created by ergo_proxy on 12.06.15.
 */

public class PagerAdapter extends FragmentPagerAdapter
{

	private Collection collection;
	private FragmentManager fragmentManager;

	public PagerAdapter(FragmentManager fragmentManager, Collection collection)
	{
		super(fragmentManager);
		this.fragmentManager = fragmentManager;
		this.collection = collection;
	}

	@Override
	public Fragment getItem(int position)
	{
		ItemCon item = collection.getItem(position);
		if(item != null)
		{
			return DetFragment.newInstance(item.getTitle(), item.getUrl(), item.getImageId(), item.isFavorite());
		}
		return null;
	}

	@Override
	public int getCount()
	{
		if (collection != null)
		{
			return collection.getItemsCount();
		}
		return 0;
	}
}
