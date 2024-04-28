package gui.Teller;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import main.TellerGUIClient;
import message.LogoutMessage;
import message.Status;

public class TellerMainPage {
	JFrame frame;
	public static TellerGUIClient client;
	
	//testing; can be 
	public static void main(String[] args) {
		run();
	}
	
	public TellerMainPage(TellerGUIClient client) {
		this.client = client;
	}
	
	public static TellerGUIClient GetTellerClient() {
		return client;
	}
	
	public static void run() {
		JFrame frame = new JFrame("Teller MainPage");
		JPanel NorthPanel = new JPanel();
		
		JButton button1 = new JButton("Login as User");
		JButton button2 = new JButton("Create a User");
		JButton button3 = new JButton("Logout");
		JButton button4 = new JButton("Open logs"); 
		JButton button5 = new JButton("Add New Teller");
		JButton button6 = new JButton("Delete a Teller");
			
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(BorderLayout.NORTH, NorthPanel);
		frame.setSize(500,100);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		
		NorthPanel.add(button1);
		NorthPanel.add(button2);
		NorthPanel.add(button3);
		if(GetTellerClient().getTeller().getAdmin()) {
			JLabel admin = new JLabel("Admin");
			JPanel SouthPanel = new JPanel();
			
			frame.getContentPane().add(BorderLayout.SOUTH, SouthPanel);
		
			SouthPanel.add(admin);
			SouthPanel.add(button4);
			SouthPanel.add(button5);
			SouthPanel.add(button6);
		}
		
		button1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// add code needed before moving to next page
				frame.setVisible(false);
				// add code to move to next page
			}
		});
		
		button2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// add code needed before moving to next page
				frame.setVisible(false);
				// add code to move to next page
			}
		});
		
		button3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// add code needed before moving to next page
				frame.setVisible(false);
				// add code to move to next page
			}
		});
		
		button4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// add code needed before moving to next page
				frame.setVisible(false);
				// add code to move to next page
			}
		});
		
		button5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// add code needed before moving to next page
				frame.setVisible(false);
				// add code to move to next page
			}
		});
		
		button6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// add code needed before moving to next page
				frame.setVisible(false);
				// add code to move to next page
			}
		});
		
	}
}