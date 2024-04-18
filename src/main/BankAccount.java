package main;
import message.AccountType;
import java.util.*;

public class BankAccount {
	private static int count = 0;
	
	private final int accountNumber;
	private final AccountType accountType;
	private int accountPin;
	private double balance;
	private List<BankUser> users;
	private int adminID;
	
	// Constructor
	BankAccount(int accountPin, double balance, AccountType accountType, int adminID) {
		this.accountNumber = ++count;
		this.accountPin = accountPin;
		this.balance = balance;
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
		return this.users;
	}
	
	public boolean deleteUser(BankUser user) {
		this.users.remove(user);
		return true;
	}
	
}
