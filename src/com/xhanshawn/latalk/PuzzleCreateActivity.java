package com.xhanshawn.latalk;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.xhanshawn.util.AlertMessageFactory;
import com.xhanshawn.view.MyListView;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class PuzzleCreateActivity extends Activity {
	
	private static final int SELECT_PHOTO = 100;

	private ActionBar mActionBar;
	private MyListView puzzle_create_mlv;
	ImageView iv1;
	ImageView iv2;
	private ArrayList<Bitmap> bmps = new ArrayList<Bitmap>();
	

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_puzzle_create);
		
		customActionBar();
		
		puzzle_create_mlv = (MyListView) findViewById(R.id.puzzle_create_mlv);
		
		Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
		photoPickerIntent.setType("image/*");
		
		if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
			Toast lower_version_toast = Toast.makeText(PuzzleCreateActivity.this,
					AlertMessageFactory.chooseImgOneByOne(),
					Toast.LENGTH_LONG);

			lower_version_toast.show();
		}
		
		photoPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
		startActivityForResult(photoPickerIntent, SELECT_PHOTO); 
		
//		Intent custom_gallery_activity = new Intent("com.xhanshawn.latalk.CUSTOMGALLERYACTIVITY");
//		startActivity(custom_gallery_activity);
		

	}
	
	
	
	@SuppressLint("NewApi")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) { 
	    super.onActivityResult(requestCode, resultCode, data); 

	    switch(requestCode) { 
	    
	    case SELECT_PHOTO:
	        if(resultCode == RESULT_OK){
	        	if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2 || data.getData() != null) {
	        		
	        		
	        		Uri data_uri = data.getData();
	        		InputStream is = null;
	        		try {
	        			is = getContentResolver().openInputStream(data_uri);
	        		} catch (FileNotFoundException e) {
	        			// TODO Auto-generated catch block
	        			e.printStackTrace();
	        		}
	        		Bitmap selected_img = BitmapFactory.decodeStream(is);
	        		bmps.add(selected_img);
	        	}else {
	        		
	        		ClipData clipdata = data.getClipData();
	                for (int i=0; i< clipdata.getItemCount(); i++) {
	                	
	                    try {
	                    	
	                        Bitmap selected_img = MediaStore.Images.Media.getBitmap(this.getContentResolver(), clipdata.getItemAt(i).getUri());
	    	        		bmps.add(selected_img);
	                    } catch (IOException e) {
	                        // TODO Auto-generated catch block
	                        e.printStackTrace();
	                    }
	                }
	                
	        	}
	        }
	    }
	}
	
	private void customActionBar(){
		
		
		mActionBar = getActionBar();
		mActionBar.show();
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.actionbar_puzzle_race_create,null);
		
		mActionBar.setDisplayShowCustomEnabled(true);

		mActionBar.setCustomView(v);
	    
	    Button back_to_main_b = (Button) v.findViewById(R.id.c_p_r_to_main_b);
	    back_to_main_b.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PuzzleCreateActivity.this.finish();
			}
		});
	    
	    Button done_puzzles_b = (Button) v.findViewById(R.id.puzzle_create_ok_b);
	    done_puzzles_b.setText("Done");
	    done_puzzles_b.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PuzzleCreateActivity.this.finish();
			}
		});
	}
}
