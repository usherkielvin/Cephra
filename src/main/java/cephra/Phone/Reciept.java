package cephra.Phone;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.List;
import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;

public class Reciept extends javax.swing.JPanel {
  
    public Reciept() {
        initComponents();
         setPreferredSize(new java.awt.Dimension(350, 750));
        setSize(350, 750);
        setupLabelPosition(); // Set label position
        makeDraggable();
        populateAmounts();
    }
     // CUSTOM CODE - DO NOT REMOVE - NetBeans will regenerate form code but this method should be preserved
    // Setup label position to prevent NetBeans from changing it
    private void setupLabelPosition() {
        if (jLabel1 != null) {
            jLabel1.setBounds(-15, 0, 398, 750);
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
            String ticket = cephra.Phone.QueueFlow.getCurrentTicketId();
            if (ticket == null || ticket.isEmpty()) return;
            
            // Use centralized calculation from QueueBridge for consistency
            double amount = cephra.Admin.QueueBridge.computeAmountDue(ticket);
            
            // Get kWh calculation from QueueBridge for consistency
            cephra.Admin.QueueBridge.BatteryInfo batteryInfo = cephra.Admin.QueueBridge.getTicketBatteryInfo(ticket);
            double usedKWh = 0.0;
            if (batteryInfo != null) {
                int start = batteryInfo.initialPercent;
                double cap = batteryInfo.capacityKWh;
                usedKWh = (100.0 - start) / 100.0 * cap;
            }
            
            // Get reference number from queue bridge first (most up-to-date)
            String refNumber = cephra.Admin.QueueBridge.getTicketRefNumber(ticket);
            
            // If not found in queue, try admin history
            if (refNumber.isEmpty()) {
                try {
                    List<Object[]> adminRecords = cephra.Admin.HistoryBridge.getRecordsForUser(cephra.CephraDB.getCurrentUsername());
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
            
            // Use values to avoid warnings and aid debugging
            System.out.println("Receipt for " + ticket + ": used=" + String.format("%.2f", usedKWh) + "kWh, amount=" + String.format("%.2f", amount));
            
            AmountPaid.setText(String.format("Php %.2f", amount));
            Fee.setText(String.format("Php %.2f/kWh (min ₱%.2f)", cephra.Admin.QueueBridge.getRatePerKWh(), cephra.Admin.QueueBridge.getMinimumFee()));
            price.setText(String.format("PHP %.2f", amount));
            RefNumber.setText(refNumber); // Use admin queue reference number
            NumTicket.setText(ticket); // Use FCH ticket number
            TimeDate.setText(java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd MMMM yyyy hh:mm:ss a")));
        } catch (Throwable t) {
            System.err.println("Error populating receipt amounts: " + t.getMessage());
        }
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

        setLayout(null);

        share.setBorder(null);
        share.setBorderPainted(false);
        share.setContentAreaFilled(false);
        share.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                shareActionPerformed(evt);
            }
        });
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
        Download.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DownloadActionPerformed(evt);
            }
        });
        add(Download);
        Download.setBounds(30, 683, 120, 30);

        price.setFont(new java.awt.Font("Segoe UI", 3, 36)); // NOI18N
        price.setText("PHP 650.00");
        add(price);
        price.setBounds(70, 280, 210, 40);

        RefNumber.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        RefNumber.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        RefNumber.setText("33873585637");
        add(RefNumber);
        RefNumber.setBounds(168, 390, 100, 21);

        TimeDate.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        TimeDate.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TimeDate.setText("29 August 2025 05:31:02 PM");
        add(TimeDate);
        TimeDate.setBounds(70, 410, 190, 20);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel5.setText("Details");
        add(jLabel5);
        jLabel5.setBounds(140, 450, 70, 30);

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setText("<html><div style='text-align:leading;'>Amount Paid: <br>Fee: <br>Account Number:  </div></html>");
        add(jLabel6);
        jLabel6.setBounds(40, 490, 110, 70);

        AmountPaid.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        AmountPaid.setText("Php 650.00");
        add(AmountPaid);
        AmountPaid.setBounds(200, 490, 100, 20);

        Fee.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Fee.setText("Php 7.00");
        add(Fee);
        Fee.setBounds(200, 510, 70, 20);

        AccNumber.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        AccNumber.setText("337293727");
        add(AccNumber);
        AccNumber.setBounds(200, 530, 90, 20);

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Ref. Number: ");
        add(jLabel8);
        jLabel8.setBounds(70, 390, 100, 21);
        jLabel8.getAccessibleContext().setAccessibleName("");

        NumTicket.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        NumTicket.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        NumTicket.setText("FCH008");
        add(NumTicket);
        NumTicket.setBounds(120, 360, 100, 30);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Photos/Receipt.png"))); // NOI18N
        add(jLabel1);
        jLabel1.setBounds(-15, 0, 398, 750);
    }// </editor-fold>//GEN-END:initComponents

    private void ExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExitActionPerformed
       SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frame.Phone) {
                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                        phoneFrame.switchPanel(new cephra.Phone.home());
                        break;
                    }
                }
            }
        });
    }//GEN-LAST:event_ExitActionPerformed

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
