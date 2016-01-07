package com.mapfinger.joepher.datacollector.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import com.mapfinger.joepher.datacollector.R;
import com.mapfinger.joepher.datacollector.activity.LocatingActivity;
// import com.mapfinger.joepher.datacollector.entity.LocationData;
import com.mapfinger.joepher.datacollector.entity.Config;
import com.mapfinger.joepher.datacollector.entity.TransferUnit;
import com.mapfinger.joepher.datacollector.executor.LocalLocationDataExecutor;
import com.mapfinger.joepher.datacollector.log.MyLog;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.app.Notification.*;
import static android.os.Build.VERSION;
import static com.baidu.location.LocationClientOption.LocationMode;

/**
 * Created by Joepher on 2015/11/9.
 */
public class BDLocationService extends Service {
	private LocationClient locationClient;
	private BDLocationListener locationListener;
	private LocalLocationDataExecutor localLocationDataExecutor;
	// private LocationData locationData;
	private TransferUnit locationData;
	private LocationMode locationMode = LocationMode.Hight_Accuracy;

	private PowerManager.WakeLock wakeLock;
	private Notification notification;
	private NotificationManager notificationManager;
	private Method setForeground, startForeground, stopForeground;
	private Object[] setForegroundArgs, startForegroundArgs, stopForegroundArgs;
	private boolean reflectFlag = false;

	private TextView locData;
	private Thread worker;

	private static final Class[] setForegroundSignature = new Class[]{boolean.class};
	private static final Class[] startForegroundSignature = new Class[]{int.class, Notification.class};
	private static final Class[] stopForegroundSignature = new Class[]{boolean.class};

	private static final int DEFAULT_DURATION = 5000;
	private static final int NOTIFICATION_ID = 1;
	private static final String DEFAULT_COOR = "bd09ll";

	public static final String ACTION = "com.mapfinger.joepher.datacollector.service.BDLocationService";

	@Override
	public void onCreate() {
		super.onCreate();
		MyLog.d("onCreate");

		initForegroundEnvironment();
		initLocationEnvironment();
		startForegroundCompat(NOTIFICATION_ID, notification);
		startLocation();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		MyLog.d("onDestroy");

		stopForegroundCompat(NOTIFICATION_ID);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		MyLog.d("onStartCommand");

		return START_STICKY;
	}

	private void initLocationEnvironment() {
		Context context = this.getApplicationContext();
		locationClient = new LocationClient(context);
		locationListener = new MyLocationListener();
		locationClient.registerLocationListener(locationListener);
		localLocationDataExecutor = new LocalLocationDataExecutor();
		// locationData = new LocationData();
		locationData = new TransferUnit();

		locData = LocatingActivity.locData;

		worker = new Thread(localLocationDataExecutor);
	}

	private void initForegroundEnvironment() {
		setForegroundArgs = new Object[1];
		startForegroundArgs = new Object[2];
		stopForegroundArgs = new Object[1];

		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		try {
			startForeground = BDLocationService.class.getMethod("startForeground", startForegroundSignature);
			stopForeground = BDLocationService.class.getMethod("stopForeground", stopForegroundSignature);
		} catch (NoSuchMethodException e) {
			startForeground = stopForeground = null;
		}

		try {
			setForeground = getClass().getMethod("setForeground", setForegroundSignature);
		} catch (NoSuchMethodException e) {
			throw new IllegalStateException("OS doesn't have Service.startForeground OR Service.setForeground");
		}

		Builder builder = new Builder(this);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, LocatingActivity.class), 0);
		builder.setContentIntent(contentIntent);
		builder.setSmallIcon(R.drawable.ic_collector);
		builder.setTicker("Location Service Start");
		builder.setContentTitle("MapFinger-DataCollector");
		builder.setContentText("正在进行活动数据采集");
		notification = builder.build();
	}

	private void startLocation() {
		worker.start();

		if (null != locationClient && locationClient.isStarted()) {
			locationClient.setLocOption(getLocationOption());
			locationClient.start();
		} else {
			locationClient.setLocOption(getLocationOption());
			locationClient.start();
		}
	}

	private void startForegroundCompat(int id, Notification notification) {
		if (reflectFlag) {
			if (null != startForeground) {
				startForegroundArgs[0] = Integer.valueOf(id);
				startForegroundArgs[1] = notification;

				invokeMethod(startForeground, startForegroundArgs);
				return;
			}

			setForegroundArgs[0] = Boolean.TRUE;
			invokeMethod(setForeground, setForegroundArgs);
			notificationManager.notify(id, notification);
		} else {
			if (VERSION.SDK_INT >= 5) {
				startForeground(id, notification);
			} else {
				setForegroundArgs[0] = Boolean.TRUE;
				invokeMethod(setForeground, setForegroundArgs);
				notificationManager.notify(id, notification);
			}
		}

		acquireWakeLock();
	}

	private void stopForegroundCompat(int id) {
		if (reflectFlag) {
			if (null != stopForeground) {
				stopForegroundArgs[0] = Boolean.TRUE;
				invokeMethod(stopForeground, stopForegroundArgs);

				return;
			}

			notificationManager.cancel(id);
			setForegroundArgs[0] = Boolean.FALSE;
			invokeMethod(setForeground, setForegroundArgs);
		} else {
			if (VERSION.SDK_INT >= 5) {
				stopForegroundArgs[0] = Boolean.FALSE;
				invokeMethod(setForeground, setForegroundArgs);
			}
		}

		releaseWakeLock();
	}

	private void invokeMethod(Method method, Object[] args) {
		try {
			method.invoke(this, args);
		} catch (IllegalAccessException e) {
			MyLog.w("Unable to invoke method", e);
		} catch (InvocationTargetException e) {
			MyLog.w("Unable to invoke method", e);
		}
	}

	private void acquireWakeLock() {
		if (null == wakeLock) {
			PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
			wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, MyLog.MYTAG);

			if (null != wakeLock) {
				wakeLock.acquire();
			}
		}
	}

	private void releaseWakeLock() {
		if (null != wakeLock) {
			wakeLock.release();
			wakeLock = null;
		}
	}

	private LocationClientOption getLocationOption() {
		LocationClientOption option = new LocationClientOption();

		option.setScanSpan(DEFAULT_DURATION);
		option.setCoorType(DEFAULT_COOR);
		option.setIsNeedAddress(true);
		option.setLocationMode(locationMode);

		return option;
	}

	private class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation bdLocation) {
			if (null == bdLocation) {
				locData.append("Not received location data\n");
				return;
			}

			String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
			locationData.setUserId(Config.getUserId());
			locationData.setTimeline(time);
			locationData.setLongitude(bdLocation.getLongitude() + "");
			locationData.setLatitude(bdLocation.getLatitude() + "");
			locationData.setAccuracy(bdLocation.getRadius() + "");
			locationData.setAddress(bdLocation.getAddrStr());

			locData.append(locationData.info() + "\n");

			localLocationDataExecutor.addLocationdata(locationData);
		}
	}
}
