package com.xhanshawn.latalk;

import java.util.ArrayList;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.xhanshawn.data.LatalkMessage;
import com.xhanshawn.util.AlertMessageFactory;
import com.xhanshawn.util.AnimationFactory;
import com.xhanshawn.util.DataPassCache;
import com.xhanshawn.util.LocationInfoFactory;
import com.xhanshawn.util.MessageGetFactory;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class TimeCapsuleActivity extends Activity {
	
	final static int GET_TIMECAPSILE = 10;
	final static int GET_PIC = 11;
	final static int UPDATE_FIRST = 12;
	final static int LOADING_TIME = 300;
	
	private ArrayList<LatalkMessage> messages = new ArrayList<LatalkMessage> ();
	
	//views
	ImageView tc_radar1_iv;
	ImageView tc_radar2_iv;
	RelativeLayout tc_panel;
	ImageView tc_iv;
	TextView tc_txt_tv;
	RelativeLayout radar_panel_rl;
	RelativeLayout tc_panel_rl;
	GoogleMap time_c_map;
	ActionBar mActionBar;
	
	//utilities
	Handler radar_handler1;
	Handler radar_handler2;
	Runnable radar_cir_1;
	AnimationSet radar;
	Location current_location;
	
	//parameters
	int width;
	int height;
	private static int read_num = 0 ;
	private int request_num;
	
	boolean radar_started = false;
	boolean radar_stoped = false;
	boolean message_added = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_time_capsule);
		
		//set customized action bar
		customActionBar();
		
		//get location info
		current_location = LocationInfoFactory.getCurrentLocation();
		if(current_location == null) {

			Toast location_closed_toast = Toast.makeText(TimeCapsuleActivity.this,
					AlertMessageFactory.locationClosedAlert(),
					Toast.LENGTH_LONG);

			location_closed_toast.show();
		}
		
		//get the screen dimensions
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		width = size.x;
		height = size.y;
		
		
		//set the rotation animation for loading
		radar_panel_rl = (RelativeLayout) findViewById(R.id.radar_panel_rl);
		tc_panel_rl = (RelativeLayout) findViewById(R.id.tc_panel_rl);
		
		tc_radar1_iv = (ImageView) findViewById(R.id.tc_radar1_iv);
		tc_radar2_iv = (ImageView) findViewById(R.id.tc_radar2_iv);

		//set basic panel layoutparams
		RelativeLayout.LayoutParams panel_params = (RelativeLayout.LayoutParams) radar_panel_rl.getLayoutParams();
		panel_params.leftMargin = (int) (width * 0.10);
		panel_params.width = width - 2 * panel_params.leftMargin;
		panel_params.height = (int) (panel_params.width * 1.2);
		radar_panel_rl.setLayoutParams(panel_params);
		
		//set rotated pointer layoutparams
		RelativeLayout.LayoutParams pointer_p = (RelativeLayout.LayoutParams) tc_radar2_iv.getLayoutParams();
		pointer_p.height = panel_params.width/2;
		pointer_p.width = (int) (panel_params.width/2);
		pointer_p.rightMargin = panel_params.width/2;
		tc_radar2_iv.setLayoutParams(pointer_p);
		

		ImageButton tc_like_ib = (ImageButton) findViewById(R.id.tc_like_ib);
		tc_like_ib.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				//down animation
				if(event.getAction() == MotionEvent.ACTION_DOWN) AnimationFactory.scaleImageButtonDown(v);
				
				//up animation and operation
				if(event.getAction() == MotionEvent.ACTION_UP) {
					AnimationFactory.scaleImageButtonUp(v);;
					if(radar_stoped) {
						updateCurrentTimeCapsule();
						if(tc_panel_rl.getChildCount() > 2 && tc_panel_rl.getChildAt(1) != v) {
							AnimationSet set = AnimationFactory.likeTimeCapsule();
							tc_panel_rl.getChildAt(1).startAnimation(set);
	                    	tc_panel_rl.removeViewAt(1);
	                    	message_added = false;
						}
					}
				}

				return false;
			}
		});
		
		updateCurrentTimeCapsule();
	}
	
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		read_num--;
	}

	///////////////////////Animations///////////////////

	//search radar animation
	private void searchRadar(){
		
		if(radar_panel_rl.getChildCount() < 2) {
			radar_panel_rl.addView(tc_radar1_iv);
			radar_panel_rl.addView(tc_radar2_iv);
		}
		radar = new AnimationSet(true);
		
		RotateAnimation rotate_p = new RotateAnimation(0, 
				360, 
				RotateAnimation.RELATIVE_TO_SELF, 
				1.0f, 
				RotateAnimation.RELATIVE_TO_SELF, 
				1.0f);
		
		rotate_p.setDuration(1800);
		rotate_p.setRepeatCount(-1);
		radar.addAnimation(rotate_p);
		tc_radar2_iv.setAnimation(radar);
		radar.setInterpolator(new LinearInterpolator());
		radar.start();
		radar_started = true;
	}
	
	//clear rotation animation
	private void clearRotation(){
		radar_started = false;
		tc_radar2_iv.clearAnimation();
		radar_stoped = true;
	}
	
	
	///////////////////////updates capsules///////////////////

	//update time capsule
	private void updateCurrentTimeCapsule() {
		
		if(messages.isEmpty() || messages.size() == read_num) {
			
			if(request_num <3) {
				if(request_num > 0) try{
					Thread.sleep(5000);
				}catch(Exception ex){}
				new TimeCapsuleGetter().execute(GET_TIMECAPSILE);
				
			} else {
				clearRotation();
				Toast no_tc_toast = Toast.makeText(TimeCapsuleActivity.this,
						AlertMessageFactory.noMessagesFound(),
						Toast.LENGTH_LONG);

				no_tc_toast.show();
			}

		} else {
			
			if(radar_started) clearRotation();
			showMessage();
		}
	}
	
	private void showMessage() {
		
		if(message_added) return;
		if(radar_stoped) tc_panel_rl.removeView(radar_panel_rl);
		
		if(messages.size() == read_num) {
			Toast no_tc_toast = Toast.makeText(TimeCapsuleActivity.this,
					AlertMessageFactory.noMessagesFound(),
					Toast.LENGTH_LONG);

			no_tc_toast.show();
			return;
		}
		LatalkMessage message = messages.get(read_num);
		
		//inflate the panel layout for showing time capsules;
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		tc_panel = (RelativeLayout) inflater.inflate(R.layout.tc_panel , null);
			
		tc_panel_rl.addView(tc_panel, 0);
		message_added = true;

		RelativeLayout.LayoutParams tc_params = (RelativeLayout.LayoutParams) tc_panel.getLayoutParams();
		tc_params.leftMargin = (int) (width * 0.1);
		tc_params.width = (int) (width - 2 * tc_params.leftMargin);
		tc_params.height = (int) (height * 0.6);
		tc_params.topMargin = (int) (width * 0.1);
		tc_panel.setLayoutParams(tc_params);
			
		tc_iv = (ImageView) tc_panel.findViewById(R.id.tc_iv);
			
		RelativeLayout.LayoutParams tc_p = (RelativeLayout.LayoutParams) tc_iv.getLayoutParams();
		tc_p.width = width - 2 * (int) (width * 0.10);
		tc_p.height = tc_p.width;
//		panel_params.width - 2 * (tc_p.topMargin - panel_params.leftMargin);
//		tc_iv.setLayoutParams(tc_p);
//		RelativeLayout.LayoutParams tc_c_p = (RelativeLayout.LayoutParams) tc_iv.getLayoutParams();
//		tc_c_p.height = (int) (tc_p.height * 0.15);
//		tc_c_panel_rl.setLayoutParams(tc_c_p);
		tc_txt_tv = (TextView) tc_panel.findViewById(R.id.tc_tv);
			
		tc_panel.setOnTouchListener(new View.OnTouchListener() {
				
	        private int _xDelta;
			private int _yDelta;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
				
				final int X = (int) event.getRawX();
				final int Y = (int) event.getRawY();
	        	RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) v.getLayoutParams();
			    switch (event.getAction() & MotionEvent.ACTION_MASK) {
			        case MotionEvent.ACTION_DOWN:
			        	RelativeLayout.LayoutParams lParams =  (RelativeLayout.LayoutParams) v.getLayoutParams();
			            _xDelta = X - lParams.leftMargin;				            
			            _yDelta = Y - lParams.topMargin;
				        break;
			        case MotionEvent.ACTION_UP:
			        	updateCurrentTimeCapsule();
			            if(layoutParams.leftMargin >= width - layoutParams.width ||
			            		(layoutParams.topMargin <= 0 && 
//			            		layoutParams.leftMargin < width - layoutParams.width && 
		            			layoutParams.leftMargin > (width - layoutParams.width)/2)) {
			            	AnimationSet set = AnimationFactory.likeTimeCapsule();
							v.startAnimation(set);
	                    	tc_panel_rl.removeView(v);
	                    	message_added = false;
			            } else if(layoutParams.leftMargin <= 0 || (layoutParams.topMargin <= 0 &&  
		            			layoutParams.leftMargin < (width - layoutParams.width)/2)) {
			            	AnimationSet set = AnimationFactory.dislikeTimeCapsule();
			            	v.startAnimation(set);
	                    	tc_panel_rl.removeView(v);
	                    	message_added = false;
			            } else {
			            	layoutParams.leftMargin = (width - layoutParams.width)/2;
				            layoutParams.topMargin = layoutParams.leftMargin;
				            layoutParams.rightMargin = -layoutParams.leftMargin;
				            layoutParams.bottomMargin = -layoutParams.topMargin;
				            v.setLayoutParams(layoutParams);
			            }
			            updateCurrentTimeCapsule();
			            break;
			        case MotionEvent.ACTION_POINTER_DOWN:
			            break;
			        case MotionEvent.ACTION_POINTER_UP:
			            break;
			        case MotionEvent.ACTION_MOVE:
			        	layoutParams.leftMargin = X - _xDelta;
			            layoutParams.topMargin = Y - _yDelta;
			            layoutParams.rightMargin = -layoutParams.leftMargin;
			            layoutParams.bottomMargin = -layoutParams.topMargin;
			            v.setLayoutParams(layoutParams);
			            
			            break;    
			    }

			    return true;
			}
		});
			
		tc_iv.setImageBitmap(message.getAttahedPic());
		tc_txt_tv.setText(message.getContent());
		read_num++;
	}
	
	
	//custom action bar
	private void customActionBar(){
		
		
		mActionBar = getActionBar();
		mActionBar.show();
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.actionbar_color_with_img,null);
		
		mActionBar.setDisplayShowCustomEnabled(true);

		mActionBar.setCustomView(v);
	    mActionBar.setBackgroundDrawable(getResources().getDrawable(R.color.purple));

		Button back_to_main_b = (Button) v.findViewById(R.id.color_ab_back_b);
	    back_to_main_b.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TimeCapsuleActivity.this.finish();
			}
		});
	    
	    ImageButton tc_map_switch_b = (ImageButton) v.findViewById(R.id.color_ab_ib);
	    tc_map_switch_b.setImageResource(R.drawable.map_switch_white);
	    tc_map_switch_b.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent tc_map_activity = new Intent("com.xhanshawn.latalk.TIMECAPSULEMAPACTIVITY");
				startActivity(tc_map_activity);
			}
		});
	    
	    TextView banner_tv = (TextView) findViewById(R.id.actionbar_color_banner);
	    banner_tv.setText("Time Capsule");
	}
	
	
	class TimeCapsuleGetter extends AsyncTask<Integer, Void, Boolean> {
		
		//radar
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			if(!radar_started) searchRadar();
		}

		//retrieve tc
		@Override
		protected Boolean doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			
			while(messages.isEmpty() || messages.size() == read_num){
				if(request_num > 2) break;
				else if(request_num > 0) {
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				MessageGetFactory.getTimeCapsuleMessagesNearby(current_location);
				messages.addAll(DataPassCache.getTimeCapsules(DataPassCache.ALL));
				request_num ++;
			}
			return !(messages.isEmpty() || messages.size() == read_num);
		}
		
		//post execution
		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			if(result) {
				new TimeCapsuleUpdater().execute(GET_PIC);
				new TimeCapsuleUpdater().execute(UPDATE_FIRST);
			}
			else {
				clearRotation();
				Toast no_tc_toast = Toast.makeText(TimeCapsuleActivity.this,
						AlertMessageFactory.noMessagesFound(),
						Toast.LENGTH_LONG);

				no_tc_toast.show();
			}
		}
	}
	
	//Async updating
	class TimeCapsuleUpdater extends AsyncTask<Integer, Void, Integer> {
		LatalkMessage first_message = null;
		LatalkMessage second_message = null;
		@Override
		protected Integer doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			
			switch (params[0]) {
			
				case GET_PIC: {
					
					if(!messages.isEmpty()) {
						
						int count = read_num;
						while(count < messages.size()) {
							
							if(messages.get(count).getAttahedPic() == null) {
								String url = messages.get(count).getPicUrl();
								if(url != null && !url.equals("")) {
									Bitmap pic = MessageGetFactory.getImage(url);
									messages.get(count).setAttachedPic(pic);
								}
							}
							
							count ++;
						}
					}
					break;
				}
				
				case UPDATE_FIRST: {
					
					int count = 0;
					boolean second  = false;
					while(count < 100) {
						
						if(!second) {
							first_message = DataPassCache.getTimeCapsule();
							second = true;
						}
						if(second) second_message = DataPassCache.getTimeCapsule();
						if(second_message != null || count >= 1 && DataPassCache.getTCSize() == 1) break;
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {}
						count++;
					}
					
					if(first_message == null) {
						
						tc_txt_tv.setText(AlertMessageFactory.loadingMessageFailed());
					} else {
						
						messages.add(first_message);
						if(second_message != null) messages.add(second_message);
					}
					
					Bitmap img = first_message.getAttahedPic();
					if(img == null) {
						String url = first_message.getPicUrl();
						img = MessageGetFactory.getImage(url);
					}
					first_message.setAttachedPic(img);
					break;
				}
				
				default: break;
			}
			 
			return params[0];
		}
		
		
		//update first
		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			switch(result) {
				
				case UPDATE_FIRST: {

					runOnUiThread(new Runnable(){

						@Override
						public void run() {
							// TODO Auto-generated method stub
							
							clearRotation();
							
							showMessage();
							message_added = false;
						}
					});

					break;
				}
				
				default: break;
			}
			
		}
		
	}
	
}
