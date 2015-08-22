package com.xhanshawn.latalk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.Inflater;

import com.xhanshawn.data.UserAccount;
import com.xhanshawn.util.AlertMessageFactory;
import com.xhanshawn.util.AnimationFactory;
import com.xhanshawn.util.UserSessionManager;
import com.xhanshawn.util.UsersController;
import com.xhanshawn.view.MyListView;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class OptionsActivity extends Activity {

	private static final String Options[] = { " Option1"," Option2"," Option3"," Option4"," Option5"};
	private MyListView account_lv;
	private MyListView general_lv;
	private MyListView others_lv;
	private View selected;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_options);
		
		showActionBar();
		
		account_lv = (MyListView) findViewById(R.id.list1);
		general_lv = (MyListView) findViewById(R.id.list2); 
		others_lv = (MyListView) findViewById(R.id.list3);
		
		account_lv.setAdapter(new BaseAdapter(){
			
			final static private int ACCOUNT = 0;
			final static private int LOGIN_SNS = 1;
			private final String ACCOUNT_OPTIONS[] = {" Account settings"," SNS connection"};
			
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
					LayoutInflater inflater = OptionsActivity.this.getLayoutInflater();
					convertView = inflater.inflate(R.layout.menulist, null);
				}
				ImageView menu_icon = (ImageView) convertView.findViewById(R.id.menu_icon);
				switch(position){
					case ACCOUNT:
						menu_icon.setImageResource(R.drawable.user_icon);
						break;
					case LOGIN_SNS:
						menu_icon.setImageResource(R.drawable.connect_icon);
						break;
					default: break;
				}
				
				TextView menu_tv = (TextView) convertView.findViewById(R.id.menu_text);
				menu_tv.setText(ACCOUNT_OPTIONS[position]);
				
				return convertView;
			}
			
		});
		
		general_lv.setAdapter(new BaseAdapter(){
			
			final static private int QUERY_SETTINGS = 0;
			final static private int GENERAL_SETTINGS = 1;
			private final String GENERAL_OPTIONS[] = {" Query settings"," General"};

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
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
					LayoutInflater inflater = OptionsActivity.this.getLayoutInflater();
					convertView = inflater.inflate(R.layout.menulist, null);
				}
				ImageView menu_icon = (ImageView) convertView.findViewById(R.id.menu_icon);
				switch(position){
					case QUERY_SETTINGS:
						menu_icon.setImageResource(R.drawable.pin_icon_deep_pink);
						break;
					case GENERAL_SETTINGS:
						menu_icon.setImageResource(R.drawable.gear_icon);
						break;
					default: break;
				}
				
				TextView menu_tv = (TextView) convertView.findViewById(R.id.menu_text);
				menu_tv.setText(GENERAL_OPTIONS[position]);
				return convertView;
			}
			
		});
		
		others_lv.setAdapter(new BaseAdapter(){
			
			final static private int CLEAR_CACHE = 0;
			final static private int ABOUT = 1;
			final static private int LOGOUT = 2;
			
			private final String OTHERS_OPTIONS[] = {" Clear Cache", " About Latalk", " Log Out"};

			@Override
			public int getCount() {
				return 3;
			}

			@Override
			public Object getItem(int position) {return null;}

			@Override
			public long getItemId(int position) {return 0;}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				if(convertView == null){
					LayoutInflater inflater = OptionsActivity.this.getLayoutInflater();
					convertView = inflater.inflate(R.layout.menulist, null);
				}
				ImageView menu_icon = (ImageView) convertView.findViewById(R.id.menu_icon);
				switch(position){
					case CLEAR_CACHE:
						menu_icon.setImageResource(R.drawable.brush_icon);
						break;
					case ABOUT:
						menu_icon.setImageResource(R.drawable.about_icon);
						break;
					case LOGOUT:
						menu_icon.setImageResource(R.drawable.logout_icon);
						break;
					default: break;
				}
				
				TextView menu_tv = (TextView) convertView.findViewById(R.id.menu_text);
				menu_tv.setText(OTHERS_OPTIONS[position]);
				return convertView;
			}
			
		});
		
		account_lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
			final static private int ACCOUNT = 0;
			final static private int LOGIN_SNS = 1;
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				switch(position){
					case ACCOUNT:
						Intent account_activity = new Intent("com.xhanshawn.latalk.ACCOUNTSETTINGSACTIVITY");
						startActivity(account_activity);
						break;
					case LOGIN_SNS:
						Intent login_activity = new Intent("com.xhanshawn.latalk.LOGINACTIVITY");
						startActivity(login_activity);
						break;
					default: break;
				}
			}
		});
		
		general_lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
			final static private int QUERY_SETTINGS = 0;
			final static private int GENERAL_SETTINGS = 1;
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				switch(position){
				case QUERY_SETTINGS:
					Intent query_activity = new Intent("com.xhanshawn.latalk.QUERYSETTINGSACTIVITY");
					startActivity(query_activity);
					break;
				case GENERAL_SETTINGS:
					
					break;
				default: break;
				}
			}
		});
		others_lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
			final static private int CLEAR_CACHE = 0;
			final static private int ABOUT = 1;
			final static private int LOGOUT = 2;
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				switch(position){
				case CLEAR_CACHE:
					Toast clear_cache_toast = Toast.makeText(OptionsActivity.this,
							AlertMessageFactory.clearCacheSuccess(),
							Toast.LENGTH_LONG);
				
					clear_cache_toast.show();
					break;
				case ABOUT:
					Intent about = new Intent("com.xhanshawn.latalk.ABOUTACTIVITY");
					startActivity(about);
					break;
				case LOGOUT:
					new AlertDialog.Builder(OptionsActivity.this)
				    .setTitle("Log out")
				    .setMessage("Are you sure you want to log out?")
				    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface dialog, int which) { 
				            // continue with delete
				        	
//				        	UserSessionManager manager = new UserSessionManager(getApplicationContext());
//							manager.logoutUser();
							
							OptionsActivity.this.finish();
							Intent logout = new Intent("com.xhanshawn.latalk.LOGINACTIVITY");
							startActivity(logout);
				        }
				     })
				    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface dialog, int which) { 
				            // do nothing
				        }
				     })
				    .setIcon(android.R.drawable.ic_dialog_alert)
				     .show();
					break;
				default: break;
				}
			}
			
		});
		
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return super.onTouchEvent(event);
	}
	

	
	private void showActionBar(){

		ActionBar actionBar = getActionBar();
		actionBar.show();
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.actionbar_color_only_back_b, null);
		
	    actionBar.setDisplayShowCustomEnabled(true);

	    actionBar.setCustomView(v);
	    actionBar.setBackgroundDrawable(getResources().getDrawable(R.color.deep_pink));
	    
	    Button back_to_main_b = (Button) v.findViewById(R.id.color_ab_back_b);
	    back_to_main_b.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				OptionsActivity.this.finish();
			}
		});
	    
	    TextView banner_tv = (TextView) findViewById(R.id.actionbar_color_banner);
	    banner_tv.setText("Settings");
	}
	
}



	


