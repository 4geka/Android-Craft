package ergo_proxy.example_four.fragments;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import ergo_proxy.example_four.func.ItemCon;

/**
 * Created by ergo_proxy on 12.06.15.
 */

public class TwoItemFilter
{

	public static final int FILTER_NOT_FAVORITES = 1;
	public static final int FILTER_FAVORITES = 2;

	public interface IOnItemFilterListener
	{
		void onResult(List<ItemCon> result);
	}
	private List<ItemCon> sourceList;
	private AsyncFilter	asyncFilter;
	private IOnItemFilterListener listener;

	public TwoItemFilter(List<ItemCon> sourceList, IOnItemFilterListener listener)
	{
		this.sourceList	= sourceList;
		this.listener	= listener;
	}

	public void filter( String keyword, boolean isFavorite)
	{
		if(asyncFilter == null)
		{
			asyncFilter = new AsyncFilter(listener, sourceList, keyword, isFavorite);
		} else
		{

			asyncFilter.cancel(true);
			asyncFilter = new AsyncFilter(listener, sourceList, keyword, isFavorite);
		}
		asyncFilter.execute();
	}


	private static class AsyncFilter extends AsyncTask<Void, Void, List<ItemCon>>
	{
		private IOnItemFilterListener listener;
		private List<ItemCon> sourceList;
		private String keyword;
		private boolean isFavorite;

		public AsyncFilter(IOnItemFilterListener listener, List<ItemCon> sourceList, String keyword, boolean isFavorite)
		{
			this.listener	= listener;
			this.sourceList	= sourceList;
			this.keyword	= keyword;
			this.isFavorite	= isFavorite;
		}

		@Override
		protected List<ItemCon> doInBackground(Void... params)
		{
			List<ItemCon> result = new ArrayList<>();
			if( keyword != null && keyword.length() > 0 )
			{

				for(int i = 0; i < sourceList.size();i++)
				{
					boolean favoriteCondition = true;
					if( isFavorite)
					{
						favoriteCondition = sourceList.get(i).isFavorite();
					}

					if (favoriteCondition && sourceList.get(i).getTitle().toUpperCase()
							.contains(keyword.toUpperCase() ) )
					{
						result.add(sourceList.get(i));
					}
				}
			} else if(isFavorite)
			{
				for (int i = 0; i < sourceList.size(); i++)
				{
					if (sourceList.get(i).isFavorite())
					{
						result.add(sourceList.get(i));
					}
				}
			} else
			{
				result	= sourceList;
			}
			return result;
		}

		@Override
		protected void onPostExecute(List<ItemCon> result)
		{
			super.onPostExecute(result);
			if(listener != null)
			{
				listener.onResult( result );
			}
		}
	}
}
