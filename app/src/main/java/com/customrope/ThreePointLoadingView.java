package com.customrope;
import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.customdrawer.Util;

import java.util.ArrayList;


/**
 * Created by lzb on 2017/3/6.
 */

public class ThreePointLoadingView extends View {
    int width;
    int height;
    Paint paint;
    int decalge;
    int radius;
    int middleHeight;
    ArrayList<LoadingThreePointModel> loadingThreePointModelArrayList = new ArrayList<>();
    private ValueAnimator mCountAnimator;
    public int timeTotal = 5000; //随机计算
    float percent;
    public ThreePointLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.parseColor("#ff8800"));
        decalge = Util.dip2px(context, 3);
        radius = Util.dip2px(context, 5);
        middleHeight = Util.dip2px(context, 14);
        loadingThreePointModelArrayList = new ArrayList<>();
        setWillNotDraw(false);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        if (loadingThreePointModelArrayList.size() == 0) {
//            LoadingThreePointModel(View view, float percent, int radius, int width, int decalage, int color)
            loadingThreePointModelArrayList.add(new LoadingThreePointModel(this, 0.0f, radius, width, decalge, Color.rgb(91, 203, 137)));
            loadingThreePointModelArrayList.add(new LoadingThreePointModel(this, 0.25f, radius, width, decalge, Color.rgb(104, 155, 224)));
            loadingThreePointModelArrayList.add(new LoadingThreePointModel(this, 0.5f, radius, width, decalge, Color.rgb(244, 137, 99)));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int size = loadingThreePointModelArrayList.size();
        for (int i = size - 1; i >= 0; i --) {
            LoadingThreePointModel loadingCircleModel = loadingThreePointModelArrayList.get(i);
//            canvas.drawCircle(56, 15, 56, loadingCircleModel.paint);
            canvas.drawCircle(loadingCircleModel.curX, middleHeight, loadingCircleModel.curRadius, loadingCircleModel.paint);
        }
    }

    public void startAnim() {
        if (mCountAnimator == null ) {
            mCountAnimator = new ValueAnimator();
            mCountAnimator.setFloatValues(0, timeTotal);
            mCountAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (float) animation.getAnimatedValue();
                    percent = value / timeTotal;
                    int size = loadingThreePointModelArrayList.size();
//                for (int i = 0; i < size; i ++) {
//                    if (i == 0)
                    LoadingThreePointModel loadingCircleModel = loadingThreePointModelArrayList.get(0);
                    loadingCircleModel.setXAndYPostInvalidate(percent);
                    float percent2 = 0.0f;
                    if (percent >= 0.75) {
                        percent2 = percent - 0.75f;
                    } else {
                        percent2 = percent + 0.25f;
                    }
                    LoadingThreePointModel loadingCircleModel2 = loadingThreePointModelArrayList.get(1);
                    loadingCircleModel2.setXAndYPostInvalidate(percent2);
                    float percent3 = 0.0f;
                    if (percent >= 0.5f) {
                        percent3 = percent - 0.5f;
                    } else {
                        percent3 = percent + 0.5f;
                    }

                    LoadingThreePointModel loadingCircleModel3 = loadingThreePointModelArrayList.get(2);
                    loadingCircleModel3.setXAndYPostInvalidate(percent3);
                    postInvalidate();
                }
//            }
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
            mCountAnimator.setInterpolator(new LinearInterpolator());
            mCountAnimator.start();
        }
      /*  int size = loadingThreePointModelArrayList.size();
        for (int i = 0; i < size; i ++) {
            LoadingThreePointModel loadingCircleModel = loadingThreePointModelArrayList.get(i);
            loadingCircleModel.startAnim();
        }*/
    }

    public void stopAnim() {
        if(mCountAnimator.isRunning() == true) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mCountAnimator.cancel();
            mCountAnimator = null;

//            }
        }
    }
}
