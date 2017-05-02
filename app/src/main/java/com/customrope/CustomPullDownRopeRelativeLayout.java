package com.customrope;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.customdrawer.ListViewForAds;
import com.customdrawer.Util;

import java.util.ArrayList;

import relation.vankyshare.com.androidstudiocustomwidget.R;

public class CustomPullDownRopeRelativeLayout extends RelativeLayout implements View.OnTouchListener, View.OnClickListener{
    Paint paint;
    Paint paintPoint;
    boolean isStop = true;
    int heightRopeMin = 80;
    int heightRope = 0;
    int radiusTouchable = 80;
    int width;
    Rect rect = new Rect();
    float mPrecY;
    int heightTrigger= 180;
    int heightMax= 280;
    Context context;
    int x = 60;
    public OnPullDownRopeEventListener onPullDownRopeEventListener;
    float radiusPoint;
    boolean isTouchInBlock = false;
    boolean isTriggered = false;
    boolean isMove = false; //判断是否移动了

    boolean isRopeEnable = true;
    private final View mView;
    private final View mViewBackground;
    private final RelativeLayout mLinearLayout;
    private final FrameLayout mFrameLayout;
    private final int heightDrawer;
    private final int heightToppest;
    private final ListViewForAds listViewForAds;
    private int heightMenu = 0;
    public interface OnPullDownRopeEventListener {
        public void onRopeHeightChangeListener(float alpha);
        public void onTriggerAnimationListener();
    }
    public CustomPullDownRopeRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor("#99ffe8ab"));
        paint.setStrokeWidth(3);

        paintPoint = new Paint();
        paintPoint.setStyle(Paint.Style.FILL);
        paintPoint.setAntiAlias(true);
        paintPoint.setColor(Color.parseColor("#ffffff"));

        setWillNotDraw(false);
        this.context = context;
        setOnTouchListener(this);
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT){
            heightRopeMin = (int) Util.dp2px(getResources(),45);
        } else {
            heightRopeMin = (int) Util.dp2px(getResources(),45);
        }
        radiusPoint = Util.dp2px(getResources(), 3.75f);
        heightTrigger = (int) Util.dp2px(getResources(), 200);
        heightMax =  (int) Util.dp2px(getResources(), 340);


        LayoutInflater layoutInflater = LayoutInflater.from(context);
        mView = layoutInflater.inflate(R.layout.layout_drawer_rope, null);
        mViewBackground = mView.findViewById(R.id.view_background);
        mLinearLayout = (RelativeLayout) mView.findViewById(R.id.linearlayout);
        mFrameLayout = (FrameLayout) mView.findViewById(R.id.framelayout);
        heightDrawer = 0;
        mLinearLayout.setY(heightDrawer);
        addView(mView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        heightToppest = (int) Util.dp2px(context.getResources(), 138);
        setOnTouchListener(this);

        listViewForAds = (ListViewForAds) mView.findViewById(R.id.listview);
        listViewForAds.setDataList(new ArrayList<String>());
        mFrameLayout.setOnClickListener(this);
        heightMenu = Util.dip2px(context, 300);
//        listViewForAds.setOnTouchListViewListener(this);
        mView.setOnClickListener(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(rect.contains((int)event.getX(), (int)event.getY()) == true) {
                    mPrecY = event.getY();
                    isTouchInBlock = true;
                    isTriggered = true;
                    Log.i("return", "down true");
                    return super.dispatchTouchEvent(event);
                } else {
                    isTriggered = false;
                    isTouchInBlock = false;
                    Log.i("return", "down false");
                    return super.dispatchTouchEvent(event);
                }


            case MotionEvent.ACTION_MOVE:
                if( isTouchInBlock == true) {
                    int deltaY = (int) (event.getY() - mPrecY);

                    heightRope = heightRope + deltaY;
                    if (heightMax < heightRope) {
                        heightRope = heightRope - deltaY;
                    }
                    float alpha = ((float)heightRope - heightRopeMin) /(heightTrigger - heightRopeMin);
                    if (onPullDownRopeEventListener != null) {
                        if (isTriggered == true)
                            onPullDownRopeEventListener.onRopeHeightChangeListener(alpha);
                    }
                    /*if (alpha < 0) alpha = 0;
                    if (alpha > 1) alpha = 1;
//                    if (homeSecurityView.isOn() == false && homeSecurityView.isAnimating() == false)
                    mViewBackground.setAlpha(alpha);
                    Log.i("return", "delta y" + deltaY);*/

                    rect.set(x - radiusTouchable, -radiusTouchable + heightRope, x + radiusTouchable, radiusTouchable + heightRope);
                    mPrecY = event.getY();
                    Log.i("return", "y " + (heightRope - heightRopeMin - heightMenu));

                    mLinearLayout.setY(heightRope - heightRopeMin - heightMenu);
                    postInvalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (heightRope - heightRopeMin < 30) { //点击
                    if(rect.contains((int)event.getX(), (int)event.getY()) == true) {
                        startAnimationClicked();
                    }
                } else {
                    if (heightRope > heightTrigger) {
                        if (isTriggered == true) {
                            isTriggered = false;
                            openDrawer();
                            if(onPullDownRopeEventListener != null)
                                onPullDownRopeEventListener.onTriggerAnimationListener();
                        }
                    } else {
                        startAnimation();
                    }
                }

//
                break;
            default:
                return super.dispatchTouchEvent(event);
        }
        return super.dispatchTouchEvent(event);

//        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        x = width - (int)Util.dp2px(getResources(), 75);
        rect.set(x - radiusTouchable, -radiusTouchable + heightRope, x + radiusTouchable, radiusTouchable + heightRope);
        setTouchDelegate(new TouchDelegate(new Rect(x - radiusTouchable, -radiusTouchable + heightRope, x + radiusTouchable, radiusTouchable + heightRope), this));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Path path = new Path();
        path.moveTo(x, 0);
        path.lineTo(x, heightRope - radiusPoint);
        canvas.drawCircle(x, heightRope, radiusPoint, paintPoint);
        canvas.drawPath(path, paint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setHeight(heightRopeMin);
        mLinearLayout.setY(-heightMenu);
    }

    public void startAnimation() {
        ObjectAnimator animator1 = ObjectAnimator.ofInt(this, "height", heightRope, heightRopeMin);//X轴平移旋转
        animator1.setDuration(1000);
        isStop = false;
        animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int heightRope = (int) animation.getAnimatedValue();
                if ((heightRope - heightRopeMin) < 20 && isStop == false) {
                    if(onPullDownRopeEventListener != null)
                        onPullDownRopeEventListener.onRopeHeightChangeListener(0);
                    isStop = true;
                }
                if (heightRope >= 0 && isStop == false) {
                    float alpha = ((float)heightRope - heightRopeMin) /(heightTrigger - heightRopeMin);
                    if(onPullDownRopeEventListener != null )
                        onPullDownRopeEventListener.onRopeHeightChangeListener(alpha);
                    /*if (alpha < 0) alpha = 0;
                    if (alpha > 1) alpha = 1;
                    mViewBackground.setAlpha(alpha);*/
                }

            }
        });
        animator1.setInterpolator(new BounceInterpolator());
        animator1.start();
    }
    public boolean isAnimated = false;
    public void startAnimationClicked() {
        if (isAnimated == true) return;

        isAnimated = true;
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animator1 = ObjectAnimator.ofInt(this, "height", heightRopeMin, heightRopeMin + 30);//X轴平移旋转
        animator1.setDuration(1000);

        ObjectAnimator animator2 = ObjectAnimator.ofInt(this, "height", heightRopeMin + 30, heightRopeMin);//X轴平移旋转
        animator2.setDuration(1000);
        animator2.setInterpolator(new BounceInterpolator());

        animatorSet.play(animator2).after(animator1);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimated = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();
    }

    public void setHeight(int heightRope) {
        this.heightRope = heightRope;
        Log.i("heightRope", heightRope + "");
        rect.set(x - radiusTouchable, -radiusTouchable + heightRope, x + radiusTouchable, radiusTouchable + heightRope);
        mLinearLayout.setY(heightRope - heightRopeMin - heightMenu);
        postInvalidate();
    }

    /*@Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }*/

    @Override
    public boolean onTouch(View v, MotionEvent event) {
//        if (isRopeEnable == false) return false;
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(rect.contains((int)event.getX(), (int)event.getY()) == true) {
                    mPrecY = event.getY();
                    isTouchInBlock = true;
                    isTriggered = true;
                    Log.i("return", "down true");
                    return true;
                } else {
                    isTriggered = false;
                    isTouchInBlock = false;
                    Log.i("return", "down false");
                    return false;
                }


            case MotionEvent.ACTION_MOVE:
                if( isTouchInBlock == true) {
                    int deltaY = (int) (event.getY() - mPrecY);

                    heightRope = heightRope + deltaY;
                    if (heightMax < heightRope) {
                        heightRope = heightRope - deltaY;
                    }

                    if (onPullDownRopeEventListener != null) {
                        if (isTriggered == true)
                            onPullDownRopeEventListener.onRopeHeightChangeListener(((float)heightRope - heightRopeMin) /(heightTrigger - heightRopeMin));
                    }
                    Log.i("return", "delta y" + deltaY);

                    rect.set(x - radiusTouchable, -radiusTouchable + heightRope, x + radiusTouchable, radiusTouchable + heightRope);
                    mPrecY = event.getY();
                    Log.i("return", "y " + (heightRope - heightRopeMin - heightMenu));

                    mLinearLayout.setY(heightRope - heightRopeMin - heightMenu);
                    postInvalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (heightRope - heightRopeMin < 30) { //点击
                    if(rect.contains((int)event.getX(), (int)event.getY()) == true) {
                        startAnimationClicked();
                    }
                } else {
                    if (heightRope > heightTrigger) {
                        if (isTriggered == true) {
                            isTriggered = false;
                            openDrawer();
                            if(onPullDownRopeEventListener != null)
                                onPullDownRopeEventListener.onTriggerAnimationListener();
                        }
                    } else {
                        startAnimation();
                    }
                }

//
                break;
            default:
                return super.onTouchEvent(event);
        }
        return true;
    }

    public void updateView() {
        postInvalidate();
    }

    public OnPullDownRopeEventListener getOnPullDownRopeEventListener() {
        return onPullDownRopeEventListener;
    }

    public void setOnPullDownRopeEventListener(OnPullDownRopeEventListener onPullDownRopeEventListener) {
        this.onPullDownRopeEventListener = onPullDownRopeEventListener;
    }
    public boolean isRopeEnable() {
        return isRopeEnable;
    }

    public void setRopeEnable(boolean ropeEnable) {
        isRopeEnable = ropeEnable;
    }

    @Override
    public void onClick(View v) {

    }

    public void openDrawer() {
        if (isAnimated == true) return;

        isAnimated = true;
        ObjectAnimator animator1 = ObjectAnimator.ofInt(this, "height", heightRope, heightMenu + heightRopeMin);//X轴平移旋转
        animator1.setDuration(1000);
        animator1.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimated = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator1.setInterpolator(new BounceInterpolator());
        animator1.start();

    }
}