package com.mapfinger.joepher.datacollector.entity;

import android.os.Environment;

import com.mapfinger.joepher.datacollector.log.MyLog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * Created by Administrator on 2016/1/6.
 */
public class Config {
	private static String DIR_HOME = "/mapfinger/datacollector";
	private static String FILE_CONFIG = "/config.properties";

	private static String KEY_USERID = "userid";

	private static String userid = null;

	public static String getHomePath() {
		String home = Environment.getExternalStorageDirectory().getPath() + DIR_HOME;
		File file = new File(home);
		if (!file.exists()) {
			file.mkdirs();
		}

		return home;
	}

	private static String getConfigPath() {
		return getHomePath() + FILE_CONFIG;
	}

	public static boolean saveUserId(String userId) {
		try {
			File config = new File(getConfigPath());
			Properties prop = new Properties();
			OutputStream out = new FileOutputStream(config);
			prop.setProperty(KEY_USERID, userId);
			prop.store(out, null);
			out.close();
		} catch (Exception e) {
			MyLog.e("Save userId " + userId + "failed.");

			return false;
		}

		return true;
	}


	public static String getUserId() {
		if (userid == null) {
			userid = loadUserId();
		}

		return userid;
	}

	private static String loadUserId() {
		try {
			File config = new File(getConfigPath());
			Properties prop = new Properties();
			prop.load(new FileInputStream(config));

			return prop.getProperty(KEY_USERID);
		} catch (Exception e) {
			MyLog.e("Read userId failed.");

			return null;
		}
	}
}
