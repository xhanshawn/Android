package com.xhanshawn.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.format.Time;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.xhanshawn.data.LatalkMessage;
import com.xhanshawn.latalk.CameraActivity;
import com.xhanshawn.latalk.Latalk;

public class MessagePostFactory {
	
	//default avd
//	private static String URIBase = "http://10.0.2.2:3000";

	//genymotion
	private static String URIBase = ServerAccessFactory.getUrlBase();
	
	
	public final static int LIKE = 0;
	public final static int DISLIKE = 1;
	
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
			message.setMessageId(attrs.getLong("id"));
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
	
	public static void postLatalks(ArrayList<LatalkMessage> list){
		
		if(list != null) new Poster().execute(list);
	}
	
	public static void likeLatalk(LatalkMessage message){
		Map<Integer, LatalkMessage> map = new HashMap<Integer, LatalkMessage>();
		map.put(LIKE, message);
		new Updater().execute(map);
	}
	
	public static void dislikeLatalk(LatalkMessage message){
		Map<Integer, LatalkMessage> map = new HashMap<Integer, LatalkMessage>();
		map.put(DISLIKE, message);
		new Updater().execute(map);
	}
	
	public static boolean updateLatalk(LatalkMessage message, int action){
		if(message == null) return true;
		
		String action_str;
		if(action == LIKE){
			action_str = "like/";
		}else if(action == DISLIKE){
			action_str = "dislike/";
		}else{
			return false;
		}
		HttpClient client = new DefaultHttpClient(); 
		long id = message.getMessageId();
		HttpPost post = new HttpPost(URIBase + "/messages/" + action_str + id +".json");
		post.addHeader("Content-Type", "application/json");
		
		JSONObject json = new JSONObject();
		try {
			JSONObject message_json = new JSONObject();
			message_json.put("message_type",message.getMessageType());
			message_json.put("user_name", message.getUserName());
			
			if(action == LIKE) {
				message_json.put("like_num", message.getLike() + 1);
				message_json.put("dislike_num", message.getDislike());
			}else if(action == DISLIKE){
				message_json.put("like_num", message.getLike());
				message_json.put("dislike_num", message.getDislike() + 1);
			}else{
				return false;
			}
			
			json.put("message", message_json);
			json.put("commit", "Update Message");
			
			StringEntity entity = new StringEntity(json.toString(), "UTF-8");
			post.setEntity(entity);
			HttpResponse response = client.execute(post);
			HttpEntity res_entity = response.getEntity();
			String data = EntityUtils.toString(res_entity);
			JSONObject attrs = new JSONObject(data);
//			Log.v("server response", attrs.toString());
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
	
	
	static class Updater extends AsyncTask<Map<Integer, LatalkMessage>, Void, Boolean>{
		
				
		@Override
		protected Boolean doInBackground(Map<Integer, LatalkMessage>... params) {
			// TODO Auto-generated method stub
			
			if(params[0].get(LIKE) != null){
				MessagePostFactory.updateLatalk(params[0].get(LIKE), LIKE);
			}else if(params[0].get(DISLIKE) != null){
				MessagePostFactory.updateLatalk(params[0].get(DISLIKE), DISLIKE);
			}
			return null;
		}
		
	}
	static class Poster extends AsyncTask<ArrayList<LatalkMessage>, Void, Boolean> {
		List<LatalkMessage> list = new ArrayList<LatalkMessage>();
		@Override
		protected Boolean doInBackground(ArrayList<LatalkMessage>... params) {
			// TODO Auto-generated method stub
			boolean result = true;
			for(LatalkMessage mess : params[0]){
				boolean succeed = postLatalkMessage(mess);
				if(!succeed) result = false;
			}
				
			list = params[0];
			return result;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			Context current_context = Latalk.getAppContext();
			if(result){
				Toast post_suc_toast = Toast.makeText(current_context,
						AlertMessageFactory.postSucceeded(),
						Toast.LENGTH_LONG);
				post_suc_toast.show();
			} else {
				

				Toast post_fail_toast = Toast.makeText(current_context,
						AlertMessageFactory.postFailed(),
						Toast.LENGTH_LONG);

				post_fail_toast.show();
			}
			
			for (LatalkMessage mess : list) {
				
				
				LatalkDbFactory ldbf = new LatalkDbFactory(current_context);
				ldbf.insert(mess);
			}
			
			
			super.onPostExecute(result);
		}
	}
}
