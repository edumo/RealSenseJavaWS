package edumo.realsense.ws.hand;

public class Hand {

	Integer uniqueId;
	Integer userId;
	Long timeStamp;
	Boolean isCalibrated;
	Integer bodySide;

	public BoundingBoxImage boundingBoxImage;
	public MassCenterImage massCenterImage;
	// massCenterImage:{
	// x:384,
	// y:284
	// },
}
