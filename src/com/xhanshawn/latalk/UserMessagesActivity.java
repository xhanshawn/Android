package com.xhanshawn.latalk;

import java.util.ArrayList;

import com.facebook.login.LoginManager;
import com.xhanshawn.data.LatalkMessage;
import com.xhanshawn.data.UserAccount;
import com.xhanshawn.util.MessagePostFactory;

import android.app.ActionBar;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Button;
import android.widget.ImageButton;
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
	private ArrayList<LatalkMessage> messages = new ArrayList<LatalkMessage>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_messages);
		customActionBar();
		
		FragmentTabHost mTabHost;
		mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        
        ImageView[] tab_ivs = new ImageView[4];
        for(int i = 0; i < 4; i ++) {
        	tab_ivs[i] = new ImageView(this);
        }
      
        tab_ivs[0].setImageResource(R.drawable.selector_map);
        tab_ivs[1].setImageResource(R.drawable.selector_grid);
        tab_ivs[2].setImageResource(R.drawable.selector_pr);
        tab_ivs[3].setImageResource(R.drawable.selector_tc);

        mTabHost.addTab(mTabHost.newTabSpec("simple").setIndicator(tab_ivs[0]),
        		UserLatalkMapFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("contacts").setIndicator(tab_ivs[1]),
        		GridTabFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("custom").setIndicator(tab_ivs[2]),
        		PuzzleTabFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("throttle").setIndicator(tab_ivs[3]),
        		TimeCapsuleTabFragment.class, null);
        
        
		TextView user_name_tv = (TextView) findViewById(R.id.user_name_tv);
		user_name_tv.setText(UserAccount.getCurrentUserName());
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
	
	private void customActionBar(){
		
		ActionBar actionBar = getActionBar();
		actionBar.show();
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.actionbar_color_with_img, null);
		
	    actionBar.setDisplayShowCustomEnabled(true);

	    actionBar.setCustomView(v);
	    actionBar.setBackgroundDrawable(getResources().getDrawable(R.color.deep_pink));
	    
	    Button back_to_main_b = (Button) v.findViewById(R.id.color_ab_back_b);
	    back_to_main_b.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				UserMessagesActivity.this.finish();
			}
		});
	    
	    ImageButton post_ib = (ImageButton) v.findViewById(R.id.color_ab_ib);
	    post_ib.setImageResource(R.drawable.capsule_white);
	    post_ib.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				UserMessagesActivity.this.finish();
			}
		});
	    
	    TextView banner_tv = (TextView) findViewById(R.id.actionbar_color_banner);
	    banner_tv.setText("Latalk");
	}
	
}
