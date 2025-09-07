package cephra.Frame;

import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.SwingConstants;

public class Phone extends javax.swing.JFrame {

    private Point dragStartPoint;
    private JLabel Iphoneframe;
    public Phone() {

        setUndecorated(true);
        initComponents();
        setSize(350, 750);
        setResizable(false);
        setAppIcon();
        addEscapeKeyListener();
        makeDraggable();
        
        // Add curved edges to the frame
        setShape(new java.awt.geom.RoundRectangle2D.Double(0, 0, 350, 750, 120, 120));
        
        // Refresh ticket counters when Phone frame is created
        try {
            cephra.Phone.QueueFlow.refreshCountersFromDatabase();
        } catch (Exception e) {
            System.err.println("Error refreshing ticket counters: " + e.getMessage());
        }

        // Start with loading screen panel
        switchPanel(new cephra.Phone.Loading_Screen());
        
        // Create and setup phone frame overlay to always appear on top
        PhoneFrame();
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
                System.out.println("App icon loaded successfully from: " + iconUrl);
            } else {
                System.err.println("Could not find app icon: lod.png");
            }
        } catch (Exception e) {
            System.err.println("Error loading app icon: " + e.getMessage());
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
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                dragStartPoint = e.getPoint();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (dragStartPoint != null) {
                    Point currentLocation = getLocation();
                    setLocation(
                        currentLocation.x + e.getX() - dragStartPoint.x,
                        currentLocation.y + e.getY() - dragStartPoint.y
                    );
                }
            }
        });
    }

    private void PhoneFrame() {
        Iphoneframe = new JLabel();
        Iphoneframe.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/PHONEFRAME.png")));
        Iphoneframe.setBounds(-30, 0, 405, 750); // center top
        Iphoneframe.setHorizontalAlignment(SwingConstants.CENTER);
        Iphoneframe.setOpaque(false); // Make sure it's transparent
        
        // Add to the root pane's layered pane to ensure it's always on top
        // Using DRAG_LAYER which is higher than POPUP_LAYER to ensure iPhone frame is always on top
        getRootPane().getLayeredPane().add(Iphoneframe, JLayeredPane.DRAG_LAYER);
        
        // Add the time label to a higher layer so it appears above the phone frame
        getRootPane().getLayeredPane().add(jLabel1, JLayeredPane.MODAL_LAYER);
        
        // Make sure it's visible and on top
        Iphoneframe.setVisible(true);
        jLabel1.setVisible(true);
        getRootPane().getLayeredPane().moveToFront(Iphoneframe);
        getRootPane().getLayeredPane().moveToFront(jLabel1);
    }

    public void switchPanel(javax.swing.JPanel newPanel) {
        getContentPane().removeAll();
        // Add padding to move panel up a few pixels
        newPanel.setLocation(0, -10); // Move up 10 pixels
        getContentPane().add(newPanel);
        revalidate();
        repaint();
        
        // Ensure iPhone frame and time label stay on top after panel switch
        if (Iphoneframe != null) {
            getRootPane().getLayeredPane().moveToFront(Iphoneframe);
        }
        if (jLabel1 != null) {
            getRootPane().getLayeredPane().moveToFront(jLabel1);
        }
    }

    /**
     * Ensures the iPhone frame and time label are always on top of all other components
     * This should be called after adding any new components to the layered pane
     */
    public void ensureIphoneFrameOnTop() {
        if (Iphoneframe != null) {
            getRootPane().getLayeredPane().moveToFront(Iphoneframe);
        }
        if (jLabel1 != null) {
            getRootPane().getLayeredPane().moveToFront(jLabel1);
        }
    }
    
    /**
     * Updates the time label with current time in 12-hour format
     */
    private void updateTime() {
        if (jLabel1 != null) {
            java.time.LocalTime now = java.time.LocalTime.now();
            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("h:mm a");
            String timeString = now.format(formatter);
            jLabel1.setText(timeString);
        }
    }
    
    /**
     * Starts a timer to update the time every minute
     */
    private void startTimeTimer() {
        javax.swing.Timer timer = new javax.swing.Timer(60000, _ -> updateTime()); // Update every minute
        timer.setRepeats(true);
        timer.start();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();

        jLabel1.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        jLabel1.setForeground(java.awt.Color.BLACK); // Make text black
        jLabel1.setBounds(28, 21, 55, 20); // Set absolute positioning
        
        // Set initial time and start timer
        updateTime();
        startTimeTimer();

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 350, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 750, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}
