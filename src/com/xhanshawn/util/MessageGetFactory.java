package com.xhanshawn.util;

import java.io.IOException;
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
					message_list.add(LatalkMessage.parseJSON(message_json));
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

}
