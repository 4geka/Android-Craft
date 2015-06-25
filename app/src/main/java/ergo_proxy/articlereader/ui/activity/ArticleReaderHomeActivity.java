package ergo_proxy.articlereader.ui.activity;

/**
 * Created by Ergo-Proxy on 23.06.2015.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;

import ergo_proxy.articlereader.R;


public class ArticleReaderHomeActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_home_activity);
        new Handler().postDelayed(new Runnable()
        {
            public void run()
            {
                Intent i = new Intent(ArticleReaderHomeActivity.this, ArticleReaderActivity.class);
                startActivity(i);
                finish();
            }
        }, 4 * 1000);
    }
}
