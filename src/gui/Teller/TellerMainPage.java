package gui.Teller;

import java.awt.*;
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
		JLabel admin = new JLabel("Admin");
		JPanel panel = new JPanel();
		
		JButton button1 = new JButton("Add New Teller");
		JButton button2 = new JButton("Login as User");
		JButton button3 = new JButton("Logout");
	
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(BorderLayout.WEST, panel);
		frame.setSize(500,300);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		panel.add(admin);
		panel.add(button1);
		panel.add(button2);
		panel.add(button3);
	}
}