package ergo_proxy.examplethree.Utils;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import ergo_proxy.examplethree.R;

/**
 * Created by Ergo-Proxy on 04.06.2015.
 */
public class ImageFragment  extends TimerFragment
{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private ImageView imageView;
    private TextView tvTime;
    volatile int i;

    public static ImageFragment newInstance(String param1, String param2)
    {
        ImageFragment fragment = new ImageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ImageFragment()
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
        LinearLayout inflateView = (LinearLayout) inflater.inflate(R.layout.activity_image, container, false);
        imageView = (ImageView) inflateView.findViewById(R.id.our_image);
        imageView.setImageResource(getResources().getIdentifier("drawable/pic1", null, getActivity().getPackageName()));
        ImageView.ScaleType imgScaleType = ImageView.ScaleType.valueOf(mParam2);
        imageView.setScaleType(imgScaleType);
        tvTime = (TextView) inflateView.findViewById(R.id.timer);
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
                        tvTime.setText(result);
                    }
                });
            }
        }, 0L, 1L * 1000);
        return inflateView;
    }
}