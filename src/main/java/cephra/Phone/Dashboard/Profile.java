package cephra.Phone.Dashboard;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.SwingUtilities;

public class Profile extends javax.swing.JPanel {

    public Profile() {
        initComponents();
        setPreferredSize(new java.awt.Dimension(370, 750));
        setSize(370, 750);
        setupLabelPosition(); // Set label position
        makeDraggable();
        
        // Get current user's email, firstname, and lastname
        String email = cephra.CephraDB.getCurrentEmail();
        String firstname = cephra.CephraDB.getCurrentFirstname();
        String lastname = cephra.CephraDB.getCurrentLastname();
        
        // Set the labels
        if (gmailProf != null) {
            gmailProf.setText(email != null ? email : "");
        }
        if (Fullname != null) {
            // Combine firstname and lastname
            String fullName = "";
            if (firstname != null && !firstname.trim().isEmpty()) {
                fullName = firstname.trim();
            }
            if (lastname != null && !lastname.trim().isEmpty()) {
                if (!fullName.isEmpty()) {
                    fullName += " " + lastname.trim();
                } else {
                    fullName = lastname.trim();
                }
            }
            Fullname.setText(fullName);
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
                    java.awt.Window window = SwingUtilities.getWindowAncestor(Profile.this);
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

        historybutton = new javax.swing.JButton();
        homebutton = new javax.swing.JButton();
        logout = new javax.swing.JButton();
        linkbutton = new javax.swing.JButton();
        charge = new javax.swing.JButton();
        Help = new javax.swing.JButton();
        Fullname = new javax.swing.JLabel();
        gmailProf = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(370, 750));
        setPreferredSize(new java.awt.Dimension(370, 750));
        setLayout(null);

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
        historybutton.setBounds(230, 683, 30, 40);

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
        homebutton.setBounds(170, 680, 40, 40);

        logout.setBorder(null);
        logout.setBorderPainted(false);
        logout.setContentAreaFilled(false);
        logout.setFocusPainted(false);
        logout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutActionPerformed(evt);
            }
        });
        add(logout);
        logout.setBounds(80, 610, 250, 40);

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
        linkbutton.setBounds(120, 680, 30, 40);

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
        charge.setBounds(60, 680, 40, 40);

        Help.setBorderPainted(false);
        Help.setContentAreaFilled(false);
        Help.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HelpActionPerformed(evt);
            }
        });
        add(Help);
        Help.setBounds(50, 520, 290, 50);

        Fullname.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        Fullname.setText("Usher Kielvin Ponce");
        add(Fullname);
        Fullname.setBounds(130, 120, 220, 40);

        gmailProf.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        gmailProf.setText("Usher@gmail.com");
        add(gmailProf);
        gmailProf.setBounds(150, 156, 190, 20);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/PhoneProfile.png"))); // NOI18N
        add(jLabel1);
        jLabel1.setBounds(0, 0, 370, 750);
    }// </editor-fold>//GEN-END:initComponents

    // CUSTOM CODE - DO NOT REMOVE - NetBeans will regenerate form code but this method should be preserved
    // Setup label position to prevent NetBeans from changing it
    private void setupLabelPosition() {
        if (jLabel1 != null) {
            jLabel1.setBounds(0, 0, 370, 750);
        }
    }

    private void chargeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chargeActionPerformed
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frame.Phone) {
                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                        phoneFrame.switchPanel(new cephra.Phone.Dashboard.ChargingOption());
                        break;
                    }
                }
            }
        });
    }//GEN-LAST:event_chargeActionPerformed

    private void historybuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_historybuttonActionPerformed
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frame.Phone) {
                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                        phoneFrame.switchPanel(new cephra.Phone.Dashboard.phonehistory());
                        break;
                    }
                }
            }
        });
    }//GEN-LAST:event_historybuttonActionPerformed

    private void logoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutActionPerformed
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Only reset battery to random level on logout when battery is 100%
                try {
                    String username = cephra.CephraDB.getCurrentUsername();
                    if (username != null && !username.isEmpty()) {
                        int currentBatteryLevel = cephra.CephraDB.getUserBatteryLevel(username);
                        
                                                // Only reset battery if it's 100%
                        if (currentBatteryLevel >= 100) {
                            // Reset battery to random level (15-50%) when logging out with full battery
                            java.util.Random random = new java.util.Random();
                            int newBatteryLevel = 15 + random.nextInt(36); // 15 to 50
                            
                            // Save to database first
                            cephra.CephraDB.setUserBatteryLevel(username, newBatteryLevel);
                            System.out.println("Profile: Reset battery level for " + username + " from " + currentBatteryLevel + "% to " + newBatteryLevel + "% on logout");
                            
                            // Check for duplicate battery level entries
                            cephra.CephraDB.checkDuplicateBatteryLevels(username);
                            
                            // Verify the database update worked
                            try {
                                int verifyBattery = cephra.CephraDB.getUserBatteryLevel(username);
                                System.out.println("Profile: Verified database update - battery level is now " + verifyBattery + "%");
                                if (verifyBattery != newBatteryLevel) {
                                    System.err.println("Profile: WARNING - Database update failed! Expected " + newBatteryLevel + "% but got " + verifyBattery + "%");
                                }
                            } catch (Exception verifyEx) {
                                System.err.println("Profile: Error verifying battery level update: " + verifyEx.getMessage());
                            }
                            
                            // Keep car linked - users only link once
                            // cephra.Phone.Utilities.AppState.isCarLinked = false; // Removed - keep car linked
                         
                            // Refresh any visible Porsche panel to show the new battery level
                            try {
                                java.awt.Window[] allWindows = java.awt.Window.getWindows();
                                for (java.awt.Window window : allWindows) {
                                    if (window instanceof cephra.Frame.Phone) {
                                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                                        if (phoneFrame.isVisible()) {
                                            // Find and refresh any PorscheTaycan panel
                                            java.awt.Component[] components = phoneFrame.getContentPane().getComponents();
                                            for (java.awt.Component comp : components) {
                                                if (comp instanceof cephra.Phone.LinkedCar) {
                                                    cephra.Phone.LinkedCar porschePanel = (cephra.Phone.LinkedCar) comp;
                                                    porschePanel.refreshBatteryDisplay();
                                                    System.out.println("Profile: Refreshed Porsche panel to show new battery level: " + newBatteryLevel + "%");
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                            } catch (Exception refreshEx) {
                                System.err.println("Error refreshing Porsche panel: " + refreshEx.getMessage());
                            }
                                                 } else {
                             // Battery is not 100%, no reset needed
                             System.out.println("Profile: User logged out with " + currentBatteryLevel + "% battery - no reset needed");
                         }
                    }
                } catch (Exception e) {
                    System.err.println("Error resetting battery level on logout: " + e.getMessage());
                }
                
                // Properly logout the current user
                cephra.CephraDB.logoutCurrentUser();
                System.out.println("Profile: User has been logged out");
                
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frame.Phone) {
                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                        phoneFrame.switchPanel(new cephra.Phone.Phonelogin());
                        break;
                    }
                }
            }
        });
    }//GEN-LAST:event_logoutActionPerformed

    private void homebuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_homebuttonActionPerformed
       SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frame.Phone) {
                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                        phoneFrame.switchPanel(new cephra.Phone.Dashboard.Home());
                        break;
                    }
                }
            }
        });
    }//GEN-LAST:event_homebuttonActionPerformed

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

    private void HelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HelpActionPerformed
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frame.Phone) {
                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                        phoneFrame.switchPanel(new cephra.Phone.helpcenter());
                        break;
                    }
                }
            }
        });           
    }//GEN-LAST:event_HelpActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Fullname;
    private javax.swing.JButton Help;
    private javax.swing.JButton charge;
    private javax.swing.JLabel gmailProf;
    private javax.swing.JButton historybutton;
    private javax.swing.JButton homebutton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton linkbutton;
    private javax.swing.JButton logout;
    // End of variables declaration//GEN-END:variables

    @Override
    public void addNotify() {
        super.addNotify();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                setupLabelPosition();
                if (jLabel1 != null) {
                    setComponentZOrder(jLabel1, getComponentCount() - 1);
                }
            }
        });
    }
}
