package com.xhanshawn.latalk;

import com.xhanshawn.data.LatalkMessage;
import com.xhanshawn.data.UserAccount;
import com.xhanshawn.util.DataPassCache;
import com.xhanshawn.util.IntegerIdentifiers;
import com.xhanshawn.util.LocationInfoFactory;
import com.xhanshawn.util.MessagePostFactory;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

public class TimeCapsuleCreateActivity extends Activity {
	private String message_content = null;
	private EditText message_input;
	private TextView location_message_tv;
	private ActionBar actionBar;
	
	
	private float current_latitude;
	private float current_longitude;
	private boolean location_is_enabled;
	private Bitmap taken_pic;
	
	
	ImageView attached_pic_iv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_time_capsule_create);
		message_input = (EditText) findViewById(R.id.message_input);
		
		
		customActionBar();
		
		
		location_message_tv = (TextView) findViewById(R.id.location_tv);
		
		location_message_tv.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		

		ImageButton post_message_ib = (ImageButton) findViewById(R.id.post_message_ib);
		
		
		post_message_ib.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				ImageButton ib = (ImageButton) v;
				
				if(event.getAction() == MotionEvent.ACTION_DOWN) {
					
					ib.setImageResource(R.drawable.capsule_pink);
				}
				
				if(event.getAction() == MotionEvent.ACTION_UP) {
					
					ib.setImageResource(R.drawable.capsule_grey);
				}
				return false;
			}
		});
		
//		post_message_ib.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				
//				
//				message_content = message_input.getText().toString();
//				LatalkMessage message = new LatalkMessage();
//				message.setContent(message_content);
//				message.setMessage_type("TimeCapsule");
//				
//				if(location_is_enabled){
//					
//					message.setLatitude(current_latitude);
//					message.setLongitude(current_longitude);
//				}
//				
//				
//				message.setHold_time(86400000000l);
//				message.setUser_name(UserAccount.getCurrentUserName());
//				
//				message.setAttachedPic(taken_pic);
//				
////				new MessagePoster().execute(message);
//			
//			}
//			
//		});
//		
		
		ImageButton add_pic_b = (ImageButton) findViewById(R.id.add_pic_ib);
		
		add_pic_b.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				
				ImageButton ib = (ImageButton) v;
				
				if(event.getAction() == MotionEvent.ACTION_DOWN) {
					
					ib.setImageResource(R.drawable.camera_icon_pink);
				}
				
				if(event.getAction() == MotionEvent.ACTION_UP) {
					
					ib.setImageResource(R.drawable.camera_icon_grey);
				}
				
				return false;
			}
		});
		
		
		
		
		attached_pic_iv = (ImageView) findViewById(R.id.attached_pic_iv);
	}
	
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		getCurrentLocation();

	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == IntegerIdentifiers.ATTACH_PIC_IDENTIFIER) {
			
			Bundle extras = data.getExtras();
			
			int key = extras.getInt("pic_key");
			byte[] byte_array = DataPassCache.getPicByKey(key);
			
			taken_pic = BitmapFactory.decodeByteArray(byte_array, 0, byte_array.length);
			attached_pic_iv.setImageBitmap(taken_pic);
		}
	}

	
	private void customActionBar(){
		actionBar = getActionBar();
		actionBar.show();
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.actionbar_create_time_capsule,null);
		
	    actionBar.setDisplayShowCustomEnabled(true);

	    actionBar.setCustomView(v);
	    
	    Button back_to_main_b = (Button) v.findViewById(R.id.c_t_to_main_b);
	    back_to_main_b.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TimeCapsuleCreateActivity.this.finish();
			}
		});
	    
	}
	

	private void getCurrentLocation(){
		LocationInfoFactory location_info = new LocationInfoFactory(TimeCapsuleCreateActivity.this);
		
		Location current_location = location_info.getCurrentLocation();
		
		if(current_location==null){
			
			location_message_tv.setText("Location not available");
			location_is_enabled = false;
		}else{
			
			location_is_enabled = true;

			current_latitude = (float)current_location.getLatitude();
			current_longitude = (float)current_location.getLongitude();
			location_message_tv.append(current_latitude +" ");
			location_message_tv.append(" " + current_longitude);
		}
	}
	
	
	public class MessagePoster extends AsyncTask<LatalkMessage, Void, Boolean> {


		@Override
		protected Boolean doInBackground(LatalkMessage... params) {
			// TODO Auto-generated method stub
			MessagePostFactory.postLatalkMessage(params[0]);
			
			return null;
		}
		
	}
}
