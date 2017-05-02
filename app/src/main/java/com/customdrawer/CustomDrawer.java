package com.customdrawer;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;


import java.util.ArrayList;

import relation.vankyshare.com.androidstudiocustomwidget.R;

/**
 * Created by Administrator on 2016/5/7.
 */
public class CustomDrawer extends LinearLayout implements View.OnTouchListener, View.OnClickListener, ListViewForAds.OnTouchListViewListener{
    Rect rectBlock = new Rect();
    Rect rectStatusBar = new Rect();
    float mPrecY;
    int heightDrawer = 0;
    boolean isTouchInBlock = false;
    boolean isTriggered = false;
    int radiusTouchable = 80;
    int x = 60;
    int heightRopeMin = 80;
    int heightTrigger= 180;
    boolean isStop = true;
    public OnPullDownDrawerEventListener onPullDownRopeEventListener;
    private final View mView;
    private final LinearLayout mLinearLayout;
    private int mWidth;
    private int mHeight;

    int heightToppest = 280;
    int heightLowest = 0;
    int heightMiddle = 0;//判断动画往上活着往下
    private boolean isAnimating = false;
    ListViewForAds listViewForAds;
    boolean isListViewEnabled = false;
    boolean isOn = false;
    private final FrameLayout mFrameLayout;
    private int curYTableView = 0;

    private boolean touchZoneInBock = true;
    private float precInterceptY;
    private boolean isListViewTouchable = false;
    private boolean isActionDownOutsideZone = false;
    private boolean isActionUpOutsideZone = false;
    private int isActionDownState = -1; //0 是statusbar 1是 block
    private final View mViewBackground;

    OnCustomDrawerEventListener onCustomDrawerEventListener;
    public interface OnCustomDrawerEventListener {
        public void onCustomDrawerChangeListener(float alpha);
    }

