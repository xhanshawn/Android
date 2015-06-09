package com.xhanshawn.latalk;

import java.util.ArrayList;

import com.xhanshawn.data.LatalkMessage;
import com.xhanshawn.util.MessageGetFactory;
import com.xhanshawn.view.LatalkItemAdapter;
import com.xhanshawn.view.MyListView;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

public class PuzzleTabActivity extends Activity {
	
	ArrayList<LatalkMessage> messages = new ArrayList<LatalkMessage>();
	MyListView latalk_tv;
	Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_puzzle_tab);
		
		
		
		latalk_tv = (MyListView) findViewById(R.id.puzzle_tab_mlv);
		
		new MessageRetriever().execute("");
		
		context = this;
        
	}
	
	
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		new MessageRetriever().execute("");
	}




	public class MessageRetriever extends AsyncTask<String, Void, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			messages = MessageGetFactory.getPuzzleMessages();
//			for(LatalkMessage message : messages) {
//				
//				if(message.getAttahedPic() == null && (message.getPicUrl() != null || message.getPicUrl() != "")) {
//					message.setAttachedPic(MessageGetFactory.getImage(message.getPicUrl()));
//				}
//			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(result){
				LatalkItemAdapter latalk_item_adapter = new LatalkItemAdapter(context, messages);
			
				latalk_tv.setAdapter(latalk_item_adapter);
			}
		}
		
	}
}
