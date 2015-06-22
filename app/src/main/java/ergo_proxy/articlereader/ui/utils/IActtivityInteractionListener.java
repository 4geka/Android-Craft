package ergo_proxy.articlereader.ui.utils;

import android.net.Uri;

import ergo_proxy.articlereader.ui.db.ArticleItem;

import java.net.URI;

/**
 * Created by ergo_proxy on 18.06.15.
 */

public interface IActtivityInteractionListener {
	void getArticleToAnotherFragment(Uri articleItem);
}
