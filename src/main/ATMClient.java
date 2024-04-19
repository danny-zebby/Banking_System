package main;

import java.net.*;
import java.io.*;
import message.*;
import java.util.Scanner;

public class ATMClient {
	Socket sock = null;
	ObjectInputStream reader = null;
	ObjectOutputStream writer = null;
	Scanner scanner = null;
	
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
			
			// handshake with server: ATM client hello
			int id = 0;
			// public HelloMessage(int id, String text, String to, String from, MessageType type, Status status)
			HelloMessage clientHello = new HelloMessage(id, "ATM Client Hello", "Server", "ATM", Status.ONGOING);
			writer.writeObject(clientHello);
			HelloMessage serverHello = (HelloMessage) reader.readObject();
			if (serverHello.getID() == ++id && serverHello.getStatus() == Status.SUCCESS) {
				System.out.println("client-server handshake successfully");
			}
			
			// ATM login
			while (true) {
				// create new login message
				LoginMessage loginMessage = loginRequest(++id);
				// send login message to server
				writer.writeObject(loginMessage);
				
				// wait for loginMessage from server
				LoginMessage loginReceipt = (LoginMessage) reader.readObject();
				if ((loginReceipt.getStatus() == Status.SUCCESS)) { // if success, break while loop
					break;
				}
				
				System.out.println("Wrong user id or password. Please try again.\n");
			} 
			
			// wait for bankuser object from server
			BankUser user = (BankUser) reader.readObject();
			// print out user object
			System.out.println("current user: ");
			System.out.println(user);
			
			
			// request info from all accounts of current user
			
			
			// codes go here...
			// while loop
				// two options: ATM Login, exit
				
				// exit
			
				// ATM login: while loop
					// Four options: deposit, withdraw, transfer, logout
			
			// close client
			closeConnection();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public LoginMessage loginRequest(int id) {
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
		
		//LoginMessage(int id, String to, String from, Status status, int userId, String password)
		return new LoginMessage(id, "Server", "ATM", Status.ONGOING, userId, password);
		
	}
	
	public static void main(String[] args) {
		ATMClient client = new ATMClient();
		client.go();
	}
}
