package cephra.Phone;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.Collections;

public class UserHistoryManager {
    private static final Map<String, List<HistoryEntry>> userHistoryMap = new HashMap<>();
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
        private final LocalDateTime timestamp;
        
        public HistoryEntry(String ticketId, String serviceType, String chargingTime) {
            this.ticketId = ticketId;
            this.serviceType = serviceType;
            this.chargingTime = chargingTime;
            this.timestamp = LocalDateTime.now();
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
        
        public String getFormattedDate() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
            return timestamp.format(formatter);
        }
        
        public String getFormattedTime() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
            return timestamp.format(formatter);
        }
    }
    
    public static void addHistoryEntry(String username, String ticketId, String serviceType, String chargingTime) {
        if (username == null || username.trim().isEmpty()) {
            System.err.println("UserHistoryManager: Cannot add history entry for empty username");
            return;
        }
        
        HistoryEntry entry = new HistoryEntry(ticketId, serviceType, chargingTime);
        
        List<HistoryEntry> userEntries = userHistoryMap.get(username);
        if (userEntries == null) {
            userEntries = new ArrayList<>();
            userHistoryMap.put(username, userEntries);
        }
        
        // Add to the beginning of the list (newest first)
        userEntries.add(0, entry);
        
        System.out.println("UserHistoryManager: Added history entry for user: " + username + 
                           ", ticket: " + ticketId + 
                           ", service: " + serviceType);
        
        // Notify all listeners about the history update
        for (HistoryUpdateListener listener : listeners) {
            try {
                listener.onHistoryUpdated(username);
            } catch (Exception e) {
                System.err.println("UserHistoryManager: Error notifying listener: " + e.getMessage());
            }
        }
    }
    
    public static List<HistoryEntry> getUserHistory(String username) {
        if (username == null || username.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        // Get user's history from the map
        List<HistoryEntry> userEntries = userHistoryMap.get(username);
        if (userEntries == null) {
            userEntries = new ArrayList<>();
        } else {
            userEntries = new ArrayList<>(userEntries);
        }
        
        // If we already have entries, don't add admin entries to avoid duplicates
        // Admin entries are already added through the addHistoryEntry method when payment is marked as paid
        
        return userEntries;
    }
    
    private static List<HistoryEntry> getAdminHistoryForUser(String username) {
        List<HistoryEntry> result = new ArrayList<>();
        
        try {
            // Access admin history records
            List<Object[]> adminRecords = cephra.Admin.HistoryBridge.getRecordsForUser(username);
            
            if (adminRecords != null) {
                for (Object[] record : adminRecords) {
                    if (record.length >= 7) {
                        String ticketId = String.valueOf(record[0]);
                        String serviceType = String.valueOf(record[2]);
                        // Get actual charging time from KWh field (index 2)
                        String chargingTime = String.valueOf(record[2]);
                        result.add(new HistoryEntry(ticketId, serviceType, chargingTime));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error accessing admin history: " + e.getMessage());
        }
        
        return result;
    }
}