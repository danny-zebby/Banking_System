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
		TellerMainPage tellerMainPage = new TellerMainPage(client);
		TellerLogin tellerLogin = new TellerLogin(client, tellerMainPage);
		
		client.go(); // set up connection and do handshake with server
		
		tellerLogin.createWindow(); // start teller login

   }
}