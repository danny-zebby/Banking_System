package gui.Teller;

import javax.swing.*;

import gui.ATM.AccountSelection;
import main.BankAccount;
import main.TellerGUIClient;

import java.awt.*;
import java.awt.event.*;
import java.util.Map;

public class TellerUserAccount {

	JButton createAccount, deleteAccount, addUserbutton, deleteUserbutton, forgetPasswordbutton, changePinbutton, transferAdminbutton, withdraw, deposit, transfer, backbutton;
	JLabel label;
	JFrame frame;
	JPanel panel = null;
	JPanel panel2 = null;
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
		panel = new JPanel(); 
		panel.setLayout(new FlowLayout());
		
		createAccount = new JButton("Create Account");
		deleteAccount = new JButton("Delete Account");
		addUserbutton = new JButton("Add User");
		deleteUserbutton = new JButton("Delete User");
		forgetPasswordbutton = new JButton("Forget Password");
		changePinbutton = new JButton("Change PIN");
		transferAdminbutton = new JButton("Transfer Admin");
		withdraw = new JButton("Withdraw");
		deposit = new JButton("Deposit");
		transfer = new JButton("Transfer");
		backbutton = new JButton("Back");
		JPanel panel = new JPanel();
		
		if(AccIn == false){
			getClient().getAccountsInfo();
			AccIn = true;
		}
		Map<Integer, BankAccount> accounts = getClient().getAccounts();
		String acc = "Bank Account Infomation:\n" + accounts;
		JTextArea textArea = new JTextArea(acc);
		panel.add(textArea);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		JPanel panel2 = new JPanel();
		panel2.setLayout(new GridLayout(3, 4));
		panel2.add(createAccount);
		panel2.add(deleteAccount);
		panel2.add(addUserbutton);
		panel2.add(deleteUserbutton);
		panel2.add(forgetPasswordbutton);
		panel2.add(changePinbutton);
		panel2.add(transferAdminbutton);
		panel2.add(withdraw);
		panel2.add(deposit);
		panel2.add(transfer);
		panel2.add(backbutton);
		
		frame.setSize(600, 350);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(BorderLayout.NORTH, panel);
		frame.getContentPane().add(BorderLayout.SOUTH, panel2);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		createAccount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				new TellerCreateAccount(getUserAcc(), ID).createWindow();;
			}
		});

		deleteAccount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				new TellerSelectAccountForDelAccount(getTellerUserAccount()).go();;
			}
		});

		addUserbutton.addActionListener(new ActionListener() {
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

		withdraw.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// insert code here
			}
		});

		deposit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// insert code here
			}
		});

		transfer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// insert code here
			}
		});
		
		backbutton.addActionListener((e) -> {
			frame.setVisible(false); // hide current page
			tellerMainPage.run(); // jump back to TellerMainPage
		});
	}
}