package com.xhanshawn.util;

import java.util.ArrayList;
import java.util.HashMap;

public class DataPassCache {
	private static ArrayList<byte[]> pic_list = new ArrayList<byte[]>();
	public static byte[] getPicByKey(int key) {
		
		return pic_list.get(key);
	}
	
	public static int cachePic(byte[] byte_array) {
		
		pic_list.add(byte_array);
		int key = pic_list.size() - 1;
		return key;
	}
}
