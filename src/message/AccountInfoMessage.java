package message;

import java.io.Serializable;
import java.util.List;

public class AccountInfoMessage implements MessageInterface, Serializable {

	private int id;
	private Status status;
	private int accountNumber;
	private List<Integer> users;
	
	public AccountInfoMessage(int id, Status status, int accountNumber, List<Integer> users) {
		this.id = id;
		this.status = status;
		this.accountNumber = accountNumber;
		this.users = users;
	}
	
	public AccountInfoMessage(int id, Status status, int accountNumber) {
		this.id = id;
		this.status = status;
		this.accountNumber = accountNumber;
		this.users = null;
	}
	
	
	@Override
	public int getID() {
		return this.id;
	}

	@Override
	public Status getStatus() {
		return this.status;
	}
	
	public int getAccountNumber() {
		return this.accountNumber;
	}
	
	public List<Integer> getUsers() {
		return this.users;
	}
}
