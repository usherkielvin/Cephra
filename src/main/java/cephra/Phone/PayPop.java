package cephra.Phone;

import javax.swing.SwingUtilities;
import javax.swing.JLayeredPane;

public class PayPop extends javax.swing.JPanel {
    
    // Static state management to prevent multiple instances
    private static PayPop currentInstance = null;
    private static String currentTicketId = null;
    private static boolean isShowing = false;
    
    // Static method to check if PayPop is currently showing for a ticket
    public static boolean isShowingForTicket(String ticketId) {
        return isShowing && ticketId != null && ticketId.equals(currentTicketId);
    }
    
    // Static method to check if PayPop CAN be shown (validation only, doesn't show)
    public static boolean canShowPayPop(String ticketId, String customerUsername) {
        System.out.println("=== PayPop.canShowPayPop() validation ===");
        System.out.println("- Ticket ID: '" + ticketId + "'");
        System.out.println("- Customer Username: '" + customerUsername + "'");
        System.out.println("- Currently showing: " + isShowing);
        
        // Allow reappearing - if already showing, hide first then show again
        if (isShowing) {
            System.out.println("PayPop: Already showing, will hide and reshow");
            hidePayPop(); // Hide current instance to allow reappearing
        }
        
        // Strict validation: Check if anyone is logged in first
        if (!cephra.CephraDB.isUserLoggedIn()) {
            System.out.println("PayPop: No user is currently logged in");
            return false;
        }
        
        // Get current logged-in user
        String currentUser = cephra.CephraDB.getCurrentUsername();
        if (currentUser == null || currentUser.trim().isEmpty()) {
            System.out.println("PayPop: Current user is null or empty");
            return false;
        }
        
        // Validate current user matches ticket owner exactly
        if (!currentUser.trim().equals(customerUsername.trim())) {
            System.out.println("PayPop: User mismatch - current user ('" + currentUser + "') does not match ticket owner ('" + customerUsername + "')");
            return false;
        }
        
        System.out.println("PayPop: Validation passed - PayPop CAN be shown for user '" + currentUser + "' and ticket '" + ticketId + "'");
        return true;
    }
    
    // Static method to show PayPop with validation
    public static boolean showPayPop(String ticketId, String customerUsername) {
        System.out.println("=== PayPop.showPayPop() called ===");
        
        // Use the validation method
        if (!canShowPayPop(ticketId, customerUsername)) {
            return false;
        }
        
        // Find Phone frame and show centered PayPop
        java.awt.Window[] windows = java.awt.Window.getWindows();
        for (java.awt.Window window : windows) {
            if (window instanceof cephra.Frame.Phone) {
                cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                showCenteredPayPop(phoneFrame, ticketId);
                return true;
            }
        }
        return false;
    }
    
    // Static method to show PayPop centered on Phone frame
    private static void showCenteredPayPop(cephra.Frame.Phone phoneFrame, String ticketId) {
        SwingUtilities.invokeLater(() -> {
            currentInstance = new PayPop();
            currentTicketId = ticketId;
            isShowing = true;
            
            // Center the PayPop on the phone frame (350x750)
            int popupWidth = 320;
            int popupHeight = 280;
            int x = (350 - popupWidth) / 2;  // (350-320)/2 = 15
            int y = (750 - popupHeight) / 2; // (750-280)/2 = 235
            
            currentInstance.setBounds(x, y, popupWidth, popupHeight);
            
            // Add to layered pane so it appears on top of current panel
            JLayeredPane layeredPane = phoneFrame.getRootPane().getLayeredPane();
            layeredPane.add(currentInstance, JLayeredPane.MODAL_LAYER);
            layeredPane.moveToFront(currentInstance);
            
            currentInstance.setVisible(true);
            phoneFrame.repaint();
        });
    }
    
