package com.xhanshawn.latalk;

import com.xhanshawn.data.LatalkMessage;
import com.xhanshawn.data.UserAccount;
import com.xhanshawn.util.LocationInfoFactory;
import com.xhanshawn.util.MessagePostFactory;

import android.app.Activity;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

public class MessageCreateActivity extends Activity {
	private String message_content = null;
	private EditText message_input;
	RadioButton puzzle_type_button;
	RadioButton timecapsule_type_button;
	private String message_type = null;
	private TextView location_message_tv;
	
	private float current_latitude;
	private float current_longitude;
	private boolean location_is_enabled;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_createmessages);
		message_input = (EditText) findViewById(R.id.message_input);
		
		location_message_tv = (TextView) findViewById(R.id.location_tv);
		
		puzzle_type_button = (RadioButton) findViewById(R.id.puzzle_type_radio);
		
		
		timecapsule_type_button = (RadioButton) findViewById(R.id.time_capsule_type_radio);
		if(puzzle_type_button.isChecked()) message_type = "Puzzle";
		else if(timecapsule_type_button.isChecked()) message_type = "Time_Capsule";

		Button post_message = (Button) findViewById(R.id.post_message_button);

		post_message.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(puzzle_type_button.isChecked()) message_type = "Puzzle";
				else if(timecapsule_type_button.isChecked()) message_type = "Time_Capsule";
				
				
				message_content = message_input.getText().toString();
				LatalkMessage message = new LatalkMessage();
				message.setContent(message_content);
				message.setMessage_type(message_type);
				
				if(location_is_enabled){
					
					message.setLatitude(current_latitude);
					message.setLongitude(current_longitude);
				}
				
				
				message.setHold_time(3000);
				message.setUser_name(UserAccount.getCurrentUserName());
				
				new MessagePoster().execute(message);
			
			}
		});
	}
	
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		getCurrentLocation();

	}



	private void getCurrentLocation(){
		LocationInfoFactory location_info = new LocationInfoFactory(MessageCreateActivity.this);
		
		Location current_location = location_info.getCurrentLocation();
		
		if(current_location==null){
			
			location_message_tv.setText("Location not available");
			location_is_enabled = false;
		}else{
			
			location_is_enabled = true;

			current_latitude = (float)current_location.getLatitude();
			current_longitude = (float)current_location.getLongitude();
			location_message_tv.append(current_latitude +" ");
			location_message_tv.append(" " + current_longitude);
		}
	}
	
	
	public class MessagePoster extends AsyncTask<LatalkMessage, Void, Boolean> {


		@Override
		protected Boolean doInBackground(LatalkMessage... params) {
			// TODO Auto-generated method stub
			MessagePostFactory.postLatalkMessage(params[0]);
			
			return null;
		}
		
	}
}
