package main;

import java.io.*;
import java.util.*;
import java.net.*;
import message.*;

public class TellerClient {
	private Teller teller;
	private BankUser user;
	Socket sock = null;
	ObjectInputStream reader = null;
	ObjectOutputStream writer = null;

	Map<Integer, BankAccount> accounts = null; // map of account number to account object
	Scanner scanner = null;

	// ATM Functions
	// Withdraw
	public void withdraw() {
		try {
			// get input acc #, amount, pin
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
			WithdrawMessage msg = new WithdrawMessage(Status.ONGOING, accountNumber, amount, accountPin);
			// send msg to server
			writer.writeUnshared(msg);
			// wait for withdraw msg receipt
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

	}

	// Deposit
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
			DepositMessage msg = new DepositMessage(Status.ONGOING, accountNumber, amount,
					accountPin);
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

				System.out.println("Fail to deposit $" + amount + " to account " +
						accountNumber);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// Transfer
	public void transfer() {
		try {

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// Teller functions
	public void addTeller() {
		scanner = new Scanner(System.in);
		System.out.println("Enter name: ");
		String name = scanner.nextLine();

		scanner = new Scanner(System.in);
		System.out.println("Enter password: ");
		String pw = scanner.nextLine();
	}

	public void deleteTeller() {
		// no inputs are required from Teller
	}

	public void createUser() {
		try {
			
			while (true) {
				
				scanner = new Scanner(System.in);
				System.out.println("Enter name: ");
				String name = scanner.nextLine();
				
				scanner = new Scanner(System.in);
				System.out.println("Enter birthday: ");
				String birthday = scanner.nextLine();
				
				scanner = new Scanner(System.in);
				System.out.println("Enter password: ");
				String password = scanner.nextLine();
				
				System.out.println("Please type yes to confirm your information below.");
				System.out.printf("Name: %s\nDOB: %s\nPassword: %s\n", name, birthday, password);
				
				String userConfirm = scanner.nextLine();
				
				if (userConfirm.equalsIgnoreCase("YES")) {
					// send Account Message to server with type ADD_USER
					AccountMessage msg = new AccountMessage(Status.ONGOING, name, birthday, password);
					writer.writeUnshared(msg);
					// wait for success status
					AccountMessage msgReceipt = (AccountMessage) reader.readObject();
					if (msgReceipt.getStatus() == Status.SUCCESS) {
						// wait for BankUser object
						user = (BankUser) reader.readObject();
						System.out.println("new User info: \n" + user);
					} else {
						System.out.println("Fail to create a new user. Please try again.");
						continue;
					}
					
					// redirect to login in as user page
					loginUserAccount(user.getId());
					
					break; // break while loop
				}
			} // end while loop
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	} // end method createUser

	public void openLogs() {
		// no inputs are required from Teller
	}

	public void loginUserAccount() {
		try {
			int userId;
			while (true) {
				
				scanner = new Scanner(System.in);
				System.out.println("Enter user id: ");
				userId = scanner.nextInt();
				scanner.nextLine();
				
				// send AccountMessage with type USER_INFO to get BankUser object
				AccountMessage msg = new AccountMessage(Status.ONGOING, userId);
				writer.writeUnshared(msg);
				// wait for success status
				AccountMessage msgReceipt = (AccountMessage) reader.readObject();
				if (msgReceipt.getStatus() == Status.SUCCESS) {
					// wait for BankUser obj
					user = (BankUser) reader.readObject();
					userId = user.getId();
					break; // break while loop
				} else {
					System.out.printf("User id %d does not exist. Please try again.\n", userId);
				}
			} // end while loop
			
			getAccountsInfo(); // get all accounts
			System.out.println("accounts: " + accounts);
			loginUserAccount(userId);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void loginUserAccount(int userId) {
		
		try {
			boolean isLoggedOut = false;
			while (!isLoggedOut) {
				System.out.println("0-Create-New-Account\n1-Remove-Account\n2-Add-User-to-Account\n"
						+ "3-Remove-User-from-Account\n4-Transfer-Admin\n5-Change-Pin\n"
						+ "6-Forget-Password\n7-Withdraw\n8-Deposit\n9-Transfer\n10-Back");
				int choice = scanner.nextInt();
				switch (choice) {
					case 0: break;
					case 1: break;
					case 2: break;
					case 3: break;
					case 4: break;
					case 5: break;
					case 6: break;
					case 7: withdraw(); break; // withdraw
					case 8: deposit(); break; // deposit
					case 9: transfer(); break; // transfer
					case 10: isLoggedOut = true; break; // going back
					default: break;
				}
			}
			
			
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
	
	public void checkTeller() {
		// needs clarification
	}

	public void createAccount() {
		// no inputs are required from Teller
	}

	public void deleteAccount() {
		scanner = new Scanner(System.in);
		System.out.println("Enter user id: ");
		String userId = scanner.nextLine();

	}

	public void addUser() {
		scanner = new Scanner(System.in);
		System.out.println("Enter user id: ");
		String userId = scanner.nextLine();

	}

	public void deleteUser() {
		scanner = new Scanner(System.in);
		System.out.println("Enter user id: ");
		String userId = scanner.nextLine();

	}

	public void forgetPassword() {
		scanner = new Scanner(System.in);
		System.out.println("Enter birthday: ");
		String bd = scanner.nextLine();

		System.out.println("Enter new password: ");
		String pd = scanner.nextLine();

	}

	public void changePin() {
		scanner = new Scanner(System.in);
		System.out.println("Enter new pin: ");
		int num = scanner.nextInt();

	}

	public void transferAdmin() {

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

	public void handshake() {
		try {
			// public HelloMessage(int id, String text, String to, String from, MessageType
			// type, Status status)
			HelloMessage clientHello = new HelloMessage("Teller", Status.ONGOING);
			writer.writeObject(clientHello);
			HelloMessage serverHello = (HelloMessage) reader.readObject();
			if (serverHello.getStatus() == Status.SUCCESS) {
				System.out.println("client-server handshake successfully");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void tellerLogin() {
		scanner = new Scanner(System.in);
		int tellerId;
		String password;
		while (true) {
			while (true) {
				System.out.println("Enter your teller id: ");
				try {
					tellerId = scanner.nextInt();
					scanner.nextLine();
					break;
				} catch (Exception e) {
					System.out.println("Error: your teller id is an integer. Please try again.");
				}
			}

			System.out.println("Enter your password: ");
			password = scanner.nextLine();

			// create LoginMessage
			LoginMessage msg = new LoginMessage(Status.ONGOING, tellerId, password);

			try {
				// send LoginMessage to server
				writer.writeUnshared(msg);

				// expect a success LoginMessage returned from the server
				LoginMessage msgReceipt = (LoginMessage) reader.readObject();

				if (msgReceipt.getStatus() == Status.SUCCESS) {
					// wait for Teller object
					teller = (Teller) reader.readObject();
					System.out.println(teller);
					break; // break the while loop
				} else {
					System.out.println("Wrong teller id or password. Please try again.");
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	public void newSession() {

		tellerLogin();
		scanner = new Scanner(System.in);
		accounts = new HashMap<>();
//		if (teller.getAdmin()) { // if this is an admin teller
//			// switch statement for admin teller
//
//		} else {}
		// switch statement for normal teller
		boolean flag = false; // flag to quit the session
		while (!flag) {
			System.out.println("0-Create-New_User\n1-Login-User-Account\n2-LogOut");
			int choice = scanner.nextInt();
			switch (choice) {
				case 0: createUser(); break; // create new user
				case 1: loginUserAccount(); break;
				case 2: flag = true; break;
				default: break;
			}
		}
		   
		// switch statement

	}

	public void go() {
		try {
			// start client
			setUpConnection();

			int id = 0;
			// handshake with server: Teller client hello
			handshake();

			newSession();

			// close client
			closeConnection();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// Driver
	public static void main(String[] args) {
		// create new tellerclient
		TellerClient client = new TellerClient();
		client.go();

	}
}