package com.customwidget.models;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by lzb on 2017/3/6.
 */

public class LoadingThreePointModel {
    private View view;
    private float percent;
    private int curRadius;
    private ValueAnimator mCountAnimator;
    private int timeTotal = 10000; //随机计算
    private int curX;
    private int curY;
    private int width;
    private float time; //当前时间
    private Paint paint;
    private int radius;
    private int decalage;
    public LoadingThreePointModel(View view, float percent, int radius, int width, int decalage, int color) {
        this.view = view;
        this.percent = percent;
        this.width = width - 2 * radius;

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(color);
        curY = 10;
        this.radius = radius;
        this.decalage = decalage;
        setXAndY(percent);
    }


    public int getCurX() {
        return curX;
    }

    public int getCurY() {
        return curY;
    }

    public Paint getPaint() {
        return paint;
    }

    public int getWidth() {
        return width;
    }

    public float getPercent() {
        return percent;
    }

    public void setPercent(float percent) {
        this.percent = percent;
    }

    public float getCurRadius() {
        return curRadius;
    }

    public void setCurRadius(int curRadius) {
        this.curRadius = curRadius;
    }

    public void setXAndYPostInvalidate(float percent) {
        setXAndY(percent);
//        view.postInvalidate();
    }

    private void setXAndY(float percent) {
        if(time > percent) time = 0;
        if(percent < 0.5f) { //二象限
            curX = radius + (int) ((1 - (0.5f - percent) / 0.5f) * width);
        } else {
            curX = radius + (int) (width - width * (percent - 0.5f) / 0.5f);
        }

        if(percent <= 0.125f) {
            curRadius = (int) (radius - (decalage * (1-(0.125 - percent)/ 0.125)));
        } else if(percent <= 0.25f) { //二象限
            curRadius = (int) (radius - decalage  + (decalage * (1- (0.25f - percent)/ 0.125)));
        } else if(percent <= 0.375f) { //二象限
            curRadius = (int) (radius  + (decalage * (1-(0.375f - percent)/ 0.125)));
        } else if (percent <=0.5f) {
            curRadius = (int) (radius + decalage - (decalage * (1- (0.5f - percent)/ 0.125)));
        } else if (percent <=0.625f) {
            curRadius = (int) (radius - (decalage * (1-(0.625f - percent)/ 0.125)));
        } else if (percent <=0.75f) {
            curRadius = (int) (radius -decalage + (decalage * (1-(0.75f - percent)/ 0.125)));
        } else if (percent <= 0.875f) {
            curRadius = (int) (radius  + (decalage * (1-(0.875f - percent)/ 0.125)));
        } else {
            curRadius = (int) (radius  + decalage - (decalage * (1-(1f - percent)/ 0.125)));
        }
    }

    public void startAnim() {
        mCountAnimator = new ValueAnimator();
        mCountAnimator.setFloatValues(0, timeTotal);
        mCountAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                percent = value / timeTotal;
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

    public void stopAnim() {
        if(mCountAnimator.isRunning() == true) {
            mCountAnimator.cancel();
        }
    }
}
