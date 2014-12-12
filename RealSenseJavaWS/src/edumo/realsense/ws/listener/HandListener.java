package edumo.realsense.ws.listener;

import java.util.List;

import edumo.realsense.ws.hand.Hand;

public interface HandListener {

	void newFaces(List<Hand> hands);
}
