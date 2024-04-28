package gui.Teller;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import main.TellerGUIClient;

public class TellerLogin {
	
	public TellerGUIClient client;
	public TellerMainPage mainpage;
	
	public TellerLogin (TellerGUIClient client, TellerMainPage mainpage) {
		this.client = client;
		this.mainpage = mainpage;
	}
	
	public TellerGUIClient getClient() {
		return this.client;
	}
	
	public TellerMainPage getMainPage() {
		return this.mainpage;
	}
	
	public void createWindow() {
	    JFrame frame = new JFrame("Teller Login");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	    tellerLoginUI(frame);
	    
	    frame.setSize(300, 200);
	    frame.setLocationRelativeTo(null);
	    frame.setVisible(true);
	}
		
	private void tellerLoginUI(final JFrame frame){
		// creates a new panel
		JPanel panel = new JPanel();
		LayoutManager layout = new FlowLayout();
		frame.add(panel);
		
		JButton okButton = new JButton("OK");
		okButton.setSize(200,200);
		JButton exitButton = new JButton("EXIT");
		exitButton.setSize(100,100);
		JTextField textField = new JTextField(25);
		JTextField textField2 = new JTextField(21);
		JLabel label1 = new JLabel("ID:");
		JLabel label2 = new JLabel("Password:");
		
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//textField getters
				String id = textField.getText();
				String password = textField2.getText();
				//if either id or password are blank, a frame opens up and asks for input.
				if (id.equals("") || password.equals("")) {
					JOptionPane.showMessageDialog(frame, "Please type in ID or password.");
				} else {
					String status = getClient().tellerLoginRequest(id, password);
					// if Login Request is successful, Login GUI is made invisible and MainPage opens
					if (status == "SUCCESS") {
						frame.setVisible(false);
						getMainPage().run();
					} else {
						JOptionPane.showMessageDialog(frame, "Invaild ID or password. Try again");
					}
				}
			}
		} );
		
		exitButton.addActionListener(new ActionListener() {
	          public void actionPerformed(ActionEvent e) {
	             JOptionPane.showMessageDialog(frame, "Goodbye");
	             //closes TellerLogin
	             System.exit(0);
	          }
		});
		
		panel.add(label1);
		panel.add(textField);
		panel.add(label2);
		panel.add(textField2);
		panel.add(okButton);
		panel.add(exitButton);
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		
	}
	
}