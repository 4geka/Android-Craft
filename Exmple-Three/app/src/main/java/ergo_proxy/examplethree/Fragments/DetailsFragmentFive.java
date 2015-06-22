package ergo_proxy.examplethree.Fragments;

import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
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
public class DetailsFragmentFive extends Fragment implements View.OnClickListener
{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private ImageView imageView;
    private LinearLayout linearLayout;
    private TextView tvTime;
    volatile int i;
    public void setScale(String scaleType)
    {
        ImageView.ScaleType imgScaleType = ImageView.ScaleType.valueOf(scaleType);
        imageView.setScaleType(imgScaleType);
    }

    public interface OnSelectedButtonListener
    {
        void onButtonSelected(int buttonIndex, Drawable drawableIndex);
    }

    public void setBorder(boolean flagBorder)
    {
        if (flagBorder)
        {
            linearLayout.setBackgroundColor(getResources().getColor(R.color.primary_dark));
        }
        else
        {
            linearLayout.setBackgroundColor(getResources().getColor(R.color.primary_light));
        }
    }

    public void setDescription(Drawable drawableIndex)
    {
        if (imageView != null)
            imageView.setImageDrawable(drawableIndex);
    }

    public static DetailsFragmentFive newInstance(String param1, String param2)
    {
        DetailsFragmentFive fragment = new DetailsFragmentFive();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public DetailsFragmentFive()
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
        LinearLayout inflateView = (LinearLayout) inflater.inflate(R.layout.activity_details5, container, false);
        linearLayout = (LinearLayout) inflateView.findViewById(R.id.fon);
        imageView = (ImageView) inflateView.findViewById(R.id.our_image);
        imageView.setImageResource(getResources().getIdentifier("drawable/pic1", null, getActivity().getPackageName()));
        ImageView.ScaleType imgScaleType = ImageView.ScaleType.valueOf(mParam2);
        imageView.setScaleType(imgScaleType);
        imageView.setOnClickListener(this);
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

    @Override
    public void onClick(View v)
    {
        OnSelectedButtonListener listener = (OnSelectedButtonListener) getActivity();
        listener.onButtonSelected(Integer.valueOf(mParam1), imageView.getDrawable());
    }
}