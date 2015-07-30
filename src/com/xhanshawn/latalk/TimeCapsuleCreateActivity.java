package com.xhanshawn.latalk;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;

import com.xhanshawn.data.LatalkMessage;
import com.xhanshawn.data.UserAccount;
import com.xhanshawn.util.AlertMessageFactory;
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
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


public class TimeCapsuleCreateActivity extends Activity {
	private String message_content = null;
	private EditText message_input;
	private TextView location_message_tv;
	private ActionBar actionBar;
	
	
	private float current_latitude;
	private float current_longitude;
	private boolean location_is_enabled;
	private Bitmap taken_pic;
	private LatalkMessage tc = new LatalkMessage();
	TimePicker tp;
	DatePicker dp;
	TextView tc_hold_time_tv;
	
	ImageView attached_pic_iv;
	RelativeLayout tp_panel_rl;
	TextView time_picker_tv;
	
	long hold_time = 0;
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
					Intent puzzle_create_activity = new Intent("com.xhanshawn.latalk.CAMERAACTIVITY");
					startActivityForResult(puzzle_create_activity, IntegerIdentifiers.ATTACH_IMG_FROM_CAM); 
				}
				
				return false;
			}
		});
		
		ImageButton add_pic_f_g_ib = (ImageButton) findViewById(R.id.add_pic_gallery_ib);
		
		add_pic_f_g_ib.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				
				ImageButton ib = (ImageButton) v;
				
				if(event.getAction() == MotionEvent.ACTION_DOWN) {
					
					ib.setImageResource(R.drawable.landscape_pink_icon);
				}
				
				if(event.getAction() == MotionEvent.ACTION_UP) {
					
					ib.setImageResource(R.drawable.landscape_grey_icon);
					Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
					photoPickerIntent.setType("image/*");
					startActivityForResult(photoPickerIntent, IntegerIdentifiers.SELECT_PHOTO); 
				}
				
				return false;
			}
		});
		
		tc_hold_time_tv = (TextView) findViewById(R.id.tc_hold_time_tv);
		
		
		
		attached_pic_iv = (ImageView) findViewById(R.id.attached_pic_iv);
		
		
		tp = new TimePicker(TimeCapsuleCreateActivity.this);
		tp.setIs24HourView(true);
