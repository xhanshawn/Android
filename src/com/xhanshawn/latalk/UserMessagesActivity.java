package com.xhanshawn.latalk;

import com.xhanshawn.data.UserAccount;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class UserMessagesActivity extends TabActivity {
	ImageView list_view;
	ScrollView m_scroll_view;
	LinearLayout header;
	TabHost messages_tab;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_messages);
		
//		m_scroll_view = (ScrollView) findViewById(R.id.user_message_sv);
		
		
		header = (LinearLayout) findViewById(R.id.user_message_header);
		
		TextView user_name_tv = (TextView) findViewById(R.id.user_name_tv);
		
		user_name_tv.setText(UserAccount.getCurrentUserName());
		
		
		
		
		
		
		messages_tab = (TabHost) findViewById(android.R.id.tabhost);
		
		TabSpec latalk_map_tab = messages_tab.newTabSpec("First Tab");
        TabSpec grid_tab = messages_tab.newTabSpec("Second Tab");
        TabSpec list_tab = messages_tab.newTabSpec("Third Tab");
        TabSpec time_capsule_tab = messages_tab.newTabSpec("Fourth Tab");
        
        latalk_map_tab.setContent(new Intent(this,UserLatalkMapActivity.class));
        latalk_map_tab.setIndicator("Map Tab");
        
        
        grid_tab.setIndicator("Tab2");
        grid_tab.setContent(new Intent(this,GridTabActivity.class));
        
        list_tab.setContent(new Intent(this,PuzzleTabActivity.class));
        
        list_view = new ImageView(this);
        list_view.setImageResource(R.drawable.ic_launcher);
       
        
        list_tab.setIndicator(list_view);
       
        
        time_capsule_tab.setContent(new Intent(this,TimeCapsuleTabActivity.class));
        time_capsule_tab.setIndicator("Time Capsule Tab");
        
        messages_tab.addTab(latalk_map_tab);
        messages_tab.addTab(grid_tab);
        messages_tab.addTab(list_tab);
        messages_tab.addTab(time_capsule_tab);
        
        messages_tab.setOnTabChangedListener(new OnTabChangeListener(){

			@Override
			public void onTabChanged(String tabId) {
				// TODO Auto-generated method stub
				messages_tab.clearFocus();
				header.requestFocus();

//				m_scroll_view.fullScroll(View.FOCUS_UP);

			}
        	
        });
	}
	
	
	
	
	
	
}
