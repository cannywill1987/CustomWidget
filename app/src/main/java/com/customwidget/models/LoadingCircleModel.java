package com.customwidget.models;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import java.util.Random;

/**
 * Created by lzb on 2016/6/24.
 */
public class LoadingCircleModel {
    private int x;
    private int y;
    private Paint paint;
    private float time; //当前时间
    private float delta = 0.1f;
    private int timeTotal; //随机计算
    private int curX;
    private int curY;
    private int xCenter;
    private int yCenter;
    private int radiusMin;
    private int curRadius;
    private int radiusMax;
    private int screenWidth;
    private int screenHeight;
    private ValueAnimator mCountAnimator;
    private float percent;
    private View view;
    private int status = 0;
    private Random rand =new Random();
    private int startTimeDelay = 0;
    private int alpha = 0;
    private float alphaTarget = 0.8f;
    public LoadingCircleModel(int x, int y, int color, int timeTotal, int xCenter, int yCenter, int curRadiusMin, int radiusMax, View view, int screenWidth, int screenHeight, int status, int startTimeDelay) {
        this.x = x;
        this.y = y;
//        this.paint = paint;
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(color);

        this.timeTotal = timeTotal;
        this.xCenter = xCenter;
        this.yCenter = yCenter;
        this.radiusMin = curRadiusMin;
        this.radiusMax = radiusMax;
        this.view = view;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.status = status;
        this.startTimeDelay = startTimeDelay;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public float getPercent() {
        return percent;
    }

    public void setPercent(float percent) {
        this.percent = percent;
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public int getTimeTotal() {
        return timeTotal;
    }

    public void setTimeTotal(int timeTotal) {
        this.timeTotal = timeTotal;
    }

    public int getCurX() {
        return curX;
    }

    public void setCurX(int curX) {
        this.curX = curX;
    }

    public int getCurY() {
        return curY;
    }

    public void setCurY(int curY) {
        this.curY = curY;
    }

    public int getxCenter() {
        return xCenter;
    }

    public void setxCenter(int xCenter) {
        this.xCenter = xCenter;
    }

    public int getyCenter() {
        return yCenter;
    }

    public void setyCenter(int yCenter) {
        this.yCenter = yCenter;
    }



    public int getCurRadius() {
        return curRadius;
    }

    public void setCurRadius(int curRadius) {
        this.curRadius = curRadius;
    }

    public void setXAndY() {
//        time = delta + time;
//        percent = time / timeTotal;
        if(time > percent) time = 0;
        paint.setAlpha(alpha);
        if(x < xCenter && yCenter > y) { //二象限
            curX = (int) ((xCenter - x) * percent) + x;
            curY = (int) ((yCenter - y) * percent) + y;
            Log.i("position", "curX" + curX + " curY" + curY);
        } else if(x < xCenter && yCenter < y) { //第三象限
            curX = (int) ((xCenter - x) * percent) + x;
            curY = y - (int) ((y - yCenter ) * percent) ;
        } else if(x > xCenter && yCenter < y) { //第四象限
            curX = x - (int) ((x - xCenter) * percent);
            curY = y - (int) ((y - yCenter ) * percent) ;
        } else if(x > xCenter && yCenter > y) { //第一象限
            curX = x - (int) ((x - xCenter) * percent);
            curY = (int) ((yCenter - y) * percent) + y;
        }
        view.postInvalidate();
    }

    public void startAnim() {
        mCountAnimator = new ValueAnimator();
        mCountAnimator.setFloatValues(0, timeTotal);
        mCountAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                percent = value / timeTotal;
                if(percent < alphaTarget) alpha = (int) (255 * percent / alphaTarget);
                curRadius = (int) (radiusMax - (radiusMax - radiusMin) * percent);
                setXAndY();
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
                mCountAnimator.setStartDelay(0);
                if(status == 0) { //左边
                    x = 0;
                    y = rand.nextInt(screenHeight);
                } else if(status == 1) { //上边
                    x = rand.nextInt(screenWidth);
                    y = 0;
                } else if(status == 2) { //右边
                    x = screenWidth;
                    y = rand.nextInt(screenHeight);
                } else if(status == 3) { //下边
                    x = rand.nextInt(screenWidth);
                    y = screenHeight;
                }
            }
        });
        mCountAnimator.setStartDelay(startTimeDelay);
        mCountAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mCountAnimator.setDuration(timeTotal);
        mCountAnimator.setInterpolator(new DecelerateInterpolator());
        mCountAnimator.start();

    }

}
