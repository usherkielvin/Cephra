package cephra.Phone;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AlreadyFull extends javax.swing.JPanel {
    
    // Static state management to prevent multiple instances
    private static AlreadyFull currentInstance = null;
    private static String currentTicketId = null;
    private static boolean isShowing = false;
    
    // Popup dimensions (centered in phone frame)
    private static final int POPUP_WIDTH = 280;
    private static final int POPUP_HEIGHT = 140;
    private static final int PHONE_WIDTH = 350; // fallback if frame size not yet realized
    private static final int PHONE_HEIGHT = 750; // fallback if frame size not yet realized
    
    /**
     * Checks if PayPop is currently showing for a specific ticket
     * @param ticketId the ticket ID to check
     * @return true if PayPop is showing for this ticket
     */
    public static boolean isShowingForTicket(String ticketId) {
        return isShowing && ticketId != null && ticketId.equals(currentTicketId);
    }
    
    /**
     * Validates if PayPop can be shown for the given ticket and user
     * @param ticketId the ticket ID
     * @param customerUsername the customer username
     * @return true if PayPop can be shown
     */
    public static boolean canShowPayPop(String ticketId, String customerUsername) {
        System.out.println("=== PayPop.canShowPayPop() validation ===");
        System.out.println("- Ticket ID: '" + ticketId + "'");
        System.out.println("- Customer Username: '" + customerUsername + "'");
        System.out.println("- Currently showing: " + isShowing);
        
        // Allow reappearing - if already showing, hide first then show again
        if (isShowing) {
            System.out.println("PayPop: Already showing, will hide and reshow");
            hidepop();
        }
        
        // Validate user is logged in
        if (!cephra.CephraDB.isUserLoggedIn()) {
            System.out.println("PayPop: No user is currently logged in");
            return false;
        }
        
        // Get and validate current user
        String currentUser = cephra.CephraDB.getCurrentUsername();
        if (currentUser == null || currentUser.trim().isEmpty()) {
            System.out.println("PayPop: Current user is null or empty");
            return false;
        }
        
        // Validate user matches ticket owner
        if (!currentUser.trim().equals(customerUsername.trim())) {
            System.out.println("PayPop: User mismatch - current user ('" + currentUser + "') does not match ticket owner ('" + customerUsername + "')");
            return false;
        }
        
        System.out.println("PayPop: Validation passed - PayPop CAN be shown for user '" + currentUser + "' and ticket '" + ticketId + "'");
        return true;
    }
    
    /**
     * Shows PayPop with validation
     * @param ticketId the ticket ID
     * @param customerUsername the customer username
     * @return true if PayPop was shown successfully
     */
    public static boolean showPayPop(String ticketId, String customerUsername) {
        System.out.println("=== PayPop.showPayPop() called ===");
        
        if (!canShowPayPop(ticketId, customerUsername)) {
            return false;
        }
        
        // Find Phone frame and show centered PayPop
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
     * Shows PayPop centered on the Phone frame
     * @param phoneFrame the Phone frame to center on
     * @param ticketId the ticket ID
     */
    private static void showCenteredPayPop(cephra.Frame.Phone phoneFrame, String ticketId) {
        SwingUtilities.invokeLater(() -> {
            currentInstance = new AlreadyFull();
            currentTicketId = ticketId;
            isShowing = true;
            // Push ticket id to UI immediately (fallback to admin queue model if needed)
            try {
                String resolved = ticketId;
                if (resolved == null || resolved.trim().isEmpty()) {
                    String currentUser = cephra.CephraDB.getCurrentUsername();
                    resolved = currentInstance.findLatestTicketForUserFromAdminModel(currentUser);
                }
                currentInstance.setTicketOnUi(resolved);
            } catch (Exception ignore) {}
            
            // Determine phone content size (fallback to constants if not realized yet)
            int containerW = phoneFrame.getContentPane() != null ? phoneFrame.getContentPane().getWidth() : 0;
            int containerH = phoneFrame.getContentPane() != null ? phoneFrame.getContentPane().getHeight() : 0;
            if (containerW <= 0) containerW = PHONE_WIDTH;
            if (containerH <= 0) containerH = PHONE_HEIGHT;
            
            // Center the PayPop on the phone frame
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
     * Hides the PayPop and cleans up resources
     */
    public static void hidepop() {
    if (currentInstance != null && isShowing) {
        // Capture a local reference to avoid race conditions
        AlreadyFull instance = currentInstance;

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
     
    }

    // ensureTicketLabelVisible: removed; form manages visual properties

    // Read the latest ticket for the current user directly from Admin Queue table model
    private String findLatestTicketForUserFromAdminModel(String username) {
        try {
            if (username == null || username.trim().isEmpty()) return null;
            javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel)
                cephra.Admin.QueueBridge.class.getDeclaredField("model").get(null);
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
    public AlreadyFull() {
        initComponents();
        initializePayPop();
    }
    
    /**
     * Initializes PayPop components and data
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
                    hidepop();
                }
            }
        });
        
        // Request focus so key events work
        SwingUtilities.invokeLater(this::requestFocusInWindow);
    }

    /**
     * Updates PayPop labels with current ticket data and amounts
     */
    private void updateTextWithAmount() {
        try {
            // Resolve ticket strictly from Admin Queue table if not provided
            String ticket = currentTicketId;
            if (ticket == null || ticket.isEmpty()) {
                String currentUser = cephra.CephraDB.getCurrentUsername();
                ticket = findLatestTicketForUserFromAdminModel(currentUser);
            }
            if (ticket == null || ticket.isEmpty()) {
                ticket = cephra.Phone.QueueFlow.getCurrentTicketId();
            }
            if (ticket == null || ticket.isEmpty()) {
                System.err.println("PayPop: No current ticket found");
                return;
            }
            
            // Calculate amounts using centralized QueueBridge methods
            double amount = cephra.Admin.QueueBridge.computeAmountDue(ticket);
            double commission = cephra.Admin.QueueBridge.computePlatformCommission(amount);
            double net = cephra.Admin.QueueBridge.computeNetToStation(amount);
            
            // Calculate energy usage
            double usedKWh = calculateEnergyUsage(ticket);
            
            // Update UI labels
            updateLabels(ticket, amount, usedKWh);
            
            // Log summary for debugging
            logPaymentSummary(ticket, amount, usedKWh, commission, net);
            
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
        cephra.Admin.QueueBridge.BatteryInfo batteryInfo = cephra.Admin.QueueBridge.getTicketBatteryInfo(ticket);
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
       
    }
    
    /**
     * Logs payment summary for debugging
     * @param ticket the ticket ID
     * @param amount the total amount
     * @param usedKWh the energy usage
     * @param commission the commission amount
     * @param net the net amount to station
     */
    private void logPaymentSummary(String ticket, double amount, double usedKWh, double commission, double net) {
        String summary = String.format(
            "Charging Complete – Please Pay ₱%.2f\nEnergy: %.1f kWh @ ₱%.2f/kWh\nMin fee: ₱%.2f applies if higher\nCommission: ₱%.2f (18%%)\nNet to station: ₱%.2f",
            amount,
            usedKWh,
            cephra.Admin.QueueBridge.getRatePerKWh(),
            cephra.Admin.QueueBridge.getMinimumFee(),
            commission,
            net
        );
        System.out.println("PayPop Summary for " + ticket + ":\n" + summary);
    }
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Golink = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setLayout(null);

        Golink.setText("Ok");
        Golink.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GolinkActionPerformed(evt);
            }
        });
        add(Golink);
        Golink.setBounds(80, 90, 100, 30);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/AlreadyFull.png"))); // NOI18N
        add(jLabel1);
        jLabel1.setBounds(0, 0, 280, 140);
    }// </editor-fold>//GEN-END:initComponents

    private void GolinkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GolinkActionPerformed
      System.out.println("AlreadyFull Ok button clicked - closing popup");
        
        // Simply hide this popup panel
        hidepop();
    }//GEN-LAST:event_GolinkActionPerformed

  
    /**
     * Handles cash payment action
     * @param evt the action event
     */
    private void CashActionPerformed(ActionEvent evt) {
        System.out.println("Cash payment button clicked");
        
        if (!validateUserLoggedIn()) {
            return;
        }
        
        String currentUser = cephra.CephraDB.getCurrentUsername();
        System.out.println("Processing cash payment for user: " + currentUser);
        
        // Hide PayPop and navigate to home
        hidepop();
        navigateToHome();
    }

    /**
     * Handles online payment action
     * @param evt the action event
     */
    private void payonlineActionPerformed(ActionEvent evt) {
        System.out.println("Online payment button clicked");
        
        if (!validateUserLoggedIn()) {
            return;
        }
        
        String currentUser = cephra.CephraDB.getCurrentUsername();
        System.out.println("Processing online payment for user: " + currentUser);
        
        SwingUtilities.invokeLater(() -> {
            try {
                processOnlinePayment();
            } catch (Exception e) {
                handlePaymentError(e);
            } finally {
                hidepop();
                navigateToReceipt();
            }
        });
    }
    
    /**
     * Validates that user is logged in
     * @return true if user is logged in, false otherwise
     */
    private boolean validateUserLoggedIn() {
        if (!cephra.CephraDB.isUserLoggedIn()) {
            System.err.println("Payment blocked: No user is logged in");
            hidepop();
            return false;
        }
        return true;
    }
    
    /**
     * Processes online payment
     * @throws Exception if payment processing fails
     */
    private void processOnlinePayment() throws Exception {
        // Check if there's an active ticket
        if (!cephra.Phone.QueueFlow.hasActiveTicket()) {
            showErrorMessage("No active ticket found for payment.\nPlease get a ticket first.");
            return;
        }
        
        String currentTicket = cephra.Phone.QueueFlow.getCurrentTicketId();
        
        // Validate ticket is ready for payment
        if (!isTicketValidForPayment(currentTicket)) {
            showErrorMessage("Ticket is not ready for payment.");
            return;
        }
        
        // Process payment
        cephra.Admin.QueueBridge.markPaymentPaidOnline(currentTicket);
        cephra.Phone.QueueFlow.updatePaymentStatus(currentTicket, "Paid");
        
        System.out.println("GCash payment marked as paid for ticket: " + currentTicket);
        
        // Clear charging bay and grid after successful payment
        cephra.Admin.BayManagement.clearChargingBayForCompletedTicket(currentTicket);
        
        // No need to show success message - receipt panel will display the information
    }
    
    /**
     * Validates if ticket is ready for payment
     * @param ticketId the ticket ID
     * @return true if ticket is valid for payment
     */
    private boolean isTicketValidForPayment(String ticketId) {
        try {
            javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) 
                cephra.Admin.QueueBridge.class.getDeclaredField("model").get(null);
            
            if (model != null) {
                for (int i = 0; i < model.getRowCount(); i++) {
                    Object ticketValue = model.getValueAt(i, 0);
                    if (ticketId.equals(String.valueOf(ticketValue))) {
                        String status = String.valueOf(model.getValueAt(i, 3));
                        String payment = String.valueOf(model.getValueAt(i, 4));
                        return "Complete".equalsIgnoreCase(status) && "Pending".equalsIgnoreCase(payment);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error validating ticket for payment: " + e.getMessage());
            // If validation fails, allow payment to proceed
            return true;
        }
        return false;
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
     * Handles payment errors
     * @param e the exception
     */
    private void handlePaymentError(Exception e) {
        System.err.println("Error updating payment status: " + e.getMessage());
        e.printStackTrace();
        
        showErrorMessage("Failed to process GCash payment.\nError: " + e.getMessage() + "\nPlease try again or contact support.");
    }
    
    /**
     * Navigates to home screen
     */
    private void navigateToHome() {
        SwingUtilities.invokeLater(() -> {
            Window[] windows = Window.getWindows();
            for (Window window : windows) {
                if (window instanceof cephra.Frame.Phone) {
                    cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                    phoneFrame.switchPanel(new cephra.Phone.home());
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
                    phoneFrame.switchPanel(new cephra.Phone.Reciept());
                    break;
                }
            }
        });
    }




    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Golink;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}
