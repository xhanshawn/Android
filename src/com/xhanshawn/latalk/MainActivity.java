package com.xhanshawn.latalk;

import java.util.ArrayList;

import com.xhanshawn.data.UserAccount;
import com.xhanshawn.util.AlertMessageFactory;
import com.xhanshawn.util.AnimationFactory;
import com.xhanshawn.util.LocationInfoFactory;
import com.xhanshawn.util.UserSessionManager;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ImageButton;

public class MainActivity extends Activity {
	
	private ActionBar action_bar = null;
	FrameLayout buttons_panel_fl;
	RelativeLayout buttons_panel_rl;
	ImageButton create_message_button;
	private ArrayList<ImageButton> image_buttons = new ArrayList<ImageButton>();
	
	
	
	Button R6;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		verifyLoginSession();
		CustomizeActionBar();
		
		//set buttons panel
		buttons_panel_fl = (FrameLayout) findViewById(R.id.buttons_panel_fl);
		buttons_panel_rl = (RelativeLayout) findViewById(R.id.buttons_panel_rl);
		
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int window_width = (int) (size.x* 1.1f);
		int window_height =(int) (size.y* 1.1f);
		
		FrameLayout.LayoutParams init_params = new FrameLayout.LayoutParams(window_width, window_height);
        init_params.leftMargin = (size.x - window_width)/2;
        init_params.topMargin = (size.y - window_height)/2;
        
        buttons_panel_fl.setLayoutParams(init_params);
        
        buttons_panel_fl.setOnTouchListener(new View.OnTouchListener() {
			
        	private int _xDelta;
			private int _yDelta;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				
				
				final int X = (int) event.getRawX();
			    final int Y = (int) event.getRawY();
			    switch (event.getAction() & MotionEvent.ACTION_MASK) {
			        case MotionEvent.ACTION_DOWN:
			        	FrameLayout.LayoutParams lParams =  (FrameLayout.LayoutParams) v.getLayoutParams();
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
		
        
        
		ImageButton options_button = (ImageButton)findViewById(R.id.options_button);
	    
	    options_button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent options_activity = new Intent("com.xhanshawn.latalk.OPTIONSACTIVITY");
				startActivity(options_activity);
			}
		}); 
		
	    ImageButton user_account_button = (ImageButton) findViewById(R.id.user_account_button);
	    user_account_button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent user_messages_activity = new Intent("com.xhanshawn.latalk.USERMESSAGESACTIVITY");
				startActivity(user_messages_activity);
			}
		});
	    
	    
	    
	    
	    
	    R6 = (Button) findViewById(R.id.reserved_button6);
	    
	    
	    create_message_button = (ImageButton) findViewById(R.id.create_message_button);
	    image_buttons.add(create_message_button);
		create_message_button.setOnClickListener(new View.OnClickListener() {
			
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent create_activity = new Intent("com.xhanshawn.latalk.CREATEACTIVITY");
				
				AnimationFactory anim_factory = new AnimationFactory();
				anim_factory.scaleButtonAndOpenActivity((ImageButton)v,
						create_activity, MainActivity.this);
				
			}
		});
		
		
		ImageButton puzzle_race_c_ib = (ImageButton) findViewById(R.id.puzzle_race_c_ib);
		image_buttons.add(puzzle_race_c_ib);
		puzzle_race_c_ib.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent puzzle_race_c_activity = new Intent("com.xhanshawn.latalk.PUZZLERACECREATEACTIVITY");
				AnimationFactory anim_factory = new AnimationFactory();
				anim_factory.scaleButtonAndOpenActivity((ImageButton)v, 
						puzzle_race_c_activity, MainActivity.this);
			}
		});
		
		
		
		
		ImageButton tc_browser_button = (ImageButton) findViewById(R.id.message_browser_button);
		image_buttons.add(tc_browser_button);
		tc_browser_button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent tc_browser_activity = new Intent("com.xhanshawn.latalk.TIMECAPSULEACTIVITY");
				AnimationFactory anim_factory = new AnimationFactory();
				anim_factory.scaleButtonAndOpenActivity((ImageButton)v, 
						tc_browser_activity, MainActivity.this);
			}
		});
		
		//sequentially scale buttons 
		scaleButtons();
		
	}
	
	
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		scaleButtons();
		resetButtons();
		
		LocationInfoFactory location_info = new LocationInfoFactory(MainActivity.this);
	}

	@SuppressLint("NewApi")
	private void resetButtons(){
		
		for(ImageButton ib : image_buttons) {

			if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) ib.setAlpha(255);
			else ib.setImageAlpha(255);
		}
	}
	
	private void scaleButtons(){
		int count = buttons_panel_rl.getChildCount();
		for(int i=0; i<count; i++) {
			
			AnimationFactory.scaleButtonAnimationSquentially(buttons_panel_rl.getChildAt(i), i);
		}
//		AnimationFactory.scaleButtonAnimationSquentially(create_message_button, 1);
	}
	
	private void checkSettings(){
		
		
	    
	}

	private void CustomizeActionBar(){
		
		
		
		action_bar = getActionBar();
		action_bar.show();
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.actionbar_main,null);
		
	    action_bar.setDisplayShowCustomEnabled(true);

	    action_bar.setCustomView(view);
	       
	}
	
	
	private void verifyLoginSession(){
		
		UserSessionManager manager = new UserSessionManager(getApplicationContext());
		if(!manager.isUserLoggedIn()){
			Intent login_activity = new Intent("com.xhanshawn.latalk.LOGINACTIVITY");
			startActivity(login_activity);
		}
		UserAccount.updateCurrentUserName(manager.getCurrentUserName());
	}
	
	
}
