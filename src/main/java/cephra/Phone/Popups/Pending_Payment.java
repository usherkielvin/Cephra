package cephra.Phone.Popups;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Pending_Payment extends javax.swing.JPanel {
    
    // Static state management to prevent multiple instances
    private static Pending_Payment currentInstance = null;
    private static String currentTicketId = null;
    private static boolean isShowing = false;
    
    // Pending_Payment persistence state for top-up flow
    private static boolean isPendingTopUpReturn = false;
    private static String pendingTicketId = null;
    private static String pendingCustomerUsername = null;
    private static double lastPaymentAmount = 0.0;
    
    // Popup dimensions (centered in phone frame)
    private static final int POPUP_WIDTH = 270;
    private static final int POPUP_HEIGHT = 280;
    private static final int PHONE_WIDTH = 350; // fallback if frame size not yet realized
    private static final int PHONE_HEIGHT = 750; // fallback if frame size not yet realized
    
    /**
     * Checks if Pending_Payment is currently showing for a specific ticket
     * @param ticketId the ticket ID to check
     * @return true if Pending_Payment is showing for this ticket
     */
    public static boolean isShowingForTicket(String ticketId) {
        return isShowing && ticketId != null && ticketId.equals(currentTicketId);
    }
    
    /**
     * Checks if there's a Pending_Payment pending to be restored after top-up
     * @return true if Pending_Payment should be restored
     */
    public static boolean hasPendingPayPop() {
        return isPendingTopUpReturn && pendingTicketId != null && pendingCustomerUsername != null;
    }
    
    /**
     * Gets the current ticket ID from Pending_Payment
     * @return the current ticket ID, or null if not available
     */
    public static String getCurrentTicketId() {
        return currentTicketId;
    }
    
    /**
     * Gets the payment amount from Pending_Payment's last payment
     * @return the payment amount, or 0.0 if not available
     */
    public static double getPaymentAmount() {
        return lastPaymentAmount;
    }
    
    /**
     * Restores Pending_Payment after returning from top-up if there was a pending payment
     * @return true if Pending_Payment was restored successfully
     */
    public static boolean restorePayPopAfterTopUp() {
        if (!hasPendingPayPop()) {
            return false;
        }
        
        
        // Store the pending values
        String ticketId = pendingTicketId;
        String username = pendingCustomerUsername;
        
        // Clear the pending state
        clearPendingState();
        
        // Show the Pending_Payment again
        return showPayPop(ticketId, username);
    }
    
    /**
     * Clears the pending Pending_Payment state
     */
    private static void clearPendingState() {
        isPendingTopUpReturn = false;
        pendingTicketId = null;
        pendingCustomerUsername = null;
    }
    
    /**
     * Validates if Pending_Payment can be shown for the given ticket and user
     * @param ticketId the ticket ID
     * @param customerUsername the customer username
     * @return true if Pending_Payment can be shown
     */
    public static boolean canShowPayPop(String ticketId, String customerUsername) {
        
        // Allow reappearing - if already showing, hide first then show again
        if (isShowing) {
            hidePayPop();
        }
        
        // Validate user is logged in
        if (!cephra.Database.CephraDB.isUserLoggedIn()) {
            return false;
        }
        
        // Get and validate current user
        String currentUser = cephra.Database.CephraDB.getCurrentUsername();
        if (currentUser == null || currentUser.trim().isEmpty()) {
            return false;
        }
        
        // Validate user matches ticket owner
        if (!currentUser.trim().equals(customerUsername.trim())) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Shows Pending_Payment with validation
     * @param ticketId the ticket ID
     * @param customerUsername the customer username
     * @return true if Pending_Payment was shown successfully
     */
    public static boolean showPayPop(String ticketId, String customerUsername) {
        
        if (!canShowPayPop(ticketId, customerUsername)) {
            return false;
        }
        
        // Find Phone frame and show centered Pending_Payment
        Window[] windows = Window.getWindows();
        for (Window window : windows) {
            if (window instanceof cephra.Frame.Phone) {
                cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                showCenteredPayPop(phoneFrame, ticketId);
                return true;
            }
        }
        return false;
    }
    
    /**
     * Shows Pending_Payment centered on the Phone frame
     * @param phoneFrame the Phone frame to center on
     * @param ticketId the ticket ID
     */
    private static void showCenteredPayPop(cephra.Frame.Phone phoneFrame, String ticketId) {
        SwingUtilities.invokeLater(() -> {
            currentInstance = new Pending_Payment();
            currentTicketId = ticketId;
            isShowing = true;
            // Push ticket id to UI immediately (fallback to admin queue model if needed)
            try {
                String resolved = ticketId;
                if (resolved == null || resolved.trim().isEmpty()) {
                    String currentUser = cephra.Database.CephraDB.getCurrentUsername();
                    resolved = currentInstance.findLatestTicketForUserFromAdminModel(currentUser);
                }
                currentInstance.setTicketOnUi(resolved);
            } catch (Exception ignore) {}
            
            // Determine phone content size (fallback to constants if not realized yet)
            int containerW = phoneFrame.getContentPane() != null ? phoneFrame.getContentPane().getWidth() : 0;
            int containerH = phoneFrame.getContentPane() != null ? phoneFrame.getContentPane().getHeight() : 0;
            if (containerW <= 0) containerW = PHONE_WIDTH;
            if (containerH <= 0) containerH = PHONE_HEIGHT;
            
            // Center the Pending_Payment on the phone frame
            int x = (containerW - POPUP_WIDTH) / 2;
            int y = (containerH - POPUP_HEIGHT) / 2;
            
            currentInstance.setBounds(x, y, POPUP_WIDTH, POPUP_HEIGHT);
            
            // Add to layered pane so it appears on top of current panel
            JLayeredPane layeredPane = phoneFrame.getRootPane().getLayeredPane();
            layeredPane.add(currentInstance, JLayeredPane.MODAL_LAYER);
            layeredPane.moveToFront(currentInstance);
            
            currentInstance.setVisible(true);
            phoneFrame.repaint();
        });
    }
    
    /**
     * Hides the Pending_Payment and cleans up resources
     */
    public static void hidePayPop() {
    if (currentInstance != null && isShowing) {
        // Capture a local reference to avoid race conditions
        Pending_Payment instance = currentInstance;

        SwingUtilities.invokeLater(() -> {
            if (instance.getParent() != null) {
                instance.getParent().remove(instance);
            }
            currentInstance = null;
            currentTicketId = null;
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


    // Sets the ticket number on the UI label immediately (safe call)
    private void setTicketOnUi(String ticketId) {
        try {
            if (TICKETNUMBER != null && ticketId != null && !ticketId.trim().isEmpty()) {
                TICKETNUMBER.setText(ticketId);
                TICKETNUMBER.repaint();
            }
        } catch (Exception ignore) {}
    }

    // ensureTicketLabelVisible: removed; form manages visual properties

    // Read the latest ticket for the current user directly from Admin Queue table model
    private String findLatestTicketForUserFromAdminModel(String username) {
        try {
            if (username == null || username.trim().isEmpty()) return null;
            javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel)
                cephra.Admin.Utilities.QueueBridge.class.getDeclaredField("model").get(null);
            if (model == null) return null;

            final int colCount = model.getColumnCount();
            int ticketCol = 0; // usually 0
            int customerCol = Math.min(1, colCount - 1);
            int statusCol = Math.min(3, colCount - 1);
            int paymentCol = Math.min(4, colCount - 1);

            String fallback = null;
            for (int i = model.getRowCount() - 1; i >= 0; i--) { // scan latest first
                String customer = String.valueOf(model.getValueAt(i, customerCol));
                if (customer != null && username.equalsIgnoreCase(customer.trim())) {
                    String t = String.valueOf(model.getValueAt(i, ticketCol));
                    String status = String.valueOf(model.getValueAt(i, statusCol));
                    String payment = String.valueOf(model.getValueAt(i, paymentCol));
                    if ("Pending".equalsIgnoreCase(payment) || "Complete".equalsIgnoreCase(status)) {
                        return t;
                    }
                    if (fallback == null) fallback = t;
                }
            }
            return fallback;
        } catch (Throwable ignore) {
            return null;
        }
    }

    /**
     * Constructor for PayPop
     */
    public Pending_Payment() {
        initComponents();
        initializePayPop();
    }
    
    /**
     * Initializes Pending_Payment components and data
     */
    private void initializePayPop() {
        // Match popup panel to background image and remove excess white by making it transparent
        setPreferredSize(new Dimension(POPUP_WIDTH, POPUP_HEIGHT));
        setSize(POPUP_WIDTH, POPUP_HEIGHT);
        setOpaque(false);
        setupCloseButton();
        
        // Update labels with actual ticket data after components are initialized
        SwingUtilities.invokeLater(this::updateTextWithAmount);
        
        // Set username if available (optional display label removed)
    }

    // NetBeans form manages background order by default

    // NetBeans form manages component Z-order
    /**
     * Sets up label position to prevent NetBeans from changing it
     * CUSTOM CODE - DO NOT REMOVE - NetBeans will regenerate form code but this method should be preserved
     */
    // NetBeans form manages label positions
    /**
     * Sets up close button functionality (ESC key)
     */
    private void setupCloseButton() {
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    hidePayPop();
                }
            }
        });
        
        // Request focus so key events work
        SwingUtilities.invokeLater(this::requestFocusInWindow);
    }

    /**
     * Updates Pending_Payment labels with current ticket data and amounts
     */
    private void updateTextWithAmount() {
        try {
            // Resolve ticket strictly from Admin Queue table if not provided
            String ticket = currentTicketId;
            if (ticket == null || ticket.isEmpty()) {
                String currentUser = cephra.Database.CephraDB.getCurrentUsername();
                ticket = findLatestTicketForUserFromAdminModel(currentUser);
            }
            if (ticket == null || ticket.isEmpty()) {
                ticket = cephra.Phone.Utilities.QueueFlow.getCurrentTicketId();
            }
            if (ticket == null || ticket.isEmpty()) {
                System.err.println("PayPop: No current ticket found");
                return;
            }
            
            // Calculate amounts using centralized QueueBridge methods
            double amount = cephra.Admin.Utilities.QueueBridge.computeAmountDue(ticket);
            
            // Calculate energy usage
            double usedKWh = calculateEnergyUsage(ticket);
            
            // Update UI labels
            updateLabels(ticket, amount, usedKWh);
            
        } catch (Exception e) {
            System.err.println("Error updating PayPop labels: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Calculates energy usage for the ticket
     * @param ticket the ticket ID
     * @return energy usage in kWh
     */
    private double calculateEnergyUsage(String ticket) {
        cephra.Admin.Utilities.QueueBridge.BatteryInfo batteryInfo = cephra.Admin.Utilities.QueueBridge.getTicketBatteryInfo(ticket);
        if (batteryInfo != null) {
            int start = batteryInfo.initialPercent;
            double cap = batteryInfo.capacityKWh;
            return (100.0 - start) / 100.0 * cap;
        }
        return 0.0;
    }
    
    /**
     * Updates all UI labels with ticket data
     * @param ticket the ticket ID
     * @param amount the total amount
     * @param usedKWh the energy usage
     */
    private void updateLabels(String ticket, double amount, double usedKWh) {
        if (TICKETNUMBER != null) {
            TICKETNUMBER.setText(ticket);
            TICKETNUMBER.repaint();
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
    }
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        TICKETNUMBER = new javax.swing.JLabel();
        ChargingDue = new javax.swing.JLabel();
        kWh = new javax.swing.JLabel();
        TotalBill = new javax.swing.JLabel();
        Cash = new javax.swing.JButton();
        payonline = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setOpaque(false);
        setLayout(null);

        TICKETNUMBER.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        add(TICKETNUMBER);
        TICKETNUMBER.setBounds(150, 75, 90, 20);

        ChargingDue.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        add(ChargingDue);
        ChargingDue.setBounds(150, 100, 90, 20);

        kWh.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        add(kWh);
        kWh.setBounds(150, 120, 80, 20);

        TotalBill.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        add(TotalBill);
        TotalBill.setBounds(150, 165, 90, 20);

        Cash.setBorder(null);
        Cash.setContentAreaFilled(false);
        Cash.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CashActionPerformed(evt);
            }
        });
        add(Cash);
        Cash.setBounds(10, 210, 120, 50);

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

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/CASH.png"))); // NOI18N
        add(jLabel1);
        jLabel1.setBounds(0, 0, 280, 280);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Handles cash payment action
     * @param evt the action event
     */
    private void CashActionPerformed(ActionEvent evt) {
        if (!validateUserLoggedIn()) {
            return;
        }
        
        try {
            // Get current ticket ID
            String currentTicket = currentTicketId;
            if (currentTicket == null || currentTicket.isEmpty()) {
                currentTicket = cephra.Phone.Utilities.QueueFlow.getCurrentTicketId();
            }
            
            if (currentTicket != null && !currentTicket.isEmpty()) {
                // Set payment method to Cash in the database
                boolean success = cephra.Database.CephraDB.updateQueueTicketPaymentMethod(currentTicket, "Cash");
                
                if (success) {
                    // Refresh admin table to show the updated payment status
                    try {
                        cephra.Admin.Utilities.QueueBridge.reloadFromDatabase();
                    } catch (Exception e) {
                        System.err.println("Error refreshing admin table after cash payment: " + e.getMessage());
                    }
                }
                
                // For cash payments, do NOT mark as paid automatically
                // Admin will manually mark as paid after receiving cash
            }
            
            // Clear any pending Pending_Payment state since payment is completed
            clearPendingState();
            
            // Hide Pending_Payment and navigate to Home
            hidePayPop();
            navigateToHome();
            
        } catch (Exception e) {
            System.err.println("Error processing cash payment: " + e.getMessage());
            e.printStackTrace();
            showErrorMessage("Failed to process cash payment.\nError: " + e.getMessage() + "\nPlease try again.");
        }
    }

    /**
     * Handles online payment action
     * @param evt the action event
     */
    private void payonlineActionPerformed(ActionEvent evt) {
        if (!validateUserLoggedIn()) {
            return;
        }
        
        SwingUtilities.invokeLater(() -> {
            boolean paymentSuccess = false;
            try {
                paymentSuccess = processOnlinePayment();
                // After successful wallet payment, ensure admin queue marks as paid and removes the ticket
                if (paymentSuccess) {
                    try {
                        String currentTicket = cephra.Phone.Utilities.QueueFlow.getCurrentTicketId();
                        if (currentTicket != null && !currentTicket.isEmpty()) {
                            // Set payment method to Online in the database
                            cephra.Database.CephraDB.updateQueueTicketPaymentMethod(currentTicket, "Online");
                            
                            cephra.Admin.Utilities.QueueBridge.markPaymentPaidOnline(currentTicket);
                        }
                    } catch (Throwable ignore) {}
                }
            } catch (Exception e) {
                handlePaymentError(e);
            } finally {
                hidePayPop();
                // Only navigate to receipt if payment was successful
                if (paymentSuccess) {
                    navigateToReceipt();
                } else {
                    // If payment failed (e.g., insufficient balance), navigate back to home
                    navigateToHome();
                }
            }
        });
    }
    
    /**
     * Validates that user is logged in
     * @return true if user is logged in, false otherwise
     */
    private boolean validateUserLoggedIn() {
        if (!cephra.Database.CephraDB.isUserLoggedIn()) {
            System.err.println("Payment blocked: No user is logged in");
            hidePayPop();
            return false;
        }
        return true;
    }
    
    /**
     * Processes online payment
     * @return true if payment was successful, false otherwise
     * @throws Exception if payment processing fails
     */
    private boolean processOnlinePayment() throws Exception {
        // Check if there's an active ticket
        if (!cephra.Phone.Utilities.QueueFlow.hasActiveTicket()) {
            showErrorMessage("No active ticket found for payment.\nPlease get a ticket first.");
            return false;
        }
        
        String currentTicket = cephra.Phone.Utilities.QueueFlow.getCurrentTicketId();
        
        // Get current user
        String currentUser = cephra.Database.CephraDB.getCurrentUsername();
        if (currentUser == null || currentUser.trim().isEmpty()) {
            showErrorMessage("No user is currently logged in.");
            return false;
        }
        
        // Calculate payment amount
        double paymentAmount = cephra.Admin.Utilities.QueueBridge.computeAmountDue(currentTicket);
        
        // Store payment amount for receipt
        lastPaymentAmount = paymentAmount;
        
        // Check wallet balance first - show top-up dialog if insufficient
        if (!cephra.Database.CephraDB.hasSufficientWalletBalance(currentUser, paymentAmount)) {
            showInsufficientBalanceMessage(paymentAmount);
            return false;
        }
        
        // No need to validate ticket status - Pending_Payment only shows when ticket is completed
        
        // Process wallet payment
        boolean walletPaymentSuccess = cephra.Database.CephraDB.processWalletPayment(currentUser, currentTicket, paymentAmount);
        
        if (!walletPaymentSuccess) {
            showErrorMessage("Failed to process wallet payment.\nPlease try again or check your balance.");
            return false;
        }
        
        // Process payment through existing system - markPaymentPaidOnline already handles everything
        cephra.Admin.Utilities.QueueBridge.markPaymentPaidOnline(currentTicket);
        
        // Clear any pending Pending_Payment state since payment is completed successfully
        clearPendingState();
        
        // Note: markPaymentPaidOnline already handles:
        // - Payment processing and database updates
        // - Charging bay and grid clearing
        // - History addition
        // - UI refresh
        
        // No need to show success message - receipt panel will display the information
        return true;
    }
    
    
    /**
     * Shows error message to user
     * @param message the error message
     */
    private void showErrorMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(
                this,
                message,
                "Payment Error",
                JOptionPane.ERROR_MESSAGE
            );
        });
    }
    
    /**
     * Shows insufficient balance message with option to top-up
     * @param requiredAmount the amount required for payment
     */
    private void showInsufficientBalanceMessage(double requiredAmount) {
        String currentUser = cephra.Database.CephraDB.getCurrentUsername();
        double currentBalance = cephra.Database.CephraDB.getUserWalletBalance(currentUser);
        
        SwingUtilities.invokeLater(() -> {
            String message = String.format(
                "Insufficient wallet balance!\n\n" +
                "Current Balance: ₱%.2f\n" +
                "Required Amount: ₱%.2f\n" +
                "Shortage: ₱%.2f\n\n" +
                "Please top up your wallet to proceed with payment.",
                currentBalance, requiredAmount, (requiredAmount - currentBalance)
            );
            
            int option = JOptionPane.showOptionDialog(
                this,
                message,
                "Insufficient Balance",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,
                new String[]{"Top Up Wallet", "Cancel"},
                "Top Up Wallet"
            );
            
            if (option == 0) { // Top Up Wallet selected
                navigateToTopUp();
            }
        });
    }
    
    
    /**
     * Handles payment errors
     * @param e the exception
     */
    private void handlePaymentError(Exception e) {
        showErrorMessage("Failed to process GCash payment.\nError: " + e.getMessage() + "\nPlease try again or contact support.");
    }
    
    /**
     * Navigates to Home screen
     */
    private void navigateToHome() {
        SwingUtilities.invokeLater(() -> {
            Window[] windows = Window.getWindows();
            for (Window window : windows) {
                if (window instanceof cephra.Frame.Phone) {
                    cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                    phoneFrame.switchPanel(cephra.Phone.Dashboard.Home.getAppropriateHomePanel());
                    break;
                }
            }
        });
    }
    
    /**
     * Navigates to receipt screen
     */
    private void navigateToReceipt() {
        SwingUtilities.invokeLater(() -> {
            Window[] windows = Window.getWindows();
            for (Window window : windows) {
                if (window instanceof cephra.Frame.Phone) {
                    cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                    phoneFrame.switchPanel(new cephra.Phone.RewardsWallet.Reciept());
                    break;
                }
            }
        });
    }
    
    /**
     * Navigates to TopUp screen while preserving Pending_Payment state
     */
    private void navigateToTopUp() {
        SwingUtilities.invokeLater(() -> {
            // Store the current Pending_Payment state before hiding
            String currentUser = cephra.Database.CephraDB.getCurrentUsername();
            if (currentUser != null && currentTicketId != null) {
                isPendingTopUpReturn = true;
                pendingTicketId = currentTicketId;
                pendingCustomerUsername = currentUser;
            }
            
            hidePayPop(); // Hide the current Pending_Payment
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
    




    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Cash;
    private javax.swing.JLabel ChargingDue;
    private javax.swing.JLabel TICKETNUMBER;
    private javax.swing.JLabel TotalBill;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel kWh;
    private javax.swing.JButton payonline;
    // End of variables declaration//GEN-END:variables
}
