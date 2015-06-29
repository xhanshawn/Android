package com.xhanshawn.data;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.xhanshawn.util.ServerAccessFactory;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class LatalkMessage {
	
	private String message_type;
	private String content;
	private long hold_time;
	private float longitude;
	private float latitude;
	private String user_name;
	private Bitmap attached_pic = null;
	private boolean isLocationSet;
	private String pic_url;
	private int message_id;
	private int race_num;
	private LatalkMessage start;
	
	
	public LatalkMessage(){
		
		isLocationSet = false;
	}

	public String getMessageType() {
		return message_type;
	}

	public void setMessageType(String message_type) {
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

	public String getUserName() {
		return user_name;
	}

	public void setUserName(String user_name) {
		this.user_name = user_name;
	}
	
	public void setAttachedPic(Bitmap bmp) {
		
		attached_pic = bmp;
	}
	
	public Bitmap getAttahedPic(){
		
		return attached_pic;
	}
	
	public void setPicUrl(String _url) {
		
		pic_url = _url;
	}
	
	public String getPicUrl(){
		return pic_url;
	}
	
	public String getFullPicUrl(){
		
		return ServerAccessFactory.getUrlBase() + pic_url;
	}
	
	
	public int getMessageId() {
		return message_id;
	}

	public void setMessageId(int message_id) {
		this.message_id = message_id;
	}

	public boolean isLocationSet(){
		
		return isLocationSet;
	}
	
	public boolean hasPic(){
		
		return !(pic_url == null || pic_url == "" || pic_url == "null");
	}
	
	public void setRaceNum(int num) {
		this.race_num = num;
	}
	
	public int getRaceNum() {
		return this.race_num;
	}
	
	public void setStart(LatalkMessage start){
		this.start = start;
	}
	
	public LatalkMessage getStart(){
		return this.start;
	}
	
	public static LatalkMessage parseJSON(JSONObject obj){
		
		if(obj == null) return null;
		LatalkMessage message = new LatalkMessage();
		
		try {
			
			message.setMessageType(obj.getString("message_type"));
			message.setContent(obj.getString("content"));
			message.setLatitude(Float.valueOf(obj.getString("latitude")));
			message.setLongitude(Float.valueOf(obj.getString("longitude")));
			message.setHold_time(obj.getLong("hold_time"));
			message.setUserName(obj.getString("user_name"));
			message.setMessageId(obj.getInt("id"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return message;
	}

}
