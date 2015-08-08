package com.xhanshawn.latalk;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xhanshawn.data.LatalkMessage;
import com.xhanshawn.data.UserAccount;
import com.xhanshawn.latalk.PuzzleRaceCreateActivity.PuzzleGridAdapter.ViewHolder;
import com.xhanshawn.util.AlertMessageFactory;
import com.xhanshawn.util.AnimationFactory;
import com.xhanshawn.util.DataPassCache;
import com.xhanshawn.util.IntegerIdentifiers;
import com.xhanshawn.util.LatalkDbFactory;
import com.xhanshawn.util.LatalkDbHelper;
import com.xhanshawn.util.LocationInfoFactory;
import com.xhanshawn.util.MessagePostFactory;
import com.xhanshawn.util.ParamsFactory;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.text.InputType;
import android.text.format.Time;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


public class TimeCapsuleCreateActivity extends Activity {
	
	/*
	 * views
	 */
	private EditText message_input;
	private TextView location_tv;
	private ActionBar actionBar;
	TimePicker tp;
	DatePicker dp;
	TextView tc_hold_time_tv;
	TextView time_picker_tv;
	ImageView attached_pic_iv;
	ListView tc_lv;
	LinearLayout buttons;
	RelativeLayout tp_panel;
	RelativeLayout tp_panel_rl;

