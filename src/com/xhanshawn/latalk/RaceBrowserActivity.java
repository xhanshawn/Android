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
import com.xhanshawn.util.LocationInfoFactory;
import com.xhanshawn.util.MessageGetFactory;
import com.xhanshawn.util.NotiArrayList;
import com.xhanshawn.util.NotiArrayList.OnSizeChangeListener;
import com.xhanshawn.util.NotiArrayList.SizeChangeEvent;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RaceBrowserActivity extends Activity {
	Location current_location;
	LocationManager manager;
	GoogleMap puzzle_race_map;
	NotiArrayList<LatalkMessage> messages = new NotiArrayList<LatalkMessage>();
	int add_num = 0;
	ActionBar mActionBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_race_browser);
		
		customActionBar();
		
		current_location = LocationInfoFactory.getCurrentLocation();
		
		manager = LocationInfoFactory.getLocationManager();
		
		manager.requestLocationUpdates(manager.getBestProvider(new Criteria(), true), 
				LocationInfoFactory.UPDATE_TIME[3], 
				LocationInfoFactory.UPDATE_DIS[0], 
				new LocationListener() {

					@Override
					public void onLocationChanged(Location location) {
						// TODO Auto-generated method stub
						if(Math.abs(LocationInfoFactory.calculateLatLngToDis(location, current_location)) > 0.5) {
							Log.v("nlocation", location.getLatitude() + "   " + location.getLongitude());
							Log.v("olocation", current_location.getLatitude() + "   " + current_location.getLongitude());
							current_location = location;
							askPRS();
						}
						
						LatLng current_lat_lng = new LatLng(location.getLatitude() ,
								location.getLongitude());
						CameraUpdate current_update = CameraUpdateFactory.newLatLngZoom(current_lat_lng, 16);
						puzzle_race_map.animateCamera(current_update);
					}

					@Override
					public void onStatusChanged(String provider, int status,
							Bundle extras) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onProviderEnabled(String provider) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onProviderDisabled(String provider) {
						// TODO Auto-generated method stub
						
					}
			
		}, Looper.getMainLooper());
		
		askPRS();
		
		MapFragment map_frag = (MapFragment) getFragmentManager().findFragmentById(R.id.puzzle_map);
		
		puzzle_race_map = map_frag.getMap();
		
		if(current_location !=null) {
			
			LatLng current_lat_lng = new LatLng(current_location.getLatitude() ,
					current_location.getLongitude());
			
			CameraUpdate current_update = CameraUpdateFactory.newLatLngZoom(current_lat_lng, 16);
			puzzle_race_map.animateCamera(current_update);
		}
		
		UiSettings puzzle_map_settings = puzzle_race_map.getUiSettings();
		puzzle_map_settings.setZoomControlsEnabled(true);
		puzzle_race_map.setMyLocationEnabled(true);
		puzzle_map_settings.setCompassEnabled(true);
		puzzle_map_settings.setZoomGesturesEnabled(true);
		NotiArrayList<LatalkMessage> pr_cache = DataPassCache.getRaceCache();
		pr_cache.setOnSizeChangeListener(new OnSizeChangeListener(){

			@Override
			public void sizeChange(SizeChangeEvent event) {
				// TODO Auto-generated method stub
				
				if(event.getEvent() == SizeChangeEvent.ADD) {
					messages.add(DataPassCache.getPuzzleRace());
				}
			}
		});
		
		
		messages.setOnSizeChangeListener(new OnSizeChangeListener(){

			@Override
			public void sizeChange(SizeChangeEvent event) {
				// TODO Auto-generated method stub
				if(event.getEvent() == SizeChangeEvent.ADD && add_num < messages.size()) {
					Log.v("prs_change", "" + event.getNewSize() );
					runOnUiThread(new Runnable(){
						
						@Override
						public void run() {
							LatalkMessage pr = messages.get(add_num);
							if(pr == null) {
								add_num ++;
								return;
							}
							
							String marker_str = " " + pr.getMessageId();
							MarkerOptions marker = new MarkerOptions().position(
			                    new LatLng(pr.getLatitude(), pr.getLongitude()))
			                    .title(marker_str)
			                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));

							puzzle_race_map.addMarker(marker);
							add_num ++;
						}
					});
				}
			}
		});
		
	}
	
	
	private void askPRS(){
		if(!LocationInfoFactory.isEnabled()) DataPassCache.retrieveMessage(MessageGetFactory.PR_AWAY);
		else DataPassCache.retrieveMessage(MessageGetFactory.PR_NEAR_BY);
		
	}
	
	private void customActionBar(){
		
		
		mActionBar = getActionBar();
		mActionBar.show();
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.actionbar_color_with_text,null);
		
		mActionBar.setDisplayShowCustomEnabled(true);

		mActionBar.setCustomView(v);
	    mActionBar.setBackgroundDrawable(getResources().getDrawable(R.color.green));

		Button back_to_main_b = (Button) v.findViewById(R.id.c_p_r_to_main_b);
	    back_to_main_b.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				RaceBrowserActivity.this.finish();
			}
		});
	    
	    Button puzzle_r_ok_b = (Button) v.findViewById(R.id.puzzle_r_create_ok_b);
	    puzzle_r_ok_b.setText("Post");
	    puzzle_r_ok_b.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				RaceBrowserActivity.this.finish();
			}
		});
	    
	    TextView banner_tv = (TextView) findViewById(R.id.actionbar_color_banner);
	    banner_tv.setText("Puzzle Race");
	}
}
