package gui.Teller;

import java.awt.*;
import javax.swing.*;

import main.TellerGUIClient;
import message.LogoutMessage;
import message.Status;

public class TellerMainPage {
	JFrame frame;
	public static TellerGUIClient client;
	
	public TellerMainPage(TellerGUIClient client) {
		this.client = client;
	}
	
	public static TellerGUIClient GetTellerClient() {
		return client;
	}
	
	public void run() {
		frame = new JFrame("Teller MainPage");
		JLabel admin = new JLabel("Admin");
		JPanel panel = new JPanel();
		panel.add(admin);
		JButton button1 = new JButton("Add New Teller");
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(BorderLayout.WEST, panel);
		frame.setSize(500,300);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		panel.add(button1);
	}
}