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
	
	public static String locationClosedAlert(){
		String message = "Location is not available. We've found something far from you";
		return message;
	}
	
	public static String noMessagesFound(){
		String message = "Sorry, we can not find messages";
		return message;
	}
	
	public static String loadingMessageFailed(){
		String message = "loadingMessageFailed";
		return message;
	}
	
	public static String clearCacheSuccess(){
		String message = "Clear Cache Succeed";
		return message;
	}
	
	public static String chooseImgOneByOne(){
		String message = "Your Android system version is too low. You have to add picture one by one!";
		return message;
	}
}
