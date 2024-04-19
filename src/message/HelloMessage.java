package message;

import java.io.Serializable;

public class HelloMessage implements MessageInterface, Serializable {
	final int id;
	final String text;
	final String to;
	final String from;
	final Status status;
	
	public HelloMessage(int id, String text, String to, String from, Status status) {
		this.id = id;
		this.text = text;
		this.to = to;
		this.from = from;
		this.status = status;
	}
	
	@Override
	public int getID() {
		return this.id;
	}

	@Override
	public String getText() {
		return this.text;
	}

	@Override
	public String getTo() {
		return this.to;
	}

	@Override
	public String getFrom() {
		return this.from;
	}

	@Override
	public Status getStatus() {
		return this.status;
	}
	
	@Override
	public String toString() {
		return getFrom() + " Hello";
	}
	
	
}
