package com.xhanshawn.latalk;

import java.util.ArrayList;

import com.xhanshawn.data.LatalkMessage;
import com.xhanshawn.util.AlertMessageFactory;
import com.xhanshawn.util.LocationInfoFactory;
import com.xhanshawn.util.MessageGetFactory;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class TimeCapsuleActivity extends Activity {
	
	private ArrayList<LatalkMessage> messages = null;
	private float offset = 0.000000f;
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
		
		
		tc_txt_tv = (TextView) findViewById(R.id.time_capsule_txt_tv);
		tc_txt_tv.setText("123");

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
		new TimeCapsuleGetter().execute("");
	}
	
	private void updateCurrentTimeCapsule() {
		
		if(messages == null || messages.size() == read_num) {
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
	public class TimeCapsuleGetter extends AsyncTask<String, Void, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			LocationInfoFactory location_info = new LocationInfoFactory(TimeCapsuleActivity.this);
			
			Location current_location = location_info.getCurrentLocation();
			if(current_location == null) {
				
				if(messages == null) {
					
					messages = MessageGetFactory.getTimeCapsuleMessagesNearby(TimeCapsuleActivity.this, 0);
				} else {
					
					messages.addAll(MessageGetFactory.getTimeCapsuleMessagesNearby(TimeCapsuleActivity.this, 0));
				}
				
			} else {
				
				while(messages == null || messages.size() == read_num){
					
					if(messages == null) {
						
						messages = MessageGetFactory.getTimeCapsuleMessagesNearby(TimeCapsuleActivity.this,offset);
					} else {
						
						messages.addAll(MessageGetFactory.getTimeCapsuleMessagesNearby(TimeCapsuleActivity.this,offset));
					}
					
					if(offset < 0.000008) offset += 0.000002f;
					if(offset >= 0.000008) offset += 0.00001f;
					if(offset >= 0.000048) offset += 0.0001f;
					if(offset >= 0.000148) offset += 0.001f;
					if(offset >= 0.001148) break;
				}
			}
			
			
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			request_num ++ ;
			if(result) radar_handler1.removeCallbacks(radar_cir_1);
			updateCurrentTimeCapsule();
		}
		
	}
	
}