//		hold_time = new Date().getTime()/(60 * 1000);
		
		
		dp = new DatePicker(TimeCapsuleCreateActivity.this);
		dp.setCalendarViewShown(false);
		dp.setId(1);
		final Calendar current = Calendar.getInstance();
		Calendar minDate = Calendar.getInstance();
		
		minDate.set(minDate.get(Calendar.YEAR),
				0, 
				1);
		dp.setMinDate(minDate.getTimeInMillis());
		dp.init(current.get(Calendar.YEAR),
				current.get(Calendar.MONTH), 
				current.get(Calendar.DAY_OF_MONTH), 
				new OnDateChangedListener() {
					
					@Override
					public void onDateChanged(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						// TODO Auto-generated method stub
						Calendar c = Calendar.getInstance();
						
						c.set(Calendar.YEAR, year);
						c.set(Calendar.MONTH, monthOfYear);
						c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
						Date date = new Date();
//						int day = p.getDays();
//						int hour = (int) (hold_time % (24 * 60)/60);
						int min_diff = (int) (hold_time % (24 * 60));
						//dst
						long mill = c.getTimeInMillis() + min_diff * 60 * 1000 - date.getTime();
						hold_time = mill / (60 * 1000);
						int day = (int) ((hold_time / (24 * 60))% 365) ;
						int year_diff = (int) (hold_time / (365 * 24 * 60));
						int hour = (int) (hold_time % (24 * 60)) / 60;
						int min = (int) (hold_time%60);
						String str = "Hold Time: ";
						if(hold_time > 0) str += 
								((year_diff > 0) ?  (year_diff + ((year_diff > 1) ? "years " : "year ")) : "") +
								((day > 0) ? (day + ((day > 1) ? "days " : "day ")) : "") +
								((hour > 0) ? (hour + ((hour > 1) ? "hours " : "hour ")) : "") + 
								((min > 0) ? (min + ((min > 1) ? "mins " : "min ")) : "");
						else str = "Please choose a time later than now";
						tc_hold_time_tv.setText(str);
					}
			
		});
		
		
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.RIGHT_OF, dp.getId());
		tp.setLayoutParams(params);
		
		
		tp.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				// TODO Auto-generated method stub
				Calendar c = Calendar.getInstance();
				
				c.set(Calendar.HOUR_OF_DAY, hourOfDay);
				c.set(Calendar.MINUTE, minute);
				long time_diff = (c.getTimeInMillis() - new Date().getTime())/(60 * 1000);
				int hour = (int) (time_diff % (24 * 60))/ 60;
				int min = (int) (time_diff % (24 * 60))% 60;
				hold_time = (hold_time/(60 * 24)) * 60 * 24 + hour * 60 + min;
				
				int day = (int) (hold_time/(24 * 60)) % 365;
				int year = (int) (hold_time/(24 * 60)) / 365;
				String str = "Hold Time: ";
				if(hold_time > 0) str += 
						((year > 0) ?  (year + ((year > 1) ? "years " : "year ")) : "") +
						((day > 0) ? (day + ((day > 1) ? "days " : "day ")) : "") +
						((hour > 0) ? (hour + ((hour > 1) ? "hs " : "h ")) : "") + 
						((min > 0) ? (min + ((min > 1) ? "mins " : "min ")) : "");
				else str = "Please choose a time later than now";

				tc_hold_time_tv.setText(str);
			}
			
		});
		
		time_picker_tv = new TextView(TimeCapsuleCreateActivity.this);
		time_picker_tv.setText(AlertMessageFactory.pickHoldTime());
		RelativeLayout.LayoutParams params_tv = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		params_tv.addRule(RelativeLayout.BELOW, dp.getId());
		params_tv.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		time_picker_tv.setLayoutParams(params_tv);
		
		tp_panel_rl = (RelativeLayout) findViewById(R.id.tp_panel_rl);
		
		ImageButton time_picker_ib = (ImageButton) findViewById(R.id.time_picker_ib);
		
		time_picker_ib.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				
				ImageButton ib = (ImageButton) v;
				
				if(event.getAction() == MotionEvent.ACTION_DOWN) {
					
					ib.setImageResource(R.drawable.clock_icon_pink);
				}
				
				if(event.getAction() == MotionEvent.ACTION_UP) {
					
					ib.setImageResource(R.drawable.clock_icon_grey);
					if(tp_panel_rl.getChildCount() > 0) tp_panel_rl.removeAllViews();
					else {
						tp_panel_rl.addView(dp);
						tp_panel_rl.addView(tp);
						tp_panel_rl.addView(time_picker_tv);
					}
				}
				
				return false;
			}
		});
		
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
		switch(requestCode) {
		case IntegerIdentifiers.SELECT_PHOTO:
	        if(resultCode == RESULT_OK){
	        	if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2 || data.getData() != null) {
	        		
	        		
	        		Uri data_uri = data.getData();
	        		InputStream is = null;
	        		try {
	        			is = getContentResolver().openInputStream(data_uri);
	        		} catch (FileNotFoundException e) {
	        			// TODO Auto-generated catch block
	        			e.printStackTrace();
	        		}
	        		taken_pic = BitmapFactory.decodeStream(is);
	        		tc.setAttachedPic(taken_pic);
	        		attached_pic_iv.setImageBitmap(taken_pic);
	        	}
	        }
	        break;
		case IntegerIdentifiers.ATTACH_IMG_FROM_CAM:
			Bundle extras = data.getExtras();
			
			int key = extras.getInt("pic_key");
			byte[] byte_array = DataPassCache.getPicByKey(key);
			
			taken_pic = BitmapFactory.decodeByteArray(byte_array, 0, byte_array.length);
			attached_pic_iv.setImageBitmap(taken_pic);
			break;
			
		default: break;
		}
	}

	
	private void customActionBar(){
		actionBar = getActionBar();
		actionBar.show();
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.actionbar_color_with_img, null);
		
	    actionBar.setDisplayShowCustomEnabled(true);

	    actionBar.setCustomView(v);
	    actionBar.setBackgroundDrawable(getResources().getDrawable(R.color.pink));
	    
	    Button back_to_main_b = (Button) v.findViewById(R.id.color_ab_back_b);
	    back_to_main_b.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TimeCapsuleCreateActivity.this.finish();
			}
		});
	    
	    
	    ImageButton post_ib = (ImageButton) v.findViewById(R.id.color_ab_ib);
	    post_ib.setImageResource(R.drawable.capsule_white);
	    post_ib.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LatalkMessage message = new LatalkMessage();
				if(location_is_enabled) {
					message.setLatitude(current_latitude);
					message.setLongitude(current_longitude);
				} else {
					message.setLatitude(LatalkMessage.NO_LATITUDE);
					message.setLongitude(LatalkMessage.NO_LONGITUDE);
				}
				message_content = message_input.getText().toString();
				message.setContent(message_content);
				message.setHold_time(hold_time * 60);
				message.setUserName(UserAccount.getCurrentUserName());
				message.setMessageType(LatalkMessage.TIME_CAPSULE);
				message.setAttachedPic(taken_pic);
				new MessagePoster().execute(message);
			}
		});
	    
	    TextView banner_tv = (TextView) findViewById(R.id.actionbar_color_banner);
	    banner_tv.setText("Time Capsule");
	}
	

	private void getCurrentLocation(){
		
		Location current_location = LocationInfoFactory.getCurrentLocation();
	
		if(current_location==null){
			
			location_message_tv.setText("Location not available");
			location_is_enabled = false;
		}else{
			
			location_is_enabled = true;

			current_latitude = (float)current_location.getLatitude();
			current_longitude = (float)current_location.getLongitude();
			location_message_tv.setText(current_latitude +" ");
			location_message_tv.append(" " + current_longitude);
		}
	}
	
	
	public class MessagePoster extends AsyncTask<LatalkMessage, Void, Boolean> {


		@Override
		protected Boolean doInBackground(LatalkMessage... params) {
			// TODO Auto-generated method stub
			boolean result = MessagePostFactory.postLatalkMessage(params[0]);
			
			return result;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(result) 	TimeCapsuleCreateActivity.this.finish();
			else {
				Toast post_failed_toast = Toast.makeText(TimeCapsuleCreateActivity.this,
						AlertMessageFactory.postFailed(),
						Toast.LENGTH_LONG);

				post_failed_toast.show();
			}
					

		}
		
		
		
	}
}
