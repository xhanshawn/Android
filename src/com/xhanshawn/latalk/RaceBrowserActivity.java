package com.xhanshawn.latalk;

import java.util.ArrayList;
import java.util.List;

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

import android.app.Activity;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class RaceBrowserActivity extends Activity {
	Location location;
	GoogleMap puzzle_race_map;
	NotiArrayList<LatalkMessage> messages;
	int add_num = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_race_browser);
		
		location = LocationInfoFactory.getCurrentLocation();
		
		if(!LocationInfoFactory.isEnabled()) DataPassCache.retrieveMessage(MessageGetFactory.PR_AWAY);
		else DataPassCache.retrieveMessage(MessageGetFactory.PR_NEAR_BY);
		
		MapFragment map_frag = (MapFragment) getFragmentManager().findFragmentById(R.id.puzzle_map);
		
		puzzle_race_map = map_frag.getMap();
		
		if(location !=null) {
			
			LatLng current_lat_lng = new LatLng(location.getLatitude() ,
					location.getLongitude());
			
			CameraUpdate current_update = CameraUpdateFactory.newLatLngZoom(current_lat_lng, 16);
			puzzle_race_map.animateCamera(current_update);
		}
		
		UiSettings puzzle_map_settings = puzzle_race_map.getUiSettings();
		puzzle_map_settings.setZoomControlsEnabled(true);
		puzzle_race_map.setMyLocationEnabled(true);
		puzzle_map_settings.setCompassEnabled(true);
		puzzle_map_settings.setZoomGesturesEnabled(true);
		
		messages = DataPassCache.getRaceCache();
		messages.setOnSizeChangeListener(new OnSizeChangeListener(){
			List<LatalkMessage> prs = new ArrayList<LatalkMessage>();
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
	
	private void askMore(){
		if(!LocationInfoFactory.isEnabled()) DataPassCache.retrieveMessage(MessageGetFactory.PR_AWAY);
		else DataPassCache.retrieveMessage(MessageGetFactory.PR_NEAR_BY);
		
	}
	
	private void addMarker(){
		if(add_num >= messages.size()) {
			askMore();
			return;
		}
		
		for(int i = add_num; i < messages.size(); i++) {
			LatalkMessage m = messages.get(i);
			String marker_str = " " + m.getMessageId();
			MarkerOptions marker = new MarkerOptions().position(
                    new LatLng(m.getLatitude(), m.getLongitude()))
                    .title(marker_str)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));

			puzzle_race_map.addMarker(marker);
			add_num++;
		}
	}
	
	class Updater extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			askMore();
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
		}
		
	}
}
