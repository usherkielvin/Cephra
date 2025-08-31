package cephra.Admin;

import java.awt.Color;
import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.JTextField;

public class Dashboard extends javax.swing.JPanel {

    private java.awt.Image dashboardImage;
    private javax.swing.JToggleButton toggleSwitch; // Add this line
    
    // Variables to store spinner values
    private int Min = 50; // Default minimum fee
    private int RPH = 15; // Default rate per hour

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

        // --- TOGGLE SWITCH SETUP ---
        toggleSwitch = new javax.swing.JToggleButton("Toggle");
        toggleSwitch.setBounds(700, 10, 100, 40); // Adjust position and size as needed
        add(toggleSwitch);
        toggleSwitch.addPropertyChangeListener(evt -> {
            if ("selected".equals(evt.getPropertyName())) {
                dashboardTogglePropertyChange(evt);
            }
        });
        
        //Spinner setHorizontalAlignment
        JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) MinfeeSpinner.getEditor();
        JFormattedTextField textfield = editor.getTextField();
        textfield.setHorizontalAlignment(JTextField.CENTER);
        
        JSpinner.DefaultEditor editor1 = (JSpinner.DefaultEditor) RPHSpinner.getEditor();
        JFormattedTextField textfield1 = editor1.getTextField();
        textfield1.setHorizontalAlignment(JTextField.CENTER);
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        totalsessions1 = new javax.swing.JLabel();
        totalsessions = new javax.swing.JLabel();
        staffbutton = new javax.swing.JButton();
        historybutton = new javax.swing.JButton();
        Baybutton = new javax.swing.JButton();
        quebutton = new javax.swing.JButton();
        exitlogin = new javax.swing.JButton();
        datetime = new javax.swing.JLabel();
        MinfeeSpinner = new javax.swing.JSpinner();
        RPHSpinner = new javax.swing.JSpinner();
        Saveminfee = new javax.swing.JButton();
        Saverateperhour = new javax.swing.JButton();
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
        totalsessions.setBounds(240, 220, 70, 120);

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

        datetime.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        datetime.setForeground(new java.awt.Color(255, 255, 255));
        datetime.setText("10:44 AM 17 August, Sunday");
        add(datetime);
        datetime.setBounds(820, 40, 170, 20);

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
        javax.swing.JOptionPane.showMessageDialog(this, "Minimum fee saved: " + Min);
        
        
    }//GEN-LAST:event_SaveminfeeActionPerformed

    private void SaverateperhourActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaverateperhourActionPerformed
        // TODO add your handling code here:
         RPH = (Integer) RPHSpinner.getValue();
        javax.swing.JOptionPane.showMessageDialog(this, "Rate per hour saved: " + RPH);
    }//GEN-LAST:event_SaverateperhourActionPerformed

    private void dashboardTogglePropertyChange(java.beans.PropertyChangeEvent evt) {
        // Functionality when the dashboard toggle is switched
        if (toggleSwitch.isSelected()) {
            // Toggle is ON
            System.out.println("Dashboard toggle is ON");
        } else {
            // Toggle is OFF
            System.out.println("Dashboard toggle is OFF");
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Baybutton;
    private javax.swing.JSpinner MinfeeSpinner;
    private javax.swing.JSpinner RPHSpinner;
    private javax.swing.JButton Saveminfee;
    private javax.swing.JButton Saverateperhour;
    private javax.swing.JLabel datetime;
    private javax.swing.JButton exitlogin;
    private javax.swing.JButton historybutton;
    private javax.swing.JLabel jLabel1;
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
    

}
