package com.customdrawer;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.customwidget.utils.Util;


public class CustomTextView extends View {
    private String text = "Hello World";
    private Paint paint;
    private int width;
    private int size;
    private  ComposeShader mComposeShader;
    private Context mContext;
    private int length;
    private int space = 100;
    private int decalage = 300;
    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setWillNotDraw(false);
        paint = new Paint();
        paint.setColor(Color.BLACK);
        size = (int) Util.dp2px(context.getResources(), 11);
        paint.setTextSize(size);
        LinearGradient lg  = new LinearGradient(0 + decalage,0,space + decalage,space,new int[] {
                Color.parseColor("#99FFFFFF"),Color.WHITE , Color.parseColor("#99FFFFFF")}, null,
                Shader.TileMode.CLAMP);
        paint.setShader(lg);
        length = (int)paint.measureText(text) ;
    }

    public void setX(int x) {
//        decalage = x;
//        decalage = (length + space + 30)* x/ 100 - space;
        decalage = -space + (length + space - (int)Util.dp2px(getResources(), 30)) *x /100;
        size = (int) Util.dp2px(mContext.getResources(), 11);
        paint.setTextSize(size);
        LinearGradient lg  = new LinearGradient(0 + decalage,0,space + decalage,space,new int[] {
                Color.parseColor("#99FFFFFF"),Color.WHITE , Color.parseColor("#99FFFFFF")}, null,
                Shader.TileMode.CLAMP);
        paint.setShader(lg);
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        start();
    }

    public void start() {
        ObjectAnimator animator1 = ObjectAnimator.ofInt(this, "x", 0, length);
        animator1.setDuration(4300);
        animator1.setInterpolator(new LinearInterpolator());
        animator1.setRepeatCount(ValueAnimator.INFINITE);
        animator1.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText(text, (width - length) / 2, size, paint);
    }
}