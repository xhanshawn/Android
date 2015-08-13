package com.xhanshawn.latalk;

import com.xhanshawn.data.UserAccount;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
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

public class UserMessagesActivity extends FragmentActivity{
	ImageView list_view;
	ScrollView m_scroll_view;
	LinearLayout header;
	TabHost messages_tab;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_messages);
		FragmentTabHost mTabHost;
		mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        mTabHost.addTab(mTabHost.newTabSpec("simple").setIndicator("Simple"),
        		UserLatalkMapFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("contacts").setIndicator("Contacts"),
        		GridTabActivity.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("custom").setIndicator("Custom"),
        		PuzzleTabActivity.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("throttle").setIndicator("Throttle"),
        		TimeCapsuleTabActivity.class, null);
        
        
        
        
//		m_scroll_view = (ScrollView) findViewById(R.id.user_message_sv);
		
//		
//		header = (LinearLayout) findViewById(R.id.user_message_header);
//		
//		TextView user_name_tv = (TextView) findViewById(R.id.user_name_tv);
//		
//		user_name_tv.setText(UserAccount.getCurrentUserName());
//		
//		
//		
//		
//		
//		
//		messages_tab = (TabHost) findViewById(android.R.id.tabhost);
//		
//		TabSpec latalk_map_tab = messages_tab.newTabSpec("First Tab");
//        TabSpec grid_tab = messages_tab.newTabSpec("Second Tab");
//        TabSpec list_tab = messages_tab.newTabSpec("Third Tab");
//        TabSpec time_capsule_tab = messages_tab.newTabSpec("Fourth Tab");
//        
//        latalk_map_tab.setContent(new Intent(this,UserLatalkMapActivity.class));
//        latalk_map_tab.setIndicator("Map Tab");
//        
//        
//        grid_tab.setIndicator("Tab2");
//        grid_tab.setContent(new Intent(this,GridTabActivity.class));
//        
//        list_tab.setContent(new Intent(this,PuzzleTabActivity.class));
//        
//        
//        ImageView[] tab_ivs = new ImageView[4];
//        for(int i = 0; i < 4; i ++) {
//        	tab_ivs[i] = new ImageView(this);
//        }
//        
//        tab_ivs[2].setImageResource(R.drawable.ic_launcher);
//       
//        
//        list_tab.setIndicator(tab_ivs[2]);
//
//       
//        
//        time_capsule_tab.setContent(new Intent(this,TimeCapsuleTabActivity.class));
//        time_capsule_tab.setIndicator("Time Capsule Tab");
//        
//        messages_tab.addTab(latalk_map_tab);
//        messages_tab.addTab(grid_tab);
//        messages_tab.addTab(list_tab);
//        messages_tab.addTab(time_capsule_tab);
//        
//        messages_tab.setOnTabChangedListener(new OnTabChangeListener(){
//
//			@Override
//			public void onTabChanged(String tabId) {
//				// TODO Auto-generated method stub
//				messages_tab.clearFocus();
//				header.requestFocus();
//
////				m_scroll_view.fullScroll(View.FOCUS_UP);
//
//			}
//        	
//        });
	}
	
	
	
	
	
	
}
