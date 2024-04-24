package main;

import java.net.*;
import java.io.*;
import message.*;
import java.util.*;

public class ATMClient {
	Socket sock = null;
	ObjectInputStream reader = null;
	ObjectOutputStream writer = null;
	Scanner scanner = null;
	BankUser user = null;
	Map<Integer, BankAccount> accounts = null; // map of account number to account object

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

	public void withdraw() {
		try {
			scanner = new Scanner(System.in);
			System.out.println("Enter the account number: ");
			int accountNumber = scanner.nextInt();
			scanner.nextLine();
			System.out.println("Enter the amount to withdraw: ");
			double amount = scanner.nextDouble();
			scanner.nextLine();
			System.out.println("Enter the account pin: ");
			int accountPin = scanner.nextInt();
			scanner.nextLine();

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
				accounts.put(accountNumber, newAccount);
				// print out newAccount
				System.out.println("new Account: " + newAccount);
				// show updated accounts
				System.out.println("Updated accounts: " + accounts);

			} else {
				System.out.println("Fail to withdraw $" + amount + " from account " + accountNumber);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	} // end method withdraw

	public void deposit() {
		try {
			// get inputs: acc#, amount, pin
			scanner = new Scanner(System.in);
			System.out.println("Enter the account number: ");
			int accountNumber = scanner.nextInt();
			scanner.nextLine();
			System.out.println("Enter the amount to deposit: ");
			double amount = scanner.nextDouble();
			scanner.nextLine();
			System.out.println("Enter the account pin: ");
			int accountPin = scanner.nextInt();
			scanner.nextLine();

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
				System.out.println("new Account: " + newAccount);
			} else {

				System.out.println("Fail to deposit $" + amount + " to account " + accountNumber);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}


	} // end method deposit

	public void transfer() {
		try {
			// get inputs: acc#, amount, pin
			scanner = new Scanner(System.in);
			System.out.println("Enter your account number: ");
			int fromAccountNumber;
			fromAccountNumber = scanner.nextInt();
			scanner.nextLine();

			int toAccountNumber;
			double amount;

			while (true) {
				System.out.println("Enter recipient account number: ");
				toAccountNumber = scanner.nextInt();
				scanner.nextLine();
				System.out.println("Enter the amount to transfer: ");
				amount = scanner.nextDouble();
				scanner.nextLine();

				// verify from server the recipient's info
				// send an account info message to the server: int id, int currUserId, int
				// accountNumber, Status status
				AccountInfoMessage msg = new AccountInfoMessage(Status.ONGOING, toAccountNumber);
				writer.writeUnshared(msg);
				// expected an SUCCESS status of account info msg, containing all users linked
				// to this recipent's account
				AccountInfoMessage msgReceipt = (AccountInfoMessage) reader.readObject();

				System.out.println("Received Account Info Message with users: " + msgReceipt.getUsers());

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
					System.out.printf("You are transferring $%.2f to account %d, which has users %s.\n", amount,
							toAccountNumber, recipentNames);
					System.out.println("Please enter yes to confirm.");
					String userInput = scanner.nextLine();
					if (userInput.equalsIgnoreCase("YES")) {
						break; // if user confirms, break the while loop
					} else {
						continue;
					}
				}

			} // end while loop

			System.out.println("Enter the account pin: ");
			int accountPin = scanner.nextInt();
			scanner.nextLine();

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
				System.out.println("new Account: " + newAccount);
				// show updated accounts
				System.out.println("Updated accounts: " + accounts);
			} else {
				System.out.println("Fail to transfer $" + amount + " from account " + fromAccountNumber + " to account "
						+ toAccountNumber);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	} // end method transfer

	public void go() {

		try {
			// start client
			setUpConnection();
			handshake(); // handshake with server

			newSession();

			// close client
			closeConnection();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void newSession() {
		// reset variables
		user = null;
		accounts = new HashMap<Integer, BankAccount>();

		try {

			// ATM login
			while (true) {
				// create new login message
				LoginMessage loginMessage = loginRequest();
				// send login message to server
				writer.writeUnshared(loginMessage);

				// wait for loginMessage from server
				LoginMessage loginReceipt = (LoginMessage) reader.readObject();
				if ((loginReceipt.getStatus() == Status.SUCCESS)) { // if success, break while loop
					break;
				}

				System.out.println("Wrong user id or password. Please try again.\n");
			}

			// wait for bankuser object from server
			user = (BankUser) reader.readObject();
			// print out user object
			System.out.println("current user: ");
			System.out.println(user);

			getAccountsInfo();
			// print out all accounts
			System.out.println("accounts: " + accounts);

			// ATM functions
			while (true) {
				System.out.println("0-Withdraw\n1-Deposit\n2-Transfer\n3-LogOut");
				int choice = scanner.nextInt();
				switch (choice) {
				case 0:
					withdraw();
					break;
				case 1:
					deposit();
					break;
				case 2:
					transfer();
					break;
				case 3:
					LogoutMessage msg = logoutRequest();
					writer.writeUnshared(msg);
					LogoutMessage msgBack = (LogoutMessage) reader.readObject();
					if (msgBack.getStatus() == Status.SUCCESS) {
						System.out.println("Logout was a success\nReturning to Login Screen:");
						newSession();
						break;
					} else {
						System.out.println("Logout failed contiune as User ID:" + user.getId());
						break;
					}
				}
			}

			// codes go here...
			// while loop
			// two options: ATM Login, exit

			// exit

			// ATM login: while loop
			// Four options: deposit, withdraw, transfer, logout

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void getAccountsInfo() {
		try {
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

	public LoginMessage loginRequest() {
		scanner = new Scanner(System.in);
		int userId;
		while (true) { // get valid user id from user
			System.out.println("Enter user id: ");
			try {
				userId = Integer.parseInt(scanner.nextLine());
				break;
			} catch (Exception e) {
				System.out.println("Invalid user id. User id only contains digits. Please try again. \n");
			}
		}

		System.out.println("Enter your password: ");
		String password = scanner.nextLine();

		// LoginMessage(int id, String to, String from, Status status, int userId,
		// String password)
		return new LoginMessage(Status.ONGOING, userId, password);

	}

	public LogoutMessage logoutRequest() {

		return new LogoutMessage(Status.ONGOING);

	}

	public static void main(String[] args) {
		ATMClient client = new ATMClient();
		client.go();
	}
}
