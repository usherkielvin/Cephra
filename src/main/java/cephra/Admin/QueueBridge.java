package cephra.Admin;

import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public final class QueueBridge {
    private static DefaultTableModel model;
    private static final List<Object[]> records = new ArrayList<>();
    private static final Map<String, BatteryInfo> ticketBattery = new HashMap<>();
    private static int totalPaidCount = 0;
    private static final Random random = new Random();

    // Configurable billing settings (central source of truth)
    private static volatile double RATE_PER_KWH = 15.0; // pesos per kWh
    private static volatile double MINIMUM_FEE = 50.0;   // pesos

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
            List<Object[]> dbTickets = cephra.CephraDB.getAllQueueTickets();
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

    /** Add a ticket with hidden random reference number */
    public static void addTicket(String ticket, String customer, String service, String status, String payment, String action) {
        String refNumber = generateReference(); // Use consistent 8-digit format
        final Object[] fullRecord = new Object[] { ticket, refNumber, customer, service, status, payment, action };

        records.add(0, fullRecord);

        // Store battery info for this ticket
        int userBatteryLevel = cephra.CephraDB.getUserBatteryLevel(customer);
        ticketBattery.put(ticket, new BatteryInfo(userBatteryLevel, 40.0)); // 40kWh capacity
        
        // Set this as the user's active ticket
        cephra.CephraDB.setActiveTicket(customer, ticket);
        
        // Add to database for persistent storage
        boolean dbSuccess = cephra.CephraDB.addQueueTicket(ticket, customer, service, status, payment, userBatteryLevel);
        
        if (!dbSuccess) {
            System.err.println("Failed to add ticket " + ticket + " to database. It may already exist.");
            // Remove from memory records since database insertion failed
            records.remove(0);
            ticketBattery.remove(ticket);
            cephra.CephraDB.clearActiveTicket(customer);
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
                int userBatteryLevel = cephra.CephraDB.getUserBatteryLevel(customer);
                info = new BatteryInfo(userBatteryLevel, 40.0);
            } else {
                info = new BatteryInfo(18, 40.0); // fallback default
            }
        }
        int start = Math.max(0, Math.min(100, info.initialPercent));
        double usedFraction = (100.0 - start) / 100.0;
        double energyKWh = usedFraction * info.capacityKWh;
        double gross = energyKWh * RATE_PER_KWH;
        return Math.max(gross, MINIMUM_FEE);
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
                if (cephra.CephraDB.isPaymentAlreadyProcessed(ticket)) {
                    System.out.println("QueueBridge: Payment already exists in database for ticket " + ticket + ", skipping duplicate");
                    return;
                }
                
                // Calculate charging details
                BatteryInfo batteryInfo = getTicketBatteryInfo(ticket);
                int initialBatteryLevel = batteryInfo != null ? batteryInfo.initialPercent : 20;
                int chargingTimeMinutes = computeEstimatedMinutes(ticket);
                double totalAmount = computeAmountDue(ticket);
                
                // Use a single database transaction to ensure consistency
                boolean dbSuccess = cephra.CephraDB.processPaymentTransaction(
                    ticket, customerName, serviceName, initialBatteryLevel, 
                    chargingTimeMinutes, totalAmount, paymentMethod, referenceNumber
                );
                
                if (dbSuccess) {
                    // Clear the active ticket and charge battery to full when payment is completed
                    cephra.CephraDB.clearActiveTicket(customerName);
                    cephra.CephraDB.chargeUserBatteryToFull(customerName);
                    
                    System.out.println("QueueBridge: " + paymentMethod + " payment completed for ticket " + ticket + 
                                     ", amount: ₱" + totalAmount + ", reference: " + referenceNumber);
                    
                    // Refresh history table to show the new completed ticket
                    try {
                        cephra.Admin.HistoryBridge.refreshHistoryTable();
                        System.out.println("QueueBridge: Refreshed history table after payment completion");
                    } catch (Exception e) {
                        System.err.println("QueueBridge: Error refreshing history table: " + e.getMessage());
                    }
                } else {
                    System.err.println("QueueBridge: Failed to process payment transaction for ticket " + ticket);
                }
                
            } catch (Throwable t) {
                System.err.println("QueueBridge: Error processing payment completion: " + t.getMessage());
                t.printStackTrace();
            }
        }
        
        // Update JTable if present
        if (model != null) {
            SwingUtilities.invokeLater(() -> {
                for (int i = 0; i < model.getRowCount(); i++) {
                    Object v = model.getValueAt(i, 0);
                    if (ticket.equals(String.valueOf(v))) {
                        model.setValueAt("Paid", i, 4); // Payment col index = 4 in visible table
                        break;
                    }
                }
            });
        }
    }
    
    private static String generateReference() {
        java.util.Random r = new java.util.Random();
        // Generate 8-digit number (10000000 to 99999999)
        int number = 10000000 + r.nextInt(90000000);
        return String.valueOf(number);
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
                int userBatteryLevel = cephra.CephraDB.getUserBatteryLevel(customer);
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
    


    /** Remove a ticket from records and table */
    public static void removeTicket(final String ticket) {
        if (ticket == null || ticket.trim().isEmpty()) {
            System.err.println("QueueBridge: Cannot remove null or empty ticket");
            return;
        }

        try {
            // Remove from database first
            boolean dbRemoved = cephra.CephraDB.removeQueueTicket(ticket);
            if (!dbRemoved) {
                System.err.println("QueueBridge: Failed to remove ticket " + ticket + " from database");
            }
        } catch (Exception e) {
            System.err.println("QueueBridge: Error removing ticket from database: " + e.getMessage());
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
                        for (int i = model.getRowCount() - 1; i >= 0; i--) {
                            Object v = model.getValueAt(i, 0);
                            if (v != null && ticket.equals(String.valueOf(v))) {
                                model.removeRow(i);
                                break;
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
}
