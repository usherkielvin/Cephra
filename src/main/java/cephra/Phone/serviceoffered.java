
package cephra.Phone;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.SwingUtilities;

public class serviceoffered extends javax.swing.JPanel {

    public serviceoffered() {
        initComponents();
        setPreferredSize(new java.awt.Dimension(350, 750));
        setSize(350, 750);
        setupLabelPosition(); // Set label position
        makeDraggable();
        
        // Check if user has active ticket and disable buttons if needed
        checkAndDisableChargeButtons();
        
        // Refresh when this panel becomes visible
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                checkAndDisableChargeButtons();
            }
        });
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
                    java.awt.Window window = SwingUtilities.getWindowAncestor(serviceoffered.this);
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

        normalcharge = new javax.swing.JButton();
        profilebutton = new javax.swing.JButton();
        historybutton = new javax.swing.JButton();
        fastcharge = new javax.swing.JButton();
        linkbutton = new javax.swing.JButton();
        homebutton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(350, 750));
        setPreferredSize(new java.awt.Dimension(350, 750));
        setLayout(null);

        normalcharge.setBorder(null);
        normalcharge.setBorderPainted(false);
        normalcharge.setContentAreaFilled(false);
        normalcharge.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                normalchargeActionPerformed(evt);
            }
        });
        add(normalcharge);
        normalcharge.setBounds(30, 380, 280, 120);

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

        fastcharge.setBorder(null);
        fastcharge.setBorderPainted(false);
        fastcharge.setContentAreaFilled(false);
        fastcharge.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fastchargeActionPerformed(evt);
            }
        });
        add(fastcharge);
        fastcharge.setBounds(30, 243, 280, 120);

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

        homebutton.setBorder(null);
        homebutton.setBorderPainted(false);
        homebutton.setContentAreaFilled(false);
        homebutton.setFocusPainted(false);
        homebutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                homebuttonActionPerformed(evt);
            }
        });
        add(homebutton);
        homebutton.setBounds(140, 680, 50, 40);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Photos/fast.png"))); // NOI18N
        add(jLabel1);
        jLabel1.setBounds(-15, 0, 398, 750);
    }// </editor-fold>//GEN-END:initComponents

    // CUSTOM CODE - DO NOT REMOVE - NetBeans will regenerate form code but this method should be preserved
    // Setup label position to prevent NetBeans from changing it
    private void setupLabelPosition() {
        if (jLabel1 != null) {
            jLabel1.setBounds(-15, 0, 398, 750);
        }
    }

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

    private void homebuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_homebuttonActionPerformed
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frame.Phone) {
                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                        phoneFrame.switchPanel(new cephra.Phone.home());
                        break;
                    }
                }
            }
        });
    }//GEN-LAST:event_homebuttonActionPerformed

    private void fastchargeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fastchargeActionPerformed
       // Check if user already has an active ticket
       String username = cephra.CephraDB.getCurrentUsername();
               if (cephra.CephraDB.hasActiveTicket(username)) {
            javax.swing.JOptionPane.showMessageDialog(this, 
                "You already have an active charging ticket. Please complete your current session first.", 
                "Active Ticket", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Check if car is linked
        if (!cephra.Phone.AppState.isCarLinked) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Please link your car first before charging.",
                "Car Not Linked", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Prevent charging if battery is already full
        int batteryLevel = cephra.CephraDB.getUserBatteryLevel(username);
        if (batteryLevel >= 100) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Your battery is already 100%.",
                "Battery Full", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Check if fast charging bays are available
        if (!cephra.Admin.BayManagement.isFastChargingAvailable()) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Fast charging is currently unavailable. All fast charging bays are either unavailable or occupied. Please try normal charging or try again later.",
                "Fast Charging Unavailable", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
       
       // Set Fast Charging service before switching to QueueTicket panel
       cephra.Phone.QueueFlow.setCurrentServiceOnly("Fast Charging");
       SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frame.Phone) {
                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                        phoneFrame.switchPanel(new cephra.Phone.QueueTicket());
                        break;
                    }
                }
            }
        });
    }//GEN-LAST:event_fastchargeActionPerformed

    private void normalchargeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_normalchargeActionPerformed
        // Check if user already has an active ticket
        String username = cephra.CephraDB.getCurrentUsername();
        if (cephra.CephraDB.hasActiveTicket(username)) {
            javax.swing.JOptionPane.showMessageDialog(this, 
                "You already have an active charging ticket.", 
                "Active Ticket", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Check if car is linked
        if (!cephra.Phone.AppState.isCarLinked) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Please link your car first before charging.",
                "Car Not Linked", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Prevent charging if battery is already full
        int batteryLevel = cephra.CephraDB.getUserBatteryLevel(username);
        if (batteryLevel >= 100) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Your battery is already 100%.",
                "Battery Full", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Check if normal charging bays are available
        if (!cephra.Admin.BayManagement.isNormalChargingAvailable()) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Normal charging is currently unavailable. All normal charging bays are either unavailable or occupied. Please try fast charging or try again later.",
                "Normal Charging Unavailable", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Set Normal Charging service before switching to QueueTicket panel
        cephra.Phone.QueueFlow.setCurrentServiceOnly("Normal Charging");
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frame.Phone) {
                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                        phoneFrame.switchPanel(new cephra.Phone.QueueTicket());
                        break;
                    }
                }
            }
        });
    }//GEN-LAST:event_normalchargeActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton fastcharge;
    private javax.swing.JButton historybutton;
    private javax.swing.JButton homebutton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton linkbutton;
    private javax.swing.JButton normalcharge;
    private javax.swing.JButton profilebutton;
    // End of variables declaration//GEN-END:variables
    
    private void checkAndDisableChargeButtons() {
        // Always keep buttons enabled; gating is handled in action listeners with dialogs
        fastcharge.setEnabled(true);
        normalcharge.setEnabled(true);
        
        // Reset button appearance to normal
        fastcharge.setBackground(null);
        normalcharge.setBackground(null);
        fastcharge.setForeground(java.awt.Color.WHITE);
        normalcharge.setForeground(java.awt.Color.WHITE);
        
        // No tooltips to avoid gray hints
        fastcharge.setToolTipText(null);
        normalcharge.setToolTipText(null);
    }
    
    // Remove disabled-button click handler utilities as they're no longer needed

    @Override
    public void addNotify() {
        super.addNotify();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                setupLabelPosition();
                // Refresh button status when screen becomes visible
                checkAndDisableChargeButtons();
            }
        });
    }
}
