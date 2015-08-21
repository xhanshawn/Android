package com.xhanshawn.latalk;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class ImportSNSActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_import_sns);
		
		AccessToken accessToken = AccessToken.getCurrentAccessToken();
		GraphRequest request = GraphRequest.newMeRequest(
				  accessToken,
				  new GraphRequest.GraphJSONObjectCallback() {
				    @Override
				    public void onCompleted(JSONObject object, GraphResponse response) {
				    // Insert your code here
				    	try {
							JSONObject posts = object.getJSONObject("posts");
							JSONArray data = posts.getJSONArray("data");
							String story = data.getJSONObject(0).getString("story");
							Log.v("story", story);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				    }
				});

		Bundle parameters = new Bundle();
		parameters.putString("fields", "posts");
		request.setParameters(parameters);
		request.executeAsync();
	}
}
