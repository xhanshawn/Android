package com.xhanshawn.latalk;

import java.util.ArrayList;
import java.util.HashMap;

import com.xhanshawn.data.UserAccount;
import com.xhanshawn.util.UserSessionManager;
import com.xhanshawn.util.UsersController;
import com.xhanshawn.view.MyListView;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class OptionsActivity extends Activity {

	private static final String Options[] = { " Option1"," Option2"," Option3"," Option4"," Option5"};
	private static final String OTHERS_OPTIONS[] = { " Log Out"," Option2"," Option3"," Option4"," Option5"};
	
	private MyListView list1;
	private MyListView list2;
	private MyListView list3;
	private ActionBar actionBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_options);
		
		showActionBar();
		
		list1 = (MyListView) findViewById(R.id.list1);
		list2 = (MyListView) findViewById(R.id.list2); 
		list3 = (MyListView) findViewById(R.id.list3);
		
		
		int ID[] = { R.id.menu_icon,R.id.menu_text}; 
		
		ArrayList<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
		
		for(int i=0; i<Options.length; i++){
			HashMap<String,Object> map = new HashMap<String,Object>();
			
			map.put("menu_icon", R.id.menu_icon);
			map.put("menu_text", Options[i]);
		
			
			list.add(map);
		}
		
		ArrayList<HashMap<String,Object>> others_list = new ArrayList<HashMap<String,Object>>(); 
		
		for(int i=0; i< OTHERS_OPTIONS.length; i++){
			HashMap<String,Object> map = new HashMap<String,Object>();
			
			map.put("menu_icon", R.id.menu_icon);
			map.put("menu_text", OTHERS_OPTIONS[i]);
		
			
			others_list.add(map);
		}
		
		SimpleAdapter adapter = new SimpleAdapter(this,list,
				R.layout.menulist,
				new String[] {"menu_icon","menu_text"},ID);
		
		SimpleAdapter others_adapter = new SimpleAdapter(this,
				others_list, R.layout.menulist,
				new String[] {"menu_icon", "menu_text"}, ID);
		
		list1.setAdapter(adapter);
		list2.setAdapter(adapter);
		list3.setAdapter(others_adapter);
		
		list3.setOnItemClickListener(new AdapterView.OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
				if(position == 0) {
					
					UserSessionManager manager = new UserSessionManager(getApplicationContext());
					manager.logoutUser();
					
					OptionsActivity.this.finish();
					Intent login_activity = new Intent("com.xhanshawn.latalk.LOGINACTIVITY");
					startActivity(login_activity);
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
		
		actionBar=getActionBar();
		actionBar.show();
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.actionbar_options,null);
		
	    actionBar.setDisplayShowCustomEnabled(true);

	    actionBar.setCustomView(v);
	    
	    ImageButton Button_menu_to_main = (ImageButton)findViewById(R.id.menu_to_main);
	   
	    Button_menu_to_main.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				OptionsActivity.this.finish();
			}
		});    
	}
	
}



	


