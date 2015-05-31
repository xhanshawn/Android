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

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.xhanshawn.data.LatalkMessage;

public class MessageGetFactory {
	
	private static String URIBase = "http://10.0.2.2:3000";
	
	public static ArrayList<LatalkMessage> getPuzzleMessages(){
		
		return getMessages("Puzzle");
	}
	
	public static ArrayList<LatalkMessage> getTimeCapsuleMessages(){
		
		return getMessages("TimeCapsule");
	}
	
	private static ArrayList<LatalkMessage> getMessages(String message_type){
		HttpClient client = new DefaultHttpClient();
		HttpGet get_request = new HttpGet(URIBase + "/messages.json" + "?message_type=" + message_type);
		get_request.setHeader("Content-Type", "application/json");
		JSONObject message_json = null;
		
		ArrayList<LatalkMessage> message_list = new ArrayList<LatalkMessage>();
		
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
					String url = getImageUrl(id);
					Bitmap attached_pic = null;
					if(url != null) attached_pic = getImage(url);
					LatalkMessage new_message = LatalkMessage.parseJSON(message_json);
					new_message.setAttachedPic(attached_pic);
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
