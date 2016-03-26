package com.wx.show.wxnews.util;


import android.util.Log;

public class LogUtil {

	public static boolean isDebug = true;

	public static void p(String msg) {
		if (isDebug)
			System.out.println(msg);
	}

	public static void d(String tag, int v) {
		if (isDebug)
			Log.d(tag, "" + v);
	}

	public static void d(String tag, String v) {
		if (isDebug)
			Log.d(tag, v);
	}

	public static void e(String tag, int v) {
		if (isDebug)
			Log.e(tag, "" + v);
	}

	public static void e(String tag, String v) {
		if (isDebug)
			Log.e(tag, v);
	}
	public static void v(String tag, String v) {
		if (isDebug)
			Log.v(tag, v);
	}
}
