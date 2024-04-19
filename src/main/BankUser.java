package main;
import java.util.*;
import java.io.Serializable;

public class BankUser implements Serializable {
	private static int count = 0;
	
	private String name;
	private String birthday;
	private String password;
	private final int id;
	private double cash;
	private List<Integer> accounts;
	
	public BankUser(String name, String birthday, String password) {
		this.name = name;
		this.birthday = birthday;
		this.password = password;
		this.id = ++count;
		this.cash = 0;
		accounts = new ArrayList<Integer>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public double getCash() {
		return this.cash;
	}

	public void setCash(double cash) {
		this.cash = cash;
	}

	public List<Integer> getAccounts() {
		Collections.sort(accounts);
		return accounts;
	}

	public void addAccount(BankAccount account) {
		this.accounts.add(account.getAccountNumber());
		account.addUser(this);
	}
	
	public boolean deleteAccount(BankAccount account) {
		this.accounts.remove(account.getAccountNumber());
		return true;
	}

	public int getId() {
		return id;
	}
	
	public String toString() {
		return "BankUser info:\n" +
				"name: " + this.name + "\n" + 
				"birthday: " + this.birthday + "\n" + 
				"user id: " + this.id + "\n" + 
				"cash: " + String.format("%.2f", this.cash) + "\n" + 
				"accounts: " + this.accounts + "\n";
	}
	
	public static void main(String[] args) {
		String n1 = "BOB";
		String b1 = "01/23/45";
		String p1 = "abcd1234";
		BankUser BU1 = new BankUser(n1,b1,p1);
		System.out.println(BU1);
	}
}
