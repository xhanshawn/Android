package com.xhanshawn.util;

import com.xhanshawn.data.UserAccount;

public class ServerAccessFactory {
	//default avd
//	private static String url_base = "http://10.0.2.2:3000";

	//genymotion
	private static String url_base = "http://10.0.3.2:3000";
	
	public static String createUser(){
		
		return url_base + "/users.json";
	}
	
	public static String login(){
		
		return url_base + "/users/login.json";
	}
	
	public static String logout(){
		
		return url_base + "/users/logout.json";
	}
	
	public static String queryMessage(){
		
		String user_name = UserAccount.getCurrentUserName();
		return url_base + "/messages.json?user_name=" + user_name;
	}
	
	public static String getUrlBase(){
		
		return url_base;
	}
}
