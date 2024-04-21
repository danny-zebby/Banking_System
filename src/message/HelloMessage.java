package message;

import java.io.Serializable;

public class HelloMessage implements MessageInterface, Serializable {
	final int id;
	final String from;
	final Status status;
	
	public HelloMessage(int id, String from, Status status) {
		this.id = id;
		this.from = from;
		this.status = status;
	}
	
	@Override
	public int getID() {
		return this.id;
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
