package com.xhanshawn.data;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class LatalkMessage {
	
	private String message_type;
	private String content;
	private long hold_time;
	private float longitude;
	private float latitude;
	private String user_name;
	private Bitmap attached_pic;
	private boolean isLocationSet;
	private String file_name;
	public LatalkMessage(){
		
		isLocationSet = false;
	}

	public String getMessage_type() {
		return message_type;
	}

	public void setMessage_type(String message_type) {
		this.message_type = message_type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getHold_time() {
		return hold_time;
	}

	public void setHold_time(long hold_time) {
		this.hold_time = hold_time;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.isLocationSet = true;
		this.longitude = longitude;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.isLocationSet = true;
		this.latitude = latitude;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	
	public void setAttachedPic(Bitmap bmp) {
		
		attached_pic = bmp;
	}
	
	public Bitmap getAttahedPic(){
		
		return attached_pic;
	}
	
	public boolean isLocationSet(){
		
		return isLocationSet;
	}
	public static LatalkMessage parseJSON(JSONObject obj){
		
		if(obj == null) return null;
		LatalkMessage message = new LatalkMessage();
		
		try {
			
			message.setMessage_type(obj.getString("message_type"));
			message.setContent(obj.getString("content"));
			message.setLatitude(Float.valueOf(obj.getString("latitude")));
			message.setLongitude(Float.valueOf(obj.getString("longitude")));
			message.setHold_time(obj.getLong("hold_time"));
			message.setUser_name(obj.getString("user_name"));
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return message;
	}

}
