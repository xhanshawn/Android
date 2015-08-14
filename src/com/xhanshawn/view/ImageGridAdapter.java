package com.xhanshawn.view;

import java.util.ArrayList;

import com.fedorvlasov.lazylist.ImageLoader;
import com.xhanshawn.data.LatalkMessage;
import com.xhanshawn.latalk.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ImageGridAdapter extends BaseAdapter{
	
	private Context context;
	private ArrayList<LatalkMessage> messages;
	private ImageLoader img_loader;
	
	
	public ImageGridAdapter(Context context, ArrayList<LatalkMessage> messages) {
		
		this.context = context;
		this.messages = messages;
		
		//adopted lazy list tool developed by fedorvlasov
		//I made some modifications for that
		img_loader = new ImageLoader(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return messages.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return messages.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		
		if(convertView == null) {
			holder = new ViewHolder();
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			convertView = inflater.inflate(R.layout.image_item_layout, parent, false);
			holder.latalk_iv = (ImageView) convertView.findViewById(R.id.img_grid_iv);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.latalk_iv.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				//to test listener
				//should be replaced by other action
				v.setRotation(180);
			}
		});
		
		
		if(holder.latalk_iv.getDrawable() == null) {
			
			LatalkMessage message = messages.get(position);
			img_loader.DisplayImage(message.getFullPicUrl(), holder.latalk_iv, R.drawable.loading_picture);
		}
		
		return convertView;
	}
	
	public void clearImageCache() {
		
		img_loader.clearCache();
	}
	
	
	class ViewHolder{
		ImageView latalk_iv;
	}
}
