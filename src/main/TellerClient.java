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
	Scanner scan = null;


	// Teller functions
	public void addTeller() {
		scan = new Scanner(System.in);
		System.out.println("Enter name: ");
		String name = scan.nextLine();

		scan = new Scanner(System.in);
		System.out.println("Enter password: ");
		String pw = scan.nextLine();
	}
	public void deleteTeller() {
		// no inputs are required from Teller
	}
	public void createUser() {
		scan = new Scanner(System.in);
		System.out.println("Enter name: ");
		String name = scan.nextLine();

		scan = new Scanner(System.in);
		System.out.println("Enter birthday: ");
		String bday = scan.nextLine();

		scan = new Scanner(System.in);
		System.out.println("Enter password: ");
		String pw = scan.nextLine();
	}
	public void openLogs() {
		// no inputs are required from Teller
	}
	public void loginUserAccount() {
		scan = new Scanner(System.in);
		System.out.println("Enter username: ");
		String userId = scan.nextLine();
	}
	// !
	public void checkTeller() {
		// needs clarification
	}

	public void createAccount() {
		// no inputs are required from Teller
	}
	public void deleteAccount() {
		scan = new Scanner(System.in);
		System.out.println("Enter user id: ");
		String userId = scan.nextLine();

	}
	public void addUser() {
		scan = new Scanner(System.in);
		System.out.println("Enter user id: ");
		String userId = scan.nextLine();

	}
	public void deleteUser() {
		scan = new Scanner(System.in);
		System.out.println("Enter user id: ");
		String userId = scan.nextLine();

	}
	public void forgetPassword() {
		scan = new Scanner(System.in);
		System.out.println("Enter birthday: ");
		String bd = scan.nextLine();

		System.out.println("Enter new password: ");
		String pd = scan.nextLine();

	}
	public void changePin() {
		scan = new Scanner(System.in);
		System.out.println("Enter new pin: ");
		int num = scan.nextInt();

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




	public int handshake(int id) {
		try {
			// public HelloMessage(int id, String text, String to, String from, MessageType type, Status status)
			HelloMessage clientHello = new HelloMessage(id, "Teller", Status.ONGOING);
			writer.writeObject(clientHello);
			HelloMessage serverHello = (HelloMessage) reader.readObject();
			if (serverHello.getID() == ++id && serverHello.getStatus() == Status.SUCCESS) {
				System.out.println("client-server handshake successfully");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return id++;
	}
	
	public int tellerLogin(int id) {
		scan = new Scanner(System.in);
		int tellerId;
		String password;
		while (true) {
			while (true) {
				System.out.println("Enter your teller id: ");
				try {
					tellerId = scan.nextInt();
					scan.nextLine();
					break;
				} catch (Exception e) {
					System.out.println("Error: your teller id is an integer. Please try again.");
				}
			}
			
			System.out.println("Enter your password: ");
			password = scan.nextLine();
			
			// create LoginMessage
			LoginMessage msg = new LoginMessage(id, Status.ONGOING, tellerId, password);
			
			try {
				// send LoginMessage to server
				writer.writeUnshared(msg);
				
				// expect a success LoginMessage returned from the server
				LoginMessage msgReceipt = (LoginMessage) reader.readObject();
				id = msgReceipt.getID() + 1;
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
		
		return id++;
	}

	public int newSession(int id) {
		
		id = tellerLogin(id);
		
		// switch statement
		return id++;
	}

	public void go() {
		try {
			// start client
			setUpConnection();

			int id = 0;
			// handshake with server: Teller client hello
			id = handshake(id);

			id = newSession(id);

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