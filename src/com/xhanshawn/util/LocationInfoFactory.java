package com.xhanshawn.util;

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
	
	final public static long[] UPDATE_TIME = new long[] {
		1000l, 2000l, 3000l, 5000l
	};
	final public static float[] UPDATE_DIS = new float[] {
		100f, 300f, 500f, 1000f, 1500f, 2000f
	};
	
	private static LocationManager manager;
	private static Location current_location;
	public static boolean isEnabled = false;
	private static String provider = "";
	public static void initialize(final Context context){
		
		//check gps
		manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
	    isEnabled = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	    
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
	    } else {
	    	
	    	
	    	Criteria criteria = new Criteria();
			provider = manager.getBestProvider(criteria, true);
			
			current_location = manager.getLastKnownLocation(provider);
			manager.requestLocationUpdates(provider, 400, 1, new LocationListener(){

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
					current_location = new Location(provider);
					current_location.setLatitude(91.0d);
					current_location.setLongitude(181.0d);
				}
				
			}, Looper.getMainLooper());
	    }
	}
	public static LocationManager getLocationManager(){
		
		return manager;
	}
	
	public static boolean isEnabled(){
	    isEnabled = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	    return isEnabled;
	}
	public static Location getCurrentLocation(){
		
		if(!isEnabled || current_location == null) {
			current_location = new Location(provider);
			current_location.setLatitude(91.0d);
			current_location.setLongitude(181.0d);
		}
		return current_location;
	}
	
	public static double calculateLatLngToDis(Location l1, Location l2){
		double theta = l1.getLongitude() - l2.getLongitude();
		double dis = Math.acos((Math.sin(Math.PI * l1.getLatitude()/180) * Math.sin(Math.PI * l2.getLatitude()/180) + 
				Math.cos(Math.PI * l1.getLatitude()/180) * Math.cos(Math.PI * l2.getLatitude()/180) * Math.cos(Math.PI * theta/180)
				));
		dis = dis * 180/Math.PI * 60 * 1.1515;
		return dis * 1.609344;
	}
	
	public static double calLatByDis(Location origin, double dis) {
		
		double rad_lat = Math.asin(Math.cos(dis * Math.PI * 1.1515 / 3) / (Math.sin(Math.PI * origin.getLatitude()/180) + 
				Math.cos(Math.PI * origin.getLatitude()/180))); 
		double lat = rad_lat * 180 / Math.PI;
		
		return Math.abs(lat - origin.getLatitude());
	}
	
	public static double calLngByDis(Location origin, double dis) {
		
		
		double rad_theta = Math.asin(Math.cos(dis * Math.PI * 1.1515 / 3) / (Math.pow(Math.sin(Math.PI * origin.getLatitude()/180), 2) + 
				Math.pow(Math.cos(Math.PI * origin.getLatitude()/180), 2)) - 1); 
		double theta = rad_theta * 180 / Math.PI;
		
		return theta;
	}
}
