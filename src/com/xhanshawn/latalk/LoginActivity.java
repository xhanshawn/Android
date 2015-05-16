package com.xhanshawn.latalk;

import com.xhanshawn.data.UserAccount;
import com.xhanshawn.util.UserSessionManager;
import com.xhanshawn.util.UsersController;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
	
	private boolean confirm_added = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		login_message = (TextView) findViewById(R.id.login_message_textview);
		
		Button login_button = (Button) findViewById(R.id.login_button);
		
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
		
		username_input = (EditText) findViewById(R.id.user_name_edittext);
		password_input = (EditText) findViewById(R.id.passward_edittext);
		
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
		
		
		Button signup_button = (Button) findViewById(R.id.signup_button);
		
		
		
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
	
	public class UserCreater extends AsyncTask<UserAccount, Void, String> {
		
		
		
		@Override
		protected String doInBackground(UserAccount... params) {
			// TODO Auto-generated method stub
			
			String notice = UsersController.createUser(params[0]);
			return notice;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			login_message.setText(result);
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
