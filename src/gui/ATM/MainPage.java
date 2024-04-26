package gui.ATM;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import main.ATMGUIClient;

import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.*;

import java.net.*;
import java.io.*;
import message.*;
import java.util.*;

public class MainPage {
	
	public static ATMGUIClient client;
	
	public MainPage(ATMGUIClient client){
		this.client = client;
	}
	public static ATMGUIClient getClient() {
		return client;
	}
	
	 public void createWindow()
	 {
		 getClient().getAccountsInfo();
		 Map accounts = getClient().getAccounts();
		 
		 String[] commands = {"Withdraw",
				 	"Deposit",
				 	"Transfer",
				 	"LogOut",};
		 // "0-Withdraw\n1-Deposit\n2-Transfer\n3-LogOut"
		int choice;
		do {
			choice = JOptionPane.showOptionDialog(null,
					 "Bank Account Infomation:\n" + accounts + "\nSelect a command\n",
					 "TBD", 
					 JOptionPane.YES_NO_CANCEL_OPTION, 
					 JOptionPane.QUESTION_MESSAGE, 
					 null, 
					 commands,
					 commands[commands.length - 1]);
			 switch (choice) {
			 	case 0: withdraw(); break;
			 	case 1: deposit(); break;
			 	case 2: transfer(); break;
			 	case 3: logout(); break;
			 } 
		 } while (choice != commands.length-1);
		 System.exit(0);
	 }
	 
	 private void withdraw() {
		 String opperation = "withdraw";
		 String Amount = JOptionPane.showInputDialog("Enter Amount to " + opperation + ":");
//		 Int accountPin = JOptionPane.showInputDialog("Enter the account pin: ");
		 
	 }
	 private void deposit() {}
	 private void transfer() {}
	 private void logout() {}
	 
}
