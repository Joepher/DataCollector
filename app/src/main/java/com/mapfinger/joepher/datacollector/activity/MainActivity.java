package com.mapfinger.joepher.datacollector.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.mapfinger.joepher.datacollector.R;
import com.mapfinger.joepher.datacollector.entity.Config;

/**
 * Created by Administrator on 2016/1/7.
 */
public class MainActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		initEnv();
	}

	private void initEnv() {
		if (checkUserId()) {
			Intent intent = new Intent(MainActivity.this, LocatingActivity.class);
			startActivity(intent);
		} else {
			Intent intent = new Intent(MainActivity.this, SettingUserIDActivity.class);
			startActivity(intent);
		}
	}

	private boolean checkUserId() {
		if (Config.getUserId() != null) {
			return true;
		} else {
			return false;
		}
	}

}
