package cephra.Admin;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BayManagement extends javax.swing.JPanel {
    
    // Static variables to track available bays
    public static boolean[] fastChargingAvailable = {true, true, true}; // Bays 1-3
    public static boolean[] normalChargingAvailable = {true, true, true, true, true}; // Bays 4-8
    
    // Static variables to track occupied bays
    public static boolean[] fastChargingOccupied = {false, false, false}; // Bays 1-3
    public static boolean[] normalChargingOccupied = {false, false, false, false, false}; // Bays 4-8
    
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
        int index = bayNumber - 1;
        if (bayNumber >= 1 && bayNumber <= 3) {
            return fastChargingAvailable[index] && !fastChargingOccupied[index];
        } else if (bayNumber >= 4 && bayNumber <= 8) {
            return normalChargingAvailable[index - 3] && !normalChargingOccupied[index - 3];
        }
        return false;
    }
    
    public static int getAvailableFastChargingCount() {
        int count = 0;
        for (int i = 0; i < fastChargingAvailable.length; i++) {
            if (fastChargingAvailable[i] && !fastChargingOccupied[i]) {
                count++;
            }
        }
        return count;
    }
    
    public static int getAvailableNormalChargingCount() {
        int count = 0;
        for (int i = 0; i < normalChargingAvailable.length; i++) {
            if (normalChargingAvailable[i] && !normalChargingOccupied[i]) {
                count++;
            }
        }
        return count;
    }
    
    public static void loadToggleStates() {
        System.out.println("Loading toggle states from database...");
        try {
            // Create bay_toggle_states table if it doesn't exist
            createToggleStatesTable();
            
            // Load toggle states from database
            loadToggleStatesFromDatabase();
            
        } catch (Exception e) {
            System.err.println("Error loading toggle states: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void saveToggleStates() {
        System.out.println("Saving toggle states to database...");
        try {
            // Save toggle states to database
            saveToggleStatesToDatabase();
            
            System.out.println("Fast Charging Available: " + getAvailableFastChargingCount() + "/3");
            System.out.println("Normal Charging Available: " + getAvailableNormalChargingCount() + "/5");
            
        } catch (Exception e) {
            System.err.println("Error saving toggle states: " + e.getMessage());
            e.printStackTrace();
        }
    }
  
    public BayManagement() {
        initComponents();
        setPreferredSize(new java.awt.Dimension(1000, 750));
        setSize(1000, 750);
        setupDateTimeTimer();
        
        // Load toggle states from database first
        loadToggleStates();
        
        setupIOSToggles();
          
        // Set default state to Available for all bays
        SwingUtilities.invokeLater(() -> {
            bay1.setText("Available");
            bay1.setForeground(new java.awt.Color(0, 128, 0));
            bay2.setText("Available");
            bay2.setForeground(new java.awt.Color(0, 128, 0));
            bay3.setText("Available");
            bay3.setForeground(new java.awt.Color(0, 128, 0));
            bay4.setText("Available");
            bay4.setForeground(new java.awt.Color(0, 128, 0));
            bay5.setText("Available");
            bay5.setForeground(new java.awt.Color(0, 128, 0));
            bay6.setText("Available");
            bay6.setForeground(new java.awt.Color(0, 128, 0));
            bay7.setText("Available");
            bay7.setForeground(new java.awt.Color(0, 128, 0));
            bay8.setText("Available");
            bay8.setForeground(new java.awt.Color(0, 128, 0));
        });
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

        datetime.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        datetime.setForeground(new java.awt.Color(255, 255, 255));
        datetime.setText("10:44 AM 17 August, Sunday");
        add(datetime);
        datetime.setBounds(820, 40, 170, 20);

        quebutton.setBorder(null);
        quebutton.setBorderPainted(false);
        quebutton.setContentAreaFilled(false);
        quebutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quebuttonActionPerformed(evt);
            }
        });
        add(quebutton);
        quebutton.setBounds(270, 10, 100, 40);

        staffbutton.setBorder(null);
        staffbutton.setBorderPainted(false);
        staffbutton.setContentAreaFilled(false);
        staffbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                staffbuttonActionPerformed(evt);
            }
        });
        add(staffbutton);
        staffbutton.setBounds(500, 20, 110, 40);

        businessbutton.setBorder(null);
        businessbutton.setBorderPainted(false);
        businessbutton.setContentAreaFilled(false);
        businessbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                businessbuttonActionPerformed(evt);
            }
        });
        add(businessbutton);
        businessbutton.setBounds(610, 10, 140, 40);

        historybutton.setBorder(null);
        historybutton.setBorderPainted(false);
        historybutton.setContentAreaFilled(false);
        historybutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                historybuttonActionPerformed(evt);
            }
        });
        add(historybutton);
        historybutton.setBounds(430, 10, 60, 40);

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

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Photos/Bay.png"))); // NOI18N
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
            ((cephra.Frame.Admin) w).switchPanel(new cephra.Admin.Dashboard());
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
            
            // Set initial state
            if (i < 3) { // Fast charging bays
                toggle.setSelected(fastChargingAvailable[i]);
            } else { // Normal charging bays
                toggle.setSelected(normalChargingAvailable[i - 3]);
            }
            
            // Apply iOS-style customization
            customizeToggleForIOS(toggle, i < 3);
            
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
                    
                    saveToggleStates();
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
        try (java.sql.Connection conn = cephra.db.DatabaseConnection.getConnection();
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
            
            System.out.println("Bay toggle states table created/verified successfully");
            
        } catch (Exception e) {
            System.err.println("Error creating toggle states table: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Loads toggle states from the database
     */
    private static void loadToggleStatesFromDatabase() {
        try (java.sql.Connection conn = cephra.db.DatabaseConnection.getConnection();
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
                    System.out.println("Toggle states loaded from bay_toggle_states table successfully");
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
                
                System.out.println("Toggle states loaded from charging_bays table successfully");
            }
            
        } catch (Exception e) {
            System.err.println("Error loading toggle states from database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Saves toggle states to the database
     */
    private static void saveToggleStatesToDatabase() {
        try (java.sql.Connection conn = cephra.db.DatabaseConnection.getConnection()) {
            
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
            
            System.out.println("Toggle states and bay status updated in database successfully");
            
        } catch (Exception e) {
            System.err.println("Error saving toggle states to database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}