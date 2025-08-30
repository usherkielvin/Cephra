package cephra.Phone;

import java.util.ArrayList;
import java.util.List;

public final class QueueFlow {

    public static final class Entry {
        public final String ticketId;
        public final String customerName;
        public final String serviceName;
        public final String status;
        public final String payment;
        public final String action;
        public final int initialBatteryPercent;
        public final double batteryCapacityKWh;

        public Entry(String ticketId, String customerName, String serviceName, String status, String payment, String action, int initialBatteryPercent, double batteryCapacityKWh) {
            this.ticketId = ticketId;
            this.customerName = customerName;
            this.serviceName = serviceName;
            this.status = status;
            this.payment = payment;
            this.action = action;
            this.initialBatteryPercent = initialBatteryPercent;
            this.batteryCapacityKWh = batteryCapacityKWh;
        }
    }

    private static final List<Entry> entries = new ArrayList<Entry>();

    private static String currentTicketId = "";
    private static String currentServiceName = "";
    private static int nextFastNumber = 1;   // FCH001, FCH002, ...
    private static int nextNormalNumber = 1; // NCH001, NCH002, ...

    private QueueFlow() {}
    
    // Initialize counters for ticket generation
    static {
        // Reset the current service and ticket for normal operation
        currentTicketId = "";
        currentServiceName = "";
        
        // Initialize counters from existing database tickets
        initializeCountersFromDatabase();
    }
    
    private static void initializeCountersFromDatabase() {
        try {
            int maxFastNumber = 0;
            int maxNormalNumber = 0;
            
            // Get all existing queue tickets from database
            List<Object[]> existingTickets = cephra.CephraDB.getAllQueueTickets();
            for (Object[] ticket : existingTickets) {
                String ticketId = String.valueOf(ticket[0]); // ticket_id is at index 0
                if (ticketId.startsWith("FCH")) {
                    int number = extractNumber(ticketId);
                    maxFastNumber = Math.max(maxFastNumber, number);
                } else if (ticketId.startsWith("NCH")) {
                    int number = extractNumber(ticketId);
                    maxNormalNumber = Math.max(maxNormalNumber, number);
                }
            }
            
            // Also check charging history for completed tickets
            List<Object[]> completedTickets = cephra.CephraDB.getAllChargingHistory();
            for (Object[] ticket : completedTickets) {
                String ticketId = String.valueOf(ticket[0]); // ticket_id is at index 0
                if (ticketId.startsWith("FCH")) {
                    int number = extractNumber(ticketId);
                    maxFastNumber = Math.max(maxFastNumber, number);
                } else if (ticketId.startsWith("NCH")) {
                    int number = extractNumber(ticketId);
                    maxNormalNumber = Math.max(maxNormalNumber, number);
                }
            }
            
            // Set counters to next available number
            nextFastNumber = maxFastNumber + 1;
            nextNormalNumber = maxNormalNumber + 1;
            
            System.out.println("QueueFlow: Initialized counters from database and history - Fast: " + nextFastNumber + ", Normal: " + nextNormalNumber);
        } catch (Exception e) {
            System.err.println("Error initializing counters from database: " + e.getMessage());
            // Keep default values if database access fails
        }
    }

    public static void setCurrent(String ticketId, String serviceName) {
        currentTicketId = ticketId;
        currentServiceName = serviceName;
    }

    public static void setCurrentServiceOnly(String serviceName) {
        currentTicketId = "";
        currentServiceName = serviceName;
    }

    public static String getCurrentTicketId() {
        return currentTicketId;
    }

    public static String getCurrentServiceName() {
        return currentServiceName;
    }

    public static List<Entry> getEntries() {
        return entries;
    }
    
    // Method to refresh counters from database (useful after application restart or manual refresh)
    public static void refreshCountersFromDatabase() {
        initializeCountersFromDatabase();
    }
    
    // Method to get current counter values for debugging
    public static String getCurrentCounterStatus() {
        return "Fast: " + nextFastNumber + ", Normal: " + nextNormalNumber;
    }
    
