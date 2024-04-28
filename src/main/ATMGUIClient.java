package main;

import java.net.*;
import java.io.*;
import message.*;
import java.util.*;

public class ATMGUIClient {
	Socket sock = null;
	ObjectInputStream reader = null;
	ObjectOutputStream writer = null;
	Scanner scanner = null;
	public BankUser user = null;
	public List<Integer> users = null;
	public Map<Integer, BankAccount> accounts = null; // map of account number to account object
	
	
	public Map<Integer, BankAccount> getAccounts() {
		return this.accounts;
	}
	public void setAccounts(Map accounts) {
		this.accounts = accounts;
	}
	public BankUser getUser() {
		return this.user;
	}
	public void setUser(BankUser user) {
		this.user = user;
	}
	public List<Integer> getUsers() {
		return this.users;
	}
	public void setUsers(List<Integer> users) {
		this.users = users;
	}

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

	public String withdraw(int accountNumber, double amount,  int accountPin) {
		try {

			// create a withdraw message
			// int id, Status status, int accountNumber, double withdrawAmount, int pin
			WithdrawMessage msg = new WithdrawMessage(Status.ONGOING, accountNumber, amount, accountPin);
			// send message to server
			writer.writeUnshared(msg);
			// wait for withdraw message receipt
			WithdrawMessage msgReceipt = (WithdrawMessage) reader.readObject();

			if (msgReceipt.getStatus() == Status.SUCCESS) {
				// expect a new Account object
				BankAccount newAccount = (BankAccount) reader.readObject();
				// update accounts
				accounts.replace(accountNumber, newAccount);
				setAccounts(accounts);
				// print out newAccount
				return "new Account: " + newAccount + "\n\n" + "Updated accounts: " + accounts;
				// show updated accounts

			} else {
				return "Fail to withdraw $" + amount + " from account " + accountNumber;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "ERROR";

	} // end method withdraw

	public String deposit(int accountNumber, double amount,  int accountPin) {
		try {
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
				return "new Account: " + newAccount;
			} else {
				return "Fail to deposit $" + amount + " to account " + accountNumber;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "ERROR";

	} // end method deposit

	public String transfer1(int fromaccountNumber, double amount, int toAccountNumber) {
		try {
			// get inputs: acc#, amount, pin
			List<Integer> toAccountUsers = null;
			// verify from server the recipient's info
			// send an account info message to the server: int id, int currUserId, int
			// accountNumber, Status status
			AccountInfoMessage msg = new AccountInfoMessage(Status.ONGOING, toAccountNumber);
			writer.writeUnshared(msg);
			// expected an SUCCESS status of account info msg, containing all users linked
			// to this recipent's account
			AccountInfoMessage msgReceipt = (AccountInfoMessage) reader.readObject();
			toAccountUsers = msgReceipt.getUsers();
			setUsers(toAccountUsers);
			String out = "Received Account Info Message with users: " + msgReceipt.getUsers() + "\n";

			// init list of string to store user names
			ArrayList<String> recipentNames = new ArrayList<>();

			if (msgReceipt.getStatus() == Status.SUCCESS) {
				// for each user of this account
				for (int userId : msgReceipt.getUsers()) {
					// send user info msg to server
					UserInfoMessage uiMsg = new UserInfoMessage(Status.ONGOING, userId);
					writer.writeUnshared(uiMsg);
					// expected SUCCESS of user info msg, which contains user name
					UserInfoMessage uiMsgReceipt = (UserInfoMessage) reader.readObject();

					if (uiMsgReceipt.getStatus() == Status.SUCCESS) {
						recipentNames.add(uiMsgReceipt.getUserName()); // add user name to list
					}
				}
				// show the confirmation msg with all the user names and amount of money
				out = out + String.format("You are transferring $%.2f to account %d, which has users %s.\n", amount,
						toAccountNumber, recipentNames);
				out = out + "\nPlease enter yes to confirm. No to try again. Anything to cancel";
				return out;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	} // end method transfer

	public String tranfer2(int fromAccountNumber, double amount, int toAccountNumber, int accountPin){
		try {
			// create a transfer message
			// int id, Status status, int accountNumber, double transferAmount, int pin
			TransferMessage txMsg = new TransferMessage(Status.ONGOING, fromAccountNumber, toAccountNumber, amount,
					accountPin);
			// send a message to server
			writer.writeUnshared(txMsg);
		
			// wait for a transfer message receipt
			TransferMessage txMsgReceipt = (TransferMessage) reader.readObject();
		
			if (txMsgReceipt.getStatus() == Status.SUCCESS) {
				// expect a new Account object
				BankAccount newAccount = (BankAccount) reader.readObject();
				// update accounts
				accounts.put(fromAccountNumber, newAccount);
				// print out newAccount
				String out = "new Account: " + newAccount;
				if (getUsers().contains(user.getId())) { // in case transfer between your accounts
					// expect another bankaccount obj
					BankAccount recipientAccount = (BankAccount) reader.readObject();
					accounts.put(toAccountNumber, recipientAccount);
				}
				// show updated accounts
				return out + "\nUpdated accounts: " + accounts;
			} else {
				return "Fail to transfer $" + amount + " from account " + fromAccountNumber + " to account "
						+ toAccountNumber;
			}
		}catch (Exception e) {
			e.printStackTrace();}
		return "";
	}
	
	public void go() {

		try {
			// start client
			setUpConnection();
			handshake(); // handshake with server

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void newSession() {
		// reset variables
		user = null;
		accounts = new HashMap<Integer, BankAccount>();

	}

	public void getAccountsInfo() {
		user = null;
		accounts = new HashMap<Integer, BankAccount>();
		
		try {
			// wait for bankuser object from server
			user = (BankUser) reader.readObject();
			setUser(user);
			// print out user object
			System.out.println("current user: ");
			System.out.println(user);
			// request info from all accounts of current user
			for (int accountNumber : user.getAccounts()) {
				if (accounts == null || accounts.get(accountNumber) == null) { // update only when it's not available

					// create new AccountMessage requesting account info
					// int id, Status status, int accountNumber, int currUserId, int pin,
					// AccountMessageType type
					AccountMessage msg = new AccountMessage(Status.ONGOING, accountNumber, user.getId(), -1,
							AccountMessageType.ACCOUNT_INFO);
					// send message
					writer.writeObject(msg);
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
			}
			setAccounts(accounts);
		} catch (Exception e) {
			e.printStackTrace();
		}


	} // end method getAccountsInfo

	public void handshake() {
		// handshake with server: ATM client hello
		// public HelloMessage(int id, String text, String to, String from, MessageType
		// type, Status status)
		HelloMessage clientHello = new HelloMessage("ATM", Status.ONGOING);
		try {
			// send message
			writer.writeObject(clientHello);
			// wait for message receipt
			HelloMessage serverHello = (HelloMessage) reader.readObject();
			if (serverHello.getStatus() == Status.SUCCESS) {
				System.out.println("client-server handshake successfully");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	} // end method handshake

	
	
	
	public String loginRequest(String id, String password) {
		boolean isInt;
		try {
            Integer.parseInt(id); // Try parsing the string to an integer
            isInt = true; // If successful, return true
        } catch (NumberFormatException e) {
        	isInt = false; // If an exception is caught, return false
        }
		if(isInt) {
			try {
				// create new login message
				LoginMessage loginMessage = new LoginMessage(Status.ONGOING, Integer.parseInt(id), password);
				// send login message to server
				writer.writeUnshared(loginMessage);
		
				// wait for loginMessage from server
				LoginMessage loginReceipt = (LoginMessage) reader.readObject();
				if ((loginReceipt.getStatus() == Status.SUCCESS)) { // if success, break while loop
					return "SUCCESS";
				}
				else {
					return "ONGOING";
	//				System.out.println("Wrong user id or password. Please try again.\n");
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "ERROR";
	}

	private boolean isInteger(String id) {
		// TODO Auto-generated method stub
		return false;
	}

	public String logoutRequest() {
		try {
			LogoutMessage msg = new LogoutMessage(Status.ONGOING);
			writer.writeUnshared(msg);
			LogoutMessage msgBack = (LogoutMessage) reader.readObject();
			if (msgBack.getStatus() == Status.SUCCESS) {
				return "SUCCESS";
			} else {
				return "ONGOING";
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return "ERROR";
	}

	public static void main(String[] args) {
		ATMGUIClient client = new ATMGUIClient();
		client.go();
	}
}
