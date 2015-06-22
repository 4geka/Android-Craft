package ergo_proxy.examplethree;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 * Created by Ergo-Proxy on 04.06.2015.
 */

public class ChoseActivity extends Activity implements View.OnClickListener
{
    private int idContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chose);

        idContainer=getIntent().getIntExtra(getString(R.string.id_container),0);
        findViewById(R.id.first).setOnClickListener(this);
        findViewById(R.id.second).setOnClickListener(this);
        findViewById(R.id.third).setOnClickListener(this);
        findViewById(R.id.fourth).setOnClickListener(this);
        findViewById(R.id.fifth).setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        Intent homeIntent = new Intent(this, MainActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        homeIntent.putExtra(getString(R.string.id_container), idContainer);
        switch (v.getId())
        {
            case R.id.first:
                homeIntent.putExtra(getString(R.string.id_fragment), 1);
                break;
            case R.id.second:
                homeIntent.putExtra(getString(R.string.id_fragment), 2);
                break;
            case R.id.third:
                homeIntent.putExtra(getString(R.string.id_fragment), 3);
                break;
            case R.id.fourth:
                homeIntent.putExtra(getString(R.string.id_fragment), 4);
                break;
            case R.id.fifth:
                homeIntent.putExtra(getString(R.string.id_fragment), 5);
                break;

        }
        setResult(RESULT_OK, homeIntent);
        finish();
    }
}