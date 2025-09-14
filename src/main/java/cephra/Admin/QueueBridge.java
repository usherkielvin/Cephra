package cephra.Admin;

import javax.swing.*;
import javax.swing.table.*;
import java.util.*;

public final class QueueBridge {
    private static DefaultTableModel model;
    private static final List<Object[]> records = new ArrayList<>();
    private static final Map<String, BatteryInfo> ticketBattery = new HashMap<>();
    private static int totalPaidCount = 0;

    // Configurable billing settings (central source of truth)
    private static volatile double RATE_PER_KWH = 15.0; // pesos per kWh
    private static volatile double MINIMUM_FEE = 50.0;   // pesos
    private static volatile double FAST_MULTIPLIER = 1.25; // Fast charging gets 25% premium
    
    // Static initialization block to load settings from database
    static {
        loadSettingsFromDatabase();
    }

    // Configurable charging speed (minutes per 1% charge)
    private static volatile double MINS_PER_PERCENT_FAST = 0.8;   // Fast charge
    private static volatile double MINS_PER_PERCENT_NORMAL = 1.6; // Normal charge

    // Battery info storage
    public static final class BatteryInfo {
        public final int initialPercent;
        public final double capacityKWh;
        public BatteryInfo(int initialPercent, double capacityKWh) {
            this.initialPercent = initialPercent;
            this.capacityKWh = capacityKWh;
        }
    }

    private QueueBridge() {}

    /** Register a JTable model so QueueBridge can sync with it */
    public static void registerModel(DefaultTableModel m) {
        model = m;
        if (model != null) {
            loadQueueFromDatabase();
        }
    }
    
