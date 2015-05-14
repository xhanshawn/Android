package com.xhanshawn.latalk;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
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
		
		username_input = (EditText) findViewById(R.id.user_name_edittext);
		password_input = (EditText) findViewById(R.id.passward_edittext);
		
		login_button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				login_message.setText("user name:" + username_input.getText().toString());
				login_message.append("password:" + password_input.getText().toString());

				if(confirm_added) {
					linearlayout_login.removeView(confirm_password);
					linearlayout_signup_buttons.removeView(create_account_button);
					confirm_added = false;
				}
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

				login_message.setText("user name:" + username_input.getText().toString());
				
				login_message.append("password:" + password_input.getText().toString());
				login_message.append("confirm password:" + confirm_password.getText().toString());
				if(!password_input.getText().toString().equals(confirm_password.getText().toString())) 
					login_message.append("The password you entered is not same");
			}
		});
	}
}
