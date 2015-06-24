package com.xhanshawn.latalk;

import java.util.ArrayList;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.xhanshawn.data.LatalkMessage;
import com.xhanshawn.util.DataPassCache;
import com.xhanshawn.util.IntegerIdentifiers;
import com.xhanshawn.util.LocationInfoFactory;
import com.xhanshawn.view.ImageDragShadowBuilder;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.view.Display;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.DragShadowBuilder;
import android.view.Window.Callback;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class PuzzleRaceCreateActivity extends Activity {
	private ActionBar mActionBar;
	private GridView attached_pic_pr_gv;
	private ArrayList<LatalkMessage> race_puzzles = new ArrayList<LatalkMessage>();
	private GoogleMap puzzle_race_map;
	private PuzzleGridAdapter pg_adapter;
	private boolean add_marker_enabled;
	private int current_img_position;
	private ImageView current_iv = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_puzzle_race_create);
		
		customActionBar();
		
		attached_pic_pr_gv = (GridView) findViewById(R.id.attached_pic_pr_gv);
		
		if(race_puzzles.isEmpty()) openPuzzleCreateActivity();
		
		
		
		//
		LocationInfoFactory location_info_f = new LocationInfoFactory(PuzzleRaceCreateActivity.this);
		Location current_location = location_info_f.getCurrentLocation();
		MapFragment map_frag = (MapFragment) getFragmentManager().findFragmentById(R.id.puzzle_race_c_map);
		puzzle_race_map = map_frag.getMap();
		
		if(current_location !=null) {
			
			LatLng current_lat_lng = new LatLng(current_location.getLatitude() ,
					current_location.getLongitude());
			
			puzzle_race_map.addMarker(new MarkerOptions()
	        .position(current_lat_lng)
	        .title("My Position")
	        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
			
			CameraUpdate current_update = CameraUpdateFactory.newLatLngZoom(current_lat_lng, 16);
			puzzle_race_map.animateCamera(current_update);
		}
		
		UiSettings puzzle_map_settings = puzzle_race_map.getUiSettings();
		puzzle_map_settings.setZoomControlsEnabled(true);
		puzzle_race_map.setMyLocationEnabled(true);
		puzzle_map_settings.setCompassEnabled(true);
		puzzle_map_settings.setZoomGesturesEnabled(true);
		puzzle_race_map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
			
			@Override
			public void onMapClick(LatLng point) {
				// TODO Auto-generated method stub
				if(add_marker_enabled) {
					MarkerOptions marker = new MarkerOptions().position(
	                        new LatLng(point.latitude, point.longitude)).title("New Marker");

					puzzle_race_map.addMarker(marker);
					add_marker_enabled = false;
					pg_adapter.removeItem(current_img_position);
					
				}
			}
		});
		
	}
	
	private void openPuzzleCreateActivity(){
		
		Intent puzzle_create_activity = new Intent("com.xhanshawn.latalk.PUZZLECREATEACTIVITY");
		startActivityForResult(puzzle_create_activity, IntegerIdentifiers.PASS_PUZZLES);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == IntegerIdentifiers.PASS_PUZZLES) {
			Bundle extras = data.getExtras();
			int key = extras.getInt("puzzles_key");
			race_puzzles.addAll(DataPassCache.getLatalks(key));
		}
		
		pg_adapter = new PuzzleGridAdapter(PuzzleRaceCreateActivity.this, race_puzzles);
		attached_pic_pr_gv.setAdapter(pg_adapter);
	}

	
	
	private void customActionBar(){
		
		
		mActionBar = getActionBar();
		mActionBar.show();
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.actionbar_puzzle_race_create,null);
		
		mActionBar.setDisplayShowCustomEnabled(true);

		mActionBar.setCustomView(v);
	    
	    Button back_to_main_b = (Button) v.findViewById(R.id.c_p_r_to_main_b);
	    back_to_main_b.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PuzzleRaceCreateActivity.this.finish();
			}
		});
	    
	    Button puzzle_r_ok_b = (Button) v.findViewById(R.id.puzzle_r_create_ok_b);
	    puzzle_r_ok_b.setText("Post");
	    puzzle_r_ok_b.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PuzzleRaceCreateActivity.this.finish();
			}
		});
	    
	}
	
	 class PuzzleGridAdapter extends BaseAdapter{
			Context context;
			ArrayList<LatalkMessage> puzzles = new ArrayList<LatalkMessage>();

			public PuzzleGridAdapter(Context context, ArrayList<LatalkMessage> data){
				
				this.context = context;
				this.puzzles = data;
				add_marker_enabled = false;
			}
			
			public void removeItem(int current_img_position) {
				
				puzzles.remove(current_img_position);
				PuzzleGridAdapter.this.notifyDataSetChanged();
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
				if(position < puzzles.size()) {
					holder.puzzle_iv.setTag(position);
					holder.puzzle_iv.setImageBitmap(puzzles.get(position).getAttahedPic());
					holder.puzzle_iv.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if (current_iv != null) current_iv.setBackgroundResource(R.color.transparent);
							current_iv = (ImageView) v;
							current_iv.setBackgroundResource(R.color.blue);
							add_marker_enabled = true;
							current_img_position = (Integer) current_iv.getTag();;
						}
					});
					
				}
				
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
			
			
			@Override
			public void notifyDataSetChanged() {
				// TODO Auto-generated method stub
				super.notifyDataSetChanged();
				if(current_iv != null) current_iv.setBackgroundResource(R.color.transparent);
			}


			class ViewHolder{
				
				ImageView puzzle_iv;
			}
		}

}
