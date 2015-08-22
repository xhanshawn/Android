package com.xhanshawn.view;

import com.xhanshawn.latalk.R;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class SeekBarPreference extends Preference implements OnSeekBarChangeListener{
	View pref_view;
	TextView title;
	TextView val;
	SeekBar seek_bar;
	String namespace = "http://schemas.android.com/apk/res/android";
	private int min;
	private int max;
	private int slope;
	private String pattern = "";
	private int pattern_pos = 0;
	private String title_str;
	
	
	public SeekBarPreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	
	public SeekBarPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		setMax(attrs.getAttributeIntValue(namespace, "max", 0));
		title_str = (String) this.getTitle();
		min = 0;
		slope = 1;
	}

	public SeekBarPreference(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public void setMax(int max){

		if(seek_bar != null) seek_bar.setMax(max);
		else this.max = max;
	}
	
	public void setMinOffset(int min){
		
		this.min = min;
	}
	
	public void setSlope(int slope){
		this.slope = slope;
	}
	@Override
	protected View onCreateView(ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		pref_view = inflater.inflate(R.layout.seekbar_pref, parent, false);
		title = (TextView) pref_view.findViewById(R.id.sb_pref_title);
		val = (TextView) pref_view.findViewById(R.id.sb_pref_val);
		seek_bar = (SeekBar) pref_view.findViewById(R.id.pref_seekbar);
		seek_bar.setOnSeekBarChangeListener(this);
		seek_bar.setMax(max);
		title.setText(title_str);
		
		return pref_view;
	}

	public void setValuePattern(String pattern, int pattern_pos){
		
		this.pattern = pattern;
		this.pattern_pos = pattern_pos;
		
	}
	
	public TextView getTitleTextView(){
		return this.title;
	}
	
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		String val_str = pattern.substring(0, pattern_pos) + (min + progress * slope) + pattern.substring(pattern_pos);
		val.setText(val_str);
		SeekBarPreference.this.getSharedPreferences().
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
	}
}
