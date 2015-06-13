package com.xhanshawn.latalk;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ControlPanelActivity extends Activity {
	FrameLayout ll;
	int window_width;
	int window_height;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_control_panel);
		
		ll = (FrameLayout) findViewById(R.id.zoom_panel);
		
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		window_width = (int) (size.x* 1.1f);
		window_height =(int) (size.y* 1.1f);
		
        FrameLayout.LayoutParams init_params = new FrameLayout.LayoutParams(window_width,window_height);
        init_params.leftMargin = (size.x - window_width)/2;
        init_params.topMargin = (size.y - window_height)/2;
        
		ll.setLayoutParams(init_params);
		
		ll.setOnTouchListener(new View.OnTouchListener() {
			private int _xDelta;
			private int _yDelta;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				
				
				final int X = (int) event.getRawX();
			    final int Y = (int) event.getRawY();
			    switch (event.getAction() & MotionEvent.ACTION_MASK) {
			        case MotionEvent.ACTION_DOWN:
			            FrameLayout.LayoutParams lParams = (FrameLayout.LayoutParams) v.getLayoutParams();
			            _xDelta = X - lParams.leftMargin;
			            _yDelta = Y - lParams.topMargin;
			            break;
			        case MotionEvent.ACTION_UP:
			            break;
			        case MotionEvent.ACTION_POINTER_DOWN:
			            break;
			        case MotionEvent.ACTION_POINTER_UP:
			            break;
			        case MotionEvent.ACTION_MOVE:
			            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) v.getLayoutParams();
			            layoutParams.leftMargin = X - _xDelta;
			            layoutParams.topMargin = Y - _yDelta;
//			            layoutParams.height = window_height;
//			            layoutParams.width = window_width;
			            
//			            layoutParams.rightMargin = -250;
//			            layoutParams.bottomMargin = -250;
			            v.setLayoutParams(layoutParams);
			            break;
			    }
//			    v.invalidate();
			    return true;
			}
		});
	}
	
	
	
}