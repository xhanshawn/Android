package com.xhanshawn.latalk;

import android.app.Application;
import android.content.Context;

public class Latalk extends Application {
	
	private static Context context;

    public void onCreate(){
        super.onCreate();
        Latalk.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return Latalk.context;
    }
}
