package cephra.Admin;

import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public final class HistoryBridge {
    private static DefaultTableModel model;
    private static final List<Object[]> records = new ArrayList<Object[]>();

    private HistoryBridge() {}

    public static void registerModel(DefaultTableModel m) {
        model = m;
        // Rebuild table from all persisted records
        if (model != null) {
            loadAllHistoryFromDatabase();
            final List<Object[]> snapshotRecords = new ArrayList<Object[]>(records);
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    // Clear any template rows
                    model.setRowCount(0);
                    // Insert existing records, newest first at top
                    for (int i = snapshotRecords.size() - 1; i >= 0; i--) {
                        model.insertRow(0, snapshotRecords.get(i));
                    }
                }
            });
        }
    }
    
    // Method to refresh history table from database
    public static void refreshHistoryTable() {
        if (model != null) {
            loadAllHistoryFromDatabase();
            final List<Object[]> snapshotRecords = new ArrayList<Object[]>(records);
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    // Clear any template rows
                    model.setRowCount(0);
                    // Insert existing records, newest first at top
                    for (int i = snapshotRecords.size() - 1; i >= 0; i--) {
                        model.insertRow(0, snapshotRecords.get(i));
                    }
                }
            });
        }
    }
    
    private static void loadAllHistoryFromDatabase() {
        try {
            // Get all charging history records from database
            List<Object[]> dbRecords = cephra.Database.CephraDB.getAllChargingHistory();
            records.clear(); // Clear existing records
            
            for (Object[] record : dbRecords) {
                // Calculate kWh used based on battery levels
                int initialBatteryLevel = (Integer) record[3];
                int finalBatteryLevel = 100; // Always 100% when completed
                double batteryCapacityKWh = 40.0; // 40kWh capacity
                double usedFraction = (finalBatteryLevel - initialBatteryLevel) / 100.0;
                double kwhUsed = usedFraction * batteryCapacityKWh;
                
                // Get payment method from payment transactions table
                String paymentMethod = cephra.Database.CephraDB.getPaymentMethodForTicket((String) record[0]);
                if (paymentMethod == null) paymentMethod = "Cash"; // Default fallback
                
                // Get the admin username who served this transaction
                String servedBy = cephra.Database.CephraDB.getCurrentAdminUsername();
                if (servedBy == null || servedBy.trim().isEmpty()) {
                    servedBy = "Admin"; // Fallback if no admin logged in
                }
                
                // Convert database format to admin history format
                // Columns: Ticket, Customer, KWh, Total, Served By, Date & Time, Reference
                Object[] adminRecord = {
                    record[0], // ticket_id
                    record[1], // username
                    String.format("%.1f kWh", kwhUsed), // KWh used
                    String.format("%.2f", record[5]), // Total amount
                    servedBy + " (" + paymentMethod + ")", // served_by with payment method
                    formatDateTimeForDisplay(record[7]), // completed_at - format as 12-hour without seconds
                    record[6] // reference_number - compact format
                };
                records.add(adminRecord);
            }
            
            System.out.println("HistoryBridge: Loaded " + records.size() + " history records from database");
        } catch (Exception e) {
            System.err.println("HistoryBridge: Error loading history from database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Method to format timestamp for display (compact format to fit table)
    private static String formatDateTimeForDisplay(Object timestamp) {
        if (timestamp == null) {
            return "";
        }
        try {
            if (timestamp instanceof java.sql.Timestamp) {
                java.sql.Timestamp ts = (java.sql.Timestamp) timestamp;
                java.time.LocalDateTime ldt = ts.toLocalDateTime();
                return ldt.format(java.time.format.DateTimeFormatter.ofPattern("MM/dd/yy hh:mm a"));
            } else if (timestamp instanceof String) {
                // If it's already a formatted string, return as is
                return (String) timestamp;
            }
        } catch (Exception e) {
            System.err.println("Error formatting timestamp: " + e.getMessage());
        }
        return String.valueOf(timestamp);
    }
    
    public static void addRecord(final Object[] row) {
        if (model == null) {
            // save for later if history table not yet registered
            records.add(0, row);
        } else {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    // insert at top of the table
                    model.insertRow(0, row);
                }
            });
            records.add(0, row);
        }
    }
    
    public static List<Object[]> getRecordsForUser(String username) {
        if (username == null || username.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        List<Object[]> userRecords = new ArrayList<>();
        
        // Get records from database (single source of truth)
        List<Object[]> dbRecords = cephra.Database.CephraDB.getChargingHistoryForUser(username);
        for (Object[] record : dbRecords) {
            // Calculate kWh used based on battery levels
            int initialBatteryLevel = (Integer) record[3]; // initial_battery_level (index 3)
            int finalBatteryLevel = 100; // Always 100% when completed
            double batteryCapacityKWh = 40.0; // 40kWh capacity
            double usedFraction = (finalBatteryLevel - initialBatteryLevel) / 100.0;
            double kwhUsed = usedFraction * batteryCapacityKWh;
            
            // Get payment method from payment transactions table
            String paymentMethod = cephra.Database.CephraDB.getPaymentMethodForTicket((String) record[0]);
            if (paymentMethod == null) paymentMethod = "Cash"; // Default fallback
            
            // Get the admin username who served this transaction
            String servedBy = cephra.Database.CephraDB.getCurrentAdminUsername();
            if (servedBy == null || servedBy.trim().isEmpty()) {
                servedBy = "Admin"; // Fallback if no admin logged in
            }
            
            // Convert database format to admin history format
            // Database columns: ticket_id, username, service_type, initial_battery_level, charging_time_minutes, total_amount, reference_number, completed_at
            // Admin history columns: Ticket, Customer, KWh, Total, Served By, Date & Time, Reference
            Object[] adminRecord = {
                record[0], // Ticket (ticket_id)
                record[1], // Customer (username)
                String.format("%.1f kWh", kwhUsed), // KWh (calculated)
                String.format("%.2f", record[5]), // Total (total_amount)
                servedBy + " (" + paymentMethod + ")", // Served By (admin + payment method)
                formatDateTimeForDisplay(record[7]), // Date & Time (completed_at) - format as 12-hour without seconds
                record[6]  // Reference (reference_number) - compact format
            };
            userRecords.add(adminRecord);
        }
        
        return userRecords;
    }
    
    // Method to get the total count of history records
    public static int getHistoryRecordCount() {
        return records.size();
    }
    
    // Method to check if history is working properly
    public static void debugHistoryStatus() {
        System.out.println("HistoryBridge Debug Info:");
        System.out.println("- Model registered: " + (model != null));
        System.out.println("- Records in memory: " + records.size());
        System.out.println("- Database records: " + cephra.Database.CephraDB.getAllChargingHistory().size());
        
        if (model != null) {
            System.out.println("- Table rows: " + model.getRowCount());
        }
    }
}


