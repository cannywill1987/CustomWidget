package com.customwidget.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

public class Util {
	public static int dip2px(Context context,float dipValue){
		final float scale=context.getResources().getDisplayMetrics().density;
		return (int)(dipValue*scale+0.5f);
	}

	public static int px2dp(Context context,float pxValue){
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int)(pxValue/scale+0.5f);
	}
	public static float dp2px(Resources resources, float dp) {
		final float scale = resources.getDisplayMetrics().density;
		return  dp * scale + 0.5f;
	}

	public static float sp2px(Resources resources, float sp){
		final float scale = resources.getDisplayMetrics().scaledDensity;
		return sp * scale;
	}

	public static void arcTo(Canvas canvas, float x, float y, float radius, float startAngle, float sweepAngle, Paint paint, boolean center) {
		RectF rect = new RectF(x-radius,y-radius,x+radius,y+radius);
		canvas.drawArc(rect, startAngle, sweepAngle, center, paint);
	}

	public static void arcTo(Path path, float x, float y, float radius, float startAngle, float sweepAngle) {
		RectF rect = new RectF(x-radius,y-radius,x+radius,y+radius);
		path.arcTo(rect, startAngle, sweepAngle);
	}
}
