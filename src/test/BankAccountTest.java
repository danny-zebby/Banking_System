package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import main.BankAccount;
import message.AccountType;

class BankAccountTest {

	@Test
	@Order(1)
	@DisplayName("Constructor")
	void test1() {
		int pin1 = 1234;
		int pin2 = 2345;
		int pin3 = 9009;
		AccountType checking = AccountType.CHECKING;
		AccountType saving = AccountType.SAVING;
		int id = 1;
		int id2 = 500;
		int id3 = 2;
		double bal0 = 0.0;
		double bal1 = 100.50;
		
		BankAccount acc1 = new BankAccount(pin1,checking,id);
		BankAccount acc2 = new BankAccount(pin2,saving,id2,bal0);
		BankAccount acc3 = new BankAccount(pin3,checking,id3,bal1);
		
		assertEquals(pin1,acc1.getAccountPin());
		assertEquals(checking,acc1.getAccountType());
		assertEquals(id,acc1.getAdminID());
		assertEquals(0.0,acc1.getBalance());
		assertEquals(1,acc1.getAccountNumber());
		
		assertEquals(pin2,acc2.getAccountPin());
		assertEquals(saving,acc2.getAccountType());
		assertEquals(id2,acc2.getAdminID());
		assertEquals(bal0,acc2.getBalance());
		assertEquals(2,acc2.getAccountNumber());
		
		assertEquals(pin3,acc3.getAccountPin());
		assertEquals(checking,acc3.getAccountType());
		assertEquals(id3,acc3.getAdminID());
		assertEquals(bal1,acc3.getBalance());
		assertEquals(3,acc3.getAccountNumber());
	}
	
	@Test
	@Order(2)
	@DisplayName("Get account Pin")
	void test2() {
		int pin1 = 1234;
		int pin2 = 2345;
		int pin3 = 9009;
		AccountType checking = AccountType.CHECKING;
		AccountType saving = AccountType.SAVING;
		int id = 1;
		int id2 = 500;
		int id3 = 2;
		double bal0 = 0.0;
		double bal1 = 100.50;
		
		BankAccount acc1 = new BankAccount(pin1,checking,id);
		BankAccount acc2 = new BankAccount(pin2,saving,id2,bal0);
		BankAccount acc3 = new BankAccount(pin3,checking,id3,bal1);
		
		assertEquals(pin1,acc1.getAccountPin());
		assertEquals(pin2,acc2.getAccountPin());
		assertEquals(pin3,acc3.getAccountPin());
	}
	
	@Test
	@Order(3)
	@DisplayName("Get Account Type")
	void test3() {
		int pin1 = 1234;
		int pin2 = 2345;
		int pin3 = 9009;
		AccountType checking = AccountType.CHECKING;
		AccountType saving = AccountType.SAVING;
		int id = 1;
		int id2 = 500;
		int id3 = 2;
		double bal0 = 0.0;
		double bal1 = 100.50;
		
		BankAccount acc1 = new BankAccount(pin1,checking,id);
		BankAccount acc2 = new BankAccount(pin2,saving,id2,bal0);
		BankAccount acc3 = new BankAccount(pin3,checking,id3,bal1);
		
		assertEquals(checking,acc1.getAccountType());
		assertEquals(saving,acc2.getAccountType());
		assertEquals(checking,acc3.getAccountType());
	}
	
	@Test
	@Order(4)
	@DisplayName("Get Account Admin")
	void test4() {
		int pin1 = 1234;
		int pin2 = 2345;
		int pin3 = 9009;
		AccountType checking = AccountType.CHECKING;
		AccountType saving = AccountType.SAVING;
		int id = 1;
		int id2 = 500;
		int id3 = 2;
		double bal0 = 0.0;
		double bal1 = 100.50;
		
		BankAccount acc1 = new BankAccount(pin1,checking,id);
		BankAccount acc2 = new BankAccount(pin2,saving,id2,bal0);
		BankAccount acc3 = new BankAccount(pin3,checking,id3,bal1);
		
		assertEquals(id,acc1.getAdminID());
		assertEquals(id2,acc2.getAdminID());
		assertEquals(id3,acc3.getAdminID());
	}
	
