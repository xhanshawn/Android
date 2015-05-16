package com.xhanshawn.latalk;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class UserMessagesActivity extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_messages);
	
		TabHost messages_tab = (TabHost) findViewById(android.R.id.tabhost);
		
		TabSpec tab1 = messages_tab.newTabSpec("First Tab");
        TabSpec tab2 = messages_tab.newTabSpec("Second Tab");
        
        tab1.setIndicator("Tab1");
        tab1.setContent(new Intent(this,GridTabActivity.class));
        
        tab2.setIndicator("Tab2");
        tab2.setContent(new Intent(this,ListTabActivity.class));
	
        messages_tab.addTab(tab1);
        messages_tab.addTab(tab2);
	}
	
}
