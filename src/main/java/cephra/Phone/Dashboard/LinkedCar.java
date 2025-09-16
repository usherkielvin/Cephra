
package cephra.Phone.Dashboard;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import java.util.Random;

public class LinkedCar extends javax.swing.JPanel {

    // Array of car image paths (c1.png to c10.png)
    private final String[] carImages = {
        "/cephra/Cephra Images/c1.png",
        "/cephra/Cephra Images/c2.png",
        "/cephra/Cephra Images/c3.png",
        "/cephra/Cephra Images/c4.png",
        "/cephra/Cephra Images/c5.png",
        "/cephra/Cephra Images/c6.png",
        "/cephra/Cephra Images/c7.png",
        "/cephra/Cephra Images/c8.png",
        "/cephra/Cephra Images/c9.png",
        "/cephra/Cephra Images/c10.png"
    };

    public LinkedCar() {
        initComponents();
         setPreferredSize(new java.awt.Dimension(370, 750));
        setSize(370, 750);
        setupLabelPosition(); // Set label position
        makeDraggable();
        
        // Update battery percentage from user's stored level
        refreshBatteryDisplay();
        
        // Set random car image for user
        setRandomCarImage();
        
        // Ensure car positioning follows NetBeans form settings
        ensureCarPositioning();
        
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
            String username = cephra.Database.CephraDB.getCurrentUsername();
            if (username != null && !username.isEmpty()) {
                int batteryLevel = cephra.Database.CephraDB.getUserBatteryLevel(username);
                
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
                    
                    // Rotate car based on battery percentage (0% = 0°, 50% = 90°, 100% = 180°)
                    updateCarRotation(batteryLevel);
                    
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
                    java.awt.Window window = SwingUtilities.getWindowAncestor(LinkedCar.this);
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
    
    // Method to rotate the car icon based on degrees
    private ImageIcon rotateIcon(ImageIcon icon, double degrees) {
        int w = icon.getIconWidth();
        int h = icon.getIconHeight();
        
        java.awt.image.BufferedImage rotated = new java.awt.image.BufferedImage(w, h, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = rotated.createGraphics();
        g2d.rotate(Math.toRadians(degrees), w/2, h/2);
        g2d.drawImage(icon.getImage(), 0, 0, null);
        g2d.dispose();
        
        return new ImageIcon(rotated);
    }
    
    // Method to update car rotation based on battery percentage
    private void updateCarRotation(int batteryLevel) {
        try {
            // Calculate rotation: 0% -> 0°, 50% -> 90°, 100% -> 180°
            double degrees = (batteryLevel / 100.0) * 180.0;
            
            // Get the current car icon and rotate it
            if (car.getIcon() != null && car.getIcon() instanceof ImageIcon) {
                // Get the original car image path based on current user
                String username = cephra.Database.CephraDB.getCurrentUsername();
                if (username != null && !username.isEmpty()) {
                    int carIndex = cephra.Database.CephraDB.getUserCarIndex(username);
                    if (carIndex >= 0 && carIndex < carImages.length) {
                        // Create fresh icon from resource to avoid cumulative rotation
                        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/cephra/Cephra Images/ver.png"));
                        ImageIcon rotatedIcon = rotateIcon(originalIcon, degrees);
                        bar.setIcon(rotatedIcon);
                        
                        System.out.println("LinkedCar: Rotated car to " + degrees + "° for battery level " + batteryLevel + "%");
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error rotating car icon: " + e.getMessage());
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        car = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        profilebutton = new javax.swing.JButton();
        historybutton = new javax.swing.JButton();
        homebutton2 = new javax.swing.JButton();
        linkbutton = new javax.swing.JButton();
        charge = new javax.swing.JButton();
        batterypercent = new javax.swing.JLabel();
        km = new javax.swing.JLabel();
        cover = new javax.swing.JLabel();
        bar = new javax.swing.JLabel();
        chargingTypeLabel = new javax.swing.JLabel();
        chargingTimeLabel = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setLayout(null);

        car.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/c7.png"))); // NOI18N
        car.setPreferredSize(new java.awt.Dimension(301, 226));
        add(car);
        car.setBounds(36, 90, 301, 226);

        jPanel1.setOpaque(false);
        jPanel1.setLayout(null);
        add(jPanel1);
        jPanel1.setBounds(20, 50, 140, 100);

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
        profilebutton.setBounds(260, 680, 50, 40);

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

        batterypercent.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        batterypercent.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        batterypercent.setText("25 %");
        add(batterypercent);
        batterypercent.setBounds(230, 430, 80, 30);

        km.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        km.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        km.setText("400 km");
        add(km);
        km.setBounds(200, 480, 140, 30);

        cover.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/co.png"))); // NOI18N
        add(cover);
        cover.setBounds(210, 390, 120, 120);

        bar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/ver.png"))); // NOI18N
        add(bar);
        bar.setBounds(210, 390, 120, 120);

        chargingTypeLabel.setFont(new java.awt.Font("Segoe UI Semibold", 1, 18)); // NOI18N
        chargingTypeLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        chargingTypeLabel.setText("Fast Charging");
        add(chargingTypeLabel);
        chargingTypeLabel.setBounds(40, 410, 140, 25);

        chargingTimeLabel.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        chargingTimeLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        chargingTimeLabel.setText("100%");
        add(chargingTimeLabel);
        chargingTimeLabel.setBounds(60, 440, 100, 50);

        jLabel2.setText("3hrs 12mins");
        add(jLabel2);
        jLabel2.setBounds(180, 568, 90, 16);

        jLabel3.setText("2hrs 50mins");
        add(jLabel3);
        jLabel3.setBounds(180, 585, 90, 16);

        jLabel4.setText("1hr 30mins");
        add(jLabel4);
        jLabel4.setBounds(180, 603, 90, 16);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/linked.png"))); // NOI18N
        add(jLabel1);
        jLabel1.setBounds(3, 0, 380, 750);
    }// </editor-fold>//GEN-END:initComponents

    private void profilebuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_profilebuttonActionPerformed
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frame.Phone) {
                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                        phoneFrame.switchPanel(new cephra.Phone.Dashboard.Profile());
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
                        phoneFrame.switchPanel(new cephra.Phone.Dashboard.ChargeHistory());
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
                        phoneFrame.switchPanel(cephra.Phone.Dashboard.Home.getAppropriateHomePanel());
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
                        phoneFrame.switchPanel(new cephra.Phone.Dashboard.LinkConnect());
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
                        phoneFrame.switchPanel(new cephra.Phone.Dashboard.ChargingOption());
                        break;
                    }
                }
            }
        });
    }//GEN-LAST:event_chargeActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel bar;
    private javax.swing.JLabel batterypercent;
    private javax.swing.JLabel car;
    private javax.swing.JButton charge;
    private javax.swing.JLabel chargingTimeLabel;
    private javax.swing.JLabel chargingTypeLabel;
    private javax.swing.JLabel cover;
    private javax.swing.JButton historybutton;
    private javax.swing.JButton homebutton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel km;
    private javax.swing.JButton linkbutton;
    private javax.swing.JButton profilebutton;
    // End of variables declaration//GEN-END:variables
    
    // Method to set random car image for the current user
    private void setRandomCarImage() {
        try {
            String username = cephra.Database.CephraDB.getCurrentUsername();
            if (username != null && !username.isEmpty()) {
                // Get user's car index from database
                int carIndex = cephra.Database.CephraDB.getUserCarIndex(username);
                
                // If no car assigned yet, assign a random one
                if (carIndex == -1) {
                    carIndex = new Random().nextInt(carImages.length);
                    cephra.Database.CephraDB.setUserCarIndex(username, carIndex);
                    System.out.println("LinkedCar: Assigned car " + (carIndex + 1) + " to user " + username);
                }
                
                // Set the car image while preserving the form positioning (x=34, y=130, size=301x226)
                // Special sizing for c2.png: increase height by 2 pixels to show full image (228 instead of 226)
                if (carIndex >= 0 && carIndex < carImages.length) {
                    car.setIcon(new javax.swing.ImageIcon(getClass().getResource(carImages[carIndex])));
                    
                    // Special case for c2.png - increase height by 2 pixels to show cut-off part
                    if (carIndex == 1) { // c2.png is at index 1 (c1=0, c2=1, c3=2, etc.)
                        car.setBounds(36, 90, 301, 228); // Height increased from 226 to 228
                        car.setPreferredSize(new java.awt.Dimension(301, 228));
                        System.out.println("LinkedCar: Set c2.png car image with increased height (301x228) to show full image");
                    } else {
                        car.setBounds(36, 90, 301, 226);
                        car.setPreferredSize(new java.awt.Dimension(301, 226));
                        System.out.println("LinkedCar: Set car image to " + carImages[carIndex] + " for user " + username + " at position (34, 130)");
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error setting car image: " + e.getMessage());
            // Fallback to default car image with preserved positioning
            car.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/c1.png")));
            car.setBounds(36, 90, 301, 226);
            car.setPreferredSize(new java.awt.Dimension(301, 226));
        }
    }
    
    // Method to ensure car positioning follows NetBeans form settings
    private void ensureCarPositioning() {
        // Set car position and size according to NetBeans form settings
        // From form: x="34" y="130" width="-1" height="-1" preferredSize="301, 226"
        // Special case for c2.png: increase height by 2 pixels to show full image (228 instead of 226)
        try {
            String username = cephra.Database.CephraDB.getCurrentUsername();
            if (username != null && !username.isEmpty()) {
                int carIndex = cephra.Database.CephraDB.getUserCarIndex(username);
                if (carIndex == 1) { // c2.png is at index 1
                    car.setBounds(36, 90, 301, 228); // Height increased from 226 to 228
                    car.setPreferredSize(new java.awt.Dimension(301, 228));
                } else {
                    car.setBounds(36, 90, 301, 226);
                    car.setPreferredSize(new java.awt.Dimension(301, 226));
                }
            } else {
                car.setBounds(36, 90, 301, 226);
                car.setPreferredSize(new java.awt.Dimension(301, 226));
            }
        } catch (Exception e) {
            car.setBounds(36, 90, 301, 226);
            car.setPreferredSize(new java.awt.Dimension(301, 226));
        }
    }
    
    @Override
    public void addNotify() {
        super.addNotify();
        // Refresh battery display when panel becomes visible
        SwingUtilities.invokeLater(() -> {
            refreshBatteryDisplay();
            setRandomCarImage(); // Also refresh car image
            ensureCarPositioning(); // Ensure positioning is maintained
        });
    }
}
