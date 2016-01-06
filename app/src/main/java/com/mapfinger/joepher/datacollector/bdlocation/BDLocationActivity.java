package com.mapfinger.joepher.datacollector.bdlocation;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.mapfinger.joepher.datacollector.R;

/**
 * Created by Joepher on 2015/4/23.
 */
public class BDLocationActivity extends Activity {
	private LocationClient locationClient;
	private TextView locData;
	private LocationMode locationMode = LocationMode.Hight_Accuracy;

	private static final int DEFAULT_DURATION = 5000;
	private static final String DEFAULT_COOR = "bd09ll";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bdlocation);

		locationClient = ((BDLocationApplication) getApplication()).locationClient;

		locData = (TextView) findViewById(R.id.locData);
		locData.setMovementMethod(new ScrollingMovementMethod());

		((BDLocationApplication) getApplication()).locData = locData;

		execute();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		locationClient.stop();
	}

	private void execute() {
		if (locationClient != null && locationClient.isStarted()) {
			locationClient.setLocOption(getLocationOption());
			locationClient.start();
		} else {
			locData.setText("locClient is null or not started\n");

			locationClient.setLocOption(getLocationOption());
			locationClient.start();
		}
	}

	private LocationClientOption getLocationOption() {
		LocationClientOption option = new LocationClientOption();

		option.setScanSpan(DEFAULT_DURATION);
		option.setCoorType(DEFAULT_COOR);
		option.setIsNeedAddress(true);
		//option.setAddrType("all");
		option.setLocationMode(locationMode);

		return option;
	}

}
