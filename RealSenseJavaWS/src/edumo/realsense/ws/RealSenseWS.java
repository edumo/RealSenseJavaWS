package edumo.realsense.ws;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.channels.NotYetConnectedException;
import java.util.ArrayList;
import java.util.Map;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_10;
import org.java_websocket.handshake.ServerHandshake;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import edumo.realsense.ws.face.Face;
import edumo.realsense.ws.face.FacesMsg;
import edumo.realsense.ws.face.ImageSize;
import edumo.realsense.ws.hand.Hand;
import edumo.realsense.ws.hand.HandMsg;
import edumo.realsense.ws.listener.FaceListener;
import edumo.realsense.ws.listener.HandListener;

public class RealSenseWS extends WebSocketClient {

	private static final int UPDATE_INSTANCE = 0;
	private static final int UPDATE_CAPTURE_MANAGER = 1;

	private static final int UPDATE_IMAGE_SIZE = 2;
	private static final int UPDATE_MODULE = 3;
	private static final int UPDATE_ACTIVE_CONFIGURATION = 4;

	/**
	 * constructor, data 'a pelo'
	 * 
	 * @throws URISyntaxException
	 */

	Gson gson = new Gson();

	AbstractRSCall lastReceivedCall;
	String lastMessage;
	String lastMessageConfigs;
	Integer tempReceiveId = null;

	ImageSize imageSize;

	int counter = 1;

	static int CUID_PXCMFaceModule = 1144209734;
	static int CUID_PXCMHandModule = 1313751368;

	boolean error = false;
	Integer instanceId = 0;

	Integer doTarget = null;

	private Integer captureManagerId;
	private Integer moduleId;
	private Integer activeConfgirationId;

	HandListener handListener;
	FaceListener faceListener;

	public boolean verbose = false;

	public void setHandListener(HandListener handListener) {
		this.handListener = handListener;
	}

	public void setFaceListener(FaceListener faceListener) {
		this.faceListener = faceListener;
	}

	public RealSenseWS() throws URISyntaxException {
		super(new URI("ws://localhost:4181"), new Draft_10());
	}

	public RealSenseWS(URI serverURI) {
		super(serverURI);
	}

	public void createInstance() {
		error = false;
		doTarget = UPDATE_INSTANCE;
		AbstractRSCall callRS = new CallRS(counter, new InstanceRS("0"),
				"2.0.1", "PXCMSenseManager_CreateInstance");
		String json = gson.toJson(callRS);
		send(json);
	}

	@Override
	public void send(String text) throws NotYetConnectedException {
		if (!error) {
			if (verbose)
				System.out.println("\"call" + text);
			super.send(text);
			counter++;
		} else {
			System.out
					.println("NO CONSEGUIMOS CONECTAR, NO ENVIAMOS LOS MENSAJES");
		}
	}

	public void enableModule(int module) {
		CallRS callRS = new CallRS(counter, new InstanceRS(""
				+ lastReceivedCall.instance.value), null,
				"PXCMSenseManager_EnableModule");
		callRS.mid = module;
		callRS.mdesc = 0;
		String json = gson.toJson(callRS);
		send(json);
	}

	public void queryModule(int module) {
		doTarget = UPDATE_MODULE;
		CallRS callRS = new CallRS(counter, new InstanceRS(""
				+ lastReceivedCall.instance.value), null,
				"PXCMSenseManager_QueryModule");
		callRS.mid = module;
		String json = gson.toJson(callRS);
		send(json);
	}

	public void createActiveConfigurationFace() {
		doTarget = UPDATE_ACTIVE_CONFIGURATION;
		AbstractRSCall callRS = new CallRS(counter, new InstanceRS(""
				+ lastReceivedCall.instance.value), null,
				"PXCMFaceModule_CreateActiveConfiguration");
		String json = gson.toJson(callRS);
		send(json);
	}

	public void createActiveConfigurationHand() {
		doTarget = UPDATE_ACTIVE_CONFIGURATION;
		AbstractRSCall callRS = new CallRS(counter, new InstanceRS(""
				+ moduleId), null,
				"PXCMHandModule_CreateActiveConfiguration");
		String json = gson.toJson(callRS);
		send(json);
	}

