package com.xhanshawn.latalk;

import java.util.ArrayList;

import com.xhanshawn.data.LatalkMessage;
import com.xhanshawn.util.MessageGetFactory;
import com.xhanshawn.util.MessagePostFactory;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MessageBrowserActivity extends Activity {
	
	private ArrayList<LatalkMessage> messages = null;
	private TextView message_textview = null;
	private String current_message_type = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_browser);
		
		message_textview = (TextView) findViewById(R.id.message_textiew);
		
		updateMessageText();
		
		
		Button get_puzzle_button = (Button) findViewById(R.id.get_puzzle_button);
		
		get_puzzle_button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(messages == null || !current_message_type.equals("Puzzle")) {
					new MessageRetriever().execute("Puzzle");
					current_message_type = "Puzzle";
				}
			}
		});
		
		
		Button get_timecapsule_button = (Button) findViewById(R.id.get_timecapsule_button);
		
		get_timecapsule_button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(messages == null || !current_message_type.equals("Time Capsule")) {
					new MessageRetriever().execute("Time Capsule");
					current_message_type = "Time Capsule";
				}
			}
		});
		
	}
	
	private void updateMessageText(){
		
		if(messages == null) message_textview.setText("No result available!");
		else{
			
			LatalkMessage message = messages.get(0);
			String message_content = "message_type: " + message.getMessage_type() + "\nContent: "
					+ message.getContent() + "\nLatitude: " + message.getLatitude() + "\nLongitude: "
					+ message.getLongitude() + "\nUser: " + message.getUser_name() + "\nHold Time:"
					+ message.getHold_time();
			message_textview.setText(message_content);
		}
		
	}
	
	public class MessageRetriever extends AsyncTask<String, Void, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			if(params[0].equals("Puzzle")) messages = MessageGetFactory.getPuzzleMessages();
			if(params[0].equals("Time Capsule")) messages = MessageGetFactory.getTimeCapsuleMessages();

			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			if(result) updateMessageText();
		}
		
		
	}
}
