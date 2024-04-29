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

import main.*;

public class TellerForgetPasswordPage {
	TellerGUIClient tellerGUIClient;
	TellerUserAccount tellerUserAccount;

	public TellerForgetPasswordPage(TellerUserAccount tellerUserAccount) {
		this.tellerUserAccount = tellerUserAccount;
		this.tellerGUIClient = tellerUserAccount.getTellerGUIClient();
	}

	public TellerGUIClient getTellerGUIClient() {
		return this.tellerGUIClient;
	}

	public TellerUserAccount getTellerUserAccount() {
		return this.tellerUserAccount;
	}

	public TellerForgetPasswordPage getTellerForgetPasswordPage() {
		return this;
	}

	public void go() {
		JFrame frame = new JFrame("Forget Password");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		tellerLoginUI(frame);

		frame.setSize(300, 200);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	private void tellerLoginUI(final JFrame frame) {
		// creates a new panel
		JPanel panel = new JPanel();
		LayoutManager layout = new FlowLayout();
		frame.add(panel);

		JButton okButton = new JButton("OK");
		okButton.setSize(200, 200);
		JButton cancelButton = new JButton("CANCEL");
		cancelButton.setSize(100, 100);
		JTextField textField = new JTextField(25);
		JTextField textField2 = new JTextField(21);
		JLabel label1 = new JLabel("Birthday (MM/DD/YYYY):");
		JLabel label2 = new JLabel("Password:");

		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// textField getters
				String birthday = textField.getText();
				String password = textField2.getText();
				// if either id or password are blank, a frame opens up and asks for input.
				if (birthday.equals("") || password.equals("")) {
					JOptionPane.showMessageDialog(frame, "Please type in both birthday and new password.");
				} else {
					// show confirm dialog
					int choice = JOptionPane.showConfirmDialog(null,
							"Please type yes to confirm your new password: " + password, "Confirmation",
							JOptionPane.OK_CANCEL_OPTION);
					switch (choice) {
					case JOptionPane.OK_OPTION: {
						String result = tellerGUIClient.forgetPassword(birthday, password);
						JOptionPane.showMessageDialog(frame, result);
						// going back to TellerUserAccount page
						frame.setVisible(false);
						getTellerUserAccount().run();
						break;
					}
					case JOptionPane.CANCEL_OPTION: {
						return;
					}
					default:
						break;
					} // end switch statement

				}
			}
		});

		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// going back to TellerUserAccount page
				frame.setVisible(false);
				getTellerUserAccount().run();
			}
		});

		panel.add(label1);
		panel.add(textField);
		panel.add(label2);
		panel.add(textField2);
		panel.add(okButton);
		panel.add(cancelButton);
		frame.getContentPane().add(panel, BorderLayout.CENTER);

	}
}
