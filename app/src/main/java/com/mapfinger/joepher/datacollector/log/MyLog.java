package com.mapfinger.joepher.datacollector.log;

import android.util.Log;
/**
 * Created by Joepher on 2015/5/9.
 */
public class MyLog {
	public static final String MYTAG = "Mapfinger";

	public static void d(String msg) {
		Log.d(MYTAG, msg);
	}

	public static void e(String msg) {
		Log.e(MYTAG, msg);
	}

	public static void i(String msg) {
		Log.i(MYTAG, msg);
	}

	public static void v(String msg) {
		Log.v(MYTAG, msg);
	}

	public static void w(String msg) {
		Log.w(MYTAG, msg);
	}

	public static void w(String msg, Throwable e) {
		Log.wtf(MYTAG, msg, e);
	}
}