	public void disableAllAlerts() {
		AbstractRSCall callRS = new CallRS(counter, new InstanceRS(""
				+ activeConfgirationId), null,
				"PXCMHandConfiguration_DisableAllAlerts");
		String json = gson.toJson(callRS);
		send(json);
	}

	public void disableAllGestures() {
		AbstractRSCall callRS = new CallRS(counter, new InstanceRS(""
				+ activeConfgirationId), null,
				"PXCMHandConfiguration_DisableAllGestures");
		String json = gson.toJson(callRS);
		send(json);
	}
	
	public void enableAllGestures() {
		AbstractRSCall callRS = new CallRS(counter, new InstanceRS(""
				+ activeConfgirationId), null,
				"PXCMHandConfiguration_EnableAllGestures");
		String json = gson.toJson(callRS);
		send(json);
	}
	
	

	public void applyChanges() {
		AbstractRSCall callRS = new CallRS(counter, new InstanceRS(""
				+ activeConfgirationId), null,
				"PXCMHandConfiguration_ApplyChanges");
		String json = gson.toJson(callRS);
		send(json);
	}

	public void getConfigurations() {
		AbstractRSCall callRS = new CallRS(counter, new InstanceRS(""
				+ lastReceivedCall.instance.value), null,
				"PXCMFaceConfiguration_GetConfigurations");
		String json = gson.toJson(callRS);
		send(json);
	}

	public void startTrackingMode() {
		CallRS callRS = new CallRS(counter, new InstanceRS(""
				+ lastReceivedCall.instance.value), null,
				"PXCMFaceConfiguration_SetTrackingMode");
		callRS.trackingMode = 1;
		String json = gson.toJson(callRS);
		send(json);

	}

	public void setConfig(boolean pose, boolean landmarks) {

		LinkedTreeMap result = gson.fromJson(lastMessageConfigs,
				LinkedTreeMap.class);

		LinkedTreeMap object = (LinkedTreeMap) result.get("configs");
		result.put("method", "PXCMFaceConfiguration_ApplyChanges");
		LinkedTreeMap instance = (LinkedTreeMap) result.remove("instance");
		instance.put("value", "" + lastReceivedCall.instance.value);
		result.put("instance", instance);
		result.put("id", counter);
		String json = gson.toJson(result);
		json = json.replaceAll(".0,", ",");
		json = json.replaceAll(".0}", "}");
		if (!pose) {
			json = json.replace("pose\":{\"isEnabled\":true",
					"pose\":{\"isEnabled\":false");
		}
		if (!landmarks) {
			json = json.replace("landmarks\":{\"isEnabled\":true",
					"landmarks\":{\"isEnabled\":false");
		}
		LinkedTreeMap object2 = (LinkedTreeMap) object
				.get("recognitionInstance");
		Double recognitionInstance = (Double) object2.get("value");
		String recognitionInstanceCad = "" + recognitionInstance;

		LinkedTreeMap object3 = (LinkedTreeMap) object
				.get("expressionInstance");
		Double expressionInstance = (Double) object3.get("value");
		String expressionInstanceCad = "" + expressionInstance;
		json = json.replaceAll(expressionInstanceCad,
				"" + expressionInstance.intValue());
		json = json.replaceAll(recognitionInstanceCad,
				"" + recognitionInstance.intValue());
		send(json);
	}

	public void init() {
		InitRS initRS = new InitRS(true, true, true, true, counter,
				new InstanceRS(instanceId), "PXCMSenseManager_Init");
		String json = gson.toJson(initRS);
		send(json);
	}

	public void queryCaptureManager() {
		doTarget = UPDATE_CAPTURE_MANAGER;
		CallRS callRS = new CallRS(counter, new InstanceRS("" + instanceId),
				null, "PXCMSenseManager_QueryCaptureManager");
		tempReceiveId = lastReceivedCall.instance.value;
		String json = gson.toJson(callRS);
		send(json);

	}