    public static void updatePaymentStatus(String ticketId, String paymentStatus) {
        for (int i = 0; i < entries.size(); i++) {
            Entry entry = entries.get(i);
            if (entry.ticketId.equals(ticketId)) {
                // Replace the entry with updated payment status
                entries.set(i, new Entry(
                    entry.ticketId,
                    entry.customerName,
                    entry.serviceName,
                    entry.status,
                    paymentStatus,
                    entry.action,
                    entry.initialBatteryPercent,
                    entry.batteryCapacityKWh
                ));
                System.out.println("QueueFlow: Updated payment status for ticket " + ticketId + " to " + paymentStatus);
                break;
            }
        }
    }
    
    public static boolean hasActiveTicket() {
        return currentTicketId != null && !currentTicketId.trim().isEmpty();
    }
    
    public static Entry getCurrentTicketEntry() {
        if (!hasActiveTicket()) {
            return null;
        }
        
        for (Entry entry : entries) {
            if (entry.ticketId.equals(currentTicketId)) {
                return entry;
            }
        }
        return null;
    }

    public static void addCurrentToAdminAndStore(String customerName) {
        String service = currentServiceName;
        // If no ticket was pre-assigned, generate the next ID for the service
        if (currentTicketId == null || currentTicketId.length() == 0) {
            currentTicketId = generateNextTicketIdForService(service);
        } else {
            // If a ticket was pre-set (e.g., FCH001), update counters so subsequent tickets increment
            updateCountersFromTicket(currentTicketId);
        }
        final String ticket = currentTicketId;
        final String status = "Pending";
        final String payment = "";
        final String action = "";
        // Get actual user battery level from CephraDB
        final int initialBatteryPercent = cephra.CephraDB.getUserBatteryLevel(customerName);
        final double batteryCapacityKWh = 40.0; // 40kWh capacity

        // Store in memory list
        entries.add(new Entry(ticket, customerName, service, status, payment, action, initialBatteryPercent, batteryCapacityKWh));

        // Reflect into Admin table if registered
        try {
            cephra.Admin.QueueBridge.addTicket(ticket, customerName, service, status, payment, action);
            cephra.Admin.QueueBridge.setTicketBatteryInfo(ticket, initialBatteryPercent, batteryCapacityKWh);
        } catch (Throwable t) {
            // ignore if admin queue not ready
        }

        // Prepare next number now that the current one has been used
        bumpCounterForService(service, ticket);
    }

    private static String generateNextTicketIdForService(String serviceName) {
        if (serviceName == null) {
            serviceName = "";
        }
        if (serviceName.toLowerCase().contains("fast")) {
            String ticket = formatTicket("FCH", nextFastNumber);
            System.out.println("QueueFlow: Generated Fast ticket: " + ticket + " (counter: " + nextFastNumber + ")");
            return ticket;
        }
        if (serviceName.toLowerCase().contains("normal")) {
            String ticket = formatTicket("NCH", nextNormalNumber);
            System.out.println("QueueFlow: Generated Normal ticket: " + ticket + " (counter: " + nextNormalNumber + ")");
            return ticket;
        }
        // default fall-back
        return formatTicket("GEN", 1);
    }

    private static void bumpCounterForService(String serviceName, String ticket) {
        if (serviceName == null) {
            return;
        }
        if (serviceName.toLowerCase().contains("fast")) {
            nextFastNumber = Math.max(nextFastNumber + 1, extractNumber(ticket) + 1);
        } else if (serviceName.toLowerCase().contains("normal")) {
            nextNormalNumber = Math.max(nextNormalNumber + 1, extractNumber(ticket) + 1);
        }
    }

    private static void updateCountersFromTicket(String ticket) {
        String upper = ticket == null ? "" : ticket.toUpperCase();
        int num = extractNumber(upper);
        if (upper.startsWith("FCH")) {
            nextFastNumber = Math.max(nextFastNumber, num + 1);
        } else if (upper.startsWith("NCH")) {
            nextNormalNumber = Math.max(nextNormalNumber, num + 1);
        }
    }

    private static int extractNumber(String ticket) {
        if (ticket == null) return 0;
        int n = 0;
        for (int i = 0; i < ticket.length(); i++) {
            char c = ticket.charAt(i);
            if (c >= '0' && c <= '9') {
                n = n * 10 + (c - '0');
            }
        }
        return n;
    }

    private static String formatTicket(String prefix, int number) {
        String numStr = String.format("%03d", number);
        return prefix + numStr;
    }
}


