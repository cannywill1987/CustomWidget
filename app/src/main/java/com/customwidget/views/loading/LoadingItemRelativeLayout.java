package com.customwidget.views.loading;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.customwidget.utils.Util;

public class LoadingItemRelativeLayout extends RelativeLayout {
    int cardBagColor = 0xffffffff;
    private int width;
    private int height;
    int radius;
    private Path mPath;
    private int y;
    private int x;
    private final Paint paint;

    public LoadingItemRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        /*TypedArray ta = context.obtainStyledAttributes(attrs,
                R.styleable.CardBag);
        cardBagColor = ta.getColor(
                R.styleable.CardBag_cardbag_color, cardBagColor);
        ta.recycle();*/
        radius = (int) Util.dp2px(getResources(), 10);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor("#ff0000"));
        setWillNotDraw(false);
    }

    public void setColor(int color) {
        paint.setColor(color);
        postInvalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height =  MeasureSpec.getSize(heightMeasureSpec);
        radius = height / 2;
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
            mPath.moveTo(0, radius);
            mPath.lineTo(0, y - radius);
            Util.arcTo(mPath, radius, y - radius, radius, 180, -90);
            mPath.lineTo(x - radius, y);
            Util.arcTo(mPath, x - radius, y - radius, radius, 90, -90);
            mPath.lineTo(x, radius / 2);
            Util.arcTo(mPath, x - radius, radius, radius, 0, -90);
            mPath.lineTo(radius, 0);
            Util.arcTo(mPath, radius, radius, radius, -90, -90);
            mPath.close();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initPath();
        canvas.drawPath(mPath, paint);
    }
}