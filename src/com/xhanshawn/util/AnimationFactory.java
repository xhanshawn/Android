package com.xhanshawn.util;

import com.google.android.gms.maps.model.Marker;
import com.xhanshawn.latalk.R;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class AnimationFactory {
	ImageButton mImageButton;
	Intent mActivity;
	Context mContext;
	
	public static void scaleButtonAnimation(View v){
		
		
		AnimationSet animation = new AnimationSet(true);

		ScaleAnimation scale1 = new ScaleAnimation(
				1.0f,0.85f,1.0f,0.85f,
				Animation.RELATIVE_TO_SELF,0.5f,
				Animation.RELATIVE_TO_SELF,0.5f);
		
		ScaleAnimation scale2 = new ScaleAnimation(
				1.0f,1.3f,1.0f,1.3f,
				Animation.RELATIVE_TO_SELF,0.5f,
				Animation.RELATIVE_TO_SELF,0.5f);

		scale1.setDuration(180);
		scale2.setDuration(200);
		scale2.setStartOffset(180);
		animation.addAnimation(scale1);
		animation.addAnimation(scale2);

		animation.setFillAfter(true);
		v.startAnimation(animation);
		
	}
	
	public void scaleButtonAndOpenActivity(ImageButton v, Intent m_activity, Context context){
		
		mImageButton = v;
		mActivity = m_activity;
		mContext = context;
		
		AnimationSet animation = new AnimationSet(true);

		ScaleAnimation scale1 = new ScaleAnimation(
				1.0f,0.85f,1.0f,0.85f,
				Animation.RELATIVE_TO_SELF,0.5f,
				Animation.RELATIVE_TO_SELF,0.5f);
		
		ScaleAnimation scale2 = new ScaleAnimation(
				1.0f,16.0f,1.0f,16.0f,
				Animation.RELATIVE_TO_SELF,0.5f,
				Animation.RELATIVE_TO_SELF,0.5f);
		
		AlphaAnimation alpha = new AlphaAnimation(1.0f, 0.50f);
		
		
		scale1.setDuration(80);
		scale2.setDuration(500);
		scale2.setStartOffset(80);
		
		alpha.setDuration(580);
		animation.addAnimation(alpha);

		animation.addAnimation(scale1);
		animation.addAnimation(scale2);
		animation.setAnimationListener(new AnimationListener(){
			
			@SuppressLint("NewApi")
			@SuppressWarnings("deprecation")
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) mImageButton.setAlpha(0);
				else mImageButton.setImageAlpha(0);
				new Handler().postDelayed(new Runnable()
				{
				   @Override
				   public void run()
				   {
						mContext.startActivity(mActivity);
				   }
				}, 400);
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
			}

		});
		v.startAnimation(animation);

//		animation.setFillAfter(true);
		
	}
	public static void scaleButtonAnimationSquentially(View v, int position){
		
		AnimationSet animation = new AnimationSet(true);

		ScaleAnimation scale1 = new ScaleAnimation(
				1.0f,0.85f,1.0f,0.85f,
				Animation.RELATIVE_TO_SELF,0.5f,
				Animation.RELATIVE_TO_SELF,0.5f);
		
		ScaleAnimation scale2 = new ScaleAnimation(
				1.0f,1.3f,1.0f,1.3f,
				Animation.RELATIVE_TO_SELF,0.5f,
				Animation.RELATIVE_TO_SELF,0.5f);
		
		scale1.setStartOffset(160*position);
		scale1.setDuration(80);
		scale2.setDuration(100);
		scale2.setStartOffset(160*position + 80);
		animation.addAnimation(scale1);
		animation.addAnimation(scale2);

//		animation.setFillAfter(true);
		v.startAnimation(animation);
	}
	
	public static void scaleImageButtonDown(View v) {
		AnimationSet set = new AnimationSet(true);
		ScaleAnimation scale = new ScaleAnimation(
				1.0f,0.85f,1.0f,0.85f,
				Animation.RELATIVE_TO_SELF,0.5f,
				Animation.RELATIVE_TO_SELF,0.5f);
		scale.setDuration(100);
		set.addAnimation(scale);
		set.setFillAfter(true);
		v.startAnimation(set);
	}
	
	public static void scaleImageButtonUp(View v) {
		AnimationSet set = new AnimationSet(true);
		ScaleAnimation scale = new ScaleAnimation(
				1.0f,1.2f,1.0f,1.2f,
				Animation.RELATIVE_TO_SELF,0.5f,
				Animation.RELATIVE_TO_SELF,0.5f);
		scale.setDuration(100);
		set.addAnimation(scale);
//		set.setFillAfter(true);
		v.startAnimation(set);
	}
	
	public static AnimationSet likeTimeCapsule() {
		
		AnimationSet set = new AnimationSet(true);
		TranslateAnimation right = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0.0f, 
				Animation.RELATIVE_TO_SELF, 1.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, 
				Animation.RELATIVE_TO_SELF, -0.3f);
		right.setDuration(300);
		set.addAnimation(right);
//		set.setFillAfter(true);
		return set;
	}
	
	public static AnimationSet dislikeTimeCapsule() {
		
		AnimationSet set = new AnimationSet(true);
		TranslateAnimation left = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0.0f, 
				Animation.RELATIVE_TO_SELF, -1.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, 
				Animation.RELATIVE_TO_SELF, -0.3f);
		left.setDuration(300);
		set.addAnimation(left);
//		set.setFillAfter(true);
		return set;
	}
	
	
	
	public static AnimationSet ZoomFromThumb(Marker marder){
		AnimationSet set = new AnimationSet(true);
		ScaleAnimation scale = new ScaleAnimation(
				1.0f,3.0f,1.0f,3.0f,
				Animation.RELATIVE_TO_SELF,0.5f,
				Animation.RELATIVE_TO_SELF,0.5f);
		scale.setDuration(300);
		set.addAnimation(scale);
		return set;
	}
	
	public static void switchVisibility(View v) {
		if(v != null){
			if(v.getVisibility() == View.INVISIBLE) v.setVisibility(View.VISIBLE);
			else v.setVisibility(View.INVISIBLE);
		}
	}
	
}
