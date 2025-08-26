package cephra.Admin;

import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public final class QueueBridge {
    private static DefaultTableModel model;

    private QueueBridge() {}

    public static void registerModel(DefaultTableModel m) {
        model = m;
    }

    public static void addTicket(String ticket, String customer, String service, String status, String payment, String action) {
        if (model == null) {
            return;
        }
        final Object[] row = new Object[] { ticket, customer, service, status, payment, action };
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // insert at top of the table
                model.insertRow(0, row);
            }
        });
    }
}
