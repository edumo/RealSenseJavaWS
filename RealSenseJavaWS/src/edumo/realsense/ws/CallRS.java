package edumo.realsense.ws;
public class CallRS extends AbstractRSCall {

	public String js_version;

	public Integer mid;

	public Integer mdesc;

	public Integer trackingMode;

	public Integer type;

	public Boolean blocking;

	public CallRS(int id, InstanceRS instance) {
		super();
		this.id = id;
		this.instance = instance;
	}

	public CallRS(int id, InstanceRS instance, String js_version, String method) {
		super();
		this.id = id;
		this.instance = instance;
		this.js_version = js_version;
		this.method = method;
	}
	
}
