package ergo_proxy.examplethree.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import ergo_proxy.examplethree.R;

/**
 * Created by Ergo-Proxy on 04.06.2015.
 */
public class DetailsFragmentTwo  extends Fragment
{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private TextView tvTime;
    private TextView tvChange;
    volatile int i;

    public static DetailsFragmentTwo newInstance(String param1, String param2)
    {
        DetailsFragmentTwo fragment = new DetailsFragmentTwo();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public DetailsFragmentTwo()
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        LinearLayout inflateView = (LinearLayout) inflater.inflate(R.layout.activity_details2, container, false);
        tvTime = (TextView) inflateView.findViewById(R.id.timer);
        tvChange = (TextView) inflateView.findViewById(R.id.change_text);
        Timer myTimer = new Timer();
        final Handler uiHandler = new Handler();
        i = 0;
        myTimer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                final String result = Integer.toString(i++);
                uiHandler.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        switch (i%3)
                        {
                            case 0:
                                tvChange.setGravity(Gravity.BOTTOM);
                                break;
                            case 1:
                                tvChange.setGravity(Gravity.CENTER);
                                break;
                            case 2:
                                tvChange.setGravity(Gravity.TOP);
                                break;
                        }
                        tvTime.setText(result);
                    }
                });

            }
        }, 0L, 1L * 1000);
        return inflateView;
    }

}

