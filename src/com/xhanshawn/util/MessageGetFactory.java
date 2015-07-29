package com.xhanshawn.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.xhanshawn.data.LatalkMessage;
import com.xhanshawn.latalk.CameraActivity;
import com.xhanshawn.latalk.TimeCapsuleCreateActivity;
import com.xhanshawn.latalk.TimeCapsuleActivity;

public class MessageGetFactory {
	//default avd
//	private static String URIBase = "http://10.0.2.2:3000";

	//genymotion
	
	final public static int[] LEVEL = new int[] {60, 120, 180, 240, 300, 360};
	
	final public static int TC_NEAR_BY = 1000;
	final public static int PR_NEAR_BY = 1001;
	final public static int TC_AWAY = 1002;
	final public static int PR_AWAY = 1003;

	final public static int GET_MESSAGE= -1;
	
	
	
	private static String URIBase = "http://10.0.3.2:3000";
	ArrayList<LatalkMessage> messages = null;
	private static int radius_level = 0;

	
	public static boolean extendRadius(){
		if(radius_level < LEVEL.length - 1) {
			radius_level++;
			return true;
		} else return false;
	}
	
	public static void resetRadius(){
		radius_level = 0;
	}
	
	
	public ArrayList<LatalkMessage> getPuzzleMessagesNearby(Location current_location){
		
		float offset = 0.0002f * LEVEL[radius_level];
		
		while(messages == null || messages.isEmpty()){
			
			float longitude = 181.000000f;
			float latitude = 91.000000f;
			
			String url = ServerAccessFactory.queryMessage() + "&message_type=Puzzle";

			//if current location is available we need to add search offset
			if(current_location != null) {
				
				longitude = (float) current_location.getLongitude();
				latitude = (float) current_location.getLatitude();
				url += "&offset=" + String.format("%.06f", offset);
			}
			
			url += "&longitude=" + String.format("%.06f", longitude)
					+ "&latitude=" + String.format("%.06f", latitude);
			
			messages = getMessages(url);
			
			if(offset < 0.00008) offset += 0.00002f;
			else if(offset < 0.00048) offset += 0.0001f;
			else if(offset < 0.00148) offset += 0.001f;
			else if(offset < 0.01148) offset += 0.01f;
			else break;
		}
			
		return messages;
	}
	
	
	public static ArrayList<LatalkMessage> getPuzzleMessages(){
		
		String url = ServerAccessFactory.queryMessage() + "&message_type=Puzzle";
		url += "&longitude=" + String.format("%.06f", 181.0f)
				+ "&latitude=" + String.format("%.06f", 91.0f);
		return getMessages(url);
	}
	
	public static ArrayList<LatalkMessage> getTimeCapsuleMessagesNearby(Location current_location){
	
		
		ArrayList<LatalkMessage> messages = null;
		float offset = 0.0002f * LEVEL[radius_level];
		
		while(messages == null || messages.isEmpty()){

			float longitude = 181.000000f;
			float latitude = 91.000000f;
			
			String url = ServerAccessFactory.queryMessage() + "&message_type=TimeCapsule";
			
			if(current_location != null) {
				
				longitude = (float) current_location.getLongitude();
				latitude = (float) current_location.getLatitude();
				url += "&offset=" + String.format("%.06f", offset);
			}
			
			url += "&longitude=" + String.format("%.06f", longitude)
					+ "&latitude=" + String.format("%.06f", latitude);
			
			getMessages(url);

			if(offset < 0.00008) offset += 0.00002f;
			else if(offset < 0.00048) offset += 0.0001f;
			else if(offset < 0.00148) offset += 0.001f;
			else if(offset < 0.01148) offset += 0.01f;
			else break;
		}
		
		return messages;
	}
	
	
	public static ArrayList<LatalkMessage> getTimeCapsuleMessages(){
		
		String url = ServerAccessFactory.queryMessage() + "&message_type=TimeCapsule";
		
		url += "&longitude=" + String.format("%.06f", 181.0f)
				+ "&latitude=" + String.format("%.06f", 91.0f);
		return getMessages(url);
	}
	
	
	
	
	private static ArrayList<LatalkMessage> getMessages(String url){
		HttpClient client = new DefaultHttpClient();
		
		JSONObject message_json = null;
		
		ArrayList<LatalkMessage> message_list = new ArrayList<LatalkMessage>();
		
		HttpGet get_request = new HttpGet(url);
		get_request.setHeader("Content-Type", "application/json");
		try {
			
			
			HttpResponse response = client.execute(get_request);
			int status = response.getStatusLine().getStatusCode();
			
			if(status >= 200 && status <= 400){
				HttpEntity entity = response.getEntity();
				String data = EntityUtils.toString(entity);
				JSONArray messages = new JSONArray(data);
				
				for(int i=0; i<messages.length(); i++){
					message_json = messages.getJSONObject(i);
					int id = message_json.getInt("id");
					String img_url = message_json.getString("image_url");
					String thumb_url = message_json.getString("thumb_url");
					String small_thumb_url = message_json.getString("small_thumb_url");
					
					LatalkMessage new_message = LatalkMessage.parseJSON(message_json);
					new_message.setPicUrl(img_url);
					new_message.setThumbUrl(thumb_url);
					new_message.setSmallThumbUrl(small_thumb_url);
					new_message.setMessageId(id);
					String type = message_json.getString("message_type");
					if(type.equals("TimeCapsule")) DataPassCache.cacheTimeCapsule(new_message);
					else DataPassCache.cachePuzzleRace(new_message);
					message_list.add(new_message);
				}
				
				
			}	
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return message_list;	
	}
	
	
//	public static String getImageUrl(int id){
//		
//		HttpClient client = new DefaultHttpClient();
//		HttpGet get = new HttpGet(URIBase + "/messages/" + id + ".json");
//		
//		get.setHeader("Content-Type", "application/json");
//		JSONObject message_json = null;
//		
//		String img_url = null;
//		try {
//			
//			
//			HttpResponse response = client.execute(get);
//			int status = response.getStatusLine().getStatusCode();
//			
//			if(status >= 200 && status <= 400){
//				HttpEntity entity = response.getEntity();
//				String data = EntityUtils.toString(entity);
//				message_json = new JSONObject(data);
//				
//				img_url = message_json.getString("image_url");
//				
//			}	
//			
//		} catch (ClientProtocolException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 
//		
//		return img_url;
//	}

	public static Bitmap getImage(String image_url){
		
		if(image_url == null || image_url == "" || image_url.equals("null")) return null;
		
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(URIBase+ "/" + image_url);
		
		Bitmap output_bmp = null;
		
		try {
			
			
			HttpResponse response = client.execute(get);
			int status = response.getStatusLine().getStatusCode();
			
			if(status >= 200 && status <= 400){
				
				HttpEntity entity = response.getEntity();
				InputStream img_stream = entity.getContent();
				output_bmp = BitmapFactory.decodeStream(img_stream);
			} 
				
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("get_pic_err", e.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return output_bmp;
	}
	
	
}
