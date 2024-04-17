package main;

import java.io.*;
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
		
	}
	public void deleteTeller() {
		
	}
	public void createUser() {
		
	}
	public void openLogs() {
		
	}
	public void loginUserAccount() {
		
	}
	// !
	public void checkTeller() {
		
	}
	
	public void createAccount() {
		
	}
	public void deleteAccount() {
		
	}
	public void addUser() {
		
	}
	public void deleteUser() {
		
	}
	public void forgetPassword() {
		
	}
	public void changePin() {
		
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
		// start client
		setUpConnection();
		
		// codes go here...
		
		
		// close client
		closeConnection();
	}
	
	// Driver
	public static void main(String[] args) {
		// create new tellerclient
		TellerClient client = new TellerClient();
		client.go();
		
	}
}