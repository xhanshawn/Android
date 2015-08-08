package com.xhanshawn.data;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.xhanshawn.util.ServerAccessFactory;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class LatalkMessage {
	
	final public static float NO_LATITUDE = 91.0f;
	final public static float NO_LONGITUDE = 181.0f;
	final public static String TIME_CAPSULE = "TimeCapsule";
	final public static String PUZZLE = "Puzzle";

	private String message_type;
	private String content;
	private long hold_time;
	private float longitude;
	private float latitude;
	private String user_name;
	private Bitmap attached_pic = null;
	private boolean isLocationSet;
	private String pic_url;
	private Long message_id = 0l;
	private int race_num;
	private LatalkMessage start;
	private String thumb_url;
	private String small_thumb_url;
	private Bitmap thumb_pic = null;
	private Bitmap small_thumb_pic = null;
	private long created_at = 0l;
	
	public LatalkMessage(){
		
		isLocationSet = false;
		created_at = new Date().getTime();
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
	
	
	public long getMessageId() {
		return message_id;
	}

	public void setMessageId(long message_id) {
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
	
	public String getThumbRrl() {
		return thumb_url;
	}

	public void setThumbUrl(String thumb_url) {
		this.thumb_url = thumb_url;
	}

	public String getSmallThumbUrl() {
		return small_thumb_url;
	}

	public void setSmallThumbUrl(String small_thumb_url) {
		this.small_thumb_url = small_thumb_url;
	}

	public Bitmap getThumbPic() {
		return thumb_pic;
	}

	public void setThumbPic(Bitmap thumb_pic) {
		this.thumb_pic = thumb_pic;
	}

	public Bitmap getSmallThumbPic() {
		return small_thumb_pic;
	}

	public void setSmallThumbPic(Bitmap small_thumb_pic) {
		this.small_thumb_pic = small_thumb_pic;
	}

	public void setCreatedAt(long server_time) {
		this.created_at = server_time;
	}
	
	public long getCreatedAt(){
		return this.created_at;
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
			message.setMessageId(obj.getLong("id"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return message;
	}

}