	public void queryImageSize() {
		doTarget = UPDATE_IMAGE_SIZE;
		CallRS callRS = new CallRS(counter, new InstanceRS(""
				+ captureManagerId), null, "PXCMCaptureManager_QueryImageSize");
		callRS.type = 1;
		String json = gson.toJson(callRS);
		send(json);
	}

	public void streamFrames() {
		CallRS callRS = new CallRS(counter, new InstanceRS("" + instanceId),
				null, "PXCMSenseManager_StreamFrames");
		tempReceiveId = null;
		callRS.blocking = false;
		String json = gson.toJson(callRS);
		send(json);
	}

	@Override
	public void onOpen(ServerHandshake handshakedata) {
		System.out.println("new connection opened");

		// primero montamos la conexión
		// createInstance();
	}

	@Override
	public void onClose(int code, String reason, boolean remote) {
		System.out.println("closed with exit code " + code
				+ " additional info: " + reason);
	}

	@Override
	public void onMessage(String message) {
		if (verbose)
			System.out.println("\"_onmessage:" + message);

		lastMessage = message;

		if (lastMessage.contains("configs")) {
			lastMessageConfigs = lastMessage;
		}

		CallRS callRS = gson.fromJson(message, CallRS.class);

		if (doTarget != null) {

			switch (doTarget) {
			case UPDATE_INSTANCE:
				instanceId = callRS.instance.value;
				break;
			case UPDATE_CAPTURE_MANAGER:
				captureManagerId = callRS.instance.value;
				break;
			case UPDATE_IMAGE_SIZE:
				imageSize = gson.fromJson(message, ImageSize.class);
				break;
			case UPDATE_MODULE:
				moduleId = callRS.instance.value;
				break;
			case UPDATE_ACTIVE_CONFIGURATION:
				activeConfgirationId = callRS.instance.value;
				break;

			}
			doTarget = null;
		}

		if ((lastReceivedCall == null || lastReceivedCall.instance.value != callRS.instance.value))
			if (callRS.instance.value != 0) {
				lastReceivedCall = callRS;
			}

		LinkedTreeMap result = gson.fromJson(message, LinkedTreeMap.class);

		if (result.containsKey("status")) {
			String statusValue = result.get("status").toString();
			int status = (int) Float.parseFloat(statusValue);
			if (status < 0) {
				System.out.println("TENEMOS UN ERROR CON STATUS " + status);
				error = true;
			}
		}

		if (result.containsKey("faces")) {
			FacesMsg facesMsg = gson.fromJson(message, FacesMsg.class);
			if (faceListener != null) {
				faceListener.newFaces(facesMsg.faces);
			}
		} else {
			if (faceListener != null)
				faceListener.newFaces(new ArrayList<Face>());
		}

		if (result.containsKey("hands")) {
			HandMsg handMsg = gson.fromJson(message, HandMsg.class);
			if (result.containsKey("gestures")) {
				ArrayList dos = (ArrayList) result.get("gestures");
				if (!dos.isEmpty()) {
					//System.out.println("hoal");
					for(int i = 0;i< dos.size();i++){
						LinkedTreeMap<String, Object> gesture = (LinkedTreeMap<String, Object>) dos.get(i);
						String gestureName = (String) gesture.get("name");
						Double handId = (Double) gesture.get("handId");
						for(Hand h:handMsg.hands){
							if(h.uniqueId == handId.intValue()){
								h.gestureName = gestureName;
							}
						}
					}
				}
			}
			if (handListener != null) {
				handListener.newHands(handMsg.hands);
			}
		} else {
			if (handListener != null) {
				handListener.newHands(new ArrayList<Hand>());
			}
		}

		
	}

	@Override
	public void onError(Exception ex) {
		System.err.println("an error occured:" + ex);
	}

	public static void main(String[] args) throws URISyntaxException {

		Gson gson = new Gson();
		AbstractRSCall callRS = new CallRS(33, new InstanceRS("3"));
		System.out.println(gson.toJson(callRS));
	}

	public int[] getImageSize() {
		if (imageSize != null) {

			int[] ret = { imageSize.size.width, imageSize.size.height };
			return ret;
		} else
			return null;

	}
}