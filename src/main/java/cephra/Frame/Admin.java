package cephra.Frame;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Admin extends javax.swing.JFrame {
    
    private String loggedInUsername = "Admin";

    public Admin() {
        setUndecorated(true);
        initComponents();
        setSize(1000, 750);
        setResizable(false);
        setAppIcon();
        addEscapeKeyListener();
        makeDraggable();
        switchPanel(new cephra.Admin.Login());
    }

    private void setAppIcon() {
        java.net.URL iconUrl = getClass().getResource("/cephra/Cephra Images/Applogo.png");
        if (iconUrl != null) {
            setIconImage(new javax.swing.ImageIcon(iconUrl).getImage());
        }
    }

    private void addEscapeKeyListener() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    System.exit(0);
                }
            }
        });
        setFocusable(true);
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
                    Point currentLocation = getLocation();
                    setLocation(
                        currentLocation.x + e.getX() - dragPoint[0].x,
                        currentLocation.y + e.getY() - dragPoint[0].y
                    );
                }
            }
        });
    }

    public void switchPanel(JPanel newPanel) {
        getContentPane().removeAll();
        getContentPane().add(newPanel, BorderLayout.CENTER);
        updateAdminPanelLabel(newPanel);
        updateButtonVisibility(newPanel);       
        revalidate();
        repaint();
    }
    
    private void updateButtonVisibility(JPanel panel) {
        try {
            // Check if user is admin
            boolean isAdmin = cephra.Database.CephraDB.isAdminUser(loggedInUsername);
            
            // Hide Staff Records button for non-admin users
            try {
                java.lang.reflect.Field staffButtonField = panel.getClass().getDeclaredField("staffbutton");
                staffButtonField.setAccessible(true);
                javax.swing.JButton staffButton = (javax.swing.JButton) staffButtonField.get(panel);
                if (staffButton != null) {
                    staffButton.setVisible(isAdmin);
                }
            } catch (Exception e) {
                // Button not found in this panel, ignore
            }
            
            // Hide Business Overview button for non-admin users
            try {
                java.lang.reflect.Field businessButtonField = panel.getClass().getDeclaredField("businessbutton");
                businessButtonField.setAccessible(true);
                javax.swing.JButton businessButton = (javax.swing.JButton) businessButtonField.get(panel);
                if (businessButton != null) {
                    businessButton.setVisible(isAdmin);
                }
            } catch (Exception e) {
                // Button not found in this panel, ignore
            }
            
            // If user is not admin and trying to access restricted panels, redirect to Bay Management
            if (!isAdmin) {
                String panelName = panel.getClass().getSimpleName();
                if ("StaffRecord".equals(panelName) || "Business_Overview".equals(panelName)) {
                    // Redirect to Bay Management
                    switchPanel(new cephra.Admin.BayManagement());
                }
            }
            
        } catch (Exception e) {
            System.err.println("Error updating button visibility: " + e.getMessage());
        }
    }
    
    public void setLoggedInUsername(String username) {
        this.loggedInUsername = username;
    }
    
    private void updateAdminPanelLabel(JPanel panel) {
        try {
            java.lang.reflect.Field labelField = panel.getClass().getDeclaredField("labelStaff");
            labelField.setAccessible(true);
            javax.swing.JLabel labelStaff = (javax.swing.JLabel) labelField.get(panel);
            if (labelStaff != null) {
                String firstname = cephra.Database.CephraDB.getStaffFirstName(loggedInUsername);
                labelStaff.setText(firstname + "!");
            }
        } catch (Exception e) {
           
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1000, 750));
        setResizable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1000, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 750, Short.MAX_VALUE)
        );

        // pack(); // Removed to prevent frame from becoming displayable before setUndecorated
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}