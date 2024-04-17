package main;

import java.net.*;
import java.io.*;
import message.*;

public class ATMClient {
	Socket sock = null;
	ObjectInputStream reader = null;
	ObjectOutputStream writer = null;
	
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
	
	public static void main(String[] args) {
		ATMClient client = new ATMClient();
		client.go();
	}
}
