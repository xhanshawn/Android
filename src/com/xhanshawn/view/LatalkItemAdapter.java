package com.xhanshawn.view;

import java.util.ArrayList;
import java.util.Calendar;

import com.fedorvlasov.lazylist.ImageLoader;
import com.xhanshawn.data.LatalkMessage;
import com.xhanshawn.latalk.R;
import com.xhanshawn.util.MessageGetFactory;
import com.xhanshawn.view.ImageGridAdapter.ViewHolder;

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
		int type = getItemViewType(position);
		ViewHolder holder = null;
		
		if(convertView == null){
			holder = new ViewHolder();
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();

			if(type == 0) { 
				
				convertView = inflater.inflate(R.layout.latalk_item, parent, false);
				convertView.setTag(holder);
			} else {
				
				convertView = inflater.inflate(R.layout.latalk_item_with_pic, null);
				convertView.setTag(holder);
				holder.pic_iv = (ImageView) convertView.findViewById(R.id.latalk_pic_iv);
			}
			
			holder.tv_content = (TextView) convertView.findViewById(R.id.latalk_content_tv);
			holder.tv_user_name = (TextView) convertView.findViewById(R.id.user_name_tv);
			holder.tv_hold_time = (TextView) convertView.findViewById(R.id.hold_time_tv);
			
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		LatalkMessage message = messages.get(position);
		
		
		holder.tv_content.setText(message.getContent());
		holder.tv_user_name.setText(message.getUserName());
		Calendar c = Calendar.getInstance();
		long hold_time = message.getHold_time() - c.getTimeInMillis()/1000;
		hold_time /= (3600 * 24);
		holder.tv_hold_time.setText(hold_time + ((hold_time>1) ? " days" : " day"));
		if(type == 1 && holder.pic_iv != null && holder.pic_iv.getDrawable() == null) {
			img_loader.DisplayImage(message.getFullPicUrl(), holder.pic_iv, R.drawable.loading_picture);
		}
		
		
		return convertView;
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
		return (messages.get(position).isValidPicUrl()) ? 1 : 0 ;
			
	}

	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		super.notifyDataSetChanged();
	}
	
	class ViewHolder{

		TextView tv_content;
		TextView tv_user_name;
		TextView tv_hold_time;
		ImageView pic_iv;
	}
	
}
