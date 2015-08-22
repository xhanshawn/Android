package com.xhanshawn.latalk;

import com.xhanshawn.view.MyListView;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class QuerySettingsActivity extends Activity {
	MyListView query_mlv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_query_settings);
		showActionBar();
		
//		query_mlv = (MyListView) findViewById(R.id.query_mlv);
//		query_mlv.setAdapter(new BaseAdapter(){
//			final static int UPDATE_INTERVAL = 0;
//			final static String interval_prefix = "Update interval: ";
//			final static int MAX_UPDATE_INTERVAL = 20;
//			final static int MIN_UPDATE_INTERVAL = 5;
//			TextView setting_title;
//			@Override
//			public int getCount() {
//				return 1;
//			}
//
//			@Override
//			public Object getItem(int position) {return null;}
//			@Override
//			public long getItemId(int position) {return 0;}
//			@Override
//			public View getView(int position, View convertView, ViewGroup parent) {
//				ViewHolder holder;
//				if(convertView == null){
//					LayoutInflater inflater = QuerySettingsActivity.this.getLayoutInflater();
//					
//					switch(position){
//					case UPDATE_INTERVAL:
//						convertView = inflater.inflate(R.layout.seekbar_list, null,false);
//						setting_title = (TextView) convertView.findViewById(R.id.seekb_list_tv);
//						setting_title.setText(interval_prefix + BackgroundLocationService.UPDATE_INTERVAL + "s");
//						SeekBar interval = (SeekBar) convertView.findViewById(R.id.list_seek);
//						interval.setMax(MAX_UPDATE_INTERVAL);
//						interval.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
//							@Override
//							public void onStopTrackingTouch(SeekBar seekBar) {
//								// TODO Auto-generated method stub
//								
//							}
//							
//							@Override
//							public void onStartTrackingTouch(SeekBar seekBar) {
//								// TODO Auto-generated method stub
//								
//							}
//							
//							@Override
//							public void onProgressChanged(SeekBar seekBar, int progress,
//									boolean fromUser) {
//								// TODO Auto-generated method stub
//								Log.v("progress", setting_title.getText() +"");
//								updateTitle(progress);
////								BackgroundLocationService.UPDATE_INTERVAL = progress;
//							}
//						});
//						
//						
//						break;
//					default: 
//						break;
//					}
//					
//				}
//				return convertView;
//			}
//			private void updateTitle(int progress){
//				setting_title.setText(interval_prefix + String.valueOf(progress + MIN_UPDATE_INTERVAL) + "s");
//			}
//			
//			class ViewHolder{
//				TextView title;
//				SeekBar sb;
//			}
//		});
		
		getFragmentManager().beginTransaction()
        .replace(android.R.id.content, new QuerySettingsFragment())
        .commit();
	}
	
	private void showActionBar(){

		ActionBar actionBar = getActionBar();
		actionBar.show();
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.actionbar_color_only_back_b, null);
		
	    actionBar.setDisplayShowCustomEnabled(true);

	    actionBar.setCustomView(v);
	    actionBar.setBackgroundDrawable(getResources().getDrawable(R.color.deep_pink));
	    
	    Button back_to_main_b = (Button) v.findViewById(R.id.color_ab_back_b);
	    back_to_main_b.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				QuerySettingsActivity.this.finish();
			}
		});
	    
	    TextView banner_tv = (TextView) findViewById(R.id.actionbar_color_banner);
	    banner_tv.setText("Query Settings");
	}
}
