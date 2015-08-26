package com.xhanshawn.latalk;

import com.xhanshawn.util.LocationInfoFactory;
import com.xhanshawn.util.MessageGetFactory;
import com.xhanshawn.view.SeekBarPreference;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.location.Location;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;

public class QuerySettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {
	final static public String KEY_PREF_UPDATE_INTERVAL = "pref_key_update_interval";
	final static public String KEY_PREF_BE_UPDATE = "pref_key_be_update";
	final static public String KEY_PREF_DISTANCE = "pref_key_distance";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.query_preference);
		Preference interval = findPreference(KEY_PREF_UPDATE_INTERVAL);
        interval.setSummary("Query Interval: " + getPreferenceScreen().getSharedPreferences().getString(KEY_PREF_UPDATE_INTERVAL, "") + "s");

        SeekBarPreference distance = (SeekBarPreference) findPreference(KEY_PREF_DISTANCE);
		distance.setMinOffset(MessageGetFactory.MIN_OFFSET);
		distance.setValuePattern("m", 0);
		distance.setMax(MessageGetFactory.LEVEL.length - 1);
		int slope = MessageGetFactory.SLOPE;
//				(MessageGetFactory.LEVEL[MessageGetFactory.LEVEL.length - 1] - MessageGetFactory.LEVEL[0])/(MessageGetFactory.LEVEL.length - 1);
		distance.setSlope(slope);
		
	}

	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		getPreferenceScreen().getSharedPreferences()
        .registerOnSharedPreferenceChangeListener(this);
	}
	
	


	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
		getPreferenceScreen().getSharedPreferences()
        .unregisterOnSharedPreferenceChangeListener(this);
	}


	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// TODO Auto-generated method stub
		if (key.equals(KEY_PREF_UPDATE_INTERVAL)) {
            Preference interval = findPreference(key);
            // Set summary to be the user-description for the selected value
            interval.setSummary("Query Interval: " + sharedPreferences.getString(key, "") + "s");
        }
	}
}
