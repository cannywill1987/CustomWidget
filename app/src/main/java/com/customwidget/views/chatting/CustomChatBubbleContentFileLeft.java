package com.customwidget.views.chatting;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;

import com.customwidget.utils.Util;

/**
 * 左边的聊天文件加载中泡泡
 */
public class CustomChatBubbleContentFileLeft extends RelativeLayout {

	Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	Paint paintGradient = new Paint(Paint.ANTI_ALIAS_FLAG);
	Paint mPaintProgressBarBG = new Paint(Paint.ANTI_ALIAS_FLAG);
	Paint mPaintProgressBar = new Paint(Paint.ANTI_ALIAS_FLAG);

	int mRadius  = Util.dip2px(getContext(), 15);
	int mHeightExtra = 200;
	int mWidth;
	int mHeight;
	private int mProgressWidth = 0;
	private int mGradientColor = 0x30e3be;
	private LinearGradient mLinearGradient;
	RectF roundedRect = new RectF(mRadius-mRadius/1.4f+mRadius,
			mHeight - mRadius,
			mWidth-mRadius, 
			mHeight - mRadius + Util.dip2px(getContext(), 5));
	RectF roundedRectProgress;
	int mProgress;
	public CustomChatBubbleContentFileLeft(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		paint.setColor(Color.parseColor("#3499DD"));

		mPaintProgressBarBG.setStyle(Style.STROKE);
		mPaintProgressBarBG.setStrokeWidth(Util.dip2px(getContext(), 1));
		mPaintProgressBarBG.setColor(Color.parseColor("#8cc7f3"));

		mPaintProgressBar.setStrokeWidth(Util.dip2px(getContext(), 1));
		mPaintProgressBar.setColor(Color.parseColor("#b2f1ff"));
		 setWillNotDraw(false);
	}



	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		super.onLayout(changed, l, t, r, b);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mWidth = MeasureSpec.getSize(widthMeasureSpec);
		mHeight = MeasureSpec.getSize(heightMeasureSpec);
		mHeightExtra = mHeight - 2 * mRadius;	

		//		paintGradient.setColor(Color.WHITE);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		paint.setColor(Color.parseColor("#3499DD")); 
		Path path = new Path();
		//左边的往下45度圆弧
		Util.arcTo(path, - mRadius/1.4f, mRadius, mRadius, -45.0f, 45.0f);
		//左下角直线
		path.lineTo(mRadius-mRadius/1.4f, mHeight - mRadius);
		//左下角90度圆弧
		Util.arcTo(path, mRadius - mRadius / 1.4f + mRadius, mHeight - mRadius, mRadius, 180, -90);
		//下面直线
		path.lineTo(mWidth - mRadius, mHeight);
		//右下角圆弧
		Util.arcTo(path, mWidth - mRadius, mHeight - mRadius, mRadius, 90, -90);
		//右边直线
		path.lineTo(mWidth, mRadius);
		//右上角圆弧
		Util.arcTo(path, mWidth - mRadius, mRadius, mRadius, 0, -90);
		//上角直线
		path.lineTo(mRadius - mRadius / 1.4f + mRadius, 0);
		//左上角圆弧
		Util.arcTo(path, mRadius - mRadius / 1.4f + mRadius, mRadius, mRadius, -90, -45);
		path.close();
		//		canvas.drawPath(path, paint);
		canvas.clipPath(path);
		canvas.drawPath(path, paint);
//		canvas.drawColor(Color.parseColor("#3499DD"));
		//		canvas.drawPath(path, paint);

		paintGradient.setShader(mLinearGradient);
		canvas.drawRect(0, 0, mProgressWidth, mHeight, paintGradient);
		canvas.drawRoundRect(roundedRect, Util.dip2px(getContext(), 8), Util.dip2px(getContext(), 8), mPaintProgressBarBG);
		if(roundedRectProgress != null) {
			canvas.drawRoundRect(roundedRectProgress, Util.dip2px(getContext(), 8), Util.dip2px(getContext(), 8), mPaintProgressBar);
		}


		/*		if(isRecordingType == true) {
			Util.drawArc(2 * radius + Util.dip2px(getContext(), 3), height / 2 + radius/2, Util.dip2px(getContext(), 20), 30, -60, false, canvas, paintRecord);
			Util.drawArc(2 * radius + Util.dip2px(getContext(), 3), height / 2 + radius/2, Util.dip2px(getContext(), 10), 30, -60, false, canvas, paintRecord);
			Util.drawArc(2 * radius + Util.dip2px(getContext(), 3), height / 2 + radius/2, Util.dip2px(getContext(), 15), 30, -60, false, canvas, paintRecord);
		}*/
	}

	public int getProgressWidth() {
		return mProgressWidth;
	}
	
	public void setProgressWithAnimation(int progress) {
		ObjectAnimator mProgressBarAnimator;
		mProgressBarAnimator = ObjectAnimator.ofInt(this, "Progress", this.getmProgress(), progress);
		mProgressBarAnimator.setDuration(300);
		mProgressBarAnimator.start();
	}
	
	public void setProgress(int progress) {
		this.mProgress = progress;
		roundedRect = new RectF(mRadius-mRadius/1.4f+mRadius,
				mHeight - mRadius,
				mWidth-mRadius, 
				mHeight - mRadius + Util.dip2px(getContext(), 5));
		Log.i("progress", "" + progress);
		this.mProgressWidth = progress * mWidth / 100;
		mLinearGradient = new LinearGradient(mProgressWidth - Util.dip2px(getContext(), 40), mHeight/2, mProgressWidth, mHeight/2, new int[]{ 0x00000000, 0xFF2fe2c2}, null, Shader.TileMode.CLAMP);
		if(mProgressWidth >  mRadius - mRadius / 1.4f + mRadius && mProgressWidth <= mWidth - mRadius) {
			roundedRectProgress = new RectF(mRadius-mRadius/1.4f+mRadius,
					mHeight - mRadius,
					mProgressWidth + mRadius / 1.4f - mRadius, 
					mHeight - mRadius + Util.dip2px(getContext(), 5));
		} else if(mProgressWidth > mWidth - mRadius){
			roundedRectProgress = new RectF(mRadius-mRadius/1.4f+mRadius,
					mHeight - mRadius,
					mWidth - mRadius, 
					mHeight - mRadius + Util.dip2px(getContext(), 5));
		} else {
			roundedRectProgress = new RectF(mRadius-mRadius/1.4f+mRadius,
					mHeight - mRadius,
					mRadius-mRadius/1.4f+mRadius, 
					mHeight - mRadius + Util.dip2px(getContext(), 5));
		}
		invalidate();
	}

	public int getmProgress() {
		return mProgress;
	}

}