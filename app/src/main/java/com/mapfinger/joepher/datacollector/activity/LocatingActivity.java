package com.mapfinger.joepher.datacollector.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.mapfinger.joepher.datacollector.R;
import com.mapfinger.joepher.datacollector.service.BDLocationService;

/**
 * Created by Joepher on 2015/11/9.
 */
public class LocatingActivity extends Activity {
	public static TextView locData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_bdlocation);

		locData = (TextView) findViewById(R.id.locData);
		locData.setMovementMethod(new ScrollingMovementMethod());

		startService(new Intent(BDLocationService.ACTION));
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
