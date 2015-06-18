package com.xhanshawn.latalk;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;


//not be used for saving development time
public class CustomGalleryActivity extends Activity {
	private GridView custom_gallery_gv;
	private ActionBar mActionBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_custom_gallery);
		
		customActionBar();
		
		custom_gallery_gv = (GridView) findViewById(R.id.custom_gallery_gv);
		
		final String[] columns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID };
        final String orderBy = MediaStore.Images.Media._ID;
        
        
		
		
	}
	
	private void customActionBar(){
		
		
		mActionBar = getActionBar();
		mActionBar.show();
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.actionbar_custom_gallery,null);
		
		mActionBar.setDisplayShowCustomEnabled(true);

		mActionBar.setCustomView(v);
	    
	    Button custom_gallery_cancel_b = (Button) v.findViewById(R.id.custom_gallery_cancel_b);
	    custom_gallery_cancel_b.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CustomGalleryActivity.this.finish();
			}
		});
	    
	    Button custom_gallery_done_b = (Button) v.findViewById(R.id.custom_gallery_done_b);
	    custom_gallery_done_b.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CustomGalleryActivity.this.finish();
			}
		});
	    
	}
	
}
