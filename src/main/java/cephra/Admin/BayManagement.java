package cephra.Admin;

import java.awt.*;
import javax.swing.*;

import cephra.Admin.Utilities.ButtonHoverEffect;

import java.awt.event.*;

public class BayManagement extends javax.swing.JPanel {
    // Static variables to track available bays - ALL SET TO AVAILABLE
    public static boolean[] fastChargingAvailable = {true, true, true};
    public static boolean[] normalChargingAvailable = {true, true, true, true, true};
    private static boolean toggleStatesLoaded = false;
    
    // Static variables to track occupied bays
    public static boolean[] fastChargingOccupied = {false, false, false};
    public static boolean[] normalChargingOccupied = {false, false, false, false, false};
    private static cephra.Admin.Queue queueInstance = null;
    private static cephra.Frame.Monitor monitorInstance = null;
    
    public static void registerQueueInstance(cephra.Admin.Queue queue) {
        queueInstance = queue;
    }
    
    public static void registerMonitorInstance(cephra.Frame.Monitor monitor) {
        monitorInstance = monitor;
    }
    
    public static void unregisterQueueInstance() {
        queueInstance = null;
    }
    
    public static void unregisterMonitorInstance() {
        monitorInstance = null;
    }
    
    private JToggleButton[] bayToggleButtons = new JToggleButton[8];
    
    public static boolean isFastChargingAvailable() {
        ensureChargingBaysExist();
        for (int i = 1; i <= 3; i++) {
            if (isBayAvailableForCharging(i)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isNormalChargingAvailable() {
        ensureChargingBaysExist();
        for (int i = 4; i <= 8; i++) {
            if (isBayAvailableForCharging(i)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isBayAvailableForCharging(int bayNumber) {
        try (java.sql.Connection conn = cephra.Database.DatabaseConnection.getConnection();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(
                 "SELECT cb.status, cb.current_ticket_id, " +
                 "       EXISTS (SELECT 1 FROM charging_grid cg WHERE cg.bay_number = cb.bay_number AND cg.ticket_id IS NOT NULL) AS has_grid_ticket " +
                 "FROM charging_bays cb WHERE cb.bay_number = ?")) {

            pstmt.setString(1, "Bay-" + bayNumber);
            try (java.sql.ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String status = rs.getString("status");
                    String ticketId = rs.getString("current_ticket_id");
                    boolean hasTicketInGrid = false;
                    try { hasTicketInGrid = rs.getBoolean("has_grid_ticket"); } catch (Exception ignore) {}

                    boolean inMaintenance = "Maintenance".equalsIgnoreCase(status);
                    boolean hasTicketInBay = ticketId != null && !ticketId.isEmpty();

                    return !inMaintenance && "Available".equalsIgnoreCase(status) && !hasTicketInBay && !hasTicketInGrid;
                }
            }
        } catch (Exception e) {
            System.err.println("Error checking bay availability for Bay-" + bayNumber + ": " + e.getMessage());
        }
        
        int index = bayNumber - 1;
        if (bayNumber >= 1 && bayNumber <= 3) {
            return fastChargingAvailable[index] && !fastChargingOccupied[index];
        } else if (bayNumber >= 4 && bayNumber <= 8) {
            return normalChargingAvailable[index - 3] && !normalChargingOccupied[index - 3];
        }
        return false;
    }
    
   //find next available bay
    public static int findNextAvailableBay(boolean isFastCharging) {
        if (isFastCharging) {
            // Check fast charging bays (1-3) - skip offline bays
            for (int i = 0; i < 3; i++) {
                int bayNumber = i + 1;
                if (isBayAvailableForCharging(bayNumber)) {
                    return bayNumber;
                } else {
                }
            }
        } else {
            // Check normal charging bays (4-8) - skip offline bays
            for (int i = 0; i < 5; i++) {
                int bayNumber = i + 4;
                if (isBayAvailableForCharging(bayNumber)) {
                    return bayNumber;
                } else {
                }
            }
        }
        return -1; // No available bay found
    }
    
    public static boolean assignTicketToBay(String ticketId, String username, int bayNumber) {
        try {
            // Check if bay is available
            if (!isBayAvailableForCharging(bayNumber)) {
                System.err.println("Bay " + bayNumber + " is not available for assignment");
                return false;
            }
            
            // Update bay occupation status
            if (bayNumber >= 1 && bayNumber <= 3) {
                fastChargingOccupied[bayNumber - 1] = true;
            } else if (bayNumber >= 4 && bayNumber <= 8) {
                normalChargingOccupied[bayNumber - 4] = true;
            }
            
            // Update database
            updateBayAssignmentInDatabase(ticketId, username, bayNumber);
            
            System.out.println("Ticket " + ticketId + " assigned to Bay-" + bayNumber + " for user " + username);
            return true;
            
        } catch (Exception e) {
            System.out.println("Error assigning ticket to bay: " + e.getMessage());
            return false;
        }
    }
    
    
    public static boolean releaseBay(int bayNumber) {
        try {
            // Update bay occupation status
            if (bayNumber >= 1 && bayNumber <= 3) {
                fastChargingOccupied[bayNumber - 1] = false;
            } else if (bayNumber >= 4 && bayNumber <= 8) {
                normalChargingOccupied[bayNumber - 4] = false;
            }
            
            // Update database
            updateBayReleaseInDatabase(bayNumber);
            
            return true;
            
        } catch (Exception e) {
            System.err.println("Error releasing bay: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public static void loadToggleStates() {
        // Only load from database ONCE - never reload
        if (toggleStatesLoaded) {
            return;
        }
        
        try {
            // Create bay_toggle_states table if it doesn't exist
            createToggleStatesTable();
            
            // Load toggle states from database
            loadToggleStatesFromDatabase();
            
            toggleStatesLoaded = true;
            
        } catch (Exception e) {
            System.out.println("Error loading toggle states: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void saveToggleStates() {
        try {
            // Save toggle states to database
            saveToggleStatesToDatabase();
            
            System.out.println("Fast Charging: " + countAvailableBays(true) + "/3");
            System.out.println("Normal Charging: " + countAvailableBays(false) + "/5");
            
        } catch (Exception e) {
            System.out.println("Error saving toggle states: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @SuppressWarnings("unused")
    private static boolean areToggleStatesLoaded() {
        return toggleStatesLoaded;
    }
   
    private void forceToggleButtonsToMatchStaticArrays() {
        if (bayToggleButtons != null) {
            for (int i = 0; i < 8; i++) {
                if (bayToggleButtons[i] != null) {
                    boolean isAvailable = (i < 3) ? fastChargingAvailable[i] : normalChargingAvailable[i - 3];
                    bayToggleButtons[i].setSelected(isAvailable);
                }
            }
        }
    }   
  
    public BayManagement() {
        initComponents();
        setPreferredSize(new java.awt.Dimension(1000, 750));
        setSize(1000, 750);
        setupDateTimeTimer();
        
        // Keep nav button hover effects
        ButtonHoverEffect.addHoverEffect(quebutton);
        ButtonHoverEffect.addHoverEffect(staffbutton);
        ButtonHoverEffect.addHoverEffect(businessbutton);
        ButtonHoverEffect.addHoverEffect(historybutton);
        
        // Load toggle states from database ONLY ONCE - never reload
        loadToggleStates();
        
        // Force toggle buttons to match current static array values
        forceToggleButtonsToMatchStaticArrays();
        
        // Load bay occupation status from database
        loadBayOccupationFromDatabase();
        
        // Recover tickets that may have been left in an inconsistent state (app closed during payment)
        recoverStalePendingPaymentTickets();
        
        // Initialize grid displays with maintenance status
        initializeGridDisplays();
        
        // Setup iOS toggles with database states
        setupIOSToggles();
          
        // Update bay labels to match database states (permanent)
        SwingUtilities.invokeLater(() -> {
            updateAllBayLabelsFromDatabase();
        });

        // Compatibility hooks removed: legacy no-op calls eliminated
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        toggle7 = new javax.swing.JToggleButton();
        toggle6 = new javax.swing.JToggleButton();
        toggle5 = new javax.swing.JToggleButton();
        toggle4 = new javax.swing.JToggleButton();
        toggle3 = new javax.swing.JToggleButton();
        toggle2 = new javax.swing.JToggleButton();
        toggle8 = new javax.swing.JToggleButton();
        toggle1 = new javax.swing.JToggleButton();
        bay8 = new javax.swing.JLabel();
        bay7 = new javax.swing.JLabel();
        bay6 = new javax.swing.JLabel();
        bay5 = new javax.swing.JLabel();
        bay4 = new javax.swing.JLabel();
        bay3 = new javax.swing.JLabel();
        bay2 = new javax.swing.JLabel();
        bay1 = new javax.swing.JLabel();
        datetime = new javax.swing.JLabel();
        quebutton = new javax.swing.JButton();
        staffbutton = new javax.swing.JButton();
        businessbutton = new javax.swing.JButton();
        historybutton = new javax.swing.JButton();
        exitlogin = new javax.swing.JButton();
        labelStaff = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setLayout(null);
        add(toggle7);
        toggle7.setBounds(260, 650, 60, 30);

        toggle6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggle6ActionPerformed(evt);
            }
        });
        add(toggle6);
        toggle6.setBounds(860, 460, 60, 30);
        add(toggle5);
        toggle5.setBounds(560, 460, 60, 30);

        toggle4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggle4ActionPerformed(evt);
            }
        });
        add(toggle4);
        toggle4.setBounds(260, 460, 60, 30);
        add(toggle3);
        toggle3.setBounds(860, 270, 60, 30);
        add(toggle2);
        toggle2.setBounds(560, 270, 60, 30);
        add(toggle8);
        toggle8.setBounds(560, 650, 60, 30);
        add(toggle1);
        toggle1.setBounds(260, 270, 60, 30);

        bay8.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        bay8.setText("Available");
        add(bay8);
        bay8.setBounds(400, 645, 150, 32);

        bay7.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        bay7.setText("Available");
        add(bay7);
        bay7.setBounds(100, 645, 150, 32);

        bay6.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        bay6.setText("Available");
        add(bay6);
        bay6.setBounds(720, 460, 150, 32);

        bay5.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        bay5.setText("Available");
        add(bay5);
        bay5.setBounds(400, 460, 150, 32);

        bay4.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        bay4.setText("Available");
        add(bay4);
        bay4.setBounds(100, 460, 150, 32);

        bay3.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        bay3.setText("Available");
        add(bay3);
        bay3.setBounds(730, 270, 150, 32);

        bay2.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        bay2.setText("Available");
        add(bay2);
        bay2.setBounds(410, 270, 150, 32);

        bay1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        bay1.setText("Available");
        add(bay1);
        bay1.setBounds(100, 270, 150, 32);

        datetime.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        datetime.setForeground(new java.awt.Color(255, 255, 255));
        datetime.setText("10:44 AM 17 August, Sunday");
        add(datetime);
        datetime.setBounds(820, 40, 170, 20);

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

        historybutton.setForeground(new java.awt.Color(255, 255, 255));
        historybutton.setText("HISTORY");
        historybutton.setBorder(null);
        historybutton.setBorderPainted(false);
        historybutton.setContentAreaFilled(false);
        historybutton.setFocusPainted(false);
        historybutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                historybuttonActionPerformed(evt);
            }
        });
        add(historybutton);
        historybutton.setBounds(433, 26, 57, 15);

        exitlogin.setBorder(null);
        exitlogin.setBorderPainted(false);
        exitlogin.setContentAreaFilled(false);
        exitlogin.setFocusPainted(false);
        exitlogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitloginActionPerformed(evt);
            }
        });
        add(exitlogin);
        exitlogin.setBounds(930, 0, 70, 60);

        labelStaff.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        labelStaff.setForeground(new java.awt.Color(255, 255, 255));
        // Set the staff first name instead of "Admin!"
        String firstName = getStaffFirstNameFromDB();
        labelStaff.setText(firstName + "!");
        add(labelStaff);
        labelStaff.setBounds(870, 10, 70, 30);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Hello,");
        add(jLabel3);
        jLabel3.setBounds(820, 10, 50, 30);

        jLabel2.setForeground(new java.awt.Color(4, 167, 182));
        jLabel2.setText("BAYS");
        add(jLabel2);
        jLabel2.setBounds(385, 26, 33, 15);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/Bay.png"))); // NOI18N
        add(jLabel1);
        jLabel1.setBounds(0, 0, 1000, 750);
    }// </editor-fold>//GEN-END:initComponents