    // Static method to hide PayPop
    public static void hidePayPop() {
        if (currentInstance != null && isShowing) {
            SwingUtilities.invokeLater(() -> {
                if (currentInstance.getParent() != null) {
                    currentInstance.getParent().remove(currentInstance);
                }
                currentInstance = null;
                currentTicketId = null;
                isShowing = false;
                
                // Repaint the phone frame
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frame.Phone) {
                        window.repaint();
                        break;
                    }
                }
            });
        }
    }

    public PayPop() {
        initComponents();
        setPreferredSize(new java.awt.Dimension(320, 280));
        setSize(320, 280);
        setupLabelPosition(); // Set label position
        setupCloseButton(); // Add close functionality
        
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
        if (ticketNo != null) {
            ticketNo.setBounds(-15, 0, 398, 750);
        }
    }
    // Setup close button functionality (clicking outside or ESC)
    private void setupCloseButton() {
        // Add key listener for ESC to close
        setFocusable(true);
        addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ESCAPE) {
                    hidePayPop();
                }
            }
        });
        
        // Request focus so key events work
        SwingUtilities.invokeLater(() -> {
            requestFocusInWindow();
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

        ChargingDue = new javax.swing.JLabel();
        ticketNo = new javax.swing.JLabel();
        kWh = new javax.swing.JLabel();
        TotalBill = new javax.swing.JLabel();
        Cash = new javax.swing.JButton();
        payonline = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        name = new javax.swing.JLabel();
        LoggedName = new javax.swing.JLabel();

        setLayout(null);

        ChargingDue.setText("jLabel1");
        add(ChargingDue);
        ChargingDue.setBounds(160, 100, 90, 16);

        ticketNo.setText("jLabel1");
        add(ticketNo);
        ticketNo.setBounds(160, 80, 90, 16);

        kWh.setText("jLabel1");
        add(kWh);
        kWh.setBounds(160, 120, 80, 16);

        TotalBill.setText("jLabel1");
        add(TotalBill);
        TotalBill.setBounds(70, 170, 90, 16);

        Cash.setBorder(null);
        Cash.setContentAreaFilled(false);
        Cash.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CashActionPerformed(evt);
            }
        });
        add(Cash);
        Cash.setBounds(10, 210, 110, 50);

        payonline.setBorder(null);
        payonline.setBorderPainted(false);
        payonline.setContentAreaFilled(false);
        payonline.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                payonlineActionPerformed(evt);
            }
        });
        add(payonline);
        payonline.setBounds(140, 210, 110, 50);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Photos/Rilpipop.png"))); // NOI18N
        jLabel1.setText("jLabel1");
        add(jLabel1);
        jLabel1.setBounds(0, 0, 260, 280);

        jPanel1.setBackground(new java.awt.Color(255, 0, 51));
        jPanel1.setOpaque(false);

        name.setText("jLabel1");

        LoggedName.setText("jLabel1");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(85, 85, 85)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(LoggedName)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(151, 151, 151)
                        .addComponent(name)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(76, 76, 76)
                .addComponent(name)
                .addGap(46, 46, 46)
                .addComponent(LoggedName)
                .addContainerGap(116, Short.MAX_VALUE))
        );

        add(jPanel1);
        jPanel1.setBounds(0, 0, 260, 270);
    }// </editor-fold>//GEN-END:initComponents

    private void CashActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CashActionPerformed
        // Validate user is still logged in before processing payment
        if (!cephra.CephraDB.isUserLoggedIn()) {
            System.err.println("Cash payment blocked: No user is logged in");
            hidePayPop();
            return;
        }
        
        String currentUser = cephra.CephraDB.getCurrentUsername();
        System.out.println("Processing cash payment for user: " + currentUser);
        
        // Hide the PayPop first
        hidePayPop();
        
        // Then navigate to home
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
        // Validate user is still logged in before processing payment
        if (!cephra.CephraDB.isUserLoggedIn()) {
            System.err.println("Online payment blocked: No user is logged in");
            hidePayPop();
            return;
        }
        
        String currentUser = cephra.CephraDB.getCurrentUsername();
        System.out.println("Processing online payment for user: " + currentUser);
        
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
                    // Update payment status in admin queue with GCash payment method
                    cephra.Admin.QueueBridge.markPaymentPaidOnline(currentTicket);
                    // Also update the local queue flow entries
                    cephra.Phone.QueueFlow.updatePaymentStatus(currentTicket, "Paid");
                    System.out.println("GCash payment marked as paid for ticket: " + currentTicket);
                } catch (Exception e) {
                    System.err.println("Error updating payment status: " + e.getMessage());
                    e.printStackTrace();
                }
                
                // Hide PayPop first
                hidePayPop();
                
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
    private javax.swing.JLabel ChargingDue;
    private javax.swing.JLabel LoggedName;
    private javax.swing.JLabel TotalBill;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel kWh;
    private javax.swing.JLabel name;
    private javax.swing.JButton payonline;
    private javax.swing.JLabel ticketNo;
    // End of variables declaration//GEN-END:variables
}
