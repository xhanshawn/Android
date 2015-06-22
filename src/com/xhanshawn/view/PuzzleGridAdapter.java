package com.xhanshawn.view;

import java.util.ArrayList;

import com.xhanshawn.data.LatalkMessage;
import com.xhanshawn.latalk.R;
import com.xhanshawn.util.IntegerIdentifiers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class PuzzleGridAdapter extends BaseAdapter{
	Context context;
	ArrayList<LatalkMessage> puzzles = new ArrayList<LatalkMessage>();
	
	public PuzzleGridAdapter(Context context, ArrayList<LatalkMessage> data){
		
		this.context = context;
		this.puzzles = data;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return (puzzles.size() + 1 <= 8) ? puzzles.size() + 1 : 8;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if(position<puzzles.size()) return puzzles.get(position);
		else return null;
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
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			holder = new ViewHolder();
			
			convertView = inflater.inflate(R.layout.puzzle_img_grid_item, parent, false);
			holder.puzzle_iv = (ImageView) convertView.findViewById(R.id.puzzle_img_grid_iv);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		if(position < puzzles.size()) holder.puzzle_iv.setImageBitmap(puzzles.get(position).getAttahedPic());
		
		if(position == puzzles.size() && position < 8) {
			
			holder.puzzle_iv.setImageResource(R.drawable.loading_picture);
			holder.puzzle_iv.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent puzzle_create_activity = new Intent("com.xhanshawn.latalk.PUZZLECREATEACTIVITY");
					((Activity)context).startActivityForResult(puzzle_create_activity, IntegerIdentifiers.PASS_PUZZLES);
				}
			});
		}
		
		return convertView;
	}
	
	class ViewHolder{
		
		ImageView puzzle_iv;
	}
}
