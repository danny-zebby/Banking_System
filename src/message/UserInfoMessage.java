package message;

import java.io.Serializable;

public class UserInfoMessage implements MessageInterface, Serializable {

	private final Status status;
	private final int userId;
	private final String userName;
	
	public UserInfoMessage(Status status, int userId, String userName) {
		this.status = status;
		this.userId = userId;
		this.userName = userName;
	}
	
	public UserInfoMessage(Status status, int userId) {
		this.status = status;
		this.userId = userId;
		this.userName = "";
	}

	@Override
	public Status getStatus() {
		return status;
	}

	public int getUserID() {
		return this.userId;
	}
	
	public String getUserName() {
		return this.userName;
	}
}
