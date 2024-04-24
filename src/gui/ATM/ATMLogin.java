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

public class ATMLogin {
  public static void main(String[] args) {
    createWindow();
  }

  private static void createWindow() {
    JFrame frame = new JFrame("ATM Login");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    createUI(frame);
    frame.setSize(300, 300);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }

  private static void createUI(final JFrame frame) {
    JPanel panel = new JPanel();
    frame.add(panel);

    JButton okButton = new JButton("OK");
    okButton.setSize(200, 200);
    JButton exitButton = new JButton("EXIT");
    exitButton.setSize(200, 200);
    JTextField textField = new JTextField(25);
    JTextField textField2 = new JTextField(25);

    okButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(frame, "Ok Button clicked.");
        textField.setText("");
        textField2.setText("");
        mainpage(frame);
      }
    });

    exitButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(frame, "Goodbye");
        System.exit(0);
      }
    });

    panel.add(textField);
    panel.add(textField2);
    panel.add(okButton);
    panel.add(exitButton);
    frame.getContentPane().add(panel, BorderLayout.CENTER);

  }

  private static void mainpage(final JFrame frame) {
    

  }

}
