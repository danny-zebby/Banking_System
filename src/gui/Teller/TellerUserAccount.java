package gui.Teller;

import javax.swing.*;

import gui.ATM.AccountSelection;
import main.BankAccount;
import main.TellerGUIClient;

import java.awt.*;
import java.awt.event.*;
import java.util.Map;

public class TellerUserAccount {


	JButton createAccountButton, deleteAccountButton, addUserbuttonButton, deleteUserbutton, forgetPasswordbutton, changePinbutton, transferAdminbutton, withdrawButton, depositButton, transferButton, backbutton;
	JLabel label;
	JFrame frame;
	JPanel centerPanel = null;
	JPanel southPanel = null;
	boolean AccIn = false;
	TellerMainPage tellerMainPage = null;
	TellerGUIClient tellerGUIClient = null;

	public AccountSelection accsel;

	int ID;
	
	public TellerUserAccount(TellerMainPage tellerMainPage, int ID) {
		this.tellerMainPage = tellerMainPage;
		this.tellerGUIClient = tellerMainPage.getTellerClient();
		this.ID = ID;
	}
	
	public TellerGUIClient getTellerGUIClient() {
		return this.tellerGUIClient;
	}
	
	public TellerGUIClient getClient() {
		return this.tellerGUIClient;
	}
	public TellerMainPage getMain() {
		return this.tellerMainPage;
	}
	public TellerUserAccount getUserAcc() {
		return this;
	}
	public TellerUserAccount getTellerUserAccount() {
		return this;
	}
	public AccountSelection getAccSel() {
		return this.accsel;
	}
	
	public void run() {
		frame = new JFrame("Teller(User)");
		centerPanel = new JPanel(); 
		
		createAccountButton = new JButton("Create Account");
		deleteAccountButton = new JButton("Delete Account");
		addUserbuttonButton = new JButton("Add User");
		deleteUserbutton = new JButton("Delete User");
		forgetPasswordbutton = new JButton("Forget Password");
		changePinbutton = new JButton("Change PIN");
		transferAdminbutton = new JButton("Transfer Admin");
		withdrawButton = new JButton("Withdraw");
		depositButton = new JButton("Deposit");
		transferButton = new JButton("Transfer");
		backbutton = new JButton("Back");
		
		if(AccIn == false){
			getClient().getAccountsInfo();
			AccIn = true;
		}
		
		tellerGUIClient.getAccountsInfo();
		Map<Integer, BankAccount> accounts = tellerGUIClient.getAccounts();
		
		String acc = "Bank Account Infomation: \n" + accounts;
		JTextArea textArea = new JTextArea(acc);
		textArea.setEditable(false);
		
		JScrollPane scroller = new JScrollPane(textArea);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		centerPanel.add(scroller);
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

		
		southPanel = new JPanel();
		southPanel.setLayout(new GridLayout(3, 4));

		southPanel.add(createAccountButton);
		southPanel.add(deleteAccountButton);
		southPanel.add(addUserbuttonButton);
		southPanel.add(deleteUserbutton);
		southPanel.add(forgetPasswordbutton);
		southPanel.add(changePinbutton);
		southPanel.add(transferAdminbutton);
		southPanel.add(withdrawButton);
		southPanel.add(depositButton);
		southPanel.add(transferButton);
		southPanel.add(backbutton);
		
		frame.setSize(600, 350);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(BorderLayout.CENTER, centerPanel);
		frame.getContentPane().add(BorderLayout.SOUTH, southPanel);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		createAccountButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				new TellerCreateAccount(getUserAcc(), ID).createWindow();;
			}
		});

		deleteAccountButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				new TellerSelectAccountForDelAccount(getTellerUserAccount()).go();;
			}
		});

		addUserbuttonButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// insert code here
				frame.setVisible(false); // hide current window
				// jump to the TellerSelectAccountPage
				new TellerSelectAccountForAddUserPage(getTellerUserAccount()).go();
			}
		});

		deleteUserbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// insert code here
				frame.setVisible(false); // hide current window
				// jump to the TellerSelectAccountDelPage
				new TellerSelectAccountForDelUserPage(getTellerUserAccount()).go();
			}
		});


		forgetPasswordbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// insert code here
				frame.setVisible(false);
				new TellerForgetPasswordPage(getTellerUserAccount()).go();
			}
		});

		changePinbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// insert code here
				frame.setVisible(false);
				new TellerSelectAccountForChangePinPage(getTellerUserAccount()).go();
			}
		});

		transferAdminbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// insert code here
				frame.setVisible(false);
				new TellerSelectAccountForTransferAdminPage(getTellerUserAccount()).go();
			}
		});


		// ATM methods
		withdrawButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// insert code here
				frame.setVisible(false); // hide current window
				new TellerWithdrawPage(getTellerUserAccount()).go();
			}
		});

		depositButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// insert code here
				frame.setVisible(false);
				new TellerDepositPage(getTellerUserAccount()).go();
			}
		});

		transferButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// insert code here
				frame.setVisible(false);
				new TellerTransferPage(getTellerUserAccount()).go();
			}
		});
		
		backbutton.addActionListener((e) -> {
			frame.setVisible(false); // hide current page
			tellerGUIClient.logoutUser();
			tellerMainPage.run(); // jump back to TellerMainPage
		});
	}
}