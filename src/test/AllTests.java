package test;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({BankUserTest.class, BankAccountTest.class, TellerTest.class}) // add test class here
public class AllTests {

}
