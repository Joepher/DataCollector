package com.mapfinger.joepher.datacollector.bdlocation;

import android.app.Application;
import android.content.Context;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
// import com.mapfinger.joepher.datacollector.entity.LocationData;
import com.mapfinger.joepher.datacollector.entity.TransferUnit;
import com.mapfinger.joepher.datacollector.executor.LocalLocationDataExecutor;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Joepher on 2015/5/7.
 * Used in BDLocationActivity(not used).
 */
public class BDLocationApplication extends Application {
	public LocationClient locationClient;
	public BDLocationListener locationListener;
	// private LocationDataExecutor locationDataExecutor;
	private LocalLocationDataExecutor localLocationDataExecutor;
	// private LocationData locationData;
	private TransferUnit locationData;

	public TextView locData;

	@Override
	public void onCreate() {
		super.onCreate();

		Context context = this.getApplicationContext();

		locationClient = new LocationClient(context);
		locationListener = new MyLocationListener();
		locationClient.registerLocationListener(locationListener);

		// locationDataExecutor = new LocationDataExecutor(context);
		// new Thread(locationDataExecutor).start();

		localLocationDataExecutor = new LocalLocationDataExecutor();
		new Thread(localLocationDataExecutor).start();

		// locationData = new LocationData();
		locationData = new TransferUnit();
	}

	public class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation bdLocation) {
			if (bdLocation == null) {
				// locData.setText("not received location data");
				locData.append("not received location data\n");
				return;
			}

			String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
			// locationData.setTimeline(bdLocation.getTime());
			locationData.setTimeline(time);
			locationData.setLatitude(bdLocation.getLatitude() + "");
			locationData.setLongitude(bdLocation.getLongitude() + "");
			locationData.setAccuracy(bdLocation.getRadius() + "");
			locationData.setAddress(bdLocation.getAddrStr());

			// locData.setText(locationData.toString());
			locData.append(locationData.info() + "\n");

			// locationDataExecutor.addLocationdata(locationData);
			localLocationDataExecutor.addLocationdata(locationData);
		}
	}

}
