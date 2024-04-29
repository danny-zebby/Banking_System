package gui.Teller;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import gui.ATM.ATMLogin;
import gui.ATM.AccountSelection;
import gui.ATM.MainPage;
import main.TellerGUIClient;
import message.LogoutMessage;
import message.Status;

public class TellerMainPage {
	JFrame frame;
	public TellerGUIClient client;
	TellerAddTellerPage tellerAddTellerPage = null;
	public TellerMainPage(TellerGUIClient client) {
		this.client = client;
	}
	
	public TellerGUIClient getTellerClient() {
		return this.client;
	}
	
	public TellerMainPage getTellerMainPage() {
		return this;
	}
	
	public void run() {
		frame = new JFrame("Teller MainPage");
		JPanel NorthPanel = new JPanel();
		
		JButton userLoginButton = new JButton("Login as User");
		JButton createUserButton = new JButton("Create a User");
		JButton logoutButton = new JButton("Logout");
		JButton openLogsButton = new JButton("Open logs");
		JButton addNewTellerButton = new JButton("Add New Teller");
		JButton deleteTellerButton = new JButton("Delete a Teller");
			
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(BorderLayout.NORTH, NorthPanel);
		frame.setSize(500,100);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		
		NorthPanel.add(userLoginButton);
		NorthPanel.add(createUserButton);
		NorthPanel.add(logoutButton);
		
		if(getTellerClient().getTeller().getAdmin()) {
			JLabel admin = new JLabel("Admin");
			JPanel SouthPanel = new JPanel();
			
			frame.getContentPane().add(BorderLayout.SOUTH, SouthPanel);
		
			SouthPanel.add(admin);
			SouthPanel.add(openLogsButton);
			SouthPanel.add(addNewTellerButton);
			SouthPanel.add(deleteTellerButton);
		}
		
		userLoginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// add code needed before moving to next page
				frame.setVisible(false);
				// add code to move to next page
			}
		});
		
		createUserButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// add code needed before moving to next page
				frame.setVisible(false);
				new TellerCreateUser(getTellerClient()).createWindow();
			}
		});
		
		logoutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// add code needed before moving to next page
				frame.setVisible(false);
				String status = getTellerClient().logoutRequest();
				if (status == "SUCCESS") {
					TellerMainPage tellerMainPage = new TellerMainPage(getTellerClient());
					TellerLogin tellerLogin = new TellerLogin(getTellerClient(), tellerMainPage);
					tellerLogin.createWindow(); // start teller login
				}
				else {
					JOptionPane.showMessageDialog(frame, "Failed to Logout, try again!!");
				}
			}
		});
		
		openLogsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// add code needed before moving to next page
				frame.setVisible(false);
				new TellerViewLogsPage(getTellerMainPage()).go();
			}
		});
		
		addNewTellerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// add code needed before moving to next page
				frame.setVisible(false);
				// add code to move to next page
				new TellerAddTellerPage(getTellerMainPage()).createWindow();
			}
		});
		
		deleteTellerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// add code needed before moving to next page
				frame.setVisible(false);
				// add code to move to next page
				
				new TellerSelectionPage(getTellerMainPage()).go();
				
			}
		});

	} // end method run
	

}

