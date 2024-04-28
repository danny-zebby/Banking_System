package message;

import java.io.Serializable;

public class ExitMessage implements MessageInterface, Serializable {
	final Status status;
	
	public ExitMessage(Status status) {
		this.status = status;
	}
	
	@Override
	public Status getStatus() {
		return this.status;
	}

}
