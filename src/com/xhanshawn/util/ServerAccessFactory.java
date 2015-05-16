package com.xhanshawn.util;

import com.xhanshawn.data.UserAccount;

public class ServerAccessFactory {
	private static String url_base = "http://10.0.2.2:3000";
	
	public static String createUser(){
		
		return url_base + "/users.json";
	}
	
	public static String login(){
		
		return url_base + "/users/login.json";
	}
	
	public static String logout(){
		
		return url_base + "/users/logout.json";
	}
}
