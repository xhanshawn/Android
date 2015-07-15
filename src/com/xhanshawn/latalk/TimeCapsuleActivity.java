package com.xhanshawn.latalk;

import java.util.ArrayList;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.xhanshawn.data.LatalkMessage;
import com.xhanshawn.latalk.PuzzleRaceCreateActivity.MessagePoster;
import com.xhanshawn.util.AlertMessageFactory;
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
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
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
	private static int read_num = 0 ;
	private int request_num;
	ImageView tc_radar1_iv;
	ImageView tc_radar2_iv;
	RelativeLayout tc_panel;
	ImageView tc_iv;

	
	Handler radar_handler1;
	Handler radar_handler2;
	Runnable radar_cir_1;
	TextView tc_txt_tv;
	RelativeLayout radar_panel_rl;
	
	AnimationSet radar;
	GoogleMap time_c_map;
	ActionBar mActionBar;
	LocationInfoFactory location_info;
	Location current_location;
	
	int width;
	int height;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_time_capsule);
		
		//set customized action bar
		customActionBar();
		
		//get location info
		location_info = new LocationInfoFactory(TimeCapsuleActivity.this);
		current_location = location_info.getCurrentLocation();
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
		tc_radar1_iv = (ImageView) findViewById(R.id.tc_radar1_iv);
		tc_radar2_iv = (ImageView) findViewById(R.id.tc_radar2_iv);

		//set basic panel layoutparams
		LinearLayout.LayoutParams panel_params = (LinearLayout.LayoutParams) radar_panel_rl.getLayoutParams();
		panel_params.leftMargin =(int) (width * 0.10);
		panel_params.width = width - 2 * panel_params.leftMargin;
		panel_params.height = (int) (panel_params.width * 1.2);
		radar_panel_rl.setLayoutParams(panel_params);
		
		//set rotated pointer layoutparams
		RelativeLayout.LayoutParams pointer_p = (RelativeLayout.LayoutParams) tc_radar2_iv.getLayoutParams();
		pointer_p.height = panel_params.width/2;
		pointer_p.width = (int) (panel_params.width/2);
		pointer_p.rightMargin = panel_params.width/2;
		tc_radar2_iv.setLayoutParams(pointer_p);
		
		
		tc_txt_tv = (TextView) findViewById(R.id.time_capsule_txt_tv);
		
		//inflate the panel layout for showing time capsules;
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		tc_panel = (RelativeLayout) inflater.inflate(R.layout.tc_panel , null);
		tc_iv = (ImageView) tc_panel.findViewById(R.id.tc_iv);
//		RelativeLayout tc_c_panel_rl = (RelativeLayout) tc_panel.findViewById(R.id.tc_c_panel_rl);
		
		RelativeLayout.LayoutParams tc_p = (RelativeLayout.LayoutParams) tc_iv.getLayoutParams();
		tc_p.height = panel_params.width - 2 * (tc_p.topMargin - panel_params.leftMargin);
//		tc_iv.setLayoutParams(tc_p);
		tc_txt_tv.setText(tc_p.height + "      " + panel_params.height);

		RelativeLayout.LayoutParams tc_c_p = (RelativeLayout.LayoutParams) tc_iv.getLayoutParams();
		tc_c_p.height = (int) (tc_p.height * 0.15);
//		tc_c_panel_rl.setLayoutParams(tc_c_p);
		
		Button search_b = (Button) findViewById(R.id.radar_search_b);
		search_b.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				updateCurrentTimeCapsule();
			}
		});

		
		updateCurrentTimeCapsule();
		new TimeCapsuleGetter().execute(UPDATE_FIRST);
		
	}
	
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		read_num--;
	}



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
		
	}
	
	//clear rotation animation
	private void clearRotation(){
		tc_radar2_iv.clearAnimation();
	}
	
	//retrieve time capsules from server
	private void getTimeCapsules(){
		
		searchRadar();
		new TimeCapsuleGetter().execute(GET_TIMECAPSILE);
	}
	
	//update time capsule
	private void updateCurrentTimeCapsule() {
		
		if(messages.isEmpty() || messages.size() == read_num) {
			if(request_num <3) {
				
				getTimeCapsules();
			} else {
				
				Toast no_tc_toast = Toast.makeText(TimeCapsuleActivity.this,
						AlertMessageFactory.noMessagesFound(),
						Toast.LENGTH_LONG);

				no_tc_toast.show();
			}
		} else {
			
			LatalkMessage new_message = messages.get(read_num);
			Bitmap img = new_message.getAttahedPic();
			
			clearRotation();
			
			if(radar_panel_rl.getChildCount() >= 2) {
				radar_panel_rl.removeAllViews();
				radar_panel_rl.removeView(tc_radar2_iv);
				radar_panel_rl.addView(tc_panel);
				RelativeLayout.LayoutParams tc_params = (RelativeLayout.LayoutParams) tc_panel.getLayoutParams();
				tc_params.width = LayoutParams.WRAP_CONTENT;
				tc_params.height = (int) (height * 0.6);
				tc_panel.setLayoutParams(tc_params);
				tc_txt_tv.append( "\n" + tc_params.height + "      " );

			}
			
			tc_iv.setImageBitmap(img);
//			tc_txt_tv.setText(new_message.getContent());
			read_num++;
			
		}
		
	}
	
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
	    tc_map_switch_b.setImageResource(R.drawable.map_switch_icon);
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
	
	
	class TimeCapsuleGetter extends AsyncTask<Integer, Void, Integer> {
		LatalkMessage first_message = null;
		
		@Override
		protected Integer doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			
			switch (params[0]) {
			
				case GET_TIMECAPSILE: {
					runOnUiThread(new Runnable(){

						@Override
						public void run() {
							// TODO Auto-generated method stub
							
							}
					});
					
					messages.addAll(MessageGetFactory.getTimeCapsuleMessagesNearby(current_location, 1.0f));
					
					break;
				}
				
				case GET_PIC: {
					
					if(!messages.isEmpty()) {
						
						int count = 0;
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
					
					while(count < 100) {
						
						first_message = DataPassCache.getTimeCapsule();
						if(first_message != null) break;
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {}
					}
					
//					radar_handler1.removeCallbacks(radar_cir_1);
					
					if(first_message == null) {
						
						tc_txt_tv.setText(AlertMessageFactory.loadingMessageFailed());
					} else {
						
						messages.add(first_message);
						read_num++;
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
		
		
		
		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			request_num ++ ;
			
			switch(result) {
				
				case GET_TIMECAPSILE: {
					
					new TimeCapsuleGetter().execute(GET_PIC);
					break;
				}
				
				case UPDATE_FIRST: {
					
					

					runOnUiThread(new Runnable(){

						@Override
						public void run() {
							// TODO Auto-generated method stub
							
							clearRotation();
							
							if(radar_panel_rl.getChildCount() >= 2) {
								radar_panel_rl.removeAllViews();
								radar_panel_rl.removeView(tc_radar2_iv);
								radar_panel_rl.addView(tc_panel);
								RelativeLayout.LayoutParams tc_params = (RelativeLayout.LayoutParams) tc_panel.getLayoutParams();
								tc_params.width = LayoutParams.MATCH_PARENT;
								tc_params.height = (int) (height * 0.6);
								tc_panel.setLayoutParams(tc_params);
							}
							tc_iv.setImageBitmap(first_message.getAttahedPic());
//							tc_txt_tv.setText(first_message.getContent());
						}
					});
					
					
					
//					read_num++;
					break;
				}
				
				default: break;
			}
			
		}
		
	}
	
}
