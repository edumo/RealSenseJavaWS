import java.net.URI;
import java.net.URISyntaxException;
import java.nio.channels.NotYetConnectedException;
import java.util.Map;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_10;
import org.java_websocket.handshake.ServerHandshake;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

public class RealSenseWS extends WebSocketClient {

	private static final int INSTANCE = 0;

	private static final int CAPTURE_MANAGER = 1;

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


	int counter = 1;

	static int CUID_PXCMFaceModule = 1144209734;

	boolean error = false;
	Integer instanceId = 0;
	Integer updateInstance = 0;

	private Integer captureManagerId;

	public RealSenseWS() throws URISyntaxException {
		super(new URI("ws://localhost:4181"), new Draft_10());
	}

	public RealSenseWS(URI serverURI) {
		super(serverURI);
	}

	public void createInstance() {
		error = false;
		updateInstance = INSTANCE;
		// "_onopen:{"js_version":"2.0.1","id":1,"instance":{"value":0},"method":"PXCMSenseManager_CreateInstance"}"
		AbstractRSCall callRS = new CallRS(counter, new InstanceRS("0"),
				"2.0.1", "PXCMSenseManager_CreateInstance");
		String json = gson.toJson(callRS);
		// lastReceivedCall = null;
		send(json);
	}

	@Override
	public void send(String text) throws NotYetConnectedException {
		if (!error) {
			System.out.println("\"call" + text);
			super.send(text);
			counter++;
		} else {
			System.out
					.println("NO CONSEGUIMOS CONECTAR, NO ENVIAMOS LOS MENSAJES");
		}
	}

	public void enableModule() {
		CallRS callRS = new CallRS(counter, new InstanceRS(""
				+ lastReceivedCall.instance.value), null,
				"PXCMSenseManager_EnableModule");
		callRS.mid = CUID_PXCMFaceModule;
		callRS.mdesc = 0;
		String json = gson.toJson(callRS);
		send(json);
		// "call{"mid":1144209734,"mdesc":0,"id":2,"instance":{"value":139895760},"method":"PXCMSenseManager_EnableModule"}"
	}

	public void queryModule() {
		// "call{"mid":1144209734,"id":3,"instance":{"value":139895760},"method":"PXCMSenseManager_QueryModule"}"
		CallRS callRS = new CallRS(counter, new InstanceRS(""
				+ lastReceivedCall.instance.value), null,
				"PXCMSenseManager_QueryModule");
		callRS.mid = CUID_PXCMFaceModule;
		String json = gson.toJson(callRS);
		send(json);
	}

	public void createActiveConfiguration() {
		// "call{"id":4,"instance":{"value":335788288},"method":"PXCMFaceModule_CreateActiveConfiguration"}"
		AbstractRSCall callRS = new CallRS(counter, new InstanceRS(""
				+ lastReceivedCall.instance.value), null,
				"PXCMFaceModule_CreateActiveConfiguration");
		String json = gson.toJson(callRS);
		send(json);
	}

	public void getConfigurations() {
		// "call{"id":5,"instance":{"value":140758416},"method":"PXCMFaceConfiguration_GetConfigurations"}"
		AbstractRSCall callRS = new CallRS(counter, new InstanceRS(""
				+ lastReceivedCall.instance.value), null,
				"PXCMFaceConfiguration_GetConfigurations");
		String json = gson.toJson(callRS);
		send(json);
	}

	public void startTrackingMode() {
		// "call{"trackingMode":1,"id":6,"instance":{"value":140758416},"method":"PXCMFaceConfiguration_SetTrackingMode"}"
		CallRS callRS = new CallRS(counter, new InstanceRS(""
				+ lastReceivedCall.instance.value), null,
				"PXCMFaceConfiguration_SetTrackingMode");
		callRS.trackingMode = 1;
		String json = gson.toJson(callRS);
		send(json);

	}

	public void setConfig() {
		// "call{"configs":{"recognitionInstance":{"value":10867240},
		// "expressionInstance":{"value":10867180},
		// "detection":{"isEnabled":true,"maxTrackedFaces":4,"smoothingLevel":0},
		// "landmarks":{"isEnabled":true,"maxTrackedFaces":1,"smoothingLevel":0,"numLandmarks":78},
		// "pose":{"isEnabled":false,"maxTrackedFaces":1,"smoothingLevel":0},
		// "expressionProperties":{"isEnabled":false,"maxTrackedFaces":1},
		// "recognitionProperties":{"isEnabled":false,"accuracyThreshold":100,"registrationMode":1},
		// "storageDesc":{"isPersistent":false,"maxUsers":33},"storageName":"","strategy":0},
		// "id":7,"instance":{"value":10867000},"method":"PXCMFaceConfiguration_ApplyChanges"}"
		// String msg =
		// "{\"configs\":{\"recognitionInstance\":{\"value\":1086240},"
		// + "\"expressionInstance\":{\"value\":10867180},"
		// +
		// "\"detection\":{\"isEnabled\":true,\"maxTrackedFaces\":4,\"smoothingLevel\":0},"
		// +
		// "\"landmarks\":{\"isEnabled\":true,\"maxTrackedFaces\":1,\"smoothingLevel\":0,"
		// +
		// "\"numLandmarks\":78},\"pose\":{\"isEnabled\":false,\"maxTrackedFaces\":1,"
		// +
		// "\"smoothingLevel\":0},\"expressionProperties\":{\"isEnabled\":false,"
		// +
		// "\"maxTrackedFaces\":1},\"recognitionProperties\":{\"isEnabled\":false,"
		// +
		// "\"accuracyThreshold\":100,\"registrationMode\":1},\"storageDesc\":{"
		// + "\"isPersistent\":false,\"maxUsers\":33},\"storageName\":\"\","
		// + "\"strategy\":0},\"id\":7,\"instance\":{\"value\":"
		// + lastReceivedCall.instance.value
		// + "},"
		// + "\"method\":\"PXCMFaceConfiguration_ApplyChanges\"}";
		// System.out.println(msg);

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
		int index = json.indexOf("pose");
		json = json.replace("pose\":{\"isEnabled\":true",
				"pose\":{\"isEnabled\":false");

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
		System.out.println(json);
		send(json);
	}

