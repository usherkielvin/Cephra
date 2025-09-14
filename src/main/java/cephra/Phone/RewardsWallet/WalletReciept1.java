package cephra.Phone.RewardsWallet;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.List;
import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;

public class WalletReciept1 extends javax.swing.JPanel {
  
    public WalletReciept1() {
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
                    java.awt.Window window = SwingUtilities.getWindowAncestor(WalletReciept1.this);
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
            String currentUser = cephra.Database.CephraDB.getCurrentUsername();
            if (currentUser == null || currentUser.isEmpty()) {
                System.err.println("No current user found for topup receipt");
                return;
            }
            
            // Get the most recent topup transaction for this user
            java.util.List<Object[]> transactions = cephra.Database.CephraDB.getWalletTransactionHistory(currentUser);
            Object[] latestTopup = null;
            
            // Find the most recent TOP_UP transaction
            for (Object[] transaction : transactions) {
                String transactionType = (String) transaction[0];
                if ("TOP_UP".equals(transactionType)) {
                    latestTopup = transaction;
                    break; // Transactions are ordered by date desc, so first match is most recent
                }
            }
            
            if (latestTopup == null) {
                System.err.println("No topup transaction found for receipt");
                return;
            }
            
            // Extract transaction data: [transaction_type, amount, new_balance, description, reference_id, transaction_date]
            double amount = Math.abs((Double) latestTopup[1]); // Get absolute amount
            String description = (String) latestTopup[3];
            String refNumber = (String) latestTopup[4];
            java.sql.Timestamp timestamp = (java.sql.Timestamp) latestTopup[5];
            
            // If no reference number, generate one
            if (refNumber == null || refNumber.isEmpty()) {
                refNumber = String.format("TU%d", System.currentTimeMillis() % 100000);
            }
            
            // Extract payment method from description if available
            String paymentMethod = "N/A";
            if (description != null && description.contains(" via ")) {
                paymentMethod = description.substring(description.indexOf(" via ") + 5);
            }
            
            // Use values to aid debugging
            System.out.println("Topup receipt for " + currentUser + ": amount=" + String.format("%.2f", amount) + ", method=" + paymentMethod);
            
            // Set the receipt fields for topup
            AmountPaid.setText(String.format("Php %.2f", amount));
            Fee.setText("Php 0.00"); // No fee for topups
          //  price.setText(String.format("PHP %.2f", amount)); // Main price display
            
            // For topup receipt, we'll show the account number as username or user ID
            AccNumber.setText(currentUser);
            
            // Use timestamp from transaction if available, otherwise current time
            String dateTime;
            if (timestamp != null) {
                dateTime = new java.text.SimpleDateFormat("dd MMMM yyyy hh:mm a").format(timestamp);
            } else {
                dateTime = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd MMMM yyyy hh:mm a"));
            }
            TimeDate.setText(dateTime);
            
            // Set reference number
           // RefNumber.setText(refNumber);
            
        } catch (Throwable t) {
            System.err.println("Error populating topup receipt amounts: " + t.getMessage());
            t.printStackTrace();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        share = new javax.swing.JButton();
        Exit = new javax.swing.JButton();
        Download = new javax.swing.JButton();
        TimeDate = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        AmountPaid = new javax.swing.JLabel();
        Fee = new javax.swing.JLabel();
        AccNumber = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(370, 750));
        setPreferredSize(new java.awt.Dimension(370, 750));
        setLayout(null);

        share.setBorder(null);
        share.setBorderPainted(false);
        share.setContentAreaFilled(false);
        add(share);
        share.setBounds(180, 680, 130, 40);

        Exit.setBorder(null);
        Exit.setBorderPainted(false);
        Exit.setContentAreaFilled(false);
        Exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExitActionPerformed(evt);
            }
        });
        add(Exit);
        Exit.setBounds(290, 50, 50, 40);

        Download.setBorder(null);
        Download.setBorderPainted(false);
        Download.setContentAreaFilled(false);
        add(Download);
        Download.setBounds(30, 683, 120, 30);

        TimeDate.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        TimeDate.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TimeDate.setText("29 August 2025 05:31:02 PM");
        add(TimeDate);
        TimeDate.setBounds(200, 300, 190, 20);

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setText("<html><div style='text-align:leading;'>Amount Paid: <br>Fee: <br>Account Number:  </div></html>");
        add(jLabel6);
        jLabel6.setBounds(100, 230, 110, 70);

        AmountPaid.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        AmountPaid.setText("Php 650.00");
        add(AmountPaid);
        AmountPaid.setBounds(230, 240, 100, 20);

        Fee.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Fee.setText("Php 7.00");
        add(Fee);
        Fee.setBounds(230, 260, 140, 20);

        AccNumber.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        AccNumber.setText("337293727");
        add(AccNumber);
        AccNumber.setBounds(230, 280, 90, 20);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/top up.png"))); // NOI18N
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
                phoneFrame.switchPanel(new cephra.Phone.RewardsWallet.Wallet());
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
    private javax.swing.JLabel TimeDate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JButton share;
    // End of variables declaration//GEN-END:variables
}
