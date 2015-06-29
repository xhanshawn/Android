package com.xhanshawn.view;

import java.util.ArrayList;

import com.fedorvlasov.lazylist.ImageLoader;
import com.xhanshawn.data.LatalkMessage;
import com.xhanshawn.latalk.R;
import com.xhanshawn.util.MessageGetFactory;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LatalkItemAdapter extends BaseAdapter {
	
	private Context context;
	private ArrayList<LatalkMessage> messages;
	private ImageLoader img_loader;
	
	public LatalkItemAdapter(Context _context, ArrayList<LatalkMessage> _messages ){
		
		this.context = _context;
		this.messages = _messages;
		
		//adopted lazy list tool developed by fedorvlasov
		//I made some modifications for that
		img_loader = new ImageLoader(_context);
		
	}
	
	public void clearImageCache() {
		
		img_loader.clearCache();
	}
	
	//how many items
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		
		return messages.size();
	}

	//get data item associated
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return messages.get(position);
	}
	
	//get the row id in certain position
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View row = convertView;
		int type = getItemViewType(position);
		
		if(row == null){
			
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			if(type == 0) { 
				
				row = inflater.inflate(R.layout.latalk_item, null);
				
			} else {
				
				row = inflater.inflate(R.layout.latalk_item_with_pic, null);
			}
            
//			row = View.inflate(context, , null);
		}
		
		LatalkMessage message = messages.get(position);
		
//		if(message.hasPic()) {
//			
//			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
//            row = inflater.inflate(R.layout.latalk_item_with_pic, parent, false);
////			ll.removeView(pic_iv);
//
////			pic_iv.setVisibility(View.INVISIBLE);
//			
//		}
		
		TextView tv_content = (TextView) row.findViewById(R.id.latalk_content_tv);
		TextView tv_user_name = (TextView) row.findViewById(R.id.user_name_tv);
		TextView tv_hold_time = (TextView) row.findViewById(R.id.hold_time_tv);
		ImageView pic_iv = (ImageView) row.findViewById(R.id.latalk_pic_iv);
		
//		LinearLayout ll = (LinearLayout) row.findViewById(R.id.latalk_item_ll);

		tv_content.setText(message.getContent());
		tv_user_name.setText(message.getUserName());
		tv_hold_time.setText(String.valueOf(message.getHold_time()));
		if(type == 1) {
			
			img_loader.DisplayImage(message.getFullPicUrl(), pic_iv, R.drawable.loading_picture);
		}
		
		
		return row;
	}
	
	public void addItem(LatalkMessage _message){
		
		messages.add(_message);
	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		return (messages.get(position).hasPic()) ? 1 : 0 ;
			
	}

	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		super.notifyDataSetChanged();
	}
	
	
	
}
