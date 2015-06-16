package ergo_proxy.articlereader.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import ergo_proxy.articlereader.ui.fragments.DetailReaderFragment;
import ergo_proxy.articlereader.prog.Collection;
import ergo_proxy.articlereader.prog.ItemCon;

/**
 * Created by ergo_proxy on 12.06.15.
 */

public class ArticlePagerAdapter extends FragmentPagerAdapter
{

	private Collection collection;
	private FragmentManager fragmentManager;

	public ArticlePagerAdapter(FragmentManager fragmentManager, Collection collection)
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
			return DetailReaderFragment.newInstance(item.getTitle(), item.getUrl(), item.getImageId(), item.isFavorite());
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
