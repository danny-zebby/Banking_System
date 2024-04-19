package main;

import java.io.*;
import java.util.*;
import java.net.*;
import message.*;

public class TellerClient {
	private String port;
	private String ip;
	private Teller teller;
	private Teller[] tellers;
	Socket sock = null;
	ObjectInputStream reader = null;
	ObjectOutputStream writer = null;
	
	Scanner scan;
	
	// Getters & Setters
	public String getPort() {
		return this.port;
	}
	public void setPort(String newPort) {
		port = newPort;
	}
	
	public Teller getTeller() {
		return this.teller;
	}
	public Teller[] getTellers() {
		return this.tellers;
	}
	public void setTeller(Teller newTeller) {
		teller = newTeller;
	}
	
	public String getIp() {
		return this.ip;
	}
	public void serIp(String newIp) {
		ip = newIp;
	}
	
	// Client operation functions
	public void connectClient() {
		
	}
	public void moveClient() {
		
	}
	public void start() {
		
	}
	public void signIn() {
		
	}
	public void signOut() {
		
	}
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
	public void accessATM() {
		
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
	
	public void go() {
		try {
			// start client
			setUpConnection();
			
			// handshake with server: Teller client hello
			int id = 0;
			// public HelloMessage(int id, String text, String to, String from, MessageType type, Status status)
			HelloMessage clientHello = new HelloMessage(id, "Teller Client Hello", "Server", "Teller", Status.ONGOING);
			writer.writeObject(clientHello);
			HelloMessage serverHello = (HelloMessage) reader.readObject();
			if (serverHello.getID() == ++id && serverHello.getStatus() == Status.SUCCESS) {
				System.out.println("client-server handshake successfully");
			}
			
			
			// codes go here...
			
			
			
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