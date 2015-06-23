package ergo_proxy.articlereader.ui.activity;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;

import ergo_proxy.articlereader.ui.db.ArticleItem;
import ergo_proxy.articlereader.ui.utils.IActtivityInteractionListener;
import ergo_proxy.articlereader.ui.utils.IDetailsFragmentInteraction;

/**
 * Created by ergo_proxy on 17.06.15.
 */

public class BaseActivity extends AppCompatActivity implements IActtivityInteractionListener,
        IDetailsFragmentInteraction
{
    @Override
    public void getArticleToAnotherFragment(Uri articleItem)
    {

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
