package cephra.Frame;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Phone extends javax.swing.JFrame {

    private Point dragStartPoint;
    private JLabel Iphoneframe;
    public Phone() {

        setUndecorated(true);
        initComponents();
        setSize(370, 750);
        setResizable(false);
        setAppIcon();
        addEscapeKeyListener();
        makeDraggable();
        // Use absolute positioning for phone panels
        getContentPane().setLayout(null);
        
        // Add curved edges to the frame
        setShape(new java.awt.geom.RoundRectangle2D.Double(0, 0, 370, 750, 120, 120));
        
        // Refresh ticket counters when Phone frame is created
        try {
            cephra.Phone.Utilities.QueueFlow.refreshCountersFromDatabase();
        } catch (Exception e) {
            System.err.println("Error refreshing ticket counters: " + e.getMessage());
        }

        // Start with loading screen panel
        switchPanel(new cephra.Phone.UserProfile.Loading_Screen());
        
        // Create and setup phone frame overlay to always appear on top
        PhoneFrame();

        // Initialize and start the time label updates
        updateTime();
        startTimeTimer();
        }

    private void setAppIcon() {
        try {
            java.net.URL iconUrl = getClass().getResource("/cephra/Cephra Images/lod.png");
            if (iconUrl != null) {
                setIconImage(new javax.swing.ImageIcon(iconUrl).getImage());
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
        Iphoneframe.setBounds(0, 0, 370, 750);
        Iphoneframe.setHorizontalAlignment(SwingConstants.CENTER);
        Iphoneframe.setOpaque(false);
        
        // Add to layered pane to ensure it's always on top
        getRootPane().getLayeredPane().add(Iphoneframe, JLayeredPane.DRAG_LAYER);
        
        // Move time label to layered pane above the phone frame
        try {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    if (jLabel1.getParent() == getContentPane()) {
                        getContentPane().remove(jLabel1);
                    }
                    jLabel1.setOpaque(false);
                    jLabel1.setForeground(Color.BLACK);
                    jLabel1.setVisible(true);
                    jLabel1.setBounds(0, 0, 55, 20);
                    getRootPane().getLayeredPane().add(jLabel1, JLayeredPane.MODAL_LAYER);
                    getRootPane().getLayeredPane().moveToFront(jLabel1);
                    getRootPane().revalidate();
                    getRootPane().repaint();
                }
            });
        } catch (Exception ignore) {}
        
        // Ensure visibility
        Iphoneframe.setVisible(true);
        jLabel1.setVisible(true);
        getRootPane().getLayeredPane().moveToFront(jLabel1);
        getRootPane().getLayeredPane().moveToFront(Iphoneframe);
    }

    public void switchPanel(javax.swing.JPanel newPanel) {
        getContentPane().removeAll();
        newPanel.setBounds(0, -6, 370, 756);
        getContentPane().add(newPanel);
        revalidate();
        repaint();
        
        // Ensure overlay stays on top
        ensureIphoneFrameOnTop();
    }

    public void ensureIphoneFrameOnTop() {
        if (Iphoneframe != null) {
            getRootPane().getLayeredPane().moveToFront(Iphoneframe);
        }
        if (jLabel1 != null) {
            getRootPane().getLayeredPane().moveToFront(jLabel1);
        }
    }
    
    private void updateTime() {
        if (jLabel1 != null) {
            java.time.LocalTime now = java.time.LocalTime.now();
            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("h:mm a");
            jLabel1.setText(now.format(formatter));
        }
    }
    
    private void startTimeTimer() {
        javax.swing.Timer timer = new javax.swing.Timer(60000, _ -> updateTime());
        timer.setRepeats(true);
        timer.start();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();

        jLabel1.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        jLabel1.setText("12:24");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(315, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addContainerGap(730, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}
