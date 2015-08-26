package com.xhanshawn.util;

import com.xhanshawn.data.UserAccount;
import com.xhanshawn.latalk.LoginActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;

public class UserSessionManager {
	
	
	SharedPreferences pref;
	
	Editor editor;
	
	Context _context;
	
	int PRIVATE_MODE = 0;
	
	private static final String PREFER_NAME = "UserSession";
	
	private static final String IS_USER_LOGIN = "IsUserLoggedIn";
	
	public static final String KEY_NAME = "name";
	public static final String KEY_EMAIL = "email";
	public static final String KEY_SNS = "sns";
	public static final String KEY_SNS_ID = "sns_id";

	
	public static final int LATALK = 10000;
	public static final int FACEBOOK = 10001;

	private static int current_SNS = 0;
	
	public UserSessionManager(Context context){
		
		this._context = context;
		pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
		
		editor = pref.edit();
	}
	
	
	public void createUserLoginSession (String name, String email){
		
		editor.putBoolean(IS_USER_LOGIN, true);
		editor.putString(KEY_NAME, name);
		editor.putString(KEY_EMAIL, email);
		editor.commit();
	}
	
	public void loginSNS(int sns_type, String sns_id){
		editor.putInt(KEY_SNS, sns_type);
		if(sns_id == null) editor.putString(KEY_SNS_ID, "");
		else editor.putString(KEY_SNS_ID, sns_id);
		editor.commit();
	}
	
	public boolean isUserLoggedIn(){
		
		//if exist is user login return or return false
		return pref.getBoolean(IS_USER_LOGIN, false);
	}
	
	
	public void logoutUser(){
		
		
		String user_name = pref.getString(KEY_NAME,null);
		UserAccount user = new UserAccount();
		user.setUser_name(user_name);
		new UserLogoutAsync().execute(user);
		
		editor.clear();
		editor.commit();
	}
	
	public String getCurrentUserName(){
		
		return pref.getString(KEY_NAME,null);
	}
	
	public int getUserLoginType(){
		
		return pref.getInt(KEY_SNS, 0);
	}
	
	public class UserLogoutAsync extends AsyncTask<UserAccount, Void, String> {
		
		private UserAccount user;
		
		@Override
		protected String doInBackground(UserAccount... params) {
			// TODO Auto-generated method stub
			
			user = params[0];
			String notice = UsersController.logout(user);
			return notice;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
		}
		
	}
	
}
