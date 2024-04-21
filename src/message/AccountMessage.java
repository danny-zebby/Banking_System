package message;
import java.io.Serializable;

public class AccountMessage implements MessageInterface, Serializable {
	final int id;
	final Status status;
	final int accountNumber;
	final int currUserId;
	final int otherUserId;
	final int pin;
	final AccountMessageType type;
	
	public AccountMessage(int id, Status status, int accountNumber, int currUserId, int pin, AccountMessageType type) {
		this.id = id;
		this.status = status;
		this.accountNumber = accountNumber;
		this.currUserId = currUserId;
		this.otherUserId = -1;
		this.pin = pin;
		this.type = type;
	}
	
	public AccountMessage(int id, int currUserId, int accountNumber, Status status) {
		this(id, status, accountNumber, currUserId, accountNumber, AccountMessageType.ACCOUNT_INFO);
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

	public int getCurrUserId() {
		return this.currUserId;
	}

	public int getOtherUserId() {
		return this.otherUserId;
	}

	public int getPin() {
		return this.pin;
	}

	public AccountMessageType getType() {
		return this.type;
	}

	
	
}
