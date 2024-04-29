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

import main.TellerGUIClient;

public class TellerAddTellerPage {

	TellerMainPage tellerMainPage = null;
	TellerGUIClient tellerGuiClient = null;
	public TellerAddTellerPage(TellerMainPage tellerMainPage) {
		this.tellerMainPage = tellerMainPage;
		this.tellerGuiClient = tellerMainPage.getTellerClient();
	}

	public void createWindow() {
		JFrame frame = new JFrame("Add Teller");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		createUI(frame);

		frame.setSize(300, 200);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	private void createUI(final JFrame frame){
		// creates a new panel
		JPanel panel = new JPanel();
		LayoutManager layout = new FlowLayout();
		frame.add(panel);

		JButton okButton = new JButton("OK");
		okButton.setSize(200,200);
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setSize(100,100);
		JTextField textField = new JTextField(25);
		JTextField textField2 = new JTextField(21);
		JLabel label1 = new JLabel("Name:");
		JLabel label2 = new JLabel("Password:");

		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//textField getters
				String name = textField.getText();
				String password = textField2.getText();
				//if either id or password are blank, a frame opens up and asks for input.
				if (name.equals("") || password.equals("")) {
					JOptionPane.showMessageDialog(frame, "Please type in name or password.");
				} else {
					String result = tellerGuiClient.addTeller(name, password);

					// pop-up confirm page
					JOptionPane.showMessageDialog(frame, result);

					// going back to main page
					// put away input page
					frame.setVisible(false);
					tellerMainPage.run();

				}
			}
		} );

		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// put away input page
				frame.setVisible(false);
				tellerMainPage.run();
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
