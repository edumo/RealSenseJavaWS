package edumo.realsense.ws;
import java.net.URISyntaxException;

import edumo.realsense.ws.listener.FaceListener;

public class RealSenseFaces {

	RealSenseWS client = null;

	public RealSenseFaces(){
		
	}
	
	public int[] getImageSize(){
		return client.getImageSize();
	}
	
	public void init(){
		try {
			client = new RealSenseWS();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	public void start(FaceListener faceListener){
		
		client.setFaceListener(faceListener);
		
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
		client.enableModule(RealSenseWS.CUID_PXCMFaceModule);
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		client.queryModule(RealSenseWS.CUID_PXCMFaceModule);

		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		client.createActiveConfigurationFace();
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		client.getConfigurations();

		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		client.startTrackingMode();

		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		client.setConfig(false, false);

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
			Thread.sleep(200);
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
	
	public static void main(String[] args) throws URISyntaxException {

	
	}
}
