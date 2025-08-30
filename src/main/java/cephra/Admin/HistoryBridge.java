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
    
    private static void loadAllHistoryFromDatabase() {
        try {
            // Get all charging history records from database
            List<Object[]> dbRecords = cephra.CephraDB.getAllChargingHistory();
            records.clear(); // Clear existing records
            
            for (Object[] record : dbRecords) {
                // Calculate kWh used based on battery levels
                int initialBatteryLevel = (Integer) record[3];
                int finalBatteryLevel = 100; // Always 100% when completed
                double batteryCapacityKWh = 40.0; // 40kWh capacity
                double usedFraction = (finalBatteryLevel - initialBatteryLevel) / 100.0;
                double kwhUsed = usedFraction * batteryCapacityKWh;
                
                // Get payment method from payment transactions table
                String paymentMethod = cephra.CephraDB.getPaymentMethodForTicket((String) record[0]);
                if (paymentMethod == null) paymentMethod = "Cash"; // Default fallback
                
                // Get the admin username who served this transaction
                String servedBy = cephra.CephraDB.getCurrentUsername();
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
                    record[6], // date_time (already in correct format)
                    record[7]  // reference_number
                };
                records.add(adminRecord);
            }
            
            System.out.println("HistoryBridge: Loaded " + records.size() + " history records from database");
        } catch (Exception e) {
            System.err.println("HistoryBridge: Error loading history from database: " + e.getMessage());
            e.printStackTrace();
        }
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
        List<Object[]> dbRecords = cephra.CephraDB.getChargingHistoryForUser(username);
        for (Object[] record : dbRecords) {
            // Calculate kWh used based on battery levels
            int initialBatteryLevel = (Integer) record[3];
            int finalBatteryLevel = 100; // Always 100% when completed
            double batteryCapacityKWh = 40.0; // 40kWh capacity
            double usedFraction = (finalBatteryLevel - initialBatteryLevel) / 100.0;
            double kwhUsed = usedFraction * batteryCapacityKWh;
            
            // Get payment method from payment transactions table
            String paymentMethod = cephra.CephraDB.getPaymentMethodForTicket((String) record[0]);
            if (paymentMethod == null) paymentMethod = "Cash"; // Default fallback
            
            // Get the admin username who served this transaction
            String servedBy = cephra.CephraDB.getCurrentUsername();
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
                record[6], // date_time (already in correct format)
                record[7]  // reference_number
            };
            userRecords.add(adminRecord);
        }
        
        return userRecords;
    }
}


