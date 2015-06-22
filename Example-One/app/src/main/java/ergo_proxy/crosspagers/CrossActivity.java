package ergo_proxy.crosspagers;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ergo_proxy.crosspagers.Util.CrossAnimation;

public class CrossActivity extends FragmentActivity
{

   /* private TabHost mTabHost;
    private ViewPager mViewPager;
    private TabsAdapter mTabsAdapter;
    Spinner spinner;
*/
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cross);
        Button button1=(Button)findViewById(R.id.button1);
        Button button2=(Button)findViewById(R.id.button2);

        button1.setText(getString(R.string.android_fragment));
        button2.setText(getString(R.string.android));
        button1.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                CrossAnimation.startActivity(CrossActivity.this, new Intent(CrossActivity.this, AndroidFragment.class));
            }
        });
        button2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CrossActivity.this,Android.class));
            }
        });
    }
}
