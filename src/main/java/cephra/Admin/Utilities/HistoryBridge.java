package cephra.Admin.Utilities;

import javax.swing.*;
import javax.swing.table.*;
import java.util.*;

public final class HistoryBridge {
    private static DefaultTableModel model;
    private static final List<Object[]> records = new ArrayList<>();

    private HistoryBridge() {}

    public static void registerModel(DefaultTableModel m) {
        model = m;
        if (model != null) {
            refreshHistoryTable();
        }
    }
    
    public static void refreshHistoryTable() {
        if (model != null) {
            loadAllHistoryFromDatabase();
            updateTableWithRecords();
        }
    }
    
    private static void loadAllHistoryFromDatabase() {
        try {
            List<Object[]> dbRecords = cephra.Database.CephraDB.getAllChargingHistory();
            records.clear();
            
            for (Object[] record : dbRecords) {
                Object[] adminRecord = convertDatabaseRecordToAdminFormat(record);
                records.add(adminRecord);
            }
        } catch (Exception e) {
            System.err.println("HistoryBridge: Error loading history from database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void updateTableWithRecords() {
        final List<Object[]> snapshotRecords = new ArrayList<>(records);
        SwingUtilities.invokeLater(() -> {
            model.setRowCount(0);
            // Insert records newest first (reverse order)
            for (int i = snapshotRecords.size() - 1; i >= 0; i--) {
                model.insertRow(0, snapshotRecords.get(i));
            }
        });
    }
    
    private static Object[] convertDatabaseRecordToAdminFormat(Object[] dbRecord) {
        int initialBatteryLevel = (Integer) dbRecord[3];
        double kwhUsed = (100 - initialBatteryLevel) / 100.0 * 40.0; // 40kWh capacity
        
        // Get the user's plate number
        String plateNumber = cephra.Database.CephraDB.getUserPlateNumber((String) dbRecord[1]);
        if (plateNumber == null || plateNumber.trim().isEmpty()) {
            plateNumber = "N/A"; // Default fallback if no plate number
        }
        
        return new Object[]{
            dbRecord[0], // Ticket ID
            dbRecord[1], // Customer username
            String.format("%.1f kWh", kwhUsed),
            String.format("%.2f", dbRecord[6]), // Total amount
            plateNumber, // Plate number
            formatDateTimeForDisplay(dbRecord[8]), // Completion date/time
            dbRecord[7] // Reference number
        };
    }
    
    private static String getPaymentMethodForTicket(String ticketId) {
        String paymentMethod = cephra.Database.CephraDB.getPaymentMethodForTicket(ticketId);
        return (paymentMethod != null) ? paymentMethod : "Cash";
    }
    
    private static String getCurrentAdminUsername() {
        String servedBy = cephra.Database.CephraDB.getCurrentAdminUsername();
        return (servedBy != null && !servedBy.trim().isEmpty()) ? servedBy : "Admin";
    }
    
    private static String formatDateTimeForDisplay(Object timestamp) {
        if (timestamp == null) return "";
        
        try {
            if (timestamp instanceof java.sql.Timestamp) {
                java.sql.Timestamp ts = (java.sql.Timestamp) timestamp;
                return ts.toLocalDateTime().format(
                    java.time.format.DateTimeFormatter.ofPattern("MM/dd/yy hh:mm a"));
            }
            return String.valueOf(timestamp);
        } catch (Exception e) {
            System.err.println("Error formatting timestamp: " + e.getMessage());
            return String.valueOf(timestamp);
        }
    }
    
    public static void addRecord(Object[] row) {
        if (model != null) {
            SwingUtilities.invokeLater(() -> model.insertRow(0, row));
        }
        records.add(0, row);
    }
    
    public static List<Object[]> getRecordsForUser(String username) {
        if (username == null || username.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        List<Object[]> userRecords = new ArrayList<>();
        List<Object[]> dbRecords = cephra.Database.CephraDB.getChargingHistoryForUser(username);
        
        for (Object[] record : dbRecords) {
            userRecords.add(convertDatabaseRecordToAdminFormat(record));
        }
        
        return userRecords;
    }
}