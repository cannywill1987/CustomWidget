package com.customwidget.views.loading;

import android.app.Activity;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import relation.vankyshare.com.androidstudiocustomwidget.R;

/**
 * 文件加载中放大镜里的控件
 * Created by lzb on 2016/6/23.
 */
public class CustomLoadingWidget extends RelativeLayout {

    private final View mView;
    private final LoadingItemRelativeLayout loadingItemRelativeLayout;
    private float yCenter;
    private float xCenter;
    private LoadingCircleItemsRelativeLayout loadingCircleItemsRelativeLayout;
    private final FrameLayout frameLayoutContainor;
    Activity context;
    ArrayList<Integer> listColors = new ArrayList<Integer>();
    ArrayList<String> listText = new ArrayList<>();
    int curItemPos = 0;
    private final int size;
    private final TextView textView;
    private final CirclePathImageView circlePathImageView;

    public CustomLoadingWidget(Activity context) {
        super(context);
        this.context = context;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        mView = layoutInflater.inflate(R.layout.layout_loading, null);
        circlePathImageView = (CirclePathImageView) mView.findViewById(R.id.imageview_circle_path);
        frameLayoutContainor = (FrameLayout) mView.findViewById(R.id.framelayout);
        textView = (TextView) mView.findViewById(R.id.textview_message);
        addView(mView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        loadingItemRelativeLayout = (LoadingItemRelativeLayout) mView.findViewById(R.id.loading_center);
        listColors.add(Color.parseColor("#e98080"));
        listColors.add(Color.parseColor("#6da4e3"));
        listColors.add(Color.parseColor("#7fcdb8"));
        listColors.add(Color.parseColor("#ef9960"));

        listText.add("利息");
        listText.add("手续费");
        listText.add("利息");
        listText.add("年费");
        listText.add("滞纳金");
        size = listColors.size();

        handler.sendEmptyMessage(0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if(loadingCircleItemsRelativeLayout == null) {
            if (width > 0 && height > 0) {
                frameLayoutContainor.addView(loadingCircleItemsRelativeLayout = new LoadingCircleItemsRelativeLayout(context, width, height), new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                loadingCircleItemsRelativeLayout.startAnim();
                circlePathImageView.startAnim();
            }
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            loadingItemRelativeLayout.setColor(listColors.get(curItemPos % size));
            textView.setText(listText.get(curItemPos % size));
            curItemPos = curItemPos + 1;
            handler.sendEmptyMessageDelayed(0, 1000);
        }
    };

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        initPos();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initPos();
    }

    private void initPos() {
        if(yCenter == 0) {
            yCenter = loadingItemRelativeLayout.getY();
            xCenter = loadingItemRelativeLayout.getX();
        }
    }

//    public void startAnim() {

    /*    ObjectAnimator animator1 = ObjectAnimator.ofFloat(loadingCircleItemRelativeLayout1, "X", loadingCircleItemRelativeLayout1.getX(), xCenter);
        animator1.setDuration(350);
        animator1.start();

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(loadingCircleItemRelativeLayout1, "Y", loadingCircleItemRelativeLayout1.getY(), yCenter);
        animator2.setDuration(350);
        animator2.start();*/

//    }



}
