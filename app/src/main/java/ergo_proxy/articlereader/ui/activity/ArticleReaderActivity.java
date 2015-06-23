package ergo_proxy.articlereader.ui.activity;

import android.net.Uri;
import android.os.Bundle;

import ergo_proxy.articlereader.R;
import ergo_proxy.articlereader.ui.fragments.ArticleReaderFragment;
import ergo_proxy.articlereader.ui.fragments.ArticleListFragment;
import ergo_proxy.articlereader.ui.utils.IActtivityInteractionListener;
import ergo_proxy.articlereader.ui.utils.IDetailsFragmentInteraction;
import ergo_proxy.articlereader.ui.db.ArticleItem;


public class ArticleReaderActivity extends FragmentActivity implements IActtivityInteractionListener,
        IDetailsFragmentInteraction
{
	public static final String LIST_FRAGMENT = "list_fragment";
	public static final String ARTICLE_FRAGMENT = "article_fragment";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_article_reader);

		if (savedInstanceState == null)
		{
			getSupportFragmentManager().beginTransaction()
					.add(R.id.headlines_fragment,
							new ArticleListFragment(), LIST_FRAGMENT)
					.add(R.id.article_fragment,
							new ArticleReaderFragment(), ARTICLE_FRAGMENT).commit();
		}
	}

	@Override
	public void getArticleToAnotherFragment(Uri uri)
	{
		IActtivityInteractionListener iClickListener=
				(IActtivityInteractionListener)getSupportFragmentManager().
						findFragmentByTag(ARTICLE_FRAGMENT);
		iClickListener.getArticleToAnotherFragment(uri);
	}

	@Override
	public void deleteArticleItem(ArticleItem articleItem)
	{

	}

	@Override
	public void updateArticleItem(ArticleItem articleItem)
	{
		IDetailsFragmentInteraction iClickListener=
				(IDetailsFragmentInteraction)getSupportFragmentManager().
						findFragmentByTag(LIST_FRAGMENT);
		iClickListener.updateArticleItem(articleItem);
	}

	@Override
	public void addArticleItem(Uri articleItem)
	{

	}
}