    private void toggle4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toggle4ActionPerformed
      
    }//GEN-LAST:event_toggle4ActionPerformed

    private void toggle6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toggle6ActionPerformed
        
    }//GEN-LAST:event_toggle6ActionPerformed

    private void quebuttonActionPerformed(java.awt.event.ActionEvent evt) {
        Window w = SwingUtilities.getWindowAncestor(BayManagement.this);
        if (w instanceof cephra.Frame.Admin) {
            ((cephra.Frame.Admin) w).switchPanel(new Queue());
        }
    }

    private void staffbuttonActionPerformed(java.awt.event.ActionEvent evt) {
        Window w = SwingUtilities.getWindowAncestor(BayManagement.this);
        if (w instanceof cephra.Frame.Admin) {
            ((cephra.Frame.Admin) w).switchPanel(new StaffRecord());
        }
    }

    private void businessbuttonActionPerformed(java.awt.event.ActionEvent evt) {
        Window w = SwingUtilities.getWindowAncestor(BayManagement.this);
        if (w instanceof cephra.Frame.Admin) {
            ((cephra.Frame.Admin) w).switchPanel(new cephra.Admin.Business_Overview());
        }
    }

    private void historybuttonActionPerformed(java.awt.event.ActionEvent evt) {
        Window w = SwingUtilities.getWindowAncestor(BayManagement.this);
        if (w instanceof cephra.Frame.Admin) {
            ((cephra.Frame.Admin) w).switchPanel(new History());
        }
    }

    private void exitloginActionPerformed(java.awt.event.ActionEvent evt) {
        Window w = SwingUtilities.getWindowAncestor(BayManagement.this);
        if (w instanceof cephra.Frame.Admin) {
            ((cephra.Frame.Admin) w).switchPanel(new cephra.Admin.Login());
        }
    }

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
    private javax.swing.JLabel bay1;
    private javax.swing.JLabel bay2;
    private javax.swing.JLabel bay3;
    private javax.swing.JLabel bay4;
    private javax.swing.JLabel bay5;
    private javax.swing.JLabel bay6;
    private javax.swing.JLabel bay7;
    private javax.swing.JLabel bay8;
    private javax.swing.JButton businessbutton;
    private javax.swing.JLabel datetime;
    private javax.swing.JButton exitlogin;
    private javax.swing.JButton historybutton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel labelStaff;
    private javax.swing.JButton quebutton;
    private javax.swing.JButton staffbutton;
    private javax.swing.JToggleButton toggle1;
    private javax.swing.JToggleButton toggle2;
    private javax.swing.JToggleButton toggle3;
    private javax.swing.JToggleButton toggle4;
    private javax.swing.JToggleButton toggle5;
    private javax.swing.JToggleButton toggle6;
    private javax.swing.JToggleButton toggle7;
    private javax.swing.JToggleButton toggle8;
    // End of variables declaration//GEN-END:variables
    
    
    private String getStaffFirstNameFromDB() {
        java.awt.Window window = javax.swing.SwingUtilities.getWindowAncestor(this);
        if (window instanceof cephra.Frame.Admin) {
            try {
                java.lang.reflect.Field field = window.getClass().getDeclaredField("loggedInUsername");
                field.setAccessible(true);
                String username = (String) field.get(window);
                if (username != null && !username.isEmpty()) {
                    return cephra.Database.CephraDB.getStaffFirstName(username);
                }
            } catch (Exception e) {
                System.err.println("Error getting staff first name: " + e.getMessage());
            }
        }
        return "Admin";
    }
    
    //setup ios toggles
    private void setupIOSToggles() {

        bayToggleButtons[0] = toggle1;
        bayToggleButtons[1] = toggle2;
        bayToggleButtons[2] = toggle3;
        bayToggleButtons[3] = toggle4;
        bayToggleButtons[4] = toggle5;
        bayToggleButtons[5] = toggle6;
        bayToggleButtons[6] = toggle7;
        bayToggleButtons[7] = toggle8;
        
        for (int i = 0; i < 8; i++) {
            final int bayIndex = i;
            JToggleButton toggle = bayToggleButtons[i];
            
           
            if (i < 3) { // Fast charging bays
                toggle.setSelected(fastChargingAvailable[i]);
            } else { // Normal charging bays
                toggle.setSelected(normalChargingAvailable[i - 3]);
            }
            
            // Apply iOS-style customization
            toggle.setBorderPainted(false);
            toggle.setFocusPainted(false);
            toggle.setContentAreaFilled(false);
            toggle.setOpaque(false);
            toggle.setUI(new IOSToggleUI(i < 3));
            
            JLabel bayLabel = getBayLabel(bayIndex + 1);
            if (bayLabel != null) {
                boolean isAvailable = (i < 3) ? fastChargingAvailable[i] : normalChargingAvailable[i - 3];
                updateBayLabel(bayLabel, isAvailable);
            }
            
            toggle.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JToggleButton source = (JToggleButton) e.getSource();
                    boolean isSelected = source.isSelected();
                    
                    // Update availability arrays
                    if (bayIndex < 3) {
                        fastChargingAvailable[bayIndex] = isSelected;
                        JLabel bayLabel = getBayLabel(bayIndex + 1);
                        if (bayLabel != null) {
                            updateBayLabel(bayLabel, isSelected);
                        }
                    } else {
                        normalChargingAvailable[bayIndex - 3] = isSelected;
                        JLabel bayLabel = getBayLabel(bayIndex + 1);
                        if (bayLabel != null) {
                            updateBayLabel(bayLabel, isSelected);
                        }
                    }
                    
                    // Update bay status in database
                    int bayNumber = bayIndex + 1;
                    String status = isSelected ? "Available" : "Maintenance";
                    updateBayStatusInDatabase(bayNumber, status);
                    
                    // Save to database immediately
                    saveToggleStates();
                    
                    System.out.println("Bay-" + bayNumber + " = " + (isSelected ? "Available" : "Maintenance"));
                    
                    // Update all grid displays with new maintenance status
                    updateAllBayGridDisplays();
                    
                    // Do not auto-assign on availability; keep tickets visible in waiting grid until admin proceeds
                }
            });
        }
    }   
    
    private JLabel getBayLabel(int bayNumber) {
        switch (bayNumber) {
            case 1: return bay1;
            case 2: return bay2;
            case 3: return bay3;
            case 4: return bay4;
            case 5: return bay5;
            case 6: return bay6;
            case 7: return bay7;
            case 8: return bay8;
            default: return null;
        }
    }
    
    private void updateBayLabel(JLabel label, boolean isAvailable) {
        if (isAvailable) {
            label.setText("Available");
            label.setForeground(new java.awt.Color(0, 128, 0));
        } else {
            label.setText("Unavailable");
            label.setForeground(new java.awt.Color(255, 0, 0));
        }
    }
    
    
    private void updateAllBayLabelsFromDatabase() {
        try {
            // Update fast charging bay labels (1-3)
            for (int i = 0; i < 3; i++) {
                JLabel bayLabel = getBayLabel(i + 1);
                if (bayLabel != null) {
                    updateBayLabel(bayLabel, fastChargingAvailable[i]);
                }
            }
            
            // Update normal charging bay labels (4-8)
            for (int i = 0; i < 5; i++) {
                JLabel bayLabel = getBayLabel(i + 4);
                if (bayLabel != null) {
                    updateBayLabel(bayLabel, normalChargingAvailable[i]);
                }
            }
            
            
        } catch (Exception e) {
            System.err.println("Error updating bay labels from database: " + e.getMessage());
            e.printStackTrace();
        }
    }  
    public static void clearWaitingGrid() {
        try (java.sql.Connection conn = cephra.Database.DatabaseConnection.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            stmt.execute("UPDATE waiting_grid SET ticket_id = NULL, username = NULL, service_type = NULL, initial_battery_level = NULL, position_in_queue = NULL");
        } catch (Exception e) {
            System.err.println("Error clearing waiting grid: " + e.getMessage());
        }
    }
 
    public static class IOSToggleUI extends javax.swing.plaf.basic.BasicToggleButtonUI {
        private Color onColor;
        private Color offColor = new Color(142, 142, 147); // iOS gray
        private Color thumbColor = Color.WHITE;
        
        public IOSToggleUI(boolean isFastCharging) {
            if (isFastCharging) {
                this.onColor = new Color(52, 199, 89); // iOS green for bays 1-3
            } else {
                this.onColor = new Color(0, 122, 255); // iOS blue for bays 4-8
            }
        }
        
        @Override
        public void paint(Graphics g, JComponent c) {
            JToggleButton button = (JToggleButton) c;
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int width = button.getWidth();
            int height = button.getHeight();
            
            // Background track
            Color trackColor = button.isSelected() ? onColor : offColor;
            g2d.setColor(trackColor);
            g2d.fillRoundRect(0, 0, width, height, height, height);
            
            // Thumb circle
            int thumbSize = height - 4;
            int thumbX = button.isSelected() ? width - thumbSize - 2 : 2;
            int thumbY = 2;
            
            // Thumb shadow
            g2d.setColor(new Color(0, 0, 0, 30));
            g2d.fillOval(thumbX + 1, thumbY + 1, thumbSize, thumbSize);
            
            // Thumb
            g2d.setColor(thumbColor);
            g2d.fillOval(thumbX, thumbY, thumbSize, thumbSize);
            
            g2d.dispose();
        }
    }
    
    private static void createToggleStatesTable() {
        try (java.sql.Connection conn = cephra.Database.DatabaseConnection.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            
            String createTableSQL = """
                CREATE TABLE IF NOT EXISTS bay_toggle_states (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    bay_number INT NOT NULL UNIQUE,
                    is_available BOOLEAN NOT NULL DEFAULT TRUE,
                    bay_type VARCHAR(20) NOT NULL,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """;
            
            stmt.execute(createTableSQL);
            
            // Insert initial toggle states if they don't exist
            for (int i = 1; i <= 8; i++) {
                String bayType = (i <= 3) ? "Fast" : "Normal";
                String insertSQL = String.format("""
                    INSERT IGNORE INTO bay_toggle_states (bay_number, is_available, bay_type) 
                    VALUES (%d, TRUE, '%s')
                """, i, bayType);
                stmt.execute(insertSQL);
            }
            
            
        } catch (Exception e) {
            System.err.println("Error creating toggle states table: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void loadToggleStatesFromDatabase() {
        try (java.sql.Connection conn = cephra.Database.DatabaseConnection.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            
            // First, try to load from bay_toggle_states table
            try (java.sql.ResultSet rs = stmt.executeQuery("SELECT bay_number, is_available FROM bay_toggle_states ORDER BY bay_number")) {
                boolean hasToggleStates = false;
                
                while (rs.next()) {
                    hasToggleStates = true;
                    int bayNumber = rs.getInt("bay_number");
                    boolean isAvailable = rs.getBoolean("is_available");
                    
                    
                    // Update the static arrays
                    if (bayNumber >= 1 && bayNumber <= 3) {
                        fastChargingAvailable[bayNumber - 1] = isAvailable;
                    } else if (bayNumber >= 4 && bayNumber <= 8) {
                        normalChargingAvailable[bayNumber - 4] = isAvailable;
                    }
                }
                
                if (hasToggleStates) {
                    toggleStatesLoaded = true;
                    return;
                }
            }
            
            // If no toggle states found, load from charging_bays table
            try (java.sql.ResultSet rs = stmt.executeQuery("SELECT bay_number, status FROM charging_bays ORDER BY bay_number")) {
                while (rs.next()) {
                    String bayNumberStr = rs.getString("bay_number");
                    String status = rs.getString("status");
                    
                    // Extract bay number from "Bay-X" format
                    int bayNumber = Integer.parseInt(bayNumberStr.replace("Bay-", ""));
                    boolean isAvailable = !"Maintenance".equals(status);
                    
                    // Update the static arrays
                    if (bayNumber >= 1 && bayNumber <= 3) {
                        fastChargingAvailable[bayNumber - 1] = isAvailable;
                    } else if (bayNumber >= 4 && bayNumber <= 8) {
                        normalChargingAvailable[bayNumber - 4] = isAvailable;
                    }
                }
                
                toggleStatesLoaded = true;
            }
            
        } catch (Exception e) {
            System.out.println("Error loading toggle states from database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    //save toggle states to the database
    private static void saveToggleStatesToDatabase() {
        try (java.sql.Connection conn = cephra.Database.DatabaseConnection.getConnection()) {
            
            // Update bay_toggle_states table
            try (java.sql.PreparedStatement pstmt = conn.prepareStatement(
                    "UPDATE bay_toggle_states SET is_available = ? WHERE bay_number = ?")) {
                
                // Save fast charging bays (1-3)
                for (int i = 0; i < fastChargingAvailable.length; i++) {
                    pstmt.setBoolean(1, fastChargingAvailable[i]);
                    pstmt.setInt(2, i + 1);
                    pstmt.executeUpdate();
                }
                
                // Save normal charging bays (4-8)
                for (int i = 0; i < normalChargingAvailable.length; i++) {
                    pstmt.setBoolean(1, normalChargingAvailable[i]);
                    pstmt.setInt(2, i + 4);
                    pstmt.executeUpdate();
                }
            }
            
            // Update charging_bays table status
            try (java.sql.PreparedStatement pstmt = conn.prepareStatement(
                    "UPDATE charging_bays SET status = ? WHERE bay_number = ?")) {
                
                // Update fast charging bays (1-3)
                for (int i = 0; i < fastChargingAvailable.length; i++) {
                    String status = fastChargingAvailable[i] ? "Available" : "Maintenance";
                    pstmt.setString(1, status);
                    pstmt.setString(2, "Bay-" + (i + 1));
                    pstmt.executeUpdate();
                }
                
                // Update normal charging bays (4-8)
                for (int i = 0; i < normalChargingAvailable.length; i++) {
                    String status = normalChargingAvailable[i] ? "Available" : "Maintenance";
                    pstmt.setString(1, status);
                    pstmt.setString(2, "Bay-" + (i + 4));
                    pstmt.executeUpdate();
                }
            }
            
            
        } catch (Exception e) {
            System.err.println("Error saving toggle states to database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    //update bay assignment in the database
    private static void updateBayAssignmentInDatabase(String ticketId, String username, int bayNumber) {
        try (java.sql.Connection conn = cephra.Database.DatabaseConnection.getConnection();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(
                 "UPDATE charging_bays SET status = 'Occupied', current_ticket_id = ?, current_username = ?, start_time = NOW() WHERE bay_number = ?")) {
            
            pstmt.setString(1, ticketId);
            pstmt.setString(2, username);
            pstmt.setString(3, "Bay-" + bayNumber);
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Bay-" + bayNumber + " assigned to ticket " + ticketId + " in database");
            } else {
                System.out.println("Failed to update bay assignment in database");
            }
            
        } catch (Exception e) {
            System.out.println("Error updating bay assignment in database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Updates bay release in the database
     */
    private static void updateBayReleaseInDatabase(int bayNumber) {
        try (java.sql.Connection conn = cephra.Database.DatabaseConnection.getConnection();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(
                 "UPDATE charging_bays SET status = 'Available', current_ticket_id = NULL, current_username = NULL, start_time = NULL WHERE bay_number = ?")) {
            
            pstmt.setString(1, "Bay-" + bayNumber);
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Bay-" + bayNumber + " released in database");
            } else {
                System.out.println("Failed to update bay release in database");
            }
            
        } catch (Exception e) {
            System.out.println("Error updating bay release in database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    //assign ticket from queue
    public static int assignTicketFromQueue(String ticketId, String username, boolean isFastCharging) {
        try {
            // Find the next available bay
            int bayNumber = findNextAvailableBay(isFastCharging);
            
            if (bayNumber == -1) {
                return -1;
            }
            
            // Assign the ticket to the bay
            boolean success = assignTicketToBay(ticketId, username, bayNumber);
            
            if (success) {
                System.out.println("Ticket " + ticketId + " assigned to Bay-" + bayNumber + " from queue");
                return bayNumber;
            } else {
                System.out.println("Failed to assign ticket " + ticketId + " to Bay-" + bayNumber);
                return -1;
            }
            
        } catch (Exception e) {
            System.out.println("Error in queue flow assignment: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }
    //get ticket bay assignment
    public static int getTicketBayAssignment(String ticketId) {
        try (java.sql.Connection conn = cephra.Database.DatabaseConnection.getConnection();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(
                 "SELECT bay_number FROM charging_bays WHERE current_ticket_id = ?")) {
            
            pstmt.setString(1, ticketId);
            try (java.sql.ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String bayNumberStr = rs.getString("bay_number");
                    return Integer.parseInt(bayNumberStr.replace("Bay-", ""));
                }
            }
            
        } catch (Exception e) {
            System.out.println("Error getting ticket bay assignment: " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }
    
    public static String getBayUser(int bayNumber) {
        try (java.sql.Connection conn = cephra.Database.DatabaseConnection.getConnection();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(
                 "SELECT current_username FROM charging_bays WHERE bay_number = ?")) {
            
            pstmt.setString(1, "Bay-" + bayNumber);
            try (java.sql.ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("current_username");
                }
            }
            
        } catch (Exception e) {
            System.out.println("Error getting bay user: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    public static String getBayTicket(int bayNumber) {
        try (java.sql.Connection conn = cephra.Database.DatabaseConnection.getConnection();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(
                 "SELECT current_ticket_id FROM charging_bays WHERE bay_number = ?")) {
            
            pstmt.setString(1, "Bay-" + bayNumber);
            try (java.sql.ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("current_ticket_id");
                }
            }
            
        } catch (Exception e) {
            System.out.println("Error getting bay ticket: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    public static String getBayDisplayText(int bayNumber) {
        if (!isBayAvailableForCharging(bayNumber)) {
            return "MAINTENANCE";
        }
        
        String ticket = getBayTicket(bayNumber);
        return ticket != null ? ticket : "";
    }
    
  
    public static java.awt.Color getBayDisplayColor(int bayNumber) {
        if (!isBayAvailableForCharging(bayNumber)) {
            return java.awt.Color.RED; // Red for maintenance
        }
        
        String ticket = getBayTicket(bayNumber);
        if (ticket != null && !ticket.isEmpty()) {
            // Priority tickets (FCHP, NCHP) show in red
            if (ticket.startsWith("FCHP") || ticket.startsWith("NCHP")) {
                return java.awt.Color.RED;
            }
            return java.awt.Color.GREEN; // Green for occupied
        }
        
        return java.awt.Color.GRAY; // Gray for available
    }
    public static boolean isBayGridSlotVisible(int bayNumber) {
        // Always show the slot, but with different styling for maintenance
        return true;
    }
    
    public static String getBayStatusText(int bayNumber) {
        if (!isBayAvailableForCharging(bayNumber)) {
            return "Maintenance";
        }
        
        String ticket = getBayTicket(bayNumber);
        if (ticket != null && !ticket.isEmpty()) {
            return "Occupied";
        }
        
        return "Available";
    }
    
    public static void updateAllBayGridDisplays() {
        notifyGridDisplayUpdate();
    }
    
    private static void notifyGridDisplayUpdate() {
        try {
            // Update Queue instance if registered
            if (queueInstance != null) {
                queueInstance.refreshGridDisplays();
                queueInstance.refreshWaitingGrid();
            }
            
            // Update Monitor instance if registered
            if (monitorInstance != null) {
                monitorInstance.refreshGridDisplays();
            }
            
        } catch (Exception e) {
            System.out.println("Error updating grid displays: " + e.getMessage());
            e.printStackTrace();
        }
    }
   
    public static String getGridSlotText(int bayNumber) {
        try {
            
            // Read directly from charging_grid table for real-time ticket status
            try (java.sql.Connection conn = cephra.Database.DatabaseConnection.getConnection();
                 java.sql.PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT cg.ticket_id, cb.status FROM charging_grid cg LEFT JOIN charging_bays cb ON cg.bay_number = cb.bay_number WHERE cg.bay_number = ?")) {
                
                pstmt.setString(1, "Bay-" + bayNumber);
                try (java.sql.ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        String ticketId = rs.getString("ticket_id");
                        String status = rs.getString("status");
                        
                        
                        // Check if bay is in maintenance
                        if ("Maintenance".equals(status)) {
                            return "OFFLINE";
                        }
                        
                        // Check if bay has an assigned ticket
                        if (ticketId != null && !ticketId.isEmpty()) {
                            return ticketId;
                        }
                        
                        // Bay is available but empty - return empty string for grid display
                        // The UI will handle showing/hiding the button based on this
                        return "";
                    } else {
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error reading bay status from database for Bay-" + bayNumber + ": " + e.getMessage());
            e.printStackTrace();
        }
        
        // Fallback to static array check
        if (!isBayAvailableForCharging(bayNumber)) {
            return "OFFLINE";
        }
        
        return "";
    }
    
    public static java.awt.Color getGridSlotColor(int bayNumber) {
        try {
            // Read directly from database for real-time status
            try (java.sql.Connection conn = cephra.Database.DatabaseConnection.getConnection();
                 java.sql.PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT cb.status, cg.ticket_id, cg.service_type " +
                     "FROM charging_bays cb LEFT JOIN charging_grid cg ON cb.bay_number = cg.bay_number " +
                     "WHERE cb.bay_number = ?")) {

                pstmt.setString(1, "Bay-" + bayNumber);
                try (java.sql.ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        String status = rs.getString("status");
                        String ticketId = rs.getString("ticket_id");
                        String serviceType = rs.getString("service_type");

                        // Maintenance always red
                        if ("Maintenance".equals(status)) {
                            return java.awt.Color.RED;
                        }

                        // Occupied coloring based on service type: Priority tickets -> red, FCH -> green, NCH -> blue
                        if (ticketId != null && !ticketId.isEmpty()) {
                            // Priority tickets (FCHP, NCHP) always show in red
                            if (ticketId.startsWith("FCHP") || ticketId.startsWith("NCHP")) {
                                return java.awt.Color.RED;
                            }
                            
                            if (serviceType != null) {
                                if (serviceType.equalsIgnoreCase("Fast") || ticketId.startsWith("FCH")) {
                                    return new java.awt.Color(0, 147, 73); // Green for Fast charging (bays 1-3)
                                }
                                if (serviceType.equalsIgnoreCase("Normal") || ticketId.startsWith("NCH")) {
                                    return new java.awt.Color(22, 130, 146); // Blue for Normal charging (bays 4-8)
                                }
                            }
                            // default occupied color
                            return new java.awt.Color(0, 147, 73); // Green for Fast charging (bays 1-3)
                        }

                        // Available/empty
                        return java.awt.Color.GRAY;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error reading bay color from database: " + e.getMessage());
            e.printStackTrace();
        }

        // Fallback to static array check
        if (!isBayAvailableForCharging(bayNumber)) {
            return java.awt.Color.RED; // Red for offline
        }

        return java.awt.Color.GRAY; // Gray for available
    }

    public static String[] getFastChargingGridTexts() {
        // Force refresh from database before returning
        loadMaintenanceStatusFromDatabase();
        
        String[] texts = new String[3];
        for (int i = 0; i < 3; i++) {
            texts[i] = getGridSlotText(i + 1); // Bay-1, Bay-2, Bay-3
        }
        
        
        return texts;
    }
    
    public static String[] getNormalChargingGridTexts() {
        // Force refresh from database before returning
        loadMaintenanceStatusFromDatabase();
        
        String[] texts = new String[5];
        for (int i = 0; i < 5; i++) {
            texts[i] = getGridSlotText(i + 4); // Bay-4, Bay-5, Bay-6, Bay-7, Bay-8
        }      
        
        return texts;
    }   

    public static java.awt.Color[] getFastChargingGridColors() {
        // Force refresh from database before returning
        loadMaintenanceStatusFromDatabase();
        
        java.awt.Color[] colors = new java.awt.Color[3];
        for (int i = 0; i < 3; i++) {
            colors[i] = getGridSlotColor(i + 1); // Bay-1, Bay-2, Bay-3
        }
        return colors;
    }
    
    public static java.awt.Color[] getNormalChargingGridColors() {
        // Force refresh from database before returning
        loadMaintenanceStatusFromDatabase();
        
        java.awt.Color[] colors = new java.awt.Color[5];
        for (int i = 0; i < 5; i++) {
            colors[i] = getGridSlotColor(i + 4); // Bay-4, Bay-5, Bay-6, Bay-7, Bay-8
        }
        return colors;
    }
    
    public static void loadBayOccupationFromDatabase() {
        try (java.sql.Connection conn = cephra.Database.DatabaseConnection.getConnection();
             java.sql.Statement stmt = conn.createStatement();
             java.sql.ResultSet rs = stmt.executeQuery("SELECT bay_number, status, current_ticket_id, current_username FROM charging_bays ORDER BY bay_number")) {
            
            while (rs.next()) {
                String bayNumberStr = rs.getString("bay_number");
                String status = rs.getString("status");
                String currentTicketId = rs.getString("current_ticket_id");
                String currentUsername = rs.getString("current_username");
                
                // Extract bay number from "Bay-X" format
                int bayNumber = Integer.parseInt(bayNumberStr.replace("Bay-", ""));
                
                // Update occupation status
                boolean isOccupied = "Occupied".equals(status) && currentTicketId != null && !currentTicketId.isEmpty();
                
                if (bayNumber >= 1 && bayNumber <= 3) {
                    fastChargingOccupied[bayNumber - 1] = isOccupied;
                } else if (bayNumber >= 4 && bayNumber <= 8) {
                    normalChargingOccupied[bayNumber - 4] = isOccupied;
                }
                
                if (isOccupied) {
                    System.out.println("Bay-" + bayNumber + " is occupied by ticket " + currentTicketId + " (user: " + currentUsername + ")");
                }
            }
            
            
        } catch (Exception e) {
            System.out.println("Error loading bay occupation from database: " + e.getMessage());
            e.printStackTrace();
        }
    }   
    
    private static void initializeGridDisplays() {
        try {
            // Ensure database connection is established
            if (!cephra.Database.DatabaseConnection.testConnection()) {
                System.out.println("Database connection failed during grid initialization");
                return;
            }
            
            // Load maintenance status from database
            loadMaintenanceStatusFromDatabase();
            
            // Ensure waiting grid table and slots exist
            ensureWaitingGridInitialized();

            // Ensure waiting_grid reflects all queue tickets with status=Waiting
            syncWaitingGridFromQueue();
            
            // Update all grid displays with current status
            updateAllBayGridDisplays();
            
            
        } catch (Exception e) {
            System.out.println("Error initializing grid displays: " + e.getMessage());
            e.printStackTrace();
        }
    }   

    // Backfill waiting_grid from queue_tickets where status is Waiting but not yet in waiting_grid
    private static void syncWaitingGridFromQueue() {
        try (java.sql.Connection conn = cephra.Database.DatabaseConnection.getConnection()) {
            try (java.sql.PreparedStatement ps = conn.prepareStatement(
                    "SELECT qt.ticket_id, qt.username, qt.service_type, qt.initial_battery_level " +
                    "FROM queue_tickets qt " +
                    "LEFT JOIN waiting_grid wg ON wg.ticket_id = qt.ticket_id " +
                    "WHERE qt.status='Waiting' AND wg.ticket_id IS NULL " +
                    "ORDER BY qt.created_at, qt.ticket_id")) {
                try (java.sql.ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String ticketId = rs.getString("ticket_id");
                        String username = rs.getString("username");
                        String serviceType = rs.getString("service_type");
                        int batteryLevel = rs.getInt("initial_battery_level");
                        addTicketToWaitingGrid(ticketId, username, serviceType, batteryLevel);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error syncing waiting grid from queue: " + e.getMessage());
        }
    }

    // Ensure waiting_grid table exists and has 10 slots
    private static void ensureWaitingGridInitialized() {
        try (java.sql.Connection conn = cephra.Database.DatabaseConnection.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS waiting_grid (
                    slot_number INT PRIMARY KEY,
                    ticket_id VARCHAR(20) NULL,
                    username VARCHAR(50) NULL,
                    service_type VARCHAR(20) NULL,
                    initial_battery_level INT NULL,
                    position_in_queue INT NULL
                )
            """);

            // Insert 10 slots if not present
            for (int i = 1; i <= 10; i++) {
                try (java.sql.PreparedStatement ps = conn.prepareStatement(
                        "INSERT IGNORE INTO waiting_grid (slot_number) VALUES (?)")) {
                    ps.setInt(1, i);
                    ps.executeUpdate();
                }
            }
        } catch (Exception e) {
            System.out.println("Error ensuring waiting_grid initialization: " + e.getMessage());
        }
    }
   
    private static void loadMaintenanceStatusFromDatabase() {
        try (java.sql.Connection conn = cephra.Database.DatabaseConnection.getConnection();
             java.sql.Statement stmt = conn.createStatement();
             java.sql.ResultSet rs = stmt.executeQuery("SELECT bay_number, status FROM charging_bays ORDER BY bay_number")) {
            
            while (rs.next()) {
                String bayNumberStr = rs.getString("bay_number");
                String status = rs.getString("status");
                
                // Extract bay number from "Bay-X" format
                int bayNumber = Integer.parseInt(bayNumberStr.replace("Bay-", ""));
                
                // Update maintenance status based on database
                boolean isMaintenance = "Maintenance".equals(status);
                
                if (bayNumber >= 1 && bayNumber <= 3) {
                    // Fast charging bays
                    fastChargingAvailable[bayNumber - 1] = !isMaintenance;
                } else if (bayNumber >= 4 && bayNumber <= 8) {
                    // Normal charging bays
                    normalChargingAvailable[bayNumber - 4] = !isMaintenance;
                }             
            }            
            
        } catch (Exception e) {
            System.out.println("Error loading maintenance status from database: " + e.getMessage());
            e.printStackTrace();
        }
    }    
   //ensure maintenance display
    public static void ensureMaintenanceDisplay() {
        try {
            // Refresh maintenance status from database
            loadMaintenanceStatusFromDatabase();
            
            // Update all grid displays
            updateAllBayGridDisplays();        
            
        } catch (Exception e) {
            System.out.println("Error ensuring maintenance display: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static boolean isBayPermanentlyInMaintenance(int bayNumber) {
        try (java.sql.Connection conn = cephra.Database.DatabaseConnection.getConnection();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(
                 "SELECT status FROM charging_bays WHERE bay_number = ?")) {
            
            pstmt.setString(1, "Bay-" + bayNumber);
            try (java.sql.ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String status = rs.getString("status");
                    return "Maintenance".equals(status);
                }
            }           
        } catch (Exception e) {
            System.out.println("Error checking permanent maintenance status: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    private static void updateBayStatusInDatabase(int bayNumber, String status) {
        try (java.sql.Connection conn = cephra.Database.DatabaseConnection.getConnection();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(
                 "UPDATE charging_bays SET status = ? WHERE bay_number = ?")) {
            
            pstmt.setString(1, status);
            pstmt.setString(2, "Bay-" + bayNumber);
            
            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Bay-" + bayNumber + " status updated to: " + status);
            } else {
                System.out.println("No rows updated for Bay-" + bayNumber);
            }
            
        } catch (Exception e) {
            System.out.println("Error updating bay status in database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
   //get bay number by ticket
    public static String getBayNumberByTicket(String ticketId) {
        if (ticketId == null || ticketId.trim().isEmpty()) {
            return null;
        }
        
        try (java.sql.Connection conn = cephra.Database.DatabaseConnection.getConnection()) {
            if (conn == null) {
                System.out.println("Could not establish database connection for bay number lookup");
                return null;
            }
            
            try (java.sql.PreparedStatement stmt = conn.prepareStatement(
                    "SELECT bay_number FROM charging_bays WHERE current_ticket_id = ?")) {
                stmt.setString(1, ticketId);
                try (java.sql.ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String bayNumberStr = rs.getString("bay_number");
                        // Extract just the number from "Bay-X" format
                        if (bayNumberStr != null && bayNumberStr.startsWith("Bay-")) {
                            return bayNumberStr.replace("Bay-", "");
                        }
                        return bayNumberStr;
                    }
                }
            }
            
        } catch (Exception e) {
            System.out.println("Error getting bay number by ticket: " + e.getMessage());
        }
        
        return null;
    }
    
    public static int addTicketToWaitingGrid(String ticketId, String username, String serviceType, int batteryLevel) {
        try {
            // Check if there's charging capacity for this service type
            boolean isFastCharging = false;
            if (serviceType != null) {
                String svc = serviceType.trim().toLowerCase();
                isFastCharging = svc.contains("fast");
            }
            if (!hasChargingCapacity(isFastCharging)) {
                return -1;
            }
            
            // Add ticket to waiting grid (admin will process via proceed button)
            try (java.sql.Connection conn = cephra.Database.DatabaseConnection.getConnection();
                 java.sql.PreparedStatement pstmt = conn.prepareStatement(
                     "UPDATE waiting_grid SET ticket_id = ?, username = ?, service_type = ?, initial_battery_level = ?, position_in_queue = ? WHERE slot_number = ? AND ticket_id IS NULL")) {
                
                // Find the first available slot
                int availableSlot = findNextAvailableWaitingSlot();
                if (availableSlot == -1) {
                    System.out.println("No available waiting slots for ticket: " + ticketId);
                    return -1;
                }
                
                pstmt.setString(1, ticketId);
                pstmt.setString(2, username);
                pstmt.setString(3, serviceType);
                pstmt.setInt(4, batteryLevel);
                pstmt.setInt(5, availableSlot);
                pstmt.setInt(6, availableSlot);
                
                int rowsUpdated = pstmt.executeUpdate();
                if (rowsUpdated > 0) {
                    return availableSlot;
                }
            }
            
        } catch (Exception e) {
            System.out.println("Error adding ticket to waiting grid: " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }
    
    private static int findNextAvailableWaitingSlot() {
        try (java.sql.Connection conn = cephra.Database.DatabaseConnection.getConnection();
             java.sql.Statement stmt = conn.createStatement();
             java.sql.ResultSet rs = stmt.executeQuery("SELECT slot_number FROM waiting_grid WHERE ticket_id IS NULL ORDER BY slot_number LIMIT 1")) {
            
            if (rs.next()) {
                return rs.getInt("slot_number");
            }
            
        } catch (Exception e) {
            System.out.println("Error finding available waiting slot: " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }
    
    public static boolean moveTicketFromWaitingToCharging(String ticketId, int bayNumber) {
        try (java.sql.Connection conn = cephra.Database.DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            
            try {
                // Get ticket info from waiting grid
                String username = null;
                String serviceType = null;
                int batteryLevel = 0;
                
                try (java.sql.PreparedStatement pstmt = conn.prepareStatement(
                        "SELECT username, service_type, initial_battery_level FROM waiting_grid WHERE ticket_id = ?")) {
                    pstmt.setString(1, ticketId);
                    try (java.sql.ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next()) {
                            username = rs.getString("username");
                            serviceType = rs.getString("service_type");
                            batteryLevel = rs.getInt("initial_battery_level");
                        } else {
                            System.out.println("BayManagement: Ticket " + ticketId + " not found in waiting grid");
                            return false;
                        }
                    }
                }
                
                // Remove from waiting grid
                try (java.sql.PreparedStatement pstmt = conn.prepareStatement(
                        "UPDATE waiting_grid SET ticket_id = NULL, username = NULL, service_type = NULL, initial_battery_level = NULL, position_in_queue = NULL WHERE ticket_id = ?")) {
                    pstmt.setString(1, ticketId);
                    pstmt.executeUpdate();
                }
                
                // Add to charging grid
                try (java.sql.PreparedStatement pstmt = conn.prepareStatement(
                        "UPDATE charging_grid SET ticket_id = ?, username = ?, service_type = ?, initial_battery_level = ?, start_time = CURRENT_TIMESTAMP WHERE bay_number = ?")) {
                    pstmt.setString(1, ticketId);
                    pstmt.setString(2, username);
                    pstmt.setString(3, serviceType);
                    pstmt.setInt(4, batteryLevel);
                    pstmt.setString(5, "Bay-" + bayNumber);
                    pstmt.executeUpdate();
                }
                
                // Update charging_bays table
                try (java.sql.PreparedStatement pstmt = conn.prepareStatement(
                        "UPDATE charging_bays SET status = 'Occupied', current_ticket_id = ?, current_username = ?, start_time = CURRENT_TIMESTAMP WHERE bay_number = ?")) {
                    pstmt.setString(1, ticketId);
                    pstmt.setString(2, username);
                    pstmt.setString(3, "Bay-" + bayNumber);
                    pstmt.executeUpdate();
                }
                
                // CRITICAL: Update queue_tickets status from "Waiting" to "In Progress"
                try (java.sql.PreparedStatement pstmt = conn.prepareStatement(
                        "UPDATE queue_tickets SET status = 'In Progress' WHERE ticket_id = ?")) {
                    pstmt.setString(1, ticketId);
                    pstmt.executeUpdate();
                }
                
                conn.commit();
                
                // Notify Queue and Monitor to refresh their displays
                notifyGridDisplayUpdate();
                
                // Also refresh the queue table to show status change
                refreshQueueTableDisplay();
                
                return true;
                
            } catch (Exception e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
            
        } catch (Exception e) {
            System.out.println("Error moving ticket from waiting to charging: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
   
    public static boolean isTicketInWaitingGrid(String ticketId) {
        try (java.sql.Connection conn = cephra.Database.DatabaseConnection.getConnection();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(
                 "SELECT COUNT(*) FROM waiting_grid WHERE ticket_id = ?")) {
            
            pstmt.setString(1, ticketId);
            try (java.sql.ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (Exception e) {
            System.out.println("Error checking if ticket is in waiting grid: " + e.getMessage());
        }
        return false;
    }
    
    public static String[] getWaitingGridTickets() {
        String[] tickets = new String[10];
        try (java.sql.Connection conn = cephra.Database.DatabaseConnection.getConnection();
             java.sql.Statement stmt = conn.createStatement();
             java.sql.ResultSet rs = stmt.executeQuery("SELECT slot_number, ticket_id FROM waiting_grid ORDER BY slot_number")) {
            
            while (rs.next()) {
                int slotNumber = rs.getInt("slot_number");
                String ticketId = rs.getString("ticket_id");
                if (slotNumber >= 1 && slotNumber <= 10) {
                    tickets[slotNumber - 1] = (ticketId != null) ? ticketId : "";
                }
            }
            
        } catch (java.sql.SQLSyntaxErrorException e) {
            if (e.getMessage().contains("doesn't exist")) {
                System.out.println("Waiting grid table doesn't exist yet. H2DatabaseConnection should have created it.");
                // Return empty array for now
                return new String[10];
            } else {
                System.out.println("Error getting waiting grid tickets: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.out.println("Error getting waiting grid tickets: " + e.getMessage());
            e.printStackTrace();
        }
        return tickets;
    }
    
    public static String[] getChargingGridTickets() {
        String[] tickets = new String[8];
        try (java.sql.Connection conn = cephra.Database.DatabaseConnection.getConnection();
             java.sql.Statement stmt = conn.createStatement();
             java.sql.ResultSet rs = stmt.executeQuery("SELECT bay_number, ticket_id FROM charging_grid ORDER BY bay_number")) {
            
            while (rs.next()) {
                String bayNumber = rs.getString("bay_number");
                String ticketId = rs.getString("ticket_id");
                
                // Extract bay number (Bay-1 -> 1, Bay-2 -> 2, etc.)
                if (bayNumber != null && bayNumber.startsWith("Bay-")) {
                    try {
                        int bayNum = Integer.parseInt(bayNumber.replace("Bay-", ""));
                        if (bayNum >= 1 && bayNum <= 8) {
                            tickets[bayNum - 1] = (ticketId != null) ? ticketId : "";
                        }
                    } catch (NumberFormatException e) {
                        // Skip invalid bay numbers
                    }
                }
            }
            
        } catch (java.sql.SQLSyntaxErrorException e) {
            if (e.getMessage().contains("doesn't exist")) {
                System.out.println("Charging grid table doesn't exist yet. H2DatabaseConnection should have created it.");
                // Return empty array for now
                return new String[8];
            } else {
                System.out.println("Error getting charging grid tickets: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.out.println("Error getting charging grid tickets: " + e.getMessage());
            e.printStackTrace();
        }
        return tickets;
    }
  
    public static int countAvailableBays(boolean isFastCharging) {
        int count = 0;
        
        if (isFastCharging) {
            for (int i = 1; i <= 3; i++) {
                if (isBayAvailableForCharging(i)) {
                    count++;
                }
            }
        } else {
            for (int i = 4; i <= 8; i++) {
                if (isBayAvailableForCharging(i)) {
                    count++;
                }
            }
        }    
        return count;
    }
    
    public static int countOccupiedBays(boolean isFastCharging) {
        int count = 0;
        try (java.sql.Connection conn = cephra.Database.DatabaseConnection.getConnection();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(
                 "SELECT COUNT(*) as count FROM charging_grid cg " +
                 "JOIN charging_bays cb ON cg.bay_number = cb.bay_number " +
                 "WHERE cg.ticket_id IS NOT NULL AND cb.status = 'Available' " +
                 "AND cg.service_type = ?")) {
            
            pstmt.setString(1, isFastCharging ? "Fast" : "Normal");
            try (java.sql.ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt("count");
                }
            }
        } catch (Exception e) {
            System.out.println("Error counting occupied bays: " + e.getMessage());
            e.printStackTrace();
        }
        return count;
    }

    public static boolean hasChargingCapacity(boolean isFastCharging) {
        ensureChargingBaysExist();
        return countAvailableBays(isFastCharging) > 0;
    }
  
    private static void ensureChargingBaysExist() {
        try (java.sql.Connection conn = cephra.Database.DatabaseConnection.getConnection()) {
            // Check if charging_bays table exists and has data
            try (java.sql.Statement stmt = conn.createStatement();
                 java.sql.ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM charging_bays")) {
                
                if (rs.next()) {
                    int count = rs.getInt("count");
                    
                    if (count == 0) {
                        initializeChargingBays();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    //initialize charging bays
    private static void initializeChargingBays() {
        try (java.sql.Connection conn = cephra.Database.DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            
            try {
                // Initialize fast charging bays (Bay-1, Bay-2, Bay-3)
                for (int i = 1; i <= 3; i++) {
                    try (java.sql.PreparedStatement pstmt = conn.prepareStatement(
                            "INSERT INTO charging_bays (bay_number, bay_type, status, current_ticket_id, current_username, start_time) VALUES (?, 'Fast', 'Available', NULL, NULL, NULL)")) {
                        pstmt.setString(1, "Bay-" + i);
                        pstmt.executeUpdate();
                    }
                }
                
                // Initialize normal charging bays (Bay-4, Bay-5, Bay-6, Bay-7, Bay-8)
                for (int i = 4; i <= 8; i++) {
                    try (java.sql.PreparedStatement pstmt = conn.prepareStatement(
                            "INSERT INTO charging_bays (bay_number, bay_type, status, current_ticket_id, current_username, start_time) VALUES (?, 'Normal', 'Available', NULL, NULL, NULL)")) {
                        pstmt.setString(1, "Bay-" + i);
                        pstmt.executeUpdate();
                    }
                }
                
                conn.commit();
                
            } catch (Exception e) {
                conn.rollback();
                e.printStackTrace();
                throw e;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void printCurrentBayStatus() {
        System.out.println("=== CURRENT BAY STATUS (Static Arrays) ===");
        System.out.println("Fast Charging Bays (1-3):");
        for (int i = 0; i < 3; i++) {
            System.out.println("  Bay-" + (i + 1) + " - Available: " + fastChargingAvailable[i] + ", Occupied: " + fastChargingOccupied[i]);
        }
        System.out.println("Normal Charging Bays (4-8):");
        for (int i = 0; i < 5; i++) {
            System.out.println("  Bay-" + (i + 4) + " - Available: " + normalChargingAvailable[i] + ", Occupied: " + normalChargingOccupied[i]);
        }
        System.out.println("=== END BAY STATUS ===");
    }
    
    public static void processAllWaitingTickets() {
        System.out.println("BayManagement: Manually processing all waiting tickets...");
        
        // First, show current bay status
        showAllBaysStatus();
        
        // Then, show what tickets are in waiting
        showWaitingTicketsStatus();
        
        // Finally, process them
        autoAssignWaitingTickets();
    } 
  
    private static void refreshQueueTableDisplay() {
        try {
            // Notify the Queue instance to refresh its table
            if (queueInstance != null) {
                SwingUtilities.invokeLater(() -> {
                    try {
                        // Force a hard refresh of the queue table
                        queueInstance.hardRefreshTable();
                    } catch (Exception e) {
                        System.err.println("Error refreshing queue table: " + e.getMessage());
                    }
                });
            }
        } catch (Exception e) {
            System.err.println("Error in refreshQueueTableDisplay: " + e.getMessage());
        }
    }
   
    public static void showAllBaysStatus() {
        System.out.println("BayManagement: Current charging bays status:");
        
        // Check fast charging bays (Bay-1, Bay-2, Bay-3)
        System.out.println("Fast Charging Bays:");
        for (int i = 1; i <= 3; i++) {
            boolean available = isBayAvailableForCharging(i);
            System.out.println("  Bay-" + i + ": " + (available ? "AVAILABLE" : "OCCUPIED/OFFLINE"));
        }   
        // Check normal charging bays (Bay-4, Bay-5, Bay-6, Bay-7, Bay-8)
        System.out.println("Normal Charging Bays:");
        for (int i = 4; i <= 8; i++) {
            boolean available = isBayAvailableForCharging(i);
            System.out.println("  Bay-" + i + ": " + (available ? "AVAILABLE" : "OCCUPIED/OFFLINE"));
        }
        
        // Show overall availability
        System.out.println("Overall Status:");
        System.out.println("  Fast Charging Available: " + isFastChargingAvailable());
        System.out.println("  Normal Charging Available: " + isNormalChargingAvailable());
    }
     
    public static void showWaitingTicketsStatus() {
        try (java.sql.Connection conn = cephra.Database.DatabaseConnection.getConnection();
             java.sql.Statement stmt = conn.createStatement();
             java.sql.ResultSet rs = stmt.executeQuery("SELECT slot_number, ticket_id, username, service_type, initial_battery_level, position_in_queue FROM waiting_grid WHERE ticket_id IS NOT NULL ORDER BY position_in_queue")) {
            
            System.out.println("BayManagement: Current waiting tickets:");
            boolean hasTickets = false;
            
            while (rs.next()) {
                hasTickets = true;
                String slot = rs.getString("slot_number");
                String ticketId = rs.getString("ticket_id");
                String username = rs.getString("username");
                String serviceType = rs.getString("service_type");
                int batteryLevel = rs.getInt("initial_battery_level");
                int position = rs.getInt("position_in_queue");
                
                System.out.println("  Slot " + slot + ": " + ticketId + " (" + username + ") - " + serviceType + " - Battery: " + batteryLevel + "% - Position: " + position);
            }
            
            if (!hasTickets) {
                System.out.println("  No tickets currently in waiting grid");
            }
            
        } catch (Exception e) {
            System.err.println("Error showing waiting tickets status: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void autoAssignWaitingTickets() {
        try {
            System.out.println("Auto-assigning waiting tickets to available bays...");
            
            // Get all waiting tickets
            try (java.sql.Connection conn = cephra.Database.DatabaseConnection.getConnection();
                 java.sql.Statement stmt = conn.createStatement();
                 java.sql.ResultSet rs = stmt.executeQuery("SELECT ticket_id, service_type FROM waiting_grid WHERE ticket_id IS NOT NULL ORDER BY position_in_queue")) {
                
                while (rs.next()) {
                    String ticketId = rs.getString("ticket_id");
                    String serviceType = rs.getString("service_type");
                    
                    if (ticketId != null && !ticketId.isEmpty()) {
                        // Check for both "Fast" and "Fast Charging" service types
                        boolean isFastCharging = "Fast".equals(serviceType) || "Fast Charging".equals(serviceType);
                        int bayNumber = findNextAvailableBay(isFastCharging);
                        
                        System.out.println("BayManagement: Processing waiting ticket " + ticketId + " - Service: '" + serviceType + "', IsFast: " + isFastCharging);
                        
                        if (bayNumber > 0) {
                            // Move ticket to available bay
                            if (moveTicketFromWaitingToCharging(ticketId, bayNumber)) {
                                System.out.println("Auto-assigned ticket " + ticketId + " to Bay-" + bayNumber);
                                // Update grid displays
                                updateAllBayGridDisplays();
                            }
                        } else {
                        }
                    }
                }
            }
            
        } catch (Exception e) {
            System.out.println("Error auto-assigning waiting tickets: " + e.getMessage());
            e.printStackTrace();
        }
    }   
    public static boolean clearChargingBayForCompletedTicket(String ticketId) {
        
        try (java.sql.Connection conn = cephra.Database.DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            
            try {
                // Clear charging_bays table
                try (java.sql.PreparedStatement pstmt = conn.prepareStatement(
                        "UPDATE charging_bays SET current_ticket_id = NULL, current_username = NULL, status = 'Available', start_time = NULL WHERE current_ticket_id = ?")) {
                    pstmt.setString(1, ticketId);
                    int bayRowsUpdated = pstmt.executeUpdate();
                }
                
                // Clear charging_grid table
                try (java.sql.PreparedStatement pstmt = conn.prepareStatement(
                        "UPDATE charging_grid SET ticket_id = NULL, username = NULL, service_type = NULL, initial_battery_level = NULL, start_time = NULL WHERE ticket_id = ?")) {
                    pstmt.setString(1, ticketId);
                    int gridRowsUpdated = pstmt.executeUpdate();
                }
                
                conn.commit();
                
                // Notify Queue and Monitor to refresh their displays
                notifyGridDisplayUpdate();
                
                return true;
                
            } catch (Exception e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
            
        } catch (Exception e) {
            System.out.println("BayManagement: Error clearing charging bay for ticket " + ticketId + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private static void recoverStalePendingPaymentTickets() {
        try (java.sql.Connection conn = cephra.Database.DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                // Get candidate tickets
                java.util.List<String> tickets = new java.util.ArrayList<>();
                try (java.sql.PreparedStatement ps = conn.prepareStatement(
                        "SELECT ticket_id FROM queue_tickets WHERE status='Pending' AND payment_status='Pending'");
                     java.sql.ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) tickets.add(rs.getString(1));
                }

                for (String ticketId : tickets) {
                    if (ticketId == null || ticketId.isEmpty()) continue;

                    // Clear charging_grid references
                    try (java.sql.PreparedStatement pg = conn.prepareStatement(
                            "UPDATE charging_grid SET ticket_id=NULL, username=NULL, service_type=NULL, initial_battery_level=NULL, start_time=NULL WHERE ticket_id=?")) {
                        pg.setString(1, ticketId);
                        pg.executeUpdate();
                    }

                    // Release charging_bays
                    try (java.sql.PreparedStatement pb = conn.prepareStatement(
                            "UPDATE charging_bays SET status='Available', current_ticket_id=NULL, current_username=NULL, start_time=NULL WHERE current_ticket_id=?")) {
                        pb.setString(1, ticketId);
                        pb.executeUpdate();
                    }

                    // Remove any stale active_tickets rows
                    try (java.sql.PreparedStatement pa = conn.prepareStatement(
                            "DELETE FROM active_tickets WHERE ticket_id=?")) {
                        pa.setString(1, ticketId);
                        pa.executeUpdate();
                    }
                }

                conn.commit();
            } catch (Exception ex) {
                conn.rollback();
                System.out.println("Recovery of pending-payment tickets failed: " + ex.getMessage());
            } finally {
                try { conn.setAutoCommit(true); } catch (Exception ignore) {}
            }
        } catch (Exception e) {
            System.out.println("Recovery connection error: " + e.getMessage());
        }
    }
}