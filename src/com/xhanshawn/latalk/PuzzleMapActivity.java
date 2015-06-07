package com.xhanshawn.latalk;

import java.util.ArrayList;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.xhanshawn.data.LatalkMessage;
import com.xhanshawn.util.MessageGetFactory;
import com.xhanshawn.view.LatalkItemAdapter;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

public class PuzzleMapActivity extends FragmentActivity implements OnMapReadyCallback {
	ArrayList<LatalkMessage> puzzles = new ArrayList<LatalkMessage>();
	GoogleMap puzzle_map;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_puzzle_map);
		
		MapFragment mMap = (MapFragment) getFragmentManager().findFragmentById(R.id.puzzle_map);
		mMap.getMapAsync(this);
		
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		// TODO Auto-generated method stub
		googleMap.addMarker(new MarkerOptions()
        .position(new LatLng(42.26905, -71.810625))
        .title("Home"));
		UiSettings puzzle_map_settings = googleMap.getUiSettings();
		puzzle_map_settings.setZoomControlsEnabled(true);
		googleMap.setMyLocationEnabled(true);
		puzzle_map_settings.setCompassEnabled(true);
		puzzle_map_settings.setZoomGesturesEnabled(true);
		puzzle_map = googleMap;
		new MessageRetriever().execute("");
	}
	
	public class MessageRetriever extends AsyncTask<String, Void, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			puzzles = MessageGetFactory.getPuzzleMessages();
			if(puzzles == null) {

				return false;
			}else{
				
				return true;
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			for(int i=0; i<puzzles.size(); i++) {
				
				LatalkMessage new_puzzle = puzzles.get(i);
				
				Marker puzzle_marker = puzzle_map.addMarker(new MarkerOptions()
		        .position(new LatLng(new_puzzle.getLatitude()
		        		, new_puzzle.getLongitude()))
		        .title("P " + (i+1)));
				
				puzzle_marker.showInfoWindow();
			}
		}
	}
}
