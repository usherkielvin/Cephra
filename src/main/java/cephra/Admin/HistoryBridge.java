package cephra.Admin;

import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public final class HistoryBridge {
    private static DefaultTableModel model;
    private static final List<Object[]> records = new ArrayList<Object[]>();

    private HistoryBridge() {}

    public static void registerModel(DefaultTableModel m) {
        model = m;
        // Rebuild table from all persisted records
        if (model != null) {
            final List<Object[]> snapshotRecords = new ArrayList<Object[]>(records);
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    // Clear any template rows
                    model.setRowCount(0);
                    // Insert existing records, newest first at top
                    for (int i = snapshotRecords.size() - 1; i >= 0; i--) {
                        model.insertRow(0, snapshotRecords.get(i));
                    }
                }
            });
        }
    }

    public static void addRecord(final Object[] row) {
        if (model == null) {
            // save for later if history table not yet registered
            records.add(0, row);
        } else {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    // insert at top of the table
                    model.insertRow(0, row);
                }
            });
            records.add(0, row);
        }
    }
    
    public static List<Object[]> getRecordsForUser(String username) {
        if (username == null || username.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        List<Object[]> userRecords = new ArrayList<>();
        for (Object[] record : records) {
            if (record != null && record.length > 1 && username.equals(String.valueOf(record[1]))) {
                userRecords.add(record);
            }
        }
        
        return userRecords;
    }
}


