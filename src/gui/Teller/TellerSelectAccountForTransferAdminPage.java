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

import gui.Teller.TellerSelectAccountForChangePinPage.cancelButtonListener;
import gui.Teller.TellerSelectAccountForChangePinPage.confirmButtonListener;
import main.BankAccount;
import main.TellerGUIClient;

public class TellerSelectAccountForTransferAdminPage {
	JFrame frame = null;
	JPanel centerPanel = null;
	JPanel southPanel = null;
	Vector<String> entries = null;
	JList<String> list = null;
	TellerUserAccount tellerUserAccount = null;
	TellerGUIClient tellerGUIClient = null;
	Map<Integer, Map<Integer, String>> adminAccountsInfo = null;
	Map<Integer, BankAccount> accountsInfo = null;

	public TellerSelectAccountForTransferAdminPage(TellerUserAccount tellerUserAccount) {
		this.tellerUserAccount = tellerUserAccount;
		this.tellerGUIClient = tellerUserAccount.getTellerGUIClient();
	}

	public void go() {
		// create new frame
		frame = new JFrame("Choose an admin account");

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
		adminAccountsInfo = tellerGUIClient.getAdminAccountsInfo();
		accountsInfo = tellerGUIClient.getAccounts();
		
		for (int accountNumber : adminAccountsInfo.keySet()) {
			entries.add(String.format("Account #%d : $%.2f", accountNumber, accountsInfo.get(accountNumber).getBalance()));
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
			
			// jump to TellerSelectUserForTransferAdminPage
			frame.setVisible(false);
			new TellerSelectUserForTransferAdminPage(tellerUserAccount, accountNumber, adminAccountsInfo).go();
		}
	}

	class cancelButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {

			frame.setVisible(false);
			tellerUserAccount.run(); // going back to tellerUserAccount page
		}
	}
}
