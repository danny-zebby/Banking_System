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

public class Server {
	Map<Integer, BankUser> userList;
	Map<Integer, BankAccount> accountList;
	Map<Integer, Teller> tellerList;

	Map<Integer, Boolean> activeUsers;
	Map<Integer, Boolean> activeAccounts;
	Map<Integer, Boolean> activeTellers;

	public Server() {
		userList = new HashMap<>();
		accountList = new HashMap<>();
		tellerList = new HashMap<>();
		activeUsers = new HashMap<>();
		activeAccounts = new HashMap<>();
		activeTellers = new HashMap<>();
	}

	public static void main(String[] args) {
		new Server().go();
	}

	public void hardCodeSetUp() {
		String name = "Donny";
		String birthday = "04/23/2000";
		String password = "letmein";
		BankUser user1 = addUser(name, birthday, password);
		BankUser user2 = addUser("Peter", "11/11/1911", "0000");
		BankAccount acc1 = addAccount(1222, AccountType.CHECKING, user1, 10000);
		BankAccount acc2 = addAccount(1234, AccountType.SAVING, user1, 10000);
		BankAccount acc3 = addAccount(3333, AccountType.CHECKING, user2, 10000);
		BankAccount acc4 = addAccount(4444, AccountType.CHECKING, user2, 10000);
		Teller tel1 = addTeller("Alice", "letmein", true); // this is the admin
		Teller tel2 = addTeller("BOB", "plsletmein", false);
		Teller tel3 = addTeller("BOB", "letmeinin", false);
	}

