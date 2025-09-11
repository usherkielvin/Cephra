package cephra.Phone;

import java.awt.event.*;
import javax.swing.*;

public class Fullnotif extends javax.swing.JPanel {

    private Timer animationTimer;
    private Timer hideTimer;
    private int yPosition = -110; // Start position (hidden above the screen)
    private int targetY = 0; // Target position when fully shown
    private boolean isShowing = false;
    private static final int ANIMATION_SPEED = 5; // Pixels to move per step
    private static final int ANIMATION_DELAY = 10; // Milliseconds between steps
    private static final int DISPLAY_DURATION = 3000; // Display for 3 seconds
    
    public Fullnotif() {
        initComponents();
        setVisible(false); // Start hidden
    }
    
  
   
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setOpaque(false);
        setLayout(null);

        jButton1.setBorderPainted(false);
        jButton1.setContentAreaFilled(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        add(jButton1);
        jButton1.setBounds(0, 0, 330, 70);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 102));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/Done.png"))); // NOI18N
        jLabel1.setText("Ur car is now fullcharge");
        add(jLabel1);
        jLabel1.setBounds(0, 0, 330, 70);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
      
        
          SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frame.Phone) {
                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                        phoneFrame.switchPanel(new cephra.Phone.NotificationHistory());
                        break;
                    }
                }
            }
        });
        
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * Shows the notification with animation sliding down from the top
     */
    public void showNotification() {
        if (isShowing) return; // Prevent multiple show animations
        
        // History recording is now handled by the Queue class with proper ticket ID
        // No need to record duplicate history here
        
        isShowing = true;
        setVisible(true);
        yPosition = -110;
        setLocation(10, yPosition); // Start off-screen

        // Cancel existing timers if any
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }
        if (hideTimer != null && hideTimer.isRunning()) {
            hideTimer.stop();
        }
        
        // Create and start the show animation
        animationTimer = new Timer(ANIMATION_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (yPosition < targetY) {
                    yPosition += ANIMATION_SPEED;
                    setLocation(10, yPosition);
                    
                    // Ensure iPhone frame stays on top during animation
                    java.awt.Window[] windows = java.awt.Window.getWindows();
                    for (java.awt.Window window : windows) {
                        if (window instanceof cephra.Frame.Phone) {
                            ((cephra.Frame.Phone) window).ensureIphoneFrameOnTop();
                            break;
                        }
                    }
                } else {
                    // Animation completed
                    animationTimer.stop();
                    
                    // Schedule hide after duration
                    hideTimer = new Timer(DISPLAY_DURATION, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            hideNotification();
                        }
                    });
                    hideTimer.setRepeats(false);
                    hideTimer.start();
                }
            }
        });
        animationTimer.start();
    }
    
    /**
     * Hides the notification with animation sliding up
     */
    public void hideNotification() {
        if (!isShowing) return; // Only hide if showing
        
        // Cancel existing animation if running
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }
        
        // Create and start the hide animation
        animationTimer = new Timer(ANIMATION_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (yPosition > -110) {
                    yPosition -= ANIMATION_SPEED;
                    setLocation(10, yPosition);
                } else {
                    // Animation completed
                    animationTimer.stop();
                    setVisible(false);
                    isShowing = false;
                }
            }
        });
        animationTimer.start();
    }
    
    /**
     * Update the OTP text
     */
    
    
    /**
     * Add this notification to a parent JFrame
     * @param frame The parent Phone frame
     */
    public void addToFrame(cephra.Frame.Phone frame) {
        this.setBounds(10, -110, 330, 170); // Initial position off-screen
        frame.getRootPane().getLayeredPane().add(this, JLayeredPane.POPUP_LAYER);
        frame.getRootPane().getLayeredPane().moveToFront(this);
        
        // Ensure iPhone frame stays on top of the notification
        frame.ensureIphoneFrameOnTop();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}
