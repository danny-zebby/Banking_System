
public class BankAccount {
	private int accountNumber;
	private int accountPin;
	private float balance;
	private Status accountState;
	private int admin;
	
	// Constructor
	BankAccount(int an, int ap, float b, Status s, int a) {
		this.accountNumber = an;
		this.accountPin = ap;
		this.balance = b;
		this.accountState = s;
		this.admin = a;
	}
	
	// Getters & Setters
	public int getAccountNumber() {
		return this.accountNumber;
	}
	public void setAccountNumber(int newAN) {
		accountNumber = newAN;
	}
	
	public int getAccountPin() {
		return this.accountPin;
	}
	public void setAccountPin() {
		
	}
	
	

}
