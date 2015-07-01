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
import android.widget.ImageButton;
import android.widget.TextView;


public class TimeCapsuleMapActivity extends Activity {
	ActionBar mActionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_time_capsule_map);
		
		customActionBar();
	}
	
private void customActionBar(){
		
		
		mActionBar = getActionBar();
		mActionBar.show();
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.actionbar_color_with_img,null);
		
		mActionBar.setDisplayShowCustomEnabled(true);

		mActionBar.setCustomView(v);
	    mActionBar.setBackgroundDrawable(getResources().getDrawable(R.color.purple));

		Button back_to_main_b = (Button) v.findViewById(R.id.c_p_r_to_main_b);
	    back_to_main_b.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TimeCapsuleMapActivity.this.finish();
			}
		});
	    
	    ImageButton tc_map_switch_b = (ImageButton) v.findViewById(R.id.color_ab_ib);
	    tc_map_switch_b.setImageResource(R.drawable.map_switch_icon);
	    tc_map_switch_b.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				TimeCapsuleMapActivity.this.finish();
			}
		});
	    
	    TextView banner_tv = (TextView) findViewById(R.id.actionbar_color_banner);
	    banner_tv.setText("Time Capsule");
	    
	}
}
