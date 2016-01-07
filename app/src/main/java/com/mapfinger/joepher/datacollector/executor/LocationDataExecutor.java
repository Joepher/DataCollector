package com.mapfinger.joepher.datacollector.executor;

import android.content.Context;


// import com.mapfinger.joepher.datacollector.entity.LocationData;
import com.mapfinger.joepher.datacollector.entity.TransferUnit;
import com.mapfinger.joepher.datacollector.log.MyLog;
import com.mapfinger.joepher.datacollector.service.DataTransferService;
import com.mapfinger.joepher.datacollector.service.NetworkConnectivity;

import java.util.Vector;

/**
 * Created by Joepher on 2015/5/9.
 */
public class LocationDataExecutor implements Runnable {
	// private LocationData dataToSend;
	private TransferUnit dataToSend;
	// private Vector<LocationData> dataQueue;
	private Vector<TransferUnit> dataQueue;
	private DataTransferService dataTransferService;
	private NetworkConnectivityExecutor networkConnectivityExecutor;

	private static final int SEND_DURATION = 5000;

	public LocationDataExecutor(Context context) {
		// this.dataToSend = new LocationData();
		this.dataToSend = new TransferUnit();
		// this.dataQueue = new Vector<LocationData>();
		this.dataQueue = new Vector<TransferUnit>();
		this.dataTransferService = new DataTransferService();
		this.networkConnectivityExecutor = new NetworkConnectivityExecutor(context);
		new Thread(networkConnectivityExecutor).start();
	}

	@Override
	public void run() {
		doTransferLocationData();
	}

	// public boolean addLocationdata(LocationData locationData) {
	public boolean addLocationdata(TransferUnit locationData) {
		synchronized (dataQueue) {
			dataQueue.add(locationData);
			dataQueue.notifyAll();
		}

		MyLog.i("[New] " + locationData.toString());

		return true;
	}

	private void doTransferLocationData() {
		while (true) {
			if (networkConnectivityExecutor.networkConnectivity.getNetStatus() == NetworkConnectivity.NET_WORK_ENABLED) {
				// Network is usable now.Try to send data to server from queue.
				// Keep the poped top data just for rolling back in case of sending failed.
				synchronized (dataQueue) {
					if (dataQueue.size() > 0) {
						//dataToSend = dataQueue.get(0);
						dataToSend = dataQueue.remove(0);
					} else {
						dataToSend = null;
					}
				}

				if (dataToSend != null) {
					// Sending location data to server.
					int responseStatus = dataTransferService.sendData(dataToSend);

					if (responseStatus == DataTransferService.SEND_SUCCESSED) {
						// Sending data successed.
						MyLog.i("[Send] " + dataToSend + " Successed");
						// Waiting for the notification from adding new location data till time out.
						waitToResendTillTimeOut();
					} else if (responseStatus == DataTransferService.SEND_FAILED) {
						// Sending data failed.
						MyLog.i("[Send] " + dataToSend + " Failed");
						// Rolling back: put the data back to the top of the queue.
						synchronized (dataQueue) {
							dataQueue.add(0, dataToSend);

							MyLog.i("[RollBack] " + dataToSend + " Successed");
							// Waiting for the notification from adding new location data till time out.
							try {
								dataQueue.wait(SEND_DURATION);
							} catch (InterruptedException e) {
								e.printStackTrace();

								MyLog.w("Interrupted while waiting for sending location data");
							}
						}
					}
				} else {
					MyLog.i("[Send] No data to send");
					// Waiting for the notification from adding new location data till time out.
					waitToResendTillTimeOut();
				}
			} else {
				// Network is not usable now.
				waitToResendTillTimeOut();
			}
		}
	}

	private void waitToResendTillTimeOut() {
		// Waiting for the notification from adding new location data till time out.
		synchronized (dataQueue) {
			try {
				dataQueue.wait(SEND_DURATION);
			} catch (InterruptedException e) {
				e.printStackTrace();

				MyLog.w("Interrupted while waiting for sending location data.");
			}
		}
	}
}
