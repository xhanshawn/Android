package com.xhanshawn.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.xhanshawn.data.LatalkMessage;

public class DataPassCache {
	private static ArrayList<byte[]> pic_list = new ArrayList<byte[]>();
	private static ArrayList<LatalkMessage> time_capsule_list = new ArrayList<LatalkMessage>();
	private static int time_capsule_read = Integer.MIN_VALUE;
	private static Set<Integer> time_capsule_id_hash = new HashSet<Integer>();
	private static ArrayList<ArrayList<LatalkMessage>> lists = new ArrayList<ArrayList<LatalkMessage>>();
	public static byte[] getPicByKey(int key) {
		
		return pic_list.get(key);
	}
	
	public static int cachePic(byte[] byte_array) {
		
		pic_list.add(byte_array);
		int key = pic_list.size() - 1;
		return key;
	}
	
	public static int cacheTimeCapsule(LatalkMessage new_time_capsule) {
		
		if(time_capsule_read == Integer.MIN_VALUE) time_capsule_read = 0;
		int tc_id = new_time_capsule.getMessageId();
		
		if(time_capsule_id_hash.contains(tc_id)) return -1;
		else {
			
			time_capsule_id_hash.add(tc_id);
			time_capsule_list.add(new_time_capsule);
		}
		
		
		return time_capsule_list.size() - 1;
	}
	
	public static ArrayList<LatalkMessage> getTimeCapsule(int num) {
		
		if(time_capsule_read == Integer.MIN_VALUE) return null;
		int i = 0;
		 
		ArrayList<LatalkMessage> messages = new ArrayList<LatalkMessage>();
		
		while(i <= num - 1) {
			messages.add(time_capsule_list.get(time_capsule_read + i));
		}
		
		return messages;
	}
	
	public static LatalkMessage getTimeCapsule() {
		
		if(time_capsule_read == Integer.MIN_VALUE) return null;
		LatalkMessage message = time_capsule_list.get(time_capsule_read);
		time_capsule_read ++ ;
		return message;
	}
	
	public static int cacheLatalks(ArrayList<LatalkMessage> messages) {
		
		lists.add(messages);
		
		return lists.size() - 1;
	}
	
	public static ArrayList<LatalkMessage> getLatalks(int key) {
		return lists.get(key);
	}
	
}
