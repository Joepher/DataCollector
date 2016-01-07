package com.mapfinger.joepher.datacollector.entity;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

/**
 * Created by Administrator on 2016/1/7.
 */
public class TransferUnit implements KvmSerializable {
	private String userId;
	private String timeline;
	private String latitude;
	private String longitude;
	private String accuracy;
	private String address;

	public TransferUnit() {
		this.userId = "";
		this.timeline = "";
		this.latitude = "";
		this.longitude = "";
		this.accuracy = "";
		this.address = "";
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTimeline() {
		return timeline;
	}

	public void setTimeline(String timeline) {
		this.timeline = timeline;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(String accuracy) {
		this.accuracy = accuracy;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String info() {
		return userId + "," + timeline + "," + longitude + "," + latitude + "," + accuracy + "," + address;
	}

	@Override
	public String toString() {
		return "TransferUnit{" +
				"userId='" + userId + '\'' +
				", timeline='" + timeline + '\'' +
				", latitude='" + latitude + '\'' +
				", longitude='" + longitude + '\'' +
				", accuracy='" + accuracy + '\'' +
				", address='" + address + '\'' +
				'}';
	}

	@Override
	public Object getProperty(int i) {
		if (i == 0) {
			return getUserId();
		} else if (i == 1) {
			return getTimeline();
		} else if (i == 2) {
			return getLatitude();
		} else if (i == 3) {
			return getLongitude();
		} else if (i == 4) {
			return getAccuracy();
		} else if (i == 5) {
			return getAddress();
		} else {
			return null;
		}
	}

	@Override
	public int getPropertyCount() {
		return 6;
	}

	@Override
	public void setProperty(int i, Object o) {
		if (i == 0) {
			setUserId((String) o);
		} else if (i == 1) {
			setTimeline((String) o);
		} else if (i == 2) {
			setLatitude((String) o);
		} else if (i == 3) {
			setLongitude((String) o);
		} else if (i == 4) {
			setAccuracy((String) o);
		} else if (i == 5) {
			setAddress((String) o);
		}
	}

	@Override
	public void getPropertyInfo(int i, Hashtable hashtable, PropertyInfo propertyInfo) {
		if (i == 0) {
			propertyInfo.name = "userid";
		} else if (i == 1) {
			propertyInfo.name = "timeline";
		} else if (i == 2) {
			propertyInfo.name = "latitude";
		} else if (i == 3) {
			propertyInfo.name = "longitude";
		} else if (i == 4) {
			propertyInfo.name = "accuracy";
		} else if (i == 5) {
			propertyInfo.name = "address";
		} else {
			return;
		}

		propertyInfo.type = String.class;
	}

}
