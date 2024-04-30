package main;

import java.io.*;
import java.util.*;
import java.net.*;
import message.*;

public class TellerGUIClient {
	private Teller teller;
	private BankUser user;
	Socket sock = null;
	ObjectInputStream reader = null;
	ObjectOutputStream writer = null;
	Map<Integer, BankAccount> accounts = null; // map of account number to account object
	Scanner scanner = null;

	public TellerGUIClient() {
		accounts = new HashMap<>();
	}
	public void setTeller(Teller teller) {
		this.teller = teller;
	}

	public Teller getTeller() {
		return this.teller;
	}

	public void setUser(BankUser user) {
		this.user = user;
	}

	public BankUser getUser() {
		return this.user;
	}

	public Map<Integer, BankAccount> getAccounts() {
		return this.accounts;
	}

	// ATM Functions
	// Withdraw
	public String withdraw(int accNum, double amount, int accPin) {
		try {
			int loggedInUserID = user.getId();
			
			// create a withdraw message
			WithdrawMessage msg = new WithdrawMessage(Status.ONGOING, accNum, amount, accPin);
			// send msg to server
			writer.writeUnshared(msg);
			// wait for withdraw msg receipt
			WithdrawMessage msgReceipt = (WithdrawMessage) reader.readObject();
			if (msgReceipt.getStatus() == Status.SUCCESS) {
				// expect a new Account object
				BankAccount newAccount = (BankAccount) reader.readObject();
				// update accounts
				accounts.put(accNum, newAccount);
				// print out newAccount
				return "new Account: " + newAccount + "\n\n" + "Updated accounts: " + accounts;
			
			} else {
				return "Fail to withdraw $" + amount + " from account " + accNum;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "ERROR";
	} // end method withdraw

	// Deposit
	public String deposit(int accountNumber, double amount, int accountPin) {
		try {
			int loggedInUserID = user.getId();
			// create a deposit message
			// int id, Status status, int accountNumber, double depositAmount, int pin
			DepositMessage msg = new DepositMessage(Status.ONGOING, accountNumber, amount, accountPin);
			// send a message to server
			writer.writeUnshared(msg);
			// wait for a deposit message receipt
			DepositMessage msgReceipt = (DepositMessage) reader.readObject();
			if (msgReceipt.getStatus() == Status.SUCCESS) {
				// expect a new Account object
				BankAccount newAccount = (BankAccount) reader.readObject();
				// update accounts
				accounts.replace(accountNumber, newAccount);
				// print out result
				return "New Account: " + newAccount;
			} else {

				return "Fail to deposit $" + amount + " to account " + accountNumber;

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "ERROR";
	} // end method deposit
	// Transfer
	
	
	public List<String> getRecipientNames(int toAccountNumber) {
		
		List<Integer> toAccountUsers = null;
		ArrayList<String> recipentNames = new ArrayList<>();
		// verify from server the recipient's info
		// send an account info message to the server: int id, int currUserId, int
		// accountNumber, Status status
		try {
			AccountInfoMessage msg = new AccountInfoMessage(Status.ONGOING, toAccountNumber);
			writer.writeUnshared(msg);
			// expected an SUCCESS status of account info msg, containing all users linked
			// to this recipent's account
			AccountInfoMessage msgReceipt = (AccountInfoMessage) reader.readObject();
			toAccountUsers = msgReceipt.getUsers();
			System.out.println("Received Account Info Message with users: " + msgReceipt.getUsers());
			// init list of string to store user names
			
			if (msgReceipt.getStatus() == Status.SUCCESS) {
				// for each user of this account
				for (int userId : toAccountUsers) {
					// send user info msg to server
					UserInfoMessage uiMsg = new UserInfoMessage(Status.ONGOING, userId);
					writer.writeUnshared(uiMsg);
					// expected SUCCESS of user info msg, which contains user name
					UserInfoMessage uiMsgReceipt = (UserInfoMessage) reader.readObject();
					if (uiMsgReceipt.getStatus() == Status.SUCCESS) {
						recipentNames.add(uiMsgReceipt.getUserName()); // add user name to list
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return recipentNames;
	}
	
	public String transfer(int fromAccountNumber, int toAccountNumber, double amount, int pin) {
		List<Integer> toAccountUsers = null;
		
		try {
			// get toAcccountUsers
			AccountInfoMessage msg = new AccountInfoMessage(Status.ONGOING, toAccountNumber);
			writer.writeUnshared(msg);
			// expected an SUCCESS status of account info msg, containing all users linked
			// to this recipent's account
			AccountInfoMessage msgReceipt = (AccountInfoMessage) reader.readObject();
			toAccountUsers = msgReceipt.getUsers();
			
			// create a transfer message
			// int id, Status status, int accountNumber, double transferAmount, int pin
			TransferMessage txMsg = new TransferMessage(Status.ONGOING, fromAccountNumber, toAccountNumber, amount, pin);
			// send a message to server
			writer.writeUnshared(txMsg);
			// wait for a transfer message receipt
			TransferMessage txMsgReceipt = (TransferMessage) reader.readObject();
			if (txMsgReceipt.getStatus() == Status.SUCCESS) {
				// expect a new Account object
				BankAccount newAccount = (BankAccount) reader.readObject();
				// update accounts
				accounts.put(fromAccountNumber, newAccount);

				if (toAccountUsers.contains(user.getId())) { // in case transfer between your accounts
					// expect another bankaccount obj
					BankAccount recipientAccount = (BankAccount) reader.readObject();
					accounts.put(toAccountNumber, recipientAccount);
				}
				return "New Account: " + newAccount;
				
			
			} else {
				return "Fail to transfer $" + amount + " from account " + fromAccountNumber + " to account "
						+ toAccountNumber;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "ERROR";
	} // end of transfer method

	public String addTeller(String name, String password) {

		// create new teller message
		TellerMessage msg = new TellerMessage(Status.ONGOING, name, password);
		try {
			// send message to server
			writer.writeUnshared(msg);
			// wait for success status
			TellerMessage msgReceipt = (TellerMessage) reader.readObject();
			if (msgReceipt.getStatus() == Status.SUCCESS) {
				System.out.println("A new teller account is created successfully.");
				// receive a Teller obj
				Teller newTeller = (Teller) reader.readObject();

				return "New teller: \n" + newTeller;

			} else {
				return "Fail to create a new teller account.";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "ERROR";

	} // end method addTeller

	public Map<Integer, String> getTellersInfo() {
		Map<Integer, String> tellersInfo = new HashMap<>();
		TellerMessage msg = new TellerMessage(Status.ONGOING, TellerMessageType.TELLERS_INFO);
		try {
			// send teller message with type TELLERS_INFO
			writer.writeUnshared(msg);
			// wait for success status
			TellerMessage msgReceipt = (TellerMessage) reader.readObject();
			if (msgReceipt.getStatus() == Status.SUCCESS) {
				Map<String, String> info = msgReceipt.getInfo();
				for (String key : info.keySet()) {
					// add info to tellersInfo
					tellersInfo.put(Integer.parseInt(key), info.get(key)); // tellerId -> teller name
				}
			} else { // status error
				System.out.println("Fail to get all tellers info.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tellersInfo;
	} // end method getTellersInfo

	public Map<Integer, String> getNonAdminTellers() {

		// get the list of tellers: tellerId + name
		Map<Integer, String> tellersInfo = getTellersInfo();
		Map<Integer, String> output = new HashMap<>();
		// show only non-admin tellers
		System.out.println("List of non-admin tellers: \ntellerId:teller name");
		for (int tellerId : tellersInfo.keySet()) {
			if (teller.getAdmin() && tellerId != teller.getId()) {
				output.put(tellerId, tellersInfo.get(tellerId));
			}
		}

		return output;
	}

	public String deleteTeller(int removeTellerId) {

		TellerMessage msg = new TellerMessage(Status.ONGOING, removeTellerId);

		try {
			// send TellerMessage to server
			writer.writeUnshared(msg);
			// wait for msgReceipt
			TellerMessage msgReceipt = (TellerMessage) reader.readObject();
			if (msgReceipt.getStatus() == Status.SUCCESS) {

				return "Successfully remove teller with id: " + removeTellerId;

			} else {
				return "Fail to remove teller with id: " + removeTellerId;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "ERROR";

	} // end method deleteTeller

	public String addUserToAccount(int accountNumber, int userIdAdd) {
		try {

			int loggedInUserID = user.getId();
			// check user id is valid.
			// create UserInfoMessage and send to server to check
			UserInfoMessage uiMsg = new UserInfoMessage(Status.ONGOING, userIdAdd);
			writer.writeUnshared(uiMsg);
			// wait for a user info message receipt
			UserInfoMessage uiMsgReceipt = (UserInfoMessage) reader.readObject();
			// check if the logged in user owns the account.
			AccountMessage accMsg = new AccountMessage(Status.ONGOING, AccountMessageType.CHK_OWN,
					loggedInUserID, accountNumber);
			writer.writeUnshared(accMsg);
			// wait for a user info message receipt
			AccountMessage accMsgReceipt = (AccountMessage) reader.readObject();
			// send accMsg to check admin status
			AccountMessage accMsgAdmin = new AccountMessage(Status.ONGOING, AccountMessageType.CHK_ACC_ADM,
					loggedInUserID, accountNumber);
			writer.writeUnshared(accMsgAdmin);
			// wait for accMsgAdmin receipt
			AccountMessage accMsgAdminReceipt = (AccountMessage) reader.readObject();

			// check if the user id is already in the account
			AccountMessage acmChkDup = new AccountMessage(Status.ONGOING, AccountMessageType.CHK_DUP,
					loggedInUserID, accountNumber, userIdAdd);
			writer.writeUnshared(acmChkDup);
			// wait for a user info message receipt
			AccountMessage acmChkDupReceipt = (AccountMessage) reader.readObject();

			if (uiMsgReceipt.getStatus() == Status.SUCCESS && accMsgReceipt.getStatus() == Status.SUCCESS

					&& accMsgAdminReceipt.getStatus() == Status.SUCCESS
					&& acmChkDupReceipt.getStatus() == Status.SUCCESS) {

				accMsg = new AccountMessage(Status.ONGOING, AccountMessageType.ADD_USER_TO_ACC, userIdAdd,
						accountNumber);

				writer.writeUnshared(accMsg);
				// expect BankUser Object from server
				BankUser userToadd = (BankUser) reader.readObject();
				// add user to accounts
				accounts.get(accountNumber).addUser(userToadd);
				// show updated accounts
				return "Successfully added user id [" + userIdAdd + "] to the account #" + accountNumber;

			} else { 
				return "Fail to add user id [" + userIdAdd + "] to the account #" + accountNumber;
			}
			

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "ERROR";
	} // end method addUserToAccount

	public String deleteUserFromAccount(int accNum, int userIdRem) {
		try {
			
			int loggedInUserID = user.getId();
				
					// check if the logged in user owns the account.
					AccountMessage acmOwn = new AccountMessage(Status.ONGOING, AccountMessageType.CHK_OWN,
							loggedInUserID, accNum);
					writer.writeUnshared(acmOwn);
					// wait for a user info message receipt
					AccountMessage acmReceipt = (AccountMessage) reader.readObject();
					// check if removing user owns the account.
					AccountMessage acmRem = new AccountMessage(Status.ONGOING, AccountMessageType.CHK_OWN, userIdRem,
							accNum);
					writer.writeUnshared(acmRem);
					// wait for a user info message receipt
					AccountMessage acmRemReceipt = (AccountMessage) reader.readObject();
					// send accMsg to check admin status
					AccountMessage accMsgAdmin = new AccountMessage(Status.ONGOING, AccountMessageType.CHK_ACC_ADM,
							loggedInUserID, accNum);
					writer.writeUnshared(accMsgAdmin);
					// wait for accMsgAdmin receipt
					AccountMessage accMsgAdminReceipt = (AccountMessage) reader.readObject();

					// check if the user is trying to delete itself from account
					AccountMessage acmChkItself = new AccountMessage(Status.ONGOING, AccountMessageType.CHK_ITSELF,
							loggedInUserID, accNum, userIdRem);
					writer.writeUnshared(acmChkItself);
					// wait for a user info message receipt
					AccountMessage acmChkItselfReceipt = (AccountMessage) reader.readObject();

					// check both msgs' status
					if (acmReceipt.getStatus() == Status.SUCCESS && acmRemReceipt.getStatus() == Status.SUCCESS
							&& accMsgAdminReceipt.getStatus() == Status.SUCCESS
							&& acmChkItselfReceipt.getStatus() == Status.SUCCESS) {

						acmRem = new AccountMessage(Status.ONGOING, AccountMessageType.REM_USER_FROM_ACC, userIdRem,
								accNum);

						writer.writeUnshared(acmRem);
						// expect BankUser Object from server
						BankUser userRem = (BankUser) reader.readObject();
						// remove user to accounts
						accounts.get(accNum).deleteUser(userRem);
						// show updated accounts
						return "Successfully removed user id [" + userIdRem + "] from the account #" + accNum;
					} else {
						return "Fail to delete user id [" + userIdRem + "] from the account #" + accNum;
					}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "ERROR";
	} // end method deleteUserFromAccount

	public String createUser(String name, String birthday, String password) {
		try {

			// send Account Message to server with type ADD_USER
			AccountMessage msg = new AccountMessage(Status.ONGOING, name, birthday, password);
			writer.writeUnshared(msg);
			// wait for success status
			AccountMessage msgReceipt = (AccountMessage) reader.readObject();
			if (msgReceipt.getStatus() == Status.SUCCESS) {
				// wait for BankUser object
				user = (BankUser) reader.readObject();
				setUser(user);
				return "SUCCESS";
			} else {
				return "Fail to create a new user. Please try again.";
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Fail";
	} // end method createUser

	public List<String> getLogs() {

		TellerMessage msg = new TellerMessage(Status.ONGOING, TellerMessageType.VIEW_LOGS);
		TellerMessage msgReceipt = null;
		List<String> logs = null;
		try {
			// send msg to server
			writer.writeUnshared(msg);
			// wait for success status
			msgReceipt = (TellerMessage) reader.readObject();
			if (msgReceipt.getStatus() == Status.SUCCESS) {
				logs = msgReceipt.getLogs();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return logs;
	} // end method getLogs

	public String loginUserAccount(int userId) {

		// send AccountMessage with type USER_INFO to get BankUser object
		AccountMessage msg = new AccountMessage(Status.ONGOING, userId);
		try {
			writer.writeUnshared(msg);
			// wait for success status
			AccountMessage msgReceipt = (AccountMessage) reader.readObject();
			if (msgReceipt.getStatus() == Status.SUCCESS) {
				// wait for BankUser obj
				user = (BankUser) reader.readObject();
				return null;
			} else {
				return String.format("User id %d does not exist. Please try again.\n", userId);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "ERROR";
	}

	public void getAccountsInfo() {
		try {
			// request info from all accounts of current user
			for (int accountNumber : user.getAccounts()) {

				// create new AccountMessage requesting account info
				// int id, Status status, int accountNumber, int currUserId, int pin,
				// AccountMessageType type
				AccountMessage msg = new AccountMessage(Status.ONGOING, accountNumber, user.getId(), -1,
						AccountMessageType.ACCOUNT_INFO);
				// send message
				writer.writeUnshared(msg);
				// wait for message receipt
				AccountMessage msgReceipt = (AccountMessage) reader.readObject();
				if (msgReceipt.getStatus() == Status.SUCCESS) {
					// wait for BankAccount object
					BankAccount acc = (BankAccount) reader.readObject();
					// add to accounts
					accounts.put(accountNumber, acc);
				} else {
					accounts.put(accountNumber, null);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	} // end method getAccountsInfo

	public void checkTeller() {
		// needs clarification
	}

	public String checkPin(String pin) {
		boolean isInt;

		try {
			Integer.parseInt(pin); // Try parsing the string to an integer
			isInt = true; // If successful, return true
		} catch (NumberFormatException e) {
			isInt = false; // If an exception is caught, return false
		}
		if (isInt) {
			return "VALID";
		} else {
			return "INVALID";
		}
	}

	public String createAccount(int loggedInUserId, int pin, AccountType accountType) {
		try {
			// send AccountMessage of to server: Status status, AccountType accountType, int
			// pin
			AccountMessage msg = new AccountMessage(Status.ONGOING, accountType, pin);
			writer.writeUnshared(msg);
			// wait for success status and new Bank Account number
			AccountMessage msgReceipt = (AccountMessage) reader.readObject();
			if (msgReceipt.getStatus() == Status.SUCCESS) {
				int accountNumber = Integer.parseInt(msgReceipt.getInfo().get("accountNumber"));
				user.getAccounts().add(accountNumber);
				// getAccountsInfo to get the newly created account to accounts
				getAccountsInfo();
				// print out all account info
				return "SUCCESS";
			} else {
				return "Fail";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Fail";
	} // end method createAccount

	// delete account if the balance is zero, else fail to delete
	public String deleteAccount(int accountNumber ) {
		// check if this account number is valid and account balance is not zero and
		// whether the user has admin access to this account
			// send AccountMessage to server
			AccountMessage msg = new AccountMessage(Status.ONGOING, accountNumber, user.getId());
			try {
				writer.writeUnshared(msg);
				// wait for success status
				AccountMessage msgReceipt = (AccountMessage) reader.readObject();
				if (msgReceipt.getStatus() == Status.SUCCESS) {
					// update accounts, user.accounts
					user.getAccounts().remove(Integer.valueOf(accountNumber));
					accounts.remove(accountNumber);
					// print out success msg
					String out = "Successfully remove account #" + accountNumber + ".";
					// print out all account info
					return out + "\nUpdated user id [" + user.getId() + "] accounts: " + accounts;
				}else {
					return "Delete Account Failed";
				}
			} catch (Exception e) {
				e.printStackTrace();
			}return "Delete Account Failed";
	} // end method deleteAccount


	public String forgetPassword(String birthday, String password) {
		
		try {
			// create new AccountMessage with type CHG_PWD
			AccountMessage msg = new AccountMessage(Status.ONGOING, birthday, password);
			// send to server
			writer.writeUnshared(msg);
			// wait for success status
			AccountMessage msgReceipt = (AccountMessage) reader.readObject();
			if (msgReceipt.getStatus() == Status.SUCCESS) {
				return "Your password is changed successfully.";
			} else {
				return "Wrong birthday. Fail to change your password.";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "ERROR";
	}

	public String changePin(int accountNumber, int pin) {
		
		try {
			// create AccountMessage with type CHG_PIN to server
			// Status status, int userId, int accountNumber, int pin
			AccountMessage msg = new AccountMessage(Status.ONGOING, user.getId(), accountNumber, pin);
			// send to server
			writer.writeUnshared(msg);
			// wait for success status
			AccountMessage msgReceipt = (AccountMessage) reader.readObject();
			if (msgReceipt.getStatus() == Status.SUCCESS) {
				// update account pin locally (optional)
				accounts.get(accountNumber).setAccountPin(pin);
				return "Your pin is changed successfully.";
			} else {
				return "Fail to change your pin.";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "ERROR";
	} // end method changePin
	
	public Map<Integer, Map<Integer, String>> getAdminAccountsInfo() {
		// 1. get latest updates of all accounts
		getAccountsInfo();
		// initialize map: key: accountNumber -> value: Map<userId, user name>
		Map<Integer, Map<Integer, String>> adminAccountsInfo = new HashMap<>();
		// 1.1 for all accounts:
		for (int accountNumber : user.getAccounts()) {
			// if it is admin account (userId: username)
			if (accounts.get(accountNumber).getAdminID() == user.getId()) {
				BankAccount tempAccount = accounts.get(accountNumber);
				// for each user:
				for (int userId : tempAccount.getUsers()) {
					// send user info msg to server
					UserInfoMessage uiMsg = new UserInfoMessage(Status.ONGOING, userId);
					try {
						writer.writeUnshared(uiMsg);
						// expected SUCCESS of user info msg, which contains user name
						UserInfoMessage uiMsgReceipt = (UserInfoMessage) reader.readObject();
						if (uiMsgReceipt.getStatus() == Status.SUCCESS) {
							if (!adminAccountsInfo.containsKey(accountNumber)) {
								adminAccountsInfo.put(accountNumber, new HashMap<>());
							}
							// add username to adminAccountsInfo
							adminAccountsInfo.get(accountNumber).put(userId, uiMsgReceipt.getUserName());
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} // end for loop
			}
		}
		return adminAccountsInfo;
	}
	
	public String transferAdmin(int accountNumber, int pin, int recipientId, String recipientName) {
		
		// send transfer admin request to server -> AccountMessage of type TXF_ADMIN
		// Status status, int userId, int accountNumber, int pin, int recipientId
		AccountMessage msg = new AccountMessage(Status.ONGOING, user.getId(), accountNumber, pin, recipientId);
		try {
			// send to server
			writer.writeUnshared(msg);
			// wait for success status
			AccountMessage msgReceipt = (AccountMessage) reader.readObject();
			if (msgReceipt.getStatus() == Status.SUCCESS) {
				// update account pin locally (optional)
				accounts.get(accountNumber).setAccountPin(pin);
				// 6. update account admin locally
				accounts.get(accountNumber).setAdminID(recipientId);
				return String.format("For you account %d, the admin is transferred to %s successfully.\n", accountNumber, recipientName);
			} else {
				return String.format("For you account %d, failed to transfer admin to %s.\n", accountNumber, recipientName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "ERROR";

	} // end method transferAdmin

	private boolean setUpConnection() {
		try {
			sock = new Socket("127.0.0.1", 50000);
			System.out.println("Connected to 127.0.0.1:50000.");
			// set up reader and writer
			writer = new ObjectOutputStream(sock.getOutputStream());
			reader = new ObjectInputStream(sock.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Fail to connect to 127.0.0.1:50000.");
		}
		return (sock != null);
	}

	private boolean closeConnection() {
		try {
			// close reader and writer
			reader.close();
			writer.close();
			// close socket
			sock.close();
			sock = null;
			System.out.println("Socket to 127.0.0.1:50000 is closed successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Fail to close Socket to 127.0.0.1:50000.");
		}
		return (sock == null);
	}

	public void handshake() {
		try {
			// public HelloMessage(int id, String text, String to, String from, MessageType
			// type, Status status)
			HelloMessage clientHello = new HelloMessage("Teller", Status.ONGOING);
			writer.writeUnshared(clientHello);
			HelloMessage serverHello = (HelloMessage) reader.readObject();
			if (serverHello.getStatus() == Status.SUCCESS) {
				System.out.println("client-server handshake successfully");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String tellerLoginRequest(String tellerId, String password) {
		boolean isInt;
		accounts = new HashMap<>();
		try {

			Integer.parseInt(tellerId); // Try parsing the string to an integer
			isInt = true; // If successful, return true
		} catch (NumberFormatException e) {
			isInt = false; // If an exception is caught, return false
		}
		if (isInt) {

			try {
				// create new login message
				LoginMessage msg = new LoginMessage(Status.ONGOING, Integer.parseInt(tellerId), password);
				// send login message to server
				writer.writeUnshared(msg);

				// wait for loginMessage from server
				LoginMessage msgReceipt = (LoginMessage) reader.readObject();
				if ((msgReceipt.getStatus() == Status.SUCCESS)) { // if success, break while loop
					Teller tell = (Teller) reader.readObject();
					setTeller(tell);
					return "SUCCESS";
				} else {
					return "ONGOING";
					// System.out.println("Wrong user id or password. Please try again.\n");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "ERROR";
	}

	public void go() {
		try {
			// start client
			setUpConnection();
			// handshake with server: Teller client hello
			handshake();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String logoutRequest() {
		try {
			LogoutMessage logoutMsg = new LogoutMessage(Status.ONGOING, LogoutMessageType.TELLER);
			writer.writeUnshared(logoutMsg);
			LogoutMessage logoutMsgReceipt = (LogoutMessage) reader.readObject();
			if (logoutMsgReceipt.getStatus() == Status.SUCCESS) {
				return "SUCCESS"; // "Logout was a success.\nReturning to teller login.\n";
			} else {
				return "Logout failed.";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Fail";
	}

	// Driver
	public static void main(String[] args) {
		// create new tellerclient
		TellerGUIClient client = new TellerGUIClient();
		client.go();
	}
}