    public CustomDrawer(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        mView = layoutInflater.inflate(R.layout.layout_drawer, null);
        mViewBackground = mView.findViewById(R.id.view_background);
        mLinearLayout = (LinearLayout) mView.findViewById(R.id.linearlayout);
        mFrameLayout = (FrameLayout) mView.findViewById(R.id.framelayout);
        heightDrawer = 0;
        mLinearLayout.setY(heightDrawer);
        addView(mView, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        heightToppest = (int) Util.dp2px(context.getResources(), 138);
        setOnTouchListener(this);

        listViewForAds = (ListViewForAds) mView.findViewById(R.id.listview);
        listViewForAds.setDataList(new ArrayList<String>());
        mFrameLayout.setOnClickListener(this);

        handler.sendEmptyMessageDelayed(0, 100);
        listViewForAds.setOnTouchListViewListener(this);
        mView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        startAnimationBottom();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    public void ontouch() {

    }

    public interface OnPullDownDrawerEventListener {
        public void onRopeHeightChangeListener(float alpha);
        public void onTriggerAnimationListener();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        heightDrawer = (int) (mHeight - Util.dp2px(getResources(), 241));
        mLinearLayout.setY(heightDrawer);
//        rectBlock.set(0, heightDrawer, mWidth, (int) (heightDrawer + Utils.dp2px(getResources(), 45)));
//        rectBlock.set(0, heightDrawer, mWidth, mHeight);
//        setTouchZoneBlock();
        updateTouchZone();
        heightLowest = heightDrawer;
        heightMiddle = (mHeight - heightToppest) / 2;
        mViewBackground.setAlpha(0);
    }

    public void setTouchZoneStatusBar() {
        rectBlock.set(0, heightDrawer, mWidth, (int) (heightDrawer + Util.dp2px(getResources(), 45)));
        rectStatusBar.set(0, heightDrawer, mWidth, (int) (heightDrawer + Util.dp2px(getResources(), 45)));
    }

    public void setTouchZoneBlock() {
        rectBlock.set(0, heightDrawer, mWidth, mHeight);
        rectStatusBar.set(0, heightDrawer, mWidth, (int) (heightDrawer + Util.dp2px(getResources(), 45)));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.i("dispatchTouchEvent1", "dispatchTouchEvent 111 " + event.getAction() + " y:" + event.getY() + " isListViewEnabled:" + isListViewEnabled);
        //点击zone以为会
        if (event.getAction() == MotionEvent.ACTION_DOWN && isOn == true && rectBlock.contains((int)event.getX(), (int)event.getY()) == false) {
            isActionDownOutsideZone = true;
        } else if(event.getAction() == MotionEvent.ACTION_DOWN){
            isActionDownOutsideZone = false;
        }

        if (event.getAction() == MotionEvent.ACTION_UP && isOn == true && rectBlock.contains((int)event.getX(), (int)event.getY()) == false) {
            isActionUpOutsideZone = true;
        } else if(event.getAction() == MotionEvent.ACTION_UP){
            isActionUpOutsideZone = false;
        }

        if(isOn == true && isActionUpOutsideZone == true && isActionDownOutsideZone == true) {
            startAnimationBottom();
            return  super.dispatchTouchEvent(event);
        }

        if(event.getAction() == MotionEvent.ACTION_UP) {
            isActionUpOutsideZone = false;
            isActionDownOutsideZone = false;

            if (heightDrawer < heightMiddle) { //往上
                startAnimationToppest();
                Log.i("animationClicked", "ACTION UP");
            } else {
                Log.i("animationClicked", "ACTION DOWN");
                startAnimationBottom();
            }
        }


        if (event.getAction() == MotionEvent.ACTION_UP) {
            isListViewTouchable = false;
        }

        if(rectStatusBar.contains((int)event.getX(), (int)event.getY()) == true && (isActionDownState == 0 || isActionDownState == -1)) {
            Boolean x1 = handleTouchEvent(event);
            if (x1 != null) return x1;
        }
        isActionDownState = 2;

        Log.i("dispatchTouchEvent1", "dispatchTouchEvent 222 " + event.getAction() + " y:" + event.getY() + " isListViewEnabled:" + isListViewEnabled);
//        if (rectBlock.contains((int)event.getX(), (int)event.getY()) == true) {
            if (isListViewEnabled == true) {
                if (isListViewTouchable == false) {
                    event.setAction(MotionEvent.ACTION_DOWN);
                    isListViewTouchable = true;
                    isActionDownState = -1;
                }
                super.dispatchTouchEvent(event);
                event.setAction(MotionEvent.ACTION_MOVE);
                int action = event.getAction();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    isListViewTouchable = false;
                }
                return super.dispatchTouchEvent(event);
            }
//        } else {
//
//        }

        if (event.getAction() == MotionEvent.ACTION_UP) {
            isListViewTouchable = false;
        }
//        Log.i("dispatchTouchEvent", "dispatchTouchEvent " + event.getAction() + " y:" + event.getY());
        Log.i("dispatchTouchEvent1", "dispatchTouchEvent 333 " + event.getAction() + " y:" + event.getY() + " isListViewEnabled:" + isListViewEnabled);

        Boolean x1 = handleTouchEvent(event);
        if (x1 != null) return x1;

        return super.dispatchTouchEvent(event);
    }

    private Boolean handleTouchEvent(MotionEvent event) {
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(rectBlock.contains((int)event.getX(), (int)event.getY()) == true) {
                    mPrecY = event.getY();
                    isTouchInBlock = true;
                    isTriggered = true;
                    return true;
                } else {
                    isTriggered = false;
                    isTouchInBlock = false;
                    return false;
                }

            case MotionEvent.ACTION_MOVE:
                Log.i("heightDrawer", "scroll y:" + listViewForAds.getScrollY());
                if(heightDrawer == heightToppest) {
                    isOn = true;
                    isListViewEnabled = true;
//                    listViewForAds.requestDisallowInterceptTouchEvent(true);
                } else {
//                    isListViewTouchable = false;

                }
                if( isTouchInBlock == true) {
                    int deltaY = (int) (event.getY() - mPrecY);
                    if (Math.abs(deltaY) < 200) {
                        heightDrawer = heightDrawer + deltaY;
                        if (heightToppest > heightDrawer) {
                            heightDrawer = heightDrawer - deltaY;
                        }

                        mLinearLayout.setY(heightDrawer);
                    }
                    float alpha = ((float)heightLowest - heightDrawer) / (heightToppest - heightLowest);
                    Log.i("alphatest", "heightLowest " + heightLowest + "heightDrawer " + heightDrawer + "alpha" + alpha);
                    if(onCustomDrawerEventListener != null) onCustomDrawerEventListener.onCustomDrawerChangeListener(alpha);
                    mViewBackground.setAlpha(Math.abs(alpha));
                    updateTouchZone();
                    mPrecY = event.getY();

                    postInvalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.i("position22", "heightDrawer:" + heightDrawer + " heightMiddle:" + heightMiddle + " heightToppest:" + heightToppest );
                isActionDownState = -1;

                if (heightDrawer < heightMiddle) { //往上
                    startAnimationToppest();
                    Log.i("animationClicked", "ACTION UP");
                } else {
                    Log.i("animationClicked", "ACTION DOWN");
                    startAnimationBottom();
                }

                if (heightDrawer - heightRopeMin < 30) { //点击
                    if(rectBlock.contains((int)event.getX(), (int)event.getY()) == true) {
                        startAnimationClicked();
                    }
                } else {
                    if (heightDrawer > heightTrigger) {
                        if (onPullDownRopeEventListener != null && isTriggered == true) {
                            isTriggered = false;
//                        isTriggered = false;
                            onPullDownRopeEventListener.onTriggerAnimationListener();
                        }
                    } else {
                    }
                }
                startAnimationToppest();
                break;
        }
        return null;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        Log.i("onintercepterExecuted", "onInterceptTouchEvent" + event.getAction());
        /*if(curYTableView > 0 || (curYTableView == 0 && isOn == true && listViewForAds.isDownwards == false)) {
            return false;
        }

        if(listViewForAds.isDownwards == true && isOn == true && curYTableView == 0) {
            isListViewEnabled = false;
        }*/
//        if(rectStatusBar.contains((int)event.getX(), (int)event.getY()) == true) {
//            return true;
//        } else {
        return !isListViewEnabled;
//        }
//        return true;
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

    public void startAnimationToppest() {
        if (isAnimating == true) return;
        isAnimating = true;
        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(mViewBackground, "alpha", mViewBackground.getAlpha(), 1);//X轴平移旋转
        animatorAlpha.setDuration(1000);
        animatorAlpha.start();

        ObjectAnimator animator1 = ObjectAnimator.ofInt(this, "height", heightDrawer, heightToppest);//X轴平移旋转
        animator1.setDuration(1000);
        isStop = false;
        animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
//                heightDrawer = (int) animation.getAnimatedValue();
//                mLinearLayout.setY(heightDrawer);
//                rectBlock.set(0, heightDrawer, mWidth, (int) (heightDrawer + Utils.dp2px(getResources(), 45)));
//                Log.i("heightDrawer", "heightDrawer:" + heightDrawer);
//                postInvalidate();
              /*  if ((heightRope - heightRopeMin) < 20 && isStop == false) {
                    onPullDownRopeEventListener.onRopeHeightChangeListener(0);
                    isStop = true;
                }
                if (onPullDownRopeEventListener != null && heightRope >= 0 && isStop == false) {
                    onPullDownRopeEventListener.onRopeHeightChangeListener(((float) heightRope - heightRopeMin) / (heightTrigger - heightRopeMin));
                }
*/
            }
        });
        animator1.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isListViewEnabled = true;
                isOn = true;
                isAnimating = false;
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


