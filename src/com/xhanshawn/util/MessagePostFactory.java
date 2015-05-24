package com.xhanshawn.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import com.xhanshawn.data.LatalkMessage;

public class MessagePostFactory {
	
	private static String URIBase = "http://10.0.2.2:3000";
	
	public static boolean postLatalkMessage(LatalkMessage message){
		
		
		
		HttpClient client = new DefaultHttpClient(); 
		HttpPost post = new HttpPost(URIBase + "/messages.json");
		post.addHeader("Content-Type", "application/json");
		
		JSONObject json = new JSONObject();
		
		
		try {
			JSONObject message_json = new JSONObject();
			message_json.put("message_type",message.getMessage_type());
			message_json.put("content",message.getContent());
			
			if(message.isLocationSet()){
				message_json.put("longitude",String.format("%.03f", message.getLongitude()));
				message_json.put("latitude",String.format("%.03f", message.getLatitude()));
			}else{
				message_json.put("longitude",String.format("%.03f",181.0f));
				message_json.put("latitude",String.format("%.03f", 91.0f));
			}
			
			message_json.put("hold_time",message.getHold_time());
			message_json.put("user_name", message.getUser_name());
			
			json.put("message", message_json);
			json.put("commit", "Create Message");
			
			StringEntity entity = new StringEntity(json.toString(), "UTF-8");
			post.setEntity(entity);
			HttpResponse response = client.execute(post);
			System.out.println(response.getStatusLine().toString());
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			client.getConnectionManager().shutdown();
		}
			
			
	
		
		
		
		return true;
		
	}
}
