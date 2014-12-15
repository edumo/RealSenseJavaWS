package edumo.realsense.ws.hand;

public class Hand {

	public Integer uniqueId;
	public Integer userId;
	public Long timeStamp;
	public Boolean isCalibrated;
	public Integer bodySide;
	
	public Integer openness;
	
	public String gestureName = null;

	public BoundingBoxImage boundingBoxImage;
	public MassCenterImage massCenterImage;
	
	public MassCenterWorld massCenterWorld;
//	massCenterWorld: {
//        x: Number,
//        y: Number,
//        z: Number,
//    }
	}