	/*
	 * parameters
	 */
	private float current_latitude;
	private float current_longitude;
	private boolean location_is_enabled;
	private String message_content = null;
	private Bitmap taken_pic;
	private LatalkMessage tc = new LatalkMessage();
	long hold_time = 0;
	int width;
	int height;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_time_capsule_create);
		
		customActionBar();
		//get window sizes
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		height = size.y;
		width = size.x;
		
		
		tc_lv = (ListView) findViewById(R.id.tc_create_lv);
		TimeCapsuleCreateAdapter tc_a = new TimeCapsuleCreateAdapter();
		tc_lv.setAdapter(tc_a);
		LinearLayout.LayoutParams tc_lv_p = (LinearLayout.LayoutParams) tc_lv.getLayoutParams();
		tc_lv_p.height = ParamsFactory.toDP(330, this);
		tc_lv.setLayoutParams(tc_lv_p);
		tp_panel_rl = (RelativeLayout) findViewById(R.id.tp_panel_rl);
		attached_pic_iv = new ImageView(TimeCapsuleCreateActivity.this);
		tp_panel_rl.addView(attached_pic_iv);
		
		RelativeLayout.LayoutParams pic_p = 
				new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		attached_pic_iv.setLayoutParams(pic_p);
		
	}
	
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		showCurrentLocation();
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
	        	}
	        }
	        break;
		case IntegerIdentifiers.ATTACH_IMG_FROM_CAM:
			Bundle extras = data.getExtras();
			
			int key = extras.getInt("pic_key");
			byte[] byte_array = DataPassCache.getPicByKey(key);
			
			taken_pic = BitmapFactory.decodeByteArray(byte_array, 0, byte_array.length);
			
			break;
			
		default: break;
		}
		attached_pic_iv.setImageBitmap(taken_pic);
		if(tp_panel.getVisibility() == View.VISIBLE && taken_pic != null) AnimationFactory.switchVisibility(tp_panel);
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
				
				
//				LatalkDbFactory ldbf = new LatalkDbFactory(TimeCapsuleCreateActivity.this);
//				
//				long item_id = ldbf.insert(message);
//				
//				LatalkMessage mdb = ldbf.readByDbId(item_id);
//				Log.v("tc in db", mdb.getMessageType() + "    " + mdb.getContent());
				
				ArrayList<LatalkMessage> list = new ArrayList<LatalkMessage>();
				list.add(message);
				MessagePostFactory.postLatalks(list);
				TimeCapsuleCreateActivity.this.finish();
			}
		});
	    
	    TextView banner_tv = (TextView) findViewById(R.id.actionbar_color_banner);
	    banner_tv.setText("Time Capsule");
	}
	

	private void showCurrentLocation(){
		
		Location current_location = LocationInfoFactory.getCurrentLocation();
		if(location_tv == null) return;
		if(current_location==null){
			
			location_tv.setText("Location not available");
			location_is_enabled = false;
		}else{
			
			location_is_enabled = true;

			current_latitude = (float)current_location.getLatitude();
			current_longitude = (float)current_location.getLongitude();
			location_tv.setText(current_latitude +" ");
			location_tv.append(" " + current_longitude);
		}
	}

	
	class TimeCapsuleCreateAdapter extends BaseAdapter{
		final static int TEXT = 0;
		final static int LOC_TIME_LV = 1;
		final static int BUTTONS = 2;


		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 3;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			
			if(convertView == null) {
				LayoutInflater inflater = TimeCapsuleCreateActivity.this.getLayoutInflater();
				convertView = inflater.inflate(R.layout.relative_layout, parent, false);
				RelativeLayout rl = (RelativeLayout) convertView;
				switch(position){
					
				case TEXT: 
					
					message_input = new EditText(TimeCapsuleCreateActivity.this);
					message_input.setBackgroundResource(R.color.white);
					message_input.setGravity(Gravity.TOP);
					RelativeLayout.LayoutParams	et_p = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, 100);
					et_p.height = ParamsFactory.toDP(150, TimeCapsuleCreateActivity.this);	
					message_input.setLayoutParams(et_p);
					
					message_input.setEms(10);
					message_input.setHint(AlertMessageFactory.tcInputHint());
					message_input.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
					rl.addView(message_input);
					
					break;
				case LOC_TIME_LV:
					
					ListView lt_lv = new ListView(TimeCapsuleCreateActivity.this);
					RelativeLayout.LayoutParams	lt_p = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT
							, ParamsFactory.toDP(90, TimeCapsuleCreateActivity.this));
					lt_p.bottomMargin = ParamsFactory.toDP(10, TimeCapsuleCreateActivity.this);
					lt_p.topMargin = ParamsFactory.toDP(10, TimeCapsuleCreateActivity.this);
					lt_lv.setLayoutParams(lt_p);
					lt_lv.setDivider(getResources().getDrawable(R.drawable.divider));
					lt_lv.setDividerHeight(1);
					lt_lv.setScrollContainer(false);
					lt_lv.setOverScrollMode(View.OVER_SCROLL_NEVER);
					lt_lv.setBackgroundResource(R.drawable.border_layout);
					lt_lv.setAdapter(new BaseAdapter(){
						@Override
						public int getCount() {
							return 2;
						}

						@Override
						public Object getItem(int position) {return null;}

						@Override
						public long getItemId(int position) {return 0;}

						@Override
						public View getView(int position, View convertView, ViewGroup parent) {
							// TODO Auto-generated method stub
							if(convertView == null){
								LayoutInflater inflater = TimeCapsuleCreateActivity.this.getLayoutInflater();
								convertView = inflater.inflate(R.layout.list_with_text, parent, false);
								ImageView icon = (ImageView) convertView.findViewById(R.id.l_t_icon);
								if(position == 0){
									icon.setImageResource(R.drawable.pin_icon_grey);
									location_tv = (TextView) convertView.findViewById(R.id.l_t_tv);
									showCurrentLocation();
									location_tv.setOnClickListener(new View.OnClickListener() {
										
										@Override
										public void onClick(View v) {
											// TODO Auto-generated method stub
											showCurrentLocation();
										}
									});
									
								}else if(position == 1){
									icon.setImageResource(R.drawable.clock_icon_grey);
									tc_hold_time_tv = (TextView) convertView.findViewById(R.id.l_t_tv);
									tc_hold_time_tv.setText("Hold Time");
								}
							}
							return convertView;
						}
						
					});
					rl.addView(lt_lv);
					
					break;
				case BUTTONS:
					buttons = (LinearLayout) inflater.inflate(R.layout.tc_buttons, parent, false);
					rl.addView(buttons);
					RelativeLayout.LayoutParams	buttons_p = (android.widget.RelativeLayout.LayoutParams) buttons.getLayoutParams();
					buttons_p.width = width;
//					buttons_p.height = ParamsFactory.toDP(50, TimeCapsuleCreateActivity.this);
					buttons.setLayoutParams(buttons_p);
					
					ImageButton add_pic_b = (ImageButton) buttons.findViewById(R.id.add_pic_ib);
					
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
					
					ImageButton add_pic_f_g_ib = (ImageButton) buttons.findViewById(R.id.add_pic_gallery_ib);

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
					
					setHoldTimePicker();
					break;
				
				default: break;
				
				}
			}
			return convertView;
		}
		
	}
	
	private void setHoldTimePicker(){
		
		tp_panel = new RelativeLayout(TimeCapsuleCreateActivity.this);
		tp = new TimePicker(TimeCapsuleCreateActivity.this);
		tp.setIs24HourView(true);
//		hold_time = new Date().getTime()/(60 * 1000);
		
		dp = new DatePicker(TimeCapsuleCreateActivity.this);
		time_picker_tv = new TextView(TimeCapsuleCreateActivity.this);

		tp_panel.addView(tp);
		tp_panel.addView(dp);
		tp_panel.addView(time_picker_tv);
		tp_panel_rl.addView(tp_panel);

		RelativeLayout.LayoutParams tp_p = (RelativeLayout.LayoutParams) tp_panel.getLayoutParams();
		tp_p.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		tp_panel.setLayoutParams(tp_p);
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
		
		time_picker_tv.setText(AlertMessageFactory.pickHoldTime());
		RelativeLayout.LayoutParams params_tv = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		params_tv.addRule(RelativeLayout.BELOW, dp.getId());
		params_tv.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		time_picker_tv.setLayoutParams(params_tv);
		ImageButton time_picker_ib = (ImageButton) buttons.findViewById(R.id.time_picker_ib);
		
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
					AnimationFactory.switchVisibility(attached_pic_iv);
					AnimationFactory.switchVisibility(tp_panel);
				}
				
				return false;
			}
		});
		
	}
	
}
