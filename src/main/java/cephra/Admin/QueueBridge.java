package cephra.Admin;

import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public final class QueueBridge {
    private static DefaultTableModel model;
    private static final List<Object[]> records = new ArrayList<Object[]>();

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

    public static void markPaymentPaid(final String ticket) {
        if (ticket == null) return;
        // Update persisted records
        for (int i = 0; i < records.size(); i++) {
            Object[] r = records.get(i);
            if (r != null && r.length > 4 && ticket.equals(String.valueOf(r[0]))) {
                r[4] = "Paid";
                break;
            }
        }
        // Update visible table
        if (model != null) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    for (int i = 0; i < model.getRowCount(); i++) {
                        Object v = model.getValueAt(i, 0);
                        if (ticket.equals(String.valueOf(v))) {
                            model.setValueAt("Paid", i, 4);
                            break;
                        }
                    }
                }
            });
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
