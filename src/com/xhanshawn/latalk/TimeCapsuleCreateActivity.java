package com.xhanshawn.latalk;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import com.xhanshawn.data.LatalkMessage;
import com.xhanshawn.data.UserAccount;
import com.xhanshawn.util.AlertMessageFactory;
import com.xhanshawn.util.DataPassCache;
import com.xhanshawn.util.IntegerIdentifiers;
import com.xhanshawn.util.LocationInfoFactory;
import com.xhanshawn.util.MessagePostFactory;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
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
					Intent puzzle_create_activity = new Intent("com.xhanshawn.latalk.CAMERAACTIVITY");
					startActivityForResult(puzzle_create_activity, IntegerIdentifiers.ATTACH_PIC_IDENTIFIER); 
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
		tp.setBackgroundColor(getResources().getColor(R.color.transparent));
		dp = new DatePicker(TimeCapsuleCreateActivity.this);
		dp.setCalendarViewShown(false);
		dp.setId(1);
		final Calendar current = Calendar.getInstance();
		dp.init(current.get(Calendar.YEAR),
				current.get(Calendar.MONTH), 
				current.get(Calendar.DAY_OF_MONTH), 
				new OnDateChangedListener() {

					@Override
					public void onDateChanged(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						// TODO Auto-generated method stub
					}
			
		});
		
		
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.RIGHT_OF, dp.getId());
		tp.setLayoutParams(params);
		
		tp.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				// TODO Auto-generated method stub
				String hold_time = "";
				int ht_h = hourOfDay;
				tc_hold_time_tv.setText(hold_time);
			}
			
		});
		tp_panel_rl = (RelativeLayout) findViewById(R.id.tp_panel_rl);
		
		ImageButton time_picker_ib = (ImageButton) findViewById(R.id.time_picker_ib);
		
		time_picker_ib.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				
				ImageButton ib = (ImageButton) v;
				
				if(event.getAction() == MotionEvent.ACTION_DOWN) {
					
					ib.setImageResource(R.drawable.landscape_pink_icon);
				}
				
				if(event.getAction() == MotionEvent.ACTION_UP) {
					
					ib.setImageResource(R.drawable.landscape_grey_icon);
					tp_panel_rl.addView(dp);
					tp_panel_rl.addView(tp);
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
	        		Bitmap selected_img = BitmapFactory.decodeStream(is);
	        		tc.setAttachedPic(selected_img);
	        		attached_pic_iv.setImageBitmap(selected_img);
	        	}
	        }
	        break;
		case IntegerIdentifiers.ATTACH_PIC_IDENTIFIER:
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
		View v = inflater.inflate(R.layout.actionbar_color_with_img,null);
		
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
				TimeCapsuleCreateActivity.this.finish();
			}
		});
	    
	    TextView banner_tv = (TextView) findViewById(R.id.actionbar_color_banner);
	    banner_tv.setText("Time Capsule");
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
			location_message_tv.setText(current_latitude +" ");
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
