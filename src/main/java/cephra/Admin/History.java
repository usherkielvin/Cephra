
package cephra.Admin;

//import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Window;

import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;


public class History extends javax.swing.JPanel {

    public History() {
        initComponents();
        setPreferredSize(new java.awt.Dimension(1000, 750));
        setSize(1000, 750);
        setupDateTimeTimer();
        
        // Add hover effects to navigation buttons
        ButtonHoverEffect.addHoverEffect(Baybutton);
        ButtonHoverEffect.addHoverEffect(staffbutton);
        ButtonHoverEffect.addHoverEffect(businessbutton);
        ButtonHoverEffect.addHoverEffect(quebutton);
        ButtonHoverEffect.addHoverEffect(exitlogin);
        
        // Register history table model so other modules can add rows
       /* jtableDesign.apply(jTable1);
        jtableDesign.makeScrollPaneTransparent(jScrollPane1);*/
        
        jtableDesign.apply(jTable1);
        jtableDesign.makeScrollPaneTransparent(jScrollPane1);
        jScrollPane1.setViewportView(jTable1);

        // Ensure vertical lines are hidden like staff table
        
        adminHistorySRCH.setOpaque(false);
        adminHistorySRCH.setBackground(new Color(0, 0, 0, 0));
        adminHistorySRCH.setBorder(null);
        
        adminHistorySRCH.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                    searchHistory();
                }
            }
        });
        
        // Disable vertical and horizontal scrollbars like staff table
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
  
        
        JTableHeader header = jTable1.getTableHeader();
        header.setFont(new Font("Sogie UI", Font.BOLD, 16));
        
        // Set column widths
        jTable1.getColumnModel().getColumn(0).setPreferredWidth(80);  // Ticket
        jTable1.getColumnModel().getColumn(1).setPreferredWidth(120); // Customer
        jTable1.getColumnModel().getColumn(2).setPreferredWidth(60);  // KWh
        jTable1.getColumnModel().getColumn(3).setPreferredWidth(80);  // Total
        jTable1.getColumnModel().getColumn(4).setPreferredWidth(100); // Served By
        jTable1.getColumnModel().getColumn(5).setPreferredWidth(100); // Date & Time (reduced for compact format)
        jTable1.getColumnModel().getColumn(6).setPreferredWidth(100); // Reference
        
        // Make table completely non-editable by overriding the table model
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        jTable1.setModel(new javax.swing.table.DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        });
        
        // Copy the column headers from the original model
        for (int i = 0; i < model.getColumnCount(); i++) {
            ((DefaultTableModel) jTable1.getModel()).addColumn(model.getColumnName(i));
        }
        
        // Register the new non-editable model with HistoryBridge
        cephra.Admin.HistoryBridge.registerModel((DefaultTableModel) jTable1.getModel());
        
    }

 
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        datetime = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        Baybutton = new javax.swing.JButton();
        staffbutton = new javax.swing.JButton();
        businessbutton = new javax.swing.JButton();
        exitlogin = new javax.swing.JButton();
        quebutton = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        labelStaff = new javax.swing.JLabel();
        adminHistorySRCH = new javax.swing.JTextField();
        HISTORYtEXT = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(1000, 750));
        setLayout(null);

        datetime.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        datetime.setForeground(new java.awt.Color(255, 255, 255));
        datetime.setText("10:44 AM 17 August, Sunday");
        add(datetime);
        datetime.setBounds(820, 40, 170, 20);

        jTable1.setBackground(new java.awt.Color(204, 204, 204));
        jTable1.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Ticket", "Customer", "KWh", "Total", "Served By", "Date & Time", "Reference"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS); // Disable horizontal scrolling
        jTable1.setAutoscrolls(true); // Enable auto-scrolling for mouse wheel
        jTable1.setFocusable(false);
        jTable1.setOpaque(false);
        // jTable1.setPreferredSize(new java.awt.Dimension(980, 550)); // Match scrollpane size - removed to allow proper scrolling
        jTable1.setRequestFocusEnabled(false);
        jTable1.setShowHorizontalLines(true);
        jTable1.getTableHeader().setResizingAllowed(false);
        jTable1.getTableHeader().setReorderingAllowed(false);
        jTable1.setVerifyInputWhenFocusTarget(false);
        jScrollPane1.setViewportView(jTable1);

        add(jScrollPane1);
        jScrollPane1.setBounds(10, 190, 980, 550);

        Baybutton.setForeground(new java.awt.Color(255, 255, 255));
        Baybutton.setText("BAYS");
        Baybutton.setBorder(null);
        Baybutton.setBorderPainted(false);
        Baybutton.setContentAreaFilled(false);
        Baybutton.setFocusPainted(false);
        Baybutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BaybuttonActionPerformed(evt);
            }
        });
        add(Baybutton);
        Baybutton.setBounds(385, 26, 33, 15);

        staffbutton.setForeground(new java.awt.Color(255, 255, 255));
        staffbutton.setText("STAFF RECORDS");
        staffbutton.setBorder(null);
        staffbutton.setBorderPainted(false);
        staffbutton.setContentAreaFilled(false);
        staffbutton.setFocusPainted(false);
        staffbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                staffbuttonActionPerformed(evt);
            }
        });
        add(staffbutton);
        staffbutton.setBounds(505, 26, 96, 15);

        businessbutton.setForeground(new java.awt.Color(255, 255, 255));
        businessbutton.setText("BUSINESS OVERVIEW");
        businessbutton.setBorder(null);
        businessbutton.setBorderPainted(false);
        businessbutton.setContentAreaFilled(false);
        businessbutton.setFocusPainted(false);
        businessbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                businessbuttonActionPerformed(evt);
            }
        });
        add(businessbutton);
        businessbutton.setBounds(617, 26, 136, 15);

        exitlogin.setBorder(null);
        exitlogin.setBorderPainted(false);
        exitlogin.setContentAreaFilled(false);
        exitlogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitloginActionPerformed(evt);
            }
        });
        add(exitlogin);
        exitlogin.setBounds(930, 0, 70, 60);

        quebutton.setForeground(new java.awt.Color(255, 255, 255));
        quebutton.setText("QUEUE LIST");
        quebutton.setBorder(null);
        quebutton.setBorderPainted(false);
        quebutton.setContentAreaFilled(false);
        quebutton.setFocusPainted(false);
        quebutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quebuttonActionPerformed(evt);
            }
        });
        add(quebutton);
        quebutton.setBounds(289, 26, 80, 15);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Hello,");
        add(jLabel3);
        jLabel3.setBounds(820, 10, 50, 30);

        labelStaff.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        labelStaff.setForeground(new java.awt.Color(255, 255, 255));
        labelStaff.setText("Admin!");
        add(labelStaff);
        labelStaff.setBounds(870, 10, 70, 30);
        add(adminHistorySRCH);
        adminHistorySRCH.setBounds(80, 140, 340, 30);

        HISTORYtEXT.setForeground(new java.awt.Color(4, 167, 182));
        HISTORYtEXT.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        HISTORYtEXT.setText("HISTORY");
        add(HISTORYtEXT);
        HISTORYtEXT.setBounds(433, 26, 57, 15);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/SHBOARD - HISTORY.png"))); // NOI18N
        add(jLabel1);
        jLabel1.setBounds(0, -10, 1000, 770);
    }// </editor-fold>//GEN-END:initComponents

    private void BaybuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BaybuttonActionPerformed
        Window w = SwingUtilities.getWindowAncestor(History.this);
        if (w instanceof cephra.Frame.Admin) {
            ((cephra.Frame.Admin) w).switchPanel(new BayManagement());
        }
    }//GEN-LAST:event_BaybuttonActionPerformed

    private void businessbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_businessbuttonActionPerformed
        Window w = SwingUtilities.getWindowAncestor(History.this);
        if (w instanceof cephra.Frame.Admin) {
            ((cephra.Frame.Admin) w).switchPanel(new cephra.Admin.Business_Overview());
        }
    }//GEN-LAST:event_businessbuttonActionPerformed

    private void staffbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_staffbuttonActionPerformed
         Window w = SwingUtilities.getWindowAncestor(History.this);
        if (w instanceof cephra.Frame.Admin) {
            ((cephra.Frame.Admin) w).switchPanel(new StaffRecord());
        }
    }//GEN-LAST:event_staffbuttonActionPerformed

    private void quebuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quebuttonActionPerformed
         Window w = SwingUtilities.getWindowAncestor(History.this);
        if (w instanceof cephra.Frame.Admin) {
            ((cephra.Frame.Admin) w).switchPanel(new Queue());
        }
    }//GEN-LAST:event_quebuttonActionPerformed

    private void exitloginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitloginActionPerformed
        Window w = SwingUtilities.getWindowAncestor(History.this);
        if (w instanceof cephra.Frame.Admin) {
            ((cephra.Frame.Admin) w).switchPanel(new cephra.Admin.Login());
        }
    }//GEN-LAST:event_exitloginActionPerformed

     private void setupDateTimeTimer() {
        updateDateTime();
        javax.swing.Timer timer = new javax.swing.Timer(1000, new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                updateDateTime();
            }
        });
        timer.start();
    }
    
    private void updateDateTime() {
        java.text.SimpleDateFormat timeFormat = new java.text.SimpleDateFormat("hh:mm a");
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("dd MMMM, EEEE");
        
        java.util.Date now = new java.util.Date();
        String time = timeFormat.format(now);
        String date = dateFormat.format(now);
        
        datetime.setText(time + " " + date);
    }

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Baybutton;
    private javax.swing.JLabel HISTORYtEXT;
    private javax.swing.JTextField adminHistorySRCH;
    private javax.swing.JButton businessbutton;
    private javax.swing.JLabel datetime;
    private javax.swing.JButton exitlogin;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel labelStaff;
    private javax.swing.JButton quebutton;
    private javax.swing.JButton staffbutton;
    // End of variables declaration//GEN-END:variables

    private void searchHistory() {
        String keyword = adminHistorySRCH.getText().trim().toLowerCase();
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);

        if (keyword.isEmpty()) {
            cephra.Admin.HistoryBridge.refreshHistoryTable();
            return;
        }

        // Get all history records from database for searching
        java.util.List<Object[]> allRecords = cephra.Database.CephraDB.getAllChargingHistory();
        for (Object[] record : allRecords) {
            boolean match = false;
            // Search through all fields in the record
            for (Object field : record) {
                if (field != null && field.toString().toLowerCase().contains(keyword)) {
                    match = true;
                    break;
                }
            }
            if (match) {
                // Convert database format to display format
                // Calculate kWh used based on battery levels
                int initialBatteryLevel = (Integer) record[3];
                int finalBatteryLevel = 100; // Always 100% when completed
                double batteryCapacityKWh = 40.0; // 40kWh capacity
                double usedFraction = (finalBatteryLevel - initialBatteryLevel) / 100.0;
                double kwhUsed = usedFraction * batteryCapacityKWh;

                // Get payment method from payment transactions table
                String paymentMethod = cephra.Database.CephraDB.getPaymentMethodForTicket((String) record[0]);
                if (paymentMethod == null) paymentMethod = "Cash"; // Default fallback

                // Get the admin username who served this transaction
                String servedBy = cephra.Database.CephraDB.getCurrentAdminUsername();
                if (servedBy == null || servedBy.trim().isEmpty()) {
                    servedBy = "Admin"; // Fallback if no admin logged in
                }

                // Convert database format to admin history format
                // Columns: Ticket, Customer, KWh, Total, Served By, Date & Time, Reference
                Object[] adminRecord = {
                    record[0], // ticket_id
                    record[1], // username
                    String.format("%.1f kWh", kwhUsed), // KWh used
                    String.format("%.2f", record[5]), // Total amount
                    servedBy + " (" + paymentMethod + ")", // served_by with payment method
                    formatDateTimeForDisplay(record[7]), // completed_at - format as 12-hour without seconds
                    record[6] // reference_number - compact format
                };
                model.addRow(adminRecord);
            }
        }
    }

    // Method to format timestamp for display (12-hour format without seconds)
    private static String formatDateTimeForDisplay(Object timestamp) {
        if (timestamp == null) {
            return "";
        }
        try {
            if (timestamp instanceof java.sql.Timestamp) {
                java.sql.Timestamp ts = (java.sql.Timestamp) timestamp;
                java.time.LocalDateTime ldt = ts.toLocalDateTime();
                return ldt.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a"));
            } else if (timestamp instanceof String) {
                // If it's already a formatted string, return as is
                return (String) timestamp;
            }
        } catch (Exception e) {
            System.err.println("Error formatting timestamp: " + e.getMessage());
        }
        return String.valueOf(timestamp);
    }
}
