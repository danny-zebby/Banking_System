package gui.Teller;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;

import gui.Teller.TellerSelectAccountForAddUserPage.cancelButtonListener;
import gui.Teller.TellerSelectAccountForAddUserPage.confirmButtonListener;
import main.BankAccount;
import main.TellerGUIClient;

public class TellerSelectAccountForChangePinPage {
	JFrame frame = null;
	JPanel centerPanel = null;
	JPanel southPanel = null;
	Vector<String> entries = null;
	JList<String> list = null;
	TellerUserAccount tellerUserAccount = null;

	public TellerGUIClient tellerGUIClient = null;

	public TellerSelectAccountForChangePinPage(TellerUserAccount tellerUserAccount) {
		this.tellerUserAccount = tellerUserAccount;
		this.tellerGUIClient = tellerUserAccount.getTellerGUIClient();
	}

	public void go() {
		// create new frame
		frame = new JFrame("Choose an account");

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
		tellerGUIClient.getAccountsInfo();
		Map<Integer, BankAccount> accountsInfo = tellerGUIClient.getAccounts();

		for (int accountNumber : accountsInfo.keySet()) {
			entries.add(
					String.format("Account #%d : $%.2f", accountNumber, accountsInfo.get(accountNumber).getBalance()));
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
			if (list.isSelectionEmpty()) { // if nothing is selected
				JOptionPane.showMessageDialog(frame, "Please select an account.");
				return;
			}

			// one line
			String selectionItem = list.getSelectedValue();
			// split the line into two parts
			String[] parts = selectionItem.split(" :");
			// Extract the number part
			String number = parts[0].substring(9);
			// Convert the number to an integer
			int accountNumber = Integer.parseInt(number);
			int pin = 0;
			String input;
			while (true) {
				input = JOptionPane.showInputDialog("Enter new Pin: ");
				if (input == null) return; // if the user clicks cancel
				try {
					pin = Integer.parseInt(input);
					break;
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Pin is an integer, please try again.");
				}
			}

			int choice = JOptionPane.showConfirmDialog(frame, String
					.format("Please confirm that your new pin is %s", input),
					"Confirmation", JOptionPane.OK_CANCEL_OPTION);

			if (choice == JOptionPane.CANCEL_OPTION) {
				return;
			} else if (choice == JOptionPane.OK_OPTION) {
				String result = tellerGUIClient.changePin(accountNumber, pin);
				JOptionPane.showMessageDialog(null, result);
				frame.setVisible(false);
				tellerUserAccount.run();

			}
		}
	}

	class cancelButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {

			frame.setVisible(false);
			tellerUserAccount.run(); // going back to mainpage
		}
	}
}
