package gui.ATM;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import main.ATMGUIClient;

import java.util.Vector;
import java.util.List;
import java.util.*;

public class AccountSelection {
	JFrame frame = null;
	JPanel centerPanel = null;
	JPanel southPanel = null;
	Vector<String> entries = null;
	JList<String> list = null;
	MainPage mainpage = null;

	public ATMGUIClient client = null;
	private String operation = null;

	public AccountSelection(ATMGUIClient client) {
		this.client = client;
	}

	public ATMGUIClient getClient() {
		return client;
	}

	public void setOpperation(String operation) {
		this.operation = operation;
	}

	public String getOperation() {
		return this.operation;
	}

	public void setMainPage(MainPage mainpage) {
		this.mainpage = mainpage;
	}

	public void go(String operation) {
		// create new frame
		frame = new JFrame("Select an Account to " + operation + " from");
		setOpperation(operation);

		// create new panel
		centerPanel = new JPanel();
		southPanel = new JPanel();
		JButton confirmButton = new JButton("Confirm");
		JButton cancelButton = new JButton("Cancel");

		confirmButton.addActionListener(new confirmButtonListener());
		cancelButton.addActionListener(new cancelButtonListener());

		southPanel.add(confirmButton);
		southPanel.add(cancelButton);
		createScrollList();

		// add components to frame
		frame.getContentPane().add(BorderLayout.CENTER, centerPanel);
		frame.getContentPane().add(BorderLayout.SOUTH, southPanel);

		// show frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 300);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public void createScrollList() {
		// add list to panel
		entries = new Vector<String>();

		List<Integer> accounts = getClient().getUser().getAccounts();

		for (int accountNumber : accounts) {
			entries.add("Acc " + accountNumber);
		}

		list = new JList<>(entries);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // restrict the user to select only one thing at a
																	// time

		JScrollPane scroller = new JScrollPane(list);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scroller.setBounds(10, 10, 470, 200);
		centerPanel.add(scroller);
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

	}

	class confirmButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			if (getOperation() != null) {
				if (list.isSelectionEmpty()) { // if nothing is selected
					JOptionPane.showMessageDialog(frame, "Please select an account.");
					return;
				}
				
				String selectionItem = list.getSelectedValue();
				String[] parts = selectionItem.split("\\s+");
				// Extract the number part
				String number = parts[parts.length - 1];
				// Convert the number to an integer
				int accnum = Integer.parseInt(number);
				
				// Get Amount
				String Amount = JOptionPane.showInputDialog("Enter Amount to " + operation + ":");
				if (Amount == null) return;
				double amount = Double.parseDouble(Amount);
				Amount = String.format("%.2f", amount);
				double AccBal = Double.parseDouble(Amount);
				
				// Three operation
				String out, out1, SAP;
				int accountPin;
				if (getOperation() == "withdraw") {
					SAP = JOptionPane.showInputDialog("Enter the account pin: ");
					if (SAP == null) return;
					accountPin = Integer.parseInt(SAP);
	
					out = getClient().withdraw(accnum, amount, accountPin);
				} else if (getOperation() == "transfer") {
					int ToAccNum;
					while (true) {
						String ToAN = JOptionPane.showInputDialog("Enter recipient account number: ");
						if (ToAN == null) return;
						ToAccNum = Integer.parseInt(ToAN);
						out1 = getClient().transfer1(accnum, amount, ToAccNum);
						String YNC = JOptionPane.showInputDialog(out1);
						if (YNC.equalsIgnoreCase("YES")) {
							break;
						} else if (YNC.equalsIgnoreCase("No")) {
							JOptionPane.showMessageDialog(null, "Transfer Cancelled, try again.");
						} else {
							JOptionPane.showMessageDialog(null, "Transfer Cancelled");
							frame.setVisible(false);
							ATMGUIClient client = new ATMGUIClient();
							AccountSelection AS = new AccountSelection(getClient());
							MainPage MP = new MainPage(getClient(), AS, true);
							MP.go();
						}
					} // end of while
					SAP = JOptionPane.showInputDialog("Enter the account pin: ");
					accountPin = Integer.parseInt(SAP);
					out = getClient().tranfer2(accnum, amount, ToAccNum, accountPin);
					
				} else if (getOperation() == "deposit") {
					SAP = JOptionPane.showInputDialog("Enter the account pin: ");
					if (SAP == null) return;
					accountPin = Integer.parseInt(SAP);
					out = getClient().deposit(accnum, amount, accountPin);
				} else {
					out = "Failed";
				}
				
				// display operation result
				JOptionPane.showMessageDialog(null, out);
				
				// going back to main page
				operation = null;
				frame.setVisible(false);
				mainpage.go();
			}
		}

	}

	class cancelButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			operation = null;
			frame.setVisible(false);
			mainpage.go(); // going back to mainpage
		}
	}
}