	public void go() {

		hardCodeSetUp();

		// set up thread pool
		ExecutorService threadPool = Executors.newFixedThreadPool(20);

		// create a server socket
		try (ServerSocket serverSock = new ServerSocket(50000)) {
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

	public BankAccount addAccount(int accountPin, AccountType accountType, BankUser user, double balance) {
		// create new BankAccount
		BankAccount account = new BankAccount(accountPin, accountType, user.getId(), balance);
		// add to HashMap accountList
		accountList.put(account.getAccountNumber(), account);
		// add to HashMap activeAccounts
		activeAccounts.put(account.getAccountNumber(), false);
		user.addAccount(account);
		return account;
	}

	public Teller addTeller(String name, String password, boolean admin) {
		// String name, String password, boolean admin
		Teller teller = new Teller(name, password, admin);
		// add to HashMap tellerList
		tellerList.put(teller.getId(), teller);
		// add to HashMap activeTellers
		activeTellers.put(teller.getId(), false);
		return teller;
	}

	public boolean checkUserLogin(int userId, String password) {
		return (userList.containsKey(userId) && userList.get(userId).getPassword().equals(password));
	}

	public boolean checkTellerLogin(int tellerId, String password) {
		return (tellerList.containsKey(tellerId) && tellerList.get(tellerId).getPassword().equals(password));
	}

	// class clientHandler
	class clientHandler implements Runnable {
		Socket sock = null;
		ObjectInputStream reader = null;
		ObjectOutputStream writer = null;

		public clientHandler(Socket sock) {
			this.sock = sock;
		}

		public boolean checkWithdraw(int userId, int accountNumber, double amount, int pin) {
			// check if it is available to withdraw
			// - accountNumber and pin matches
			// - amount < balance
			return (userList.get(userId).getAccounts().contains(accountNumber) && accountList.get(accountNumber).getAccountPin() == pin
					&& accountList.get(accountNumber).getBalance() >= amount);
		}

		private void withdraw(int accountNumber, double amount) {
			// calculating a new balance
			double newBalance = accountList.get(accountNumber).getBalance() - amount;
			// create new account obj
			BankAccount account = accountList.get(accountNumber);
			// set the new balance to the new account obj
			account.setBalance(newBalance);
			// print
			System.out.println("new account info: " + account);
			try {
				// send new account object to client
				writer.writeUnshared(account);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		public boolean checkDeposit(int userId, int accountNumber, double amount, int pin) {
			// check if it is available to withdraw
			// - accountNumber and pin matches
			// - amount < balance
			return (userList.get(userId).getAccounts().contains(accountNumber) && accountList.get(accountNumber).getAccountPin() == pin);
		}
		
		public boolean checkUserId(int userId) {
			return userList.containsKey(userId) && !activeUsers.get(userId);
		}

		private void deposit(int accountNumber, double amount) {
			// calculating a new balance
			double newBalance = accountList.get(accountNumber).getBalance() + amount;
			// create new account obj
			BankAccount account = accountList.get(accountNumber);
			// set the new balance to the new account obj
			account.setBalance(newBalance);
			// print
			System.out.println("new account info: " + account);
			try {
				// send new account object to client
				writer.writeUnshared(account);

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		public boolean checkTransfer(int userId, int fromAccountNumber, double amount, int pin) {
			// if account number and pin matches and amount <= balance
			return (userList.get(userId).getAccounts().contains(fromAccountNumber) 
					&& accountList.get(fromAccountNumber).getAccountPin() == pin
					&& accountList.get(fromAccountNumber).getBalance() >= amount);
		}

		private void transfer(int fromAccountNumber, int toAccountNumber, double amount, int currUserID) {
			BankAccount account = accountList.get(fromAccountNumber);
			double newBalance = account.getBalance() - amount;
			account.setBalance(newBalance);
			// print
			System.out.println("new account info: " + account);
			try {
				// send new account object to client
				writer.writeUnshared(account);
			} catch (Exception e) {
				e.printStackTrace();
			}

			// update recipient account
			BankAccount recipientAccount = accountList.get(toAccountNumber);
			double recipientNewBalance = recipientAccount.getBalance() + amount;
			recipientAccount.setBalance(recipientNewBalance);
			// send recipient account to the client
			// if recipient account contains
			if (recipientAccount.getUsers().contains(currUserID)) {
				try {
					writer.writeUnshared(recipientAccount);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			System.out.println("new recipient account info: " + recipientAccount);
		}
		
		private int atmLogin() {
			Object obj;
			int currUserId = 0;
			try {
				
				while (true) {
					obj = reader.readObject();
					LoginMessage loginMessage = (LoginMessage) obj;
					LoginMessage loginReceipt;
					currUserId = loginMessage.getUserId();
					String password = loginMessage.getPassword();
					
					synchronized (activeUsers) {
												
						if (checkUserLogin(currUserId, password) && !activeUsers.get(currUserId)) {
							// LoginMessage(int id, String to, String from, Status status, String text)
							loginReceipt = new LoginMessage(Status.SUCCESS);
							writer.writeObject(loginReceipt); // send loginReceipt
							
							// Update activeUsers
							activeUsers.replace(currUserId, true);	
							
							writer.writeObject(userList.get(currUserId)); // send BankUser object to client
							System.out.println("ATM client logged in with user: " + userList.get(currUserId).getName());
							break;
						} else { // fail to login
							// return error message
							loginReceipt = new LoginMessage(Status.ERROR);
							writer.writeObject(loginReceipt); // send loginReceipt
						}
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return currUserId;
		}
		
		private void closeActiveAccounts(int userId) {
			List<Integer> accounts = userList.get(userId).getAccounts();
			
			synchronized (activeAccounts) {
				for (int accountNumber : accounts) {
					activeAccounts.replace(accountNumber, false);
				}
			}
			
			synchronized (activeUsers) {
				activeUsers.replace(userId, false);
			}
		}
		
		
		private void atmHandler() {

			Object obj;
			int currUserId = atmLogin();

			try {
				// Wait for Account Message
				while ((obj = reader.readObject()) != null) {

					if (obj instanceof LoginMessage) {
						LoginMessage msg = (LoginMessage) obj;
						// ignore this message

					} else if (obj instanceof LogoutMessage) {
						LogoutMessage msg = (LogoutMessage) obj;
						LogoutMessage logoutReceipt = new LogoutMessage(Status.SUCCESS);
						writer.writeObject(logoutReceipt);
						
						// CLOSE ACTIVE ACCOUNTS
						closeActiveAccounts(currUserId);
						
						atmHandler(); // handle new login

					} else if (obj instanceof DepositMessage) {
						// code goes here
						DepositMessage msg = (DepositMessage) obj;
						int accountNumber = msg.getAccountNumner();
						double amount = msg.getDepositAmount();
						int pin = msg.getPin();
						// if it is available to deposit
						if (checkDeposit(currUserId, accountNumber, amount, pin)) {
							writer.writeObject(new DepositMessage(Status.SUCCESS));
							// deposit
							deposit(accountNumber, amount);
						} else { // is it is not available to deposit
							// return error message
							writer.writeObject(new DepositMessage(Status.ERROR));
						}

					} else if (obj instanceof WithdrawMessage) {
						WithdrawMessage msg = (WithdrawMessage) obj;
						// code goes here
						int accountNumber = msg.getAccountNumber();
						double amount = msg.getWithdrawAmount();
						int pin = msg.getPin();

						if (checkWithdraw(currUserId, accountNumber, amount, pin)) { // if it is available to withdraw
							// send back success message
							writer.writeObject(new WithdrawMessage(Status.SUCCESS));
							// withdraw
							withdraw(accountNumber, amount);
						} else { // if it is not available to withdraw
							// return error message
							writer.writeObject(new WithdrawMessage(Status.ERROR));
						}

					} else if (obj instanceof TransferMessage) {
						TransferMessage msg = (TransferMessage) obj;
						int fromAccountNumber = msg.getFromAccountNumber();
						int toAccountNumber = msg.getToAccountNumber();
						double transferAmount = msg.getTransferAmount();
						int pin = msg.getPin();

						TransferMessage msgReceipt;
						if (msg.getStatus() == Status.ONGOING
								&& checkTransfer(currUserId, fromAccountNumber, transferAmount, pin)) {
							msgReceipt = new TransferMessage(Status.SUCCESS, fromAccountNumber, toAccountNumber,
									transferAmount);
							writer.writeUnshared(msgReceipt);

							transfer(fromAccountNumber, toAccountNumber, transferAmount, currUserId);
						} else {
							msgReceipt = new TransferMessage(Status.ERROR, fromAccountNumber, toAccountNumber,
									transferAmount);
							writer.writeUnshared(msgReceipt);
						}

					} else if (obj instanceof AccountMessage) {
						AccountMessage msg = (AccountMessage) obj;
						// code goes here
						currUserId = msg.getCurrUserId();
						int accountNumber = msg.getAccountNumber();
						synchronized (activeAccounts) {
							if (accountList.get(accountNumber).getUsers().contains(currUserId)
									&& activeAccounts.get(accountNumber) == false) {
								activeAccounts.replace(accountNumber, true);
								// int id, int currUserId, int accountNumber, Status status
								AccountMessage msgReceipt = new AccountMessage(currUserId, accountNumber,
										Status.SUCCESS);
								writer.writeObject(msgReceipt);
								writer.writeObject(accountList.get(accountNumber));
							} else { // Invalid request
								AccountMessage msgReceipt = new AccountMessage(currUserId, accountNumber, Status.ERROR);
								writer.writeObject(msgReceipt);
							}
						}

					} else if (obj instanceof AccountInfoMessage) {
						AccountInfoMessage msg = (AccountInfoMessage) obj;
						int accountNumber = msg.getAccountNumber();
						AccountInfoMessage msgReceipt;
						if (msg.getStatus() == Status.ONGOING && accountList.containsKey(accountNumber)) {
							msgReceipt = new AccountInfoMessage(Status.SUCCESS, accountNumber,
									accountList.get(accountNumber).getUsers());
						} else {
							msgReceipt = new AccountInfoMessage(Status.ERROR, accountNumber);
						}
						writer.writeUnshared(msgReceipt);

					} else if (obj instanceof UserInfoMessage) {
						UserInfoMessage msg = (UserInfoMessage) obj;
						int userId = msg.getUserID();
						UserInfoMessage msgReceipt;
						if (msg.getStatus() == Status.ONGOING && userList.containsKey(userId)) {
							msgReceipt = new UserInfoMessage(Status.SUCCESS, userId, userList.get(userId).getName());
						} else {
							msgReceipt = new UserInfoMessage(Status.ERROR, userId);
						}
						writer.writeUnshared(msgReceipt);
					} else {
						// ignore the message
					}

				} // end while loop

			} catch (Exception e) {
				e.printStackTrace();
			}

		} // end method atmHandler
		
		private int tellerLogin() {
			Object obj;
			int tellerId = 0;
			try {
				// handle Teller login
				while (true) {
					obj = reader.readObject();
					LoginMessage loginMessage = (LoginMessage) obj;
					LoginMessage loginReceipt;
					tellerId = loginMessage.getUserId();
					String password = loginMessage.getPassword();

					// if user exists and password is correct, return success message, user info and
					// break
					if (checkTellerLogin(tellerId, password)) {
						// LoginMessage(int id, String to, String from, Status status, String text)
						loginReceipt = new LoginMessage(Status.SUCCESS);
						writer.writeUnshared(loginReceipt); // send loginReceipt
						writer.writeUnshared(tellerList.get(tellerId)); // send BankUser object to client
						System.out
						.println("Teller client logged in with teller: " + tellerList.get(tellerId).getName());
						break;
					} else { // fail to login
						// return error message
						loginReceipt = new LoginMessage(Status.ERROR);
						writer.writeUnshared(loginReceipt); // send loginReceipt
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return tellerId;
		}
		
		private void tellerHandler() {
			Object obj;
			int tellerId = tellerLogin();
			int userId = 0;
			try {
				// handle remaining messages
				while ((obj = reader.readObject()) != null) {

					if (obj instanceof LoginMessage) {
						LoginMessage msg = (LoginMessage) obj;
						// ignore this message

					} else if (obj instanceof LogoutMessage) {
						LogoutMessage msg = (LogoutMessage) obj;
						// code goes here

					} else if (obj instanceof DepositMessage) {
						DepositMessage msg = (DepositMessage) obj;
						// code goes here
						int accountNumber = msg.getAccountNumner();
						double amount = msg.getDepositAmount();
						int pin = msg.getPin();
						// if it is available to deposit
						if (checkDeposit(userId, accountNumber, amount, pin)) {
							writer.writeObject(new DepositMessage(Status.SUCCESS));
							// deposit
							deposit(accountNumber, amount);
						} else { // is it is not available to deposit
							// return error message
							writer.writeObject(new DepositMessage(Status.ERROR));
						}

					} else if (obj instanceof WithdrawMessage) {
						WithdrawMessage msg = (WithdrawMessage) obj;
						// code goes here
						int accountNumber = msg.getAccountNumber();
						double amount = msg.getWithdrawAmount();
						int pin = msg.getPin();
						if (checkWithdraw(userId, accountNumber, amount, pin)) { // if it is available to withdraw
							// send back success message
							writer.writeObject(new WithdrawMessage(Status.SUCCESS));
							// withdraw
							withdraw(accountNumber, amount);
						} else { // if it is not available to withdraw
							// return error message
							writer.writeObject(new WithdrawMessage(Status.ERROR));
						}

					} else if (obj instanceof TransferMessage) {
						TransferMessage msg = (TransferMessage) obj;
						// code goes here

					} else if (obj instanceof AccountMessage) {
						AccountMessage msg = (AccountMessage) obj;
						// code goes here
						AccountMessageType type = msg.getType();
						AccountMessage msgReceipt;
						switch (type) {
							case ADD_USER: {
								// reply with success status
								msgReceipt = new AccountMessage(Status.SUCCESS, AccountMessageType.ADD_USER);
								// send back to client
								writer.writeUnshared(msgReceipt);
								
								// create new BankUser
								Map<String, String> info = msg.getInfo();
								String name = info.get("name");
								String birthday = info.get("birthday");
								String password = info .get("password");
								BankUser newUser = addUser(name, birthday, password);
								System.out.println("new user created: " + newUser);
								
								// send BankUser to client
								writer.writeUnshared(newUser);
								
								break;
							}
							case USER_INFO: {
								// check if user id is valid
								userId = Integer.parseInt( msg.getInfo().get("userId"));
								if (checkUserId(userId)) {
									// reply with success status and send back to client
									msgReceipt = new AccountMessage(Status.SUCCESS, AccountMessageType.USER_INFO);
									writer.writeUnshared(msgReceipt);
									// send back BankUser obj
									writer.writeUnshared(userList.get(userId));
								} else {
									// reply with ERROR status and send back to client
									msgReceipt = new AccountMessage(Status.ERROR, AccountMessageType.USER_INFO);
									writer.writeUnshared(msgReceipt);
								}
								
								break;
							}
							case ACCOUNT_INFO:{
								
								int accountNumber = msg.getAccountNumber();
								// debugging
								System.out.println("Requesting account " + accountNumber);
								
								synchronized (activeAccounts) {
									if (accountList.get(accountNumber).getUsers().contains(userId)
											&& activeAccounts.get(accountNumber) == false) {
										activeAccounts.replace(accountNumber, true);
										// int id, int currUserId, int accountNumber, Status status
										msgReceipt = new AccountMessage(userId, accountNumber,
												Status.SUCCESS);
										writer.writeUnshared(msgReceipt);
										writer.writeUnshared(accountList.get(accountNumber));
									} else { // Invalid request
										msgReceipt = new AccountMessage(userId, accountNumber, Status.ERROR);
										writer.writeUnshared(msgReceipt);
									}
								}
								break;
							}
							case ADD_ACCOUNT: {
								Map<String, String> info = msg.getInfo();
								AccountType accountType = AccountType.valueOf(info.get("accountType"));
								int pin = Integer.parseInt(info.get("pin"));
								
								BankAccount bankAccount = new BankAccount(pin, accountType, userId);
								int accountNumber = bankAccount.getAccountNumber();
								synchronized (accountList) {
									accountList.put(accountNumber, bankAccount);
								}
								synchronized (activeAccounts) {
									activeAccounts.put(accountNumber, false);
								}
								userList.get(userId).addAccount(bankAccount);
								
								// reply with success status with new accountNumber
								Map<String, String> newInfo = new HashMap<>();
								newInfo.put("accountNumber", Integer.toString(accountNumber));
								msgReceipt = new AccountMessage(Status.SUCCESS, AccountMessageType.ADD_ACCOUNT, newInfo);
								writer.writeUnshared(msgReceipt);
								
								break;
							}
							case REM_ACCOUNT: {
								Map<String, String> info = msg.getInfo();
								int accountNumber = Integer.parseInt(info.get("accountNumber"));
								
								// check if it is ready to remove account
								// - if this user belongs to this account
								// - if this account has balance of zero
								// - if this user is the admin of this account
								if (accountList.get(accountNumber).getUsers().contains(userId)
									&& accountList.get(accountNumber).getBalance() == 0
									&& accountList.get(accountNumber).getAdminID() == userId) {
									
									// update BankUser in userList, 
									userList.get(userId).getAccounts().remove(Integer.valueOf(accountNumber));
									
									// update activeAccounts, accountList
									synchronized (activeAccounts) {
										activeAccounts.remove(accountNumber);
									}
									accountList.remove(accountNumber);
									
									// send back msgReceipt
									msgReceipt = new AccountMessage(Status.SUCCESS, AccountMessageType.REM_ACCOUNT);
									// send back msgReceipt
									writer.writeUnshared(msgReceipt);
									
								} else { // not able to remove
									msgReceipt = new AccountMessage(Status.ERROR, AccountMessageType.REM_ACCOUNT);
									// send back msgReceipt
									writer.writeUnshared(msgReceipt);
								}
								
								break;
							}									
							default: break;
						}

					} else if (obj instanceof TellerMessage) {
						TellerMessage msg = (TellerMessage) obj;
						// code goes here

					}

				} // end while loop

			} catch (Exception e) {
				e.printStackTrace();
			}

		} // end method tellerHandler

		private HelloMessage handshake() {
			Object obj = null;
			HelloMessage clientHello = null;
			try {
				// receive hello messages and recognize which client (ATM/Teller) it's connected
				// to
				obj = reader.readObject();
				if (obj instanceof HelloMessage) {
					clientHello = (HelloMessage) obj;
					System.out.println(clientHello.toString());
					HelloMessage serverHello = new HelloMessage("Server", Status.SUCCESS);
					writer.writeObject(serverHello);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return clientHello;

		}

		public void run() {
			// print out connection info
			System.out.println("A client is connected: " + this.sock);

			try {
				reader = new ObjectInputStream(sock.getInputStream());
				writer = new ObjectOutputStream(sock.getOutputStream());

				HelloMessage clientHello = handshake(); // handshake with client

				if (clientHello.getFrom().equals("ATM")) { // if it's from ATM, hand it to ATM handler
					System.out.println("An ATM is connected: " + this.sock);
					atmHandler();
				} else { // if it's from Teller, hand it to Teller handler
					System.out.println("A Teller is connected: " + this.sock);
					tellerHandler();
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
