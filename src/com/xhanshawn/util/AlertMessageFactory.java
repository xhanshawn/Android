package com.xhanshawn.util;

public class AlertMessageFactory {
	
	public static String getLocationEnableAlert(){
		
		String message = "Location setting is not enabled. Please turn it on to enjoy Latalk better";
		return message;
	}
	
	public static String noCameraAlert(){
		
		String message = "Your device has no camera or no camera is available";
		return message;
	}
}
