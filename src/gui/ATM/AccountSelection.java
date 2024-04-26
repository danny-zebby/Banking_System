package gui.ATM;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.Vector;

public class AccountSelection {
	JFrame frame = null;
	JPanel centerPanel = null;
	JPanel southPanel = null;
	Vector<String> entries = null;
	JList<String> list = null;
	
	public static void main(String[] args) {
		new AccountSelection().go();
	}
	
	public void go() {
		// create new frame
		frame = new JFrame();
		
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
		frame.setVisible(true);
	}
	
	public void createScrollList() {
		// add list to panel
		entries = new Vector<String>();
		
		entries.add("alpha");
		entries.add("beta");
		
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
			String selectionItem = list.getSelectedValue();
			System.out.println(selectionItem);
		}
	}
	class cancelButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			String selectionItem = list.getSelectedValue();
			System.out.println(selectionItem);
		}
	}
}
