import java.io.*;

public class ATM {
	private float money;
	private int admin;
	private BankUser user;
	private BankUser account;
	
	public void setMoney(float money) {
		 this.money = money;
	}
	
	public void setAdmin(int admin) {
		this.admin = admin;
	}
	
	public void setUser(BankUser user) {
		this.user = user;
	}
	
	public float getMoney() {
		return this.money;
	}
	
	public int getAdmin() {
		return this.admin;
	}
	
	public void getUser() {
		return this.user;
	}
	
	public void deposit(float money) {
		money += account;
	}
	
	public void withdraw() {
		// insert code here
	}
	
	public void transfer() {
		// insert code here
	}
}
