package com.customdrawer;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.text.SimpleDateFormat;
import java.util.List;

import relation.vankyshare.com.androidstudiocustomwidget.R;


public class ListViewForAds extends ListView {
	MyAdapter mDataAdapter;
	private SimpleDateFormat mSdf;
	OnItemCouponClickListener mOnItemCouponClickListener;
	private float precInterceptY;

	OnTouchListViewListener onTouchListViewListener;
	public boolean isDownwards() {
		return isDownwards;
	}
	interface OnTouchListViewListener{
		public void ontouch();
	}
	public void setDownwards(boolean downwards) {
		isDownwards = downwards;
	}

	boolean isDownwards = false;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				precInterceptY = event.getY();
				break;
			case MotionEvent.ACTION_MOVE:
				if ((event.getY()- precInterceptY > 0)) { //往下
					isDownwards = true;
					Log.i("moveAction", "listview moveDown");
				} else {
					isDownwards = false;
					Log.i("moveAction", "listview moveUp");
				}
		}
		if (onTouchListViewListener != null) {
			onTouchListViewListener.ontouch();
		}
		return super.onTouchEvent(event);
	}

	public interface OnItemCouponClickListener {
		void onUserGuideClick();
//		void onItemClickCoupon(CouponInfo couponInfo);
	}

	public ListViewForAds(Context context, AttributeSet attrs) {
		super(context, attrs);
		mSdf = new SimpleDateFormat("yyyy-MM-dd");

	}

	public void setDataList(List<String> list) {
		if (mDataAdapter == null) {
			mDataAdapter = new MyAdapter(getContext(), list);
			setAdapter(mDataAdapter);
		} else {
			mDataAdapter.setDataList(list);
			mDataAdapter.notifyDataSetChanged();
		}
	}

	public class MyAdapter extends BaseAdapter implements OnClickListener{
		List<String> dataList;
		Context context;
		LayoutInflater mLayoutInflater;
		public MyAdapter(Context context, List<String> dataList) {
			super();
			this.dataList = dataList;
			this.context = context;
			mLayoutInflater = LayoutInflater.from(context);
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			//			return dataList.size();
			return 20;
//			return dataList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder viewHolder = null;
			if(convertView == null) {
				convertView = mLayoutInflater.inflate(R.layout.item_ads, null);
				viewHolder = new ViewHolder();
				viewHolder.relativeLayout = (RelativeLayout) convertView.findViewById(R.id.relativelayout);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			if(position % 2 == 0) {
				viewHolder.relativeLayout.setBackgroundColor(Color.parseColor("#f8f8f8"));
			} else {
				viewHolder.relativeLayout.setBackgroundColor(Color.parseColor("#ffffff"));
			}
			return convertView;
		}


		public List<String> getDataList() {
			return dataList;
		}

		public void setDataList(List<String> dataList) {
			this.dataList = dataList;
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(mOnItemCouponClickListener != null) {
			/*	if(v.getId() == R.id.relativelayout_item) {
					CouponInfo couponInfo = (CouponInfo) v.getTag();
					mOnItemCouponClickListener.onItemClickCoupon(couponInfo);
				} else if(v.getId() == R.id.framelayout_user_guide) {
					mOnItemCouponClickListener.onUserGuideClick();	
				}*/
			}
		}

	}

	public static class ViewHolder {
		ImageView mImageView;
		TextView mTextViewTitle;
		TextView mTextViewSubTitle;
		TextView mTextViewInterest;
		RelativeLayout relativeLayout;
	}

	public OnItemCouponClickListener getmOnItemCouponClickListener() {
		return mOnItemCouponClickListener;
	}

	public void setmOnItemCouponClickListener(
			OnItemCouponClickListener mOnItemCouponClickListener) {
		this.mOnItemCouponClickListener = mOnItemCouponClickListener;
	}


	public int getScrollY2() {
		View c = getChildAt(0);
		if (c == null) {
			return 0;
		}
		int firstVisiblePosition = getFirstVisiblePosition();
		int top = c.getTop();
		return -top + firstVisiblePosition * c.getHeight() ;
	}
	public void  requestDisallowInterceptTouch(boolean intercepter){
		requestDisallowInterceptTouchEvent(intercepter);
	}

	public OnTouchListViewListener getOnTouchListViewListener() {
		return onTouchListViewListener;
	}

	public void setOnTouchListViewListener(OnTouchListViewListener onTouchListViewListener) {
		this.onTouchListViewListener = onTouchListViewListener;
	}
}