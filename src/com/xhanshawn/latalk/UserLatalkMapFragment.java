package com.xhanshawn.latalk;

import java.util.ArrayList;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.xhanshawn.data.LatalkMessage;
import com.xhanshawn.util.LocationInfoFactory;
import com.xhanshawn.util.MessageGetFactory;

import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class UserLatalkMapFragment extends Fragment {
	ArrayList<LatalkMessage> messages = new ArrayList<LatalkMessage>();
	GoogleMap user_messages_map;
	private View view;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		Location current_location = LocationInfoFactory.getCurrentLocation();

		FragmentActivity fa = super.getActivity();
		
		if(view == null){
			view = (RelativeLayout) inflater.inflate(R.layout.activity_user_latalk_map, container, false);
		} 
		//child fragment Manager is very tricky
		SupportMapFragment map_frag = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.user_latalk_map);

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
		if(messages.isEmpty()) new MessageRetriever().execute("");

		
		return view;
	}

	class MessageRetriever extends AsyncTask<String, Void, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			messages.addAll(MessageGetFactory.getUserMessages());

			return !(messages.size() == 0);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			if(result) {
				for(int i=0; i < messages.size(); i++) {
					
					LatalkMessage new_puzzle = messages.get(i);
					
					Marker puzzle_marker = user_messages_map.addMarker(new MarkerOptions()
			        .position(new LatLng(new_puzzle.getLatitude()
			        		, new_puzzle.getLongitude()))
			        .title("P " + (i+1)));
					
					puzzle_marker.showInfoWindow();
				}
				
			}
			
		}
	}
	
	
}
