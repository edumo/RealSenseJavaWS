package edumo.realsense.ws;
import java.net.URI;
import java.net.URISyntaxException;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_10;
import org.java_websocket.handshake.ServerHandshake;

public class EmptyClient extends WebSocketClient {

	public EmptyClient(URI serverUri, Draft draft) {
		super(serverUri, draft);
	}

	public EmptyClient(URI serverURI) {
		super(serverURI);
	}

	@Override
	public void onOpen(ServerHandshake handshakedata) {
		System.out.println("new connection opened");

		// primero montamos la conexión

		send("{\"js_version\":\"2.0.1\",\"id\":1,\"instance\":{\"value\":0},\"method\":\"PXCMSenseManager_CreateInstance\"}");
	}

	@Override
	public void onClose(int code, String reason, boolean remote) {
		System.out.println("closed with exit code " + code
				+ " additional info: " + reason);
	}

	@Override
	public void onMessage(String message) {
		System.out.println("received message: " + message);
	}

	@Override
	public void onError(Exception ex) {
		System.err.println("an error occured:" + ex);
	}

	public static void main(String[] args) throws URISyntaxException {
		WebSocketClient client = new EmptyClient(
				new URI("ws://localhost:4181"), new Draft_10());
		client.connect();
	}
}