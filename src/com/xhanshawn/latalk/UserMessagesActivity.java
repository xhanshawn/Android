package com.xhanshawn.latalk;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class UserMessagesActivity extends TabActivity {
	ImageView list_view;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_messages);
	
		TabHost messages_tab = (TabHost) findViewById(android.R.id.tabhost);
		
		TabSpec list_tab = messages_tab.newTabSpec("First Tab");
        TabSpec grid_tab = messages_tab.newTabSpec("Second Tab");
        TabSpec puzzle_tab = messages_tab.newTabSpec("Third Tab");
        TabSpec time_capsule_tab = messages_tab.newTabSpec("Fourth Tab");
        
        list_tab.setContent(new Intent(this,ListTabActivity.class));
        
        list_view = new ImageView(this);
        list_view.setImageResource(R.drawable.ic_launcher);
       
        
        list_tab.setIndicator(list_view);
        
        grid_tab.setIndicator("Tab2");
        grid_tab.setContent(new Intent(this,GridTabActivity.class));
        
        puzzle_tab.setContent(new Intent(this,PuzzleTabActivity.class));
        puzzle_tab.setIndicator("Puzzle Tab");
        
        time_capsule_tab.setContent(new Intent(this,TimeCapsuleTabActivity.class));
        time_capsule_tab.setIndicator("Time Capsule Tab");
        
        messages_tab.addTab(list_tab);
        messages_tab.addTab(grid_tab);
        messages_tab.addTab(puzzle_tab);
        messages_tab.addTab(time_capsule_tab);
	}
	
}
