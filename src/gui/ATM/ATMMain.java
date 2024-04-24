package ATM;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ATMMain extends JFrame {

    public ATMMain() {
        setTitle("ATM Interface");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);

        // Create a panel to hold the buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 10)); // 3 rows, 1 column, with gaps

        // Create three buttons
        JButton depositButton = new JButton("Deposit");
        JButton withdrawButton = new JButton("Withdraw");
        JButton balanceButton = new JButton("Check Balance");

        // Add action listeners to handle button clicks
        depositButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(ATMMain.this, "Deposit operation selected");
                // You can add your deposit logic here
            }
        });

        withdrawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(ATMMain.this, "Withdraw operation selected");
                // You can add your withdraw logic here
            }
        });

        balanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(ATMMain.this, "Check Balance operation selected");
                // You can add your check balance logic here
            }
        });

        // Add buttons to the panel
        buttonPanel.add(depositButton);
        buttonPanel.add(withdrawButton);
        buttonPanel.add(balanceButton);

        // Add the panel to the frame
        getContentPane().add(buttonPanel, BorderLayout.CENTER);

        // Center the frame on the screen
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        // Create and display the GUI on the Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ATMMain atmMain = new ATMMain();
                atmMain.setVisible(true);
            }
        });
    }
}
