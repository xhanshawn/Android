package com.xhanshawn.latalk;

import com.xhanshawn.data.UserAccount;
import com.xhanshawn.view.MyListView;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AccountSettingsActivity extends Activity {
	MyListView account_mlv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_settings);
		account_mlv = (MyListView) findViewById(R.id.account_mlv);
		
		account_mlv.setAdapter(new BaseAdapter(){
			final static int PHOTO = 0;
			final static int MESSAGE_ACCESS = 1;
			@Override
			public int getCount() {
				return 2;
			}

			@Override
			public Object getItem(int position) {return null;}

			@Override
			public long getItemId(int position) {return 0;}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if(convertView == null){
					LayoutInflater inflater = AccountSettingsActivity.this.getLayoutInflater();
					switch(position){
						case PHOTO: 
							convertView = inflater.inflate(R.layout.user_photo, null, false);
							ImageView photo_iv = (ImageView) convertView.findViewById(R.id.account_photo_iv);
							TextView name_tv = (TextView) convertView.findViewById(R.id.account_name_tv);
							name_tv.setText(UserAccount.getCurrentUserName());
							break;
						case MESSAGE_ACCESS:
							convertView = inflater.inflate(R.layout.user_photo, null, false);
						default: break;
					}
				}
				return convertView;
			}
			
		});
		 
	}
}
