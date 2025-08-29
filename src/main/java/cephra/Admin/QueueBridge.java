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
            final List<Object[]> snapshot = new ArrayList<>(records);
            SwingUtilities.invokeLater(() -> {
                model.setRowCount(0); // clear
                // Insert saved records (newest first) into visible table
                for (int i = snapshot.size() - 1; i >= 0; i--) {
                    Object[] fullRecord = snapshot.get(i);
                    Object[] visibleRow = toVisibleRow(fullRecord);
                    model.insertRow(0, visibleRow);
                }
            });
        }
    }

    /** Add a ticket with hidden random reference number */
    public static void addTicket(String ticket, String customer, String service, String status, String payment, String action) {
        String refNumber = String.valueOf(1000000 + random.nextInt(9000000)); // 7-digit ref #
        final Object[] fullRecord = new Object[] { ticket, refNumber, customer, service, status, payment, action };

        records.add(0, fullRecord);

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
                return String.valueOf(record[1]);
            }
        }
        return "";
    }

    /** Billing Calculations */
    public static double computeAmountDue(String ticket) {
        BatteryInfo info = ticketBattery.get(ticket);
        if (info == null) {
            info = new BatteryInfo(18, 40.0); // defaults
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

    /** Mark a payment as Paid and add history */
    public static void markPaymentPaid(final String ticket) {
        if (ticket == null || ticket.trim().isEmpty()) {
            System.err.println("QueueBridge: Invalid ticket ID");
            return;
        }

        boolean foundInRecords = false;
        boolean incrementCounter = false;
        String customerName = "";
        String serviceName = "";

        // Update in memory records
        for (Object[] r : records) {
            if (r != null && ticket.equals(String.valueOf(r[0]))) {
                String prev = String.valueOf(r[5]); // Payment is index 5
                if (!"Paid".equalsIgnoreCase(prev)) {
                    incrementCounter = true;
                }
                r[5] = "Paid";
                foundInRecords = true;
                customerName = String.valueOf(r[2]);
                serviceName = String.valueOf(r[3]);
                break;
            }
        }

        if (incrementCounter) totalPaidCount++;

        if (foundInRecords) {
            try {
                cephra.Phone.UserHistoryManager.addHistoryEntry(customerName, ticket, serviceName, "40 mins");
            } catch (Throwable t) {
                System.err.println("QueueBridge: Error adding history entry: " + t.getMessage());
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

    /** Remove a ticket from records and table */
    public static void removeTicket(final String ticket) {
        if (ticket == null) return;

        for (int i = records.size() - 1; i >= 0; i--) {
            Object[] r = records.get(i);
            if (r != null && ticket.equals(String.valueOf(r[0]))) {
                records.remove(i);
                break;
            }
        }

        if (model != null) {
            SwingUtilities.invokeLater(() -> {
                for (int i = 0; i < model.getRowCount(); i++) {
                    Object v = model.getValueAt(i, 0);
                    if (ticket.equals(String.valueOf(v))) {
                        model.removeRow(i);
                        break;
                    }
                }
            });
        }
    }
}
