package cephra.Admin;

import javax.swing.*;
import java.util.List;

public class Business_Overview extends javax.swing.JPanel {

    private java.awt.Image dashboardImage;
    
    // Variables to store spinner values
    private int Min = 50; // Default minimum fee
    private int RPH = 15; // Default rate per hour
    private double fastMultiplier = 1.25; // Fast charging gets 25% premium

    public Business_Overview() {
        initComponents();
        setPreferredSize(new java.awt.Dimension(1000, 750));
        setSize(1000, 750);
        loadDashboardImage();
        updateLabelIcon();
        setupDateTimeTimer();
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                updateLabelIcon();
            }
        });
        
        // Load saved settings from database
        loadSettingsFromDatabase();
        
        // Load dashboard statistics
        loadDashboardStats();
        
        // Configure spinners with SpinnerNumberModel to prevent negative values
        MinfeeSpinner.setModel(new javax.swing.SpinnerNumberModel(Min, 0, Integer.MAX_VALUE, 1));
        RPHSpinner.setModel(new javax.swing.SpinnerNumberModel(RPH, 0, Integer.MAX_VALUE, 1));
        
        // Add change listeners to automatically save spinner values
        MinfeeSpinner.addChangeListener(_ -> {
            Min = (Integer) MinfeeSpinner.getValue();
        });
        
        RPHSpinner.addChangeListener(_ -> {
            RPH = (Integer) RPHSpinner.getValue();
        });
        
        // Add Enter key listeners to save values when Enter is pressed
        MinfeeSpinner.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                    SaveminfeeActionPerformed(null);
                }
            }
        });
        
        RPHSpinner.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                    SaverateperhourActionPerformed(null);
                }
            }
        });


        
        // Add hover effects to navigation buttons
        ButtonHoverEffect.addHoverEffect(quebutton);
        ButtonHoverEffect.addHoverEffect(Baybutton);
        ButtonHoverEffect.addHoverEffect(historybutton);
        ButtonHoverEffect.addHoverEffect(staffbutton);
        

        //Spinner setHorizontalAlignment
        JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) MinfeeSpinner.getEditor();
        javax.swing.JFormattedTextField textfield = editor.getTextField();
        textfield.setHorizontalAlignment(JTextField.CENTER);

        JSpinner.DefaultEditor editor1 = (JSpinner.DefaultEditor) RPHSpinner.getEditor();
        javax.swing.JFormattedTextField textfield1 = editor1.getTextField();
        textfield1.setHorizontalAlignment(JTextField.CENTER);
        

        

    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        earnings = new javax.swing.JLabel();
        totalcharge = new javax.swing.JLabel();
        MinfeeSpinner = new javax.swing.JSpinner();
        RPHSpinner = new javax.swing.JSpinner();
        staffbutton = new javax.swing.JButton();
        historybutton = new javax.swing.JButton();
        Baybutton = new javax.swing.JButton();
        quebutton = new javax.swing.JButton();
        exitlogin = new javax.swing.JButton();
        datetimeStaff = new javax.swing.JLabel();
        Saveminfee = new javax.swing.JButton();
        Saverateperhour = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        labelStaff = new javax.swing.JLabel();
        BUSINESStEXT = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(1000, 750));
        setMinimumSize(new java.awt.Dimension(1000, 750));
        setPreferredSize(new java.awt.Dimension(1000, 750));
        setLayout(null);

        earnings.setFont(new java.awt.Font("Segoe UI", 1, 60)); // NOI18N
        earnings.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        earnings.setText("₱6,789");
        add(earnings);
        earnings.setBounds(600, 220, 310, 120);

        totalcharge.setFont(new java.awt.Font("Segoe UI", 1, 60)); // NOI18N
        totalcharge.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        totalcharge.setText("31");
        add(totalcharge);
        totalcharge.setBounds(150, 220, 250, 120);

        MinfeeSpinner.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        MinfeeSpinner.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.lightGray, java.awt.Color.lightGray, java.awt.Color.lightGray, java.awt.Color.lightGray));
        MinfeeSpinner.setValue(50);
        add(MinfeeSpinner);
        MinfeeSpinner.setBounds(120, 510, 300, 40);

        RPHSpinner.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        RPHSpinner.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.lightGray, java.awt.Color.lightGray, java.awt.Color.lightGray, java.awt.Color.lightGray));
        RPHSpinner.setValue(15);
        add(RPHSpinner);
        RPHSpinner.setBounds(600, 508, 300, 40);

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

        datetimeStaff.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        datetimeStaff.setForeground(new java.awt.Color(255, 255, 255));
        datetimeStaff.setText("10:44 AM 17 August, Sunday");
        add(datetimeStaff);
        datetimeStaff.setBounds(820, 40, 180, 20);

        Saveminfee.setBorderPainted(false);
        Saveminfee.setContentAreaFilled(false);
        Saveminfee.setFocusPainted(false);
        Saveminfee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SaveminfeeActionPerformed(evt);
            }
        });
        add(Saveminfee);
        Saveminfee.setBounds(120, 550, 290, 50);

        Saverateperhour.setBorderPainted(false);
        Saverateperhour.setContentAreaFilled(false);
        Saverateperhour.setFocusPainted(false);
        Saverateperhour.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SaverateperhourActionPerformed(evt);
            }
        });
        add(Saverateperhour);
        Saverateperhour.setBounds(610, 560, 290, 40);

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

        BUSINESStEXT.setForeground(new java.awt.Color(4, 167, 182));
        BUSINESStEXT.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        BUSINESStEXT.setText("BUSINESS OVERVIEW");
        add(BUSINESStEXT);
        BUSINESStEXT.setBounds(617, 26, 136, 15);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/Business.png"))); // NOI18N
        add(jLabel1);
        jLabel1.setBounds(0, 0, 1240, 750);
    }// </editor-fold>//GEN-END:initComponents

    private void quebuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quebuttonActionPerformed
        java.awt.Window w = javax.swing.SwingUtilities.getWindowAncestor(Business_Overview.this);
        if (w instanceof cephra.Frame.Admin) {
            ((cephra.Frame.Admin) w).switchPanel(new cephra.Admin.Queue());
        }
    }//GEN-LAST:event_quebuttonActionPerformed

    private void BaybuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BaybuttonActionPerformed
      java.awt.Window w = javax.swing.SwingUtilities.getWindowAncestor(Business_Overview.this);
        if (w instanceof cephra.Frame.Admin) {
            ((cephra.Frame.Admin) w).switchPanel(new cephra.Admin.BayManagement());
        }
    }//GEN-LAST:event_BaybuttonActionPerformed

    private void historybuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_historybuttonActionPerformed
       java.awt.Window w = javax.swing.SwingUtilities.getWindowAncestor(Business_Overview.this);
        if (w instanceof cephra.Frame.Admin) {
            ((cephra.Frame.Admin) w).switchPanel(new cephra.Admin.History());
        }
    }//GEN-LAST:event_historybuttonActionPerformed

    private void staffbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_staffbuttonActionPerformed
        java.awt.Window w = javax.swing.SwingUtilities.getWindowAncestor(Business_Overview.this);
        if (w instanceof cephra.Frame.Admin) {
            ((cephra.Frame.Admin) w).switchPanel(new cephra.Admin.StaffRecord());
        }
    }//GEN-LAST:event_staffbuttonActionPerformed
    
   
    
   

    private void exitloginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitloginActionPerformed
        java.awt.Window w = javax.swing.SwingUtilities.getWindowAncestor(Business_Overview.this);
        if (w instanceof cephra.Frame.Admin) {
            ((cephra.Frame.Admin) w).switchPanel(new cephra.Admin.Login());
        }
    }//GEN-LAST:event_exitloginActionPerformed

    private void SaveminfeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaveminfeeActionPerformed
        Min = (Integer) MinfeeSpinner.getValue();
        
        // Save to database
        boolean saved = cephra.db.CephraDB.updateSystemSetting("minimum_fee", String.valueOf(Min));
        if (saved) {
            // Update QueueBridge with new minimum fee
            cephra.Admin.QueueBridge.setMinimumFee(Min);
            javax.swing.JOptionPane.showMessageDialog(this, "Minimum fee saved: ₱" + Min);
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "Error saving minimum fee!", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_SaveminfeeActionPerformed

    private void SaverateperhourActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaverateperhourActionPerformed
        RPH = (Integer) RPHSpinner.getValue();
        
        // Save to database
        boolean saved = cephra.db.CephraDB.updateSystemSetting("rate_per_kwh", String.valueOf(RPH));
        if (saved) {
            // Update QueueBridge with new rate
            cephra.Admin.QueueBridge.setRatePerKWh(RPH);
            javax.swing.JOptionPane.showMessageDialog(this, "Rate per kWh saved: ₱" + RPH);
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "Error saving rate per kWh!", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_SaverateperhourActionPerformed





    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel BUSINESStEXT;
    private javax.swing.JButton Baybutton;
    private javax.swing.JSpinner MinfeeSpinner;
    private javax.swing.JSpinner RPHSpinner;
    private javax.swing.JButton Saveminfee;
    private javax.swing.JButton Saverateperhour;
    private javax.swing.JLabel datetimeStaff;
    private javax.swing.JLabel earnings;
    private javax.swing.JButton exitlogin;
    private javax.swing.JButton historybutton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel labelStaff;
    private javax.swing.JButton quebutton;
    private javax.swing.JButton staffbutton;
    private javax.swing.JLabel totalcharge;
    // End of variables declaration//GEN-END:variables

   

    private void loadDashboardImage() {
        try {
            java.net.URL url = getClass().getResource("/cephra/Cephra Images/Business.png");
            if (url == null) url = getClass().getResource("/cephra/Cephra Images/res.png");
            if (url == null) url = getClass().getResource("/cephra/Cephra Images/emp.png");
            if (url == null) url = getClass().getResource("/cephra/Cephra Images/bigs.png");
            if (url == null) url = getClass().getResource("/cephra/Cephra Images/logi.png");
            if (url == null) url = getClass().getResource("/cephra/Cephra Images/pho.png");
            if (url != null) {
                dashboardImage = new javax.swing.ImageIcon(url).getImage();
            }
        } catch (Exception ignored) {}
    }

    private void updateLabelIcon() {
        if (dashboardImage == null) {
            jLabel1.setIcon(null);
            return;
        }
        int w = Math.max(1, getWidth());
        int h = Math.max(1, getHeight());
        java.awt.Image scaled = dashboardImage.getScaledInstance(w, h, java.awt.Image.SCALE_SMOOTH);
        jLabel1.setIcon(new javax.swing.ImageIcon(scaled));
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

        datetimeStaff.setText(time + " " + date);
    }
    
    private void loadSettingsFromDatabase() {
        try {
            // Load minimum fee from database
            String minFeeStr = cephra.db.CephraDB.getSystemSetting("minimum_fee");
            if (minFeeStr != null && !minFeeStr.trim().isEmpty()) {
                Min = Integer.parseInt(minFeeStr);
                System.out.println("Dashboard: Loaded minimum fee from database: ₱" + Min);
            } else {
                // Set default if not found in database
                cephra.db.CephraDB.updateSystemSetting("minimum_fee", String.valueOf(Min));
                System.out.println("Dashboard: Set default minimum fee: ₱" + Min);
            }
            
            // Load rate per kWh from database
            String rateStr = cephra.db.CephraDB.getSystemSetting("rate_per_kwh");
            if (rateStr != null && !rateStr.trim().isEmpty()) {
                RPH = Integer.parseInt(rateStr);
                System.out.println("Dashboard: Loaded rate per kWh from database: ₱" + RPH);
            } else {
                // Set default if not found in database
                cephra.db.CephraDB.updateSystemSetting("rate_per_kwh", String.valueOf(RPH));
                System.out.println("Dashboard: Set default rate per kWh: ₱" + RPH);
            }
            
            // Load fast multiplier from database
            String multiplierStr = cephra.db.CephraDB.getSystemSetting("fast_multiplier");
            if (multiplierStr != null && !multiplierStr.trim().isEmpty()) {
                fastMultiplier = Double.parseDouble(multiplierStr);
                System.out.println("Dashboard: Loaded fast multiplier from database: " + String.format("%.0f%%", (fastMultiplier - 1) * 100));
            } else {
                // Set default if not found in database
                cephra.db.CephraDB.updateSystemSetting("fast_multiplier", String.valueOf(fastMultiplier));
                System.out.println("Dashboard: Set default fast multiplier: " + String.format("%.0f%%", (fastMultiplier - 1) * 100));
            }
            
            // Update spinners with loaded values
            MinfeeSpinner.setValue(Min);
            RPHSpinner.setValue(RPH);
            
            // Update QueueBridge with loaded values
            cephra.Admin.QueueBridge.setMinimumFee(Min);
            cephra.Admin.QueueBridge.setRatePerKWh(RPH);
            cephra.Admin.QueueBridge.setFastMultiplier(fastMultiplier);
            
        } catch (Exception e) {
            System.err.println("Dashboard: Error loading settings from database: " + e.getMessage());
            // Keep default values if there's an error
        }
    }
    
    private void loadDashboardStats() {
        try {
            // Get all charging history records
            List<Object[]> historyRecords = cephra.db.CephraDB.getAllChargingHistory();
            
            // Calculate total charge sessions count
            int totalSessions = historyRecords.size();
            totalcharge.setText(String.valueOf(totalSessions));
            System.out.println("Dashboard: Loaded total charge sessions: " + totalSessions);
            
            // Calculate total earnings from all records
            double totalEarnings = 0.0;
            for (Object[] record : historyRecords) {
                // Total amount is at index 5 in the history record
                if (record[5] != null) {
                    try {
                        totalEarnings += Double.parseDouble(record[5].toString());
                    } catch (NumberFormatException e) {
                        System.err.println("Error parsing amount: " + record[5]);
                    }
                }
            }
            
            earnings.setText("₱" + String.format("%,.0f", totalEarnings));
            System.out.println("Dashboard: Loaded total earnings: ₱" + totalEarnings);
            
        } catch (Exception e) {
            System.err.println("Dashboard: Error loading dashboard stats: " + e.getMessage());
            // Set default values if there's an error
            totalcharge.setText("0");
            earnings.setText("₱0");
        }
    }
    
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

}
