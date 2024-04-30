package gui.ATM;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Map;
import java.util.Vector;
import main.*;
public class MainPage {
	JFrame frame = null;
	JPanel southPanel = null;
	JPanel centerPanel = null;
	JPanel eastPanel = null;
	Vector<String> entries = null;
	boolean show = true;
	boolean AccIn = false;
	boolean click = true;
	
	public ATMGUIClient client;
	public AccountSelection accsel;
	
	public MainPage(ATMGUIClient client, AccountSelection accsel, boolean AccIn){
		this.client = client;
		this.accsel = accsel;
		this.AccIn = AccIn;
		
		this.accsel.setMainPage(this);
	}
	
	public ATMGUIClient getClient() {
		return this.client;
	}
	
	public AccountSelection getAccSel() {
		return this.accsel;
	}
	
	public void go() {
		// create new frame
		frame = new JFrame("ATM MainPage");
		
		// create new panel
		southPanel = new JPanel();
		centerPanel = new JPanel();
		eastPanel = new JPanel();
		JButton showButton = new JButton("Show");
		eastPanel.setLayout(new FlowLayout());
		eastPanel.add(showButton);
		
		if(AccIn == false){
			getClient().getAccountsInfo();
			AccIn = true;
		}
		Map<Integer, BankAccount> accounts = getClient().getAccounts();
		String acc = "Bank Account Infomation:\n" + accounts + "\nSelect a command\n";
		JTextArea textArea = new JTextArea(acc);
		
		JScrollPane scroller = new JScrollPane(textArea);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		centerPanel.add(scroller);
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
		
		// Add components to panel
		createUIView();
		
		// add components to frame
		frame.getContentPane().add(BorderLayout.CENTER, centerPanel);
		frame.getContentPane().add(BorderLayout.SOUTH, southPanel);
		frame.getContentPane().add(BorderLayout.EAST, eastPanel);
		
		// show frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 300);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		showButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (click) {
					textArea.setText("test");
					click = false;
				} else {
					textArea.setText(acc);
					click = true;
				}
			} // end of action
		}); // end of show
	}
	
	public void createUIView() {
		
		// create JButtons
		JButton WithdrawButton = new JButton("Withdraw");
		JButton depositButton = new JButton("Deposit");
		JButton transferButton = new JButton("Transfer");
		JButton logoutButton = new JButton("Logout");
		// add to south panel
		southPanel.add(WithdrawButton);
		southPanel.add(depositButton);
		southPanel.add(transferButton);
		southPanel.add(logoutButton);
		
		WithdrawButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String operation = "withdraw";
				frame.setVisible(false);
				getAccSel().go(operation);
			}
		});
		
		depositButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String operation = "deposit";
				frame.setVisible(false);
				getAccSel().go(operation);
			}
		});
		transferButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String operation = "transfer";
				frame.setVisible(false);
				getAccSel().go(operation);
			}
		});
		logoutButton.addActionListener(new ActionListener() {
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
					JOptionPane.showMessageDialog(frame, "Failed to Logout, try again!!");
				}
				
			}
		});
		
	}
}



