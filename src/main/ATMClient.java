package main;

import java.net.*;
import java.io.*;
import message.*;
import java.util.*;

public class ATMClient {
    Socket sock = null;
    ObjectInputStream reader = null;
    ObjectOutputStream writer = null;
    Scanner scanner = null;
    BankUser user = null;
    Map<Integer, BankAccount> accounts = null; // map of account number to account object

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

    public int withdraw(int id) {
        try {
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
            // int id, Status status, int accountNumber, double withdrawAmount, int pin
            WithdrawMessage msg = new WithdrawMessage(id, Status.ONGOING, accountNumber, amount, accountPin);
            // send message to server
            writer.reset();
            writer.writeObject(msg);
            // wait for withdraw message receipt
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

        return ++id;
    } // end method withdraw

    public int deposit(int id) {
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
    DepositMessage msg = new DepositMessage(id, Status.ONGOING, accountNumber, amount,
    accountPin);
    // send a message to server
    writer.writeObject(msg);

    // wait for a deposit message receipt
    DepositMessage msgReceipt = (DepositMessage) reader.readObject();

    if (msgReceipt.getStatus() == Status.SUCCESS) {
    // expect a new Account object
    BankAccount newAccount = (BankAccount) reader.readObject();
    // update accounts
    accounts.replace(accountNumber, newAccount);
    } else {
  
      System.out.println("Fail to deposit $" + amount + " to account " +
    accountNumber);
    }
    } catch (Exception e) {
    e.printStackTrace();
    }

    return ++id;

    } // end method deposit

    public int transfer(int id) {
      try {
        // get inputs: acc#, amount, pin
        scanner = new Scanner(System.in);
        System.out.println("Enter your account number: ");
        int fromAccountNumber = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Enter recipient account number: ");
        int toAccountNumber = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Enter the amount to transfer: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Enter the account pin: ");
        int accountPin = scanner.nextInt();
        scanner.nextLine();
        
        // create a transfer message
        // int id, Status status, int accountNumber, double transferAmount, int pin
        TransferMessage msg = new TransferMessage(id, Status.ONGOING, fromAccountNumber, toAccountNumber, amount, accountPin);
        // send a message to server
        writer.writeObject(msg);
        
        // wait for a transfer message receipt
        TransferMessage msgReceipt = (TransferMessage) reader.readObject();
        
        if (msgReceipt.getStatus() == Status.SUCCESS) {
          // expeect a new Account object
          BankAccount newAccount = (BankAccount) reader.readObject();
          // update accounts
          accounts.put(fromAccountNumber, newAccount);
          // print out newAccount
          System.out.println("new Account: " + newAccount);
          // show updated accounts
          System.out.println("Updated accounts: " + accounts);
        } else {
          System.out.println("Fail to transfer $" + amount + " from account " + fromAccountNumber + " to account " + toAccountNumber);
        }
        
      } catch (Exception e) {
        e.printStackTrace();
      }

      return ++id;
    } // end method transfer

    public void go() {

        try {
            // start client
            setUpConnection();
            int id = 0; // initialize message id
            id = handshake(id); // handshake with server

            id = newSession(id);

            // close client
            closeConnection();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public int newSession(int id) {
        // reset variables
        user = null;
        accounts = new HashMap<Integer, BankAccount>();

        try {

            // ATM login
            while (true) {
                // create new login message
                LoginMessage loginMessage = loginRequest(++id);
                // send login message to server
                writer.writeObject(loginMessage);

                // wait for loginMessage from server
                LoginMessage loginReceipt = (LoginMessage) reader.readObject();
                id++;
                if ((loginReceipt.getStatus() == Status.SUCCESS)) { // if success, break while loop
                    break;
                }

                System.out.println("Wrong user id or password. Please try again.\n");
            }

            // wait for bankuser object from server
            user = (BankUser) reader.readObject();
            id++;
            // print out user object
            System.out.println("current user: ");
            System.out.println(user);

            id = getAccountsInfo(id);
            // print out all accounts
            System.out.println("accounts: " + accounts);

            // ATM withdraw
            id = withdraw(id);

            // codes go here...
            // while loop
            // two options: ATM Login, exit

            // exit

            // ATM login: while loop
            // Four options: deposit, withdraw, transfer, logout

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ++id;
    }

    public int getAccountsInfo(int id) {
        try {
            // request info from all accounts of current user
            for (int accountNumber : user.getAccounts()) {
                if (accounts == null || accounts.get(accountNumber) == null) { // update only when it's not available

                    // create new AccountMessage requesting account info
                    // int id, Status status, int accountNumber, int currUserId, int pin,
                    // AccountMessageType type
                    AccountMessage msg = new AccountMessage(++id, Status.ONGOING, accountNumber, user.getId(), -1,
                            AccountMessageType.ACCOUNT_INFO);
                    // send message
                    writer.writeObject(msg);
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
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return id++;

    } // end method getAccountsInfo

    public int handshake(int id) {
        // handshake with server: ATM client hello
        // public HelloMessage(int id, String text, String to, String from, MessageType
        // type, Status status)
        HelloMessage clientHello = new HelloMessage(id, "ATM", Status.ONGOING);
        try {
            // send message
            writer.writeObject(clientHello);
            // wait for message receipt
            HelloMessage serverHello = (HelloMessage) reader.readObject();
            if (serverHello.getID() == ++id && serverHello.getStatus() == Status.SUCCESS) {
                System.out.println("client-server handshake successfully");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return id++;
    } // end method handshake

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

        // LoginMessage(int id, String to, String from, Status status, int userId,
        // String password)
        return new LoginMessage(id, Status.ONGOING, userId, password);

    }

    public static void main(String[] args) {
        ATMClient client = new ATMClient();
        client.go();
    }
}
