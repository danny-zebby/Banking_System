package message;
import java.io.Serializable;

public class LoginMessage implements MessageInterface, Serializable {
	final int id;
	final String text;
	final String to;
	final String from;
	final Status status;
	final int userId;
	final String password;
	
	public LoginMessage(int id, String to, String from, Status status, int userId, String password) {
		this.id = id;
		this.text = userId + "@" + password;
		this.to = to;
		this.from = from;
		this.status = status;
		this.userId = userId;
		this.password = password;
	}
	
	public LoginMessage(int id, String to, String from, Status status, String text) {
		this.id = id;
		this.text = text;
		this.to = to;
		this.from = from;
		this.status = status;
		this.userId = 0;
		this.password = null;
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

	public int getUserId() {
		return this.userId;
	}
	
	public String getPassword() {
		return this.password;
	}
}
