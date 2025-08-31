package cephra.Admin;

import java.awt.Window;
import javax.swing.SwingUtilities;


public class Bay extends javax.swing.JPanel {

  
    public Bay() {
        initComponents();
        setPreferredSize(new java.awt.Dimension(1000, 750));
        setSize(1000, 750);
        setupDateTimeTimer();
        
        // Set default state to Available for all bays
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
    private javax.swing.JButton quebutton;
    private javax.swing.JButton staffbutton;
    // End of variables declaration//GEN-END:variables
}
