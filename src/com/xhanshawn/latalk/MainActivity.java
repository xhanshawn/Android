package com.xhanshawn.latalk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button create_message = (Button) findViewById(R.id.create_message_button);
		create_message.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent create_activity = new Intent("com.xhanshawn.latalk.CREATEACTIVITY");
				startActivity(create_activity);
			}
		});
		
		
	}
	
	
	
	
}
