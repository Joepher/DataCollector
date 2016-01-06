package com.mapfinger.joepher.datacollector.service;

import android.os.Environment;

import com.mapfinger.joepher.datacollector.entity.LocationData;

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
	private String home;

	private List<LocationData> locationDatas;

	private static final int MINPTS = 12;

	public LocalDataTransferService() {
		this.locationDatas = new ArrayList<>();
		this.home = Environment.getExternalStorageDirectory().getPath() + "/mapfinger";
		this.home = getHome();
	}

	public void sendData(LocationData locationData) {
		//persistanceWithBuffer(locationData);
		persistance(locationData);
	}

	private void persistance(LocationData locationData) {
		String filepath = this.home + getFileName();
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

	private void persistanceWithBuffer(LocationData locationData) {
		locationDatas.add(locationData);

		if (locationDatas.size() == MINPTS) {
			String filepath = this.home + getFileName();
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

	private String getHome() {
		File file = new File(home);
		if (!file.exists()) {
			file.mkdir();
		}

		return home;
	}

	private String getFileName() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return "/" + format.format(new Date()) + ".txt";
	}

}
