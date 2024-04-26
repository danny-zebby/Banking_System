package main;

import javax.swing.*;

import main.ATMGUIClient;
import gui.ATM.ATMLogin;
import gui.ATM.AccountSelection;
import gui.ATM.MainPage;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ATMMain extends JFrame {
	
	ATMGUIClient client = null;
	AccountSelection As = null;
	MainPage mainpage = null;
	ATMLogin AL = null;
	
    public static void main(String[] args) {
    	
    	ATMGUIClient client = new ATMGUIClient();
		AccountSelection AS = new AccountSelection(client);
		MainPage MP = new MainPage(client, AS, false);
		ATMLogin AL = new ATMLogin(client, MP);
		
		client.go();
		//Login Gui
		AL.createWindow();
		
		// Open 
		
		
   }
}
