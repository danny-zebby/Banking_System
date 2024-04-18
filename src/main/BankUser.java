package main;
import java.util.*;

public class BankUser implements Comparable<BankUser> {
	private static int count = 0;
	
	private String name;
	private String birthday;
	private String password;
	private final int id;
	private double cash;
	private List<BankAccount> accounts;
	
	public BankUser(String name, String birthday, String password) {
		this.name = name;
		this.birthday = birthday;
		this.password = password;
		this.id = ++count;
		accounts = new ArrayList<BankAccount>();
	}
	
	public int compareTo(BankUser one) {
		return this.id - one.id;
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
		return cash;
	}

	public void setCash(double cash) {
		this.cash = cash;
	}

	public List<BankAccount> getAccounts() {
		return accounts;
	}

	public void addAccount(BankAccount account) {
		this.accounts.add(account);
	}
	
	public boolean deleteAccount(BankAccount account) {
		this.accounts.remove(account);
		return true;
	}

	public int getId() {
		return id;
	}
	
	
}
