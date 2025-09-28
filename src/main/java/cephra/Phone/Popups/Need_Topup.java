/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package cephra.Phone.Popups;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 *
 * @author Kenji
 */
public class Need_Topup extends javax.swing.JPanel {

    // Static state management
    private static Need_Topup currentInstance = null;
    private static boolean isShowing = false;
    
    /**
     * Creates new form InsufficientBalPOP
     */
    public Need_Topup() {
        initComponents();
        setupActionListeners();
        
        // Make the background image label transparent
        if (ICON != null) {
            ICON.setOpaque(false);
        }
    }
    
    /**
     * Sets the balance information on the labels
     * @param currentBalance the current wallet balance
     * @param requiredAmount the amount required for payment
     * @param shortage the shortage amount
     */
    public void setBalanceInfo(double currentBalance, double requiredAmount, double shortage) {
        if (InsufficientCurrBal != null) {
            InsufficientCurrBal.setText(String.format("₱%.2f", currentBalance));
        }
        if (InsufficientRequiredAmount != null) {
            InsufficientRequiredAmount.setText(String.format("₱%.2f", requiredAmount));
        }
        if (InsufficientShortage != null) {
            InsufficientShortage.setText(String.format("₱%.2f", shortage));
        }
    }
    
    /**
     * Sets up action listeners for buttons
     */
    private void setupActionListeners() {
        // Add ESC key support for closing
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    hideNeedTopup();
                }
            }
        });
        
        // Request focus so key events work
        SwingUtilities.invokeLater(this::requestFocusInWindow);
    }
    
    /**
     * Navigates to TopUp screen while preserving Pending_Payment state
     */
    private void navigateToTopUp() {
        SwingUtilities.invokeLater(() -> {
            // Store the current Pending_Payment state before hiding
            String currentUser = cephra.Database.CephraDB.getCurrentUsername();
            String currentTicket = cephra.Phone.Utilities.QueueFlow.getCurrentTicketId();
            
            if (currentUser != null && currentTicket != null) {
                // Set pending state in Pending_Payment class
                setPendingPaymentState(currentUser, currentTicket);
            }
            
            hideNeedTopup();
            Window[] windows = Window.getWindows();
            for (Window window : windows) {
                if (window instanceof cephra.Frame.Phone) {
                    cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                    phoneFrame.switchPanel(new cephra.Phone.RewardsWallet.Topup());
                    break;
                }
            }
        });
    }
    
    /**
     * Sets the pending payment state in Pending_Payment class
     * @param username the current username
     * @param ticketId the current ticket ID
     */
    private void setPendingPaymentState(String username, String ticketId) {
        try {
            // Use reflection to set the pending state in Pending_Payment class
            Class<?> pendingPaymentClass = cephra.Phone.Popups.Pending_Payment.class;
            
            // Set isPendingTopUpReturn to true
            java.lang.reflect.Field pendingField = pendingPaymentClass.getDeclaredField("isPendingTopUpReturn");
            pendingField.setAccessible(true);
            pendingField.set(null, true);
            
            // Set pendingTicketId
            java.lang.reflect.Field ticketField = pendingPaymentClass.getDeclaredField("pendingTicketId");
            ticketField.setAccessible(true);
            ticketField.set(null, ticketId);
            
            // Set pendingCustomerUsername
            java.lang.reflect.Field userField = pendingPaymentClass.getDeclaredField("pendingCustomerUsername");
            userField.setAccessible(true);
            userField.set(null, username);
            
        } catch (Exception e) {
            System.err.println("Error setting pending payment state: " + e.getMessage());
        }
    }
    
    /**
     * Hides the Need_Topup panel
     */
    public static void hideNeedTopup() {
        if (currentInstance != null && isShowing) {
            SwingUtilities.invokeLater(() -> {
                if (currentInstance.getParent() != null) {
                    currentInstance.getParent().remove(currentInstance);
                }
                currentInstance = null;
                isShowing = false;
                
                // Repaint the phone frame
                for (Window window : Window.getWindows()) {
                    if (window instanceof cephra.Frame.Phone) {
                        window.repaint();
                        break;
                    }
                }
            });
        }
    }

   
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        TopUpBTN = new javax.swing.JButton();
        InsufficientCurrBal = new javax.swing.JLabel();
        InsufficientRequiredAmount = new javax.swing.JLabel();
        InsufficientShortage = new javax.swing.JLabel();
        EXT = new javax.swing.JButton();
        ICON = new javax.swing.JLabel();

        setOpaque(false);
        setLayout(null);

        TopUpBTN.setBorder(null);
        TopUpBTN.setBorderPainted(false);
        TopUpBTN.setContentAreaFilled(false);
        TopUpBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TopUpBTNActionPerformed(evt);
            }
        });
        add(TopUpBTN);
        TopUpBTN.setBounds(60, 293, 210, 35);

        InsufficientCurrBal.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        InsufficientCurrBal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        InsufficientCurrBal.setText("jLabel1");
        add(InsufficientCurrBal);
        InsufficientCurrBal.setBounds(180, 169, 100, 16);

        InsufficientRequiredAmount.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        InsufficientRequiredAmount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        InsufficientRequiredAmount.setText("jLabel2");
        add(InsufficientRequiredAmount);
        InsufficientRequiredAmount.setBounds(180, 193, 100, 16);

        InsufficientShortage.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        InsufficientShortage.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        InsufficientShortage.setText("jLabel3");
        add(InsufficientShortage);
        InsufficientShortage.setBounds(180, 217, 100, 16);

        EXT.setBorder(null);
        EXT.setBorderPainted(false);
        EXT.setContentAreaFilled(false);
        EXT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EXTActionPerformed(evt);
            }
        });
        add(EXT);
        EXT.setBounds(260, 30, 30, 30);

        ICON.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/InsufficientBalPOP.png"))); // NOI18N
        add(ICON);
        ICON.setBounds(30, 20, 270, 330);
    }// </editor-fold>//GEN-END:initComponents

    private void EXTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EXTActionPerformed
        hideNeedTopup();
    }//GEN-LAST:event_EXTActionPerformed

    private void TopUpBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TopUpBTNActionPerformed
        navigateToTopUp();
    }//GEN-LAST:event_TopUpBTNActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton EXT;
    private javax.swing.JLabel ICON;
    private javax.swing.JLabel InsufficientCurrBal;
    private javax.swing.JLabel InsufficientRequiredAmount;
    private javax.swing.JLabel InsufficientShortage;
    private javax.swing.JButton TopUpBTN;
    // End of variables declaration//GEN-END:variables
}
