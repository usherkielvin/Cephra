package cephra.Admin;

public class Dashboard extends javax.swing.JPanel {

    private java.awt.Image dashboardImage;
   
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
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        totalsessions1 = new javax.swing.JLabel();
        totalsessions = new javax.swing.JLabel();
        rate1 = new javax.swing.JLabel();
        rate = new javax.swing.JLabel();
        staffbutton = new javax.swing.JButton();
        historybutton = new javax.swing.JButton();
        Baybutton = new javax.swing.JButton();
        datetime1 = new javax.swing.JLabel();
        quebutton = new javax.swing.JButton();
        exitlogin = new javax.swing.JButton();
        toggleSwitch = new cephra.Admin.ToggleSwitch();
        datetime = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(1000, 750));
        setMinimumSize(new java.awt.Dimension(1000, 750));
        setPreferredSize(new java.awt.Dimension(1000, 750));
        setLayout(null);

        totalsessions1.setFont(new java.awt.Font("Segoe UI", 1, 60)); // NOI18N
        totalsessions1.setForeground(new java.awt.Color(0, 0, 0));
        totalsessions1.setText("₱6,789");
        add(totalsessions1);
        totalsessions1.setBounds(70, 490, 310, 120);

        totalsessions.setFont(new java.awt.Font("Segoe UI", 1, 60)); // NOI18N
        totalsessions.setForeground(new java.awt.Color(0, 0, 0));
        totalsessions.setText("31");
        add(totalsessions);
        totalsessions.setBounds(130, 250, 70, 120);

        rate1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        rate1.setForeground(new java.awt.Color(0, 0, 0));
        rate1.setText("50");
        add(rate1);
        rate1.setBounds(580, 470, 28, 60);

        rate.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        rate.setForeground(new java.awt.Color(0, 0, 0));
        rate.setText("20");
        add(rate);
        rate.setBounds(580, 350, 28, 50);

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

        datetime1.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        datetime1.setForeground(new java.awt.Color(153, 153, 153));
        datetime1.setText("As of 8:00 AM of Aug 20, 2025");
        add(datetime1);
        datetime1.setBounds(250, 180, 230, 20);

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
        add(toggleSwitch);
        toggleSwitch.setBounds(580, 600, 60, 30);
        toggleSwitch.addPropertyChangeListener("selected", new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                dashboardTogglePropertyChange(evt);
            }
        });

        datetime.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        datetime.setForeground(new java.awt.Color(255, 255, 255));
        updateDateTime();
        add(datetime);
        datetime.setBounds(820, 40, 170, 20);

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

    private void dashboardTogglePropertyChange(java.beans.PropertyChangeEvent evt) {
        // You can add functionality here when the dashboard toggle is switched
        // For example, enable/disable certain features, change status, etc.
        if (toggleSwitch.isSelected()) {
            // Toggle is ON - add your functionality here
            System.out.println("Dashboard toggle is ON");
        } else {
            // Toggle is OFF - add your functionality here
            System.out.println("Dashboard toggle is OFF");
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Baybutton;
    private javax.swing.JLabel datetime;
    private javax.swing.JLabel datetime1;
    private javax.swing.JButton exitlogin;
    private javax.swing.JButton historybutton;
    private javax.swing.JLabel jLabel1;
    private cephra.Admin.ToggleSwitch toggleSwitch;
    private javax.swing.JButton quebutton;
    private javax.swing.JLabel rate;
    private javax.swing.JLabel rate1;
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
        updateDateTime1();
        javax.swing.Timer timer = new javax.swing.Timer(1000, new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                updateDateTime();
                updateDateTime1();
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
    
    private void updateDateTime1() {
        java.text.SimpleDateFormat timeFormat = new java.text.SimpleDateFormat("hh:mm a");
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("MMM dd, yyyy");
        
        java.util.Date now = new java.util.Date();
        String time = timeFormat.format(now);
        String date = dateFormat.format(now);
        
        datetime1.setText("As of " + time + " of " + date);
    }
}
