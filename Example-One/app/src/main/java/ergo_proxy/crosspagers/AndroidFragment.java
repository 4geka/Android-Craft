package ergo_proxy.crosspagers;

/**
 * Created by Ergo-Proxy on 27.05.2015.
 */
import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import android.view.MenuItem;

import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.Collections;

import ergo_proxy.crosspagers.Util.CrossAnimation;

public class AndroidFragment extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        CrossAnimation.prepareAnimation(this);
        setContentView(R.layout.android_fragment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CrossAnimation.animate(this, 1500);

        final ImageView imageView=(ImageView)findViewById(R.id.image);
        imageView.setImageResource(R.drawable.ic_launcher);
        final RadioGroup radioGroup=(RadioGroup) findViewById(R.id.radioGroup);

        ArrayAdapter<CharSequence> arrayAdapter=ArrayAdapter.createFromResource(this,R.array.scale_names,android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        for(int i=0;i<arrayAdapter.getCount();i++)
        {
            String sRadio=arrayAdapter.getItem(i).toString();
            RadioButton radioButton=new RadioButton(this);
            radioButton.setText(sRadio);
            radioButton.setId(i);
            radioGroup.addView(radioButton);
        }
        radioGroup.check(radioGroup.getChildAt(0).getId());
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                ArrayList arrayList=new ArrayList();
                Collections.addAll(arrayList, getResources().getStringArray(R.array.scale_values));
                imageView.setScaleType(ImageView.ScaleType.valueOf(arrayList.get(radioGroup.getChildAt(checkedId).getId()).toString()));
            }
        });
    }

    @Override
    protected void onStop()
    {
        CrossAnimation.cancel();
        super.onStop();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        switch (menuItem.getItemId())
        {
            case android.R.id.home:
                Intent homeIntent = new Intent(this, CrossActivity.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
        }
        return (super.onOptionsItemSelected(menuItem));
    }

}
