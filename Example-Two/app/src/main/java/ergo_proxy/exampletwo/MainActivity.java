package ergo_proxy.exampletwo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import ergo_proxy.exampletwo.img.Made;


public class MainActivity extends AppCompatActivity implements View.OnClickListener
{

    public static TextView text;
    public static final int CHOOSE_THIEF = 0;
    Button buttonStart;
    String first, second;
    ArrayList<Made> showListOperation;
    Bundle bundle;
    String sIstoriya;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonStart = (Button)findViewById(R.id.button1);
        buttonStart.setText(getString(R.string.start));
        buttonStart.setOnClickListener(this);
        text = (TextView)findViewById(R.id.tv);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(String.valueOf(text), "onRestoreInstanceState");

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.button1:
                startActivityForResult(new Intent(MainActivity.this, TwoActivity.class), CHOOSE_THIEF);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode!=RESULT_OK)
        {
            return;
        }
        else
        {
            bundle = data.getBundleExtra("mas");
            showListOperation = (ArrayList<Made>) bundle.getSerializable("mas");
            sIstoriya=getString(R.string.one_fragment)+"  "+getString(R.string.two_fragment)+'\n';
            for(int i = 0;i<showListOperation.size();i++)
            {
                first = showListOperation.get(i).getFirstFragment();
                second = showListOperation.get(i).getSecondFragment();
                if (Integer.toString(i+1).length() > 1)
                    sIstoriya = sIstoriya + Integer.toString(i+1) + " " + first + "  " + second + '\n';
                else
                    sIstoriya = sIstoriya + Integer.toString(i+1) + "   " + first + "  " + second + '\n';
            }
            text.setText(sIstoriya);
        }
    }
}
