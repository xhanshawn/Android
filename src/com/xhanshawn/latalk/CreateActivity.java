package com.xhanshawn.latalk;

import com.xhanshawn.data.LatalkMessage;
import com.xhanshawn.util.MessagePostFactory;

import android.app.Activity;
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

public class CreateActivity extends Activity {
	private String message_content = null;
	private EditText message_input;
	RadioButton puzzle_type_button;
	RadioButton timecapsule_type_button;
	private String message_type = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create);
		message_input = (EditText) findViewById(R.id.message_input);
		
		
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
				message.setLatitude(30f + 0.323f );
				message.setLongitude(100f + 0.324f );
				message.setHold_time(3000);
				message.setUser_name("xhanshawn");
				
				new MessagePoster().execute(message);
			
			}
		});
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
