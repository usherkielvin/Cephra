package cephra.Phone.Utilities;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class HistoryManager {
    private static final List<HistoryUpdateListener> listeners = new CopyOnWriteArrayList<>();
    
    public interface HistoryUpdateListener {
        void onHistoryUpdated(String username);
    }
    
    public static void addHistoryUpdateListener(HistoryUpdateListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }
    
    public static void removeHistoryUpdateListener(HistoryUpdateListener listener) {
        listeners.remove(listener);
    }
    
    public static class HistoryEntry {
        private final String ticketId;
        private final String serviceType;
        private final String chargingTime;
        private final String total;
        private final String referenceNumber;
        private final LocalDateTime timestamp;
        
        public HistoryEntry(String ticketId, String serviceType, String chargingTime) {
            this.ticketId = ticketId;
            this.serviceType = serviceType;
            this.chargingTime = chargingTime;
            this.total = "";
            this.referenceNumber = "";
            this.timestamp = LocalDateTime.now();
        }
        
        public HistoryEntry(String ticketId, String serviceType, String chargingTime, String referenceNumber, LocalDateTime timestamp) {
            this.ticketId = ticketId;
            this.serviceType = serviceType;
            this.chargingTime = chargingTime;
            this.total = "";
            this.referenceNumber = referenceNumber;
            this.timestamp = timestamp;
        }
        
        public HistoryEntry(String ticketId, String serviceType, String chargingTime, String total, String referenceNumber, LocalDateTime timestamp) {
            this.ticketId = ticketId;
            this.serviceType = serviceType;
            this.chargingTime = chargingTime;
            this.total = total;
            this.referenceNumber = referenceNumber;
            this.timestamp = timestamp;
        }
        
        public String getTicketId() {
            return ticketId;
        }
        
        public String getServiceType() {
            return serviceType;
        }
        
        public String getChargingTime() {
            return chargingTime;
        }
        
        public String getReferenceNumber() {
            return referenceNumber;
        }
        
        public String getTotal() {
            return total;
        }
        
        public String getFormattedDate() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
            return timestamp.format(formatter);
        }
        
        public String getFormattedTime() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
            return timestamp.format(formatter);
        }
        
        public LocalDateTime getTimestamp() {
            return timestamp;
        }
    }
    

    
    public static List<HistoryEntry> getUserHistory(String username) {
        if (username == null || username.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        // Get charging history from database (this is the single source of truth)
        List<Object[]> dbHistory = cephra.Database.CephraDB.getChargingHistoryForUser(username);
        List<HistoryEntry> historyEntries = new ArrayList<>();
        
        for (Object[] record : dbHistory) {
            String ticketId = (String) record[0];
            String serviceType = (String) record[2];
            int chargingTimeMinutes = (Integer) record[4];
            double totalAmount = (Double) record[5];
            String referenceNumber = (String) record[6];
            java.sql.Timestamp timestamp = (java.sql.Timestamp) record[7];
            
            // Convert timestamp to LocalDateTime
            LocalDateTime localDateTime = timestamp.toLocalDateTime();
            
            // Get payment method from database
            String paymentMethod = cephra.Database.CephraDB.getPaymentMethodForTicket(ticketId);
            if (paymentMethod == null) paymentMethod = "Cash"; // Default fallback
            
            HistoryEntry entry = new HistoryEntry(
                ticketId, 
                serviceType + " (" + paymentMethod + ")", // Include payment method in service type
                chargingTimeMinutes + " mins", 
                String.format("₱%.2f", totalAmount), 
                referenceNumber, 
                localDateTime
            );
            historyEntries.add(entry);
        }
        
        // Sort the result by timestamp (newest first)
        historyEntries.sort((entry1, entry2) -> entry2.getTimestamp().compareTo(entry1.getTimestamp()));
        
        return historyEntries;
    }
    


    // Method to notify all listeners about a history update for a specific user
    public static void notifyHistoryUpdate(String username) {
        if (username == null || username.trim().isEmpty()) {
            System.err.println("UserHistoryManager: Cannot notify history update for empty username");
            return;
        }
        
        System.out.println("UserHistoryManager: Notifying listeners about history update for user: " + username);
        System.out.println("UserHistoryManager: Number of registered listeners: " + listeners.size());
        
        // Notify all listeners about the history update
        for (HistoryUpdateListener listener : listeners) {
            try {
                System.out.println("UserHistoryManager: Notifying listener: " + listener.getClass().getSimpleName());
                listener.onHistoryUpdated(username);
            } catch (Exception e) {
                System.err.println("UserHistoryManager: Error notifying listener: " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        System.out.println("UserHistoryManager: Finished notifying all listeners for user: " + username);
    }
    
    // Method to get current counter values for debugging
}
