package cephra.Phone;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

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
        
        // Get admin history records for this user (these are the source of truth)
        List<HistoryEntry> adminEntries = getAdminHistoryForUser(username);
        
        // Get user's local history entries
        List<HistoryEntry> userEntries = userHistoryMap.get(username);
        if (userEntries == null) {
            userEntries = new ArrayList<>();
        } else {
            userEntries = new ArrayList<>(userEntries);
        }
        
        // Create a set of ticket IDs from admin entries to avoid duplicates
        Set<String> adminTicketIds = new HashSet<>();
        for (HistoryEntry adminEntry : adminEntries) {
            adminTicketIds.add(adminEntry.getTicketId());
        }
        
        // Only add local entries that don't exist in admin history
        List<HistoryEntry> result = new ArrayList<>(adminEntries);
        for (HistoryEntry localEntry : userEntries) {
            if (!adminTicketIds.contains(localEntry.getTicketId())) {
                result.add(localEntry);
            }
        }
        
        // Sort the result by timestamp (newest first)
        result.sort((entry1, entry2) -> entry2.getTimestamp().compareTo(entry1.getTimestamp()));
        
        return result;
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
                        
                        // Determine service type based on ticket ID (FCH = Fast Charge, NCH = Normal Charge)
                        String serviceType = "";
                        if (ticketId.startsWith("FCH")) {
                            serviceType = "Fast Charge";
                        } else if (ticketId.startsWith("NCH")) {
                            serviceType = "Normal Charge";
                        } else {
                            serviceType = "Charging Service";
                        }
                        
                        String chargingTime = "40 mins"; // default
                        try {
                            int est = cephra.Admin.QueueBridge.computeEstimatedMinutes(ticketId);
                            if (est > 0) {
                                chargingTime = est + " mins";
                            }
                        } catch (Throwable t) {
                            // keep default
                        }
                        String total = String.valueOf(record[3]) + " PHP"; // Total amount from admin history
                        String referenceNumber = String.valueOf(record[6]); // Reference number
                        System.out.println("UserHistoryManager: Extracted reference number '" + referenceNumber + "' for ticket " + ticketId + " from admin record");
                        
                        // Parse date and time from admin history (index 5)
                        LocalDateTime adminDateTime = LocalDateTime.now(); // Default to current time
                        try {
                            String dateTimeStr = String.valueOf(record[5]);
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                            adminDateTime = LocalDateTime.parse(dateTimeStr, formatter);
                        } catch (Exception e) {
                            System.err.println("Error parsing admin history date: " + e.getMessage());
                        }
                        
                        result.add(new HistoryEntry(ticketId, serviceType, chargingTime, total, referenceNumber, adminDateTime));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error accessing admin history: " + e.getMessage());
        }
        
        return result;
    }
    

}