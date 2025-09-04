package cephra.Admin;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Bay extends javax.swing.JPanel {
//
   
    // Static variables to track available bays
    public static boolean[] fastChargingAvailable = {true, true, true}; // Bays 1-3
    public static boolean[] normalChargingAvailable = {true, true, true, true, true}; // Bays 4-8
    
    // Static variables to track occupied bays
    public static boolean[] fastChargingOccupied = {false, false, false}; // Bays 1-3
    public static boolean[] normalChargingOccupied = {false, false, false, false, false}; // Bays 4-8
    
    // Toggle buttons for bay availability
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
    
    // Method to check if a specific bay is available for charging
    public static boolean isBayAvailableForCharging(int bayNumber) {
        // Bay numbers are 1-indexed, arrays are 0-indexed
        int index = bayNumber - 1;
        
        // Check if it's a fast charging bay (1-3)
        if (bayNumber >= 1 && bayNumber <= 3) {
            return fastChargingAvailable[index] && !fastChargingOccupied[index];
        }
        // Check if it's a normal charging bay (4-8)
        else if (bayNumber >= 4 && bayNumber <= 8) {
            return normalChargingAvailable[index - 3] && !normalChargingOccupied[index - 3];
        }
        
        return false; // Invalid bay number
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
    
    // Method to load toggle states from the database
    public static void loadToggleStates() {
        System.out.println("Loading toggle states...");
        
        // Could add database loading of toggle states here if needed
    }
    
    // Method to save toggle states
    public static void saveToggleStates() {
        // This would ideally save to database, but for now we'll just use the static variables
        System.out.println("Saving toggle states...");
        System.out.println("Fast Charging Available: " + getAvailableFastChargingCount() + "/3");
        System.out.println("Normal Charging Available: " + getAvailableNormalChargingCount() + "/5");
    }
  
    public Bay() {
        initComponents();
        setPreferredSize(new java.awt.Dimension(1000, 750));
        setSize(1000, 750);
        setupDateTimeTimer();
        setupBayToggleButtons();
          
        // Set default state to Available for all bays
        SwingUtilities.invokeLater(() -> {
            bay1.setText("Available");
            bay1.setForeground(new java.awt.Color(0, 128, 0)); // Green color for available
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

        bay8.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        bay8.setText("Available");
        add(bay8);
        bay8.setBounds(390, 650, 150, 32);

        bay7.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        bay7.setText("Available");
        add(bay7);
        bay7.setBounds(90, 640, 150, 32);

        bay6.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        bay6.setText("Available");
        add(bay6);
        bay6.setBounds(690, 460, 150, 32);

        bay5.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        bay5.setText("Available");
        add(bay5);
        bay5.setBounds(380, 460, 150, 32);

        bay4.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        bay4.setText("Available");
        add(bay4);
        bay4.setBounds(80, 460, 150, 32);

        bay3.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        bay3.setText("Available");
        add(bay3);
        bay3.setBounds(680, 270, 150, 32);

        bay2.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        bay2.setText("Available");
        add(bay2);
        bay2.setBounds(380, 270, 150, 32);

        bay1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        bay1.setText("Available");
        add(bay1);
        bay1.setBounds(100, 270, 150, 32);

        datetime.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
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

    private void quebuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quebuttonActionPerformed
        Window w = SwingUtilities.getWindowAncestor(Bay.this);
        if (w instanceof cephra.Frame.Admin) {
            ((cephra.Frame.Admin) w).switchPanel(new Queue());
        }
    }//GEN-LAST:event_quebuttonActionPerformed

    private void staffbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_staffbuttonActionPerformed
        Window w = SwingUtilities.getWindowAncestor(Bay.this);
        if (w instanceof cephra.Frame.Admin) {
            ((cephra.Frame.Admin) w).switchPanel(new StaffRecord());
        }
    }//GEN-LAST:event_staffbuttonActionPerformed

    private void businessbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_businessbuttonActionPerformed
        Window w = SwingUtilities.getWindowAncestor(Bay.this);
        if (w instanceof cephra.Frame.Admin) {
            ((cephra.Frame.Admin) w).switchPanel(new cephra.Admin.Dashboard());
        }
    }//GEN-LAST:event_businessbuttonActionPerformed

    private void historybuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_historybuttonActionPerformed
        Window w = SwingUtilities.getWindowAncestor(Bay.this);
        if (w instanceof cephra.Frame.Admin) {
            ((cephra.Frame.Admin) w).switchPanel(new History());
        }
    }//GEN-LAST:event_historybuttonActionPerformed

    private void exitloginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitloginActionPerformed
        Window w = SwingUtilities.getWindowAncestor(Bay.this);
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
    // End of variables declaration//GEN-END:variables
    
    @SuppressWarnings("unused")
    private String getLoggedInUsername() {
        try {
            java.awt.Window window = javax.swing.SwingUtilities.getWindowAncestor(this);
            if (window instanceof cephra.Frame.Admin) {
                // Use reflection to get the loggedInUsername field
                java.lang.reflect.Field usernameField = window.getClass().getDeclaredField("loggedInUsername");
                usernameField.setAccessible(true);
                return (String) usernameField.get(window);
            }
        } catch (Exception e) {
            System.err.println("Error getting logged-in username: " + e.getMessage());
        }
        return "Admin"; // Fallback
    }
    
    /**
     * Sets up the toggle buttons for controlling bay availability
     */
    private void setupBayToggleButtons() {
        // Create toggle buttons for each bay
        for (int i = 0; i < 8; i++) {
            final int bayIndex = i;
            bayToggleButtons[i] = new JToggleButton();
            bayToggleButtons[i].setText("Bay " + (i + 1));
            
            // Set initial state based on availability arrays
            if (i < 3) { // Fast charging bays (0-2)
                bayToggleButtons[i].setSelected(fastChargingAvailable[i]);
                bayToggleButtons[i].setText("Fast Bay " + (i + 1));
                bayToggleButtons[i].setBackground(new Color(0, 150, 255)); // Blue for fast charging
            } else { // Normal charging bays (3-7)
                bayToggleButtons[i].setSelected(normalChargingAvailable[i - 3]);
                bayToggleButtons[i].setText("Normal Bay " + (i + 1));
                bayToggleButtons[i].setBackground(new Color(0, 180, 0)); // Green for normal charging
            }
            
            // Style the toggle button
            bayToggleButtons[i].setForeground(Color.WHITE);
            bayToggleButtons[i].setFont(new Font("Segoe UI", Font.BOLD, 14));
            bayToggleButtons[i].setFocusPainted(false);
            
            // Add action listener to update availability arrays when toggled
            bayToggleButtons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JToggleButton source = (JToggleButton) e.getSource();
                    boolean isAvailable = source.isSelected();
                    
                    // Update the appropriate availability array
                    if (bayIndex < 3) { // Fast charging bays
                        fastChargingAvailable[bayIndex] = isAvailable;
                        // Update the bay label
                        JLabel bayLabel = getBayLabel(bayIndex + 1);
                        if (bayLabel != null) {
                            updateBayLabel(bayLabel, isAvailable);
                        }
                    } else { // Normal charging bays
                        normalChargingAvailable[bayIndex - 3] = isAvailable;
                        // Update the bay label
                        JLabel bayLabel = getBayLabel(bayIndex + 1);
                        if (bayLabel != null) {
                            updateBayLabel(bayLabel, isAvailable);
                        }
                    }
                    
                    // Save the updated toggle states
                    saveToggleStates();
                }
            });
            
            // Add the toggle button to the panel
            add(bayToggleButtons[i]);
            
            // Position the toggle buttons below the bay labels
            if (i < 3) { // First row - Fast charging bays
                bayToggleButtons[i].setBounds(230 + (i * 310), 275, 40, 40);
            } else if (i < 6) { // Second row - Normal charging bays 4-6
                bayToggleButtons[i].setBounds(230 + ((i - 3) * 310), 475, 150, 40);
            } else { // Third row - Normal charging bays 7-8
                bayToggleButtons[i].setBounds(230 + ((i - 6) * 310), 645, 150, 40);
            }
        }
    }
    
    /**
     * Gets the JLabel for a specific bay number
     * @param bayNumber The bay number (1-8)
     * @return The JLabel for the bay, or null if not found
     */
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
    
    /**
     * Updates a bay label based on availability
     * @param label The JLabel to update
     * @param isAvailable Whether the bay is available
     */
    private void updateBayLabel(JLabel label, boolean isAvailable) {
        if (isAvailable) {
            label.setText("Available");
            label.setForeground(new java.awt.Color(0, 128, 0)); // Green for available
        } else {
            label.setText("Unavailable");
            label.setForeground(new java.awt.Color(255, 0, 0)); // Red for unavailable
        }
    }
}
