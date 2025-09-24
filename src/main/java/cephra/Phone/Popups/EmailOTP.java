/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package cephra.Phone.Popups;

/**
 *
 * @author usher
 */
public class EmailOTP extends javax.swing.JPanel {

    /**
     * Creates new form EmailOTP
     */
    public EmailOTP() {
        initComponents();
    }

    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        toPassEmail = new javax.swing.JButton();
        otp = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setOpaque(false);
        setLayout(null);

        toPassEmail.setBorder(null);
        toPassEmail.setBorderPainted(false);
        toPassEmail.setContentAreaFilled(false);
        toPassEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toPassEmailActionPerformed(evt);
            }
        });
        add(toPassEmail);
        toPassEmail.setBounds(20, 50, 330, 100);

        otp.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        otp.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        otp.setText("878432");
        add(otp);
        otp.setBounds(210, 122, 90, 20);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/Notif.png"))); // NOI18N
        add(jLabel1);
        jLabel1.setBounds(25, 0, 330, 200);
    }// </editor-fold>//GEN-END:initComponents

    private void toPassEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toPassEmailActionPerformed
         javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frame.Phone) {
                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                        phoneFrame.switchPanel(new cephra.Phone.UserProfile.PasswordEmail());
                        break;
                    }
                }
            }
        });
    }//GEN-LAST:event_toPassEmailActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel otp;
    private javax.swing.JButton toPassEmail;
    // End of variables declaration//GEN-END:variables
    
    /**
     * Sets the OTP code in the label
     * @param otpCode the OTP code to display
     */
    public void setOTPCode(String otpCode) {
        if (otp != null && otpCode != null) {
            otp.setText(otpCode);
        }
    }
    
    /**
     * Shows the popup with animation
     */
    public void showPopup() {
        setVisible(true);
        
        // Slide down animation
        javax.swing.Timer animationTimer = new javax.swing.Timer(10, new java.awt.event.ActionListener() {
            private int currentY = -193; // Start position (above screen, moved 3 pixels higher)
            private final int targetY = -6; // Target position (6 pixels from top of screen)
            private final int step = 5; // Animation speed
            
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (currentY < targetY) {
                    currentY += step;
                    setLocation(0, currentY);
                } else {
                    // Animation complete
                    ((javax.swing.Timer) evt.getSource()).stop();
                    
                    // Auto-hide after 3 seconds
                    javax.swing.Timer hideTimer = new javax.swing.Timer(3000, new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent hideEvt) {
                            hidePopup();
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
     * Hides the popup with slide-up animation
     */
    public void hidePopup() {
        javax.swing.Timer hideTimer = new javax.swing.Timer(10, new java.awt.event.ActionListener() {
            private int currentY = getY();
            private final int targetY = -193; // Hide above screen (moved 3 pixels higher)
            private final int step = 5; // Animation speed
            
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (currentY > targetY) {
                    currentY -= step;
                    setLocation(0, currentY);
                } else {
                    // Animation complete, remove from parent
                    ((javax.swing.Timer) evt.getSource()).stop();
                    java.awt.Container parent = getParent();
                    if (parent != null) {
                        parent.remove(EmailOTP.this);
                        parent.repaint();
                    }
                }
            }
        });
        hideTimer.start();
    }
}
