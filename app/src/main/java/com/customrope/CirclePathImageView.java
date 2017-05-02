package com.customrope;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by Administrator on 2016/7/1.
 */
public class CirclePathImageView extends ImageView {
    int r = 20;
    int centerX = 0;
    int centerY = 0;
    int x = 0;
    int y = 0;
    private ValueAnimator mCountAnimator;
    int timeTotal = 1000;
    public CirclePathImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public void setCircle() {
        setX(x);
        setY(y);
    }

    public void startAnim() {
        mCountAnimator = new ValueAnimator();
        mCountAnimator.setFloatValues(0, timeTotal);
        mCountAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                float percent = value / timeTotal;
                setProgress(percent);
            }
        });

        mCountAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {


            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // do nothing
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                // do nothing
            }
        });
        mCountAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mCountAnimator.setDuration(timeTotal);
        mCountAnimator.start();
    }

    public void setProgress(float percent) {
        double angle = 2 * Math.PI * percent;
        y = (int) (r - Math.sin(angle) * r);
        if(percent > 0.25f && percent < 0.75f) {
            x = r - (int) (Math.sqrt(r * r - (y - r) * (y - r)));
        } else {
            x = (int) (Math.sqrt(r * r - (y - r) * (y - r)) + r);
        }
        Log.i("tag123", "percent:" + percent + "x:" + x + "y:" + y);
        setCircle();
    }

}
