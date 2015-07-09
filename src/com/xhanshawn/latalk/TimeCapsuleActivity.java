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
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TimeCapsuleActivity extends Activity {
	
	final static int GET_TIMECAPSILE = 10;
	final static int GET_PIC = 11;
	final static int UPDATE_FIRST = 12;
	
	final static int LOADING_TIME = 300;

	
	private ArrayList<LatalkMessage> messages = new ArrayList<LatalkMessage> ();
	private int read_num = 0 ;
	private int request_num;
	ImageView tc_pic1_iv;
	Handler radar_handler1;
	Handler radar_handler2;
	Runnable radar_cir_1;
	TextView tc_txt_tv;
	
	AnimationSet radar;
	GoogleMap time_c_map;
	ActionBar mActionBar;
	LocationInfoFactory location_info;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_time_capsule);
		customActionBar();
		
		location_info = new LocationInfoFactory(TimeCapsuleActivity.this);

		
		tc_pic1_iv = (ImageView) findViewById(R.id.time_capsule_pic1_iv);
		tc_pic1_iv.setImageResource(R.drawable.radar_circle_purple);
		

		
		radar = new AnimationSet(true);
		ScaleAnimation radar_search = new ScaleAnimation(0.3f, 1.0f, 0.3f, 1.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, 
				Animation.RELATIVE_TO_SELF, 0.5f);
		radar_search.setDuration(1800);
		radar.addAnimation(radar_search);
		
		ImageButton tc_map_switch_ib = (ImageButton) findViewById(R.id.map_switch_ib);
		tc_map_switch_ib.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent tc_map_activity = new Intent("com.xhanshawn.latalk.TIMECAPSULEMAPACTIVITY");
				startActivity(tc_map_activity);
			}
		});
		
		
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
		
		tc_txt_tv = (TextView) findViewById(R.id.time_capsule_txt_tv);
		tc_txt_tv.setText("123");
		
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		RelativeLayout time_capsule_ll = (RelativeLayout) findViewById(R.id.time_capsule_rl);
//		time_capsule_ll.setLayoutParams(new LinearLayout.LayoutParams(width, width));
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
	
	private void searchRadar(){
		
		tc_pic1_iv.setImageResource(R.drawable.radar_circle_purple);

		radar_handler1 = new Handler();
		
		radar_cir_1 = new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				tc_pic1_iv.startAnimation(radar);
				radar_handler1.postDelayed(radar_cir_1, 1800);

				
			}
		};
		radar_cir_1.run();
		
	}
	
	private void getTimeCapsules(){
		
		searchRadar();
		new TimeCapsuleGetter().execute(GET_TIMECAPSILE);
	}
	
	private void updateCurrentTimeCapsule() {
		
		if(messages.isEmpty() || messages.size() == read_num) {
			if(request_num <3) {
				
				getTimeCapsules();
			} else {
				
				Toast location_closed_toast = Toast.makeText(TimeCapsuleActivity.this,
						AlertMessageFactory.noMessagesFound(),
						Toast.LENGTH_LONG);

				location_closed_toast.show();
			}
		} else {
			
			LatalkMessage new_message = messages.get(read_num);
			Bitmap img = new_message.getAttahedPic();
			tc_pic1_iv.setImageBitmap(img);
			tc_txt_tv.setText(new_message.getContent());
			tc_txt_tv.setText(read_num + "");
			read_num++;
			
		}
		
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
					
					Location current_location = location_info.getCurrentLocation();
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
					
					radar_handler1.removeCallbacks(radar_cir_1);
					
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
							
							tc_pic1_iv.setImageBitmap(first_message.getAttahedPic());
							tc_txt_tv.setText(first_message.getContent());
							tc_txt_tv.setText(read_num + "");
						}
					});
					
					
					
					
					read_num++;
					break;
				}
				
				default: break;
			}
			
		}
		
	}
	
}
