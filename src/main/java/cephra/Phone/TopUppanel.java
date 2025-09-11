
package cephra.Phone;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TopUppanel extends javax.swing.JPanel {

    // Quick transaction amounts - updated to match the button values
    private static final double[] QUICK_AMOUNTS = {500.0, 1000.0, 2500.0, 5000.0, 10000.0, 20000.0};

    public TopUppanel() {
        initComponents();
        setPreferredSize(new java.awt.Dimension(370, 750));
        setSize(370, 750);
        setupQuickAmountButtons();
        loadCurrentBalance();
        initializeCustomAmount();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        Currentbalance = new javax.swing.JLabel();
        A = new javax.swing.JButton();
        B = new javax.swing.JButton();
        C = new javax.swing.JButton();
        D = new javax.swing.JButton();
        E = new javax.swing.JButton();
        F = new javax.swing.JButton();
        Proceed = new javax.swing.JButton();
        Customamount = new javax.swing.JTextField();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        jRadioButton4 = new javax.swing.JRadioButton();
        bck = new javax.swing.JButton();

        setLayout(null);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(null);

        Currentbalance.setText("xxxxxxx");
        jPanel1.add(Currentbalance);
        Currentbalance.setBounds(120, 100, 70, 30);

        A.setText("500");
        jPanel1.add(A);
        A.setBounds(40, 190, 72, 23);

        B.setText("1000");
        jPanel1.add(B);
        B.setBounds(150, 190, 72, 23);

        C.setText("2500");
        jPanel1.add(C);
        C.setBounds(260, 190, 72, 23);

        D.setText("5000");
        jPanel1.add(D);
        D.setBounds(40, 260, 72, 23);

        E.setText("10000");
        jPanel1.add(E);
        E.setBounds(150, 260, 72, 23);

        F.setText("20000");
        jPanel1.add(F);
        F.setBounds(260, 260, 75, 23);

        Proceed.setText("Procceed");
        Proceed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ProceedActionPerformed(evt);
            }
        });
        jPanel1.add(Proceed);
        Proceed.setBounds(80, 500, 190, 70);

        Customamount.setText("xxxxxx");
        jPanel1.add(Customamount);
        Customamount.setBounds(110, 300, 130, 22);

        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setText("Earnest Bank");
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jRadioButton1);
        jRadioButton1.setBounds(140, 360, 90, 21);

        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setText("Glangs");
        jPanel1.add(jRadioButton2);
        jRadioButton2.setBounds(140, 380, 90, 21);

        buttonGroup1.add(jRadioButton3);
        jRadioButton3.setText("Exness");
        jPanel1.add(jRadioButton3);
        jRadioButton3.setBounds(140, 420, 120, 21);

        buttonGroup1.add(jRadioButton4);
        jRadioButton4.setText("Kalapati");
        jPanel1.add(jRadioButton4);
        jRadioButton4.setBounds(140, 400, 100, 21);

        bck.setText("Back");
        bck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bckActionPerformed(evt);
            }
        });
        jPanel1.add(bck);
        bck.setBounds(30, 40, 72, 23);

        add(jPanel1);
        jPanel1.setBounds(0, 0, 360, 720);
    }// </editor-fold>//GEN-END:initComponents

    private void ProceedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ProceedActionPerformed
        processTopUp();
        
    }//GEN-LAST:event_ProceedActionPerformed

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private void bckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bckActionPerformed
        // TODO add your handling code here:
          SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frame.Phone) {
                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                        phoneFrame.switchPanel(new cephra.Phone.Wallet());
                        break;
                    }
                }
            }
        });
    }//GEN-LAST:event_bckActionPerformed

    /**
     * Sets up the quick amount buttons with action listeners
     */
    private void setupQuickAmountButtons() {
        JButton[] buttons = {A, B, C, D, E, F};
        
        for (int i = 0; i < buttons.length; i++) {
            final int index = i;
            JButton button = buttons[i];
            
            // Add action listener to set custom amount when clicked
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setCustomAmount(QUICK_AMOUNTS[index]);
                }
            });
        }
    }

    /**
     * Loads and displays the current wallet balance
     */
    private void loadCurrentBalance() {
        try {
            String currentUser = cephra.CephraDB.getCurrentUsername();
            if (currentUser != null && !currentUser.isEmpty()) {
                double balance = cephra.CephraDB.getUserWalletBalance(currentUser);
                Currentbalance.setText(String.format("₱%.2f", balance));
            } else {
                Currentbalance.setText("₱0.00");
            }
        } catch (Exception e) {
            System.err.println("Error loading current balance: " + e.getMessage());
            Currentbalance.setText("₱0.00");
        }
    }

    /**
     * Initializes the custom amount field
     */
    private void initializeCustomAmount() {
        Customamount.setText("0.00");
        Customamount.setHorizontalAlignment(JTextField.CENTER);
    }

    /**
     * Sets the custom amount field with the selected quick amount
     */
    private void setCustomAmount(double amount) {
        Customamount.setText(String.format("%.2f", amount));
    }

    /**
     * Processes the top-up transaction
     */
    private void processTopUp() {
        try {
            String currentUser = cephra.CephraDB.getCurrentUsername();
            if (currentUser == null || currentUser.isEmpty()) {
                showErrorMessage("No user is currently logged in.");
                return;
            }

            // Check if a payment method (radio button) is selected
            String selectedPaymentMethod = getSelectedPaymentMethod();
            if (selectedPaymentMethod == null) {
                showErrorMessage("Please select a payment method to proceed.");
                return;
            }

            // Parse the amount from the custom amount field
            String amountText = Customamount.getText().trim();
            if (amountText.isEmpty()) {
                showErrorMessage("Please enter an amount to top up.");
                return;
            }

            double amount;
            try {
                amount = Double.parseDouble(amountText);
            } catch (NumberFormatException e) {
                showErrorMessage("Please enter a valid amount.");
                return;
            }

            if (amount <= 0) {
                showErrorMessage("Please enter a positive amount.");
                return;
            }

            if (amount > 50000) {
                showErrorMessage("Maximum top-up amount is ₱50,000.00");
                return;
            }

            // Determine which quick amount was selected or if it's custom
            String topUpMethod = "Custom Amount";
            for (int i = 0; i < QUICK_AMOUNTS.length; i++) {
                if (Math.abs(amount - QUICK_AMOUNTS[i]) < 0.01) {
                    topUpMethod = "Quick Amount ";
                    break;
                }
            }
            
            // Include selected payment method in the description
            topUpMethod += " via " + selectedPaymentMethod;

            // Process the top-up
            boolean success = cephra.CephraDB.processWalletTopUp(currentUser, amount, topUpMethod);

            if (success) {
                showSuccessMessage(String.format("Successfully topped up ₱%.2f to your wallet via %s!", amount, selectedPaymentMethod));
                loadCurrentBalance(); // Refresh balance display
                Customamount.setText("0.00"); // Reset amount field
                clearPaymentMethodSelection(); // Clear radio button selection
                
                  SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frame.Phone) {
                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                        phoneFrame.switchPanel(new cephra.Phone.Wallet());
                        break;
                    }
                }
            }
        });
            } else {
                showErrorMessage("Failed to process top-up. Please try again.");
            }

        } catch (Exception e) {
            System.err.println("Error processing top-up: " + e.getMessage());
            e.printStackTrace();
            showErrorMessage("An error occurred while processing your top-up.");
        }
    }

    /**
     * Shows an error message to the user
     */
    private void showErrorMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(
                this,
                message,
                "Top-Up Error",
                JOptionPane.ERROR_MESSAGE
            );
        });
    }

    /**
     * Shows a success message to the user
     */
    private void showSuccessMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(
                this,
                message,
                "Top-Up Successful",
                JOptionPane.INFORMATION_MESSAGE
            );
        });
    }

    /**
     * Public method to refresh balance when returning to this screen
     */
    public void refreshBalance() {
        loadCurrentBalance();
    }
    
    /**
     * Gets the selected payment method from radio buttons
     * @return the selected payment method name, or null if none selected
     */
    private String getSelectedPaymentMethod() {
        if (jRadioButton1.isSelected()) {
            return "Earnest Bank";
        } else if (jRadioButton2.isSelected()) {
            return "Glangs";
        } else if (jRadioButton3.isSelected()) {
            return "Exness";
        } else if (jRadioButton4.isSelected()) {
            return "Kalapati";
        }
        return null; // No radio button selected
    }
    
    /**
     * Clears the selected payment method (radio button)
     */
    private void clearPaymentMethodSelection() {
        buttonGroup1.clearSelection();
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton A;
    private javax.swing.JButton B;
    private javax.swing.JButton C;
    private javax.swing.JLabel Currentbalance;
    private javax.swing.JTextField Customamount;
    private javax.swing.JButton D;
    private javax.swing.JButton E;
    private javax.swing.JButton F;
    private javax.swing.JButton Proceed;
    private javax.swing.JButton bck;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    // End of variables declaration//GEN-END:variables
}
