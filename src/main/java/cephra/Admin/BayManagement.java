package cephra.Admin;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class BayManagement extends javax.swing.JPanel {
    
    // Static variables to track available bays - ALL SET TO AVAILABLE
    public static boolean[] fastChargingAvailable = {true, true, true}; // Bays 1-3 - ALL AVAILABLE
    public static boolean[] normalChargingAvailable = {true, true, true, true, true}; // Bays 4-8 - ALL AVAILABLE
    
    // Static flag to track if toggle states have been loaded from database
    private static boolean toggleStatesLoaded = false; // Reset to false to force reload from database
    
    // Static variables to track occupied bays
    public static boolean[] fastChargingOccupied = {false, false, false}; // Bays 1-3
    public static boolean[] normalChargingOccupied = {false, false, false, false, false}; // Bays 4-8
    
    // Static references to Queue and Monitor instances for real-time updates
    private static cephra.Admin.Queue queueInstance = null;
    private static cephra.Frame.Monitor monitorInstance = null;
    
    // Logging control: set to true to enable debug prints
    private static final boolean VERBOSE = false;
    
    private static void logDebug(String msg) { if (VERBOSE) System.out.println(msg); }
    private static void logInfo(String msg)  { if (VERBOSE) System.out.println(msg); }
    private static void logWarn(String msg)  { System.out.println(msg); }
    private static void logError(String msg) { System.err.println(msg); }
    private static void logError(String msg, Throwable t) { System.err.println(msg); if (t != null) t.printStackTrace(); }
    
    /**
     * Registers a Queue instance for real-time updates
     */
    public static void registerQueueInstance(cephra.Admin.Queue queue) {
        queueInstance = queue;
        logDebug("Queue instance registered for real-time updates");
    }
    
    /**
     * Registers a Monitor instance for real-time updates
     */
    public static void registerMonitorInstance(cephra.Frame.Monitor monitor) {
        monitorInstance = monitor;
        logDebug("Monitor instance registered for real-time updates");
    }
    
    /**
     * Unregisters Queue instance
     */
    public static void unregisterQueueInstance() {
        queueInstance = null;
        logDebug("Queue instance unregistered");
    }
    
    /**
     * Unregisters Monitor instance
     */
    public static void unregisterMonitorInstance() {
        monitorInstance = null;
        logDebug("Monitor instance unregistered");
    }
    
    // Toggle buttons array for easy access
    private JToggleButton[] bayToggleButtons = new JToggleButton[8];
    
    // Static methods to check availability
    public static boolean isFastChargingAvailable() {
        for (int i = 0; i < fastChargingAvailable.length; i++) {
            if (fastChargingAvailable[i] && !fastChargingOccupied[i]) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isNormalChargingAvailable() {
        for (int i = 0; i < normalChargingAvailable.length; i++) {
            if (normalChargingAvailable[i] && !normalChargingOccupied[i]) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isBayAvailableForCharging(int bayNumber) {
        try {
            // Check database for real-time status
            try (java.sql.Connection conn = cephra.Database.DatabaseConnection.getConnection();
                 java.sql.PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT cb.status, cg.ticket_id FROM charging_bays cb LEFT JOIN charging_grid cg ON cb.bay_number = cg.bay_number WHERE cb.bay_number = ?")) {
                
                pstmt.setString(1, "Bay-" + bayNumber);
                try (java.sql.ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        String status = rs.getString("status");
                        String ticketId = rs.getString("ticket_id");
                        
                        boolean isAvailable = "Available".equals(status) && (ticketId == null || ticketId.isEmpty());
                        System.out.println("BayManagement: Bay-" + bayNumber + " - Status: " + status + ", Ticket: " + ticketId + ", Available: " + isAvailable);
                        return isAvailable;
                    } else {
                        System.err.println("BayManagement: No database record found for Bay-" + bayNumber);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error checking bay availability for Bay-" + bayNumber + ": " + e.getMessage());
            e.printStackTrace();
        }
        
        // Fallback to static array check
        int index = bayNumber - 1;
        boolean fallbackResult = false;
        if (bayNumber >= 1 && bayNumber <= 3) {
            fallbackResult = fastChargingAvailable[index] && !fastChargingOccupied[index];
            logDebug("BayManagement: Fallback check for Bay-" + bayNumber + " - Available: " + fastChargingAvailable[index] + ", Occupied: " + fastChargingOccupied[index] + ", Result: " + fallbackResult);
        } else if (bayNumber >= 4 && bayNumber <= 8) {
            fallbackResult = normalChargingAvailable[index - 3] && !normalChargingOccupied[index - 3];
            logDebug("BayManagement: Fallback check for Bay-" + bayNumber + " - Available: " + normalChargingAvailable[index - 3] + ", Occupied: " + normalChargingOccupied[index - 3] + ", Result: " + fallbackResult);
        }
        return fallbackResult;
    }
    
    /**
     * Finds the next available bay for a specific service type
     * @param isFastCharging true for fast charging, false for normal charging
     * @return bay number (1-8) or -1 if no bay available
     */
    public static int findNextAvailableBay(boolean isFastCharging) {
        if (isFastCharging) {
            // Check fast charging bays (1-3) - skip offline bays
            for (int i = 0; i < 3; i++) {
                int bayNumber = i + 1;
                if (isBayAvailableForCharging(bayNumber)) {
                    logInfo("Found available fast charging bay: Bay-" + bayNumber);
                    return bayNumber;
                } else {
                    logDebug("Bay-" + bayNumber + " is not available (offline or occupied)");
                }
            }
        } else {
            // Check normal charging bays (4-8) - skip offline bays
            for (int i = 0; i < 5; i++) {
                int bayNumber = i + 4;
                if (isBayAvailableForCharging(bayNumber)) {
                    logInfo("Found available normal charging bay: Bay-" + bayNumber);
                    return bayNumber;
                } else {
                    logDebug("Bay-" + bayNumber + " is not available (offline or occupied)");
                }
            }
        }
        logInfo("No available " + (isFastCharging ? "fast" : "normal") + " charging bays found");
        return -1; // No available bay found
    }
    
    /**
     * Gets the available bay slots for display in grids (skips maintenance bays)
     * @param isFastCharging true for fast charging, false for normal charging
     * @return array of available bay numbers
     */
    public static int[] getAvailableBaySlots(boolean isFastCharging) {
        java.util.List<Integer> availableBays = new java.util.ArrayList<>();
        
        if (isFastCharging) {
            // Check fast charging bays (1-3)
            for (int i = 0; i < 3; i++) {
                int bayNumber = i + 1;
                if (isBayAvailableForCharging(bayNumber)) {
                    availableBays.add(bayNumber);
                }
            }
        } else {
            // Check normal charging bays (4-8)
            for (int i = 0; i < 5; i++) {
                int bayNumber = i + 4;
                if (isBayAvailableForCharging(bayNumber)) {
                    availableBays.add(bayNumber);
                }
            }
        }
        
        // Convert to array
        int[] result = new int[availableBays.size()];
        for (int i = 0; i < availableBays.size(); i++) {
            result[i] = availableBays.get(i);
        }
        return result;
    }
    
    /**
     * Maps bay number to grid slot index (for Queue and Monitor display)
     * @param bayNumber the bay number (1-8)
     * @return grid slot index (0-based) or -1 if bay is unavailable
     */
    public static int getBayGridSlotIndex(int bayNumber) {
        if (bayNumber >= 1 && bayNumber <= 3) {
            // Fast charging bays
            if (!isBayAvailableForCharging(bayNumber)) {
                return -1; // Bay is in maintenance
            }
            return bayNumber - 1; // Bay-1 = slot 0, Bay-2 = slot 1, Bay-3 = slot 2
        } else if (bayNumber >= 4 && bayNumber <= 8) {
            // Normal charging bays
            if (!isBayAvailableForCharging(bayNumber)) {
                return -1; // Bay is in maintenance
            }
            return bayNumber - 4; // Bay-4 = slot 0, Bay-5 = slot 1, etc.
        }
        return -1; // Invalid bay number
    }
    
    /**
     * Gets the next available grid slot for assignment (skips maintenance bays)
     * @param isFastCharging true for fast charging, false for normal charging
     * @return grid slot index (0-based) or -1 if no slot available
     */
    public static int getNextAvailableGridSlot(boolean isFastCharging) {
        int[] availableBays = getAvailableBaySlots(isFastCharging);
        
        if (availableBays.length == 0) {
            return -1; // No available bays
        }
        
        // Return the first available bay's grid slot index
        return getBayGridSlotIndex(availableBays[0]);
    }
    
    /**
     * Assigns a ticket to a specific bay and updates database
     * @param ticketId the ticket ID
     * @param username the username
     * @param bayNumber the bay number to assign
     * @return true if assignment successful, false otherwise
     */
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
            
            logInfo("Ticket " + ticketId + " assigned to Bay-" + bayNumber + " for user " + username);
            return true;
            
        } catch (Exception e) {
            logError("Error assigning ticket to bay: " + e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Releases a bay when charging is complete
     * @param bayNumber the bay number to release
     * @return true if release successful, false otherwise
     */
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
            
            System.out.println("Bay-" + bayNumber + " released");
            return true;
            
        } catch (Exception e) {
            System.err.println("Error releasing bay: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public static int getAvailableFastChargingCount() {
        if (fastChargingAvailable == null) return 0;
        int count = 0;
        for (int i = 0; i < fastChargingAvailable.length; i++) {
            if (fastChargingAvailable[i] && !fastChargingOccupied[i]) {
                count++;
            }
        }
        return count;
    }
    
    public static int getAvailableNormalChargingCount() {
        if (normalChargingAvailable == null) return 0;
        int count = 0;
        for (int i = 0; i < normalChargingAvailable.length; i++) {
            if (normalChargingAvailable[i] && !normalChargingOccupied[i]) {
                count++;
            }
        }
        return count;
    }
    
    public static void loadToggleStates() {
        // Only load from database ONCE - never reload
        if (toggleStatesLoaded) {
            System.out.println("*** TOGGLE STATES ALREADY LOADED - PRESERVING CURRENT VALUES ***");
            System.out.println("Current fastChargingAvailable: [" + fastChargingAvailable[0] + ", " + fastChargingAvailable[1] + ", " + fastChargingAvailable[2] + "]");
            System.out.println("Current normalChargingAvailable: [" + normalChargingAvailable[0] + ", " + normalChargingAvailable[1] + ", " + normalChargingAvailable[2] + ", " + normalChargingAvailable[3] + ", " + normalChargingAvailable[4] + "]");
            return;
        }
        
        logDebug("*** LOADING TOGGLE STATES FROM DATABASE FOR THE FIRST TIME ONLY ***");
        try {
            // Create bay_toggle_states table if it doesn't exist
            createToggleStatesTable();
            
            // Load toggle states from database
            loadToggleStatesFromDatabase();
            
            toggleStatesLoaded = true;
            logDebug("*** TOGGLE STATES LOADED AND LOCKED - WILL NEVER RELOAD AGAIN ***");
            
        } catch (Exception e) {
            logError("Error loading toggle states: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void saveToggleStates() {
        logDebug("Saving toggle states to database...");
        try {
            // Save toggle states to database
            saveToggleStatesToDatabase();
            
            logWarn("Fast Charging Available: " + getAvailableFastChargingCount() + "/3");
            logWarn("Normal Charging Available: " + getAvailableNormalChargingCount() + "/5");
            
        } catch (Exception e) {
            logError("Error saving toggle states: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Checks if toggle states have been loaded from database
     * @return true if toggle states are loaded, false otherwise
     */
    @SuppressWarnings("unused")
    private static boolean areToggleStatesLoaded() {
        return toggleStatesLoaded;
    }
    
    /**
     * Forces toggle buttons to match current static array values
     */
    private void forceToggleButtonsToMatchStaticArrays() {
        logDebug("*** FORCING TOGGLE BUTTONS TO MATCH STATIC ARRAYS ***");
        if (bayToggleButtons != null) {
            for (int i = 0; i < 8; i++) {
                if (bayToggleButtons[i] != null) {
                    boolean isAvailable = (i < 3) ? fastChargingAvailable[i] : normalChargingAvailable[i - 3];
                    bayToggleButtons[i].setSelected(isAvailable);
                    logDebug("Bay-" + (i + 1) + " toggle FORCED to: " + (isAvailable ? "Available" : "Maintenance"));
                }
            }
        }
        logDebug("*** TOGGLE BUTTONS FORCED TO MATCH STATIC ARRAYS ***");
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
        labelStaff.setText("Admin!");
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
    
    @SuppressWarnings("unused")
    private String getLoggedInUsername() {
        try {
            java.awt.Window window = javax.swing.SwingUtilities.getWindowAncestor(this);
            if (window instanceof cephra.Frame.Admin) {
                java.lang.reflect.Field usernameField = window.getClass().getDeclaredField("loggedInUsername");
                usernameField.setAccessible(true);
                return (String) usernameField.get(window);
            }
        } catch (Exception e) {
            System.err.println("Error getting logged-in username: " + e.getMessage());
        }
        return "Admin";
    }
    
    /**
     * Sets up iOS-style toggle switches using NetBeans toggle buttons
     */
    private void setupIOSToggles() {
        // Initialize toggle buttons array with NetBeans toggles
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
            
            // Set initial state from database
            if (i < 3) { // Fast charging bays
                toggle.setSelected(fastChargingAvailable[i]);
            } else { // Normal charging bays
                toggle.setSelected(normalChargingAvailable[i - 3]);
            }
            
            // Apply iOS-style customization
            customizeToggleForIOS(toggle, i < 3);
            
            // Update bay labels to match database state
            JLabel bayLabel = getBayLabel(bayIndex + 1);
            if (bayLabel != null) {
                boolean isAvailable = (i < 3) ? fastChargingAvailable[i] : normalChargingAvailable[i - 3];
                updateBayLabel(bayLabel, isAvailable);
            }
            
            // Add action listener
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
                    
                    logInfo("*** TOGGLE SAVED PERMANENTLY *** Bay-" + bayNumber + " = " + (isSelected ? "Available" : "Maintenance"));
                    logDebug("Static arrays updated and saved to database - will NOT reset");
                    
                    // Update all grid displays with new maintenance status
                    updateAllBayGridDisplays();
                    
                    // If bay became available, try to auto-assign waiting tickets
                    if (isSelected) {
                        autoAssignWaitingTickets();
                    }
                    
                    logInfo("Bay-" + bayNumber + " toggled to: " + (isSelected ? "Available" : "Maintenance"));
                }
            });
        }
    }
    
    /**
     * Customizes a toggle button to look like iOS toggle
     */
    private void customizeToggleForIOS(JToggleButton toggle, boolean isFastCharging) {
        // Remove default styling
        toggle.setBorderPainted(false);
        toggle.setFocusPainted(false);
        toggle.setContentAreaFilled(false);
        toggle.setOpaque(false);
        
        // Apply custom iOS UI
        toggle.setUI(new IOSToggleUI(isFastCharging));
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
    
    /**
     * Updates all bay labels to match the database states (permanent)
     */
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
            
            logInfo("All bay labels updated to match database states (permanent)");
            
        } catch (Exception e) {
            System.err.println("Error updating bay labels from database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Forces refresh of toggle states and labels from database (for permanent persistence)
     * REMOVED - This method was causing toggle states to reset
     */
    public void refreshToggleStatesFromDatabase() {
        System.out.println("refreshToggleStatesFromDatabase() called but DISABLED to prevent toggle reset");
        // Method disabled to prevent toggle states from being reset
    }
    
    /**
     * Clears all tickets from the waiting grid (removes test tickets)
     */
    public static void clearWaitingGrid() {
        try {
            System.out.println("Clearing waiting grid of any existing tickets...");
            
            try (java.sql.Connection conn = cephra.Database.DatabaseConnection.getConnection();
                 java.sql.Statement stmt = conn.createStatement()) {
                
                // Clear all waiting grid slots
                stmt.execute("UPDATE waiting_grid SET ticket_id = NULL, username = NULL, service_type = NULL, initial_battery_level = NULL, position_in_queue = NULL");
                
                System.out.println("Waiting grid cleared successfully");
                
            }
            
        } catch (Exception e) {
            System.err.println("Error clearing waiting grid: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    
    
    /**
     * Custom iOS-style toggle UI for JToggleButton
     */
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
    
    /**
     * Creates the bay_toggle_states table if it doesn't exist
     */
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
            
            logInfo("Bay toggle states table created/verified successfully");
            
        } catch (Exception e) {
            System.err.println("Error creating toggle states table: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Loads toggle states from the database
     */
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
                    
                    logDebug("BayManagement: Loading toggle state for Bay-" + bayNumber + " = " + isAvailable);
                    
                    // Update the static arrays
                    if (bayNumber >= 1 && bayNumber <= 3) {
                        fastChargingAvailable[bayNumber - 1] = isAvailable;
                        logDebug("BayManagement: Set fastChargingAvailable[" + (bayNumber - 1) + "] = " + isAvailable);
                    } else if (bayNumber >= 4 && bayNumber <= 8) {
                        normalChargingAvailable[bayNumber - 4] = isAvailable;
                        logDebug("BayManagement: Set normalChargingAvailable[" + (bayNumber - 4) + "] = " + isAvailable);
                    }
                }
                
                if (hasToggleStates) {
                    logInfo("Toggle states loaded from bay_toggle_states table successfully");
                    logDebug("Current fastChargingAvailable: [" + fastChargingAvailable[0] + ", " + fastChargingAvailable[1] + ", " + fastChargingAvailable[2] + "]");
                    logDebug("Current normalChargingAvailable: [" + normalChargingAvailable[0] + ", " + normalChargingAvailable[1] + ", " + normalChargingAvailable[2] + ", " + normalChargingAvailable[3] + ", " + normalChargingAvailable[4] + "]");
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
                
                logInfo("Toggle states loaded from charging_bays table successfully");
                toggleStatesLoaded = true;
            }
            
        } catch (Exception e) {
            logError("Error loading toggle states from database: " + e.getMessage(), e);
            e.printStackTrace();
        }
    }
    
    /**
     * Saves toggle states to the database
     */
    private static void saveToggleStatesToDatabase() {
        try (java.sql.Connection conn = cephra.Database.DatabaseConnection.getConnection()) {
            
            // Update bay_toggle_states table
            try (java.sql.PreparedStatement pstmt = conn.prepareStatement(
                    "UPDATE bay_toggle_states SET is_available = ? WHERE bay_number = ?")) {
                
                // Save fast charging bays (1-3)
                for (int i = 0; i < fastChargingAvailable.length; i++) {
                    pstmt.setBoolean(1, fastChargingAvailable[i]);
                    pstmt.setInt(2, i + 1);
                    int rowsUpdated = pstmt.executeUpdate();
                    logDebug("BayManagement: Updated bay_toggle_states for Bay-" + (i + 1) + " to " + fastChargingAvailable[i] + " - rows updated: " + rowsUpdated);
                }
                
                // Save normal charging bays (4-8)
                for (int i = 0; i < normalChargingAvailable.length; i++) {
                    pstmt.setBoolean(1, normalChargingAvailable[i]);
                    pstmt.setInt(2, i + 4);
                    int rowsUpdated = pstmt.executeUpdate();
                    logDebug("BayManagement: Updated bay_toggle_states for Bay-" + (i + 4) + " to " + normalChargingAvailable[i] + " - rows updated: " + rowsUpdated);
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
                    int rowsUpdated = pstmt.executeUpdate();
                    logDebug("BayManagement: Updated charging_bays for Bay-" + (i + 1) + " to " + status + " - rows updated: " + rowsUpdated);
                }
                
                // Update normal charging bays (4-8)
                for (int i = 0; i < normalChargingAvailable.length; i++) {
                    String status = normalChargingAvailable[i] ? "Available" : "Maintenance";
                    pstmt.setString(1, status);
                    pstmt.setString(2, "Bay-" + (i + 4));
                    int rowsUpdated = pstmt.executeUpdate();
                    logDebug("BayManagement: Updated charging_bays for Bay-" + (i + 4) + " to " + status + " - rows updated: " + rowsUpdated);
                }
            }
            
            logDebug("Toggle states and bay status updated in database successfully");
            
        } catch (Exception e) {
            System.err.println("Error saving toggle states to database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Updates bay assignment in the database
     */
    private static void updateBayAssignmentInDatabase(String ticketId, String username, int bayNumber) {
        try (java.sql.Connection conn = cephra.Database.DatabaseConnection.getConnection();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(
                 "UPDATE charging_bays SET status = 'Occupied', current_ticket_id = ?, current_username = ?, start_time = NOW() WHERE bay_number = ?")) {
            
            pstmt.setString(1, ticketId);
            pstmt.setString(2, username);
            pstmt.setString(3, "Bay-" + bayNumber);
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                logInfo("Bay-" + bayNumber + " assigned to ticket " + ticketId + " in database");
            } else {
                logWarn("Failed to update bay assignment in database");
            }
            
        } catch (Exception e) {
            logError("Error updating bay assignment in database: " + e.getMessage(), e);
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
                logInfo("Bay-" + bayNumber + " released in database");
            } else {
                logWarn("Failed to update bay release in database");
            }
            
        } catch (Exception e) {
            logError("Error updating bay release in database: " + e.getMessage(), e);
            e.printStackTrace();
        }
    }
    
    /**
     * Smart queue flow: Assigns tickets from waiting to available bays
     * @param ticketId the ticket ID
     * @param username the username
     * @param isFastCharging true for fast charging, false for normal charging
     * @return the assigned bay number (1-8) or -1 if no bay available
     */
    public static int assignTicketFromQueue(String ticketId, String username, boolean isFastCharging) {
        try {
            // Find the next available bay
            int bayNumber = findNextAvailableBay(isFastCharging);
            
            if (bayNumber == -1) {
                logInfo("No available " + (isFastCharging ? "fast" : "normal") + " charging bays for ticket " + ticketId);
                return -1;
            }
            
            // Assign the ticket to the bay
            boolean success = assignTicketToBay(ticketId, username, bayNumber);
            
            if (success) {
                logInfo("Ticket " + ticketId + " assigned to Bay-" + bayNumber + " from queue");
                return bayNumber;
            } else {
                logError("Failed to assign ticket " + ticketId + " to Bay-" + bayNumber);
                return -1;
            }
            
        } catch (Exception e) {
            logError("Error in queue flow assignment: " + e.getMessage(), e);
            e.printStackTrace();
            return -1;
        }
    }
    
    /**
     * Gets the current bay assignment for a ticket
     * @param ticketId the ticket ID
     * @return bay number (1-8) or -1 if not assigned
     */
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
            logError("Error getting ticket bay assignment: " + e.getMessage(), e);
            e.printStackTrace();
        }
        return -1;
    }
    
    /**
     * Gets the current user assigned to a bay
     * @param bayNumber the bay number (1-8)
     * @return username or null if no user assigned
     */
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
            logError("Error getting bay user: " + e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Gets the current ticket assigned to a bay
     * @param bayNumber the bay number (1-8)
     * @return ticket ID or null if no ticket assigned
     */
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
            logError("Error getting bay ticket: " + e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Gets the display text for a bay grid slot (handles maintenance bays)
     * @param bayNumber the bay number (1-8)
     * @return display text or "MAINTENANCE" if bay is unavailable
     */
    public static String getBayDisplayText(int bayNumber) {
        if (!isBayAvailableForCharging(bayNumber)) {
            return "MAINTENANCE";
        }
        
        String ticket = getBayTicket(bayNumber);
        return ticket != null ? ticket : "";
    }
    
    /**
     * Gets the display color for a bay grid slot
     * @param bayNumber the bay number (1-8)
     * @return Color object for the bay display
     */
    public static java.awt.Color getBayDisplayColor(int bayNumber) {
        if (!isBayAvailableForCharging(bayNumber)) {
            return java.awt.Color.RED; // Red for maintenance
        }
        
        String ticket = getBayTicket(bayNumber);
        if (ticket != null && !ticket.isEmpty()) {
            return java.awt.Color.GREEN; // Green for occupied
        }
        
        return java.awt.Color.GRAY; // Gray for available
    }
    
    /**
     * Checks if a bay grid slot should be visible
     * @param bayNumber the bay number (1-8)
     * @return true if slot should be visible, false if in maintenance
     */
    public static boolean isBayGridSlotVisible(int bayNumber) {
        // Always show the slot, but with different styling for maintenance
        return true;
    }
    
    /**
     * Gets the status text for a bay
     * @param bayNumber the bay number (1-8)
     * @return status text (Available, Occupied, Maintenance)
     */
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
    
    /**
     * Updates all bay grid displays with current status (for Queue and Monitor)
     * This method should be called whenever bay status changes
     */
    public static void updateAllBayGridDisplays() {
        // This method will be called by Queue and Monitor components
        // to refresh their grid displays with current bay status
        logDebug("Bay grid displays updated - maintenance bays will show 'OFFLINE' text");
        
        // Notify Queue and Monitor components to refresh their displays
        notifyGridDisplayUpdate();
    }
    
    /**
     * Notifies Queue and Monitor components to update their grid displays
     */
    private static void notifyGridDisplayUpdate() {
        try {
            // Update Queue instance if registered
            if (queueInstance != null) {
                queueInstance.refreshGridDisplays();
            }
            
            // Update Monitor instance if registered
            if (monitorInstance != null) {
                monitorInstance.refreshGridDisplays();
            }
            
        } catch (Exception e) {
            logError("Error updating grid displays: " + e.getMessage(), e);
            e.printStackTrace();
        }
    }
    
    
    
    /**
     * Gets the grid display text for a specific bay slot
     * @param bayNumber the bay number (1-8)
     * @return display text: ticket ID, "OFFLINE", or empty string
     */
    public static String getGridSlotText(int bayNumber) {
        try {
            // Debug: Log which bay we're checking
            if (bayNumber == 1 || bayNumber == 2 || bayNumber == 3 || bayNumber == 6) {
                logDebug("DEBUG: Getting grid slot text for Bay-" + bayNumber);
            }
            
            // Read directly from charging_grid table for real-time ticket status
            try (java.sql.Connection conn = cephra.Database.DatabaseConnection.getConnection();
                 java.sql.PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT cg.ticket_id, cb.status FROM charging_grid cg LEFT JOIN charging_bays cb ON cg.bay_number = cb.bay_number WHERE cg.bay_number = ?")) {
                
                pstmt.setString(1, "Bay-" + bayNumber);
                try (java.sql.ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        String ticketId = rs.getString("ticket_id");
                        String status = rs.getString("status");
                        
                        // Debug: Log Bay details
                        if (bayNumber == 1 || bayNumber == 2 || bayNumber == 3 || bayNumber == 6) {
                            logDebug("DEBUG: Bay-" + bayNumber + " - ticketId: " + ticketId + ", status: " + status);
                        }
                        
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
                        // Debug: Log if no result found
                        if (bayNumber == 1 || bayNumber == 2 || bayNumber == 3 || bayNumber == 6) {
                            logDebug("DEBUG: No result found for Bay-" + bayNumber + " in database query");
                        }
                    }
                }
            }
        } catch (Exception e) {
            logError("Error reading bay status from database for Bay-" + bayNumber + ": " + e.getMessage(), e);
            e.printStackTrace();
        }
        
        // Fallback to static array check
        if (!isBayAvailableForCharging(bayNumber)) {
            return "OFFLINE";
        }
        
        return "";
    }
    
    /**
     * Gets the grid display color for a specific bay slot
     * @param bayNumber the bay number (1-8)
     * @return Color for the grid slot display
     */
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

                        // Occupied coloring based on service type: FCH -> green, NCH -> blue
                        if (ticketId != null && !ticketId.isEmpty()) {
                            if (serviceType != null) {
                                if (serviceType.equalsIgnoreCase("Fast") || ticketId.startsWith("FCH")) {
                                    return java.awt.Color.GREEN;
                                }
                                if (serviceType.equalsIgnoreCase("Normal") || ticketId.startsWith("NCH")) {
                                    return new java.awt.Color(0, 120, 215); // Blue for NCH
                                }
                            }
                            // default occupied color
                            return java.awt.Color.GREEN;
                        }

                        // Available/empty
                        return java.awt.Color.GRAY;
                    }
                }
            }
        } catch (Exception e) {
            logError("Error reading bay color from database: " + e.getMessage(), e);
            e.printStackTrace();
        }

        // Fallback to static array check
        if (!isBayAvailableForCharging(bayNumber)) {
            return java.awt.Color.RED; // Red for offline
        }

        return java.awt.Color.GRAY; // Gray for available
    }
    
    /**
     * Gets all fast charging bay grid texts (for Queue fastslot1-3 and Monitor f1-3)
     * @return array of 3 strings for fast charging grid slots
     */
    public static String[] getFastChargingGridTexts() {
        // Force refresh from database before returning
        loadMaintenanceStatusFromDatabase();
        
        String[] texts = new String[3];
        for (int i = 0; i < 3; i++) {
            texts[i] = getGridSlotText(i + 1); // Bay-1, Bay-2, Bay-3
        }
        
        // Debug: Log fast charging grid texts
        logDebug("DEBUG: Fast charging grid texts: " + java.util.Arrays.toString(texts));
        
        return texts;
    }
    
    /**
     * Gets all normal charging bay grid texts (for Queue normalcharge1-5 and Monitor b1-5)
     * @return array of 5 strings for normal charging grid slots
     */
    public static String[] getNormalChargingGridTexts() {
        // Force refresh from database before returning
        loadMaintenanceStatusFromDatabase();
        
        String[] texts = new String[5];
        for (int i = 0; i < 5; i++) {
            texts[i] = getGridSlotText(i + 4); // Bay-4, Bay-5, Bay-6, Bay-7, Bay-8
        }
        
        // Debug: Log normal charging grid texts
        logDebug("DEBUG: Normal charging grid texts: " + java.util.Arrays.toString(texts));
        
        return texts;
    }
    
    /**
     * Gets all fast charging bay grid colors (for Queue fastslot1-3 and Monitor f1-3)
     * @return array of 3 colors for fast charging grid slots
     */
    public static java.awt.Color[] getFastChargingGridColors() {
        // Force refresh from database before returning
        loadMaintenanceStatusFromDatabase();
        
        java.awt.Color[] colors = new java.awt.Color[3];
        for (int i = 0; i < 3; i++) {
            colors[i] = getGridSlotColor(i + 1); // Bay-1, Bay-2, Bay-3
        }
        return colors;
    }
    
    /**
     * Gets all normal charging bay grid colors (for Queue normalcharge1-5 and Monitor b1-5)
     * @return array of 5 colors for normal charging grid slots
     */
    public static java.awt.Color[] getNormalChargingGridColors() {
        // Force refresh from database before returning
        loadMaintenanceStatusFromDatabase();
        
        java.awt.Color[] colors = new java.awt.Color[5];
        for (int i = 0; i < 5; i++) {
            colors[i] = getGridSlotColor(i + 4); // Bay-4, Bay-5, Bay-6, Bay-7, Bay-8
        }
        return colors;
    }
    
    /**
     * Loads bay occupation status from database on startup
     */
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
                    logInfo("Bay-" + bayNumber + " is occupied by ticket " + currentTicketId + " (user: " + currentUsername + ")");
                }
            }
            
            logInfo("Bay occupation status loaded from database successfully");
            
        } catch (Exception e) {
            logError("Error loading bay occupation from database: " + e.getMessage(), e);
            e.printStackTrace();
        }
    }
    
    /**
     * Initializes grid displays with maintenance status from database
     */
    private static void initializeGridDisplays() {
        try {
            // Ensure database connection is established
            if (!cephra.Database.DatabaseConnection.testConnection()) {
                logError("Database connection failed during grid initialization");
                return;
            }
            
            // Load maintenance status from database
            loadMaintenanceStatusFromDatabase();
            
            // Update all grid displays with current status
            updateAllBayGridDisplays();
            
            logInfo("Grid displays initialized with offline status from database");
            
        } catch (Exception e) {
            logError("Error initializing grid displays: " + e.getMessage(), e);
            e.printStackTrace();
        }
    }
    
    /**
     * Loads maintenance status from database and ensures permanent display
     */
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
                
                if (isMaintenance) {
                    logInfo("Bay-" + bayNumber + " is in maintenance mode - will show 'OFFLINE' text permanently");
                }
            }
            
            logInfo("Maintenance status loaded from database successfully");
            
        } catch (Exception e) {
            logError("Error loading maintenance status from database: " + e.getMessage(), e);
            e.printStackTrace();
        }
    }
    
    /**
     * Ensures maintenance bays are permanently displayed with "MAINTENANCE" text
     * This method should be called whenever the system starts or bay status changes
     */
    public static void ensureMaintenanceDisplay() {
        try {
            // Refresh maintenance status from database
            loadMaintenanceStatusFromDatabase();
            
            // Update all grid displays
            updateAllBayGridDisplays();
            
            // Log maintenance bays
            for (int i = 1; i <= 8; i++) {
                if (!isBayAvailableForCharging(i)) {
                    logInfo("Bay-" + i + " maintenance status: PERMANENT 'OFFLINE' text displayed");
                }
            }
            
        } catch (Exception e) {
            logError("Error ensuring maintenance display: " + e.getMessage(), e);
            e.printStackTrace();
        }
    }
    
    /**
     * Gets the permanent maintenance status for a bay
     * @param bayNumber the bay number (1-8)
     * @return true if bay is permanently in maintenance, false otherwise
     */
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
            logError("Error checking permanent maintenance status: " + e.getMessage(), e);
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Updates bay status in the database
     * @param bayNumber the bay number (1-8)
     * @param status the new status ("Available", "Occupied", "Maintenance")
     */
    private static void updateBayStatusInDatabase(int bayNumber, String status) {
        try (java.sql.Connection conn = cephra.Database.DatabaseConnection.getConnection();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(
                 "UPDATE charging_bays SET status = ? WHERE bay_number = ?")) {
            
            pstmt.setString(1, status);
            pstmt.setString(2, "Bay-" + bayNumber);
            
            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                logInfo("Bay-" + bayNumber + " status updated to: " + status);
            } else {
                logWarn("No rows updated for Bay-" + bayNumber);
            }
            
        } catch (Exception e) {
            logError("Error updating bay status in database: " + e.getMessage(), e);
            e.printStackTrace();
        }
    }
    
    /**
     * Sets some bays to maintenance for testing purposes
     */
  
    
    
    /**
     * Gets the bay number for a given ticket ID
     * @param ticketId the ticket ID to look up
     * @return the bay number (1-8), or null if not found
     */
    public static String getBayNumberByTicket(String ticketId) {
        if (ticketId == null || ticketId.trim().isEmpty()) {
            return null;
        }
        
        try (java.sql.Connection conn = cephra.Database.DatabaseConnection.getConnection()) {
            if (conn == null) {
                logError("Could not establish database connection for bay number lookup");
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
            logError("Error getting bay number by ticket: " + e.getMessage(), e);
        }
        
        return null;
    }
    
    /**
     * Adds a ticket to the waiting grid database
     * @param ticketId the ticket ID
     * @param username the username
     * @param serviceType the service type (Fast/Normal)
     * @param batteryLevel the initial battery level
     * @return the slot number where the ticket was placed, or -1 if failed
     */
    public static int addTicketToWaitingGrid(String ticketId, String username, String serviceType, int batteryLevel) {
        try {
            // Check if there's charging capacity for this service type
            boolean isFastCharging = false;
            if (serviceType != null) {
                String svc = serviceType.trim().toLowerCase();
                isFastCharging = svc.contains("fast");
            }
            if (!hasChargingCapacity(isFastCharging)) {
                logInfo("No available charging bays for " + serviceType + " service. Ticket " + ticketId + " cannot be added to waiting grid.");
                return -1;
            }
            
            // Check if we can directly assign to a charging bay instead of waiting
            int availableBay = findNextAvailableBay(isFastCharging);
            if (availableBay > 0) {
                // Directly assign to charging bay
                if (moveTicketFromWaitingToCharging(ticketId, availableBay)) {
                    logInfo("Ticket " + ticketId + " directly assigned to Bay-" + availableBay + " (no waiting needed)");
                    return availableBay;
                }
            }
            
            // If no direct assignment possible, add to waiting grid
            try (java.sql.Connection conn = cephra.Database.DatabaseConnection.getConnection();
                 java.sql.PreparedStatement pstmt = conn.prepareStatement(
                     "UPDATE waiting_grid SET ticket_id = ?, username = ?, service_type = ?, initial_battery_level = ?, position_in_queue = ? WHERE slot_number = ? AND ticket_id IS NULL")) {
                
                // Find the first available slot
                int availableSlot = findNextAvailableWaitingSlot();
                if (availableSlot == -1) {
                    logInfo("No available waiting slots for ticket: " + ticketId);
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
                    logInfo("Ticket " + ticketId + " added to waiting grid slot " + availableSlot);
                    return availableSlot;
                }
            }
            
        } catch (Exception e) {
            logError("Error adding ticket to waiting grid: " + e.getMessage(), e);
            e.printStackTrace();
        }
        return -1;
    }
    
    /**
     * Finds the next available waiting slot
     * @return slot number (1-10) or -1 if no slots available
     */
    private static int findNextAvailableWaitingSlot() {
        try (java.sql.Connection conn = cephra.Database.DatabaseConnection.getConnection();
             java.sql.Statement stmt = conn.createStatement();
             java.sql.ResultSet rs = stmt.executeQuery("SELECT slot_number FROM waiting_grid WHERE ticket_id IS NULL ORDER BY slot_number LIMIT 1")) {
            
            if (rs.next()) {
                return rs.getInt("slot_number");
            }
            
        } catch (Exception e) {
            logError("Error finding available waiting slot: " + e.getMessage(), e);
            e.printStackTrace();
        }
        return -1;
    }
    
    /**
     * Moves a ticket from waiting grid to charging bay
     * @param ticketId the ticket ID to move
     * @param bayNumber the bay number (1-8)
     * @return true if successful, false otherwise
     */
    public static boolean moveTicketFromWaitingToCharging(String ticketId, int bayNumber) {
        logDebug("BayManagement: Starting moveTicketFromWaitingToCharging for ticket " + ticketId + " to Bay-" + bayNumber);
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
                            logInfo("BayManagement: Found ticket " + ticketId + " in waiting grid - username: " + username + ", service: " + serviceType + ", battery: " + batteryLevel);
                        } else {
                            logError("BayManagement: Ticket " + ticketId + " not found in waiting grid");
                            return false;
                        }
                    }
                }
                
                // Remove from waiting grid
                try (java.sql.PreparedStatement pstmt = conn.prepareStatement(
                        "UPDATE waiting_grid SET ticket_id = NULL, username = NULL, service_type = NULL, initial_battery_level = NULL, position_in_queue = NULL WHERE ticket_id = ?")) {
                    pstmt.setString(1, ticketId);
                    int waitingRowsUpdated = pstmt.executeUpdate();
                    logInfo("BayManagement: Removed ticket " + ticketId + " from waiting grid - rows updated: " + waitingRowsUpdated);
                }
                
                // Add to charging grid
                try (java.sql.PreparedStatement pstmt = conn.prepareStatement(
                        "UPDATE charging_grid SET ticket_id = ?, username = ?, service_type = ?, initial_battery_level = ?, start_time = CURRENT_TIMESTAMP WHERE bay_number = ?")) {
                    pstmt.setString(1, ticketId);
                    pstmt.setString(2, username);
                    pstmt.setString(3, serviceType);
                    pstmt.setInt(4, batteryLevel);
                    pstmt.setString(5, "Bay-" + bayNumber);
                    int chargingRowsUpdated = pstmt.executeUpdate();
                    logInfo("BayManagement: Added ticket " + ticketId + " to charging grid Bay-" + bayNumber + " - rows updated: " + chargingRowsUpdated);
                }
                
                // Update charging_bays table
                try (java.sql.PreparedStatement pstmt = conn.prepareStatement(
                        "UPDATE charging_bays SET status = 'Occupied', current_ticket_id = ?, current_username = ?, start_time = CURRENT_TIMESTAMP WHERE bay_number = ?")) {
                    pstmt.setString(1, ticketId);
                    pstmt.setString(2, username);
                    pstmt.setString(3, "Bay-" + bayNumber);
                    int bayRowsUpdated = pstmt.executeUpdate();
                    logInfo("BayManagement: Updated charging_bays Bay-" + bayNumber + " to Occupied - rows updated: " + bayRowsUpdated);
                }
                
                conn.commit();
                logInfo("Ticket " + ticketId + " moved from waiting grid to Bay-" + bayNumber);
                
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
            logError("Error moving ticket from waiting to charging: " + e.getMessage(), e);
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Gets all waiting grid tickets
     * @return array of ticket IDs in waiting grid (empty strings for empty slots)
     */
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
                logInfo("Waiting grid table doesn't exist yet. H2DatabaseConnection should have created it.");
                // Return empty array for now
                return new String[10];
            } else {
                logError("Error getting waiting grid tickets: " + e.getMessage(), e);
                e.printStackTrace();
            }
        } catch (Exception e) {
            logError("Error getting waiting grid tickets: " + e.getMessage(), e);
            e.printStackTrace();
        }
        return tickets;
    }
    
    /**
     * Gets all charging grid tickets
     * @return array of ticket IDs in charging bays (empty strings for empty bays)
     */
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
                logInfo("Charging grid table doesn't exist yet. H2DatabaseConnection should have created it.");
                // Return empty array for now
                return new String[8];
            } else {
                logError("Error getting charging grid tickets: " + e.getMessage(), e);
                e.printStackTrace();
            }
        } catch (Exception e) {
            logError("Error getting charging grid tickets: " + e.getMessage(), e);
            e.printStackTrace();
        }
        return tickets;
    }
    
    /**
     * Counts the number of available bays for a specific service type (excluding offline bays)
     * @param isFastCharging true for fast charging, false for normal charging
     * @return number of available bays
     */
    public static int countAvailableBays(boolean isFastCharging) {
        int count = 0;
        logDebug("BayManagement: Counting available " + (isFastCharging ? "fast" : "normal") + " charging bays...");
        
        if (isFastCharging) {
            // Count available fast charging bays (1-3)
            for (int i = 1; i <= 3; i++) {
                boolean available = isBayAvailableForCharging(i);
                logDebug("BayManagement: Bay-" + i + " available: " + available);
                if (available) {
                    count++;
                }
            }
        } else {
            // Count available normal charging bays (4-8)
            for (int i = 4; i <= 8; i++) {
                boolean available = isBayAvailableForCharging(i);
                logDebug("BayManagement: Bay-" + i + " available: " + available);
                if (available) {
                    count++;
                }
            }
        }
        
        logDebug("BayManagement: Total available " + (isFastCharging ? "fast" : "normal") + " charging bays: " + count);
        return count;
    }
    
    /**
     * Counts the number of occupied bays for a specific service type
     * @param isFastCharging true for fast charging, false for normal charging
     * @return number of occupied bays
     */
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
            logError("Error counting occupied bays: " + e.getMessage(), e);
            e.printStackTrace();
        }
        return count;
    }
    
    /**
     * Checks if there's capacity for a new ticket in the charging bays
     * @param isFastCharging true for fast charging, false for normal charging
     * @return true if there's at least one available bay
     */
    public static boolean hasChargingCapacity(boolean isFastCharging) {
        return countAvailableBays(isFastCharging) > 0;
    }
    
    /**
     * Synchronizes the static bay availability arrays with the database
     * This should be called during initialization to ensure consistency
     */
    public static void synchronizeBayStatusWithDatabase() {
        logDebug("*** synchronizeBayStatusWithDatabase() DISABLED to prevent toggle reset ***");
        // Method disabled to prevent toggle states from being reset
        // This method was overriding the toggle states and causing Bay-2 and Bay-6 to reset
    }
    
    
    /**
     * Debug method to print current state of static arrays
     */
    public static void printCurrentBayStatus() {
        logDebug("=== CURRENT BAY STATUS (Static Arrays) ===");
        logDebug("Fast Charging Bays (1-3):");
        for (int i = 0; i < 3; i++) {
            logDebug("  Bay-" + (i + 1) + " - Available: " + fastChargingAvailable[i] + ", Occupied: " + fastChargingOccupied[i]);
        }
        logDebug("Normal Charging Bays (4-8):");
        for (int i = 0; i < 5; i++) {
            logDebug("  Bay-" + (i + 4) + " - Available: " + normalChargingAvailable[i] + ", Occupied: " + normalChargingOccupied[i]);
        }
        logDebug("=== END BAY STATUS ===");
    }
    
    /**
     * Automatically moves waiting tickets to available charging bays
     * This method should be called when bay status changes
     */
    public static void autoAssignWaitingTickets() {
        try {
            logInfo("Auto-assigning waiting tickets to available bays...");
            
            // Get all waiting tickets
            try (java.sql.Connection conn = cephra.Database.DatabaseConnection.getConnection();
                 java.sql.Statement stmt = conn.createStatement();
                 java.sql.ResultSet rs = stmt.executeQuery("SELECT ticket_id, service_type FROM waiting_grid WHERE ticket_id IS NOT NULL ORDER BY position_in_queue")) {
                
                while (rs.next()) {
                    String ticketId = rs.getString("ticket_id");
                    String serviceType = rs.getString("service_type");
                    
                    if (ticketId != null && !ticketId.isEmpty()) {
                        boolean isFastCharging = "Fast".equals(serviceType);
                        int bayNumber = findNextAvailableBay(isFastCharging);
                        
                        if (bayNumber > 0) {
                            // Move ticket to available bay
                            if (moveTicketFromWaitingToCharging(ticketId, bayNumber)) {
                                logInfo("Auto-assigned ticket " + ticketId + " to Bay-" + bayNumber);
                                // Update grid displays
                                updateAllBayGridDisplays();
                            }
                        } else {
                            logInfo("No available " + (isFastCharging ? "fast" : "normal") + " charging bays for ticket " + ticketId);
                        }
                    }
                }
            }
            
        } catch (Exception e) {
            logError("Error auto-assigning waiting tickets: " + e.getMessage(), e);
            e.printStackTrace();
        }
    }
    
    /**
     * Clears charging bay and grid for a completed ticket
     * @param ticketId the ticket ID to clear
     * @return true if successful, false otherwise
     */
    public static boolean clearChargingBayForCompletedTicket(String ticketId) {
        logInfo("BayManagement: Clearing charging bay for completed ticket " + ticketId);
        
        try (java.sql.Connection conn = cephra.Database.DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            
            try {
                // Clear charging_bays table
                try (java.sql.PreparedStatement pstmt = conn.prepareStatement(
                        "UPDATE charging_bays SET current_ticket_id = NULL, current_username = NULL, status = 'Available', start_time = NULL WHERE current_ticket_id = ?")) {
                    pstmt.setString(1, ticketId);
                    int bayRowsUpdated = pstmt.executeUpdate();
                    logDebug("BayManagement: Cleared charging bay for ticket " + ticketId + " - rows updated: " + bayRowsUpdated);
                }
                
                // Clear charging_grid table
                try (java.sql.PreparedStatement pstmt = conn.prepareStatement(
                        "UPDATE charging_grid SET ticket_id = NULL, username = NULL, service_type = NULL, initial_battery_level = NULL, start_time = NULL WHERE ticket_id = ?")) {
                    pstmt.setString(1, ticketId);
                    int gridRowsUpdated = pstmt.executeUpdate();
                    logDebug("BayManagement: Cleared charging grid for ticket " + ticketId + " - rows updated: " + gridRowsUpdated);
                }
                
                conn.commit();
                logInfo("BayManagement: Successfully cleared charging bay and grid for ticket " + ticketId);
                
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
            logError("BayManagement: Error clearing charging bay for ticket " + ticketId + ": " + e.getMessage(), e);
            e.printStackTrace();
            return false;
        }
    }
    
   
    public static void displayCapacityStatus() {
        // Cleaned: no-op helper retained for compatibility
    }
    
    /**
     * Crash-recovery: unlock tickets that are stuck when the app was closed
     * during payment. Ensures such tickets become actionable again.
     * Strategy:
     *  - For tickets in queue_tickets with status='Pending' AND payment_status='Pending':
     *      * Remove any stale rows in active_tickets
     *      * Clear charging_grid entries referencing the ticket
     *      * Release any charging_bays holding the ticket (set Available)
     */
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
                logInfo("Recovered " + tickets.size() + " pending-payment ticket(s) from stale state (if any)");
            } catch (Exception ex) {
                conn.rollback();
                logError("Recovery of pending-payment tickets failed: " + ex.getMessage(), ex);
            } finally {
                try { conn.setAutoCommit(true); } catch (Exception ignore) {}
            }
        } catch (Exception e) {
            logError("Recovery connection error: " + e.getMessage(), e);
        }
    }
}