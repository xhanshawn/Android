package com.xhanshawn.data;

public class LatalkMessage {
	
	private String message_type;
	private String content;
	private long hold_time;
	private float longitude;
	private float latitude;
	private String user_name;
	
	public LatalkMessage(){
		
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
		this.longitude = longitude;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	
	
}
