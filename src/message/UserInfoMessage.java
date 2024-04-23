package message;

import java.io.Serializable;

public class UserInfoMessage implements MessageInterface, Serializable {

	private int id;
	private Status status;
	private int userId;
	private String userName;
	
	public UserInfoMessage(int id, Status status, int userId, String userName) {
		this.id = id;
		this.status = status;
		this.userId = userId;
		this.userName = userName;
	}
	
	public UserInfoMessage(int id, Status status, int userId) {
		this.id = id;
		this.status = status;
		this.userId = userId;
		this.userName = "";
	}
	
	@Override
	public int getID() {
		return id;
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
