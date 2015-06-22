package ergo_proxy.exampletwo;

/**
 * Created by Ergo-Proxy on 01.06.2015.
 */
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import java.util.ArrayList;
import java.util.Collections;

import ergo_proxy.exampletwo.img.Images;
import ergo_proxy.exampletwo.img.Made;


public class TwoActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener, Images.OnSelectedButtonListener
{
    private ArrayList<Made> listOperation = new ArrayList<>();
    private Images fragmentFirst;
    private Drawable drawableFirst;
    FragmentManager fragmentManager;
    Images fragmentNext;
    Button buttonFinish;
    Spinner ourSpinner;
    ArrayAdapter<CharSequence> arrayAdapter;
    Bundle bundle;
    Intent homeIntent;
    ArrayList arrayList;
    FragmentTransaction fragmentTransaction;
    String sScaleType;


    @Override
    public void onButtonSelected(int buttonIndex, Drawable drawableIndex)
    {
        if (fragmentFirst != null) {
            fragmentManager = getFragmentManager();
            fragmentNext = (Images) fragmentManager.findFragmentByTag(String.valueOf(buttonIndex));

            if (!fragmentFirst.equals(fragmentNext)) {
                fragmentFirst.setDescription(drawableIndex);
                fragmentNext.setDescription(drawableFirst);
                fragmentFirst.setBorder(false);
                listOperation.add(new Made(fragmentFirst.getTag(), fragmentNext.getTag()));

                drawableFirst = null;
                fragmentFirst = null;
            }
        }
        else
        {
            drawableFirst = drawableIndex;
            fragmentManager = getFragmentManager();
            fragmentFirst = (Images) fragmentManager.findFragmentByTag(String.valueOf(buttonIndex));
            fragmentFirst.setBorder(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        buttonFinish = (Button) findViewById(R.id.over);
        buttonFinish.setText(getString(R.string.over));
        buttonFinish.setOnClickListener(this);

        ourSpinner = (Spinner) findViewById(R.id.our_spinner);
        arrayAdapter = ArrayAdapter.createFromResource(this, R.array.scale_names, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ourSpinner.setAdapter(arrayAdapter);
        ourSpinner.setOnItemSelectedListener(this);
        fragmentManager = getFragmentManager();

        fragmentTransaction = fragmentManager.beginTransaction().setCustomAnimations(R.animator.animator, R.animator.animator_end);
        fragmentTransaction.add(R.id.pic1, Images.newInstance("1", "FIT_XY"), "1");
        fragmentTransaction.add(R.id.pic2, Images.newInstance("2", "FIT_XY"), "2");
        fragmentTransaction.add(R.id.pic3, Images.newInstance("3", "FIT_XY"), "3");
        fragmentTransaction.add(R.id.pic4, Images.newInstance("4", "FIT_XY"), "4");
        fragmentTransaction.add(R.id.pic5, Images.newInstance("5", "FIT_XY"), "5");
        fragmentTransaction.add(R.id.pic6, Images.newInstance("6", "FIT_XY"), "6");
        fragmentTransaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        switch (menuItem.getItemId())
        {
            case android.R.id.home:
                homeIntent = new Intent(this, MainActivity.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                bundle = new Bundle();
                bundle.putSerializable("mas", listOperation);
                homeIntent.putExtra("mas", bundle);
                setResult(RESULT_OK, homeIntent);
                finish();
        }
        return (super.onOptionsItemSelected(menuItem));
    }

    @Override
    public void onBackPressed()
    {
        homeIntent = new Intent(this, MainActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        bundle = new Bundle();
        bundle.putSerializable("mas", listOperation);
        homeIntent.putExtra("mas", bundle);
        setResult(RESULT_OK, homeIntent);
        finish();
    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.over:
                homeIntent = new Intent(this, MainActivity.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                bundle = new Bundle();
                bundle.putSerializable("mas", listOperation);
                homeIntent.putExtra("mas", bundle);
                setResult(RESULT_OK, homeIntent);
                finish();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        arrayList = new ArrayList();
        Collections.addAll(arrayList, getResources().getStringArray(R.array.scale_values));
        sScaleType = arrayList.get(position).toString();
        fragmentManager = getFragmentManager();
        for (int i = 1; i <= 6; i++)
        {
            ((Images) fragmentManager.findFragmentByTag(String.valueOf(i))).setScale(sScaleType);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {

    }
}
