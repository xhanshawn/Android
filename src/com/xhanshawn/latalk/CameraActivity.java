package com.xhanshawn.latalk;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.xhanshawn.util.AlertMessageFactory;
import com.xhanshawn.util.DataPassCache;
import com.xhanshawn.util.IntegerIdentifiers;
import com.xhanshawn.view.CameraPreview;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class CameraActivity extends Activity {
	
	
	final static int PIC_WIDTH = 640;
	final static int PIC_HEIGHT = 640;

	Camera mCamera;
	CameraPreview mPreview;
	PictureCallback mPicture;
	LinearLayout preview_layout;
	
	private boolean has_camera;
	private int current_id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);
		
		getActionBar().hide();
		
		Button take_pic_b = (Button) findViewById(R.id.take_picture_b);
		take_pic_b.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mCamera.takePicture(null, null, mPicture);
				
			}
		});
		
		Button front_switch_b = (Button) findViewById(R.id.front_cam_b);
		front_switch_b.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				releaseCameraAndPreview();
				
				if(current_id == getBackCameraId()){
					
					current_id = getFrontCameraId();
					safeCameraOpen(getFrontCameraId());
					mPicture = getPictureCallback();
					
				}
				else if(current_id == getFrontCameraId()){
					
					current_id = getBackCameraId();
					safeCameraOpen(getBackCameraId());
					mPicture = getPictureCallback();
				}
				else{
					
					//error
					
				}
				
				
				mPreview.refreshCamera(0, 0);
			}
		});
		
		
		preview_layout = (LinearLayout) findViewById(R.id.camera_preview_ll);

		
		
		has_camera = CameraActivity.this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
		
		if(!has_camera) alertNoCamera();
		
		if(mCamera == null){
			
			current_id = getBackCameraId();
			safeCameraOpen(getBackCameraId());
			mPicture = getPictureCallback();
		}
		
		
		mPreview = new CameraPreview(CameraActivity.this, mCamera);
		
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		
		mPreview.refreshCamera(width, width);
		preview_layout.addView(mPreview);
		preview_layout.setLayoutParams(new LinearLayout.LayoutParams(width, width));
	}
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		
		if(!has_camera) alertNoCamera();
		
		if(mCamera == null){
			
			current_id = getBackCameraId();
			safeCameraOpen(current_id);
			mPicture = getPictureCallback();
		}
		
		if(mPreview == null){
			
			mPreview = new CameraPreview(CameraActivity.this, mCamera);
			preview_layout.addView(mPreview);
		}
		mPreview.refreshCamera(0, 0);
		
	}
	
	private void alertNoCamera(){
		
		Toast no_camera_toast = Toast.makeText(CameraActivity.this,
				AlertMessageFactory.noCameraAlert(),
				Toast.LENGTH_LONG);

		no_camera_toast.show();
		CameraActivity.this.finish();
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		releaseCameraAndPreview();
	}

	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode == IntegerIdentifiers.ATTACH_PIC_IDENTIFIER) {
			Intent resultIntent = new Intent();
			Bundle extras = data.getExtras();
			int key = extras.getInt("pic_key");
			
			resultIntent.putExtra("pic_key", key);
			setResult(IntegerIdentifiers.ATTACH_PIC_IDENTIFIER, resultIntent);
			CameraActivity.this.finish();
		}
	}


	
	
	
	private PictureCallback getPictureCallback(){
		
		PictureCallback picture = new PictureCallback(){

			@Override
			public void onPictureTaken(byte[] data, Camera camera) {
				// TODO Auto-generated method stub
				
//				File picture_file = getOutputMediaFile();
				
//				if(picture_file == null) {
//					return;
//				}
				
				try{
					
//					FileOutputStream file_os = new FileOutputStream(picture_file);
					
					byte[] pic_byte_array = resizePicture(data);
					
//					CameraActivity.this.finish();
					
					Intent intent = new Intent("com.xhanshawn.latalk.PHOTOCONFIRMACTIVITY");
					int key = DataPassCache.cachePic(pic_byte_array);
					intent.putExtra("pic_key", key);
					
					startActivityForResult(intent, IntegerIdentifiers.ATTACH_PIC_IDENTIFIER);

					
//					file_os.write(resizePicture(data));
//					file_os.close();
					
//					Toast pic_saved_toast = Toast.makeText(CameraActivity.this, "Picture Saved", Toast.LENGTH_LONG);
//					pic_saved_toast.show();
					
				} catch (Exception e){
					e.printStackTrace();
				}
				
//				mPreview.refreshCamera(0, 0);
			}
		};

		return picture;
	}
	
	
	private byte[] resizePicture(byte[] input){
		
		Bitmap original_pic = BitmapFactory.decodeByteArray(input, 0, input.length);
		Bitmap resized_pic = Bitmap.createScaledBitmap(original_pic, PIC_WIDTH, PIC_HEIGHT, true);
		ByteArrayOutputStream blob = new ByteArrayOutputStream();
		resized_pic.compress(Bitmap.CompressFormat.JPEG, 100, blob);
	 
	    return blob.toByteArray();
		
	}
	
	private static File getOutputMediaFile(){
		
		File storage_dir = new File("/sdcard/", "Latalk");
		
		if(!storage_dir.exists()){
			
			if(!storage_dir.mkdirs()) return null;
		}
		
		String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		File media_file = new File(storage_dir.getPath() + File.separator + "IMG_" + timestamp + ".jpg");
		
		return media_file;
	}

	
	private int getFrontCameraId(){
		
		int front_id = -1;
		int camera_num = Camera.getNumberOfCameras();
		for(int i=0; i<camera_num; i++){
			
			CameraInfo info = new CameraInfo();
			Camera.getCameraInfo(i, info);
			if(info.facing == CameraInfo.CAMERA_FACING_FRONT){
				front_id = i;
			}
		}
		
		return front_id;
	}
	
	
	private int getBackCameraId(){
		
		int back_id = -1;
		int camera_num = Camera.getNumberOfCameras();
		for(int i=0; i<camera_num; i++){
			
			CameraInfo info = new CameraInfo();
			Camera.getCameraInfo(i, info);
			if(info.facing == CameraInfo.CAMERA_FACING_BACK){
				back_id = i;
			}
		}
		
		return back_id;
	}

	
	private boolean safeCameraOpen(int id){
		
		try{
			releaseCameraAndPreview();
			mCamera = Camera.open(id);
			return (mCamera != null);
		} catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	private void releaseCameraAndPreview(){
		
		if(mPreview != null)mPreview.setCamera(null);
		
		if(mCamera != null) {
			
			mCamera.release();
			mCamera = null;
		}
	}
}
