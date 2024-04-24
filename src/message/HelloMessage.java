package message;

import java.io.Serializable;

public class HelloMessage implements MessageInterface, Serializable {
	final String from;
	final Status status;
	
	public HelloMessage(String from, Status status) {
		this.from = from;
		this.status = status;
	}
	

	@Override
	public Status getStatus() {
		return this.status;
	}
	
	public String getFrom() {
		return this.from;
	}
	
	public String toString() {
		return getFrom() + " Hello";
	}
	
	
}
