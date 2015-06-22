package com.xhanshawn.latalk;

import java.util.ArrayList;

import com.xhanshawn.data.LatalkMessage;
import com.xhanshawn.util.DataPassCache;
import com.xhanshawn.util.IntegerIdentifiers;
import com.xhanshawn.view.PuzzleGridAdapter;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class PuzzleRaceCreateActivity extends Activity {
	private ActionBar mActionBar;
	private GridView attached_pic_pr_gv;
	private ArrayList<LatalkMessage> race_puzzles = new ArrayList<LatalkMessage>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_puzzle_race_create);
		
		customActionBar();
		
		attached_pic_pr_gv = (GridView) findViewById(R.id.attached_pic_pr_gv);
		
		if(race_puzzles.isEmpty()) openPuzzleCreateActivity();
		
	}
	
	private void openPuzzleCreateActivity(){
		
		Intent puzzle_create_activity = new Intent("com.xhanshawn.latalk.PUZZLECREATEACTIVITY");
		startActivityForResult(puzzle_create_activity, IntegerIdentifiers.PASS_PUZZLES);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == IntegerIdentifiers.PASS_PUZZLES) {
			Bundle extras = data.getExtras();
			int key = extras.getInt("puzzles_key");
			race_puzzles.addAll(DataPassCache.getLatalks(key));
		}
		
		GridView puzzle_race_gv = (GridView) findViewById(R.id.attached_pic_pr_gv);
		PuzzleGridAdapter pg_adapter = new PuzzleGridAdapter(PuzzleRaceCreateActivity.this, race_puzzles);
		puzzle_race_gv.setAdapter(pg_adapter);
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
				PuzzleRaceCreateActivity.this.finish();
			}
		});
	    
	    Button puzzle_r_ok_b = (Button) v.findViewById(R.id.puzzle_r_create_ok_b);
	    puzzle_r_ok_b.setText("Post");
	    puzzle_r_ok_b.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PuzzleRaceCreateActivity.this.finish();
			}
		});
	    
	}
}