	public void init() {
		// "call{"handler":true,"onModuleProcessedFrame":true,
		// "onConnect":false,"attachDataToCallbacks":true,"id":8,
		// "instance":{"value":95497648},"method":"PXCMSenseManager_Init"}"
		InitRS initRS = new InitRS(true, true, false, true, counter,
				new InstanceRS(instanceId),
				"PXCMSenseManager_Init");
		String json = gson.toJson(initRS);
		send(json);
	}

	// "call{"id":9,"instance":{"value":21135664},"method":"PXCMSenseManager_QueryCaptureManager"}"
	// realsense-2.0.js:692
	// "_onmessage:{"id":9,"instance":{"value":20827984}}" realsense-2.0.js:733
	// "call{"type":1,"id":10,"instance":{"value":20827984},"method":"PXCMCaptureManager_QueryImageSize"}"
	// realsense-2.0.js:692
	// "_onmessage:{"id":10,"size":{"width":1280,"height":720},"instance":{"value":0}}"
	// realsense-2.0.js:733
	// "call{"blocking":false,"id":11,"instance":{"value":21135664},"method":"PXCMSenseManager_StreamFrames"}"
	// realsense-2.0.js:692
	// "_onmessage:{"id":11,"instance":{"value":0}}"

	public void queryCaptureManager() {
		updateInstance = CAPTURE_MANAGER;
		CallRS callRS = new CallRS(counter, new InstanceRS(""
				+ instanceId), null,
				"PXCMSenseManager_QueryCaptureManager");
		tempReceiveId = lastReceivedCall.instance.value;
		String json = gson.toJson(callRS);
		send(json);

	}

	public void queryImageSize() {
		// "call{"trackingMode":1,"id":6,"instance":{"value":140758416},"method":"PXCMFaceConfiguration_SetTrackingMode"}"
		CallRS callRS = new CallRS(counter, new InstanceRS(""
				+ captureManagerId), null,
				"PXCMCaptureManager_QueryImageSize");
		callRS.type = 1;
		String json = gson.toJson(callRS);
		System.out.println(json);
		 send(json);
	}

