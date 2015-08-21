package com.xhanshawn.latalk;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AboutActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		
		showActionBar();
		TextView about = (TextView) findViewById(R.id.about_tv);
		
		String about_content = "This application is developed by xhanshawn. \n"
				+ "Email: xhan@wpi.edu";
		about.setText(about_content);
		
	}
	
	
	private void showActionBar(){

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
				AboutActivity.this.finish();
			}
		});
	    
	    TextView banner_tv = (TextView) findViewById(R.id.actionbar_color_banner);
	    banner_tv.setText("About");
	}
}
