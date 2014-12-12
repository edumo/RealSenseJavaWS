package edumo.realsense.ws.listener;

import java.util.List;

import edumo.realsense.ws.hand.Hand;

public interface HandListener {

	void newHands(List<Hand> hands);
}
