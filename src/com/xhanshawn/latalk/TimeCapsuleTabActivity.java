package com.xhanshawn.latalk;

import java.awt.Color;
import java.util.ArrayList;

import com.xhanshawn.data.LatalkMessage;
import com.xhanshawn.latalk.PuzzleTabActivity.MessageRetriever;
import com.xhanshawn.util.MessageGetFactory;
import com.xhanshawn.view.LatalkItemAdapter;
import com.xhanshawn.view.MyListView;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TimeCapsuleTabActivity extends Activity {
	
	ArrayList<LatalkMessage> messages = new ArrayList<LatalkMessage>();
	MyListView latalk_mlv;
	LatalkItemAdapter latalk_item_adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        setContentView(R.layout.activity_time_capsule_tab);
        
        latalk_mlv = (MyListView) findViewById(R.id.tc_tab_mlv);
        
		new MessageRetriever().execute("");
		
		
		
	}
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		new MessageRetriever().execute("GetMessage");
	}
	
	
	public class MessageRetriever extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			if(params[0].equals("GetMessage")) messages = MessageGetFactory.getTimeCapsuleMessages();
			if(params[0].equals("LoadImage")) {
				
			}
			
			return params[0];
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(result.equals("GetMessage")){
				latalk_item_adapter = new LatalkItemAdapter(TimeCapsuleTabActivity.this,messages);
			
				latalk_mlv.setAdapter(latalk_item_adapter);
				latalk_item_adapter.clearImageCache();
				
			}
			
			
		}
		
	}

}
