package com.xhanshawn.util;

import android.content.Context;
import android.util.TypedValue;

public class ParamsFactory {
	public static int toDP(int v, Context context){
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, v, context.getResources().getDisplayMetrics());
	}
}
