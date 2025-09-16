package cephra.Frame;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JPanel;

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
        
        // Start with the Login panel (Splash removed)
        switchPanel(new cephra.Admin.Login());
    }

    private void setAppIcon() {
        try {
            // Try different resource paths to find the icon
            java.net.URL iconUrl = getClass().getResource("/cephra/Cephra Images/lod.png");
            if (iconUrl == null) {
                iconUrl = getClass().getClassLoader().getResource("cephra/Cephra Images/lod.png");
            }
            if (iconUrl == null) {
                iconUrl = getClass().getResource("/cephra/Photos/lod.png");
            }
            
            if (iconUrl != null) {
                setIconImage(new javax.swing.ImageIcon(iconUrl).getImage());
            } else {
                System.err.println("Could not find admin app icon: lod.png");
            }
        } catch (Exception e) {
            System.err.println("Error loading admin app icon: " + e.getMessage());
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
        
        // Update labelStaff in admin panels if it's an admin panel
        updateAdminPanelLabel(newPanel);
        
        // Hide/show buttons based on user role
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
            // Use reflection to find and update labelStaff in admin panels
            java.lang.reflect.Field labelField = panel.getClass().getDeclaredField("labelStaff");
            labelField.setAccessible(true);
            javax.swing.JLabel labelStaff = (javax.swing.JLabel) labelField.get(panel);
            if (labelStaff != null) {
                // Get the firstname from database instead of using username
                String firstname = cephra.Database.CephraDB.getStaffFirstName(loggedInUsername);
                labelStaff.setText(firstname + "!");
            }
        } catch (Exception e) {
            // Not an admin panel or labelStaff not found, ignore
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