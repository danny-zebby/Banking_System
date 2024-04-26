package main;

import javax.swing.*;

import main.ATMGUIClient;
import gui.ATM.ATMLogin;
import gui.ATM.MainPage;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ATMMain extends JFrame {
		
    public static void main(String[] args) {
		// Start Client
    	ATMGUIClient client = new ATMGUIClient();
		client.go();
		
		// Create GUIs
		MainPage MP = new MainPage(client);
		ATMLogin AL = new ATMLogin(client, MP);
		
		
		//Login Gui
		AL.createWindow();
		
		// Open 
		
		
   }
}
