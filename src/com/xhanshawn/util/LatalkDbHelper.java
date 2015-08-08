package com.xhanshawn.util;

import com.xhanshawn.util.LatalkDbContract.LatalkEntry;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class LatalkDbHelper extends SQLiteOpenHelper{
	
	public static final int DATABASE_VERSION = 2;
	public static final String DATABASE_NAME = "Latalk.db";
	
	private static final String TEXT_TYPE = " TEXT";
	private static final String INT_TYPE = " INT";
	private static final String VARCHAR_TYPE = " VARCHAR";
	private static final String BIGINT_TYPE = " BIGINT";
	private static final String DOUBLE_TYPE = " DOUBLE";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
        "CREATE TABLE " + LatalkEntry.TABLE_NAME + " (" +
        LatalkEntry._ID + " INTEGER PRIMARY KEY," +
        LatalkEntry.COLUMN_NAME_MESSAGE_ID + INT_TYPE + COMMA_SEP +
        LatalkEntry.COLUMN_NAME_MESSAGE_TYPE + VARCHAR_TYPE + "(255)" + COMMA_SEP + 
        LatalkEntry.COLUMN_NAME_HOLD_TIME + BIGINT_TYPE + COMMA_SEP +
        LatalkEntry.COLUMN_NAME_LATITUDE + DOUBLE_TYPE + COMMA_SEP +
        LatalkEntry.COLUMN_NAME_LONGITUDE + DOUBLE_TYPE + COMMA_SEP +
        LatalkEntry.COLUMN_NAME_CONTENT + TEXT_TYPE + COMMA_SEP +
        LatalkEntry.COLUMN_NAME_USER_NAME + VARCHAR_TYPE + "(255)" + COMMA_SEP + 
        LatalkEntry.COLIMN_NAME_CREATED_AT + BIGINT_TYPE +
        " );";

    private static final String SQL_DELETE_ENTRIES =
        "DROP TABLE IF EXISTS " + LatalkEntry.TABLE_NAME;
    
    
	public LatalkDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(SQL_CREATE_ENTRIES);
		Log.v("create db", "create db");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL(SQL_DELETE_ENTRIES);
		db.setVersion(newVersion);
		onCreate(db);
	}

	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		super.onDowngrade(db, oldVersion, newVersion);
	}
}
