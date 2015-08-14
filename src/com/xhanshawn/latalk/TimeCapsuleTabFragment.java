package com.xhanshawn.latalk;

import java.util.ArrayList;

import com.xhanshawn.data.LatalkMessage;
import com.xhanshawn.latalk.TimeCapsuleTabActivity.MessageRetriever;
import com.xhanshawn.util.MessageGetFactory;
import com.xhanshawn.view.LatalkItemAdapter;
import com.xhanshawn.view.MyListView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

public class TimeCapsuleTabFragment extends Fragment{

	ArrayList<LatalkMessage> messages = new ArrayList<LatalkMessage>();
	MyListView latalk_mlv;
	LatalkItemAdapter latalk_item_adapter;
	FragmentActivity context;
	private View view;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if(view == null) view = (ScrollView) inflater.inflate(R.layout.activity_time_capsule_tab, container, false);
		latalk_mlv = (MyListView) view.findViewById(R.id.tc_tab_mlv);
        context = getActivity();
		
		if(messages.isEmpty()) new MessageRetriever().execute("GetMessage");
		return view;
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
				latalk_item_adapter = new LatalkItemAdapter(context,messages);
			
				latalk_mlv.setAdapter(latalk_item_adapter);
				latalk_item_adapter.clearImageCache();
				
			}
			
			
		}
		
	}
}
