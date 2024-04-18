package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;

import main.BankUser;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(OrderAnnotation.class)
class BankUserTest {

	@Test
	@Order(1)
	@DisplayName("Constructor")
	void test1() {
		String n1 = "BOB";
		String b1 = "01/23/45";
		String p1 = "abcd1234";
		BankUser BU1 = new BankUser(n1,b1,p1);
		assertEquals("BOB",BU1.getName());
		assertEquals("01/23/45",BU1.getBirthday());
		assertEquals("abcd1234",BU1.getPassword());
		assertEquals(1,BU1.getId());
		
		
		String n2 = "TOM";
		String b2 = "12/34/56";
		String p2 = "4321dcba";
		BankUser BU2 = new BankUser(n2,b2,p2);
		assertEquals("TOM",BU2.getName());
		assertEquals("12/34/56",BU2.getBirthday());
		assertEquals("4321dcba",BU2.getPassword());
		assertEquals(2,BU2.getId());
		
		BankUser BU3 = new BankUser(n1,b1,p1);
		assertEquals(3,BU3.getId());
	}
	
	@Test
	@Order(2)
	@DisplayName("Name Getter")
	void test2() {
		String n = "BOB";
		String b = "01/23/45";
		String p = "abcd1234";
		BankUser BU = new BankUser(n,b,p);
		assertEquals("BOB",BU.getName());
	}
	
	@Test
	@Order(3)
	@DisplayName("Birthday Getter")
	void test3() {
		String n = "BOB";
		String b = "01/23/45";
		String p = "abcd1234";
		BankUser BU = new BankUser(n,b,p);
		assertEquals("01/23/45",BU.getBirthday());
	}
	
	@Test
	@Order(4)
	@DisplayName("Password Getter")
	void test4() {
		String n = "BOB";
		String b = "01/23/45";
		String p = "abcd1234";
		BankUser BU = new BankUser(n,b,p);
		assertEquals("abcd1234",BU.getPassword());
	}
	
	@Test
	@Order(5)
	@DisplayName("ID Getter")
	void test5() {
		String n = "BOB";
		String b = "01/23/45";
		String p = "abcd1234";
		BankUser BU = new BankUser(n,b,p);
		assertEquals(7,BU.getId());
	}
	
	@Test
	@Order(6)
	@DisplayName("Cash Getter")
	void test6() {
		String n = "BOB";
		String b = "01/23/45";
		String p = "abcd1234";
		BankUser BU = new BankUser(n,b,p);
		double c = 250.75;
		BU.setCash(c);
		assertEquals(250.75,BU.getCash());
	}
	
	@Test
	@Order(7)
	@DisplayName("Name Setters")
	void test7() {
		String n = "BOB";
		String b = "01/23/45";
		String p = "abcd1234";
		BankUser BU = new BankUser(n,b,p);
		BU.setName("Daniel");
		assertEquals("Daniel",BU.getName());

	}
	
	@Test
	@Order(8)
	@DisplayName("Birthday Setters")
	void test8() {
		String n = "BOB";
		String b = "01/23/45";
		String p = "abcd1234";
		BankUser BU = new BankUser(n,b,p);
		BU.setBirthday("06/19/02");
		assertEquals("06/19/02",BU.getBirthday());
	}
	
	
	@Test
	@Order(9)
	@DisplayName("Password Setters")
	void test9() {
		String n = "BOB";
		String b = "01/23/45";
		String p = "abcd1234";
		BankUser BU = new BankUser(n,b,p);
		BU.setPassword("password");
		assertEquals("password",BU.getPassword());
	}
	
	@Test
	@Order(10)
	@DisplayName("Cash Setters")
	void test10() {
		String n = "BOB";
		String b = "01/23/45";
		String p = "abcd1234";
		BankUser BU = new BankUser(n,b,p);
		double c = 250.75;
		BU.setCash(c);
		assertEquals(250.75,BU.getCash());
	}
	
	@Test
	@Order(11)
	@DisplayName("Compare")
	void test11() {
		String n = "BOB";
		String b = "01/23/45";
		String p = "abcd1234";
		BankUser BU = new BankUser(n,b,p);
		assertEquals(0,BU.compareTo(BU));
		
		BankUser BU2 = new BankUser(n,b,p);
		assertEquals(0,BU2.compareTo(BU2));
		assertEquals(-1,BU.compareTo(BU2));
		assertEquals(1,BU2.compareTo(BU));
	}
}