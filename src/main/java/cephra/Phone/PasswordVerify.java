
package cephra.Phone;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.SwingUtilities;

public class PasswordVerify extends javax.swing.JPanel {

    // Flag to prevent setupButtons from running multiple times
    private boolean buttonsSetup = false;
    // Flag to prevent multiple verification attempts
    private boolean verificationInProgress = false;
   
    public PasswordVerify() {
        initComponents();
        setPreferredSize(new java.awt.Dimension(350, 750));
        setSize(350, 750);
        email.setOpaque(false);
        email.setBackground(new java.awt.Color(0, 0, 0, 0));
        setupLabelPosition();
        setupButtons(); 
        makeDraggable();       
        otpPreviewLabel.setText(cephra.CephraDB.getGeneratedOTP());
    }

   
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        email = new javax.swing.JTextField();
        resend = new javax.swing.JButton();
        Back = new javax.swing.JButton();
        resetsend = new javax.swing.JButton();
        contactsup = new javax.swing.JButton();
        otpPreviewLabel = new javax.swing.JLabel();
        cephraemail = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setLayout(null);

        email.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        email.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        email.setBorder(null);
        // Action listener removed to prevent duplicate Enter key handling
        // Enter key is now handled by KeyListener in setupButtons()
        add(email);
        email.setBounds(54, 336, 250, 25);

