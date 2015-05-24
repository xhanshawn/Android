package com.xhanshawn.data;

public class UserAccount {
	private String user_name;
	private String password;
	private String confirm_password;
	private static String current_user_name;
	
	public UserAccount(){
		
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirm_password() {
		return confirm_password;
	}

	public void setConfirm_password(String confirm_password) {
		this.confirm_password = confirm_password;
	}
	
	public static void updateCurrentUserName(String _current_user_name){
		
		current_user_name = _current_user_name;
	}
	
	public static String getCurrentUserName(){
		return current_user_name;
	}
}
