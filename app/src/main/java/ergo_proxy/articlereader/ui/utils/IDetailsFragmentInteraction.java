package ergo_proxy.articlereader.ui.utils;

import android.net.Uri;

import ergo_proxy.articlereader.ui.db.ArticleItem;

/**
 * Created by ergo_proxy on 19.06.15.
 */

public interface IDetailsFragmentInteraction {
    void deleteArticleItem(ArticleItem articleItem);
    void updateArticleItem(ArticleItem articleItem);
    void addArticleItem(Uri articleItem);
}
