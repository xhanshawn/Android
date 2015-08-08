package com.xhanshawn.util;

import android.provider.BaseColumns;

public final class LatalkDbContract {
	public LatalkDbContract(){}
	
	public static abstract class LatalkEntry implements BaseColumns {
		public static final String TABLE_NAME = "latalk";
		public static final String COLUMN_NAME_MESSAGE_ID = "message_id";
		public static final String COLUMN_NAME_MESSAGE_TYPE = "message_type";
        public static final String COLUMN_NAME_CONTENT = "content";
        public static final String COLUMN_NAME_LATITUDE = "latitude";
        public static final String COLUMN_NAME_LONGITUDE = "longitude";
        public static final String COLUMN_NAME_USER_NAME = "user_name";
        public static final String COLUMN_NAME_HOLD_TIME = "hold_time";
        public static final String COLIMN_NAME_CREATED_AT = "created_at";
        public static final String COLUMN_NAME_NULLABLE = "";
        
	}
}
