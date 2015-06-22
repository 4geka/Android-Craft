package ergo_proxy.crosspagers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Ergo-Proxy on 27.05.2015.
 */
public class Android extends AppCompatActivity
{

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.android);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final ImageView imageView=(ImageView)findViewById(R.id.image);
        imageView.setImageResource(R.drawable.ic_launcher);

        Spinner spinner=(Spinner) findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> arrayAdapter=ArrayAdapter.createFromResource(this,R.array.scale_names,android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                ArrayList arrayList=new ArrayList();

                Collections.addAll(arrayList, getResources().getStringArray(R.array.scale_values));
                String sScaleType=arrayList.get(position).toString();
                ImageView.ScaleType imgScaleType=ImageView.ScaleType.valueOf(sScaleType);

                imageView.setScaleType(imgScaleType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                //Ничего
            }
        });
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
