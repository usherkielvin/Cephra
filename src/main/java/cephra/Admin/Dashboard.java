package cephra.Admin;

import javax.swing.*;

public class Dashboard extends javax.swing.JPanel {

    private java.awt.Image dashboardImage;
    
    // Variables to store spinner values
    private int Min = 50; // Default minimum fee
    private int RPH = 15; // Default rate per hour
    private double fastMultiplier = 1.25; // Fast charging gets 25% premium

    public Dashboard() {
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
        
        // Configure spinners with SpinnerNumberModel to prevent negative values
        MinfeeSpinner.setModel(new javax.swing.SpinnerNumberModel(Min, 0, Integer.MAX_VALUE, 1));
        RPHSpinner.setModel(new javax.swing.SpinnerNumberModel(RPH, 0, Integer.MAX_VALUE, 1));
        
        // Add change listeners to automatically save spinner values
        MinfeeSpinner.addChangeListener(e -> {
            Min = (Integer) MinfeeSpinner.getValue();
        });
        
        RPHSpinner.addChangeListener(e -> {
            RPH = (Integer) RPHSpinner.getValue();
        });


        
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

        totalsessions1 = new javax.swing.JLabel();
        totalsessions = new javax.swing.JLabel();
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
        labelStaff = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(1000, 750));
        setMinimumSize(new java.awt.Dimension(1000, 750));
        setPreferredSize(new java.awt.Dimension(1000, 750));
        setLayout(null);

        totalsessions1.setFont(new java.awt.Font("Segoe UI", 1, 60)); // NOI18N
        totalsessions1.setText("₱6,789");
        add(totalsessions1);
        totalsessions1.setBounds(650, 220, 310, 120);

        totalsessions.setFont(new java.awt.Font("Segoe UI", 1, 60)); // NOI18N
        totalsessions.setText("31");
        add(totalsessions);
        totalsessions.setBounds(240, 220, 160, 120);

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

