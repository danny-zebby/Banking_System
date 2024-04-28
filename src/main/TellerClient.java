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
	public void withdraw(int loggedInUserId) {
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
				System.out.println("New Account info: " + newAccount);
				// print out all account info
				System.out.println("Updated user id [" + loggedInUserId + "] accounts: " + accounts);

			} else {
				System.out.println("Fail to withdraw $" + amount + " from account " + accountNumber);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// Deposit
	public void deposit(int loggedInUserId) {
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
				// print out result
				System.out.println("New Account: " + newAccount);
				// print out all account info
				System.out.println("Updated user id [" + loggedInUserId + "] accounts: " + accounts);
			} else {

				System.out.println("Fail to deposit $" + amount + " to account " +
						accountNumber);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	} // end method deposit

	// Transfer
	public void transfer(int loggedInUserId) {
		try {
			// get inputs: acc#, amount, pin
			scanner = new Scanner(System.in);
			System.out.println("Enter your account number: ");
			int fromAccountNumber;
			fromAccountNumber = scanner.nextInt();
			scanner.nextLine();
			List<Integer> toAccountUsers = null;

			int toAccountNumber;
			double amount;

			while (true) {
				System.out.println("Enter recipient account number: ");
				toAccountNumber = scanner.nextInt();
				scanner.nextLine();
				System.out.println("Enter the amount to transfer: ");
				amount = scanner.nextDouble();
				scanner.nextLine();

				// verify from server the recipient's info
				// send an account info message to the server: int id, int currUserId, int
				// accountNumber, Status status
				AccountInfoMessage msg = new AccountInfoMessage(Status.ONGOING, toAccountNumber);
				writer.writeUnshared(msg);
				// expected an SUCCESS status of account info msg, containing all users linked
				// to this recipent's account
				AccountInfoMessage msgReceipt = (AccountInfoMessage) reader.readObject();
				toAccountUsers = msgReceipt.getUsers();
				System.out.println("Received Account Info Message with users: " + msgReceipt.getUsers());

				// init list of string to store user names
				ArrayList<String> recipentNames = new ArrayList<>();

				if (msgReceipt.getStatus() == Status.SUCCESS) {
					// for each user of this account
					for (int userId : toAccountUsers) {
						// send user info msg to server
						UserInfoMessage uiMsg = new UserInfoMessage(Status.ONGOING, userId);
						writer.writeUnshared(uiMsg);
						// expected SUCCESS of user info msg, which contains user name
						UserInfoMessage uiMsgReceipt = (UserInfoMessage) reader.readObject();

						if (uiMsgReceipt.getStatus() == Status.SUCCESS) {
							recipentNames.add(uiMsgReceipt.getUserName()); // add user name to list
						}

					}
					// show the confirmation msg with all the user names and amount of money
					System.out.printf("You are transferring $%.2f to account %d, which has users %s.\n", amount,
							toAccountNumber, recipentNames);
					System.out.println("Please enter yes to confirm.");
					String userInput = scanner.nextLine();
					if (userInput.equalsIgnoreCase("YES")) {
						break; // if user confirms, break the while loop
					} else {
						continue;
					}
				}

			} // end while loop

			System.out.println("Enter the account pin: ");
			int accountPin = scanner.nextInt();
			scanner.nextLine();

			// create a transfer message
			// int id, Status status, int accountNumber, double transferAmount, int pin
			TransferMessage txMsg = new TransferMessage(Status.ONGOING, fromAccountNumber, toAccountNumber, amount,
					accountPin);
			// send a message to server
			writer.writeUnshared(txMsg);

			// wait for a transfer message receipt
			TransferMessage txMsgReceipt = (TransferMessage) reader.readObject();

			if (txMsgReceipt.getStatus() == Status.SUCCESS) {
				// expect a new Account object
				BankAccount newAccount = (BankAccount) reader.readObject();
				// update accounts
				accounts.put(fromAccountNumber, newAccount);
				// print out newAccount
				System.out.println("New Account: " + newAccount);
				if (toAccountUsers.contains(user.getId())) { // in case transfer between your accounts
					// expect another bankaccount obj
					BankAccount recipientAccount = (BankAccount) reader.readObject();
					accounts.put(toAccountNumber, recipientAccount);
				}
				// print out all account info
				System.out.println("Updated user id [" + loggedInUserId + "] accounts: " + accounts);
			} else {
				System.out.println("Fail to transfer $" + amount + " from account " + fromAccountNumber + " to account "
						+ toAccountNumber);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	} // end of transfer method


	public void addTeller() {
		scanner = new Scanner(System.in);

		System.out.println("Enter name: ");
		String name = scanner.nextLine();

		System.out.println("Enter password: ");
		String password = scanner.nextLine();

		// create new teller message
		TellerMessage msg = new TellerMessage(Status.ONGOING, name, password);
		try {
			// send message to server
			writer.writeUnshared(msg);

			// wait for success status
			TellerMessage msgReceipt = (TellerMessage) reader.readObject();

			if (msgReceipt.getStatus() == Status.SUCCESS) {
				System.out.println("A new teller account is created successfully.");

				// receive a Teller obj
				Teller newTeller = (Teller) reader.readObject();

				System.out.println("New teller: \n" +  newTeller);
			} else {
				System.out.println("Fail to create a new teller account.");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	} // end method addTeller

	public Map<Integer, String> getTellersInfo() {
		Map<Integer, String> tellersInfo = new HashMap<>();

		TellerMessage msg = new TellerMessage(Status.ONGOING, TellerMessageType.TELLERS_INFO);
		try {
			// send teller message with type TELLERS_INFO
			writer.writeUnshared(msg);

			// wait for success status
			TellerMessage msgReceipt = (TellerMessage) reader.readObject();

			if (msgReceipt.getStatus() == Status.SUCCESS) {

				Map<String, String> info = msgReceipt.getInfo();
				for (String key : info.keySet()) {
					// add info to tellersInfo
					tellersInfo.put(Integer.parseInt(key), info.get(key)); // tellerId -> teller name
				}

			} else { // status error
				System.out.println("Fail to get all tellers info.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return tellersInfo;
	} // end method getTellersInfo

	public void deleteTeller() {

		scanner = new Scanner(System.in);

		// get the list of tellers: tellerId + name
		Map<Integer, String> tellersInfo = getTellersInfo();
		// show only non-admin tellers
		System.out.println("List of non-admin tellers: \ntellerId:teller name");
		for (int tellerId : tellersInfo.keySet()) {
			if (tellerId != teller.getId()) {
				System.out.println(tellerId + ":" + tellersInfo.get(tellerId));
			}
		}

		System.out.println("Please enter the id of the teller to remove: ");
		int tempTellerId = scanner.nextInt();
		scanner.nextLine();

		TellerMessage msg = new TellerMessage(Status.ONGOING, tempTellerId);
		try {
			// send TellerMessage to server
			writer.writeUnshared(msg);

			// wait for msgReceipt
			TellerMessage msgReceipt = (TellerMessage) reader.readObject();

			if (msgReceipt.getStatus() == Status.SUCCESS) {

				System.out.println("Successfully remove teller with id: " + tempTellerId);

			} else {
				System.out.println("Fail to remove teller with id: " + tempTellerId);
			}


		} catch (Exception e) {
			e.printStackTrace();
		}

	} // end method deleteTeller

	public void addUserToAccount(int loggedInUserID) {

		try {
			while (true ) {
				scanner = new Scanner(System.in);
				System.out.println("Enter account number to add a user: ");
				int accNum = scanner.nextInt();
				scanner.nextLine();
				System.out.println("Enter user id to be added: ");
				int userIdAdd = scanner.nextInt();
				scanner.nextLine();

				System.out.println("Please type yes to confirm your information below.");
				System.out.printf("user id: %s\nAccount Number: %s\n", userIdAdd, accNum);

				String userConfirm = scanner.nextLine();

				if (userConfirm.equalsIgnoreCase("YES")) {
					// check user id is valid.
					// create UserInfoMessage and send to server to check
					UserInfoMessage uiMsg = new UserInfoMessage(Status.ONGOING, userIdAdd);
					writer.writeUnshared(uiMsg);
					// wait for a user info message receipt
					UserInfoMessage uiMsgReceipt = (UserInfoMessage) reader.readObject();

					// check if the logged in user owns the account.
					AccountMessage accMsg = new AccountMessage(Status.ONGOING, AccountMessageType.CHK_OWN, loggedInUserID, accNum);
					writer.writeUnshared(accMsg);
					// wait for a user info message receipt
					AccountMessage accMsgReceipt = (AccountMessage) reader.readObject();

					// send accMsg to check admin status
					AccountMessage accMsgAdmin = new AccountMessage(Status.ONGOING, AccountMessageType.CHK_ACC_ADM, loggedInUserID, accNum);
					writer.writeUnshared(accMsgAdmin);
					// wait for accMsgAdmin receipt
					AccountMessage accMsgAdminReceipt = (AccountMessage) reader.readObject();

					if (uiMsgReceipt.getStatus() == Status.SUCCESS
							&& accMsgReceipt.getStatus() == Status.SUCCESS
							&& accMsgAdminReceipt.getStatus() == Status.SUCCESS) {

						accMsg = new AccountMessage(Status.ONGOING, AccountMessageType.ADD_USER_TO_ACC, userIdAdd, accNum);
						writer.writeUnshared(accMsg);
						// expect BankUser Object from server
						BankUser userToadd = (BankUser) reader.readObject();
						// add user to accounts
						accounts.get(accNum).addUser(userToadd);
						// show updated accounts
						System.out.println("Successfully added user id [" + userIdAdd + "] to the account #" + accNum);
						// print out all account info
						System.out.println("Updated user id [" + loggedInUserID + "] accounts: " + accounts);
					} else { // if user confirm was not YES
						System.out.println("Fail to add user id [" + userIdAdd + "] to the account #" + accNum);
					}
					// break while loop
					break;
				} else { // if user confirm was not YES
					System.out.println("Fail to create a new user. Please try again.");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	} // end method addUserToAccount

	public void deleteUserFromAccount(int loggedInUserID) {
		try {
			while (true) {
				scanner = new Scanner(System.in);
				System.out.println("Enter account number to delete a user: ");
				int accNum = scanner.nextInt();
				scanner.nextLine();
				System.out.println("Enter user id to be deleted: ");
				int userIdRem = scanner.nextInt();
				scanner.nextLine();

				System.out.println("Please type yes to confirm your information below.");
				System.out.printf("user id: %s\nAccount Number: %s\n", userIdRem, accNum);
				String userConfirm = scanner.nextLine();

				if (userConfirm.equalsIgnoreCase("YES")) {
					// check if the logged in user owns the account.
					AccountMessage acmOwn = new AccountMessage(Status.ONGOING, AccountMessageType.CHK_OWN, loggedInUserID, accNum);
					writer.writeUnshared(acmOwn);
					// wait for a user info message receipt
					AccountMessage acmReceipt = (AccountMessage) reader.readObject();

					// check if removing user owns the account.
					AccountMessage acmRem = new AccountMessage(Status.ONGOING, AccountMessageType.CHK_OWN, userIdRem, accNum);
					writer.writeUnshared(acmRem);
					// wait for a user info message receipt
					AccountMessage acmRemReceipt = (AccountMessage) reader.readObject();

					// send accMsg to check admin status
					AccountMessage accMsgAdmin = new AccountMessage(Status.ONGOING, AccountMessageType.CHK_ACC_ADM, loggedInUserID, accNum);
					writer.writeUnshared(accMsgAdmin);
					// wait for accMsgAdmin receipt
					AccountMessage accMsgAdminReceipt = (AccountMessage) reader.readObject();

					// check both msgs' status
					if (acmReceipt.getStatus() == Status.SUCCESS
							&& acmRemReceipt.getStatus() == Status.SUCCESS
							&& accMsgAdminReceipt.getStatus() == Status.SUCCESS) {

						acmRem = new AccountMessage(Status.ONGOING, AccountMessageType.REM_USER_FROM_ACC, userIdRem, accNum);
						writer.writeUnshared(acmRem);
						// expect BankUser Object from server
						BankUser userRem = (BankUser) reader.readObject();
						// remove user to accounts
						accounts.get(accNum).deleteUser(userRem);
						// show updated accounts
						System.out.println("Successfully removed user id [" + userIdRem + "] from the account #" + accNum);
						// print out all account info
						System.out.println("Updated user id [" + loggedInUserID + "] accounts: " + accounts);
					} else {
						System.out.println("Fail to delete user id [" + userIdRem + "] from the account #" + accNum);
					}
					break;
				} else { // if user confirm was not YES
					System.out.println("Fail to create a new user. Please try again.");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	} // end method deleteUserFromAccount

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
					
					System.out.println(String.format("Log in as %s(%d)\n", user.getName(), user.getId()));
					
					// redirect to login in as user page
					loginUserAccount(user.getId());
					break; // break while loop

				} else { // if userConfirm is not YES

					System.out.println("Fail to create a new user. Please try again.");

				}
			} // end while loop

		} catch (Exception e) {
			e.printStackTrace();
		}
	} // end method createUser

	public void openLogs() {
		TellerMessage msg = new TellerMessage(Status.ONGOING, TellerMessageType.VIEW_LOGS);
		TellerMessage msgReceipt = null;
		List<String> logs = null;

		try {
			// send msg to server
			writer.writeUnshared(msg);

			// wait for success status
			msgReceipt = (TellerMessage) reader.readObject();

			if (msgReceipt.getStatus() == Status.SUCCESS) {

				logs = msgReceipt.getLogs();

			} else {

				System.out.println("Fail to get logs from server.");

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (logs != null) {
			System.out.println("Logs: ");
			for (String s : logs) {
				System.out.println(s);
			}
			System.out.println();
		}
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
			//System.out.println("accounts: " + user.getAccounts());
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
				case 0: createAccount(userId); break; // create new account
				case 1: deleteAccount(userId); break; // remove account
				case 2: addUserToAccount(userId); break; // add user to account
				case 3: deleteUserFromAccount(userId); break; // remove user from account
				case 4: transferAdmin(); break; // transfer admin
				case 5: changePin(); break; // change pin
				case 6: forgetPassword(); break; // forget password
				case 7: withdraw(userId); break; // withdraw
				case 8: deposit(userId); break; // deposit
				case 9: transfer(userId); break; // transfer
				case 10:
					isLoggedOut = true;
					LogoutMessage msg = logoutRequest();
					writer.writeUnshared(msg);
					LogoutMessage msgBack = (LogoutMessage) reader.readObject();
					if (msgBack.getStatus() == Status.SUCCESS) {
						System.out.println("Logout was a success.\nReturning to teller main.");
						accounts.clear();
						break;
					} else {
						System.out.println("Logout failed, continue as User ID: " + userId);
						break;
					}

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
				//				if (accounts == null || accounts.get(accountNumber) == null) {}// update only when it's not available

				// create new AccountMessage requesting account info
				// int id, Status status, int accountNumber, int currUserId, int pin,
				// AccountMessageType type
				AccountMessage msg = new AccountMessage(Status.ONGOING, accountNumber, user.getId(), -1,
						AccountMessageType.ACCOUNT_INFO);
				// send message
				writer.writeUnshared(msg);
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

		} catch (Exception e) {
			e.printStackTrace();
		}


	} // end method getAccountsInfo

	public void checkTeller() {
		// needs clarification
	}

	public void createAccount(int loggedInUserId) {
		scanner = new Scanner(System.in);
		int choice;
		AccountType accountType;
		int pin;

		while (true) {
			// prompt the user to choose account type
			while (true) {
				System.out.println("Choose an account type or enter 2 to cancel: \n0-checking\n1-saving");
				try {
					choice = scanner.nextInt();
					scanner.nextLine();
					if (choice == 0) { // checking account
						accountType = AccountType.CHECKING;
						break; // break the while loop
					} else if (choice == 1) {
						accountType = AccountType.SAVING;
						break;
					} else if (choice == 2) { // quit createAccount method
						return;
					}
				} catch (Exception e) {} // do nothing

				System.out.println("Invalid input. Please enter 0 or 1.");

			} // end while loop


			// enter pin
			while (true) {
				System.out.println("Enter pin (only digits): ");
				try {
					pin = scanner.nextInt();
					scanner.nextLine();
					if (pin > 0) {
						break;
					}
				} catch (Exception e) {
					System.out.println("Invalid pin. Please try again.");
					scanner.nextLine(); // consume the invalid input
				}
			} // end while loop

			// confirm
			System.out.printf("You are creating a %s account with pin %d.\n", accountType, pin);
			System.out.println("Please enter yes to confirm.");
			String userInput = scanner.nextLine();
			if (userInput.equalsIgnoreCase("YES")) {
				break; // if user confirms, break outer while loop
			} else {
				continue;
			}
		} // end outer while loop


		try {
			// send AccountMessage of to server: Status status, AccountType accountType, int pin
			AccountMessage msg = new AccountMessage(Status.ONGOING, accountType, pin);
			writer.writeUnshared(msg);

			// wait for success status and new Bank Account number
			AccountMessage msgReceipt = (AccountMessage) reader.readObject();

			if (msgReceipt.getStatus() == Status.SUCCESS) {
				int accountNumber = Integer.parseInt(msgReceipt.getInfo().get("accountNumber"));
				user.getAccounts().add(accountNumber);

				// getAccountsInfo to get the newly created account to accounts
				getAccountsInfo();
				// print out all account info
				System.out.println("Updated user id [" + loggedInUserId + "] accounts: " + accounts);

			} else {
				System.out.printf("Fail to create a new %s account.\n", accountType);
				return;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	} // end method createAccount

	// delete account if the balance is zero, else fail to delete
	public void deleteAccount(int loggedInUserId) {
		scanner = new Scanner(System.in);
		System.out.println("Enter Account number: ");
		int accountNumber = scanner.nextInt();
		scanner.nextLine();


		// check if this account number is valid and account balance is not zero and whether the user has admin access to this account
		if (user.getAccounts().contains(accountNumber) 
				&& accounts.get(accountNumber).getBalance() == 0
				&& accounts.get(accountNumber).getAdminID() == user.getId()) {

			// send AccountMessage to server
			AccountMessage msg = new AccountMessage(Status.ONGOING, accountNumber, user.getId());
			try {
				writer.writeUnshared(msg);
				// wait for success status
				AccountMessage msgReceipt = (AccountMessage) reader.readObject();

				if (msgReceipt.getStatus() == Status.SUCCESS) {
					// update accounts, user.accounts
					user.getAccounts().remove(Integer.valueOf(accountNumber));
					accounts.remove(accountNumber);
					// print out success msg
					System.out.println("Successfully remove account #" + accountNumber + ".");
					// print out all account info
					System.out.println("Updated user id [" + loggedInUserId + "] accounts: " + accounts);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}



		} else {
			System.out.println("Invalid accountNumber or your balance is not zero or you are not the admin of this account.");
		}



	} // end method deleteAccount


	public void forgetPassword() {
		scanner = new Scanner(System.in);
		while (true) {
			System.out.println("Enter birthday: ");
			String birthday = scanner.nextLine();

			System.out.println("Enter new password: ");
			String password = scanner.nextLine();

			System.out.println("Please type yes to confirm that your new password is " + password);
			String input = scanner.nextLine();
			if (input.equalsIgnoreCase("YES")) {
				try {

					// create new AccountMessage with type CHG_PWD
					AccountMessage msg = new AccountMessage(Status.ONGOING, birthday, password);

					// send to server
					writer.writeUnshared(msg);

					// wait for success status
					AccountMessage msgReceipt = (AccountMessage) reader.readObject();

					if (msgReceipt.getStatus() == Status.SUCCESS) {
						System.out.println("Your password is changed successfully.");
					} else {
						System.out.println("Wrong birthday. Fail to change your password.");
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

				break; // break the while loop
			}
		}
	}

	public void changePin() {

		scanner = new Scanner(System.in);
		System.out.println("Choose an account: ");
		int accountNumber = scanner.nextInt();

		System.out.println("Enter new pin: ");
		int pin = scanner.nextInt();
		scanner.nextLine();

		System.out.println("Type Yes to confirm your new pin is " + pin);
		String input = scanner.nextLine();
		if (input.equalsIgnoreCase("YES")) {
			try {
				// create AccountMessage with type CHG_PIN to server
				// Status status, int userId, int accountNumber, int pin
				AccountMessage msg = new AccountMessage(Status.ONGOING, user.getId(), accountNumber, pin);

				// send to server
				writer.writeUnshared(msg);

				// wait for success status
				AccountMessage msgReceipt = (AccountMessage) reader.readObject();

				if (msgReceipt.getStatus() == Status.SUCCESS) {
					System.out.println("Your pin is changed successfully.");
					// update account pin locally (optional)
					accounts.get(accountNumber).setAccountPin(pin);
				} else {
					System.out.println("Fail to change your pin.");
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}


	} // end method changePin

	public void transferAdmin() {
		scanner = new Scanner(System.in);

		// 1. get latest updates of all accounts
		getAccountsInfo();
		// initialize map: key: accountNumber -> value: Map<userId, user name>
		Map<Integer, Map<Integer, String>> adminAccountsInfo = new HashMap<>();
		// 1.1 for all accounts:
		for (int accountNumber : user.getAccounts()) {

			// if it is admin account (userId: username)
			if (accounts.get(accountNumber).getAdminID() == user.getId()) {
				BankAccount tempAccount = accounts.get(accountNumber);
				// for each user:
				for (int userId : tempAccount.getUsers()) {
					// send user info msg to server
					UserInfoMessage uiMsg = new UserInfoMessage(Status.ONGOING, userId);

					try {
						writer.writeUnshared(uiMsg);
						// expected SUCCESS of user info msg, which contains user name
						UserInfoMessage uiMsgReceipt = (UserInfoMessage) reader.readObject();
						if (uiMsgReceipt.getStatus() == Status.SUCCESS) {
							if (!adminAccountsInfo.containsKey(accountNumber)) {
								adminAccountsInfo.put(accountNumber, new HashMap<>());
							} 
							// add username to adminAccountsInfo
							adminAccountsInfo.get(accountNumber).put(userId, uiMsgReceipt.getUserName());
						}

					} catch (Exception e) {
						e.printStackTrace();
					}

				} // end for loop
			}

		}
		// 1.2 list out all admin accounts
		System.out.println("Please choose one of the admin accounts to transfer admin: ");
		System.out.println(adminAccountsInfo.keySet());


		// 2. choose admin account
		int accountNumber = scanner.nextInt();
		// 2.1 show all users of this account
		System.out.println("Please choose one of the users to transfer: ");
		System.out.println(adminAccountsInfo.get(accountNumber));

		// 3. choose recipient user id
		int recipientId = scanner.nextInt();

		// 4. enter pin
		System.out.println("Enter account pin: ");
		int pin = scanner.nextInt();

		// 5. send transfer admin request to server -> AccountMessage of type TXF_ADMIN
		// Status status, int userId, int accountNumber, int pin, int recipientId
		AccountMessage msg = new AccountMessage(Status.ONGOING, user.getId(), accountNumber, pin, recipientId);

		try {
			// send to server
			writer.writeUnshared(msg);

			// wait for success status
			AccountMessage msgReceipt = (AccountMessage) reader.readObject();

			if (msgReceipt.getStatus() == Status.SUCCESS) {
				System.out.printf("For you account %d, the admin is transferred to %s successfully.\n", accountNumber, adminAccountsInfo.get(accountNumber).get(recipientId));
				// update account pin locally (optional)
				accounts.get(accountNumber).setAccountPin(pin);

				// 6. update account admin locally
				accounts.get(accountNumber).setAdminID(recipientId);

			} else {
				System.out.printf("For you account %d, failed to transfer admin to %s.\n", accountNumber, adminAccountsInfo.get(accountNumber).get(recipientId));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}



	} // end method transferAdmin

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
			writer.writeUnshared(clientHello);
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
				System.out.println("Enter your teller id or type exit to quit: ");
				String input = null;
				try {
					input = scanner.nextLine();
					tellerId = Integer.parseInt(input);
					break;
				} catch (Exception e) {
					if (input.equalsIgnoreCase("EXIT")) {
						ExitMessage msg = new ExitMessage(Status.ONGOING);
						try {
							writer.writeUnshared(msg);
							// wait for success status
							ExitMessage msgReceipt = (ExitMessage) reader.readObject();
							if (msgReceipt.getStatus() == Status.SUCCESS) {
								System.out.println("Teller client exiting...");
								// a good way to exit
								System.exit(0);
								System.out.println("after system exit");
							}
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						
					} else {
						System.out.println("Error: your teller id is an integer. Please try again.");						
					}
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

		tellerMenu();
	}

	public void tellerMenu() {
		scanner = new Scanner(System.in);
		accounts = new HashMap<>();
		boolean flag = false; // flag to quit the session
		if (teller.getAdmin()) { // if this is an admin teller
			// switch statement for admin teller
			while (!flag) {
				System.out.println("0-Add-New-Teller\n1-Delete-Teller\n2-View-logs\n3-Create-New-User\n4-Login-User-Account\n5-LogOut");
				int choice = scanner.nextInt();
				scanner.nextLine();

				switch (choice) {
				case 0: addTeller(); break; // add teller
				case 1: deleteTeller(); break; // delete teller
				case 2: openLogs(); break; // view logs
				case 3: createUser(); break; // create new user
				case 4: loginUserAccount(); break; // login user account
				case 5:
					

					try {

						LogoutMessage logoutMsg = new LogoutMessage(Status.ONGOING, LogoutMessageType.TELLER);
						writer.writeUnshared(logoutMsg);
						LogoutMessage logoutMsgReceipt = (LogoutMessage) reader.readObject();

						if (logoutMsgReceipt.getStatus() == Status.SUCCESS) {
							flag = true;
							System.out.println("Logout was a success.\nReturning to teller login.\n");
						} else {
							System.out.println("Logout failed.");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					newSession();

					break; // logout
					
				default: break;
				}

			} // end while loop
		} else {
			// switch statement for normal teller
			while (!flag) {
				System.out.println("0-Create-New_User\n1-Login-User-Account\n2-LogOut");
				int choice = scanner.nextInt();
				scanner.nextLine();

				switch (choice) {
				case 0: createUser(); break; // create new user
				case 1: loginUserAccount(); break;
				case 2:
					

					try {

						LogoutMessage logoutMsg = new LogoutMessage(Status.ONGOING, LogoutMessageType.TELLER);
						writer.writeUnshared(logoutMsg);
						LogoutMessage logoutMsgReceipt = (LogoutMessage) reader.readObject();

						if (logoutMsgReceipt.getStatus() == Status.SUCCESS) {
							flag = true;
							System.out.println("Logout was a success.\nReturning to teller login.");
						} else {
							System.out.println("Logout failed.");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					System.out.println("Hit newSession");
					newSession();
					System.out.println("after newSession");
					
					break;
				default: break;
				}

			} // end while loop
		}
	}

	public void go() {
		try {
			// start client
			setUpConnection();

			// handshake with server: Teller client hello
			handshake();

			newSession();

			// close client
			closeConnection();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public LogoutMessage logoutRequest() {

		return new LogoutMessage(Status.ONGOING);

	}

	// Driver
	public static void main(String[] args) {
		// create new tellerclient
		TellerClient client = new TellerClient();
		client.go();

	}
}