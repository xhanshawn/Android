package com.xhanshawn.util;

import java.util.ArrayList;

import com.xhanshawn.data.LatalkMessage;
import com.xhanshawn.util.LatalkDbContract.LatalkEntry;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.util.Log;

public class LatalkDbFactory {
	LatalkDbHelper db_helper;
	public LatalkDbFactory(Context context){
		db_helper = new LatalkDbHelper(context);
	}
	
	public long insert(LatalkMessage message){
		SQLiteDatabase db = db_helper.getWritableDatabase();
		
		if(db.getVersion() < db_helper.DATABASE_VERSION){
			db_helper.onUpgrade(db, db.getVersion(), db_helper.DATABASE_VERSION);
		}
		// Create a new map of values, where column names are the keys
		ContentValues values = new ContentValues();
		values.put(LatalkEntry.COLUMN_NAME_MESSAGE_ID, message.getMessageId());
		values.put(LatalkEntry.COLUMN_NAME_MESSAGE_TYPE, message.getMessageType());
		values.put(LatalkEntry.COLUMN_NAME_CONTENT, message.getContent());
		values.put(LatalkEntry.COLUMN_NAME_HOLD_TIME, message.getHold_time());
		values.put(LatalkEntry.COLUMN_NAME_LATITUDE, message.getLatitude());
		values.put(LatalkEntry.COLUMN_NAME_LONGITUDE, message.getLongitude());
		values.put(LatalkEntry.COLUMN_NAME_USER_NAME, message.getUserName());
		values.put(LatalkEntry.COLIMN_NAME_CREATED_AT, message.getCreatedAt());;
		// Insert the new row, returning the primary key value of the new row
		long newRowId;
		newRowId = db.insert(
		         LatalkEntry.TABLE_NAME,
		         LatalkEntry.COLUMN_NAME_NULLABLE,
		         values);
		Log.v("inserted_db_id", message.getMessageId() + "    " + newRowId + "   ");
		return newRowId;
	}
	
	public ArrayList<LatalkMessage> readByLocation(Location location){
		
		SQLiteDatabase db = db_helper.getReadableDatabase();

		String[] projection = {
				
				LatalkEntry._ID,
				LatalkEntry.COLUMN_NAME_MESSAGE_ID,
				LatalkEntry.COLUMN_NAME_LATITUDE,
				LatalkEntry.COLUMN_NAME_LONGITUDE,
				LatalkEntry.COLUMN_NAME_HOLD_TIME,
		};
		
		double lat_diff = LocationInfoFactory.calLatByDis(location, LocationInfoFactory.UPDATE_DIS[0]);
		double lng_diff = LocationInfoFactory.calLngByDis(location, LocationInfoFactory.UPDATE_DIS[0]);
		
		Cursor c = db.query(LatalkEntry.TABLE_NAME,
				projection, 
				LatalkEntry.COLUMN_NAME_LATITUDE + " < " + String.valueOf(location.getLatitude() + lat_diff) + "AND" +
				LatalkEntry.COLUMN_NAME_LATITUDE + " > " + String.valueOf(location.getLatitude() - lat_diff) + "AND" +
				LatalkEntry.COLUMN_NAME_LATITUDE + " < " + String.valueOf(location.getLongitude() + lng_diff) + "AND" +
				LatalkEntry.COLUMN_NAME_LATITUDE + " > " + String.valueOf(location.getLongitude() - lng_diff) 
				//reserve for hold time
				, 
				null, 
				null, 
				null, 
				null);
		
		ArrayList<LatalkMessage> list = new ArrayList<LatalkMessage>();
		
		if(c.moveToFirst()) list.add(this.readByDbId(c.getLong(c.getColumnIndexOrThrow(LatalkEntry._ID))));
		
		while(c.moveToNext()){
			list.add(this.readByDbId(c.getLong(c.getColumnIndexOrThrow(LatalkEntry._ID))));
		}
		
		return list;
	}
	
	public LatalkMessage  readByDbId(long item_id){
		SQLiteDatabase db = db_helper.getReadableDatabase();

		String[] projection = {
				
				LatalkEntry._ID,
				LatalkEntry.COLUMN_NAME_MESSAGE_ID,
				LatalkEntry.COLUMN_NAME_HOLD_TIME,
				LatalkEntry.COLUMN_NAME_MESSAGE_TYPE,
				LatalkEntry.COLUMN_NAME_LATITUDE,
				LatalkEntry.COLUMN_NAME_LONGITUDE,
				LatalkEntry.COLUMN_NAME_USER_NAME,
				LatalkEntry.COLUMN_NAME_CONTENT,
				LatalkEntry.COLIMN_NAME_CREATED_AT
		};
		
		Cursor  c = db.query(
			LatalkEntry.TABLE_NAME, 
			projection, 
			LatalkEntry._ID + "= ?", 
			new String[]{String.valueOf(item_id)}, 
			null, 
			null, 
			null);
		
		boolean got = c.moveToFirst();
		if(got){
			
			LatalkMessage message = new LatalkMessage();
			message.setMessageId(c.getLong(c.getColumnIndexOrThrow(LatalkEntry.COLUMN_NAME_MESSAGE_ID)));
			message.setHold_time(c.getLong(c.getColumnIndexOrThrow(LatalkEntry.COLUMN_NAME_HOLD_TIME)));
			message.setLatitude(c.getFloat(c.getColumnIndexOrThrow(LatalkEntry.COLUMN_NAME_LATITUDE)));
			message.setLongitude(c.getFloat(c.getColumnIndexOrThrow(LatalkEntry.COLUMN_NAME_LONGITUDE)));
			message.setMessageType(c.getString(c.getColumnIndexOrThrow(LatalkEntry.COLUMN_NAME_MESSAGE_TYPE)));
			message.setContent(c.getString(c.getColumnIndexOrThrow(LatalkEntry.COLUMN_NAME_CONTENT)));
			message.setUserName(c.getString(c.getColumnIndexOrThrow(LatalkEntry.COLUMN_NAME_USER_NAME)));
			message.setCreatedAt(c.getLong(c.getColumnIndexOrThrow(LatalkEntry.COLIMN_NAME_CREATED_AT)));
			return message;

		} else return null;
	}
	
	
}
