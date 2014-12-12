package edumo.realsense.ws.listener;

import java.util.List;

import edumo.realsense.ws.face.Face;

public interface FaceListener {

	void newFaces(List<Face> faces);
}