        staffbutton.setBorder(null);
        staffbutton.setBorderPainted(false);
        staffbutton.setContentAreaFilled(false);
        staffbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                staffbuttonActionPerformed(evt);
            }
        });
        add(staffbutton);
        staffbutton.setBounds(500, 10, 110, 40);

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

        Baybutton.setBorder(null);
        Baybutton.setBorderPainted(false);
        Baybutton.setContentAreaFilled(false);
        Baybutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BaybuttonActionPerformed(evt);
            }
        });
        add(Baybutton);
        Baybutton.setBounds(380, 10, 40, 40);

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

        datetimeStaff.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        datetimeStaff.setForeground(new java.awt.Color(255, 255, 255));
        datetimeStaff.setText("10:44 AM 17 August, Sunday");
        add(datetimeStaff);
        datetimeStaff.setBounds(820, 40, 170, 20);

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

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Photos/Business.png"))); // NOI18N
        add(jLabel1);
        jLabel1.setBounds(0, 0, 1240, 750);
    }// </editor-fold>//GEN-END:initComponents

    private void quebuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quebuttonActionPerformed
        java.awt.Window w = javax.swing.SwingUtilities.getWindowAncestor(Dashboard.this);
        if (w instanceof cephra.Frame.Admin) {
            ((cephra.Frame.Admin) w).switchPanel(new cephra.Admin.Queue());
        }
    }//GEN-LAST:event_quebuttonActionPerformed

    private void BaybuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BaybuttonActionPerformed
      java.awt.Window w = javax.swing.SwingUtilities.getWindowAncestor(Dashboard.this);
        if (w instanceof cephra.Frame.Admin) {
            ((cephra.Frame.Admin) w).switchPanel(new cephra.Admin.Bay());
        }
    }//GEN-LAST:event_BaybuttonActionPerformed

    private void historybuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_historybuttonActionPerformed
       java.awt.Window w = javax.swing.SwingUtilities.getWindowAncestor(Dashboard.this);
        if (w instanceof cephra.Frame.Admin) {
            ((cephra.Frame.Admin) w).switchPanel(new cephra.Admin.History());
        }
    }//GEN-LAST:event_historybuttonActionPerformed

    private void staffbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_staffbuttonActionPerformed
        java.awt.Window w = javax.swing.SwingUtilities.getWindowAncestor(Dashboard.this);
        if (w instanceof cephra.Frame.Admin) {
            ((cephra.Frame.Admin) w).switchPanel(new cephra.Admin.StaffRecord());
        }
    }//GEN-LAST:event_staffbuttonActionPerformed
    
   
    
   

    private void exitloginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitloginActionPerformed
        java.awt.Window w = javax.swing.SwingUtilities.getWindowAncestor(Dashboard.this);
        if (w instanceof cephra.Frame.Admin) {
            ((cephra.Frame.Admin) w).switchPanel(new cephra.Admin.Login());
        }
    }//GEN-LAST:event_exitloginActionPerformed

    private void SaveminfeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaveminfeeActionPerformed
        Min = (Integer) MinfeeSpinner.getValue();
        
        // Save to database
        boolean saved = cephra.CephraDB.updateSystemSetting("minimum_fee", String.valueOf(Min));
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
        boolean saved = cephra.CephraDB.updateSystemSetting("rate_per_kwh", String.valueOf(RPH));
        if (saved) {
            // Update QueueBridge with new rate
            cephra.Admin.QueueBridge.setRatePerKWh(RPH);
            javax.swing.JOptionPane.showMessageDialog(this, "Rate per kWh saved: ₱" + RPH);
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "Error saving rate per kWh!", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_SaverateperhourActionPerformed

    private void SaveFastMultiplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaveFastMultiplierActionPerformed
        fastMultiplier = (Double) FastMultiplierSpinner.getValue();
        
        // Save to database
        boolean saved = cephra.CephraDB.updateSystemSetting("fast_multiplier", String.valueOf(fastMultiplier));
        if (saved) {
            // Update QueueBridge with new fast multiplier
            cephra.Admin.QueueBridge.setFastMultiplier(fastMultiplier);
            javax.swing.JOptionPane.showMessageDialog(this, "Fast charging multiplier saved: " + String.format("%.0f%%", (fastMultiplier - 1) * 100));
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "Error saving fast multiplier!", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_SaveFastMultiplierActionPerformed



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Baybutton;
    private javax.swing.JSpinner MinfeeSpinner;
    private javax.swing.JSpinner RPHSpinner;
    private javax.swing.JButton Saveminfee;
    private javax.swing.JButton Saverateperhour;
    private javax.swing.JLabel datetimeStaff;
    private javax.swing.JButton exitlogin;
    private javax.swing.JButton historybutton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel labelStaff;
    private javax.swing.JButton quebutton;
    private javax.swing.JButton staffbutton;
    private javax.swing.JLabel totalsessions;
    private javax.swing.JLabel totalsessions1;
    // End of variables declaration//GEN-END:variables

   

    private void loadDashboardImage() {
        try {
            java.net.URL url = getClass().getResource("/cephra/Photos/Business.png");
            if (url == null) url = getClass().getResource("/cephra/Photos/res.png");
            if (url == null) url = getClass().getResource("/cephra/Photos/emp.png");
            if (url == null) url = getClass().getResource("/cephra/Photos/bigs.png");
            if (url == null) url = getClass().getResource("/cephra/Photos/logi.png");
            if (url == null) url = getClass().getResource("/cephra/Photos/pho.png");
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

        datetime.setText(time + " " + date);
    }
    
    private void loadSettingsFromDatabase() {
        try {
            // Load minimum fee from database
            String minFeeStr = cephra.CephraDB.getSystemSetting("minimum_fee");
            if (minFeeStr != null && !minFeeStr.trim().isEmpty()) {
                Min = Integer.parseInt(minFeeStr);
                System.out.println("Dashboard: Loaded minimum fee from database: ₱" + Min);
            } else {
                // Set default if not found in database
                cephra.CephraDB.updateSystemSetting("minimum_fee", String.valueOf(Min));
                System.out.println("Dashboard: Set default minimum fee: ₱" + Min);
            }
            
            // Load rate per kWh from database
            String rateStr = cephra.CephraDB.getSystemSetting("rate_per_kwh");
            if (rateStr != null && !rateStr.trim().isEmpty()) {
                RPH = Integer.parseInt(rateStr);
                System.out.println("Dashboard: Loaded rate per kWh from database: ₱" + RPH);
            } else {
                // Set default if not found in database
                cephra.CephraDB.updateSystemSetting("rate_per_kwh", String.valueOf(RPH));
                System.out.println("Dashboard: Set default rate per kWh: ₱" + RPH);
            }
            
            // Load fast multiplier from database
            String multiplierStr = cephra.CephraDB.getSystemSetting("fast_multiplier");
            if (multiplierStr != null && !multiplierStr.trim().isEmpty()) {
                fastMultiplier = Double.parseDouble(multiplierStr);
                System.out.println("Dashboard: Loaded fast multiplier from database: " + String.format("%.0f%%", (fastMultiplier - 1) * 100));
            } else {
                // Set default if not found in database
                cephra.CephraDB.updateSystemSetting("fast_multiplier", String.valueOf(fastMultiplier));
                System.out.println("Dashboard: Set default fast multiplier: " + String.format("%.0f%%", (fastMultiplier - 1) * 100));
            }
            
            // Update QueueBridge with loaded values
            cephra.Admin.QueueBridge.setMinimumFee(Min);
            cephra.Admin.QueueBridge.setRatePerKWh(RPH);
            cephra.Admin.QueueBridge.setFastMultiplier(fastMultiplier);
            
        } catch (Exception e) {
            System.err.println("Dashboard: Error loading settings from database: " + e.getMessage());
            // Keep default values if there's an error
        }
    }
    

}
