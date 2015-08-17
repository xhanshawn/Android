package com.xhanshawn.latalk;

import android.app.Application;
import android.content.Context;

public class Latalk extends Application {
	
	final public static String FACEBOOK_APPID = "1673127742921347";

	private static Context context;
    public void onCreate(){
        super.onCreate();
        Latalk.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return Latalk.context;
    }
    
}
