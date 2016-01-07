package com.mapfinger.joepher.datacollector.service;

import com.mapfinger.joepher.datacollector.entity.Config;
// import com.mapfinger.joepher.datacollector.entity.LocationData;
import com.mapfinger.joepher.datacollector.entity.TransferUnit;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * Created by Joepher on 2015/11/3.
 */
public class LocalDataTransferService {
	// private List<LocationData> locationDatas;
	private List<TransferUnit> locationDatas;

	private static final int MINPTS = 12;

	public LocalDataTransferService() {
		this.locationDatas = new ArrayList<>();
	}

	// public void sendData(LocationData locationData) {
	public void sendData(TransferUnit locationData) {
		//persistanceWithBuffer(locationData);
		persistance(locationData);
	}

	// private void persistance(LocationData locationData) {
	private void persistance(TransferUnit locationData) {
		String filepath = Config.getHomePath() + getFileName();
		File file = new File(filepath);

		try {
			OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(file, true), Charset.forName("gbk"));
			BufferedWriter writer = new BufferedWriter(out);
			writer.write(locationData.info());
			writer.newLine();
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// private void persistanceWithBuffer(LocationData locationData) {
	private void persistanceWithBuffer(TransferUnit locationData) {
		locationDatas.add(locationData);

		if (locationDatas.size() == MINPTS) {
			String filepath = Config.getHomePath() + getFileName();
			File file = new File(filepath);

			try {
				OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(file, true), Charset.forName("gbk"));
				BufferedWriter writer = new BufferedWriter(out);
				for (int i = 0; i < MINPTS; ++i) {
					writer.write(locationDatas.get(i).info());
					writer.newLine();
				}
				writer.flush();
				writer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			locationDatas.clear();
		}

	}

	private String getFileName() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return "/" + format.format(new Date()) + ".txt";
	}

}
