public class InitRS extends AbstractRSCall {

	Boolean handler;
	Boolean onModuleProcessedFrame;
	Boolean onConnect;
	Boolean attachDataToCallbacks;

	public InitRS(Boolean handler, Boolean onModuleProcessedFrame,
			Boolean onConnect, Boolean attachDataToCallbacks, Integer id,
			InstanceRS instance, String method) {
		super();
		this.handler = handler;
		this.onModuleProcessedFrame = onModuleProcessedFrame;
		this.onConnect = onConnect;
		this.attachDataToCallbacks = attachDataToCallbacks;
		this.id = id;
		this.instance = instance;
		this.method = method;
	}

}
