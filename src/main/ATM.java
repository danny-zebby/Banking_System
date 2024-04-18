import java.io.*;
import java.util.*;

public class ATM {
	private float money;
	private int admin;
	// temporary
	private BankUser user;
	private BankUser account;
	private BankAccount accountPin;
	private BankAccount balance;
	Scanner scan;
	
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
		// Console purposes, may change once GUI is implemented
		scan = new Scanner(System.in);
		System.out.println("Enter Amount: ");
		String num = scan.nextLine();
		money = Float.parseFloat(num);
		System.out.println("Enter pin: ");
		String pin = scan.nextLine();
		if (pin == ) {
			money += account;
			System.out.println("Transaction Successful.");
		}
		else {
			System.out.println("Error, pin is incorrect. Please try again.");
		}
	}
	
	public void withdraw() {
		// Console purposes, may change once GUI is implemented
		scan = new Scanner(System.in);
		System.out.println("Enter Amount: ");
		String num = scan.nextLine();
		money = Float.parseFloat(num);
		System.out.println("Enter pin: ");
		String pin = scan.nextLine();
		if (pin == accountPin) {
			money -= account;
			System.out.println("Transaction Successful");
		}
		else {
			System.out.println("Error, pin is incorrect. Please try again.");
		}
	}
	
	public void transfer() {
		scan = new Scanner(System.in);
		System.out.println("Enter Receiver's Account Number: ");
		String accnum = scan.nextLine();
		
		System.out.println("Enter Amount: ");
		String num = scan.nextLine();
		money = Float.parseFloat(num);
		System.out.println("Is this correct?" + "\n" + accnum + "\n" + balance + "\n");
		String text = scan.nextLine();
		text.toUpperCase();
		if (text == "YES") {
			System.out.println("Enter pin: ");
			String pin = scan.nextLine();
			if (pin == ) {
				System.out.println("Transaction Successful");
			} else {
				System.out.println("Error, pin is incorrect. Please try again.");
			}
		} else {
			transfer();
		}
	}
}