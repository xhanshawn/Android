package com.xhanshawn.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.xhanshawn.data.UserAccount;

public class UsersController {
	
	public static final int LOGIN_ACTION = 0;
	public static final int LOGOUT_ACTION = 1;

	public static String createUser(UserAccount user){
		
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(ServerAccessFactory.createUser());
		post.addHeader("Content-Type", "application/json");
		
		JSONObject user_json = new JSONObject();
		String status = null;
		String notice = null;
		try {
			
			
			
			JSONObject user_obj = new JSONObject();
			user_obj.put("name", user.getUser_name());
			user_obj.put("password", user.getPassword());
			user_obj.put("password_confirmatioin", user.getConfirm_password());
			
			
			user_json.put("user",user_obj);
			user_json.put("commit", "Create User");
			StringEntity entity = new StringEntity(user_json.toString(),"UTF-8");
			post.setEntity(entity);
			
			HttpResponse response = client.execute(post);
			
			String entity_str = EntityUtils.toString(response.getEntity());
			JSONObject status_json = new JSONObject(entity_str);
			status = status_json.getString("status");
			if(status.equals("error")) notice = status_json.getString("notice");
			

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
		
		return notice;
	}
	
	public static String login(UserAccount user){
		
		return controlUserSession(user, LOGIN_ACTION);
	}
	
	public static String logout(UserAccount user){
		
		return controlUserSession(user, LOGOUT_ACTION);
	}
	
	private static String controlUserSession(UserAccount user, int action){
		
		
		HttpClient client = new DefaultHttpClient();
		
		String uri = null;
		
		if(action == LOGIN_ACTION) uri = ServerAccessFactory.login();
		else if(action == LOGOUT_ACTION) uri = ServerAccessFactory.logout();
		
		HttpPost post = new HttpPost(uri);
		
		post.addHeader("Content-Type", "application/json");
		
		JSONObject user_json = new JSONObject();
		JSONObject user_obj = new JSONObject();
		String status = null;
		String notice = null;
		
		try {
			user_obj.put("name", user.getUser_name());
			if(user.getPassword()!=null) user_obj.put("password", user.getPassword());

			user_json.put("user", user_obj);

			JSONObject session = new JSONObject();
			session.put("user_name", user.getUser_name());
			user_json.put("session", session);
			
			if(action == LOGIN_ACTION) user_json.put("commit", "Create Session");
			else if(action == LOGOUT_ACTION) user_json.put("commit", "Update Session");
			
			StringEntity entity = new StringEntity(user_json.toString(),"UTF-8");
			post.setEntity(entity);
			
			HttpResponse response = client.execute(post);
			
			String entity_str = EntityUtils.toString(response.getEntity());
			JSONObject response_entity = new JSONObject(entity_str);
			status = response_entity.getString("status");
			if(status.equals("error")) notice = response_entity.getString("notice");
			else notice = status;
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
		
		return notice;
		
	}
	
}
