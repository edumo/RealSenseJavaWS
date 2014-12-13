package edumo.realsense.ws.hand;

public class Hand {

	public Integer uniqueId;
	public Integer userId;
	public Long timeStamp;
	public Boolean isCalibrated;
	public Integer bodySide;

	public BoundingBoxImage boundingBoxImage;
	public MassCenterImage massCenterImage;
	// massCenterImage:{
	// x:384,
	// y:284
	// },
}
