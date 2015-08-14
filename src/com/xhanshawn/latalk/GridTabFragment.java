package com.xhanshawn.latalk;

import java.util.ArrayList;

import com.fedorvlasov.lazylist.ImageLoader;
import com.xhanshawn.data.LatalkMessage;
import com.xhanshawn.latalk.R.color;
import com.xhanshawn.util.MessageGetFactory;
import com.xhanshawn.view.ImageGridAdapter;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class GridTabFragment extends Fragment {

	GridView user_img_gv;
	ImageLoader loader;
	ArrayList<LatalkMessage> messages = new ArrayList<LatalkMessage>();
	FragmentActivity context;
	private View view;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if(view == null) view = (LinearLayout) inflater.inflate(R.layout.activity_grid_tab, container, false);
		context = getActivity();
		
		loader = new ImageLoader(context);
		
		user_img_gv = (GridView) view.findViewById(R.id.user_pics_gv);
        
        if(messages.isEmpty()) new MessageRetriever().execute("GetMessage");
        
        return view;
    }
	
	
	class MessageRetriever extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			if(params[0].equals("GetMessage")) {
				messages = MessageGetFactory.getUserMessages();
			}
			if(params[0].equals("UpdateImage")) {
				
				getActivity().runOnUiThread(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						
						for(LatalkMessage message : messages) {
							
							ImageView iv = new ImageView(context);
//							iv.setPadding(1, 1, 1, 1);
//							loader.DisplayImage(message.getFullPicUrl(),
//									iv, R.drawable.loading_picture);
//							iv.setImageBitmap(MessageGetFactory.getImage(message.getPicUrl()));
							iv.setBackgroundColor(color.black);
//							iv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
							iv.setOnClickListener(new View.OnClickListener() {
								
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									
									
								}
							});
							
						}
						
					}
					
				});
			}
			
			return params[0];
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(result.equals("GetMessage")){
				
				ArrayList<LatalkMessage> img_messages = new ArrayList<LatalkMessage>();
				
				for(LatalkMessage message : messages) {
					
					if(message.isValidPicUrl()) img_messages.add(message);
				}
				
				messages = img_messages;
//				new MessageRetriever().execute("UpdateImage");
				
				ImageGridAdapter img_grid_adapter = new ImageGridAdapter(context, messages);
				user_img_gv.setAdapter(img_grid_adapter);
				img_grid_adapter.clearImageCache();
			}
			
			
		}
		
	}
}
