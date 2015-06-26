package com.xhanshawn.latalk;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.xhanshawn.data.LatalkMessage;
import com.xhanshawn.util.AlertMessageFactory;
import com.xhanshawn.util.DataPassCache;
import com.xhanshawn.util.IntegerIdentifiers;
import com.xhanshawn.view.MyListView;
import com.xhanshawn.view.PicPuzzleAdapter;

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
	

	private ActionBar mActionBar;
	private MyListView puzzle_create_mlv;
	ImageView iv1;
	ImageView iv2;
	private ArrayList<LatalkMessage> puzzles = new ArrayList<LatalkMessage>();
	

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_puzzle_create);
		
		customActionBar();
		
		puzzle_create_mlv = (MyListView) findViewById(R.id.puzzle_create_mlv);

		int img_src = getIntent().getExtras().getInt("Img_src");
		
		switch(img_src) {
			case IntegerIdentifiers.ATTACH_IMG_FROM_GAL :
				
				Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
				photoPickerIntent.setType("image/*");
				
				if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
					Toast lower_version_toast = Toast.makeText(PuzzleCreateActivity.this,
							AlertMessageFactory.chooseImgOneByOne(),
							Toast.LENGTH_LONG);

					lower_version_toast.show();
				}
				
				photoPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
				startActivityForResult(photoPickerIntent, IntegerIdentifiers.SELECT_PHOTO); 
				
				break;
			case IntegerIdentifiers.ATTACH_IMG_FROM_CAM:
				
				Intent puzzle_create_activity = new Intent("com.xhanshawn.latalk.CAMERAACTIVITY");
				startActivityForResult(puzzle_create_activity, IntegerIdentifiers.ATTACH_PIC_IDENTIFIER); 
				break;
				
			default: break;
				
		}
		
		
		
		
	}
	
	
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		PicPuzzleAdapter pp_adapter = new PicPuzzleAdapter(PuzzleCreateActivity.this, puzzles);
		
		puzzle_create_mlv.setAdapter(pp_adapter);
		
	}




	@SuppressLint("NewApi")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) { 
	    super.onActivityResult(requestCode, resultCode, data); 
	    
	    if(data == null) PuzzleCreateActivity.this.finish();

	    switch(requestCode) { 
	    
	    case IntegerIdentifiers.SELECT_PHOTO:
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
	        		LatalkMessage message = new LatalkMessage();
	        		message.setAttachedPic(selected_img);
	        		puzzles.add(message);
	        	}else {
	        		
	        		ClipData clipdata = data.getClipData();
	                for (int i=0; i< clipdata.getItemCount(); i++) {
	                	
	                    try {
	                    	
	                        Bitmap selected_img = MediaStore.Images.Media.getBitmap(this.getContentResolver(), clipdata.getItemAt(i).getUri());
	                        LatalkMessage message = new LatalkMessage();
	    	        		message.setAttachedPic(selected_img);
	    	        		puzzles.add(message);
	                    } catch (IOException e) {
	                        // TODO Auto-generated catch block
	                        e.printStackTrace();
	                    }
	                }
	                
	        	}
	        }
	        break;
	    case IntegerIdentifiers.ATTACH_PIC_IDENTIFIER:
	    	if(data == null) break;
	    	int key = data.getExtras().getInt("pic_key");
	    	byte[] img_byte_array = DataPassCache.getPicByKey(key);
	    	Bitmap selected_img = BitmapFactory.decodeByteArray(img_byte_array, 0, img_byte_array.length);
    		LatalkMessage message = new LatalkMessage();
    		message.setAttachedPic(selected_img);
    		puzzles.add(message);
	    	break;
	    	
	    default: break;
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
	    
	    Button done_puzzles_b = (Button) v.findViewById(R.id.puzzle_r_create_ok_b);
	    done_puzzles_b.setText("Done");
	    done_puzzles_b.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int key = DataPassCache.cacheLatalks(puzzles);
				Intent resultIntent = new Intent();
				resultIntent.putExtra("puzzles_key", key);
				setResult(IntegerIdentifiers.PASS_PUZZLES, resultIntent);
				PuzzleCreateActivity.this.finish();
			}
		});
	}
	
	
}
