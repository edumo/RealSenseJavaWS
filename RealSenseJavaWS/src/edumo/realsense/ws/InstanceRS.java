package edumo.realsense.ws;

public class InstanceRS {

	public int value;

	public InstanceRS(String value) {
		super();
		if (value != null)
			this.value = Integer.valueOf(value);
	}

	public InstanceRS(int value) {
		super();
		this.value = value;
	}
}
