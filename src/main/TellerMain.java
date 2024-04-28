package main;

import javax.swing.*;

import main.TellerGUIClient;
import gui.Teller.TellerLogin;
import gui.Teller.TellerMainPage;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TellerMain extends JFrame {
	
	TellerGUIClient client = null;
	TellerMainPage mainpage = null;
	TellerLogin TL = null;
	
    public static void main(String[] args) {
    	
    	TellerGUIClient client = new TellerGUIClient();
		TellerMainPage TMP = new TellerMainPage(client);
		TellerLogin TL = new TellerLogin(client, TMP);
		
		client.go();
		//Login Gui
		TL.createWindow();
		// Open 
		
		
   }
}