    public void startAnimationBottom() {
        if (isAnimating == true) return;
        isAnimating = true;

        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(mViewBackground, "alpha", mViewBackground.getAlpha(), 0);//X轴平移旋转
        animatorAlpha.setDuration(1000);
        animatorAlpha.start();

        isListViewEnabled = false;
        ObjectAnimator animator1 = ObjectAnimator.ofInt(this, "height", heightDrawer, heightLowest);//X轴平移旋转
        animator1.setDuration(1000);
        isStop = false;
        animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
//                int heightRope = (int) animation.getAnimatedValue();
//                heightDrawer = (int) animation.getAnimatedValue();
//                Log.i("heightDrawer", "heightDrawer:" + heightDrawer);
//                mLinearLayout.setY(heightDrawer);
//                rectBlock.set(0, heightDrawer, mWidth, (int) (heightDrawer + Utils.dp2px(getResources(), 45)));

              /*  if ((heightRope - heightRopeMin) < 20 && isStop == false) {
                    onPullDownRopeEventListener.onRopeHeightChangeListener(0);
                    isStop = true;
                }
                if (onPullDownRopeEventListener != null && heightRope >= 0 && isStop == false) {
                    onPullDownRopeEventListener.onRopeHeightChangeListener(((float) heightRope - heightRopeMin) / (heightTrigger - heightRopeMin));
                }
*/
            }
        });
        animator1.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimating = false;
                isOn = false;
                listViewForAds.smoothScrollToPosition(0);
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

    public void setHeight(int heightDrawer) {
        this.heightDrawer = heightDrawer;
        mLinearLayout.setY(heightDrawer);
        updateTouchZone();
    }

    private void updateTouchZone() {
        if (touchZoneInBock == true) {
            setTouchZoneBlock();
        } else {
            setTouchZoneStatusBar();
        }
    }

    public boolean isListViewEnabled() {
        return isListViewEnabled;
    }

    public void setListViewEnabled(boolean listViewEnabled) {
        isListViewEnabled = listViewEnabled;
    }

    public void setTouchZoneInBock(boolean touchZoneInBock) {
        this.touchZoneInBock = touchZoneInBock;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            curYTableView = listViewForAds.getScrollY2();
            Log.i("listviewstate", "isListViewEnabled:" + isListViewEnabled);

            Log.i("heightDrawer", "scroll y:" + listViewForAds.getScrollY2());
            if(listViewForAds.isDownwards == true &&curYTableView == 0) {
                isListViewEnabled = false;
                isListViewTouchable = false;
            }
            handler.sendEmptyMessageDelayed(0, 10);
        }
    };

    public void setOnCustomDrawerEventListener(OnCustomDrawerEventListener onCustomDrawerEventListener) {
        this.onCustomDrawerEventListener = onCustomDrawerEventListener;
    }
}
