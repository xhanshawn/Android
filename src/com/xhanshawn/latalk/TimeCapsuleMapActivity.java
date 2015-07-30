package com.xhanshawn.latalk;

import java.util.ArrayList;

import com.fedorvlasov.lazylist.ImageLoader;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.xhanshawn.data.LatalkMessage;
import com.xhanshawn.util.AlertMessageFactory;
import com.xhanshawn.util.DataPassCache;
import com.xhanshawn.util.IntegerIdentifiers;
import com.xhanshawn.util.LocationInfoFactory;
import com.xhanshawn.util.MessageGetFactory;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class TimeCapsuleMapActivity extends Activity {
	
	ActionBar mActionBar;
	private ArrayList<LatalkMessage> tcs = new ArrayList<LatalkMessage> ();
	private int tc_got_num = 0;
	private int thumb_got_num = 0;
	private int sml_thumb_num = 0;

	
	private GoogleMap tc_map;
	RelativeLayout tc_map_rl;
	int width;
	int height;
	ImageView pic_iv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_time_capsule_map);
		
		customActionBar();
		tc_map_rl = (RelativeLayout) findViewById(R.id.tc_map_rl);
		
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		width = size.x;
		height = size.y;
		
		Location current_location = LocationInfoFactory.getCurrentLocation();
		MapFragment map_frag = (MapFragment) getFragmentManager().findFragmentById(R.id.tc_map);
		
		tc_map = map_frag.getMap();
		if(current_location != null) {
			
			LatLng current_lat_lng = new LatLng(current_location.getLatitude() ,
				current_location.getLongitude());
		
			CameraUpdate current_update = CameraUpdateFactory.newLatLngZoom(current_lat_lng, 16);
			tc_map.animateCamera(current_update);
		}
	
		UiSettings puzzle_map_settings = tc_map.getUiSettings();
		puzzle_map_settings.setZoomControlsEnabled(true);
		tc_map.setMyLocationEnabled(true);
		puzzle_map_settings.setCompassEnabled(true);
		puzzle_map_settings.setZoomGesturesEnabled(true);
		tcs.addAll(DataPassCache.getTimeCapsules(DataPassCache.ALL));
		
		tc_map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
			
			@Override
			public boolean onMarkerClick(Marker marker) {
				// TODO Auto-generated method stub
				
				Intent full_pic = new Intent("com.xhanshawn.latalk.FULLPICACTIVITY");
				full_pic.putExtra("id", marker.getTitle());
				startActivity(full_pic);
				return false;
			}
		});
	    new TimeCapsuleGetter().execute(IntegerIdentifiers.GET_PIC);

	}
	
	private void customActionBar(){
		
		
		mActionBar = getActionBar();
		mActionBar.show();
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.actionbar_color_with_img,null);
		
		mActionBar.setDisplayShowCustomEnabled(true);

		mActionBar.setCustomView(v);
	    mActionBar.setBackgroundDrawable(getResources().getDrawable(R.color.purple));

		Button back_to_main_b = (Button) v.findViewById(R.id.color_ab_back_b);
	    back_to_main_b.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TimeCapsuleMapActivity.this.finish();
			}
		});
	    
	    ImageButton tc_map_switch_b = (ImageButton) v.findViewById(R.id.color_ab_ib);
	    tc_map_switch_b.setImageResource(R.drawable.map_switch_white);
	    tc_map_switch_b.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				TimeCapsuleMapActivity.this.finish();
			}
		});
	    
	    TextView banner_tv = (TextView) findViewById(R.id.actionbar_color_banner);
	    banner_tv.setText("Time Capsule");
	}

	class TimeCapsuleGetter extends AsyncTask<Integer, Void, Integer> {

		@Override
		protected Integer doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			
			
			switch (params[0]) {
			
				case IntegerIdentifiers.GET_TIMECAPSULE:
					Location current_location = LocationInfoFactory.getCurrentLocation();
					tcs.addAll(MessageGetFactory.getTimeCapsuleMessagesNearby(current_location));
//					tcs.addAll(MessageGetFactory.getTimeCapsuleMessages());
					break;
					
				case IntegerIdentifiers.GET_PIC:
					
					if(sml_thumb_num < tcs.size()) {
						for(int i = sml_thumb_num; i < tcs.size(); i++) {
							LatalkMessage message = tcs.get(i);
							message.setSmallThumbPic(
									MessageGetFactory.getImage(message.getSmallThumbUrl()));
							
						}
						sml_thumb_num = tcs.size();
					}
					break;
			
				default: break;
			
			}
			return params[0];
		}

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			
			switch(result) {
				case IntegerIdentifiers.GET_TIMECAPSULE:
					new TimeCapsuleGetter().execute(IntegerIdentifiers.GET_PIC);
					break;
				case IntegerIdentifiers.GET_PIC:
					if(tc_got_num < tcs.size()) {
						
						for(int i=tc_got_num; i<tcs.size();i++) {
							LatalkMessage tc = tcs.get(i);
							float lng = tc.getLongitude();
							float lat = tc.getLatitude();
							Bitmap thumb = tc.getSmallThumbPic();
							BitmapDescriptor bd = null;
							if(thumb != null) bd = BitmapDescriptorFactory.fromBitmap(tc.getSmallThumbPic());
//							else bd = BitmapDescriptorFactory.fromResource(R.drawable.loading_picture);
							MarkerOptions marker = new MarkerOptions().position(
			                        new LatLng(lat, lng))
			                        .title("" + tc.getMessageId())
			                        .icon(bd);
							tc_map.addMarker(marker);
						}
						tc_got_num = tcs.size();
					}
					
					break;
		
				default: break;
			}
			super.onPostExecute(result);
		}
		
		
		
	}
	
}
