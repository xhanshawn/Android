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
import android.widget.Toast;

import com.xhanshawn.data.LatalkMessage;
import com.xhanshawn.latalk.CameraActivity;
import com.xhanshawn.latalk.MessageCreateActivity;
import com.xhanshawn.latalk.TimeCapsuleActivity;

public class MessageGetFactory {
	
	private static String URIBase = "http://10.0.2.2:3000";
	
	public static ArrayList<LatalkMessage> getPuzzleMessagesNearby(Context context){
		
		return getMessages("");
	}
	
	public static ArrayList<LatalkMessage> getPuzzleMessages(){
		
		String url = URIBase + "/messages.json" + "?message_type=Puzzle";

		return getMessages(url);
	}
	
	public static ArrayList<LatalkMessage> getTimeCapsuleMessagesNearby(Location current_location){
	
		
		ArrayList<LatalkMessage> messages = null;
		float offset = 0.000002f;
		
		while(messages == null){
			
				
				
			float longitude = 181.000000f;
			float latitude = 91.000000f;
			
			String url = URIBase + "/messages.json" + "?message_type=TimeCapsule";
			
			if(current_location != null) {
				
				longitude = (float) current_location.getLongitude();
				latitude = (float) current_location.getLatitude();
				
				url += "&offset=" + offset;
			}
			
			url += "&longitude=" + String.format("%.06f", longitude)
					+ "&latitude=" + String.format("%.06f", latitude);
			
			
			messages = getMessages(url);

			if(offset < 0.000008) offset += 0.000002f;
			if(offset >= 0.000008) offset += 0.00001f;
			if(offset >= 0.000048) offset += 0.0001f;
			if(offset >= 0.000148) offset += 0.001f;
			if(offset >= 0.001148) break;
		}
		
		return messages;
	}
	
	public static ArrayList<LatalkMessage> getTimeCapsuleMessages(){
		
		String url = URIBase + "/messages.json" + "?message_type=TimeCapsule";
		
		//
		
		//add user
		//
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
					String img_url = getImageUrl(id);
//					Bitmap attached_pic = null;
//					if(img_url != null && img_url != "") attached_pic = getImage(img_url);
					LatalkMessage new_message = LatalkMessage.parseJSON(message_json);
					new_message.setPicUrl(img_url);
//					new_message.setAttachedPic(attached_pic);
					
					DataPassCache.cacheTimeCapsule(new_message);
					
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
	
	
	public static String getImageUrl(int id){
		
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(URIBase + "/messages/" + id + ".json");
		
		get.setHeader("Content-Type", "application/json");
		JSONObject message_json = null;
		
		String img_url = null;
		try {
			
			
			HttpResponse response = client.execute(get);
			int status = response.getStatusLine().getStatusCode();
			
			if(status >= 200 && status <= 400){
				HttpEntity entity = response.getEntity();
				String data = EntityUtils.toString(entity);
				message_json = new JSONObject(data);
				
				img_url = message_json.getString("image_url");
				
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
		
		return img_url;
	}
	
	public static Bitmap getImage(String image_url){
		
		if(image_url == null) return null;
		
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return output_bmp;
	}
	
}
