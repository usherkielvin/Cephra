package cephra.Phone.Popups;
import java.awt.event.*;
import javax.swing.*;
public class Notification extends javax.swing.JPanel {
    private Timer animationTimer;
    private Timer hideTimer;
    private int yPosition = -70;
    private int targetY = 50;
    private boolean isShowing = false;
    private static final int ANIMATION_SPEED = 5;
    private static final int ANIMATION_DELAY = 10;
    private static final int DISPLAY_DURATION = 3000;
    public static final String TYPE_WAITING = "WAITING";
    public static final String TYPE_PENDING = "PENDING";
    public static final String TYPE_MY_TURN = "MY_TURN";
    public static final String TYPE_DONE = "DONE";
    public static final String TYPE_OTP = "OTP"; 
    private String currentNotificationType = TYPE_WAITING;
    private String ticketId = "";
    private String bayNumber = "";  
    public Notification() {
        initComponents();
        setOpaque(false);
        setVisible(false);
    }  
    public void setNotificationType(String type, String ticketId, String bayNumber) {
        this.currentNotificationType = type;
        this.ticketId = ticketId != null ? ticketId : "";
        this.bayNumber = bayNumber != null ? bayNumber : "";
        
        updateNotificationDisplay();
    }
    
    private void updateNotificationDisplay() {
        switch (currentNotificationType) {
            case TYPE_WAITING:
                statusLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/NotifWaitingBay.png")));
                statusLabel.setText("Your ticket \"" + ticketId + "\" is now waiting");
                statusLabel.setForeground(new java.awt.Color(0, 102, 102));
                break;
                
            case TYPE_PENDING:
                statusLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/NotifPendingBay.png")));
                statusLabel.setText("Your ticket \"" + ticketId + "\" is now pending");
                statusLabel.setForeground(new java.awt.Color(255, 0, 0));
                break;
                
            case TYPE_MY_TURN:
                statusLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/NotifProceedBay.png")));
                statusLabel.setText("Please go to your bay \"" + bayNumber + "\" now");
                statusLabel.setForeground(new java.awt.Color(0, 150, 0));
                break;
                
            case TYPE_DONE:
                statusLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/Done.png")));
                statusLabel.setText("Your ticket \"" + ticketId + "\" is now fullcharge");
                statusLabel.setForeground(new java.awt.Color(255, 255, 102));
                break;
                
            case TYPE_OTP:
                statusLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/notif.png")));
                statusLabel.setText("Your verification code: " + ticketId);
                statusLabel.setForeground(new java.awt.Color(0, 102, 102));
                break;
                
            default:
                statusLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/NotifWaitingBay.png")));
                statusLabel.setText("Notification");
                statusLabel.setForeground(new java.awt.Color(0, 102, 102));
                break;
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        statusLabel = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setMaximumSize(new java.awt.Dimension(370, 750));
        setPreferredSize(new java.awt.Dimension(370, 750));
        setLayout(null);

        jButton1.setBorderPainted(false);
        jButton1.setContentAreaFilled(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        add(jButton1);
        jButton1.setBounds(0, 10, 330, 60);

        statusLabel.setForeground(new java.awt.Color(0, 102, 102));
        statusLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/NotifWaitingBay.png"))); // NOI18N
        add(statusLabel);
        statusLabel.setBounds(0, 0, 330, 70);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frame.Phone) {
                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                        phoneFrame.switchPanel(new cephra.Phone.Dashboard.NotificationHistory());
                        break;
                    }
                }
            }
        });
    }//GEN-LAST:event_jButton1ActionPerformed


    public void forceShowNotification() {
        isShowing = false;
        showNotification();
    }

    public void updateAndShowNotification(String type, String ticketId, String bayNumber) {
        setNotificationType(type, ticketId, bayNumber);
        
        if (isShowing) {
            yPosition = -70;
            setLocation(23, yPosition);
            
            if (animationTimer != null && animationTimer.isRunning()) {
                animationTimer.stop();
            }
            if (hideTimer != null && hideTimer.isRunning()) {
                hideTimer.stop();
            }
            
            showNotificationAnimation();
        } else {
            showNotification();
        }
    } 

    private void showNotificationAnimation() {
        animationTimer = new Timer(ANIMATION_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (yPosition < targetY) {
                    yPosition += ANIMATION_SPEED;
                    setLocation(23, yPosition);
                    
                    java.awt.Window[] windows = java.awt.Window.getWindows();
                    for (java.awt.Window window : windows) {
                        if (window instanceof cephra.Frame.Phone) {
                            ((cephra.Frame.Phone) window).ensureIphoneFrameOnTop();
                            break;
                        }
                    }
                } else {
                    animationTimer.stop();
                    
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
    
    public void showNotification() {
       
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }
        if (hideTimer != null && hideTimer.isRunning()) {
            hideTimer.stop();
        }
        
        isShowing = true;
        setVisible(true);
        yPosition = -70;
        setLocation(23, yPosition);
        
        showNotificationAnimation();
    }
    
    public void hideNotification() {
        if (!isShowing) return;
        
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }  
        animationTimer = new Timer(ANIMATION_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (yPosition > -70) {
                    yPosition -= ANIMATION_SPEED;
                    setLocation(23, yPosition);
                } else {
                    
                    animationTimer.stop();
                    setVisible(false);
                    isShowing = false;
                }
            }
        });
        animationTimer.start();
    }
 
    public void addToFrame(cephra.Frame.Phone frame) {
        this.setBounds(23, -50, 330, 70); // Initial position off-screen, centered horizontally
        frame.getRootPane().getLayeredPane().add(this, JLayeredPane.POPUP_LAYER);
        frame.getRootPane().getLayeredPane().moveToFront(this);       
        frame.ensureIphoneFrameOnTop();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel statusLabel;
    // End of variables declaration//GEN-END:variables
}
