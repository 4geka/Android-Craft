package ergo_proxy.crosspagers.Util;

/**
 * Created by Ergo-Proxy on 28.05.2015.
 */

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;


public class CrossAnimation
{

    public static Bitmap mBmp1 = null;
    public static Bitmap mBmp2 = null;
    private static int[] mLoc1;
    private static int[] mLoc2;
    private static ImageView mTopImage;
    private static ImageView mBottomImage;
    private static AnimatorSet mSetAnim;
    private static int splitYCoord;

    public static void startActivity(Activity currActivity, Intent intent)
    {

        prepare(currActivity);
        currActivity.startActivity(intent);
        currActivity.overridePendingTransition(0, 0);
    }

    public static void prepareAnimation(final Activity destActivity)
    {
        mTopImage = createImageView(destActivity, mBmp1, mLoc1);
        mBottomImage = createImageView(destActivity, mBmp2, mLoc2);
    }

    public static void animate(final Activity destActivity, final int duration, final TimeInterpolator interpolator)
    {

        new Handler().post(new Runnable()
        {

            @Override
            public void run()
            {
                mSetAnim = new AnimatorSet();
                mTopImage.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                mBottomImage.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                mSetAnim.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation)
                    {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation)
                    {
                        clean(destActivity);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation)
                    {
                        clean(destActivity);
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation)
                    {

                    }
                });

                Animator anim1 = ObjectAnimator.ofFloat(mTopImage, "translationY", mTopImage.getHeight() * -1);
                Animator anim2 = ObjectAnimator.ofFloat(mBottomImage, "translationY", mBottomImage.getHeight());

                if (interpolator != null)
                {
                    anim1.setInterpolator(interpolator);
                    anim2.setInterpolator(interpolator);
                }

                mSetAnim.setDuration(duration);
                mSetAnim.playTogether(anim1, anim2);
                mSetAnim.start();
            }
        });
    }

    public static void animate(final Activity destActivity, final int duration)
    {
        animate(destActivity, duration, new DecelerateInterpolator());
    }

    public static void cancel()
    {
        if (mSetAnim != null)
            mSetAnim.cancel();
    }

    private static void clean(Activity activity)
    {
        if (mTopImage != null)
        {
            mTopImage.setLayerType(View.LAYER_TYPE_NONE, null);
            try
            {

                activity.getWindowManager().removeViewImmediate(mBottomImage);
            } catch (Exception ignored)
            {
            }
        }
        if (mBottomImage != null)
        {
            mBottomImage.setLayerType(View.LAYER_TYPE_NONE, null);
            try
            {
                activity.getWindowManager().removeViewImmediate(mTopImage);
            } catch (Exception ignored)
            {
            }
        }

        mBmp1 = null;
        mBmp2 = null;
    }

    private static void prepare(Activity currActivity)
    {

        View root = currActivity.getWindow().getDecorView().findViewById(android.R.id.content);
        root.setDrawingCacheEnabled(true);
        Bitmap bmp = root.getDrawingCache();

        splitYCoord = bmp.getHeight() / 2;

        mBmp1 = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), splitYCoord);
        mBmp2 = Bitmap.createBitmap(bmp, 0, splitYCoord, bmp.getWidth(), bmp.getHeight() - splitYCoord);

        mLoc1 = new int[]{0, root.getTop()};
        mLoc2 = new int[]{0, root.getTop() + splitYCoord};
    }

    private static ImageView createImageView(Activity destActivity, Bitmap bmp, int loc[])
    {
        ImageView imageView = new ImageView(destActivity);
        imageView.setImageBitmap(bmp);

        WindowManager.LayoutParams windowParams = new WindowManager.LayoutParams();
        windowParams.gravity = Gravity.TOP;
        windowParams.x = loc[0];
        windowParams.y = loc[1];
        windowParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        windowParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        windowParams.flags = WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        windowParams.format = PixelFormat.TRANSLUCENT;
        windowParams.windowAnimations = 0;
        destActivity.getWindowManager().addView(imageView, windowParams);

        return imageView;
    }
}
