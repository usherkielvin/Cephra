package cephra.Phone;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.SwingUtilities;

public class PayPop extends javax.swing.JPanel {

    public PayPop() {
        initComponents();
        setPreferredSize(new java.awt.Dimension(350, 750));
        setSize(350, 750);
        setupLabelPosition(); // Set label position
        makeDraggable();
        
        // Update labels with actual ticket data after components are initialized
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                updateTextWithAmount();
            }
        });
        
        String username = cephra.CephraDB.getCurrentUsername();
        
        if (LoggedName != null && !username.isEmpty()) {
            LoggedName.setText(username);
        }
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
                    java.awt.Window window = SwingUtilities.getWindowAncestor(PayPop.this);
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

    private void updateTextWithAmount() {
        try {
            String ticket = cephra.Phone.QueueFlow.getCurrentTicketId();
            if (ticket == null || ticket.isEmpty()) return;
            
            // Use centralized calculation from QueueBridge for consistency
            double amount = cephra.Admin.QueueBridge.computeAmountDue(ticket);
            double commission = cephra.Admin.QueueBridge.computePlatformCommission(amount);
            double net = cephra.Admin.QueueBridge.computeNetToStation(amount);
            
            // Get kWh calculation from QueueBridge for consistency
            cephra.Admin.QueueBridge.BatteryInfo batteryInfo = cephra.Admin.QueueBridge.getTicketBatteryInfo(ticket);
            double usedKWh = 0.0;
            if (batteryInfo != null) {
                int start = batteryInfo.initialPercent;
                double cap = batteryInfo.capacityKWh;
                usedKWh = (100.0 - start) / 100.0 * cap;
            }
            
            // Update all labels with actual ticket data
            if (ticketNo != null) {
                ticketNo.setText(ticket);
            }
            if (ChargingDue != null) {
                ChargingDue.setText(String.format("₱%.2f", amount));
            }
            if (kWh != null) {
                kWh.setText(String.format("%.1f kWh", usedKWh));
            }
            if (TotalBill != null) {
                TotalBill.setText(String.format("₱%.2f", amount));
            }
            if (name != null) {
                String username = cephra.CephraDB.getCurrentUsername();
                if (username != null && !username.isEmpty()) {
                    name.setText(username);
                }
            }
            
            String summary = String.format(
                "Charging Complete – Please Pay ₱%.2f\nEnergy: %.1f kWh @ ₱%.2f/kWh\nMin fee: ₱%.2f applies if higher\nCommission: ₱%.2f (18%%)\nNet to station: ₱%.2f",
                amount,
                usedKWh,
                cephra.Admin.QueueBridge.getRatePerKWh(),
                cephra.Admin.QueueBridge.getMinimumFee(),
                commission,
                net
            );
            System.out.println(summary);
        } catch (Throwable t) {
            System.err.println("Error updating PayPop labels: " + t.getMessage());
        }
    }
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        payonline = new javax.swing.JButton();
        Cash = new javax.swing.JButton();
        charge = new javax.swing.JButton();
        profilebutton = new javax.swing.JButton();
        historybutton = new javax.swing.JButton();
        linkbutton = new javax.swing.JButton();
        homebutton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setLayout(null);

        payonline.setBorder(null);
        payonline.setBorderPainted(false);
        payonline.setContentAreaFilled(false);
        payonline.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                payonlineActionPerformed(evt);
            }
        });
        add(payonline);
        payonline.setBounds(170, 450, 130, 40);

        Cash.setBorder(null);
        Cash.setBorderPainted(false);
        Cash.setContentAreaFilled(false);
        Cash.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CashActionPerformed(evt);
            }
        });
        add(Cash);
        Cash.setBounds(45, 453, 120, 40);

        charge.setBorder(null);
        charge.setBorderPainted(false);
        charge.setContentAreaFilled(false);
        charge.setFocusPainted(false);
        charge.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chargeActionPerformed(evt);
            }
        });
        add(charge);
        charge.setBounds(30, 680, 50, 40);

        profilebutton.setBorder(null);
        profilebutton.setBorderPainted(false);
        profilebutton.setContentAreaFilled(false);
        profilebutton.setFocusPainted(false);
        profilebutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                profilebuttonActionPerformed(evt);
            }
        });
        add(profilebutton);
        profilebutton.setBounds(260, 670, 50, 50);

        historybutton.setBorder(null);
        historybutton.setBorderPainted(false);
        historybutton.setContentAreaFilled(false);
        historybutton.setFocusPainted(false);
        historybutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                historybuttonActionPerformed(evt);
            }
        });
        add(historybutton);
        historybutton.setBounds(200, 680, 50, 40);

        linkbutton.setBorder(null);
        linkbutton.setBorderPainted(false);
        linkbutton.setContentAreaFilled(false);
        linkbutton.setFocusPainted(false);
        linkbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                linkbuttonActionPerformed(evt);
            }
        });
        add(linkbutton);
        linkbutton.setBounds(90, 680, 50, 40);

        homebutton2.setBorder(null);
        homebutton2.setBorderPainted(false);
        homebutton2.setContentAreaFilled(false);
        homebutton2.setFocusPainted(false);
        homebutton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                homebutton2ActionPerformed(evt);
            }
        });
        add(homebutton2);
        homebutton2.setBounds(150, 680, 40, 40);

        LoggedName = new javax.swing.JLabel();
        LoggedName.setText("Name");
        add(LoggedName);
        LoggedName.setBounds(120, 120, 50, 30);

        ticketNo = new javax.swing.JLabel();
        ticketNo.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        ticketNo.setText(""); // Will be populated with actual ticket number
        add(ticketNo);
        ticketNo.setBounds(220, 310, 60, 17);

        ChargingDue = new javax.swing.JLabel();
        ChargingDue.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        ChargingDue.setText(""); // Will be populated with actual amount
        add(ChargingDue);
        ChargingDue.setBounds(220, 335, 60, 17);

        kWh = new javax.swing.JLabel();
        kWh.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        kWh.setText(""); // Will be populated with actual kWh
        add(kWh);
        kWh.setBounds(220, 360, 60, 17);

        TotalBill = new javax.swing.JLabel();
        TotalBill.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        TotalBill.setText(""); // Will be populated with actual total
        add(TotalBill);
        TotalBill.setBounds(217, 405, 60, 17);

        name = new javax.swing.JLabel();
        name.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        name.setText(""); // Will be populated with actual username
        add(name);
        name.setBounds(68, 73, 190, 30);

        jLabel3 = new javax.swing.JLabel();
        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        jLabel3.setText(",");
        add(jLabel3);
        jLabel3.setBounds(55, 75, 20, 30);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Photos/paypop.png"))); // NOI18N
        add(jLabel1);
        jLabel1.setBounds(-15, 0, 398, 750);
    }// </editor-fold>//GEN-END:initComponents

    private void chargeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chargeActionPerformed
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frame.Phone) {
                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                        phoneFrame.switchPanel(new cephra.Phone.serviceoffered());
                        break;
                    }
                }
            }
        });
    }//GEN-LAST:event_chargeActionPerformed

    private void linkbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_linkbuttonActionPerformed
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frame.Phone) {
                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                        phoneFrame.switchPanel(new cephra.Phone.link());
                        break;
                    }
                }
            }
        });
    }//GEN-LAST:event_linkbuttonActionPerformed

    private void homebutton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_homebutton2ActionPerformed
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
    }//GEN-LAST:event_homebutton2ActionPerformed

    private void historybuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_historybuttonActionPerformed
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frame.Phone) {
                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                        phoneFrame.switchPanel(new cephra.Phone.phonehistory());
                        break;
                    }
                }
            }
        });
    }//GEN-LAST:event_historybuttonActionPerformed

    private void profilebuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_profilebuttonActionPerformed
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frame.Phone) {
                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                        phoneFrame.switchPanel(new cephra.Phone.Profile());
                        break;
                    }
                }
            }
        });
    }//GEN-LAST:event_profilebuttonActionPerformed

    private void CashActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CashActionPerformed
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
    }//GEN-LAST:event_CashActionPerformed

    private void payonlineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_payonlineActionPerformed
       SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    // Check if there's an active ticket
                    if (!cephra.Phone.QueueFlow.hasActiveTicket()) {
                        System.out.println("No active ticket found for payment");
                        
                        // Show error message to user
                        javax.swing.SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                javax.swing.JOptionPane.showMessageDialog(
                                    PayPop.this,
                                    "No active ticket found for payment.\nPlease get a ticket first.",
                                    "Payment Error",
                                    javax.swing.JOptionPane.ERROR_MESSAGE
                                );
                            }
                        });
                        return;
                    }
                    
                    // Get current ticket ID
                    String currentTicket = cephra.Phone.QueueFlow.getCurrentTicketId();

                    // Validate that the ticket is Complete and Payment is Pending in admin table
                    boolean validForPayment = false;
                    try {
                        javax.swing.table.DefaultTableModel m = (javax.swing.table.DefaultTableModel) cephra.Admin.QueueBridge.class.getDeclaredField("model").get(null);
                        if (m != null) {
                            for (int i = 0; i < m.getRowCount(); i++) {
                                Object v = m.getValueAt(i, 0);
                                if (currentTicket.equals(String.valueOf(v))) {
                                    String status = String.valueOf(m.getValueAt(i, 3));
                                    String payment = String.valueOf(m.getValueAt(i, 4));
                                    validForPayment = "Complete".equalsIgnoreCase(status) && "Pending".equalsIgnoreCase(payment);
                                    break;
                                }
                            }
                        }
                    } catch (Throwable t) {
                        // if reflection fails, skip validation
                        validForPayment = true;
                    }
                    if (!validForPayment) {
                        javax.swing.JOptionPane.showMessageDialog(
                            PayPop.this,
                            "Ticket is not ready for payment.",
                            "Payment Error",
                            javax.swing.JOptionPane.ERROR_MESSAGE
                        );
                        return;
                    }
                    // Update payment status in admin queue
                    cephra.Admin.QueueBridge.markPaymentPaid(currentTicket);
                    // Also update the local queue flow entries
                    cephra.Phone.QueueFlow.updatePaymentStatus(currentTicket, "Paid");
                    System.out.println("Payment marked as paid for ticket: " + currentTicket);
                } catch (Exception e) {
                    System.err.println("Error updating payment status: " + e.getMessage());
                    e.printStackTrace();
                }
                
                // Navigate to receipt regardless of payment update success
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frame.Phone) {
                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                        phoneFrame.switchPanel(new cephra.Phone.Reciept());
                        break;
                    }
                }
            }
        });
    }//GEN-LAST:event_payonlineActionPerformed




    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Cash;
    private javax.swing.JButton charge;
    private javax.swing.JButton historybutton;
    private javax.swing.JButton homebutton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel kWh;
    private javax.swing.JButton linkbutton;
    private javax.swing.JLabel name;
    private javax.swing.JButton payonline;
    private javax.swing.JButton profilebutton;
    private javax.swing.JLabel ticketNo;
    private javax.swing.JLabel LoggedName;
    private javax.swing.JLabel ChargingDue;
    private javax.swing.JLabel TotalBill;
    // End of variables declaration//GEN-END:variables
}
