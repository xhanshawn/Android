package com.xhanshawn.latalk;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import com.xhanshawn.data.LatalkMessage;
import com.xhanshawn.util.MessageGetFactory;
import com.xhanshawn.util.MessagePostFactory;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MessageBrowserActivity extends Activity {
	
	private ArrayList<LatalkMessage> messages = null;
	private TextView message_textview = null;
	private String current_message_type = null;
	ImageView message_img_iv;
	
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
//				if(messages == null || !current_message_type.equals("Puzzle")) {
//					new MessageRetriever().execute("Puzzle");
//					current_message_type = "Puzzle";
//				}
				
				Intent puzzle = new Intent("com.xhanshawn.latalk.PUZZLEMAPACTIVITY");
				startActivity(puzzle);
				
			}
		});
		
		
		Button get_timecapsule_button = (Button) findViewById(R.id.get_timecapsule_button);
		
		get_timecapsule_button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				if(messages == null || !current_message_type.equals("Time Capsule")) {
//					new MessageRetriever().execute("Time Capsule");
//					current_message_type = "Time Capsule";
//				}
				
				Intent time_capsule = new Intent("com.xhanshawn.latalk.TIMECAPSULEACTIVITY");
				startActivity(time_capsule);
			}
		});
		
		message_img_iv = (ImageView) findViewById(R.id.message_img_iv);
		
		Button get_image_button = (Button) findViewById(R.id.get_image_button);
		get_image_button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				new MessageRetriever().execute("");
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
	
	private void updateMessageImage(Bitmap bitmap){
		
		message_img_iv.setImageBitmap(bitmap);
		
		
	}
	
	public class MessageRetriever extends AsyncTask<String, Void, Boolean> {
		
		InputStream img_stream;
		String image_url;
		Bitmap bitmap;
		
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			if(params[0].equals("Puzzle")) messages = MessageGetFactory.getPuzzleMessages();
			if(params[0].equals("Time Capsule")) messages = MessageGetFactory.getTimeCapsuleMessages();
			
			
			
			try{
				
				bitmap = BitmapFactory.decodeStream(img_stream);
				

			} catch(Exception e){
				e.printStackTrace();
			}
			
			
			
			
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			message_textview.setText(image_url);
			if(result) updateMessageText();
			updateMessageImage(bitmap);
		}
		
		
	}
}
