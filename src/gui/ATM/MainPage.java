package gui.ATM;

import java.awt.*;
import javax.swing.*;

import main.ATMGUIClient;
import message.LogoutMessage;
import message.Status;

import java.awt.event.*;
import java.util.Map;
import java.util.Vector;

public class MainPage {
	JFrame frame = null;
	JPanel southPanel = null;
	JPanel centerPanel = null;
	JPanel eastPanel = null;
	JPanel westPanel = null;
	Vector<String> entries = null;
	boolean show = true;
	boolean AccIn = false;
	private Map accounts;
	
//	public MainPage(Map accounts){
//		this.accounts = accounts;
//	}

//	public static void main(String[] args) {
//		MainPage MP = new MainPage();
//		MP.go();
//	}
	
	public static ATMGUIClient client;
	public static AccountSelection accsel;
	
	public MainPage(ATMGUIClient client, AccountSelection accsel, boolean AccIn){
		this.client = client;
		this.accsel = accsel;
		this.AccIn = AccIn;
	}
	
	public static ATMGUIClient getClient() {
		return client;
	}
	
	public static AccountSelection getAccSel() {
		return accsel;
	}
	
	public void go() {
		// create new frame
		frame = new JFrame("ATM MainPage");
		
		// create new panel
		southPanel = new JPanel();
		centerPanel = new JPanel();
		eastPanel = new JPanel();
		westPanel = new JPanel();
		JButton showButton = new JButton("Show");
		eastPanel.setLayout(new FlowLayout());
		eastPanel.add(showButton);
		
		if(AccIn == false){
			getClient().getAccountsInfo();
			AccIn = true;
		}
		Map accounts = getClient().getAccounts();
		String acc = "Bank Account Infomation:\n" + accounts + "\nSelect a command\n";
		JTextArea textArea = new JTextArea(acc);
		westPanel.add(textArea);
		
		// Add components to panel
		createUIView();
		
		// add components to frame
		frame.getContentPane().add(BorderLayout.CENTER, centerPanel);
		frame.getContentPane().add(BorderLayout.SOUTH, southPanel);
		frame.getContentPane().add(BorderLayout.EAST, eastPanel);
		frame.getContentPane().add(BorderLayout.WEST, westPanel);
		
		// show frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 300);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	public void createUIView() {
		
		// create JButtons
		JButton button1 = new JButton("Withdraw");
		JButton button2 = new JButton("Deposit");
		JButton button3 = new JButton("Transfer");
		JButton button4 = new JButton("Logout");
		// add to south panel
		southPanel.add(button1, BorderLayout.SOUTH);
		southPanel.add(button2, BorderLayout.SOUTH);
		southPanel.add(button3, BorderLayout.SOUTH);
		southPanel.add(button4, BorderLayout.SOUTH);
		
		
//		JList<String> list = new JList<>(entries);
//		JScrollPane scroller = new JScrollPane(list);
//		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
//		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
//		// set the number of lines to show before scrolling
//		list.setVisibleRowCount(4);
//		// add to panel
//		centerPanel.add(scroller);
//		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
		
		button1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String opperation = "withdraw";
				frame.setVisible(false);
				getAccSel().go(opperation);
			}
		});
		
		button2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String opperation = "deposit";
				frame.setVisible(false);
				getAccSel().go(opperation);
			}
		});
		button3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String opperation = "transfer";
				frame.setVisible(false);
				getAccSel().go(opperation);
			}
		});
		button4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				String status = getClient().logoutRequest();
				if (status == "SUCCESS") {
					AccountSelection AS = new AccountSelection(getClient());
				MainPage MP = new MainPage(getClient(), AS, false);
				ATMLogin AL = new ATMLogin(getClient(), MP);
				AL.createWindow();
				}
				else {
					JOptionPane.showMessageDialog(frame, "Invaild ID or password. Try again");
				}
				
			}
		});
		
	}

	
	class showButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			if(show == false) {
				entries.clear();
				String[] input = {"alpha", 
						"beta", 
						"gamma", "sigma", "phi", "omega"};
				
				for (String s : input) {
					entries.add(s);
				}; // end of for
				frame.repaint();
				show = true;
			} // end of if
			else {
				entries.clear();
				entries.add("A");
				entries.add("B");
				entries.add("G");
				entries.add("S");
				entries.add("P");
				entries.add("W");
				frame.repaint();
				show = false;
			} // end of else
			
		} // end of action
	} // end of show
}
