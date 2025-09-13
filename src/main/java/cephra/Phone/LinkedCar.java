
package cephra.Phone;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.SwingUtilities;

public class PorscheTaycan extends javax.swing.JPanel {


    public PorscheTaycan() {
        initComponents();
         setPreferredSize(new java.awt.Dimension(370, 750));
        setSize(370, 750);
        setupLabelPosition(); // Set label position
        makeDraggable();
        
        // Update battery percentage from user's stored level
        refreshBatteryDisplay();
        
        // Add a focus listener to refresh battery display when panel becomes visible
        addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                refreshBatteryDisplay();
            }
        });
        
        // Also refresh when component becomes visible
        addHierarchyListener(new java.awt.event.HierarchyListener() {
            @Override
            public void hierarchyChanged(java.awt.event.HierarchyEvent e) {
                if ((e.getChangeFlags() & java.awt.event.HierarchyEvent.SHOWING_CHANGED) != 0) {
                    if (isShowing()) {
                        refreshBatteryDisplay();
                    }
                }
            }
        });
    }
    
    // Method to refresh battery display with current values from database
    public void refreshBatteryDisplay() {
        try {
            String username = cephra.CephraDB.getCurrentUsername();
            if (username != null && !username.isEmpty()) {
                int batteryLevel = cephra.CephraDB.getUserBatteryLevel(username);
                
                if (batteryLevel == -1) {
                    // No battery initialized yet - show "Link Car" message
                    batterypercent.setText("Link Car");
                    km.setText("0 km");
                    charge.setEnabled(false);
                    charge.setToolTipText("Please link your car first to initialize battery level.");
                    charge.setBackground(new java.awt.Color(200, 200, 200));
                    charge.setForeground(new java.awt.Color(100, 100, 100));
                    System.out.println("PorscheTaycan: No battery initialized for user " + username + " - showing 'Link Car' message");
                } else {
                    batterypercent.setText(batteryLevel + " %");
                    
                    // Update range based on battery level (roughly 8km per 1% battery)
                    int rangeKm = (int)(batteryLevel * 8);
                    km.setText(rangeKm + " km");
                    
                    // Keep charge button always enabled - full battery handling is done in action listener with JDialog
                    charge.setEnabled(true);
                    charge.setToolTipText(null);
                    // Reset button appearance
                    charge.setBackground(null);
                    charge.setForeground(java.awt.Color.WHITE);
                    
                    System.out.println("PorscheTaycan: Updated battery display to " + batteryLevel + "% for user " + username);
                }
            }
        } catch (Exception e) {
            System.err.println("Error refreshing battery display: " + e.getMessage());
        }
    }
    
    // CUSTOM CODE - DO NOT REMOVE - NetBeans will regenerate form code but this method should be preserved
    // Setup label position to prevent NetBeans from changing it
    private void setupLabelPosition() {
        if (jLabel1 != null) {
            jLabel1.setBounds(0, 0, 370, 750);
        }
    }
     private void makeDraggable() {
        final Point[] dragPoint = {null};

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                dragPoint[0] = e.getPoint();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (dragPoint[0] != null) {
                    java.awt.Window window = SwingUtilities.getWindowAncestor(PorscheTaycan.this);
                    if (window != null) {
                        Point currentLocation = window.getLocation();
                        window.setLocation(
                            currentLocation.x + e.getX() - dragPoint[0].x,
                            currentLocation.y + e.getY() - dragPoint[0].y
                        );
                    }
                }
            }
        });
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        profilebutton = new javax.swing.JButton();
        historybutton = new javax.swing.JButton();
        homebutton2 = new javax.swing.JButton();
        linkbutton = new javax.swing.JButton();
        charge = new javax.swing.JButton();
        batterypercent = new javax.swing.JLabel();
        km = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setLayout(null);

        jPanel1.setOpaque(false);
        jPanel1.setLayout(null);
        jPanel1.add(jLabel2);
        jLabel2.setBounds(0, -80, 350, 270);

        add(jPanel1);
        jPanel1.setBounds(0, 160, 350, 270);

        profilebutton.setBorder(null);
        profilebutton.setBorderPainted(false);
        profilebutton.setContentAreaFilled(false);
        profilebutton.setFocusPainted(false);
        profilebutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                profilebuttonActionPerformed(evt);
            }
        });
        add(profilebutton);
        profilebutton.setBounds(260, 670, 50, 50);

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
        historybutton.setBounds(200, 680, 50, 40);

        homebutton2.setBorder(null);
        homebutton2.setBorderPainted(false);
        homebutton2.setContentAreaFilled(false);
        homebutton2.setFocusPainted(false);
        homebutton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                homebutton2ActionPerformed(evt);
            }
        });
        add(homebutton2);
        homebutton2.setBounds(150, 680, 40, 40);

        linkbutton.setBorder(null);
        linkbutton.setBorderPainted(false);
        linkbutton.setContentAreaFilled(false);
        linkbutton.setFocusPainted(false);
        linkbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                linkbuttonActionPerformed(evt);
            }
        });
        add(linkbutton);
        linkbutton.setBounds(90, 680, 50, 40);

        charge.setBorder(null);
        charge.setBorderPainted(false);
        charge.setContentAreaFilled(false);
        charge.setFocusPainted(false);
        charge.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chargeActionPerformed(evt);
            }
        });
        add(charge);
        charge.setBounds(30, 680, 50, 40);

        batterypercent.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        batterypercent.setText("25 %");
        add(batterypercent);
        batterypercent.setBounds(20, 455, 210, 30);

        km.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        km.setText("400 km");
        add(km);
        km.setBounds(20, 515, 130, 30);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/hyundai.png"))); // NOI18N
        add(jLabel1);
        jLabel1.setBounds(-15, 0, 398, 750);
    }// </editor-fold>//GEN-END:initComponents

    private void profilebuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_profilebuttonActionPerformed
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frame.Phone) {
                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                        phoneFrame.switchPanel(new cephra.Phone.Profile());
                        break;
                    }
                }
            }
        });
    }//GEN-LAST:event_profilebuttonActionPerformed

    private void historybuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_historybuttonActionPerformed
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frame.Phone) {
                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                        phoneFrame.switchPanel(new cephra.Phone.phonehistory());
                        break;
                    }
                }
            }
        });
    }//GEN-LAST:event_historybuttonActionPerformed

    private void homebutton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_homebutton2ActionPerformed
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frame.Phone) {
                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                        phoneFrame.switchPanel(new cephra.Phone.Home());
                        break;
                    }
                }
            }
        });
    }//GEN-LAST:event_homebutton2ActionPerformed

    private void linkbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_linkbuttonActionPerformed
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frame.Phone) {
                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                        phoneFrame.switchPanel(new cephra.Phone.link());
                        break;
                    }
                }
            }
        });
    }//GEN-LAST:event_linkbuttonActionPerformed

    private void chargeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chargeActionPerformed
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frame.Phone) {
                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                        phoneFrame.switchPanel(new cephra.Phone.ChargingOption());
                        break;
                    }
                }
            }
        });
    }//GEN-LAST:event_chargeActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel batterypercent;
    private javax.swing.JButton charge;
    private javax.swing.JButton historybutton;
    private javax.swing.JButton homebutton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel km;
    private javax.swing.JButton linkbutton;
    private javax.swing.JButton profilebutton;
    // End of variables declaration//GEN-END:variables
    
    @Override
    public void addNotify() {
        super.addNotify();
        // Refresh battery display when panel becomes visible
        SwingUtilities.invokeLater(() -> refreshBatteryDisplay());
    }
}
