package cephra.Phone.RewardsWallet;

import java.awt.*;
import java.util.List;
import java.awt.event.*;
import javax.swing.*;

public class Reciept extends javax.swing.JPanel {
  
    public Reciept() {
        initComponents();
        setPreferredSize(new java.awt.Dimension(370, 750));
        setSize(370, 750);
        setupLabelPosition(); // Set label position
        makeDraggable();
        populateAmounts();
    }
     // CUSTOM CODE - DO NOT REMOVE - NetBeans will regenerate form code but this method should be preserved
    // Setup label position to prevent NetBeans from changing it
    private void setupLabelPosition() {
        if (jLabel1 != null) {
            jLabel1.setBounds(0, 0, 370, 750);
        }
    }
     private void makeDraggable() {
        final Point[] dragPoint = {null};

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                dragPoint[0] = e.getPoint();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (dragPoint[0] != null) {
                    java.awt.Window window = SwingUtilities.getWindowAncestor(Reciept.this);
                    if (window != null) {
                        Point currentLocation = window.getLocation();
                        window.setLocation(
                            currentLocation.x + e.getX() - dragPoint[0].x,
                            currentLocation.y + e.getY() - dragPoint[0].y
                        );
                    }
                }
            }
        });
    }

    private void populateAmounts() {
        try {
            // Try to get ticket ID from PayPop first (most recent payment)
            String ticket = getTicketFromPayPop();
            boolean isPayPopPayment = (ticket != null && !ticket.isEmpty());
            
            if (ticket == null || ticket.isEmpty()) {
                // Fallback to QueueFlow
                ticket = cephra.Phone.Utilities.QueueFlow.getCurrentTicketId();
            }
            if (ticket == null || ticket.isEmpty()) return;
            
            // Get amount - prioritize PayPop payment amount for online payments
            double amount = 0.0;
            if (isPayPopPayment) {
                // For PayPop payments, get the actual payment amount from PayPop
                amount = cephra.Phone.Popups.PayPop.getPaymentAmount();
            }
            
            // If PayPop amount is not available or 0, try other sources
            if (amount <= 0) {
                // Get amount from charging history since ticket may have been removed from queue
                amount = getAmountFromChargingHistory(ticket);
                if (amount <= 0) {
                    // Fallback to QueueBridge calculation if not found in history
                    amount = cephra.Admin.Utilities.QueueBridge.computeAmountDue(ticket);
                }
            }
            
            
            // Handle reference number - hide for PayPop payments
            String refNumber = "";
            if (!isPayPopPayment) {
                // Get reference number from queue bridge first (most up-to-date)
                refNumber = cephra.Admin.Utilities.QueueBridge.getTicketRefNumber(ticket);
                
                // If not found in queue, try admin history
                if (refNumber.isEmpty()) {
                    try {
                        List<Object[]> adminRecords = cephra.Admin.Utilities.HistoryBridge.getRecordsForUser(cephra.Database.CephraDB.getCurrentUsername());
                        if (adminRecords != null) {
                            for (Object[] record : adminRecords) {
                                if (record.length >= 7 && ticket.equals(String.valueOf(record[0]))) {
                                    refNumber = String.valueOf(record[6]); // Reference number is at index 6
                                    break;
                                }
                            }
                        }
                    } catch (Exception e) {
                        System.err.println("Error getting reference from admin history: " + e.getMessage());
                    }
                }
                
                // If still empty, generate a temporary reference
                if (refNumber.isEmpty()) {
                    refNumber = String.format("REF%s", ticket);
                }
            }
            
            
            // Amount is already a double, so use it directly
            double finalAmount = amount;
            
            
            AmountPaid.setText(String.format("Php %.2f", finalAmount));
            Fee.setText(String.format("Php %.2f", cephra.Admin.Utilities.QueueBridge.getMinimumFee()));
            price.setText(String.format("PHP %.2f", finalAmount));
            
            // Handle reference number display - show reference for all payments
            if (isPayPopPayment) {
                // For PayPop payments, use the ticket ID as reference or get from PayPop
                RefNumber.setText(ticket); // Use ticket ID as reference for PayPop payments
                jLabel8.setText("Ref. Number: "); // Show the label
            } else {
                RefNumber.setText(refNumber); // Use admin queue reference number
                jLabel8.setText("Ref. Number: "); // Show the label for non-PayPop payments
            }
            
            NumTicket.setText(ticket); // Use FCH ticket number
            
            TimeDate.setText(java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd MMMM yyyy hh:mm a")));
        } catch (Throwable t) {
            System.err.println("Error populating receipt amounts: " + t.getMessage());
        }
    }
    
    /**
     * Gets the actual payment amount from charging history
     * @param ticket the ticket ID
     * @return the amount paid, or 0 if not found
     */
    private double getAmountFromChargingHistory(String ticket) {
        try {
            String username = cephra.Database.CephraDB.getCurrentUsername();
            if (username == null || username.isEmpty()) return 0;
            
            // Get charging history records for the user
            java.util.List<Object[]> adminRecords = cephra.Admin.Utilities.HistoryBridge.getRecordsForUser(username);
            if (adminRecords != null) {
                for (Object[] record : adminRecords) {
                    if (record.length >= 7 && ticket.equals(String.valueOf(record[0]))) {
                        // Admin history format: [Ticket, Customer, KWh, Total, Served By, Date & Time, Reference]
                        Object amountObj = record[3]; // Total amount is at index 3 in admin history format
                        if (amountObj instanceof Number) {
                            double amount = ((Number) amountObj).doubleValue();
                            return amount;
                        } else if (amountObj instanceof String) {
                            try {
                                double amount = Double.parseDouble((String) amountObj);
                                return amount;
                            } catch (NumberFormatException e) {
                                System.err.println("Error parsing amount from history: " + amountObj);
                            }
                        }
                        break;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error getting amount from charging history: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
    
    /**
     * Gets the ticket ID from PayPop if available
     * @return the ticket ID from PayPop, or null if not available
     */
    private String getTicketFromPayPop() {
        try {
            // Try to get the ticket ID from PayPop's current instance
            String ticketId = cephra.Phone.Popups.PayPop.getCurrentTicketId();
            if (ticketId != null && !ticketId.trim().isEmpty()) {
                return ticketId;
            }
        } catch (Exception e) {
            System.err.println("Receipt: Error getting ticket from PayPop: " + e.getMessage());
        }
        return null;
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        share = new javax.swing.JButton();
        Exit = new javax.swing.JButton();
        Download = new javax.swing.JButton();
        price = new javax.swing.JLabel();
        RefNumber = new javax.swing.JLabel();
        TimeDate = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        AmountPaid = new javax.swing.JLabel();
        Fee = new javax.swing.JLabel();
        AccNumber = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        NumTicket = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(370, 750));
        setPreferredSize(new java.awt.Dimension(370, 750));
        setLayout(null);

        share.setBorder(null);
        share.setBorderPainted(false);
        share.setContentAreaFilled(false);
        add(share);
        share.setBounds(200, 680, 130, 40);

        Exit.setBorder(null);
        Exit.setBorderPainted(false);
        Exit.setContentAreaFilled(false);
        Exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExitActionPerformed(evt);
            }
        });
        add(Exit);
        Exit.setBounds(310, 50, 30, 30);

        Download.setBorder(null);
        Download.setBorderPainted(false);
        Download.setContentAreaFilled(false);
        add(Download);
        Download.setBounds(50, 683, 110, 30);

        price.setFont(new java.awt.Font("Segoe UI", 3, 36)); // NOI18N
        price.setText("PHP 650.00");
        add(price);
        price.setBounds(85, 280, 210, 40);

        RefNumber.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        RefNumber.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        RefNumber.setText("33873585637");
        add(RefNumber);
        RefNumber.setBounds(185, 390, 100, 21);

        TimeDate.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        TimeDate.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TimeDate.setText("29 August 2025 05:31 PM");
        add(TimeDate);
        TimeDate.setBounds(90, 410, 190, 20);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel5.setText("Details");
        add(jLabel5);
        jLabel5.setBounds(160, 450, 70, 30);

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setText("<html><div style='text-align:leading;'>Amount Paid: <br>Fee: <br>Account Number:  </div></html>");
        add(jLabel6);
        jLabel6.setBounds(65, 490, 110, 70);

        AmountPaid.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        AmountPaid.setText("Php 650.00");
        add(AmountPaid);
        AmountPaid.setBounds(220, 490, 100, 20);

        Fee.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Fee.setText("Php 7.00");
        add(Fee);
        Fee.setBounds(220, 510, 140, 20);

        AccNumber.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        AccNumber.setText("337293727");
        add(AccNumber);
        AccNumber.setBounds(220, 530, 90, 20);

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Ref. Number: ");
        add(jLabel8);
        jLabel8.setBounds(85, 390, 100, 21);
        jLabel8.getAccessibleContext().setAccessibleName("");

        NumTicket.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        NumTicket.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        NumTicket.setText("FCH008");
        add(NumTicket);
        NumTicket.setBounds(140, 360, 100, 30);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/Receipt.png"))); // NOI18N
        add(jLabel1);
        jLabel1.setBounds(0, 0, 370, 750);
    }// </editor-fold>//GEN-END:initComponents

    @Override
    public void addNotify() {
        super.addNotify();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                setupLabelPosition();
            }
        });
    }

    private void ExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExitActionPerformed
        java.awt.Window[] windows = java.awt.Window.getWindows();
        for (java.awt.Window window : windows) {
            if (window instanceof cephra.Frame.Phone) {
                cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                phoneFrame.switchPanel(cephra.Phone.Dashboard.Home.getAppropriateHomePanel());
                break;
            }
        }
    }//GEN-LAST:event_ExitActionPerformed
