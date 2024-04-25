package gui.ATM;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ATMLogin {
	public static void main(String[] args) {
	      createWindow();
	}
	
	private static void createWindow() {
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
				// testing purposes
				JOptionPane.showMessageDialog(frame, id);
				JOptionPane.showMessageDialog(frame, password);
			
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
	
	private static void mainpage(final JFrame frame){
		//opens Mainpage.java
		//ATMMain.ATMMain();
	}
	
}
