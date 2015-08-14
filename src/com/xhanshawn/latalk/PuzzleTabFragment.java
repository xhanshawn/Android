package com.xhanshawn.latalk;

import java.util.ArrayList;

import com.xhanshawn.data.LatalkMessage;
import com.xhanshawn.latalk.PuzzleTabActivity.MessageRetriever;
import com.xhanshawn.util.MessageGetFactory;
import com.xhanshawn.view.LatalkItemAdapter;
import com.xhanshawn.view.MyListView;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

public class PuzzleTabFragment extends Fragment {
	
	ArrayList<LatalkMessage> messages = new ArrayList<LatalkMessage>();
	MyListView latalk_mlv;
	Context context;
	private View view;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		if(view == null) view = (ScrollView) inflater.inflate(R.layout.activity_puzzle_tab, container, false);
		
		
		latalk_mlv = (MyListView) view.findViewById(R.id.puzzle_tab_mlv);
		
		
		context = getActivity();
		
		if(messages.isEmpty()) new MessageRetriever().execute("");

		
		return view;
	}
	
	class MessageRetriever extends AsyncTask<String, Void, Boolean> {

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
			
				latalk_mlv.setAdapter(latalk_item_adapter);
			}
		}
		
	}
}