@SuppressWarnings("unused")
    private void DownloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DownloadActionPerformed
        try {
            // Simulate download operation
            // In a real application, you would implement actual file saving logic here
            // For now, we'll just show a success message
            JOptionPane.showMessageDialog(
                this,
                "Receipt saved to Photos",
                "Saved",
                JOptionPane.INFORMATION_MESSAGE
            );
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this,
                "Error saving receipt: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }//GEN-LAST:event_DownloadActionPerformed
@SuppressWarnings("unused")
    private void shareActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_shareActionPerformed
        try {
            // Simulate sharing operation
            // In a real application, you would implement actual sharing logic here
            // For now, we'll just show a success message
            JOptionPane.showMessageDialog(
                this,
                "Link copied to clipboard",
                "Shared",
                JOptionPane.INFORMATION_MESSAGE
            );
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this,
                "Error sharing receipt: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }//GEN-LAST:event_shareActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel AccNumber;
    private javax.swing.JLabel AmountPaid;
    private javax.swing.JButton Download;
    private javax.swing.JButton Exit;
    private javax.swing.JLabel Fee;
    private javax.swing.JLabel NumTicket;
    private javax.swing.JLabel RefNumber;
    private javax.swing.JLabel TimeDate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel price;
    private javax.swing.JButton share;
    // End of variables declaration//GEN-END:variables
}
