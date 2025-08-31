package cephra.Admin;

import java.awt.Window;
import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import cephra.db.DatabaseConnection;


public class Bay extends javax.swing.JPanel {
//
    // Static variables to track available bays
    public static boolean[] fastChargingAvailable = {true, true, true}; // Bays 1-3
    public static boolean[] normalChargingAvailable = {true, true, true, true, true}; // Bays 4-8
    
    // Static variables to track occupied bays
    public static boolean[] fastChargingOccupied = {false, false, false}; // Bays 1-3
    public static boolean[] normalChargingOccupied = {false, false, false, false, false}; // Bays 4-8
    
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
        
        // Load toggle states from database or static variables
        loadToggleStates();
        
        // Load toggle states from static variables
        SwingUtilities.invokeLater(() -> {
            System.out.println("Setting toggle UI states...");
            
            // Set toggle switches based on availability
            toggleSwitch1.setSelected(fastChargingAvailable[0]);
            toggleSwitch2.setSelected(fastChargingAvailable[1]);
            toggleSwitch3.setSelected(fastChargingAvailable[2]);
            toggleSwitch4.setSelected(normalChargingAvailable[0]);
            toggleSwitch5.setSelected(normalChargingAvailable[1]);
            toggleSwitch6.setSelected(normalChargingAvailable[2]);
            toggleSwitch7.setSelected(normalChargingAvailable[3]);
            toggleSwitch8.setSelected(normalChargingAvailable[4]);
            
            // Update bay labels based on toggle state
            updateBayLabel(bay1, fastChargingAvailable[0]);
            updateBayLabel(bay2, fastChargingAvailable[1]);
            updateBayLabel(bay3, fastChargingAvailable[2]);
            updateBayLabel(bay4, normalChargingAvailable[0]);
            updateBayLabel(bay5, normalChargingAvailable[1]);
            updateBayLabel(bay6, normalChargingAvailable[2]);
            updateBayLabel(bay7, normalChargingAvailable[3]);
            updateBayLabel(bay8, normalChargingAvailable[4]);
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
        exitlogin = new javax.swing.JButton();
        historybutton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setLayout(null);

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

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Photos/Bay.png"))); // NOI18N
        add(jLabel1);
        jLabel1.setBounds(0, 0, 1000, 750);

        toggleSwitch1 = new javax.swing.JToggleButton("ON/OFF");
        toggleSwitch1.setOpaque(true);
        toggleSwitch1.setBackground(new java.awt.Color(255, 255, 255));
        add(toggleSwitch1);
        toggleSwitch1.setBounds(260, 270, 60, 30);
        
        // Test button to see if components are visible
        javax.swing.JButton testButton = new javax.swing.JButton("TEST");
        testButton.setBounds(260, 200, 60, 30);
        testButton.setBackground(java.awt.Color.RED);
        add(testButton);

        toggleSwitch2 = new javax.swing.JToggleButton("ON/OFF");
        toggleSwitch2.setOpaque(true);
        toggleSwitch2.setBackground(new java.awt.Color(255, 255, 255));
        add(toggleSwitch2);
        toggleSwitch2.setBounds(570, 270, 60, 30);

        toggleSwitch3 = new javax.swing.JToggleButton("ON/OFF");
        toggleSwitch3.setOpaque(true);
        toggleSwitch3.setBackground(new java.awt.Color(255, 255, 255));
        add(toggleSwitch3);
        toggleSwitch3.setBounds(890, 270, 60, 30);

        toggleSwitch4 = new javax.swing.JToggleButton("ON/OFF");
        toggleSwitch4.setOpaque(true);
        toggleSwitch4.setBackground(new java.awt.Color(255, 255, 255));
        add(toggleSwitch4);
        toggleSwitch4.setBounds(260, 460, 60, 30);

        toggleSwitch5 = new javax.swing.JToggleButton("ON/OFF");
        toggleSwitch5.setOpaque(true);
        toggleSwitch5.setBackground(new java.awt.Color(255, 255, 255));
        add(toggleSwitch5);
        toggleSwitch5.setBounds(560, 460, 60, 30);

        toggleSwitch6 = new javax.swing.JToggleButton("ON/OFF");
        toggleSwitch6.setOpaque(true);
        toggleSwitch6.setBackground(new java.awt.Color(255, 255, 255));
        add(toggleSwitch6);
        toggleSwitch6.setBounds(880, 460, 60, 30);

        toggleSwitch7 = new javax.swing.JToggleButton("ON/OFF");
        toggleSwitch7.setOpaque(true);
        toggleSwitch7.setBackground(new java.awt.Color(255, 255, 255));
        add(toggleSwitch7);
        toggleSwitch7.setBounds(260, 645, 60, 30);

        toggleSwitch8 = new javax.swing.JToggleButton("ON/OFF");
        toggleSwitch8.setOpaque(true);
        toggleSwitch8.setBackground(new java.awt.Color(255, 255, 255));
        add(toggleSwitch8);
        toggleSwitch8.setBounds(560, 645, 60, 30);

        toggleSwitch1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleSwitch1ActionPerformed(evt);
            }
        });

        toggleSwitch2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleSwitch2ActionPerformed(evt);
            }
        });

        toggleSwitch3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleSwitch3ActionPerformed(evt);
            }
        });

        toggleSwitch4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleSwitch4ActionPerformed(evt);
            }
        });

        toggleSwitch5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleSwitch5ActionPerformed(evt);
            }
        });

        toggleSwitch6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleSwitch6ActionPerformed(evt);
            }
        });

        toggleSwitch7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleSwitch7ActionPerformed(evt);
            }
        });

        toggleSwitch8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleSwitch8ActionPerformed(evt);
            }
        });
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

    // Helper method to update bay label based on availability
    private void updateBayLabel(javax.swing.JLabel bayLabel, boolean available) {
        if (!available) {
            bayLabel.setText("Unavailable");
            bayLabel.setForeground(new java.awt.Color(255, 0, 0)); // Red color for unavailable
        } else {
            bayLabel.setText("Available");
            bayLabel.setForeground(new java.awt.Color(0, 128, 0)); // Green color for available
        }
    }
    
    private void toggleSwitch1ActionPerformed(java.awt.event.ActionEvent evt) {
        if (toggleSwitch1.isSelected()) {
            fastChargingAvailable[0] = true;
        } else {
            fastChargingAvailable[0] = false;
        }
        updateBayLabel(bay1, fastChargingAvailable[0]);
        System.out.println("Fast Charging Bays Available: " + getAvailableFastChargingCount());
        saveToggleStates();
    }
    
    private void toggleSwitch2ActionPerformed(java.awt.event.ActionEvent evt) {
        if (toggleSwitch2.isSelected()) {
            fastChargingAvailable[1] = true;
        } else {
            fastChargingAvailable[1] = false;
        }
        updateBayLabel(bay2, fastChargingAvailable[1]);
        System.out.println("Fast Charging Bays Available: " + getAvailableFastChargingCount());
        saveToggleStates();
    }
    
    private void toggleSwitch3ActionPerformed(java.awt.event.ActionEvent evt) {
        if (toggleSwitch3.isSelected()) {
            fastChargingAvailable[2] = true;
        } else {
            fastChargingAvailable[2] = false;
        }
        updateBayLabel(bay3, fastChargingAvailable[2]);
        System.out.println("Fast Charging Bays Available: " + getAvailableFastChargingCount());
        saveToggleStates();
    }
    
    private void toggleSwitch4ActionPerformed(java.awt.event.ActionEvent evt) {
        if (toggleSwitch4.isSelected()) {
            normalChargingAvailable[0] = true;
        } else {
            normalChargingAvailable[0] = false;
        }
        updateBayLabel(bay4, normalChargingAvailable[0]);
        System.out.println("Normal Charging Bays Available: " + getAvailableNormalChargingCount());
        saveToggleStates();
    }
    
    private void toggleSwitch5ActionPerformed(java.awt.event.ActionEvent evt) {
        if (toggleSwitch5.isSelected()) {
            normalChargingAvailable[1] = true;
        } else {
            normalChargingAvailable[1] = false;
        }
        updateBayLabel(bay5, normalChargingAvailable[1]);
        System.out.println("Normal Charging Bays Available: " + getAvailableNormalChargingCount());
        saveToggleStates();
    }
    
    private void toggleSwitch6ActionPerformed(java.awt.event.ActionEvent evt) {
        if (toggleSwitch6.isSelected()) {
            normalChargingAvailable[2] = true;
        } else {
            normalChargingAvailable[2] = false;
        }
        updateBayLabel(bay6, normalChargingAvailable[2]);
        System.out.println("Normal Charging Bays Available: " + getAvailableNormalChargingCount());
        saveToggleStates();
    }
    
    private void toggleSwitch7ActionPerformed(java.awt.event.ActionEvent evt) {
        if (toggleSwitch7.isSelected()) {
            normalChargingAvailable[3] = true;
        } else {
            normalChargingAvailable[3] = false;
        }
        updateBayLabel(bay7, normalChargingAvailable[3]);
        System.out.println("Normal Charging Bays Available: " + getAvailableNormalChargingCount());
        saveToggleStates();
    }
    
    private void toggleSwitch8ActionPerformed(java.awt.event.ActionEvent evt) {
        if (toggleSwitch8.isSelected()) {
            normalChargingAvailable[4] = true;
        } else {
            normalChargingAvailable[4] = false;
        }
        updateBayLabel(bay8, normalChargingAvailable[4]);
        System.out.println("Normal Charging Bays Available: " + getAvailableNormalChargingCount());
        saveToggleStates();
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
    private javax.swing.JButton quebutton;
    private javax.swing.JButton staffbutton;
    private javax.swing.JToggleButton toggleSwitch1;
    private javax.swing.JToggleButton toggleSwitch2;
    private javax.swing.JToggleButton toggleSwitch3;
    private javax.swing.JToggleButton toggleSwitch4;
    private javax.swing.JToggleButton toggleSwitch5;
    private javax.swing.JToggleButton toggleSwitch6;
    private javax.swing.JToggleButton toggleSwitch7;
    private javax.swing.JToggleButton toggleSwitch8;
    // End of variables declaration//GEN-END:variables
}
