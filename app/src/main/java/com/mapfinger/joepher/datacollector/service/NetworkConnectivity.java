package com.mapfinger.joepher.datacollector.service;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
/**
 * Created by Joepher on 2015/5/9.
 */
public class NetworkConnectivity {
	private int netStatus;
	private ConnectivityManager connectivityManager;
	private NetworkInfo networkInfo;

	public static final int NET_WORK_DISABLED = 0;
	public static final int NET_WORK_ENABLED = 1;

	public NetworkConnectivity(Context context) {
		connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		this.setNetStatus(NET_WORK_DISABLED);
	}

	public void checkNetworkStatus() {
		networkInfo = connectivityManager.getActiveNetworkInfo();

		if (networkInfo == null || !networkInfo.isAvailable()) {
			this.setNetStatus(NET_WORK_DISABLED);
		} else {
			this.setNetStatus(NET_WORK_ENABLED);
		}
	}

	public synchronized int getNetStatus() {
		return this.netStatus;
	}

	private synchronized void setNetStatus(int netStatus) {
		this.netStatus = netStatus;
	}
	
}