        resend.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        resend.setForeground(new java.awt.Color(0, 204, 204));
        resend.setText("Resend");
        resend.setBorder(null);
        resend.setBorderPainted(false);
        resend.setContentAreaFilled(false);
        resend.setFocusPainted(false);
        resend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resendActionPerformed(evt);
            }
        });
        add(resend);
        resend.setBounds(170, 370, 150, 30);

        Back.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Back.setForeground(new java.awt.Color(0, 204, 204));
        Back.setText("Back");
        Back.setBorder(null);
        Back.setBorderPainted(false);
        Back.setContentAreaFilled(false);
        Back.setFocusPainted(false);
        Back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BackActionPerformed(evt);
            }
        });
        add(Back);
        Back.setBounds(120, 630, 120, 23);

        resetsend.setBorder(null);
        resetsend.setBorderPainted(false);
        resetsend.setContentAreaFilled(false);
        resetsend.setFocusPainted(false);
        resetsend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetsendActionPerformed(evt);
            }
        });
        add(resetsend);
        resetsend.setBounds(50, 440, 270, 50);

        contactsup.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        contactsup.setForeground(new java.awt.Color(0, 204, 204));
        contactsup.setText("Contact Support");
        contactsup.setBorder(null);
        contactsup.setBorderPainted(false);
        contactsup.setContentAreaFilled(false);
        contactsup.setFocusPainted(false);
        contactsup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                contactsupActionPerformed(evt);
            }
        });
        add(contactsup);
        contactsup.setBounds(160, 663, 130, 30);

        otpPreviewLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        otpPreviewLabel.setText("99999");
        add(otpPreviewLabel);
        otpPreviewLabel.setBounds(210, 131, 140, 20);

        cephraemail.setBorder(null);
        cephraemail.setBorderPainted(false);
        cephraemail.setContentAreaFilled(false);
        cephraemail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cephraemailActionPerformed(evt);
            }
        });
        add(cephraemail);
        cephraemail.setBounds(20, 50, 330, 110);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Photos/forgotpass-pop.png"))); // NOI18N
        add(jLabel1);
        jLabel1.setBounds(0, 0, 370, 750);
    }// </editor-fold>//GEN-END:initComponents
 // CUSTOM CODE - DO NOT REMOVE - NetBeans will regenerate form code but this method should be preserved
    // Setup label position to prevent NetBeans from changing it
    private void setupLabelPosition() {
        if (jLabel1 != null) {
            jLabel1.setBounds(-15, 0, 398, 750);
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
                    java.awt.Window window = SwingUtilities.getWindowAncestor(PasswordVerify.this);
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
    // emailActionPerformed method removed - no longer needed
    // Enter key is now handled by KeyListener in setupButtons()

    private void cephraemailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cephraemailActionPerformed
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frame.Phone) {
                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                        phoneFrame.switchPanel(new cephra.Phone.PasswordEmail());
                        break;
                    }
                }
            }
        });
    }//GEN-LAST:event_cephraemailActionPerformed

    private void resetsendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetsendActionPerformed
        // Prevent multiple verification attempts
        if (verificationInProgress) {
            return;
        }
        verificationInProgress = true;
        
        // Handle verification code submission
        String enteredOTP = email.getText();
        String correctOTP = cephra.CephraDB.getGeneratedOTP();

        if (enteredOTP.equals(correctOTP)) {
            javax.swing.JOptionPane.showMessageDialog(this, "Verification code submitted successfully!", "Success", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            
            // Navigate to PasswordNew panel after OK is clicked
            javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    java.awt.Window[] windows = java.awt.Window.getWindows();
                    for (java.awt.Window window : windows) {
                        if (window instanceof cephra.Frame.Phone) {
                            cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                            phoneFrame.switchPanel(new cephra.Phone.PasswordNew());
                            break;
                        }
                    }
                }
            });
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "Invalid verification code. Please try again.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            email.setText(""); // Clear the text field
            email.requestFocusInWindow(); // Auto focus back on email field
        }
        
        // Reset verification flag after a short delay to ensure dialog is fully closed
        javax.swing.Timer resetTimer = new javax.swing.Timer(100, new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                verificationInProgress = false;
            }
        });
        resetTimer.setRepeats(false);
        resetTimer.start();
    }//GEN-LAST:event_resetsendActionPerformed

    private void BackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BackActionPerformed
         // Navigate to PasswordNew panel after OK is clicked
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frame.Phone) {
                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                        phoneFrame.switchPanel(new cephra.Phone.PasswordForgot());
                        break;
                    }
                }
            }
        });
    }//GEN-LAST:event_BackActionPerformed

    private void contactsupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_contactsupActionPerformed
         javax.swing.JOptionPane.showMessageDialog(this, "Contact support at: support@cephra.com", "Contact Support", javax.swing.JOptionPane.INFORMATION_MESSAGE);
    
    }//GEN-LAST:event_contactsupActionPerformed

        private void resendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resendActionPerformed
        // Generate a new OTP
        cephra.CephraDB.generateAndStoreOTP();
        
        // Update the OTP label with the new OTP
        otpPreviewLabel.setText(cephra.CephraDB.getGeneratedOTP());
        
        // Clear the email field and show success message
        email.setText("");
        email.requestFocusInWindow();
        
        javax.swing.JOptionPane.showMessageDialog(this, 
            "New verification code has been generated and sent!", 
            "Resend Successful", 
            javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_resendActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Back;
    private javax.swing.JButton cephraemail;
    private javax.swing.JButton contactsup;
    private javax.swing.JTextField email;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel otpPreviewLabel;
    private javax.swing.JButton resend;
    private javax.swing.JButton resetsend;
    // End of variables declaration//GEN-END:variables
    
    // Add button functionality after initComponents to prevent NetBeans from removing it
    private void setupButtons() {
        // Prevent running multiple times
        if (buttonsSetup) {
            return;
        }
        buttonsSetup = true;
        
        // Setup Resend button (only hover effects, action listener already set in initComponents)
        resend.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        resend.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                resend.setForeground(new java.awt.Color(0, 255, 255));
                resend.setText("<html><u>Resend</u></html>");
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                resend.setForeground(new java.awt.Color(0, 204, 204));
                resend.setText("Resend");
            }
        });

        // Setup Contact Support button (only hover effects, action listener already set in initComponents)
        contactsup.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        contactsup.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                contactsup.setForeground(new java.awt.Color(0, 255, 255));
                contactsup.setText("<html><u>Contact Support</u></html>");
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                contactsup.setForeground(new java.awt.Color(0, 204, 204));
                contactsup.setText("Contact Support");
            }
        });

        // Setup Back button
        Back.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Back.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                Back.setForeground(new java.awt.Color(0, 255, 255));
                Back.setText("<html><u>Back</u></html>");
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                Back.setForeground(new java.awt.Color(0, 204, 204));
                Back.setText("Back");
            }
        });

        // Setup email field center alignment
        email.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        
        // Add Enter key listener to email field - NetBeans cannot override this
        // Remove any existing KeyListeners first to prevent duplicates
        for (java.awt.event.KeyListener kl : email.getKeyListeners()) {
            email.removeKeyListener(kl);
        }
        
        email.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                    // First check if verification is already in progress
                    if (verificationInProgress) {
                        return; // Don't process Enter key if verification is ongoing
                    }
                    
                    // Check if any dialog is currently showing - if so, don't process Enter key
                    // This allows the dialog's OK button to receive the Enter key focus
                    try {
                        java.awt.Window[] windows = java.awt.Window.getWindows();
                        for (java.awt.Window window : windows) {
                            if (window instanceof javax.swing.JDialog && window.isVisible()) {
                                return; // Dialog is showing, let it handle Enter key
                            }
                        }
                        // Also check for JOptionPane dialogs
                        if (javax.swing.SwingUtilities.getWindowAncestor(PasswordVerify.this) != null) {
                            java.awt.Window parentWindow = javax.swing.SwingUtilities.getWindowAncestor(PasswordVerify.this);
                            if (parentWindow.getOwnedWindows().length > 0) {
                                for (java.awt.Window ownedWindow : parentWindow.getOwnedWindows()) {
                                    if (ownedWindow instanceof javax.swing.JDialog && ownedWindow.isVisible()) {
                                        return; // Owned dialog is showing
                                    }
                                }
                            }
                        }
                        // No dialog showing and no verification in progress, process Enter key normally
                        resetsend.doClick();
                    } catch (Exception e) {
                        // If any error occurs, don't process the Enter key
                        return;
                    }
                }
            }
        });
    }

    @Override
    public void addNotify() {
        super.addNotify();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                setupLabelPosition(); // Set label position
                setupButtons(); // Setup buttons when panel is added to container
                if (email != null) {
                    email.requestFocusInWindow();
                }
                javax.swing.JRootPane root = javax.swing.SwingUtilities.getRootPane(PasswordVerify.this);
                if (root != null && resetsend != null) {
                    root.setDefaultButton(resetsend);
                }
                
                // Enter key functionality is already set up in setupButtons() method
            }
        });
    }

    public void focusEmailField() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (email != null) {
                    email.requestFocusInWindow();
                }
            }
        });
    }
    
    // Custom variables
   
}
