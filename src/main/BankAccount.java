package main;
import message.AccountType;
import java.util.*;

public class BankAccount implements Comparable<BankAccount>{
	private static int count = 0;
	
	private final int accountNumber;
	private final AccountType accountType;
	private int accountPin;
	private double balance;
	private List<BankUser> users;
	private int adminID; // the user id of the admin user
	
	// Constructor
	public BankAccount(int accountPin, AccountType accountType, int adminID) {
		this.accountNumber = ++count;
		this.accountPin = accountPin;
		this.balance = 0;
		this.accountType = accountType;
		this.users = new ArrayList<BankUser>();
		this.adminID = adminID;
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
		this.users.add(user);
	}
	
	public List<BankUser> getUsers() {
		Collections.sort(users);
		return this.users;
	}
	
	public boolean deleteUser(BankUser user) {
		this.users.remove(user);
		return true;
	}
	
	public int getAdminID() {
		return this.adminID;
	}
	
	public void setAdminID(int adminID) {
		this.adminID = adminID;
	}
	
	public int compareTo(BankAccount other) {
		return this.accountNumber - other.accountNumber;
	}
	
}
