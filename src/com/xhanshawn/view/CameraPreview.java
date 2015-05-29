package com.xhanshawn.view;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

	private Camera camera;
	private SurfaceHolder holder;

	public CameraPreview(Context context, Camera _camera) {
		super(context);
		// TODO Auto-generated constructor stub
		
		camera = _camera;
		holder = getHolder();
		holder.addCallback(this);
		
		// for version older than 3.0 
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}
	
	
	public void refreshCamera(int width, int height) {
		
		if(camera != null){
			
			
			if(width !=0 && height != 0) {
				
				Camera.Parameters parameters = camera.getParameters();
			    parameters.setPreviewSize(width, height);
			    requestLayout();
			    camera.setParameters(parameters);
			}
		    camera.startPreview();
		}
		
//		if(holder.getSurface() == null) {
//			return;
//		}
//		
//		_camera.stopPreview();
//		
//		this.setCamera(_camera);

//		camera = _camera;
//		
//		try {
//			camera.setPreviewDisplay(holder);
//			camera.startPreview();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	 
	@Override
	public void surfaceCreated(SurfaceHolder _holder) {
		// TODO Auto-generated method stub
		
		if(camera != null){
			
			try {
				camera.setPreviewDisplay(_holder);
				camera.startPreview();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	public void setCamera(Camera _camera){
		if(camera == _camera) return;
		
		stopPreviewAndFreeCamera();
		
		camera = _camera;
		
		if(camera != null){
			requestLayout();
			
			try{
				
				camera.setPreviewDisplay(holder);
				camera.startPreview();
			}catch(IOException e){
				
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
		if(holder.getSurface() == null) return;
		
		try{
			
			camera.stopPreview();
		} catch(Exception e){
			
		}
		
		try{
			
			camera.setPreviewDisplay(holder);
			camera.startPreview();
		} catch(Exception e){
			
		}
	}
	
	
	

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		if(camera != null){
			
			camera.stopPreview();
		}
	}
	
	private void stopPreviewAndFreeCamera(){
		
		if(camera != null){
			
			camera.stopPreview();
			camera.release();
			camera = null;
		}
	}


	
}
