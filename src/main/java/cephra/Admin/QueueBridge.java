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
    private static final List<Object[]> records = new ArrayList<Object[]>();
    private static final Map<String, BatteryInfo> ticketBattery = new HashMap<String, BatteryInfo>();
    private static int totalPaidCount = 0;
    private static final Random random = new Random();

    public static final class BatteryInfo {
        public final int initialPercent;
        public final double capacityKWh;
        public BatteryInfo(int initialPercent, double capacityKWh) {
            this.initialPercent = initialPercent;
            this.capacityKWh = capacityKWh;
        }
    }

    private QueueBridge() {}

    public static void registerModel(DefaultTableModel m) {
        model = m;
        if (model != null) {
            final List<Object[]> snapshot = new ArrayList<Object[]>(records);
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    // Clear initial template rows
                    model.setRowCount(0);
                    // Insert saved records, newest first at top (only visible columns)
                    for (int i = snapshot.size() - 1; i >= 0; i--) {
                        Object[] fullRecord = snapshot.get(i);
                        // Convert full record to visible row (skip ref number column)
                        Object[] visibleRow = new Object[] { 
                            fullRecord[0], // ticket
                            fullRecord[2], // customer  
                            fullRecord[3], // service
                            fullRecord[4], // status
                            fullRecord[5], // payment
                            fullRecord[6]  // action
                        };
                        model.insertRow(0, visibleRow);
                    }
                }
            });
        }
    }

    public static void addTicket(String ticket, String customer, String service, String status, String payment, String action) {
        // Generate random reference number (7 digits) - hidden from table
        String refNumber = String.valueOf(1000000 + random.nextInt(9000000)); // Random 7-digit number
        
        // Store complete record with ref number internally
        final Object[] fullRecord = new Object[] { ticket, refNumber, customer, service, status, payment, action };
        
        // Store in records with ref number
        records.add(0, fullRecord);
        
        // Only show visible columns in table (without ref number)
        final Object[] visibleRow = new Object[] { ticket, customer, service, status, payment, action };
        
        if (model == null) {
            return;
        }
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                model.insertRow(0, visibleRow);
            }
        });
    }

    public static void setTicketBatteryInfo(String ticket, int initialPercent, double capacityKWh) {
        if (ticket == null) return;
        ticketBattery.put(ticket, new BatteryInfo(initialPercent, capacityKWh));
    }

    public static BatteryInfo getTicketBatteryInfo(String ticket) {
        return ticketBattery.get(ticket);
    }

    public static String getTicketRefNumber(String ticket) {
        if (ticket == null) return "";
        
        // Search in records first
        for (Object[] record : records) {
            if (record != null && record.length > 1 && ticket.equals(String.valueOf(record[0]))) {
                return String.valueOf(record[1]); // Reference number is in column 1
            }
        }
        
        // Search in table model if available
        if (model != null) {
            for (int i = 0; i < model.getRowCount(); i++) {
                Object ticketId = model.getValueAt(i, 0);
                if (ticket.equals(String.valueOf(ticketId))) {
                    return String.valueOf(model.getValueAt(i, 1)); // Reference number is in column 1
                }
            }
        }
        
        return "";
    }

    public static double computeAmountDue(String ticket) {
        BatteryInfo info = ticketBattery.get(ticket);
        if (info == null) {
            // fallback defaults
            info = new BatteryInfo(18, 40.0);
        }
        int start = Math.max(0, Math.min(100, info.initialPercent));
        double usedFraction = (100.0 - start) / 100.0;
        double energyKWh = usedFraction * info.capacityKWh;
        double gross = energyKWh * 15.0;
        return Math.max(gross, 50.0);
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

    public static void markPaymentPaid(final String ticket) {
        if (ticket == null || ticket.trim().isEmpty()) {
            System.err.println("QueueBridge: Invalid ticket ID provided for payment update");
            return;
        }
        
        System.out.println("QueueBridge: Marking payment as paid for ticket: " + ticket);
        
        boolean foundInRecords = false;
        boolean incrementCounter = false;
        // Update persisted records
        for (int i = 0; i < records.size(); i++) {
            Object[] r = records.get(i);
            if (r != null && r.length > 5 && ticket.equals(String.valueOf(r[0]))) {
                String prev = String.valueOf(r[5]); // Payment is now at index 5
                if (!"Paid".equalsIgnoreCase(prev)) {
                    incrementCounter = true;
                }
                r[5] = "Paid";
                foundInRecords = true;
                System.out.println("QueueBridge: Updated payment status in records for ticket: " + ticket);
                break;
            }
        }
        if (incrementCounter) {
            totalPaidCount++;
        }
        
        if (!foundInRecords) {
            System.out.println("QueueBridge: Ticket not found in records: " + ticket);
        }
        
        // Update visible table
        if (model != null) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    boolean foundInTable = false;
                    for (int i = 0; i < model.getRowCount(); i++) {
                        Object v = model.getValueAt(i, 0);
                        if (ticket.equals(String.valueOf(v))) {
                            model.setValueAt("Paid", i, 4); // Payment column is still at index 4 in visible table
                            foundInTable = true;
                            System.out.println("QueueBridge: Updated payment status in table for ticket: " + ticket);
                            break;
                        }
                    }
                    if (!foundInTable) {
                        System.out.println("QueueBridge: Ticket not found in table: " + ticket);
                    }
                }
            });
        } else {
            System.out.println("QueueBridge: No table model registered, skipping table update");
        }
    }

    public static void removeTicket(final String ticket) {
        if (ticket == null) return;
        // Remove from persisted records
        for (int i = records.size() - 1; i >= 0; i--) {
            Object[] r = records.get(i);
            if (r != null && r.length > 0 && ticket.equals(String.valueOf(r[0]))) {
                records.remove(i);
                break;
            }
        }
        // Optionally remove from visible table if registered
        if (model != null) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    for (int i = 0; i < model.getRowCount(); i++) {
                        Object v = model.getValueAt(i, 0);
                        if (ticket.equals(String.valueOf(v))) {
                            model.removeRow(i);
                            break;
                        }
                    }
                }
            });
        }
    }
}
