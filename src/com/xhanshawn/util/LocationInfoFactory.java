package com.xhanshawn.util;

import com.xhanshawn.latalk.MainActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;

public class LocationInfoFactory {
	
	LocationManager service;
	Location current_location;
	public static boolean isEnabled = false;
	
	public LocationInfoFactory(final Context context){
		
		//check gps
		service = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
	    isEnabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
	    
	    if(!isEnabled){
	    	
	    	if(context != null) {
	    		AlertDialog.Builder gps_ad_builder = new AlertDialog.Builder(context);
		    	gps_ad_builder.setMessage(AlertMessageFactory.getLocationEnableAlert());
		    	gps_ad_builder.setCancelable(true);
		    	gps_ad_builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
						dialog.cancel();
						Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						context.startActivity(intent);
					}
				});
		    	
		    	gps_ad_builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
						dialog.cancel();
					}
				});
		    	
		    	AlertDialog gps_ad = gps_ad_builder.create();
		    	try{
		    		gps_ad.show();
		    	}catch(Exception ex){
		    		ex.printStackTrace();
		    	}
	    	}
	    }
	}
	
	public Location getCurrentLocation(){
		
		Criteria criteria = new Criteria();
		String provider = service.getBestProvider(criteria, true);
		
		current_location = service.getLastKnownLocation(provider);
		
		service.requestLocationUpdates(provider, 400, 1, new LocationListener(){

			@Override
			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub
				current_location = location;
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
		
		return current_location;
	}
}
