package message;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class AccountMessage implements MessageInterface, Serializable {
	final Status status;
	final AccountMessageType type;
	final Map<String, String> info;


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
	
	public AccountMessage(Status status, AccountMessageType type, Map<String, String> info) {
		this.status = status;
		this.type = type;
		this.info = info;
	}
	
	// use for teller client login user account
	public AccountMessage(Status status, int userId) {
		this.status = status;
		this.type = AccountMessageType.USER_INFO;
		this.info = new HashMap<>();
		info.put("userId", Integer.toString(userId));
	}
	
	// use for create new account in teller client
	public AccountMessage(Status status, AccountType accountType, int pin) {
		this.status = status;
		this.type = AccountMessageType.ADD_ACCOUNT;
		this.info = new HashMap<>();
		info.put("accountType", accountType.toString());
		info.put("pin", Integer.toString(pin));
	}
	
	// use for delete account
	public AccountMessage(Status status, int accountNumber, int userId) {
		this.status = status;
		this.type = AccountMessageType.REM_ACCOUNT;
		this.info = new HashMap<>();
		info.put("accountNumber", Integer.toString(accountNumber));
		info.put("userId", Integer.toString(userId));
	}
	
	// use for forget password
	public AccountMessage(Status status, String birthday, String password) {
		this.status = status;
		this.type = AccountMessageType.CHG_PWD;
		this.info = new HashMap<>();
		info.put("birthday", birthday);
		info.put("password", password);
	}
	
	// use for change pin
	public AccountMessage(Status status, int userId, int accountNumber, int pin) {
		this.status = status;
		this.type = AccountMessageType.CHG_PIN;
		this.info = new HashMap<>();
		info.put("userId", Integer.toString(userId));
		info.put("accountNumber", Integer.toString(accountNumber));
		info.put("pin", Integer.toString(pin));
	}
	
	// use for transfer admin
	public AccountMessage(Status status, int userId, int accountNumber, int pin, int recipientId) {
		this.status = status;
		this.type = AccountMessageType.TXF_ADMIN;
		this.info = new HashMap<>();
		info.put("userId", Integer.toString(userId));
		info.put("accountNumber", Integer.toString(accountNumber));
		info.put("pin", Integer.toString(pin));
		info.put("recipientId", Integer.toString(recipientId));
	}
	
	// use for add user to account, delete user to account for checking account ownership
	public AccountMessage(Status status, AccountMessageType type, int userId, int accountNumber) {
		this.status = status;
		this.type = type;
		this.info = new HashMap<>();
		info.put("userId", Integer.toString(userId));
		info.put("accountNumber", Integer.toString(accountNumber));
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
