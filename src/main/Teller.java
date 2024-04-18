package main;

public class Teller implements Comparable<Teller>{
	private static int count = 0;
  
  	private String name;
	private String password;
	private final int id;
	private boolean admin;
	
	public Teller(String name, String password, boolean admin) {
		this.name = name;
		this.password = password;
		this.id = ++count;
		this.admin = admin;
	}
	
	public int compareTo(Teller one) {
		return this.id - one.id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getId() {
		return this.id;
	}

	public boolean getAdmin() {
		return this.admin;
	}
  
//	public void setAdmin(boolean admin) {
//		this.admin = admin;
//	}
	
}
