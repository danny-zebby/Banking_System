package message;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class AccountMessage implements MessageInterface, Serializable {
	final Status status;
	final AccountMessageType type;
	final HashMap<String, String> info;


	public AccountMessage(Status status, int accountNumber, int currUserId, int pin, AccountMessageType type) {
		this.status = status;
		this.type = type;
		this.info = new HashMap<>();
		this.info.put("accountNumber", Integer.toString(accountNumber));
		this.info.put("currUserId", Integer.toString(currUserId));
		this.info.put("pin", Integer.toString(pin));
	}

	public AccountMessage(int currUserId, int accountNumber, Status status) {
		this(status, accountNumber, currUserId, accountNumber, AccountMessageType.ACCOUNT_INFO);
	}

	public AccountMessage(Status status, String name, String birthday, String password) {
		this.status = status;
		this.type = AccountMessageType.ADD_USER;
		this.info = new HashMap<>();
		this.info.put("name", name);
		this.info.put("birthday", birthday);
		this.info.put("password", password);
	}

	public AccountMessage(Status status, AccountMessageType type) {
		this.status = status;
		this.type = type;
		this.info = null;
	}

	@Override
	public Status getStatus() {
		return this.status;
	}

	public Map<String, String> getInfo() {
		return this.info;
	}

	public AccountMessageType getType() {
		return this.type;
	}

	public int getAccountNumber() {
		try {
			return Integer.parseInt(info.get("accountNumber"));
		} catch (Exception e) {
			return -1;
		}
	}

	public int getCurrUserId() {
		try {
			return Integer.parseInt(info.get("currUserId"));
		} catch (Exception e) {
			return -1;
		}
	}

	public int getOtherUserId() {
		try {
			return Integer.parseInt(info.get("otherUserId"));
		} catch (Exception e) {
			return -1;
		}
	}

	public int getPin() {
		try {
			return Integer.parseInt(info.get("pin"));
		} catch (Exception e) {
			return -1;
		}
	}





}
