package gui.Teller;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import gui.Teller.TellerMainPage;
import main.TellerGUIClient;
import java.util.Random;
import java.io.*;
import java.util.*;
import java.net.*;
import message.*;
public class TellerCreateAccount {
	TellerUserAccount tellerUserAccount = null;
	TellerGUIClient tellerGUIClient = null;
	TellerMainPage tellerMainPage = null;
	int id;
	
	public TellerCreateAccount (TellerUserAccount tellerUserAccount, int id) {
		this.tellerUserAccount = tellerUserAccount;
		this.tellerGUIClient = tellerUserAccount.getClient();
		this.tellerMainPage = tellerUserAccount.getMain();
		this.id = id;
	}
	
	public TellerGUIClient getClient() {
		return this.tellerGUIClient;
	}
	public TellerUserAccount getUserAcc() {
		return this.tellerUserAccount;
	}
	
	public void createWindow() {
	    JFrame frame = new JFrame("Create a Bank User");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    TellerCreateAccountUI(frame);
	    frame.setSize(450,300);
	    frame.setLocationRelativeTo(null);
	    frame.setVisible(true);
	}
		
	private void TellerCreateAccountUI(final JFrame frame){
		// creates a new panel
		JPanel panel = new JPanel();
		LayoutManager layout = new FlowLayout();
		frame.add(panel);
		
		JButton savingButton = new JButton("SAVINGS");
		savingButton.setSize(200,200);
		JButton checkingButton = new JButton("CHECKINGS");
		checkingButton.setSize(200,200);
		JButton exitButton = new JButton("Cancel");
		exitButton.setSize(100,100);
		JTextField textField = new JTextField(25);
		JLabel label1 = new JLabel("Account Pin: ");
		
		savingButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String pin = textField.getText();
				if (pin.equals("")) {
					// Generate a random 4-digit pin
					Random random = new Random();
			        int randomPin = 1000 + random.nextInt(9000);
			        pin = String.valueOf(randomPin);
				}
				String status = tellerGUIClient.checkPin(pin);
				if(status.equalsIgnoreCase("VALID")){
					String userConfirm = JOptionPane.showInputDialog("You are creating a savings account with pin " + pin
							+ "\nPlease enter yes to confirm.");
					if (userConfirm.equalsIgnoreCase("YES")) {
						int intPin = Integer.parseInt(pin);
						AccountType saving = AccountType.SAVING;
						String stat = tellerGUIClient.createAccount(id, intPin, saving);
						if(stat.equalsIgnoreCase("SUCCESS")) {
							JOptionPane.showMessageDialog(frame,"Updated user id ["
						+ id + "] accounts: " + tellerGUIClient.getAccounts());
						frame.setVisible(false);
//			            new TellerUserAccount(tellerMainPage, id).run();
						tellerUserAccount.run(); // going back to main page
						}
					}else {
						JOptionPane.showMessageDialog(frame, "Fail to create a new SAVINGS.\n");
					}
				}
				else{
					JOptionPane.showMessageDialog(frame, "Invalid pin. Try again.");
				}
			}
		} );
		
		
		checkingButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String pin = textField.getText();
				if (pin.equals("")) {
					// Generate a random 4-digit pin
					Random random = new Random();
			        int randomPin = 1000 + random.nextInt(9000);
			        pin = String.valueOf(randomPin);
				}
				String status = tellerGUIClient.checkPin(pin);
				if(status.equalsIgnoreCase("VALID")){
					String userConfirm = JOptionPane.showInputDialog("You are creating a checking account with pin " + pin
							+ "\nPlease enter yes to confirm.");
					if (userConfirm.equalsIgnoreCase("YES")) {
						int intPin = Integer.parseInt(pin);
						AccountType saving = AccountType.CHECKING;
						String stat = tellerGUIClient.createAccount(id, intPin, saving);
						if(stat.equalsIgnoreCase("SUCCESS")) {
							JOptionPane.showMessageDialog(frame,"Updated user id ["
						+ id + "] accounts: " + tellerGUIClient.getAccounts());
							frame.setVisible(false);
//				            new TellerUserAccount(tellerMainPage, id).run();
							tellerUserAccount.run(); // going back to main page
						}
					}else {
						JOptionPane.showMessageDialog(frame, "Fail to create a new CHECKINGS.\n");
					}
				}
				else{
					JOptionPane.showMessageDialog(frame, "Invalid pin. Try again.");
				}
			}
		} );
		exitButton.addActionListener(new ActionListener() {
	          public void actionPerformed(ActionEvent e) {
	             JOptionPane.showMessageDialog(frame, "Back to user page.");
	             //closes TellerCreateUser
	             frame.setVisible(false);
//	             new TellerUserAccount(tellerMainPage, id).run();
	             tellerUserAccount.run(); // going back to main page
	          }
		});
		
		
		panel.add(savingButton);
		panel.add(checkingButton);
		panel.add(label1);
		panel.add(textField);
		panel.add(exitButton);
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		
	}
}



