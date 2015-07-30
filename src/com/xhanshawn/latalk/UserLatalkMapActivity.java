package com.xhanshawn.latalk;

import java.util.ArrayList;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.xhanshawn.data.LatalkMessage;
import com.xhanshawn.util.LocationInfoFactory;
import com.xhanshawn.util.MessageGetFactory;

import android.app.Activity;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class UserLatalkMapActivity extends FragmentActivity {
	
	ArrayList<LatalkMessage> puzzles = new ArrayList<LatalkMessage>();
	ArrayList<LatalkMessage> time_capsules = new ArrayList<LatalkMessage>();

	GoogleMap user_messages_map;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_latalk_map);
		
		Location current_location = LocationInfoFactory.getCurrentLocation();
		
		
		
		MapFragment map_frag = (MapFragment) getFragmentManager().findFragmentById(R.id.user_latalk_map);
		user_messages_map = map_frag.getMap();
		
		//update together, not separately
		
//		map.moveCamera(CameraUpdateFactory.newLatLng(current_lat_lng));
		
		if(current_location !=null) {
			
			LatLng current_lat_lng = new LatLng(current_location.getLatitude() ,
					current_location.getLongitude());
			
			user_messages_map.addMarker(new MarkerOptions()
	        .position(current_lat_lng)
	        .title("Current"));
			
			CameraUpdate current_update = CameraUpdateFactory.newLatLngZoom(current_lat_lng, 16);
			user_messages_map.animateCamera(current_update);
		}
		
		UiSettings puzzle_map_settings = user_messages_map.getUiSettings();
		puzzle_map_settings.setZoomControlsEnabled(true);
		user_messages_map.setMyLocationEnabled(true);
		puzzle_map_settings.setCompassEnabled(true);
		puzzle_map_settings.setZoomGesturesEnabled(true);
		new MessageRetriever().execute("");
	}
	
	
	class MessageRetriever extends AsyncTask<String, Void, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			puzzles = MessageGetFactory.getPuzzleMessages();
			
			time_capsules = MessageGetFactory.getTimeCapsuleMessages();

			if(puzzles == null || time_capsules == null) {

				return false;
			}else{
				
				return true;
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			if(result) {
				
				for(int i=0; i<puzzles.size(); i++) {
					
					LatalkMessage new_puzzle = puzzles.get(i);
					
					Marker puzzle_marker = user_messages_map.addMarker(new MarkerOptions()
			        .position(new LatLng(new_puzzle.getLatitude()
			        		, new_puzzle.getLongitude()))
			        .title("P " + (i+1)));
					
					puzzle_marker.showInfoWindow();
				}
				
				for(int i=0; i<time_capsules.size(); i++) {
					
					LatalkMessage new_time_capsule = time_capsules.get(i);
					
					Marker tc_marker = user_messages_map.addMarker(new MarkerOptions()
			        .position(new LatLng(new_time_capsule.getLatitude()
			        		, new_time_capsule.getLongitude()))
			        .title("tc " + (i+1)));
					
					tc_marker.showInfoWindow();
				}
				
			}
			
		}
	}
}
