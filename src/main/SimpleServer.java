package main;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import main.Server.clientHandler;
import message.*;

public class SimpleServer {
	Map<Integer, BankUser> userList;
	Map<Integer, BankAccount> accountList;
	Map<Integer, Teller> tellerList;
	
	Map<Integer, Boolean> activeUsers;
	Map<Integer, Boolean> activeAccounts;
	Map<Integer, Boolean> activeTellers;
	
	
	
	public SimpleServer() {
		userList = new HashMap<>();
		accountList = new HashMap<>();
		tellerList = new HashMap<>();
		activeUsers = new HashMap<>();
		activeAccounts = new HashMap<>();
		activeTellers = new HashMap<>();
	}
	
	public static void main(String[] args) {
		new SimpleServer().go();
	}
	
	public void go() {
		String name = "Donny";
		String birthday = "04/23/2000";
		String password = "letmein";
		BankUser user1 = addUser(name, birthday, password);
		BankUser user2 = addUser("Peter", "11/11/1911", "0000");
		BankAccount acc1 = addAccount(1222, AccountType.CHECKING, user1);
		BankAccount acc2 = addAccount(1234, AccountType.SAVING, user1);
		BankAccount acc3 = addAccount(3333, AccountType.CHECKING, user2);
		BankAccount acc4 = addAccount(4444, AccountType.CHECKING, user2);
		
//		System.out.println("Userlist: " + this.getUserList() + "\n");
//		System.out.println("accountList: " + this.getAccountList() + "\n");
//		System.out.println("activeUsers: " + this.getActiveUsers() + "\n");
//		System.out.println("activeAccounts: " + this.getActiveAccounts() + "\n");
		
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
	}
	
	public BankUser addUser(String name, String birthday, String password) {
		// create new BankUser
		BankUser user = new BankUser(name, birthday, password);
		// add to HashMap userList
		userList.put(user.getId(), user);
		// add to HashMap activeUsers
		activeUsers.put(user.getId(), false);
		return user;
	}
	
	public BankAccount addAccount(int accountPin, AccountType accountType, BankUser user) {
		// create new BankAccount
		BankAccount account = new BankAccount(accountPin, accountType, user.getId());
		// add to HashMap accountList
		accountList.put(account.getAccountNumber(), account);
		// add to HashMap activeAccounts
		activeAccounts.put(account.getAccountNumber(), false);
		user.addAccount(account);
		return account;
	}
	
	public boolean userLogin(int userId, String password) {
		return (userList.containsKey(userId) && userList.get(userId).getPassword().equals(password));
	}
	
	// class clientHandler
	class clientHandler implements Runnable {
		Socket sock = null;
		ObjectInputStream reader = null;
		ObjectOutputStream writer = null;
		
		public clientHandler(Socket sock) {
			this.sock = sock;
		}
		
		private void atmHandler() {
			
			Object obj;
			
			try {
				// handle ATM login
				while (true) {
					obj = reader.readObject();
					LoginMessage loginMessage = (LoginMessage) obj;
					LoginMessage loginReceipt;
					int userId = loginMessage.getUserId();
					String password = loginMessage.getPassword();
					int msgId = loginMessage.getID();
					
					// if user exists and password is correct, return success message, user info and break
					if ( userLogin(userId, password) ) {
						//  LoginMessage(int id, String to, String from, Status status, String text)
						loginReceipt = new LoginMessage(msgId + 1, loginMessage.getFrom(), loginMessage.getTo(), Status.SUCCESS, "Login in successfully");
						writer.writeObject(loginReceipt); // send loginReceipt
						writer.writeObject(userList.get(userId)); // send BankUser object to client
						System.out.println("ATM client logged in with user: " + userList.get(userId).getName());
						break;
					} else { // fail to login
						// return error message
						loginReceipt = new LoginMessage(msgId + 1, loginMessage.getFrom(), loginMessage.getTo(), Status.ERROR, "Fail to login");
						writer.writeObject(loginReceipt); // send loginReceipt
					}
				}
				
				
				// Wait for Account Message
				
				
				
//				// handle remaining messages
//				while ( (obj = reader.readObject()) != null ) {
//					
//					if (obj instanceof LoginMessage) {
//						LoginMessage msg = (LoginMessage) obj;
//						// ignore this message
//						
//					} else if (obj instanceof LogoutMessage) {
//						LogoutMessage msg = (LogoutMessage) obj;
//						// code goes here
//						
//					} else if (obj instanceof DepositMessage) {
//						DepositMessage msg = (DepositMessage) obj;
//						// code goes here
//						
//					} else if (obj instanceof WithdrawMessage) {
//						WithdrawMessage msg = (WithdrawMessage) obj;
//						// code goes here
//						
//					} else if (obj instanceof TransferMessage) {
//						TransferMessage msg = (TransferMessage) obj;
//						// code goes here
//						
//					} else {
//						// ignore this message
//					}
//					
//				} // end while loop
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		} // end method atmHandler
		
		
		private void tellerHandler() {
			Object obj;
			
			try {
				
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
					
				} // end while loop
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		} // end method tellerHandler
		
		public void run() {
			// print out connection info
			System.out.println("A client is connected: " + this.sock);
			
			
			try {
				reader = new ObjectInputStream(sock.getInputStream());
				writer = new ObjectOutputStream(sock.getOutputStream());
				Object obj;
				
				// receive hello messages and recognize which client (ATM/Teller) it's connected to
				obj = reader.readObject();
				if (obj instanceof HelloMessage) {
					HelloMessage clientHello = (HelloMessage) obj;
					System.out.println(clientHello.toString());
					HelloMessage serverHello = new HelloMessage(clientHello.getID() + 1, "Server Hello", clientHello.getFrom(), 
												clientHello.getTo(), Status.SUCCESS);
					writer.writeObject(serverHello);
					
					if (clientHello.getFrom().equals("ATM")) { // if it's from ATM, hand it to ATM handler
						System.out.println("An ATM is connected: " + this.sock);
						atmHandler();
					} else { // if it's from Teller, hand it to Teller handler
						System.out.println("A Teller is connected: " + this.sock);
						tellerHandler();
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
	
	

	public Map<Integer, BankUser> getUserList() {
		return this.userList;
	}

	public Map<Integer, BankAccount> getAccountList() {
		return this.accountList;
	}

	public Map<Integer, Teller> getTellerList() {
		return this.tellerList;
	}

	public Map<Integer, Boolean> getActiveUsers() {
		return this.activeUsers;
	}

	public Map<Integer, Boolean> getActiveAccounts() {
		return this.activeAccounts;
	}

	public Map<Integer, Boolean> getActiveTellers() {
		return this.activeTellers;
	}
	
	
	
}
