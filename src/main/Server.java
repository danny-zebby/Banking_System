package main;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.net.*;
import java.io.*;
import java.util.Scanner;
import message.*;

public class Server {
	
	public void go() {
		// set up thread pool
		ExecutorService threadPool = Executors.newFixedThreadPool(20);
		
		// create a server socket
		try (ServerSocket serverSock = new ServerSocket(50000)){
			// print out server socket info
			System.out.println("Server is running at 127.0.0.1:50000 ...");
			
			// while true:
			while (true) {
				// accept incoming connection as a new socket
				Socket sock = serverSock.accept();
				// hand socket to client Handler
				threadPool.execute(new clientHandler(sock));
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// close thread pool
		threadPool.shutdown();
		
	} // end method go
	
	
	// class clientHandler
	class clientHandler implements Runnable {
		Socket sock;
		
		public clientHandler(Socket sock) {
			this.sock = sock;
		}
		
		public void run() {
			// print out connection info
			System.out.println("A client is connected: " + this.sock);
			// set up reader
			ObjectInputStream reader;
			// set up writer
			ObjectOutputStream writer;
			
			try {
				reader = new ObjectInputStream(sock.getInputStream());
				writer = new ObjectOutputStream(sock.getOutputStream());
				Object obj;
				
				// handle login
				// code goes here
				
				
				// handle remaining messages
				while ( (obj = reader.readObject()) != null ) {
					
					if (obj instanceof LoginMessage) {
						LoginMessage msg = (LoginMessage) obj;
						// ignore this message
						
					} else if (obj instanceof LogoutMessage) {
						LogoutMessage msg = (LogoutMessage) obj;
						// code goes here
						
					} else if (obj instanceof DepositMessage) {
						DepositMessage msg = (DepositMessage) obj;
						// code goes here
						
					} else if (obj instanceof WithdrawMessage) {
						WithdrawMessage msg = (WithdrawMessage) obj;
						// code goes here
						
					} else if (obj instanceof TransferMessage) {
						TransferMessage msg = (TransferMessage) obj;
						// code goes here
						
					} else if (obj instanceof AccountMessage) {
						AccountMessage msg = (AccountMessage) obj;
						// code goes here
						
					} else if (obj instanceof TellerMessage) {
						TellerMessage msg = (TellerMessage) obj;
						// code goes here
						
					}
					
				}
				
				
				// after sending logout messages to client
				// close reader
				reader.close();
				// close writer
				writer.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					// close sock
					sock.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} // end try/catch/finally
			
			
			
			
		} // end method run
		
		
		
	} // end class clientHandler
	
	
	
	public static void main(String[] args) {
		Server server = new Server();
		server.go();
	}
}
