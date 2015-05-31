package com.xhanshawn.view;

import java.util.ArrayList;

import com.xhanshawn.data.LatalkMessage;
import com.xhanshawn.latalk.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LatalkItemAdapter extends BaseAdapter {
	
	private Context context;
	private ArrayList<LatalkMessage> messages;
	
	public LatalkItemAdapter(Context _context, ArrayList<LatalkMessage> _messages ){
		
		this.context = _context;
		this.messages = _messages;
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
		
		if(row == null){
			
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(R.layout.latalk_item, parent, false);
//			row = View.inflate(context, , null);
		}
		
		LatalkMessage message = messages.get(position);
		
		TextView tv_content = (TextView) row.findViewById(R.id.latalk_content_tv);
		TextView tv_user_name = (TextView) row.findViewById(R.id.user_name_tv);
		TextView tv_hold_time = (TextView) row.findViewById(R.id.hold_time_tv);
		ImageView pic_iv = (ImageView) row.findViewById(R.id.latalk_pic_iv);
		
		pic_iv.setImageBitmap(message.getAttahedPic());
		tv_content.setText(message.getContent());
		tv_user_name.setText(message.getUser_name());
		tv_hold_time.setText(String.valueOf(message.getHold_time()));
		
		
		return row;
	}
	
	public void addItem(LatalkMessage _message){
		
		messages.add(_message);
	}
}
