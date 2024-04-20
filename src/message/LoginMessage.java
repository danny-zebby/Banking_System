package message;
import java.io.Serializable;

public class LoginMessage implements MessageInterface, Serializable {
	final int id;
	final Status status;
	final int userId;
	final String password;
	
	public LoginMessage(int id, Status status, int userId, String password) {
		this.id = id;
		this.status = status;
		this.userId = userId;
		this.password = password;
	}
	
	public LoginMessage(int id, Status status) {
		this.id = id;
		this.status = status;
		this.userId = 0;
		this.password = null;
	}
	
	@Override
	public int getID() {
		return this.id;
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
