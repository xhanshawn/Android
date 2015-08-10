package com.xhanshawn.latalk;

import java.util.Calendar;

import com.xhanshawn.util.LocationInfoFactory;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class BackgroundLocationService extends Service {
	boolean isEnabled;
	public static int UPDATE_INTERVAL = 10;
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		super.onStartCommand(intent, flags, startId);
		LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		isEnabled = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		Criteria criteria = new Criteria();
		String provider = manager.getBestProvider(criteria, true);
		
//		Location current_location = manager.getLastKnownLocation(provider);
		manager.requestLocationUpdates(provider, UPDATE_INTERVAL * 1000, 1, new LocationListener(){
			Location old_location;
			
			@SuppressLint("NewApi")
			@Override
			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub
				if(old_location == null) old_location = location;
				double dis = LocationInfoFactory.calculateLatLngToDis(old_location, location);
				if(dis * 1000 >= LocationInfoFactory.UPDATE_DIS[0]) {
					
					Intent tc_map = new Intent(BackgroundLocationService.this, TimeCapsuleMapActivity.class);
					PendingIntent p_main = PendingIntent.getActivity(BackgroundLocationService.this, 0, tc_map, 0);

					Notification n = new Notification.Builder(BackgroundLocationService.this)
						.setContentTitle("larger")
						.setContentText(dis + "     ")
						.setSmallIcon(R.drawable.camera_icon)
						.setContentIntent(p_main)
						.setAutoCancel(true)
//						.addAction(R.drawable.camera_icon_pink, "pink", null)
						.build();
					NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
					nm.notify(0, n);
					
					Log.v("larger", old_location.getLatitude() + "    " + old_location.getLongitude() +
							"     " + location.getLatitude() + "     " + location.getLongitude());
				} else {
					Log.v("smaller", old_location.getLatitude() + "    " + old_location.getLongitude() +
							"     " + location.getLatitude() + "     " + location.getLongitude());
				}
				
				old_location = location;
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
			
		});
		
		return START_STICKY;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		
		super.onCreate();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
}
