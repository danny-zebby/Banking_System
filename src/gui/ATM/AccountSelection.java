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
	
	public static ATMGUIClient client;
	private String opperation = null;
	
	public AccountSelection(ATMGUIClient client){
		this.client = client;
	}
	public static ATMGUIClient getClient() {
		return client;
	}
	public void setOpperation(String opperation) {
		this.opperation = opperation;
	}
	public String getOpperation() {
		return this.opperation;
	}
	public void setMainPage(MainPage mainpage) {
		this.mainpage = mainpage;
	}
	
//	public static void main(String[] args) {
//		new AccountSelection().go();
//	}
	
	public void go(String opperation) {
		// create new frame
		frame = new JFrame("Select an Account to " + opperation + " from");
		setOpperation(opperation);
		
		// create new panel
		centerPanel = new JPanel();
		southPanel = new JPanel();
		JButton confirmButton = new JButton("Confirm");
		JButton cancelButton = new JButton("Cancel");
		
		confirmButton.addActionListener(new confirmButtonListener());
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
		
		for(int accountNumber : accounts) {
			entries.add("Acc " + accountNumber);
		}
		
		list = new JList<>(entries);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // restrict the user to select only one thing at a time
		
		JScrollPane scroller = new JScrollPane(list);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scroller.setBounds(10, 10, 470, 200);
		centerPanel.add(scroller);
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
		
	}
	
	
	class confirmButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			if (getOpperation() == null) {
				
			}else {
				String selectionItem = list.getSelectedValue();
				String[] parts = selectionItem.split("\\s+");
	            // Extract the number part 
	            String number = parts[parts.length - 1];            
	            // Convert the number to an integer
	            int accnum = Integer.parseInt(number);
	            
	            // Get Amount
	            String Amount = JOptionPane.showInputDialog("Enter Amount to " + opperation + ":");
	            double amount = Double.parseDouble(Amount);
	            Amount = String.format("%.2f", amount);	
	            double AccBal = Double.parseDouble(Amount);
	            
	            // Three opperations
	            String out, out1, SAP;
	            int accountPin;
	            if(getOpperation() == "withdraw") {
	            	SAP = JOptionPane.showInputDialog("Enter the account pin: ");
		            accountPin = Integer.parseInt(SAP);
	            	out = getClient().withdraw(accnum, amount, accountPin);
	            }else if(getOpperation() == "transfer"){
	            	int ToAccNum;
	            	while(true) {
	            		String ToAN = JOptionPane.showInputDialog("Enter recipient account number: ");
			            ToAccNum = Integer.parseInt(ToAN);
	            		out1 = getClient().transfer1(accnum, amount, ToAccNum);
	            		String YNC = JOptionPane.showInputDialog(out1);
	            		if(YNC.equalsIgnoreCase("YES")) {
	            			break;
	            		}else if(YNC.equalsIgnoreCase("No")) {
	            			JOptionPane.showMessageDialog(null, "Transfer Cancelled, try again.");
	            		}else {
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
	            	
	            }else if(getOpperation() == "deposit"){
	            	SAP = JOptionPane.showInputDialog("Enter the account pin: ");
		            accountPin = Integer.parseInt(SAP);
	            	out = getClient().deposit(accnum, amount, accountPin);
	            }else {
	            	out = "Failed";
	            }
	            JOptionPane.showMessageDialog(null, out);
	            frame.setVisible(false);
	            ATMGUIClient client = new ATMGUIClient();
	    		AccountSelection AS = new AccountSelection(getClient());
	    		MainPage MP = new MainPage(getClient(), AS, true);
	    		MP.go();
			}
		}
		
	}
	class cancelButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			String selectionItem = list.getSelectedValue();
			System.out.println(selectionItem);
		}
	}
}
