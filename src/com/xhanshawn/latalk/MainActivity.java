package com.xhanshawn.latalk;

import com.xhanshawn.data.UserAccount;
import com.xhanshawn.util.AlertMessageFactory;
import com.xhanshawn.util.LocationInfoFactory;
import com.xhanshawn.util.UserSessionManager;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends Activity {
	
	private ActionBar action_bar = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		verifyLoginSession();
		CustomizeActionBar();
		
		ImageButton options_button = (ImageButton)findViewById(R.id.options_button);
	    
	    options_button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent options_activity = new Intent("com.xhanshawn.latalk.OPTIONSACTIVITY");
				startActivity(options_activity);
			}
		}); 
		
	    ImageButton user_account_button = (ImageButton) findViewById(R.id.user_account_button);
	    user_account_button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent user_messages_activity = new Intent("com.xhanshawn.latalk.USERMESSAGESACTIVITY");
				startActivity(user_messages_activity);
			}
		});
	    
	    
	    
	    
	    
	    
	    
		Button create_message_button = (Button) findViewById(R.id.create_message_button);
		create_message_button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent create_activity = new Intent("com.xhanshawn.latalk.CREATEACTIVITY");
				startActivity(create_activity);
			}
		});
		
		Button message_browser_button = (Button) findViewById(R.id.message_browser_button);
		message_browser_button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent message_browser_activity = new Intent("com.xhanshawn.latalk.MESSAGEBROWSERACTIVITY");
				startActivity(message_browser_activity);
			}
		});
		
		
		
		
	}
	
	
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		LocationInfoFactory location_info = new LocationInfoFactory(MainActivity.this);
	}


	
	private void checkSettings(){
		
		
	    
	}

	private void CustomizeActionBar(){
		
		
		
		action_bar = getActionBar();
		action_bar.show();
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.actionbar_main,null);
		
	    action_bar.setDisplayShowCustomEnabled(true);

	    action_bar.setCustomView(view);
	       
	}
	
	
	private void verifyLoginSession(){
		
		UserSessionManager manager = new UserSessionManager(getApplicationContext());
		if(!manager.isUserLoggedIn()){
			Intent login_activity = new Intent("com.xhanshawn.latalk.LOGINACTIVITY");
			startActivity(login_activity);
		}
		UserAccount.updateCurrentUserName(manager.getCurrentUserName());
	}
	
	
}