	public void streamFrames() {
		// "call{"trackingMode":1,"id":6,"instance":{"value":140758416},"method":"PXCMFaceConfiguration_SetTrackingMode"}"
		CallRS callRS = new CallRS(counter, new InstanceRS("" + instanceId),
				null, "PXCMSenseManager_StreamFrames");
		tempReceiveId = null;
		callRS.blocking = false;
		String json = gson.toJson(callRS);
		System.out.println(json);
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
		System.out.println("\"_onmessage:" + message);

		lastMessage = message;

		if (lastMessage.contains("configs")) {
			lastMessageConfigs = lastMessage;
		}

		CallRS callRS = gson.fromJson(message, CallRS.class);

		if(updateInstance != null){
			
			switch(updateInstance){
			case INSTANCE:
				instanceId = callRS.instance.value;
				break;
			case CAPTURE_MANAGER:
				captureManagerId = callRS.instance.value;
				break;
			}
			updateInstance = null;
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

	// SIN CAMARA
	// "_onopen:{"js_version":"2.0.1","id":1,"instance":{"value":0},"method":"PXCMSenseManager_CreateInstance"}"
	// realsense-2.0.js:725
	// "_onmessage:{"id":1,"instance":{"value":10959640}}" realsense-2.0.js:733
	// "call{"mid":1144209734,"mdesc":0,"id":2,"instance":{"value":10959640},"method":"PXCMSenseManager_EnableModule"}"
	// realsense-2.0.js:692
	// "_onmessage:{"id":2,"instance":{"value":0}}" realsense-2.0.js:733
	// "call{"mid":1144209734,"id":3,"instance":{"value":10959640},"method":"PXCMSenseManager_QueryModule"}"
	// realsense-2.0.js:692
	// "_onmessage:{"id":3,"instance":{"value":11320304}}" realsense-2.0.js:733
	// "call{"id":4,"instance":{"value":11320304},"method":"PXCMFaceModule_CreateActiveConfiguration"}"
	// realsense-2.0.js:692
	// "_onmessage:{"id":4,"instance":{"value":11125136}}" realsense-2.0.js:733
	// "call{"id":5,"instance":{"value":11125136},"method":"PXCMFaceConfiguration_GetConfigurations"}"
	// realsense-2.0.js:692
	// "_onmessage:{"id":5,"configs":{"recognitionInstance":{"value":11125376},"expressionInstance":{"value":11125316},"detection":{"isEnabled":true,"maxTrackedFaces":4,"smoothingLevel":0},"landmarks":{"isEnabled":true,"maxTrackedFaces":1,"smoothingLevel":0,"numLandmarks":78},"pose":{"isEnabled":true,"maxTrackedFaces":1,"smoothingLevel":0},"expressionProperties":{"isEnabled":false,"maxTrackedFaces":1},"recognitionProperties":{"isEnabled":false,"accuracyThreshold":100,"registrationMode":1},"storageDesc":{"isPersistent":false,"maxUsers":33},"storageName":"","strategy":0},"instance":{"value":0}}"
	// realsense-2.0.js:733
	// "call{"trackingMode":1,"id":6,"instance":{"value":11125136},"method":"PXCMFaceConfiguration_SetTrackingMode"}"
	// realsense-2.0.js:692
	// "_onmessage:{"id":6,"instance":{"value":0}}" realsense-2.0.js:733
	// "call{"configs":{"recognitionInstance":{"value":11125376},"expressionInstance":{"value":11125316},"detection":{"isEnabled":true,"maxTrackedFaces":4,"smoothingLevel":0},"landmarks":{"isEnabled":true,"maxTrackedFaces":1,"smoothingLevel":0,"numLandmarks":78},"pose":{"isEnabled":false,"maxTrackedFaces":1,"smoothingLevel":0},"expressionProperties":{"isEnabled":false,"maxTrackedFaces":1},"recognitionProperties":{"isEnabled":false,"accuracyThreshold":100,"registrationMode":1},"storageDesc":{"isPersistent":false,"maxUsers":33},"storageName":"","strategy":0},"id":7,"instance":{"value":11125136},"method":"PXCMFaceConfiguration_ApplyChanges"}"
	// realsense-2.0.js:692
	// "_onmessage:{"id":7,"instance":{"value":0}}" realsense-2.0.js:733
	// "call{"handler":true,"onModuleProcessedFrame":true,"onConnect":false,"attachDataToCallbacks":true,"id":8,"instance":{"value":10959640},"method":"PXCMSenseManager_Init"}"
	// realsense-2.0.js:692
	// "_onmessage:{"id":8,"status":-3,"instance":{"value":0}}"

	// "_onopen:{"js_version":"2.0.1","id":1,"instance":{"value":0},"method":"PXCMSenseManager_CreateInstance"}"
	// realsense-2.0.js:725
	// "_onmessage:{"id":1,"instance":{"value":139895760}}" realsense-2.0.js:733
	// "call{"mid":1144209734,"mdesc":0,"id":2,"instance":{"value":139895760},"method":"PXCMSenseManager_EnableModule"}"
	// realsense-2.0.js:692
	// "_onmessage:{"id":2,"instance":{"value":0}}" realsense-2.0.js:733
	// "call{"mid":1144209734,"id":3,"instance":{"value":139895760},"method":"PXCMSenseManager_QueryModule"}"
	// realsense-2.0.js:692
	// "_onmessage:{"id":3,"instance":{"value":335788288}}" realsense-2.0.js:733
	// "call{"id":4,"instance":{"value":335788288},"method":"PXCMFaceModule_CreateActiveConfiguration"}"
	// realsense-2.0.js:692
	// "_onmessage:{"id":4,"instance":{"value":140758416}}" realsense-2.0.js:733
	// "call{"id":5,"instance":{"value":140758416},"method":"PXCMFaceConfiguration_GetConfigurations"}"
	// realsense-2.0.js:692
	// "_onmessage:{"id":5,"configs":{"recognitionInstance":{"value":140758656},"expressionInstance":{"value":140758596},"detection":{"isEnabled":true,"maxTrackedFaces":4,"smoothingLevel":0},"landmarks":{"isEnabled":true,"maxTrackedFaces":1,"smoothingLevel":0,"numLandmarks":78},"pose":{"isEnabled":true,"maxTrackedFaces":1,"smoothingLevel":0},"expressionProperties":{"isEnabled":false,"maxTrackedFaces":1},"recognitionProperties":{"isEnabled":false,"accuracyThreshold":100,"registrationMode":1},"storageDesc":{"isPersistent":false,"maxUsers":33},"storageName":"","strategy":0},"instance":{"value":0}}"
	// realsense-2.0.js:733
	// "call{"trackingMode":1,"id":6,"instance":{"value":140758416},"method":"PXCMFaceConfiguration_SetTrackingMode"}"
	// realsense-2.0.js:692
	// "_onmessage:{"id":6,"instance":{"value":0}}"

}