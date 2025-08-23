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
        toggleSwitch1.setSelected(true);
        toggleSwitch2.setSelected(true);
        toggleSwitch3.setSelected(true);
        toggleSwitch4.setSelected(true);
        toggleSwitch5.setSelected(true);
        toggleSwitch6.setSelected(true);
        toggleSwitch7.setSelected(true);
        toggleSwitch8.setSelected(true);
        
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
        toggleSwitch1 = new cephra.Admin.ToggleSwitch();
        toggleSwitch2 = new cephra.Admin.ToggleSwitch();
        toggleSwitch3 = new cephra.Admin.ToggleSwitch();
        toggleSwitch4 = new cephra.Admin.ToggleSwitch();
        toggleSwitch5 = new cephra.Admin.ToggleSwitch();
        toggleSwitch6 = new cephra.Admin.ToggleSwitch();
        toggleSwitch7 = new cephra.Admin.ToggleSwitch();
        toggleSwitch8 = new cephra.Admin.ToggleSwitch();
        quebutton = new javax.swing.JButton();
        staffbutton = new javax.swing.JButton();
        businessbutton = new javax.swing.JButton();
        exitlogin = new javax.swing.JButton();
        historybutton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setLayout(null);

        bay8.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        bay8.setForeground(new java.awt.Color(0, 0, 0));
        bay8.setText("Available");
        add(bay8);
        bay8.setBounds(400, 680, 150, 32);

        bay7.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        bay7.setForeground(new java.awt.Color(0, 0, 0));
        bay7.setText("Available");
        add(bay7);
        bay7.setBounds(100, 680, 150, 32);

        bay6.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        bay6.setForeground(new java.awt.Color(0, 0, 0));
        bay6.setText("Available");
        add(bay6);
        bay6.setBounds(720, 480, 150, 32);

        bay5.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        bay5.setForeground(new java.awt.Color(0, 0, 0));
        bay5.setText("Available");
        add(bay5);
        bay5.setBounds(400, 480, 150, 32);

        bay4.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        bay4.setForeground(new java.awt.Color(0, 0, 0));
        bay4.setText("Available");
        add(bay4);
        bay4.setBounds(100, 480, 150, 32);

        bay3.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        bay3.setForeground(new java.awt.Color(0, 0, 0));
        bay3.setText("Available");
        add(bay3);
        bay3.setBounds(730, 270, 150, 32);

        bay2.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        bay2.setForeground(new java.awt.Color(0, 0, 0));
        bay2.setText("Available");
        add(bay2);
        bay2.setBounds(410, 270, 150, 32);

        bay1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        bay1.setForeground(new java.awt.Color(0, 0, 0));
        bay1.setText("Available");
        add(bay1);
        bay1.setBounds(100, 270, 150, 32);

        datetime.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        datetime.setForeground(new java.awt.Color(255, 255, 255));
        datetime.setText("10:44 AM 17 August, Sunday");
        add(datetime);
        datetime.setBounds(820, 40, 170, 20);
        
        // Add toggle switches to the right of each bay label
        add(toggleSwitch1);
        toggleSwitch1.setBounds(260, 270, 60, 30);
        toggleSwitch1.addPropertyChangeListener("selected", new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                toggleSwitch1PropertyChange(evt);
            }
        });
        
        add(toggleSwitch2);
        toggleSwitch2.setBounds(570, 270, 60, 30);
        toggleSwitch2.addPropertyChangeListener("selected", new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                toggleSwitch2PropertyChange(evt);
            }
        });
        
        add(toggleSwitch3);
        toggleSwitch3.setBounds(890, 270, 60, 30);
        toggleSwitch3.addPropertyChangeListener("selected", new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                toggleSwitch3PropertyChange(evt);
            }
        });
        
        add(toggleSwitch4);
        toggleSwitch4.setBounds(260, 480, 60, 30);
        toggleSwitch4.addPropertyChangeListener("selected", new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                toggleSwitch4PropertyChange(evt);
            }
        });
        
        add(toggleSwitch5);
        toggleSwitch5.setBounds(570, 480, 60, 30);
        toggleSwitch5.addPropertyChangeListener("selected", new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                toggleSwitch5PropertyChange(evt);
            }
        });
        
        add(toggleSwitch6);
        toggleSwitch6.setBounds(890, 480, 60, 30);
        toggleSwitch6.addPropertyChangeListener("selected", new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                toggleSwitch6PropertyChange(evt);
            }
        });
        
        add(toggleSwitch7);
        toggleSwitch7.setBounds(260, 680, 60, 30);
        toggleSwitch7.addPropertyChangeListener("selected", new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                toggleSwitch7PropertyChange(evt);
            }
        });
        
        add(toggleSwitch8);
        toggleSwitch8.setBounds(570, 680, 60, 30);
        toggleSwitch8.addPropertyChangeListener("selected", new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                toggleSwitch8PropertyChange(evt);
            }
        });

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

    private void toggleSwitch1PropertyChange(java.beans.PropertyChangeEvent evt) {
        if (toggleSwitch1.isSelected()) {
            bay1.setText("Available");
            bay1.setForeground(new java.awt.Color(0, 128, 0)); // Green color for available
        } else {
            bay1.setText("Unavailable");
            bay1.setForeground(new java.awt.Color(255, 0, 0)); // Red color for unavailable
        }
    }
    
    private void toggleSwitch2PropertyChange(java.beans.PropertyChangeEvent evt) {
        if (toggleSwitch2.isSelected()) {
            bay2.setText("Available");
            bay2.setForeground(new java.awt.Color(0, 128, 0)); // Green color for available
        } else {
            bay2.setText("Unavailable");
            bay2.setForeground(new java.awt.Color(255, 0, 0)); // Red color for unavailable
        }
    }
    
    private void toggleSwitch3PropertyChange(java.beans.PropertyChangeEvent evt) {
        if (toggleSwitch3.isSelected()) {
            bay3.setText("Available");
            bay3.setForeground(new java.awt.Color(0, 128, 0)); // Green color for available
        } else {
            bay3.setText("Unavailable");
            bay3.setForeground(new java.awt.Color(255, 0, 0)); // Red color for unavailable
        }
    }
    
    private void toggleSwitch4PropertyChange(java.beans.PropertyChangeEvent evt) {
        if (toggleSwitch4.isSelected()) {
            bay4.setText("Available");
            bay4.setForeground(new java.awt.Color(0, 128, 0)); // Green color for available
        } else {
            bay4.setText("Unavailable");
            bay4.setForeground(new java.awt.Color(255, 0, 0)); // Red color for unavailable
        }
    }
    
    private void toggleSwitch5PropertyChange(java.beans.PropertyChangeEvent evt) {
        if (toggleSwitch5.isSelected()) {
            bay5.setText("Available");
            bay5.setForeground(new java.awt.Color(0, 128, 0)); // Green color for available
        } else {
            bay5.setText("Unavailable");
            bay5.setForeground(new java.awt.Color(255, 0, 0)); // Red color for unavailable
        }
    }
    
    private void toggleSwitch6PropertyChange(java.beans.PropertyChangeEvent evt) {
        if (toggleSwitch6.isSelected()) {
            bay6.setText("Available");
            bay6.setForeground(new java.awt.Color(0, 128, 0)); // Green color for available
        } else {
            bay6.setText("Unavailable");
            bay6.setForeground(new java.awt.Color(255, 0, 0)); // Red color for unavailable
        }
    }
    
    private void toggleSwitch7PropertyChange(java.beans.PropertyChangeEvent evt) {
        if (toggleSwitch7.isSelected()) {
            bay7.setText("Available");
            bay7.setForeground(new java.awt.Color(0, 128, 0)); // Green color for available
        } else {
            bay7.setText("Unavailable");
            bay7.setForeground(new java.awt.Color(255, 0, 0)); // Red color for unavailable
        }
    }
    
    private void toggleSwitch8PropertyChange(java.beans.PropertyChangeEvent evt) {
        if (toggleSwitch8.isSelected()) {
            bay8.setText("Available");
            bay8.setForeground(new java.awt.Color(0, 128, 0)); // Green color for available
        } else {
            bay8.setText("Unavailable");
            bay8.setForeground(new java.awt.Color(255, 0, 0)); // Red color for unavailable
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
    private javax.swing.JButton quebutton;
    private javax.swing.JButton staffbutton;
    private cephra.Admin.ToggleSwitch toggleSwitch1;
    private cephra.Admin.ToggleSwitch toggleSwitch2;
    private cephra.Admin.ToggleSwitch toggleSwitch3;
    private cephra.Admin.ToggleSwitch toggleSwitch4;
    private cephra.Admin.ToggleSwitch toggleSwitch5;
    private cephra.Admin.ToggleSwitch toggleSwitch6;
    private cephra.Admin.ToggleSwitch toggleSwitch7;
    private cephra.Admin.ToggleSwitch toggleSwitch8;
    // End of variables declaration//GEN-END:variables
}
