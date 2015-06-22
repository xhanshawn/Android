package com.xhanshawn.view;

import java.util.ArrayList;

import com.xhanshawn.data.LatalkMessage;
import com.xhanshawn.latalk.PuzzleCreateActivity;
import com.xhanshawn.latalk.R;
import com.xhanshawn.util.AlertMessageFactory;
import com.xhanshawn.util.IntegerIdentifiers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class PicPuzzleAdapter extends BaseAdapter {
	
	
	ArrayList<LatalkMessage> data = new ArrayList<LatalkMessage>();
	Context context;
	ViewHolder holder;
	int current_position;
	
	

	public PicPuzzleAdapter (Context context, ArrayList<LatalkMessage> user_data){
		
		this.data = user_data;
		this.context = context;
		
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size() + 1;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if(position > data.size()) return null;
		return data.get(position);
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
//		current_position = position;
		if(convertView == null) {
			
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			holder = new ViewHolder();
			if(position < data.size()) {
				convertView = inflater.inflate(R.layout.pic_puzzle_item, parent, false);
			}else{
				convertView = inflater.inflate(R.layout.add_item, parent, false);
			}
				
			holder.pic = (ImageView) convertView.findViewById(R.id.pic_puzzle_iv);
			if(position < data.size()) {
				holder.pic_description = (EditText) convertView.findViewById(R.id.pic_puzzle_et);
				holder.pic_description.setTag(data.get(position));
			}
			
			convertView.setTag(holder);	
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		if(position < data.size()) {
			LatalkMessage message = data.get(position);
			holder.pic.setImageBitmap(message.getAttahedPic());
		}
		if(position == data.size() && position < 8) {
			holder.pic.setImageResource(R.drawable.loading_picture);
		}
		
		holder.pic.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
				photoPickerIntent.setType("image/*");
				
				if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
					Toast lower_version_toast = Toast.makeText(context,
							AlertMessageFactory.chooseImgOneByOne(),
							Toast.LENGTH_LONG);

					lower_version_toast.show();
				}
				
				photoPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
				((Activity) context).startActivityForResult(photoPickerIntent, IntegerIdentifiers.SELECT_PHOTO); 
			}
		});
		
		if(position < data.size()) holder.pic_description.setOnFocusChangeListener(new View.OnFocusChangeListener(){
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				EditText et = (EditText) v;
				if(!hasFocus) ((LatalkMessage) v.getTag()).setContent(et.getEditableText().toString());
			}
		});
		
		return convertView;
	}

	public ArrayList<LatalkMessage> getItems(){
		return data;
	}
	
	class ViewHolder {
		
		EditText pic_description;
		ImageView pic;
	}
}
