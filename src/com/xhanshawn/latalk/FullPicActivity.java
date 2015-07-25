package com.xhanshawn.latalk;

import com.apple.eawt.event.GestureListener;
import com.xhanshawn.data.LatalkMessage;
import com.xhanshawn.util.DataPassCache;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class FullPicActivity extends Activity {
	ImageView pic_iv;
	GestureDetector gd;
	int width;
	int height;
	double zoom_ratio;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		//get the screen dimensions
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		width = size.x;
		height = size.y;
		zoom_ratio = height/width;

		setContentView(R.layout.activity_full_pic);
		pic_iv = (ImageView) findViewById(R.id.full_screen_iv);
		
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) pic_iv.getLayoutParams();
		params.width = width;
		params.height = width;
		pic_iv.setLayoutParams(params);
		
		int id = Integer.valueOf((String) getIntent().getExtras().get("id"));
		LatalkMessage message = DataPassCache.getTCById(id);
		pic_iv.setImageBitmap(message.getAttahedPic());
		gd = new GestureDetector(FullPicActivity.this, new GestureDetector.SimpleOnGestureListener() {

			@Override
			public boolean onDoubleTap(MotionEvent e) {
				// TODO Auto-generated method stub
				RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) pic_iv.getLayoutParams();
				if(params.height < height) {
					params.height = height;
					params.width = height;
					pic_iv.setLayoutParams(params);
				} else {
					params.width = width;
					params.height = width;
					pic_iv.setLayoutParams(params);
				}
				
//				pic_iv.setImageResource(R.drawable.camera_icon);
				return super.onDoubleTap(e);
			}
		});
		
		pic_iv.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return gd.onTouchEvent(event);
			}
		});
		
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return gd.onTouchEvent(event);
	}
	
}
