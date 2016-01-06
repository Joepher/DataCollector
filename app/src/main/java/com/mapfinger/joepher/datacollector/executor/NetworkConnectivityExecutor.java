package com.mapfinger.joepher.datacollector.executor;

import android.content.Context;

import com.mapfinger.joepher.datacollector.log.MyLog;
import com.mapfinger.joepher.datacollector.service.NetworkConnectivity;

/**
 * Created by Joepher on 2015/5/9.
 */
public class NetworkConnectivityExecutor implements Runnable {
	public NetworkConnectivity networkConnectivity;

	private static final int CHECK_DURATION = 10000;

	public NetworkConnectivityExecutor(Context context) {
		networkConnectivity = new NetworkConnectivity(context);
	}

	@Override
	public void run() {
		while (true) {
			networkConnectivity.checkNetworkStatus();

			try {
				Thread.sleep(CHECK_DURATION);

				MyLog.d("Waiting 10s to recheck the network status.");
			} catch (InterruptedException e) {
				e.printStackTrace();

				MyLog.w("Interrupted while waiting for checking the network status.");
			}
		}
	}
}
