package com.xhanshawn.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.imageio.ImageIO;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sun.misc.BASE64Encoder;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.format.Time;
import android.util.Base64;
import android.util.Log;

import com.xhanshawn.data.LatalkMessage;

public class MessagePostFactory {
	
	//default avd
//	private static String URIBase = "http://10.0.2.2:3000";

	//genymotion
	private static String URIBase = "http://10.0.3.2:3000";
	
	public static boolean postLatalkMessage(LatalkMessage message){
		
		
		
		HttpClient client = new DefaultHttpClient(); 
		HttpPost post = new HttpPost(URIBase + "/messages.json");
		post.addHeader("Content-Type", "application/json");
		
		JSONObject json = new JSONObject();
		
		
		try {
			JSONObject message_json = new JSONObject();
			message_json.put("message_type",message.getMessageType());
			message_json.put("content",message.getContent());
			
			if(message.getMessageType().equals("Puzzle")) {
				message_json.put("race_num", message.getRaceNum());
				message_json.put("start_id", message.getStart().getMessageId());
			}
			
			if(message.isLocationSet()){
				message_json.put("longitude",String.format("%.06f", message.getLongitude()));
				message_json.put("latitude",String.format("%.06f", message.getLatitude()));
			}else{
				message_json.put("longitude",String.format("%.06f",181.0f));
				message_json.put("latitude",String.format("%.06f", 91.0f));
			}
			Time now = new Time();
			now.setToNow();
			message_json.put("hold_time",message.getHold_time() + now.toMillis(false)/1000);
			message_json.put("user_name", message.getUserName());
			if(message.getAttahedPic() != null) {
				message_json.put("image", "data:image/jpg;base64," + parseToBase64String(message.getAttahedPic()));;
				Log.v("image", message.getAttahedPic().toString());

			}
			
			json.put("message", message_json);
			json.put("commit", "Create Message");
			
			StringEntity entity = new StringEntity(json.toString(), "UTF-8");
			post.setEntity(entity);
			HttpResponse response = client.execute(post);
			HttpEntity res_entity = response.getEntity();
			String data = EntityUtils.toString(res_entity);
			JSONObject attrs = new JSONObject(data);
			Log.v("server response", attrs.toString());
			message.setMessageId(attrs.getInt("id"));
			return true;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			client.getConnectionManager().shutdown();
		}
			
			
	
		return false;
	}
	
	public static boolean postImage(String url){
		
		
		
		HttpClient client = new DefaultHttpClient(); 
		HttpPost post = new HttpPost(URIBase + "/messages.json");
		post.addHeader("Content-Type", "application/json");
		
		JSONObject json = new JSONObject();
		
		try {
			JSONObject message_json = new JSONObject();
			
			message_json.put("message_type","Puzzle");
			message_json.put("content","puzzle for test image");
			
				message_json.put("longitude",String.format("%.03f",181.0f));
				message_json.put("latitude",String.format("%.03f", 91.0f));
			
			message_json.put("hold_time",30000);
			message_json.put("user_name", "xhanshawn");
			
			
			
			JSONObject image_json = new JSONObject();
			
			String[] url_array = url.split("/");
			String file_name = url_array[url_array.length - 1];
//			image_json.put("content", parseToBase64String(url));
//			message_json.put("content_type", "image/jpeg");
//			message_json.put("filename", file_name);
			
//			message_json.put("image_data", parseToBase64String(url));
			
//			message_json.put("image", "data:image/jpg;base64," + parseToBase64String(url));

			
			json.put("message", message_json);
			json.put("commit", "Create Message");
			
			StringEntity entity = new StringEntity(json.toString(), "UTF-8");
			post.setEntity(entity);
			HttpResponse response = client.execute(post);
			System.out.println(response.getStatusLine().toString());
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			client.getConnectionManager().shutdown();
		}
			
			
	
		
		
		
		return true;
	}
	
	public static String parseToBase64String(Bitmap bmp){
		
		if(bmp == null) return null;
		String img_str = null;
		
		Bitmap bm_img = bmp;
		ByteArrayOutputStream byte_array_os = new ByteArrayOutputStream();  
		bm_img.compress(Bitmap.CompressFormat.JPEG, 100, byte_array_os); //bm is the bitmap object   
		byte[] byte_array = byte_array_os.toByteArray(); 
		
		img_str = Base64.encodeToString(byte_array, Base64.DEFAULT);
		
		return img_str;
	}
}