	@Test
	@Order(5)
	@DisplayName("Get Balance")
	void test5() {
		int pin1 = 1234;
		int pin2 = 2345;
		int pin3 = 9009;
		AccountType checking = AccountType.CHECKING;
		AccountType saving = AccountType.SAVING;
		int id = 1;
		int id2 = 500;
		int id3 = 2;
		double bal0 = 0.0;
		double bal1 = 100.50;
		
		BankAccount acc1 = new BankAccount(pin1,checking,id);
		BankAccount acc2 = new BankAccount(pin2,saving,id2,bal0);
		BankAccount acc3 = new BankAccount(pin3,checking,id3,bal1);
		
		assertEquals(0.0,acc1.getBalance());
		assertEquals(bal0,acc2.getBalance());
		assertEquals(bal1,acc3.getBalance());
	}
	
	@Test
	@Order(6)
	@DisplayName("Get Account Number")
	void test6() {
		int pin1 = 1234;
		int pin2 = 2345;
		int pin3 = 9009;
		AccountType checking = AccountType.CHECKING;
		AccountType saving = AccountType.SAVING;
		int id = 1;
		int id2 = 500;
		int id3 = 2;
		double bal0 = 0.0;
		double bal1 = 100.50;
		
		
		BankAccount acc1 = new BankAccount(pin1,checking,id);
		BankAccount acc2 = new BankAccount(pin2,saving,id2,bal0);
		BankAccount acc3 = new BankAccount(pin3,checking,id3,bal1);

		assertEquals(16,acc1.getAccountNumber());
		assertEquals(17,acc2.getAccountNumber());
		assertEquals(18,acc3.getAccountNumber());
		
		}
	
	@Test
	@Order(7)
	@DisplayName("Set account Pin")
	void test7() {
		int pin1 = 1234;
		int pin2 = 2345;
		int pin3 = 9009;
		AccountType checking = AccountType.CHECKING;
		AccountType saving = AccountType.SAVING;
		int id = 1;
		int id2 = 500;
		int id3 = 2;
		double bal0 = 0.0;
		double bal1 = 100.50;
		
		BankAccount acc1 = new BankAccount(pin1,checking,id);
		BankAccount acc2 = new BankAccount(pin2,saving,id2,bal0);
		BankAccount acc3 = new BankAccount(pin3,checking,id3,bal1);
		
		acc1.setAccountPin(pin2);
		acc2.setAccountPin(pin3);
		acc3.setAccountPin(pin1);
		
		assertEquals(pin2,acc1.getAccountPin());
		assertEquals(pin3,acc2.getAccountPin());
		assertEquals(pin1,acc3.getAccountPin());
	}
	
	@Test
	@Order(8)
	@DisplayName("Get Account Admin")
	void test8() {
		int pin1 = 1234;
		int pin2 = 2345;
		int pin3 = 9009;
		AccountType checking = AccountType.CHECKING;
		AccountType saving = AccountType.SAVING;
		int id = 1;
		int id2 = 500;
		int id3 = 2;
		double bal0 = 0.0;
		double bal1 = 100.50;
		
		BankAccount acc1 = new BankAccount(pin1,checking,id);
		BankAccount acc2 = new BankAccount(pin2,saving,id2,bal0);
		BankAccount acc3 = new BankAccount(pin3,checking,id3,bal1);
		
		acc1.setAdminID(id2);
		acc2.setAdminID(id3);
		acc3.setAdminID(id);
		
		assertEquals(id2,acc1.getAdminID());
		assertEquals(id3,acc2.getAdminID());
		assertEquals(id,acc3.getAdminID());
	}
	
	@Test
	@Order(9)
	@DisplayName("Set Balance")
	void test9() {
		int pin1 = 1234;
		int pin2 = 2345;
		int pin3 = 9009;
		AccountType checking = AccountType.CHECKING;
		AccountType saving = AccountType.SAVING;
		int id = 1;
		int id2 = 500;
		int id3 = 2;
		double bal0 = 0.0;
		double bal1 = 100.50;
		
		BankAccount acc1 = new BankAccount(pin1,checking,id);
		BankAccount acc2 = new BankAccount(pin2,saving,id2,bal0);
		BankAccount acc3 = new BankAccount(pin3,checking,id3,bal1);
		
		acc1.setBalance(bal1);
		acc2.setBalance(99.99);
		acc3.setBalance(bal0);
		
		assertEquals(bal1,acc1.getBalance());
		assertEquals(99.99,acc2.getBalance());
		assertEquals(bal0,acc3.getBalance());
	}
}
