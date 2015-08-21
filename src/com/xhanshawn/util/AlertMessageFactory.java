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
	
	public static String pickHoldTime(){
		String message = "We will keep your Time Casule until the time you pick.";
		return message;
	}
	
//	public static String postFailed(){
//		String message = "Post message failed. Please check your networks or Server is not available.";
//		return message;
//	}
	
	public static String noContentLatalk(){
		String message = "This puzzle doesn't have description";
		return message;
	}
	
	public static String chooseAttachSources(){
		String message = "Pick a image source";
		return message;
	}
	
	public static String tcInputHint(){
		String message = "Anything you want to keep in a Time Capsule";
		return message;
	}
	
	public static String postSucceeded(){
		String message = "Your messages were posted successfully";
		return message;
	}
	
	public static String postFailed(){
		String message = "Post failed. Messages will be kept utill network is available and then post all";
		return message;
	}
	
	public static String alreadyLogin(){
		String message = "You've already loged in";
		return message;
	}
}
