package com.xhanshawn.util;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.Button;

public class AnimationFactory {
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
	public AnimationSet scaleButtonAndOpenActivity(){
		
		AnimationSet animation = new AnimationSet(true);

		ScaleAnimation scale1 = new ScaleAnimation(
				1.0f,0.85f,1.0f,0.85f,
				Animation.RELATIVE_TO_SELF,0.5f,
				Animation.RELATIVE_TO_SELF,0.5f);
		
		ScaleAnimation scale2 = new ScaleAnimation(
				1.0f,16.0f,1.0f,16.0f,
				Animation.RELATIVE_TO_SELF,0.5f,
				Animation.RELATIVE_TO_SELF,0.5f);

		scale1.setDuration(80);
		scale2.setDuration(500);
		scale2.setStartOffset(80);
		animation.addAnimation(scale1);
		animation.addAnimation(scale2);

//		animation.setFillAfter(true);
		
		return animation;
		
		
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
		scale2.setStartOffset(180*position + 80);
		animation.addAnimation(scale1);
		animation.addAnimation(scale2);

//		animation.setFillAfter(true);
		v.startAnimation(animation);
	}
}
