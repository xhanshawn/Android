package com.xhanshawn.latalk;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.xhanshawn.data.UserAccount;
import com.xhanshawn.util.AlertMessageFactory;
import com.xhanshawn.util.SNSLoginManager;
import com.xhanshawn.util.UserSessionManager;
import com.xhanshawn.util.UsersController;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LoginActivity extends Activity {
	
	TextView login_message;
	LinearLayout linearlayout_login;
	LinearLayout linearlayout_signup_buttons;
	EditText confirm_password;
	EditText username_input;
	EditText password_input;
	Button create_account_button;
	CallbackManager callbackManager;
	
	
	private boolean confirm_added = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		customActionBar();
		
		login_message = (TextView) findViewById(R.id.login_message_textview);
		Button login_button = (Button) findViewById(R.id.login_button);
		Button signup_button = (Button) findViewById(R.id.signup_button);
		username_input = (EditText) findViewById(R.id.user_name_edittext);
		password_input = (EditText) findViewById(R.id.passward_edittext);
		
		
		UserSessionManager manager = new UserSessionManager(getApplicationContext());
		
		if(manager.isUserLoggedIn()){
			login_message.setText(AlertMessageFactory.alreadyLogin());
			login_button.setVisibility(View.INVISIBLE);
			signup_button.setVisibility(View.INVISIBLE);
			username_input.setVisibility(View.INVISIBLE);
			password_input.setVisibility(View.INVISIBLE);
			
		} else {
		
			
			linearlayout_login = (LinearLayout) findViewById(R.id.linearlayout_login);
			linearlayout_signup_buttons = (LinearLayout) findViewById(R.id.linearlayout_signup_buttons);
			
			confirm_password = new EditText(this);
			confirm_password.setHint("confirm your password");
			confirm_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
			
			create_account_button = new Button(this);
			create_account_button.setText("create");
			create_account_button.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(!password_input.getText().toString().equals(confirm_password.getText().toString())) 
						login_message.append("The password you entered is not same");
					else{
						UserAccount user = new UserAccount();
						user.setUser_name(username_input.getText().toString());
						user.setPassword(password_input.getText().toString());
						user.setConfirm_password(confirm_password.getText().toString());
						new UserCreater().execute(user);
					}
				}
			});
			
			login_button.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(confirm_added) {
						linearlayout_login.removeView(confirm_password);
						linearlayout_signup_buttons.removeView(create_account_button);
						confirm_added = false;
					}
					
					UserAccount user = new UserAccount();
					user.setUser_name(username_input.getText().toString());
					user.setPassword(password_input.getText().toString());
					
					new UserLoginAsync().execute(user);
					
				}
			});
			
			
			
			
			
			signup_button.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(!confirm_added) {
						linearlayout_login.addView(confirm_password);
						linearlayout_signup_buttons.addView(create_account_button);
						confirm_added = true;
					}
					
				}
			});
		}
		
		
		
		LoginButton fb_login_b = (LoginButton) findViewById(R.id.fb_login_ib);
		
		fb_login_b.setReadPermissions(SNSLoginManager.getFacebookReadPermissions());
//		Log.v("token",AccessToken.getCurrentAccessToken().toString());
		callbackManager = CallbackManager.Factory.create();
		fb_login_b.registerCallback(callbackManager, new FacebookCallback<LoginResult>(){

			@Override
			public void onSuccess(LoginResult result) {
				// TODO Auto-generated method stub
				UserSessionManager manager = new UserSessionManager(getApplicationContext());
				if(manager.isUserLoggedIn()){
					manager.loginSNS(UserSessionManager.FACEBOOK, result.getAccessToken().getUserId());
				}else{
					UserAccount user = UserAccount.createSNSUser(UserSessionManager.FACEBOOK, result.getAccessToken().getUserId());
					new UserCreater().execute(user);
					manager.loginSNS(UserSessionManager.FACEBOOK, result.getAccessToken().getUserId());
				}
			}

			@Override
			public void onCancel() {
				// TODO Auto-generated method stub
			}

			@Override
			public void onError(FacebookException error) {
				// TODO Auto-generated method stub
				error.printStackTrace();
				Log.e("FacebookException", error.getMessage());
			}
		});
		try {
	        PackageInfo info = getPackageManager().getPackageInfo(
	                "com.facebook.samples.hellofacebook", 
	                PackageManager.GET_SIGNATURES);
	        for (Signature signature : info.signatures) {
	            MessageDigest md = MessageDigest.getInstance("SHA");
	            md.update(signature.toByteArray());
	            Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
	            }
	    } catch (NameNotFoundException e) {

	    } catch (NoSuchAlgorithmException e) {

	    }
		
//		Intent import_activity = new Intent("com.xhanshawn.latalk.IMPORTSNSACTIVITY");
//		startActivity(import_activity);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    callbackManager.onActivityResult(requestCode, resultCode, data);
	}

	
	private void customActionBar(){
		
		ActionBar actionBar = getActionBar();
		actionBar.show();
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.actionbar_color_only_back_b, null);
		
	    actionBar.setDisplayShowCustomEnabled(true);

	    actionBar.setCustomView(v);
	    actionBar.setBackgroundDrawable(getResources().getDrawable(R.color.deep_pink));
	    
	    Button back_to_main_b = (Button) v.findViewById(R.id.color_ab_back_b);
	    back_to_main_b.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LoginActivity.this.finish();
			}
		});
	    
	    TextView banner_tv = (TextView) findViewById(R.id.actionbar_color_banner);
	    banner_tv.setText("Latalk");
	}
	
	public class UserCreater extends AsyncTask<UserAccount, Void, String> {
		
		
		UserAccount user;
		@Override
		protected String doInBackground(UserAccount... params) {
			// TODO Auto-generated method stub
			
			String notice = UsersController.createUser(params[0]);
			user = params[0];
			return notice;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			login_message.setText(result);
			new UserLoginAsync().execute(user);
		}
		
	}
	
	public class UserLoginAsync extends AsyncTask<UserAccount, Void, String> {
		
		private UserAccount user;
		
		@Override
		protected String doInBackground(UserAccount... params) {
			// TODO Auto-generated method stub
			
			user = params[0];
			String notice = UsersController.login(user);
			return notice;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(result.equals("OK")){
				
				login_message.setText("Login Successfully");
				UserSessionManager manager = new UserSessionManager(getApplicationContext());
				manager.createUserLoginSession(user.getUser_name(), "email");
				LoginActivity.this.finish();

			}else{
				login_message.setText(result);
			}
		}
		
	}
}
