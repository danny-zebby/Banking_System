package gui.ATM;

import javax.swing.*;

import main.ATMGUIClient;
import java.awt.*;
import java.awt.event.*;

public class ATMLogin {
	
	public static ATMGUIClient client;
	public static MainPage mainpage;
	
//	public static void main(String[] args) {
//	      createWindow();
//	}
	
	public ATMLogin (ATMGUIClient client, MainPage mainpage) {
		this.client = client;
		this.mainpage = mainpage;
	}
	
	public static ATMGUIClient getClient() {
		return client;
	}
	
	public static MainPage getMainPage() {
		return mainpage;
	}
	
	public static void createWindow() {
	    JFrame frame = new JFrame("ATM Login");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    LoginUI(frame);
	    frame.setSize(300, 200);
	    frame.setLocationRelativeTo(null);
	    frame.setVisible(true);
	}
		
	private static void LoginUI(final JFrame frame){
		// creates a new panel
		JPanel panel = new JPanel();
		LayoutManager layout = new FlowLayout();
		frame.add(panel);
		
		JButton okButton = new JButton("OK");
		okButton.setSize(200,200);
		JButton exitButton = new JButton("EXIT");
		exitButton.setSize(100,100);
		JTextField textField = new JTextField(25);
		JTextField textField2 = new JTextField(21);
		JLabel label1 = new JLabel("ID:");
		JLabel label2 = new JLabel("Password:");
		
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String id = textField.getText();
				String password = textField2.getText();
				//if either id or password are blank, a frame opens up and asks for input.
				if (id.contentEquals("") || password.contentEquals("")) {
					JOptionPane.showMessageDialog(frame, "Please type in ID or password.");
				} else {
					// testing purposes
					String status = getClient().loginRequest(id, password);
					if (status == "SUCCESS") {
//						System.exit(0);
						frame.setVisible(false);
						getMainPage().createWindow();
					}
					else {
						JOptionPane.showMessageDialog(frame, "Invaild ID or password. Try again");
					}
				}
			}
		});
		
		exitButton.addActionListener(new ActionListener() {
	          public void actionPerformed(ActionEvent e) {
	        	 // testing purposes
	             JOptionPane.showMessageDialog(frame, "Goodbye");
	             //closes ATMclient
	             System.exit(0);
	          }
		});
		
		panel.add(label1);
		panel.add(textField);
		panel.add(label2);
		panel.add(textField2);
		panel.add(okButton);
		panel.add(exitButton);
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		
	}
	
//	private static void mainpage(final JFrame frame){
//		//opens Mainpage.java
//		//MainPage.go();
//	}
	
}
