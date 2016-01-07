package com.mapfinger.joepher.datacollector.executor;

// import com.mapfinger.joepher.datacollector.entity.LocationData;
import com.mapfinger.joepher.datacollector.entity.TransferUnit;
import com.mapfinger.joepher.datacollector.service.LocalDataTransferService;

import java.util.Vector;
/**
 * Created by Joepher on 2015/11/3.
 */
public class LocalLocationDataExecutor implements Runnable {
	// private Vector<LocationData> dataQueue;
	private Vector<TransferUnit> dataQueue;
	private LocalDataTransferService service;

	public LocalLocationDataExecutor() {
		this.dataQueue = new Vector<>();
		this.service = new LocalDataTransferService();
	}

	// public boolean addLocationdata(LocationData locationData) {
	public boolean addLocationdata(TransferUnit locationData) {
		synchronized (dataQueue) {
			dataQueue.add(locationData);
			dataQueue.notifyAll();
		}

		return true;
	}

	@Override
	public void run() {
		runwork();
	}

	private void runwork() {
		while (true) {
			synchronized (dataQueue) {
				if (dataQueue.size() > 0) {
					service.sendData(dataQueue.remove(0));
				} else {
					try {
						dataQueue.wait();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			try {
				Thread.sleep(2000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
