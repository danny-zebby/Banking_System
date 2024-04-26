package main;

import message.AccountType;
import java.util.*;
import java.io.Serializable;

public class BankAccount implements Serializable {
  private static int count = 0;

  private final int accountNumber;
  private final AccountType accountType;
  private int accountPin;
  private double balance;
  private List<Integer> users;
  private int adminID; // the user id of the admin user

  // Constructor
  public BankAccount(int accountPin, AccountType accountType, int adminID) {
    this.accountNumber = ++count;
    this.accountPin = accountPin;
    this.balance = 0;
    this.accountType = accountType;
    this.users = new ArrayList<Integer>();
    this.adminID = adminID;
  }

  // Constructor
  public BankAccount(int accountPin, AccountType accountType, int adminID, double balance) {
    this.accountNumber = ++count;
    this.accountPin = accountPin;
    this.accountType = accountType;
    this.users = new ArrayList<Integer>();
    this.adminID = adminID;
    this.balance = balance;
  }

  // Getters & Setters
  public int getAccountNumber() {
    return this.accountNumber;
  }

  public int getAccountPin() {
    return this.accountPin;
  }

  public void setAccountPin(int newPin) {
    this.accountPin = newPin;
  }

  public double getBalance() {
    return this.balance;
  }

  public void setBalance(double balance) {
    this.balance = balance;
  }

  public AccountType getAccountType() {
    return this.accountType;
  }

  public void addUser(BankUser user) {
    this.users.add(user.getId());
  }

  public List<Integer> getUsers() {
    return this.users;
  }

  public boolean deleteUser(BankUser user) {
    this.users.remove(user.getId());
    return true;
  }

  public int getAdminID() {
    return this.adminID;
  }

  public void setAdminID(int adminID) {
    this.adminID = adminID;
  }

  public String toStringNoBalance() {
	    return "BankAccount info:\n" +
	        "accountNum: " + this.accountNumber + "\n" +
	        "accountType: " + this.accountType + "\n" +
	        "balance: ***.** \n" +
	        "users: " + this.users + "\n";
	  }
  
  public String toString() {
    return "BankAccount info:\n" +
        "accountNum: " + this.accountNumber + "\n" +
        "accountType: " + this.accountType + "\n" +
        "balance: " + String.format("%.2f", this.balance) + "\n" +
        "users: " + this.users + "\n";
  }
}
