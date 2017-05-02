package com.customwidget.views.loading;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.widget.RelativeLayout;

import com.customwidget.models.LoadingCircleModel;
import com.customwidget.utils.Util;

import java.util.ArrayList;
import java.util.Random;

public class LoadingCircleItemsRelativeLayout extends RelativeLayout {
    private int width;
    private int height;
    int radius;
    private Path mPath;
    private int y;
    private int x;
    ArrayList<LoadingCircleModel> listLoadingCircleModel = new ArrayList<LoadingCircleModel>();
    ArrayList<Integer> listColor = new ArrayList<>();
    private int total = 3;
    private int delay = 1000;
    public LoadingCircleItemsRelativeLayout(Activity context, int width, int height) {
        super(context);
        setWillNotDraw(false);
        Random rand =new Random();
        listColor.add(Color.parseColor("#f5e0e0"));
        listColor.add(Color.parseColor("#cfe9f9"));
        listColor.add(Color.parseColor("#ffecc6"));
        listColor.add(Color.parseColor("#c2eae2"));
        listColor.add(Color.parseColor("#cfed80"));
        listColor.add(Color.parseColor("#c5f8ed"));
        int sizeColors = listColor.size();
        for(int i = 0; i < total; i++) {
            int randColor = rand.nextInt(sizeColors);
            int randColorValue = listColor.get(randColor);
            int randomInt = 0;
            LoadingCircleModel loadingCircleModel = new LoadingCircleModel(0, rand.nextInt(height), randColorValue, 1000, width / 2, height / 2, (int) Util.dp2px(getResources(), 3), (int)Util.dp2px(getResources(), 30), this, width, height, randomInt, rand.nextInt(delay));
            listLoadingCircleModel.add(loadingCircleModel);
        }
        for(int i = 0; i < total; i++) {
            int randColor = rand.nextInt(sizeColors);
            int randColorValue = listColor.get(randColor);
            int randomInt = 1;
            LoadingCircleModel loadingCircleModel = new LoadingCircleModel(0, rand.nextInt(height), randColorValue, 1000, width / 2, height / 2, (int) Util.dp2px(getResources(), 3), (int)Util.dp2px(getResources(), 30), this, width, height, randomInt, rand.nextInt(delay));
            listLoadingCircleModel.add(loadingCircleModel);
        }
        for(int i = 0; i < total; i++) {
            int randColor = rand.nextInt(sizeColors);
            int randColorValue = listColor.get(randColor);
            int randomInt = 2;
            LoadingCircleModel loadingCircleModel = new LoadingCircleModel(0, rand.nextInt(height), randColorValue, 1000, width / 2, height / 2, (int)Util.dp2px(getResources(), 3), (int)Util.dp2px(getResources(), 30), this, width, height, randomInt, rand.nextInt(delay));
            listLoadingCircleModel.add(loadingCircleModel);
        }
        for(int i = 0; i < total; i++) {
            int randColor = rand.nextInt(sizeColors);
            int randColorValue = listColor.get(randColor);
            int randomInt = 3;
            LoadingCircleModel loadingCircleModel = new LoadingCircleModel(0, rand.nextInt(height), randColorValue, 1000, width / 2, height / 2, (int)Util.dp2px(getResources(), 3), (int)Util.dp2px(getResources(), 30), this, width, height, randomInt, rand.nextInt(delay));
            listLoadingCircleModel.add(loadingCircleModel);
        }
    }

    public void setColor(int color) {
        postInvalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height =  MeasureSpec.getSize(heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        y = height;
        x = width;
        initPath();
    }

    private void initPath() {
        if(mPath == null) {
            mPath = new Path();
            mPath.addCircle(x / 2, y /2 , x / 2, Path.Direction.CW);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int size = listLoadingCircleModel.size();
        for (int i = 0; i < size; i ++) {
            LoadingCircleModel loadingCircleModel = listLoadingCircleModel.get(i);
            canvas.drawCircle(loadingCircleModel.getCurX(), loadingCircleModel.getCurY(), loadingCircleModel.getCurRadius(), loadingCircleModel.getPaint());
        }
    }

    public void startAnim() {
        int size = listLoadingCircleModel.size();
        for (int i = 0; i < size; i ++) {
            LoadingCircleModel loadingCircleModel = listLoadingCircleModel.get(i);
            loadingCircleModel.startAnim();
        }
    }
}