package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;

import main.Teller;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(OrderAnnotation.class)
class TellerTest {
	
	String n1 = "BOB";
	String p1 = "abcd1234";
	boolean admin = true;
	boolean a = true;
	boolean teller = false;
	
	@Test
	@Order(1)
	@DisplayName("Constructor")
	void test1() {
		Teller TU1 = new Teller(n1,p1,admin);
		assertEquals("BOB",TU1.getName());
		assertEquals("abcd1234",TU1.getPassword());
		assertEquals(true, TU1.getAdmin());
		assertEquals(1,TU1.getId());


		String n2 = "TOM";
		String p2 = "4321dcba";
		Teller TU2 = new Teller(n2,p2,teller);
		assertEquals("TOM",TU2.getName());
		assertEquals("4321dcba",TU2.getPassword());
		assertEquals(false, TU2.getAdmin());
		assertEquals(2,TU2.getId());

		Teller TU3 = new Teller(n1,p1,a);
		assertEquals(3,TU3.getId());
	}

	@Test
	@Order(2)
	@DisplayName("Name Getter")
	void test2() {
		Teller TU = new Teller(n1,p1,a);
		assertEquals("BOB",TU.getName());
	}

	@Test
	@Order(3)
	@DisplayName("Admin Getter")
	void test3() {

		Teller TU = new Teller(n1,p1,a);
		assertEquals(true,TU.getAdmin());
	}

	@Test
	@Order(4)
	@DisplayName("Password Getter")
	void test4() {

		Teller TU = new Teller(n1,p1,a);
		assertEquals("abcd1234",TU.getPassword());
	}

	@Test
	@Order(5)
	@DisplayName("ID Getter")
	void test5() {

		Teller TU = new Teller(n1,p1,a);
		assertEquals(7,TU.getId());
	}


	@Test
	@Order(6)
	@DisplayName("Name Setters")
	void test6() {

		Teller TU = new Teller(n1,p1,a);
		TU.setName("Daniel");
		assertEquals("Daniel",TU.getName());

	}

	@Test
	@Order(8)
	@DisplayName("Password Setters")
	void test8() {
	
		Teller TU = new Teller(n1,p1,a);
		TU.setPassword("password");
		assertEquals("password",TU.getPassword());
	}


}