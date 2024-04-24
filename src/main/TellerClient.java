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
				// wait for success status
				// wait for BankUser object

				// add to userList and activeUsers

				// redirect to login in as user page
				// codes go here... #######
				break;
			}
		}

	}

	public void openLogs() {
		// no inputs are required from Teller
	}

	public void loginUserAccount() {
		scanner = new Scanner(System.in);
		System.out.println("Enter username: ");
		String userId = scanner.nextLine();
	}

	// !
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

		if (teller.getAdmin()) { // if this is an admin teller
			// switch statement for admin teller

		} else {
			// switch statement for normal teller
			while (true) {
				System.out.println("0-Create-New_User\n1-Login-User-Account\n2-LogOut");
				int choice = scanner.nextInt();
				switch (choice) {
				case 0: createUser(); break; // create new user
				//					case 1: id = ; break; // login user account
				//					case 2: id = ; break; // logout
				}
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