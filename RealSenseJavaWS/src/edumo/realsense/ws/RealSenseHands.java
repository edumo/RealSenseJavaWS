package edumo.realsense.ws;

import java.net.URI;
import java.net.URISyntaxException;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_10;

import com.google.gson.Gson;

public class RealSenseHands {
	RealSenseWS client;

	public RealSenseHands() {
		super();
	}

	public void init() {
		try {
			client = new RealSenseWS();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	public void start() {

		client.connect();
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		client.createInstance();
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		client.enableModule(RealSenseWS.CUID_PXCMHandModule);
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		client.queryModule();

		try {
			Thread.sleep(1200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		client.init();

		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		client.queryCaptureManager();

		try {
			Thread.sleep(1200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		client.queryImageSize();

		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		client.streamFrames();
	}

}
