package com.xhanshawn.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.util.SparseIntArray;

import com.xhanshawn.data.LatalkMessage;

public class DataPassCache {
	
	final public static int ALL = -1;
	final public static int UNREAD_ALL = -2;
	final public static boolean NEAR_BY = true;
	final public static boolean AWAY = false;

	
	private static ArrayList<byte[]> pic_list = new ArrayList<byte[]>();
	private static ArrayList<LatalkMessage> time_capsule_list = new ArrayList<LatalkMessage>();
	private static int time_capsule_read = Integer.MIN_VALUE;
	private static Set<Integer> time_capsule_id_hash = new HashSet<Integer>();
	private static ArrayList<ArrayList<LatalkMessage>> lists = new ArrayList<ArrayList<LatalkMessage>>();
	private static ArrayList<LatalkMessage> puzzle_race_list = new ArrayList<LatalkMessage>();
	private static SparseIntArray race_id_map = new SparseIntArray();
	private static int race_read = 0;
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
	
	public static List<LatalkMessage> getTimeCapsules(int num) {
		
		if(time_capsule_read == Integer.MIN_VALUE) return new ArrayList<LatalkMessage>();
		if(num == ALL) return  time_capsule_list.subList(time_capsule_read, time_capsule_list.size());
		int i = 0;
		
		ArrayList<LatalkMessage> messages = new ArrayList<LatalkMessage>();
		
		while(i <= num - 1) {
			messages.add(time_capsule_list.get(time_capsule_read + i));
		}
		
		return messages;
	}
	
	public static LatalkMessage getTimeCapsule() {
		
		if(time_capsule_read == Integer.MIN_VALUE || time_capsule_read >= time_capsule_list.size()) return null;
		LatalkMessage message = time_capsule_list.get(time_capsule_read);
		time_capsule_read ++ ;
		return message;
	}
	
	public static LatalkMessage getTCById(int id){
		for(LatalkMessage m : time_capsule_list) if(m.getMessageId() == id) return m;
		return null;
	}
	
	public static int getTCSize(){
		return time_capsule_list.size();
	}
	public static int cacheLatalks(ArrayList<LatalkMessage> messages) {
		
		lists.add(messages);
		
		return lists.size() - 1;
	}
	
	public static ArrayList<LatalkMessage> getLatalks(int key) {
		return lists.get(key);
	}
	
	public static int cachePuzzleRace(LatalkMessage message) {
		
		puzzle_race_list.add(message);
		int key = puzzle_race_list.size() - 1;
		race_id_map.put(message.getMessageId(), key);
		return key;
	}
	
	
	
	public static LatalkMessage getPuzzleRaceById(int id) {
		
		return puzzle_race_list.get(race_id_map.get(id, -1));
	}
	
	public static List<LatalkMessage> getPuzzleRaces(int num, boolean nearby){
		
		if(num == ALL) return puzzle_race_list;
		
		while(!DataPassCache.gotRaces()){ 
			if(MessageGetFactory.extendRadius()) {
				if(nearby) retrieveMessage(MessageGetFactory.TC_NEAR_BY);
				else retrieveMessage(MessageGetFactory.TC_AWAY);
			} else {
				
				MessageGetFactory.resetRadius();
				return null;
			}
		}	
		
		if(num == UNREAD_ALL) {
			int start = race_read;
			race_read = puzzle_race_list.size();
			return puzzle_race_list.subList(start, puzzle_race_list.size());
		}
		
		
		
		if(num + race_read <= puzzle_race_list.size()) {
			race_read += num;
			return puzzle_race_list.subList(race_read - num, race_read);
		} else {
			int start = race_read;
			race_read = puzzle_race_list.size();
			return puzzle_race_list.subList(start, race_read);
		}
	}
	
	public static void retrieveMessage(int type){
		
		MessageGetFactory mgf = new MessageGetFactory();
		switch(type) {
			case MessageGetFactory.PR_NEAR_BY: 
				mgf.getPuzzleMessagesNearby(null);
				break;
			case MessageGetFactory.TC_NEAR_BY:
				break;
			case MessageGetFactory.PR_AWAY:
				mgf.getPuzzleMessagesNearby(null);
				break;
			case MessageGetFactory.TC_AWAY:
				break;
			default: break;
		}
	}
	
	public static boolean gotRaces() {
		return puzzle_race_list.size() > 0 && race_read < puzzle_race_list.size();
	}
}
