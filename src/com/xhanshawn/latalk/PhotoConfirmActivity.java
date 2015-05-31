package com.xhanshawn.latalk;

import com.xhanshawn.util.DataPassCache;
import com.xhanshawn.util.IntegerIdentifiers;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class PhotoConfirmActivity extends Activity {
	
	Bitmap taken_pic_bmp;
	int pic_key;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_confirm);
		
		getActionBar().hide();
		
		Bundle extras = getIntent().getExtras();
		pic_key = extras.getInt("pic_key");
		byte[] byteArray = DataPassCache.getPicByKey(pic_key);

		taken_pic_bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

		ImageView taken_pic_iv = (ImageView) findViewById(R.id.taken_pic_iv);
		taken_pic_iv.setImageBitmap(taken_pic_bmp);
		
		Button comfirm_pic_b = (Button) findViewById(R.id.confirm_pic_b);
		comfirm_pic_b.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent resultIntent = new Intent();
				resultIntent.putExtra("pic_key", pic_key);
				setResult(IntegerIdentifiers.ATTACHED_PIC_IDENTIFIER, resultIntent);
				PhotoConfirmActivity.this.finish();
			}
		});
		
		Button retake_pic_b = (Button) findViewById(R.id.retake_pic_b);
		retake_pic_b.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				PhotoConfirmActivity.this.finish();
//				Intent camera_activity = new Intent("com.xhanshawn.latalk.CAMERAACTIVITY");
//				startActivity(camera_activity);
				
			}
		});

		
	}
}
