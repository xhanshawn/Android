package com.xhanshawn.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.util.SparseIntArray;
import android.widget.Toast;

import com.xhanshawn.data.LatalkMessage;
import com.xhanshawn.latalk.Latalk;
import com.xhanshawn.latalk.TimeCapsuleActivity;

public class DataPassCache {
	
	final public static int ALL = -1;
	final public static int UNREAD_ALL = -2;
	final public static boolean NEAR_BY = true;
	final public static boolean AWAY = false;
	final public static int TIME_OUT = 10;
	
	private static ArrayList<byte[]> pic_list = new ArrayList<byte[]>();
	private static NotiArrayList<LatalkMessage> time_capsule_list = new NotiArrayList<LatalkMessage>();
	private static int time_capsule_read = Integer.MIN_VALUE;
	private static HashMap<Long, Integer> time_capsule_id_hash = new HashMap<Long, Integer>();
	private static List<ArrayList<LatalkMessage>> lists = new ArrayList<ArrayList<LatalkMessage>>();
	private static NotiArrayList<LatalkMessage> puzzle_race_list = new NotiArrayList<LatalkMessage>();
	private static HashMap<Long, Integer> race_id_map = new HashMap<Long, Integer>();
	private static int race_read = 0;
	private static boolean got_all = false;
	
	public static byte[] getPicByKey(int key) {
		return pic_list.get(key);
	}
	
	public static int cachePic(byte[] byte_array) {
		
		pic_list.add(byte_array);
		int key = pic_list.size() - 1;
		return key;
	}
	
	
	
	public static int cacheLatalks(ArrayList<LatalkMessage> messages) {
		
		lists.add(messages);
		return lists.size() - 1;
	}
	
	public static ArrayList<LatalkMessage> getLatalks(int key) {
		return lists.get(key);
	}
	
	public static void clearLatalks(){
		lists = new ArrayList<ArrayList<LatalkMessage>>();
	}
	
	
	public static int cacheTimeCapsule(LatalkMessage new_time_capsule) {
		if(time_capsule_read == Integer.MIN_VALUE) time_capsule_read = 0;
		
		
		long tc_id = new_time_capsule.getMessageId();
		
		if(time_capsule_id_hash.get(tc_id) != null) return -1;
		else {
			time_capsule_list.add(new_time_capsule);
			time_capsule_id_hash.put(tc_id, time_capsule_list.size() - 1);
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
	
	public static LatalkMessage getTCById(long id){
		
		if(time_capsule_id_hash.containsKey(id)) return time_capsule_list.get(time_capsule_id_hash.get(id));
		else return new LatalkMessage();
	}
	
	public static int getTCSize(){
		return time_capsule_list.size();
	}
	
	public static NotiArrayList<LatalkMessage> getTCCache(){
		return time_capsule_list;
	}
	
	
	
	
	
	public static int cachePuzzleRace(LatalkMessage message) {
		if(message == null) return 0;
		puzzle_race_list.add(message);
		int key = puzzle_race_list.size() - 1;
		race_id_map.put(message.getMessageId(), key);
		return key;
	}
	public static NotiArrayList<LatalkMessage> getRaceCache(){
		return puzzle_race_list;
	}
	
	
	public static LatalkMessage getPuzzleRaceById(int id) {
		
		if(time_capsule_id_hash.containsKey(id)) return puzzle_race_list.get(race_id_map.get(id));
		else return new LatalkMessage();
	}
	
	public static LatalkMessage getPuzzleRace(){
		if(1 + race_read <= puzzle_race_list.size()) {
			race_read ++;
			return puzzle_race_list.get(race_read - 1);
		} else return null;
	}
	public static List<LatalkMessage> getPuzzleRaces(int num){
		
		if(num == ALL) return puzzle_race_list;
		
		if(!hasRaces()) {
			return new ArrayList<LatalkMessage>();
		}
		
		if(num == UNREAD_ALL) {
			int time_out = TIME_OUT;
			while(!got_all && time_out > 0) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) { e.printStackTrace(); }
				time_out--;
			}
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
	
	public static void retrieveMessage(int type) {
		
		new MessageGetter().execute(type);
	}
	public static boolean hasRaces() {
		return puzzle_race_list.size() > 0 && race_read < puzzle_race_list.size();
	}
	
	public static boolean gotAll(){
		return got_all;
	}
	
	
	
	
	
	
	static class MessageGetter extends AsyncTask<Integer, Void, Boolean> {
		int type;
		@Override
		protected Boolean doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			got_all = false;
			type = params[0];
			MessageGetFactory mgf = new MessageGetFactory();
			
			List<LatalkMessage> messages = null;
			Location current_location = LocationInfoFactory.getCurrentLocation();
			switch(params[0]) {
			case MessageGetFactory.PR_NEAR_BY: 
				messages = mgf.getPuzzleMessagesNearby(current_location);
				break;
			case MessageGetFactory.TC_NEAR_BY:
				
				messages = mgf.getTimeCapsuleMessagesNearby(current_location);
				break;
			case MessageGetFactory.PR_AWAY:
				messages = mgf.getPuzzleMessagesNearby(current_location);
				break;
			case MessageGetFactory.TC_AWAY:
				messages = mgf.getTimeCapsuleMessages();
				break;
			default: break;
			}
			return messages != null && !messages.isEmpty();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(result) got_all = true;
			else {
				if(MessageGetFactory.extendRadius()){
					new MessageGetter().execute(type);
				} else {
					if(type == MessageGetFactory.PR_AWAY || type == MessageGetFactory.TC_AWAY) return;
					Toast no_tc_toast = Toast.makeText(Latalk.getAppContext(),
							AlertMessageFactory.noMessagesFound(),
							Toast.LENGTH_SHORT);
					no_tc_toast.show();
					MessageGetFactory.resetRadius();
					if(type == MessageGetFactory.PR_NEAR_BY) new MessageGetter().execute(MessageGetFactory.PR_AWAY);
					else if(type == MessageGetFactory.TC_NEAR_BY) new MessageGetter().execute(MessageGetFactory.TC_AWAY);
				}
			}
		}

	}
}
