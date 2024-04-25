package message;

import java.io.Serializable;
import java.util.List;

//this class is only for getting list of users given accountNumber
public class AccountInfoMessage implements MessageInterface, Serializable {

	private Status status;
	private int accountNumber;
	private List<Integer> users;

	public AccountInfoMessage(Status status, int accountNumber, List<Integer> users) {
		this.status = status;
		this.accountNumber = accountNumber;
		this.users = users;
	}

	public AccountInfoMessage(Status status, int accountNumber) {
		this.status = status;
		this.accountNumber = accountNumber;
		this.users = null;
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
