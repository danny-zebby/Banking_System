package gui.Teller;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import gui.Teller.TellerMainPage;
import main.TellerGUIClient;

public class TellerCreateUser {
	public static TellerGUIClient client;
	
	public TellerCreateUser (TellerGUIClient client) {
		this.client = client;
	}
	
	public static TellerGUIClient getClient() {
		return client;
	}
	
	public static void createWindow() {
	    JFrame frame = new JFrame("Create a Bank User");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    TellerLoginUI(frame);
	    frame.setSize(450,300);
	    frame.setLocationRelativeTo(null);
	    frame.setVisible(true);
	}
		
	private static void TellerLoginUI(final JFrame frame){
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
		JTextField textField3 = new JTextField(21);
		JLabel label1 = new JLabel("Enter name: ");
		JLabel label2 = new JLabel("Enter date of birth (**/**/****): ");
		JLabel label3 = new JLabel("Enter password: ");
		
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = textField.getText();
				String birthday = textField2.getText();
				String password = textField3.getText();
				
				
				//if either id or password are blank, a frame opens up and asks for input.
				if (name.equals("") || birthday.equals("") || password.equals("")) {
					JOptionPane.showMessageDialog(frame, "Please type in ID or password.");
				}else {
					//check if user meant what they enter
					String userConfirm = JOptionPane.showInputDialog("Please type yes to confirm your information below."
							+ "\nName: " + name + " D.O.T: " + birthday + " Passwords: " + password);
			
					if (userConfirm.equalsIgnoreCase("YES")) {
						//if either id or password are blank, a frame opens up and asks for input.
						String status = getClient().createUser(name, birthday, password);
						// if Login Request is successful, Login GUI is made invisible and MainPage opens
						if (status == "SUCCESS") {
							JOptionPane.showMessageDialog(frame,"new User info: \n" + getClient().getUser());
							frame.setVisible(false);
							// go to telleruserpage
						}else {
							JOptionPane.showMessageDialog(frame, "Invaild ID or password. Try again");
						}
					} // end of if
				} // end if else 
			}
		} );
		
		exitButton.addActionListener(new ActionListener() {
	          public void actionPerformed(ActionEvent e) {
	             JOptionPane.showMessageDialog(frame, "Back to Main Page");
	             //closes TellerCreateUser
	             frame.setVisible(false);
	             new TellerMainPage(getClient()).run();
	          }
		});
		
		panel.add(label1);
		panel.add(textField);
		panel.add(label2);
		panel.add(textField2);
		panel.add(label3);
		panel.add(textField3);
		panel.add(okButton);
		panel.add(exitButton);
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		
	}
}
