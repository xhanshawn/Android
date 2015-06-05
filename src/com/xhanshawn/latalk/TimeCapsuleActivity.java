package com.xhanshawn.latalk;

import java.util.ArrayList;

import com.xhanshawn.data.LatalkMessage;
import com.xhanshawn.util.AlertMessageFactory;
import com.xhanshawn.util.DataPassCache;
import com.xhanshawn.util.LocationInfoFactory;
import com.xhanshawn.util.MessageGetFactory;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
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
	ImageView tc_pic2_iv;
	Handler radar_handler1;
	Handler radar_handler2;
	Runnable radar_cir_1;
	TextView tc_txt_tv;
	
	AnimationSet radar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_time_capsule);
		getActionBar().hide();
		
		tc_pic1_iv = (ImageView) findViewById(R.id.time_capsule_pic1_iv);
		tc_pic1_iv.setImageResource(R.drawable.radar_circle_purple);
		

		
		radar = new AnimationSet(true);
		ScaleAnimation radar_search = new ScaleAnimation(0.3f, 1.0f, 0.3f, 1.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, 
				Animation.RELATIVE_TO_SELF, 0.5f);
		radar_search.setDuration(1800);
		radar.addAnimation(radar_search);
		
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
		LinearLayout time_capsule_ll = (LinearLayout) findViewById(R.id.time_capsule_ll);
		time_capsule_ll.setLayoutParams(new LinearLayout.LayoutParams(width, width));
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
	public class TimeCapsuleGetter extends AsyncTask<Integer, Void, Integer> {
		LatalkMessage first_message = null;
		
		@Override
		protected Integer doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			
			switch (params[0]) {
			
				case GET_TIMECAPSILE: {
					
					LocationInfoFactory location_info = new LocationInfoFactory(TimeCapsuleActivity.this);
					Location current_location = location_info.getCurrentLocation();
					
					
					
					messages.addAll(MessageGetFactory.getTimeCapsuleMessagesNearby(current_location));
					
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
