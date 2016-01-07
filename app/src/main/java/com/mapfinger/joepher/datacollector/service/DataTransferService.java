package com.mapfinger.joepher.datacollector.service;

// import com.mapfinger.joepher.datacollector.entity.LocationData;
import com.mapfinger.joepher.datacollector.entity.TransferUnit;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

/**
 * Created by Joepher on 2015/5/10.
 */
public class DataTransferService {
	private int responseStatus;
	private SoapObject request;
	private SoapObject response;
	private HttpTransportSE service;
	private SoapSerializationEnvelope envelope;

	private static final String SERVICE_NAMESPACE = "http://impl.server.mapfinger.com/";
	private static final String SERVICE_METHOD = "parseData";
	private static final String SERVICE_URL = "http://192.168.1.101/MapFingerServer/LocationDataParseServiceImplPort?wsdl";

	public static final int SEND_SUCCESSED = 101;
	public static final int SEND_FAILED = 102;
	public static final int NO_DATA_TO_SEND = 103;

	public DataTransferService() {
		responseStatus = 0;
		request = new SoapObject(SERVICE_NAMESPACE, SERVICE_METHOD);
		service = new HttpTransportSE(SERVICE_URL);
		envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	}

	// public int sendData(LocationData locationData) {
	public int sendData(TransferUnit locationData) {
		if (locationData != null) {
			request.addProperty("arg0", locationData);
			envelope.bodyOut = request;

			try {
				service.call(null, envelope);

				if (envelope.getResponse() != null) {
					response = (SoapObject) envelope.bodyIn;
					responseStatus = Integer.parseInt(response.getProperty(0).toString());
				} else {
					responseStatus = SEND_FAILED;
				}

			} catch (Exception e) {
				responseStatus = SEND_FAILED;

				e.printStackTrace();
			}
		} else {
			responseStatus = NO_DATA_TO_SEND;
		}

		return responseStatus;
	}
}
