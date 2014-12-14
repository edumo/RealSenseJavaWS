package edumo.realsense.ws;

import java.net.URISyntaxException;
import java.util.List;

import edumo.realsense.ws.hand.Hand;
import edumo.realsense.ws.listener.HandListener;

public class RealSenseHands {

	RealSenseWS client = null;

	public RealSenseHands() {
		
	}
	
	public int[] getImageSize(){
		return client.getImageSize();
	}

	public void init() {
		try {
			client = new RealSenseWS();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	public void start(HandListener handListener) {
		client.setHandListener(handListener);
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
		client.queryModule(RealSenseWS.CUID_PXCMHandModule);

		try {
			Thread.sleep(1200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		client.init();

		try {
			Thread.sleep(400);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		client.createActiveConfigurationHand();

		 try {
		 Thread.sleep(200);
		 } catch (InterruptedException e) {
		 e.printStackTrace();
		 }
		 client.disableAllAlerts();
		
		 try {
		 Thread.sleep(200);
		 } catch (InterruptedException e) {
		 e.printStackTrace();
		 }
		// client.disableAllGestures();
		 client.enableAllGestures();
		//
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		client.applyChanges();

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
	
	public static void main(String[] args) {
		RealSenseHands realSenseHands = new RealSenseHands();
		realSenseHands.init();
		realSenseHands.client.verbose = true;
		realSenseHands.start(new HandListener() {

			@Override
			public void newHands(List<Hand> hands) {
				//currentHands = hands;
			}
		});
	}
}
