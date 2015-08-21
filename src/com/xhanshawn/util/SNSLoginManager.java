package com.xhanshawn.util;

import java.util.ArrayList;
import java.util.List;

import com.xhanshawn.data.LatalkMessage;

public class SNSLoginManager {
	
	public static LatalkMessage facebookToLatalk(String Url){
		
		
		return null;
	}
	
	public static List<String> getFacebookReadPermissions(){
		List<String> permissions = new ArrayList<String>();
		permissions.add("user_photos");
		permissions.add("user_posts");
		permissions.add("user_location");
		
		return permissions;
	}
}
