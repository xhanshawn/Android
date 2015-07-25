package com.xhanshawn.latalk;

import java.util.ArrayList;
import java.util.List;

import com.xhanshawn.data.LatalkMessage;
import com.xhanshawn.util.DataPassCache;
import com.xhanshawn.util.LocationInfoFactory;
import com.xhanshawn.util.MessageGetFactory;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class RaceBrowserActivity extends Activity {
	Location location;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_race_browser);
		
		LocationInfoFactory location_info = new LocationInfoFactory(RaceBrowserActivity.this);
		location = location_info.getCurrentLocation();
		
		List<LatalkMessage> messages = new ArrayList<LatalkMessage>();

		messages = DataPassCache.getPuzzleRaces(DataPassCache.UNREAD_ALL, 
					(location == null) ? DataPassCache.AWAY : DataPassCache.NEAR_BY);
		
		
	}
}
