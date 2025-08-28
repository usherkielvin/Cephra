package cephra.Admin;

import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class QueueBridge {
    private static DefaultTableModel model;
    private static final List<Object[]> records = new ArrayList<Object[]>();
    private static final Map<String, BatteryInfo> ticketBattery = new HashMap<String, BatteryInfo>();
    private static int totalPaidCount = 0;

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
                    // Insert saved records, newest first at top
                    for (int i = snapshot.size() - 1; i >= 0; i--) {
                        model.insertRow(0, snapshot.get(i));
                    }
                }
            });
        }
    }

    public static void addTicket(String ticket, String customer, String service, String status, String payment, String action) {
        final Object[] row = new Object[] { ticket, customer, service, status, payment, action };
        if (model == null) {
            records.add(0, row);
            return;
        }
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                model.insertRow(0, row);
            }
        });
        records.add(0, row);
    }

    public static void setTicketBatteryInfo(String ticket, int initialPercent, double capacityKWh) {
        if (ticket == null) return;
        ticketBattery.put(ticket, new BatteryInfo(initialPercent, capacityKWh));
    }

    public static BatteryInfo getTicketBatteryInfo(String ticket) {
        return ticketBattery.get(ticket);
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
        String serviceName = "";
        String customerName = "";
        
        // Update persisted records
        for (int i = 0; i < records.size(); i++) {
            Object[] r = records.get(i);
            if (r != null && r.length > 4 && ticket.equals(String.valueOf(r[0]))) {
                String prev = String.valueOf(r[4]);
                if (!"Paid".equalsIgnoreCase(prev)) {
                    incrementCounter = true;
                }
                r[4] = "Paid";
                foundInRecords = true;
                
                // Get service name and customer name for history entry
                if (r.length > 2) {
                    customerName = String.valueOf(r[1]);
                    serviceName = String.valueOf(r[2]);
                }
                
                System.out.println("QueueBridge: Updated payment status in records for ticket: " + ticket);
                break;
            }
        }
        if (incrementCounter) {
            totalPaidCount++;
        }
        
        if (!foundInRecords) {
            System.out.println("QueueBridge: Ticket not found in records: " + ticket);
        } else {
            // Add history entry for the user
            try {
                // Add history entry with 40 minutes charging time
                cephra.Phone.UserHistoryManager.addHistoryEntry(
                    customerName, 
                    ticket, 
                    serviceName, 
                    "40 mins"
                );
                System.out.println("QueueBridge: Added history entry for user: " + customerName);
            } catch (Throwable t) {
                System.err.println("QueueBridge: Error adding history entry: " + t.getMessage());
            }
        }
        
        // Update visible table
        if (model != null) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    boolean foundInTable = false;
                    for (int i = 0; i < model.getRowCount(); i++) {
                        Object v = model.getValueAt(i, 0);
                        if (ticket.equals(String.valueOf(v))) {
                            model.setValueAt("Paid", i, 4);
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