    /** Load queue tickets from database */
    private static void loadQueueFromDatabase() {
        try {
            // Get all queue tickets from database
            List<Object[]> dbTickets = cephra.Database.CephraDB.getAllQueueTickets();
            records.clear(); // Clear existing records
            
            for (Object[] dbTicket : dbTickets) {
                // Convert database format to queue format
                Object[] queueRecord = {
                    dbTicket[0], // ticket_id
                    dbTicket[1], // reference_number (or generate if null)
                    dbTicket[2], // username
                    dbTicket[3], // service_type
                    dbTicket[4], // status
                    dbTicket[5], // payment_status
                    "" // action (empty for now)
                };
                records.add(queueRecord);
            }
            
            // Update the table
            SwingUtilities.invokeLater(() -> {
                model.setRowCount(0); // clear
                for (Object[] record : records) {
                    Object[] visibleRow = toVisibleRow(record);
                    model.insertRow(0, visibleRow);
                }
            });
            
        } catch (Exception e) {
            System.err.println("Error loading queue from database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /** Public method to reload queue from database (for hard refresh) */
    public static void reloadFromDatabase() {
        System.out.println("QueueBridge: Reloading queue from database");
        loadQueueFromDatabase();
    }

    /** Add a ticket with hidden random reference number */
    public static void addTicket(String ticket, String customer, String service, String status, String payment, String action) {
        String refNumber = generateReference(); // Use consistent 8-digit format
        final Object[] fullRecord = new Object[] { ticket, refNumber, customer, service, status, payment, action };

        records.add(0, fullRecord);

        // Store battery info for this ticket
        int userBatteryLevel = cephra.Database.CephraDB.getUserBatteryLevel(customer);
        ticketBattery.put(ticket, new BatteryInfo(userBatteryLevel, 40.0)); // 40kWh capacity
        
        // Set this as the user's active ticket with correct service type
        cephra.Database.CephraDB.setActiveTicket(customer, ticket, service, userBatteryLevel, "");
        
        // Add to database for persistent storage
        boolean dbSuccess = cephra.Database.CephraDB.addQueueTicket(ticket, customer, service, status, payment, userBatteryLevel);
        
        if (!dbSuccess) {
            System.err.println("Failed to add ticket " + ticket + " to database. It may already exist.");
            // Remove from memory records since database insertion failed
            records.remove(0);
            ticketBattery.remove(ticket);
            cephra.Database.CephraDB.clearActiveTicket(customer);
        }

        if (model != null) {
            final Object[] visibleRow = toVisibleRow(fullRecord);
            SwingUtilities.invokeLater(() -> model.insertRow(0, visibleRow));
        }
    }

    /** Helper: Convert full record (with ref) to visible row for table */
    private static Object[] toVisibleRow(Object[] fullRecord) {
        return new Object[] {
            fullRecord[0], // ticket
            fullRecord[2], // customer
            fullRecord[3], // service
            fullRecord[4], // status
            fullRecord[5], // payment
            fullRecord[6]  // action
        };
    }

    /** Battery Info Management */
    public static void setTicketBatteryInfo(String ticket, int initialPercent, double capacityKWh) {
        if (ticket != null) {
            ticketBattery.put(ticket, new BatteryInfo(initialPercent, capacityKWh));
        }
    }

    public static BatteryInfo getTicketBatteryInfo(String ticket) {
        return ticketBattery.get(ticket);
    }

    /** Retrieve hidden reference number */
    public static String getTicketRefNumber(String ticket) {
        if (ticket == null) return "";
        for (Object[] record : records) {
            if (record != null && ticket.equals(String.valueOf(record[0]))) {
                // Reference number is always stored at index 1
                if (record[1] != null) {
                    return String.valueOf(record[1]);
                }
            }
        }
        return "";
    }

    /** Billing Calculations */
    public static double computeAmountDue(String ticket) {
        BatteryInfo info = ticketBattery.get(ticket);
        if (info == null) {
            // Get actual user battery level from the ticket customer
            String customer = getTicketCustomer(ticket);
            if (customer != null && !customer.isEmpty()) {
                int userBatteryLevel = cephra.Database.CephraDB.getUserBatteryLevel(customer);
                info = new BatteryInfo(userBatteryLevel, 40.0);
            } else {
                info = new BatteryInfo(18, 40.0); // fallback default
            }
        }
        
        // Get service type to determine pricing
        String serviceType = getTicketService(ticket);
        double multiplier = 1.0; // Default multiplier for normal charging
        
        if (serviceType != null && serviceType.toLowerCase().contains("fast")) {
            multiplier = FAST_MULTIPLIER; // Apply fast charging premium
        }
        
        int start = Math.max(0, Math.min(100, info.initialPercent));
        double usedFraction = (100.0 - start) / 100.0;
        double energyKWh = usedFraction * info.capacityKWh;
        double gross = energyKWh * RATE_PER_KWH * multiplier; // Apply service multiplier
        return Math.max(gross, MINIMUM_FEE * multiplier); // Apply multiplier to minimum fee too
    }
    
    /** Helper method to get customer name from ticket */
    private static String getTicketCustomer(String ticket) {
        if (ticket == null) return null;
        for (Object[] record : records) {
            if (record != null && ticket.equals(String.valueOf(record[0]))) {
                return String.valueOf(record[2]); // Customer is at index 2
            }
        }
        return null;
    }

    public static double computePlatformCommission(double grossAmount) {
        return grossAmount * 0.18; // 18%
    }

    public static double computeNetToStation(double grossAmount) {
        return grossAmount * 0.82; // 82%
    }

    public static int getTotalPaidCount() {
        return totalPaidCount;
    }

    /** Mark a payment as Paid and add history (Cash payment - Admin) */
    public static void markPaymentPaid(final String ticket) {
        markPaymentPaidWithMethod(ticket, "Cash");
    }
    
    /** Mark a payment as Paid and add history (GCash payment - Online) */
    public static void markPaymentPaidOnline(final String ticket) {
        markPaymentPaidWithMethod(ticket, "GCash");
    }
    
    /** Mark a payment as Paid and add history with specified payment method */
    private static void markPaymentPaidWithMethod(final String ticket, final String paymentMethod) {
        if (ticket == null || ticket.trim().isEmpty()) {
            System.err.println("QueueBridge: Invalid ticket ID");
            return;
        }

        boolean foundInRecords = false;
        boolean incrementCounter = false;
        boolean alreadyPaid = false;
        String customerName = "";
        String serviceName = "";
        String referenceNumber = "";

        // Check if payment has already been processed for this ticket
        for (Object[] r : records) {
            if (r != null && ticket.equals(String.valueOf(r[0]))) {
                String prev = String.valueOf(r[5]); // Payment is index 5
                if ("Paid".equalsIgnoreCase(prev)) {
                    alreadyPaid = true;
                    System.out.println("QueueBridge: Payment already processed for ticket " + ticket + ", skipping duplicate");
                    break;
                }
                if (!"Paid".equalsIgnoreCase(prev)) {
                    incrementCounter = true;
                }
                r[5] = "Paid";
                // Generate a new unique reference number for this payment
                referenceNumber = generateReference();
                r[1] = referenceNumber; // Store in the original reference field
                foundInRecords = true;
                customerName = String.valueOf(r[2]);
                serviceName = String.valueOf(r[3]);
                
                // Note: We don't update the table model here anymore
                // The database operation will remove the ticket entirely, so no need to set "Paid" status
                // The proceed button will disappear when the ticket is removed from the table
                break;
            }
        }

        // If already paid, don't process again
        if (alreadyPaid) {
            return;
        }

        if (incrementCounter) totalPaidCount++;

        if (foundInRecords) {
            try {
                // Check if payment already exists in database to prevent duplicates
                if (cephra.Database.CephraDB.isPaymentAlreadyProcessed(ticket)) {
                    System.out.println("QueueBridge: Payment already exists in database for ticket " + ticket + ", ensuring UI cleanup");
                    try {
                        removeTicket(ticket);
                        triggerHardRefresh();
                        triggerPanelSwitchRefresh();
                    } catch (Throwable ignore) {}
                    return;
                }
                
                // Calculate charging details
                BatteryInfo batteryInfo = getTicketBatteryInfo(ticket);
                int initialBatteryLevel = batteryInfo != null ? batteryInfo.initialPercent : 20;
                int chargingTimeMinutes = computeEstimatedMinutes(ticket);
                double totalAmount = computeAmountDue(ticket);
                
                // Use a single database transaction to ensure consistency
                System.out.println("QueueBridge: About to process payment transaction for ticket " + ticket + 
                                 ", customer: " + customerName + ", service: " + serviceName + 
                                 ", amount: " + totalAmount + ", method: " + paymentMethod);
                boolean dbSuccess = cephra.Database.CephraDB.processPaymentTransaction(
                    ticket, customerName, serviceName, initialBatteryLevel, 
                    chargingTimeMinutes, totalAmount, paymentMethod, referenceNumber
                );
                System.out.println("QueueBridge: Payment transaction result for ticket " + ticket + ": " + dbSuccess);
                
                if (dbSuccess) {
                    // Note: processPaymentTransaction already handles:
                    // - Battery update to 100%
                    // - Ticket removal from queue
                    // - Active ticket clearing
                    // - History addition
                    
                    System.out.println("QueueBridge: " + paymentMethod + " payment completed for ticket " + ticket + 
                                     ", amount: ₱" + totalAmount + ", reference: " + referenceNumber);
                    
                    // Refresh history table to show the new completed ticket
                    try {
                        cephra.Admin.HistoryBridge.refreshHistoryTable();
                        System.out.println("QueueBridge: Refreshed history table after payment completion");
                    } catch (Exception e) {
                        System.err.println("QueueBridge: Error refreshing history table: " + e.getMessage());
                    }
                    
                    // Close PayPop on the phone if it is currently showing
                    SwingUtilities.invokeLater(() -> {
                        try {
                            cephra.Phone.PayPop.hidePayPop();
                            System.out.println("QueueBridge: Closed PayPop on phone after marking ticket as Paid: " + ticket);
                        } catch (Throwable t) {
                            System.err.println("QueueBridge: Failed to close PayPop: " + t.getMessage());
                        }
                    });
                    
                    // COPY THE SAME APPROACH AS MANUAL "MARK AS PAID":
                    // Add a longer delay to ensure database operations complete before UI updates
                    javax.swing.Timer refreshTimer = new javax.swing.Timer(200, _ -> {
                        try {
                            // Remove ticket from queue (same as manual payment)
                            removeTicket(ticket);
                            System.out.println("QueueBridge: Successfully removed ticket " + ticket + " via removeTicket (same as manual payment)");
                            
                            // Trigger multiple refresh approaches to ensure UI updates
                            triggerHardRefresh();
                            triggerPanelSwitchRefresh();
                            System.out.println("QueueBridge: Triggered multiple refresh approaches (same as manual payment)");
                        } catch (Throwable t) {
                            System.err.println("QueueBridge: Error removing ticket after online payment: " + t.getMessage());
                        }
                    });
                    refreshTimer.setRepeats(false);
                    refreshTimer.start();
                } else {
                    System.err.println("QueueBridge: Failed to process payment transaction for ticket " + ticket);
                    // Revert the payment status in the records array
                    for (Object[] r : records) {
                        if (r != null && ticket.equals(String.valueOf(r[0]))) {
                            r[5] = "Pending"; // Revert payment status
                            r[1] = ""; // Clear reference number
                            break;
                        }
                    }
                }
                
            } catch (Throwable t) {
                System.err.println("QueueBridge: Error processing payment completion: " + t.getMessage());
                t.printStackTrace();
                
                // Revert any changes made to the records array
                for (Object[] r : records) {
                    if (r != null && ticket.equals(String.valueOf(r[0]))) {
                        r[5] = "Pending"; // Revert payment status
                        r[1] = ""; // Clear reference number
                        break;
                    }
                }
            }
        }
        
        // Note: Table model is now updated BEFORE database operations in updateTableModelForPayment()
    }
    
    private static String generateReference() {
        // Generate 8-digit number (10000000 to 99999999)
        int number = 10000000 + new Random().nextInt(90000000);
        return String.valueOf(number);
    }
    
    
    /**
     * Triggers a hard refresh by finding the Queue panel and calling its hard refresh method
     */
    private static void triggerHardRefresh() {
        SwingUtilities.invokeLater(() -> {
            try {
                // Find the Queue panel in the current window
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frame.Admin) {
                        cephra.Frame.Admin adminFrame = (cephra.Frame.Admin) window;
                        
                        // Look for Queue panel in the admin frame
                        cephra.Admin.Queue queuePanel = findQueuePanel(adminFrame);
                        if (queuePanel != null) {
                            // Find and refresh any tabbed panes that might contain the queue
                            refreshTabbedPanes(adminFrame);
                            
                            // Trigger hard refresh
                            queuePanel.hardRefreshTable();
                            System.out.println("QueueBridge: Triggered HARD refresh");
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("QueueBridge: Error triggering hard refresh: " + e.getMessage());
            }
        });
    }
    
    /**
     * Triggers a panel switch refresh that mimics switching panels and coming back
     */
    private static void triggerPanelSwitchRefresh() {
        SwingUtilities.invokeLater(() -> {
            try {
                // Find the Admin frame
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frame.Admin) {
                        cephra.Frame.Admin adminFrame = (cephra.Frame.Admin) window;
                        
                        // Look for Queue panel in the admin frame
                        cephra.Admin.Queue queuePanel = findQueuePanel(adminFrame);
                        if (queuePanel != null) {
                            // Find and refresh any tabbed panes that might contain the queue
                            refreshTabbedPanes(adminFrame);
                            
                            // Mimic what happens when switching panels: revalidate and repaint
                            adminFrame.revalidate();
                            adminFrame.repaint();
                            
                            // Also force the queue panel to refresh
                            queuePanel.revalidate();
                            queuePanel.repaint();
                            
                            // Force table refresh
                            queuePanel.hardRefreshTable();
                            
                            System.out.println("QueueBridge: Triggered panel switch refresh (mimics switching panels)");
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("QueueBridge: Error triggering panel switch refresh: " + e.getMessage());
            }
        });
    }
    
    /**
     * Helper method to refresh any tabbed panes in the container hierarchy
     */
    private static void refreshTabbedPanes(java.awt.Container container) {
        if (container instanceof javax.swing.JTabbedPane) {
            javax.swing.JTabbedPane tabbedPane = (javax.swing.JTabbedPane) container;
            tabbedPane.revalidate();
            tabbedPane.repaint();
            System.out.println("QueueBridge: Refreshed tabbed pane");
        }
        
        for (java.awt.Component component : container.getComponents()) {
            if (component instanceof java.awt.Container) {
                refreshTabbedPanes((java.awt.Container) component);
            }
        }
    }
    
    /**
     * Helper method to find the Queue panel in the admin frame
     */
    private static cephra.Admin.Queue findQueuePanel(java.awt.Container container) {
        if (container instanceof cephra.Admin.Queue) {
            return (cephra.Admin.Queue) container;
        }
        
        for (java.awt.Component component : container.getComponents()) {
            if (component instanceof java.awt.Container) {
                cephra.Admin.Queue found = findQueuePanel((java.awt.Container) component);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }
    
    /** Get ticket service name (e.g., Fast Charging / Normal Charging) */
    public static String getTicketService(String ticket) {
        if (ticket == null) return null;
        for (Object[] record : records) {
            if (record != null && ticket.equals(String.valueOf(record[0]))) {
                return String.valueOf(record[3]); // service at index 3
            }
        }
        return null;
    }

    /** Compute estimated minutes to full using stored ticket battery and service */
    public static int computeEstimatedMinutes(String ticket) {
        BatteryInfo info = getTicketBatteryInfo(ticket);
        if (info == null) {
            // fallback: try reconstruct from customer
            String customer = getTicketCustomer(ticket);
            if (customer != null) {
                int userBatteryLevel = cephra.Database.CephraDB.getUserBatteryLevel(customer);
                info = new BatteryInfo(userBatteryLevel, 40.0);
            } else {
                info = new BatteryInfo(18, 40.0);
            }
        }
        String service = getTicketService(ticket);
        return computeEstimatedMinutes(info.initialPercent, service);
    }

    /** Compute estimated minutes from start percent and service name */
    public static int computeEstimatedMinutes(int startPercent, String serviceName) {
        int clamped = Math.max(0, Math.min(100, startPercent));
        int needed = 100 - clamped;
        double minsPerPercent = getMinsPerPercentForService(serviceName);
        return (int)Math.round(needed * minsPerPercent);
    }

    /** Helper: get minutes per percent based on service */
    public static double getMinsPerPercentForService(String serviceName) {
        if (serviceName != null && serviceName.toLowerCase().contains("fast")) {
            return MINS_PER_PERCENT_FAST;
        }
        return MINS_PER_PERCENT_NORMAL;
    }

    // Charging speed settings API
    public static void setMinsPerPercentFast(double value) { if (value > 0) MINS_PER_PERCENT_FAST = value; }
    public static double getMinsPerPercentFast() { return MINS_PER_PERCENT_FAST; }
    public static void setMinsPerPercentNormal(double value) { if (value > 0) MINS_PER_PERCENT_NORMAL = value; }
    public static double getMinsPerPercentNormal() { return MINS_PER_PERCENT_NORMAL; }
    
    // Billing settings API (for Dashboard/Admin to control)
    public static void setRatePerKWh(double rate) {
        if (rate > 0) {
            RATE_PER_KWH = rate;
        }
    }

    public static double getRatePerKWh() {
        return RATE_PER_KWH;
    }

    public static void setMinimumFee(double minFee) {
        if (minFee >= 0) {
            MINIMUM_FEE = minFee;
        }
    }

    public static double getMinimumFee() {
        return MINIMUM_FEE;
    }
    
    public static void setFastMultiplier(double multiplier) {
        if (multiplier >= 1.0) {
            FAST_MULTIPLIER = multiplier;
        }
    }
    
    public static double getFastMultiplier() {
        return FAST_MULTIPLIER;
    }
    
    // Load settings from database
    private static void loadSettingsFromDatabase() {
        try {
            // Load minimum fee from database
            String minFeeStr = cephra.Database.CephraDB.getSystemSetting("minimum_fee");
            if (minFeeStr != null && !minFeeStr.trim().isEmpty()) {
                MINIMUM_FEE = Double.parseDouble(minFeeStr);
                System.out.println("QueueBridge: Loaded minimum fee from database: ₱" + MINIMUM_FEE);
            } else {
                // Set default if not found in database
                cephra.Database.CephraDB.updateSystemSetting("minimum_fee", String.valueOf(MINIMUM_FEE));
                System.out.println("QueueBridge: Set default minimum fee: ₱" + MINIMUM_FEE);
            }
            
            // Load rate per kWh from database
            String rateStr = cephra.Database.CephraDB.getSystemSetting("rate_per_kwh");
            if (rateStr != null && !rateStr.trim().isEmpty()) {
                RATE_PER_KWH = Double.parseDouble(rateStr);
                System.out.println("QueueBridge: Loaded rate per kWh from database: ₱" + RATE_PER_KWH);
            } else {
                // Set default if not found in database
                cephra.Database.CephraDB.updateSystemSetting("rate_per_kwh", String.valueOf(RATE_PER_KWH));
                System.out.println("QueueBridge: Set default rate per kWh: ₱" + RATE_PER_KWH);
            }
            
            // Load fast multiplier from database
            String multiplierStr = cephra.Database.CephraDB.getSystemSetting("fast_multiplier");
            if (multiplierStr != null && !multiplierStr.trim().isEmpty()) {
                FAST_MULTIPLIER = Double.parseDouble(multiplierStr);
                System.out.println("QueueBridge: Loaded fast multiplier from database: " + String.format("%.0f%%", (FAST_MULTIPLIER - 1) * 100));
            } else {
                // Set default if not found in database
                cephra.Database.CephraDB.updateSystemSetting("fast_multiplier", String.valueOf(FAST_MULTIPLIER));
                System.out.println("QueueBridge: Set default fast multiplier: " + String.format("%.0f%%", (FAST_MULTIPLIER - 1) * 100));
            }
            
        } catch (Exception e) {
            System.err.println("QueueBridge: Error loading settings from database: " + e.getMessage());
            // Keep default values if there's an error
        }
    }
    


    /** Remove a ticket from records and table */
    public static void removeTicket(final String ticket) {
        if (ticket == null || ticket.trim().isEmpty()) {
            System.err.println("QueueBridge: Cannot remove null or empty ticket");
            return;
        }

        // Check if ticket is already processed (moved to history)
        // If it's already processed, just remove from UI without database operation
        boolean isAlreadyProcessed = isTicketInHistory(ticket);
        
        if (!isAlreadyProcessed) {
            try {
                // Remove from database only if not already processed
                boolean dbRemoved = cephra.Database.CephraDB.removeQueueTicket(ticket);
                if (!dbRemoved) {
                    System.err.println("QueueBridge: Failed to remove ticket " + ticket + " from database");
                }
            } catch (Exception e) {
                System.err.println("QueueBridge: Error removing ticket from database: " + e.getMessage());
            }
        } else {
            System.out.println("QueueBridge: Ticket " + ticket + " already processed, removing from UI only");
        }

        // Remove from memory records
        try {
            for (int i = records.size() - 1; i >= 0; i--) {
                Object[] r = records.get(i);
                if (r != null && ticket.equals(String.valueOf(r[0]))) {
                    records.remove(i);
                    // Also remove battery info
                    ticketBattery.remove(ticket);
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println("QueueBridge: Error removing ticket from memory records: " + e.getMessage());
        }

        // Remove from table model
        if (model != null) {
            try {
                SwingUtilities.invokeLater(() -> {
                    try {
                        int rowCount = model.getRowCount();
                        for (int i = rowCount - 1; i >= 0; i--) {
                            if (i < model.getRowCount()) { // Double-check row still exists
                                Object v = model.getValueAt(i, 0);
                                if (v != null && ticket.equals(String.valueOf(v))) {
                                    model.removeRow(i);
                                    System.out.println("QueueBridge: Successfully removed ticket " + ticket + " from table model at row " + i);
                                    
                                    // Force table model to fire change events
                                    model.fireTableDataChanged();
                                    break;
                                }
                            }
                        }
                    } catch (Exception e) {
                        System.err.println("QueueBridge: Error removing ticket from table model: " + e.getMessage());
                    }
                });
            } catch (Exception e) {
                System.err.println("QueueBridge: Error scheduling table model update: " + e.getMessage());
            }
        }
    }
    
    /** Check if ticket is already in charging history (already processed) */
    private static boolean isTicketInHistory(String ticket) {
        try {
            // Check if ticket exists in charging_history table
            return cephra.Database.CephraDB.isTicketInChargingHistory(ticket);
        } catch (Exception e) {
            System.err.println("QueueBridge: Error checking if ticket is in history: " + e.getMessage());
            return false;
        }
    }
    
}
