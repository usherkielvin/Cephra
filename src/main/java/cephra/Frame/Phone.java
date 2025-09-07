package cephra.Frame;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javax.swing.*;

/**
 * Phone frame class that creates a mobile-like interface with curved edges
 * and draggable functionality.
 */
public class Phone extends JFrame {
    
    // Frame dimensions
    private static final int FRAME_WIDTH = 370;
    private static final int FRAME_HEIGHT = 750;
    private static final int CORNER_RADIUS = 120;
    
    private Point dragStartPoint;
    private JLabel iphoneFrame;
    public Phone() {
        setUndecorated(true);
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setResizable(false);
        
        // Add curved edges to the frame
        setShape(new RoundRectangle2D.Double(0, 0, FRAME_WIDTH, FRAME_HEIGHT, CORNER_RADIUS, CORNER_RADIUS));
        
        initComponents();
        setAppIcon();
        addEscapeKeyListener();
        makeDraggable();
        
        // Refresh ticket counters when Phone frame is created
        try {
            cephra.Phone.QueueFlow.refreshCountersFromDatabase();
        } catch (Exception e) {
            System.err.println("Error refreshing ticket counters: " + e.getMessage());
        }

        // Start with loading screen panel
        switchPanel(new cephra.Phone.Loading_Screen());
        
        // Create and setup phone frame overlay to always appear on top
        setupPhoneFrame();
        
        // Start the time display timer
        startTimeTimer();
    }

    /**
     * Sets the application icon from resources.
     */
    private void setAppIcon() {
        try {
            java.net.URL iconUrl = getClass().getResource("/cephra/Cephra Images/lod.png");
            if (iconUrl != null) {
                setIconImage(new ImageIcon(iconUrl).getImage());
                System.out.println("App icon loaded successfully from: " + iconUrl);
            } else {
                System.err.println("Could not find app icon: lod.png");
            }
        } catch (Exception e) {
            System.err.println("Error loading app icon: " + e.getMessage());
        }
    }

    /**
     * Adds escape key listener to close the application.
     */
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

    /**
     * Makes the frame draggable by adding mouse listeners.
     */
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

    /**
     * Sets up the phone frame overlay that appears on top of content.
     */
    private void setupPhoneFrame() {
        iphoneFrame = new JLabel();
        iphoneFrame.setIcon(new ImageIcon(getClass().getResource("/cephra/Cephra Images/PHONEFRAME.png")));
        iphoneFrame.setBounds(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
        iphoneFrame.setHorizontalAlignment(SwingConstants.CENTER);
        iphoneFrame.setOpaque(false);
        
        // Add phone frame to layered pane
        getRootPane().getLayeredPane().add(iphoneFrame, JLayeredPane.DRAG_LAYER);
        
        // Ensure time label is visible and on top
        if (jLabel1 != null) {
            jLabel1.setVisible(true);
            jLabel1.setOpaque(false);
            getRootPane().getLayeredPane().moveToFront(jLabel1);
        }
        
        // Ensure phone frame is visible
        iphoneFrame.setVisible(true);
        getRootPane().getLayeredPane().moveToFront(iphoneFrame);
    }

    /**
     * Switches the current panel to a new panel.
     * @param newPanel the panel to switch to
     */
    public void switchPanel(JPanel newPanel) {
        getContentPane().removeAll();
        newPanel.setLocation(0, -10); // Move up 10 pixels
        getContentPane().add(newPanel);
        revalidate();
        repaint();
        
        // Ensure overlay stays on top
        ensureIphoneFrameOnTop();
    }

    /**
     * Ensures the iPhone frame overlay stays on top of all content.
     */
    public void ensureIphoneFrameOnTop() {
        if (jLabel1 != null) {
            getRootPane().getLayeredPane().moveToFront(jLabel1);
        }
        if (iphoneFrame != null) {
            getRootPane().getLayeredPane().moveToFront(iphoneFrame);
        }
    }
    
    /**
     * Updates the time display on the status bar.
     */
    private void updateTime() {
        if (jLabel1 != null) {
            LocalTime now = LocalTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");
            jLabel1.setText(now.format(formatter));
        }
    }
    
    /**
     * Starts the timer to update the time display every minute.
     */
    private void startTimeTimer() {
        // Set initial time
        updateTime();
        
        // Start timer to update every minute
        Timer timer = new Timer(60000, _ -> updateTime());
        timer.setRepeats(true);
        timer.start();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();

        jLabel1.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        jLabel1.setText("12:24");
        jLabel1.setBounds(39, 21, 55, 20); // Set absolute positioning (moved to match form)
        jLabel1.setForeground(Color.BLACK); // Make text black
        jLabel1.setOpaque(false); // Make background transparent
        jLabel1.setVisible(true); // Ensure it's visible

        // Add time label to layered pane instead of content pane
        getRootPane().getLayeredPane().add(jLabel1, JLayeredPane.POPUP_LAYER);
        
        // Set content pane layout to null for absolute positioning
        getContentPane().setLayout(null);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}
