package com.xhanshawn.latalk;

import java.util.ArrayList;

import com.directions.route.Route;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.xhanshawn.data.LatalkMessage;
import com.xhanshawn.util.DataPassCache;
import com.xhanshawn.util.IntegerIdentifiers;
import com.xhanshawn.util.LocationInfoFactory;
import com.xhanshawn.view.ImageDragShadowBuilder;
import com.xhanshawn.view.ResizeAnimation;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class PuzzleRaceCreateActivity extends Activity {
	final static double BUTTONS_PANEL_RATIO = 1.0/18.0;
	final static double RACE_PANEL_RATIO = 3.5/9.0;

	private ActionBar mActionBar;
	private GridView attached_pic_pr_gv;
	private ArrayList<LatalkMessage> race_puzzles = new ArrayList<LatalkMessage>();
	private GoogleMap puzzle_race_map;
	private PuzzleGridAdapter pg_adapter;
	private boolean add_marker_enabled;
	private int current_img_position;
	private ImageView current_iv = null;
	private RelativeLayout puzzle_r_c_panel_rl;
	private LinearLayout puzzle_r_c_panel_ll;
	private LinearLayout p_r_buttons_ll;
	private int screen_height;
	private FrameLayout p_r_c_map_fl;
	private LatLng last_pin;
//	private Routing routing;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_puzzle_race_create);
		
		customActionBar();
		
		attached_pic_pr_gv = (GridView) findViewById(R.id.attached_pic_pr_gv);
		
//		if(race_puzzles.isEmpty()) openPuzzleCreateActivity();
		
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		screen_height = size.y;
		int screen_width = size.x;
		
		puzzle_r_c_panel_ll = (LinearLayout) findViewById(R.id.puzzle_r_c_panel_ll);
		puzzle_r_c_panel_ll.setLayoutParams(
				new LinearLayout.LayoutParams(screen_width, (int) (screen_height * RACE_PANEL_RATIO)));
        p_r_buttons_ll = (LinearLayout) findViewById(R.id.p_r_buttons_ll);
        p_r_buttons_ll.setLayoutParams(
        		new LinearLayout.LayoutParams(screen_width, (int) (screen_height * BUTTONS_PANEL_RATIO)));

		p_r_c_map_fl = (FrameLayout) findViewById(R.id.p_r_c_map_fl);
		
		
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

		//routing
		
		
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
					if(last_pin != null) {
						Routing routing = new Routing(Routing.TravelMode.WALKING);
						routing.registerListener(new RoutingListener() {

							@Override
							public void onRoutingFailure() {
								// TODO Auto-generated method stub
							}

							@Override
							public void onRoutingStart() {
								// TODO Auto-generated method stub
							}

							@Override
							public void onRoutingSuccess(PolylineOptions mPolyOptions,
									Route route) {
								// TODO Auto-generated method stub
								PolylineOptions polyoptions = new PolylineOptions();
						        polyoptions.color(Color.BLUE);
						        polyoptions.width(10);
						        polyoptions.addAll(mPolyOptions.getPoints());
						        puzzle_race_map.addPolyline(polyoptions);
							}
							
						});
						routing.execute(last_pin, point);
					}
					last_pin = point;
				}
			}
		});
		
		puzzle_r_c_panel_rl = (RelativeLayout) findViewById(R.id.puzzle_r_c_panel_rl);
		
		ImageButton pic_f_gallery_pr_ib = (ImageButton) findViewById(R.id.pic_f_gallery_pr_ib);
		pic_f_gallery_pr_ib.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				
				ImageButton ib = (ImageButton) v;
				
				if(event.getAction() == MotionEvent.ACTION_DOWN) {
					
					ib.setImageResource(R.drawable.landscape_blue_icon);
				}
				
				if(event.getAction() == MotionEvent.ACTION_UP) {
					
					ib.setImageResource(R.drawable.landscape_grey_icon);
					openPuzzleCreateActivity(IntegerIdentifiers.ATTACH_IMG_FROM_GAL);
				}
				return false;
			}
		});
		
		ImageButton puzzle_race_map_open_ib = (ImageButton) findViewById(R.id.puzzle_race_map_open_ib);
		puzzle_race_map_open_ib.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				ImageButton ib = (ImageButton) v;
				
				if(event.getAction() == MotionEvent.ACTION_DOWN) {
					
					ib.setImageResource(R.drawable.pin_icon_blue);
				}
				
				if(event.getAction() == MotionEvent.ACTION_UP) {
					
					ib.setImageResource(R.drawable.pin_icon_grey);
					
					
					int rl_height = puzzle_r_c_panel_rl.getHeight();

					
					if(rl_height == 0) {
						ResizeAnimation resize = new ResizeAnimation(puzzle_r_c_panel_ll, 
								(int)(screen_height * RACE_PANEL_RATIO));
						resize.setDuration(600);
						puzzle_r_c_panel_rl.startAnimation(resize);
						resize.start();
					}else {
						ResizeAnimation resize = new ResizeAnimation(puzzle_r_c_panel_ll, 
								(int)(0 - screen_height * RACE_PANEL_RATIO));
						resize.setDuration(600);
						puzzle_r_c_panel_rl.startAnimation(resize);
						resize.start();
					}
				
				}
				
				return false;
			}
		});
		
		ImageButton add_pic_f_cam_ib = (ImageButton) findViewById(R.id.add_pic_f_cam_ib);
		add_pic_f_cam_ib.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				
				ImageButton ib = (ImageButton) v;
				
				if(event.getAction() == MotionEvent.ACTION_DOWN) {
					
					ib.setImageResource(R.drawable.camera_icon_blue);
				}
				
				if(event.getAction() == MotionEvent.ACTION_UP) {
					
					ib.setImageResource(R.drawable.camera_icon_grey);
					openPuzzleCreateActivity(IntegerIdentifiers.ATTACH_IMG_FROM_CAM);
				}
				return false;
			}
		});
		
	}
	
	private void openPuzzleCreateActivity(int identifier){
		
		Intent puzzle_create_activity = new Intent("com.xhanshawn.latalk.PUZZLECREATEACTIVITY");
		puzzle_create_activity.putExtra("Img_src", identifier);
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
					if(position<puzzles.size()) {
						holder.num_icon = (TextView) convertView.findViewById(R.id.img_num_grey_icon);
						holder.close_icon = (ImageView) convertView.findViewById(R.id.puzzle_r_c_close_icon);
					}
					holder.puzzle_iv = (ImageView) convertView.findViewById(R.id.puzzle_img_grid_iv);
					convertView.setTag(holder);
				}else {
					holder = (ViewHolder) convertView.getTag();
				}
				if(position < puzzles.size()) {
					if(holder.num_icon != null) {
						int num = position + 1;
						holder.num_icon.setText("" + num);
						holder.num_icon.setBackgroundResource(R.drawable.img_num_blue_icon);
					}
					if(holder.close_icon != null) {
						holder.close_icon.setBackgroundResource(R.drawable.tiny_close_icon);
						holder.close_icon.setTag(position);
						holder.close_icon.setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								ImageView iv = (ImageView) v;
								int position = (Integer) iv.getTag();
								pg_adapter.removeItem(position);
							}
						});
					}
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
					
					if(holder.num_icon != null) {
						
						holder.num_icon.setText("");
						holder.num_icon.setBackgroundResource(R.color.transparent);
					}
					
					if(holder.close_icon != null) holder.close_icon.setBackgroundResource(R.color.transparent);
					
					holder.puzzle_iv.setImageResource(R.drawable.loading_picture);
					holder.puzzle_iv.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent puzzle_create_activity = new Intent("com.xhanshawn.latalk.PUZZLECREATEACTIVITY");
							puzzle_create_activity.putExtra("Img_src", IntegerIdentifiers.ATTACH_IMG_FROM_GAL);
							startActivityForResult(puzzle_create_activity, IntegerIdentifiers.PASS_PUZZLES);
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
				TextView num_icon;
				ImageView puzzle_iv;
				ImageView close_icon;
			}
		}

}
