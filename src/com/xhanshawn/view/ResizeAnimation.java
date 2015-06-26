package com.xhanshawn.view;

import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Transformation;


public class ResizeAnimation extends Animation {
	final int startHeight;
	final int resizeHeight;

	View view;
 
	public ResizeAnimation(View view, int resizeHeight) {
		this.view = view;
		this.resizeHeight = resizeHeight;
		startHeight = view.getHeight();
	}
 
	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		int newHeight = (int) (startHeight + resizeHeight * interpolatedTime);
		view.getLayoutParams().height = newHeight;
		view.requestLayout();
	}
 
	@Override
	public void initialize(int Height, int height, int parentWidth, int parentHeight) {
		super.initialize(Height, height, parentWidth, parentHeight);
	}
 
	@Override
	public boolean willChangeBounds() {
		return true;
	}
}
