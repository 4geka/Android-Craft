package ergo_proxy.examplethree;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import ergo_proxy.examplethree.Fragments.DetailsFragmentThree;
import ergo_proxy.examplethree.Fragments.DetailsFragmentTwo;
import ergo_proxy.examplethree.Utils.ImageFragment;


public class MainActivity extends AppCompatActivity implements View.OnClickListener
{

    private Button button_activity_1;
    private Button button_activity_2;

    public static final int CHOOSE_THIEF = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_activity_1 = (Button) findViewById(R.id.button1);
        button_activity_2 = (Button) findViewById(R.id.button2);

        findViewById(R.id.oneID).setOnClickListener(this);
        findViewById(R.id.twoID).setOnClickListener(this);
        findViewById(R.id.threeID).setOnClickListener(this);
        findViewById(R.id.fourID).setOnClickListener(this);
        findViewById(R.id.fiveID).setOnClickListener(this);
        findViewById(R.id.sixID).setOnClickListener(this);

        button_activity_1.setOnClickListener(this);
        button_activity_2.setOnClickListener(this);

    }

    @Override
    public void onClick(View v)
    {
        Intent intent = new Intent(MainActivity.this, ChoseActivity.class);

        switch (v.getId())
        {
            case R.id.button1:
                for (int i = 1; i <= 6; i++)
                {

                    Fragment fragment = getFragmentManager().findFragmentByTag(String.valueOf(i));
                    if(fragment != null)
                        getFragmentManager().beginTransaction().setCustomAnimations(R.animator.animator, R.animator.animator_end).remove(fragment).commit();
                }
                break;
             case R.id.button2:
                clearAll();
                break;
            case R.id.oneID:
                intent.putExtra("id_container", 1);
                break;
            case R.id.twoID:
                intent.putExtra("id_container", 2);
                break;
            case R.id.threeID:
                intent.putExtra("id_container", 3);
                break;
            case R.id.fourID:
                intent.putExtra("id_container", 4);
                break;
            case R.id.fiveID:
                intent.putExtra("id_container", 5);
                break;
            case R.id.sixID:
                intent.putExtra("id_container", 6);
                break;
        }
        if ((v.getId()!=R.id.button1)&&(v.getId()!=R.id.button2))
        {
            startActivityForResult(intent, CHOOSE_THIEF);
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
            int idCont = data.getIntExtra(getString(R.string.id_container),0);
            int idFrag =data.getIntExtra(getString(R.string.id_fragment),0);
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().setCustomAnimations(R.animator.animator, R.animator.animator_end);
            fragmentTransaction.commit();
            switch (idFrag)
            {
                case 1:
                    fragmentTransaction.add(intToResoursId(idCont), ImageFragment.newInstance(Integer.toString(idCont), "FIT_XY"), Integer.toString(idCont));
                    break;
                case 2:
                    fragmentTransaction.add(intToResoursId(idCont), DetailsFragmentTwo.newInstance(Integer.toString(idCont), "FIT_XY"), Integer.toString(idCont));
                    break;
                case 3:
                    fragmentTransaction.add(intToResoursId(idCont), DetailsFragmentThree.newInstance(Integer.toString(idCont), "FIT_XY"), Integer.toString(idCont));
                    break;
                case 4:
                    fragmentTransaction.add(intToResoursId(idCont), ImageFragment.newInstance(Integer.toString(idCont), "FIT_XY"), Integer.toString(idCont));
                    break;
                case 5:
                    fragmentTransaction.add(intToResoursId(idCont), ImageFragment.newInstance(Integer.toString(idCont), "FIT_XY"), Integer.toString(idCont));
                    break;

            }
        }

    }
    
    private int intToResoursId(int i)
    {
        switch (i)
        {
            case 1:
                return R.id.oneID;
            case 2:
                return R.id.twoID;
            case 3:
                return R.id.threeID;
            case 4:
                return R.id.fourID;
            case 5:
                return R.id.fiveID;
            case 6:
                return R.id.sixID;
            default:
                return 0;
        }
    }
}

