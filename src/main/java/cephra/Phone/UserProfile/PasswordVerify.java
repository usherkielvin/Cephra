package cephra.Phone.UserProfile;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.SwingUtilities;
import cephra.Phone.Popups.UnifiedNotification;

public class PasswordVerify extends javax.swing.JPanel {

    // Flag to prevent setupButtons from running multiple times
    private boolean buttonsSetup = false;
    // Flag to prevent multiple verification attempts
    private boolean verificationInProgress = false;
    // Notification popup instance
    private UnifiedNotification notificationPop;
   
    public PasswordVerify() {
        initComponents();
        setPreferredSize(new java.awt.Dimension(370, 750));
        setSize(370, 750);
        setupLabelPosition();
        setupButtons(); 
        makeDraggable();       
     //   otpPreviewLabel.setText(cephra.CephraDB.getGeneratedOTP());
        code1.setOpaque(false);
        code1.setBackground(new Color(0, 0, 0, 0));
        code2.setOpaque(false);
        code2.setBackground(new Color(0, 0, 0, 0));
        code3.setOpaque(false);
        code3.setBackground(new Color(0, 0, 0, 0));
        code4.setOpaque(false);
        code4.setBackground(new Color(0, 0, 0, 0));
        code5.setOpaque(false);
        code5.setBackground(new Color(0, 0, 0, 0));
        code6.setOpaque(false);
        code6.setBackground(new Color(0, 0, 0, 0));
        
        // Setup digit-only input for all code fields
        setupDigitOnlyInput();
    }

   
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cephramail = new javax.swing.JLabel();
        code6 = new javax.swing.JTextField();
        code5 = new javax.swing.JTextField();
        code4 = new javax.swing.JTextField();
        code3 = new javax.swing.JTextField();
        code2 = new javax.swing.JTextField();
        code1 = new javax.swing.JTextField();
        resend = new javax.swing.JButton();
        Back = new javax.swing.JButton();
        resetsend = new javax.swing.JButton();
        contactsup = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setLayout(null);

        cephramail.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        cephramail.setText("dizon@cephra.com");
        add(cephramail);
        cephramail.setBounds(200, 251, 160, 20);

        code6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        code6.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        code6.setText("1");
        code6.setBorder(null);
        code6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                code6ActionPerformed(evt);
            }
        });
        add(code6);
        code6.setBounds(300, 310, 30, 50);

        code5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        code5.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        code5.setText("1");
        code5.setBorder(null);
        code5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                code5ActionPerformed(evt);
            }
        });
        add(code5);
        code5.setBounds(248, 310, 30, 50);

        code4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        code4.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        code4.setText("1");
        code4.setBorder(null);
        code4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                code4ActionPerformed(evt);
            }
        });
        add(code4);
        code4.setBounds(197, 310, 30, 50);

        code3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        code3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        code3.setText("1");
        code3.setBorder(null);
        code3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                code3ActionPerformed(evt);
            }
        });
        add(code3);
        code3.setBounds(145, 310, 30, 50);

        code2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        code2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        code2.setText("1");
        code2.setBorder(null);
        code2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                code2ActionPerformed(evt);
            }
        });
        add(code2);
        code2.setBounds(93, 310, 30, 50);

        code1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        code1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        code1.setText("1");
        code1.setBorder(null);
        code1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                code1ActionPerformed(evt);
            }
        });
        add(code1);
        code1.setBounds(40, 310, 30, 50);

        resend.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        resend.setForeground(new java.awt.Color(0, 204, 204));
        resend.setText("Resend email");
        resend.setBorder(null);
        resend.setBorderPainted(false);
        resend.setContentAreaFilled(false);
        resend.setFocusPainted(false);
        resend.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        resend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resendActionPerformed(evt);
            }
        });
        add(resend);
        resend.setBounds(230, 430, 90, 40);

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
        resetsend.setBounds(40, 380, 300, 40);

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

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/checkmail.png"))); // NOI18N
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
    
    // Setup digit-only input for verification code fields
    private void setupDigitOnlyInput() {
        javax.swing.JTextField[] codeFields = {code1, code2, code3, code4, code5, code6};
        
        for (javax.swing.JTextField field : codeFields) {
            // Limit to 1 character
            field.setDocument(new javax.swing.text.PlainDocument() {
                @Override
                public void insertString(int offs, String str, javax.swing.text.AttributeSet a) 
                        throws javax.swing.text.BadLocationException {
                    // Only allow digits and limit to 1 character
                    if (str != null && str.matches("\\d") && getLength() < 1) {
                        super.insertString(offs, str, a);
                    }
                }
            });
            
            // Add key listener for auto-focus to next field
            field.addKeyListener(new java.awt.event.KeyAdapter() {
                @Override
                public void keyTyped(java.awt.event.KeyEvent e) {
                    if (Character.isDigit(e.getKeyChar()) && field.getText().length() == 0) {
                        // Auto-focus to next field after typing a digit
                        javax.swing.SwingUtilities.invokeLater(() -> {
                            int currentIndex = -1;
                            for (int i = 0; i < codeFields.length; i++) {
                                if (codeFields[i] == field) {
                                    currentIndex = i;
                                    break;
                                }
                            }
                            if (currentIndex >= 0 && currentIndex < codeFields.length - 1) {
                                codeFields[currentIndex + 1].requestFocus();
                            }
                        });
                    }
                }
                
                @Override
                public void keyPressed(java.awt.event.KeyEvent e) {
                    // Handle backspace to go to previous field
                    if (e.getKeyCode() == java.awt.event.KeyEvent.VK_BACK_SPACE) {
                        if (field.getText().isEmpty()) {
                            int currentIndex = -1;
                            for (int i = 0; i < codeFields.length; i++) {
                                if (codeFields[i] == field) {
                                    currentIndex = i;
                                    break;
                                }
                            }
                            if (currentIndex > 0) {
                                codeFields[currentIndex - 1].requestFocus();
                            }
                        }
                    }
                }
            });
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

    private void resetsendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetsendActionPerformed
        // Prevent multiple verification attempts
        if (verificationInProgress) {
            return;
        }
        verificationInProgress = true;
        
        // Handle verification code submission
        String enteredOTP = code1.getText() + code2.getText() + code3.getText() + 
                           code4.getText() + code5.getText() + code6.getText();
        String correctOTP = cephra.Database.CephraDB.getGeneratedOTP();

        if (enteredOTP.equals(correctOTP)) {
            javax.swing.JOptionPane.showMessageDialog(this, "Verification code submitted successfully!", "Success", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            
            // Navigate to PasswordNew panel after OK is clicked
            javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    java.awt.Window[] windows = java.awt.Window.getWindows();
                    for (java.awt.Window window : windows) {
                        if (window instanceof cephra.Frame.Phone) {
                            cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                            phoneFrame.switchPanel(new cephra.Phone.UserProfile.PasswordNew());
                            break;
                        }
                    }
                }
            });
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "Invalid verification code. Please try again.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            // Clear all code fields
            code1.setText("");
            code2.setText("");
            code3.setText("");
            code4.setText("");
            code5.setText("");
            code6.setText("");
            code1.requestFocusInWindow();
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
                        phoneFrame.switchPanel(new cephra.Phone.UserProfile.PasswordForgot());
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
        cephra.Database.CephraDB.generateAndStoreOTP();
        
        // Update the OTP label with the new OTP
    //    otpPreviewLabel.setText(cephra.CephraDB.getGeneratedOTP());
        
        // Clear all code fields
        code1.setText("");
        code2.setText("");
        code3.setText("");
        code4.setText("");
        code5.setText("");
        code6.setText("");
        code1.requestFocusInWindow();
        
        // Set flag to show notification for resend
        cephra.Phone.Utilities.AppSessionState.showOtpNotification = true;
        
        // Show new notification with updated OTP instead of dialog
        showOTPNotification(cephra.Database.CephraDB.getGeneratedOTP());
    }//GEN-LAST:event_resendActionPerformed

    private void code1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_code1ActionPerformed
        code2.requestFocus();
    }//GEN-LAST:event_code1ActionPerformed

    private void code2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_code2ActionPerformed
       code3.requestFocus();
    }//GEN-LAST:event_code2ActionPerformed

    private void code3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_code3ActionPerformed
       code4.requestFocus();
    }//GEN-LAST:event_code3ActionPerformed

    private void code4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_code4ActionPerformed
     code5.requestFocus();
    }//GEN-LAST:event_code4ActionPerformed

    private void code5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_code5ActionPerformed
       code6.requestFocus();
    }//GEN-LAST:event_code5ActionPerformed

    private void code6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_code6ActionPerformed
        
    }//GEN-LAST:event_code6ActionPerformed



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Back;
    private javax.swing.JLabel cephramail;
    private javax.swing.JTextField code1;
    private javax.swing.JTextField code2;
    private javax.swing.JTextField code3;
    private javax.swing.JTextField code4;
    private javax.swing.JTextField code5;
    private javax.swing.JTextField code6;
    private javax.swing.JButton contactsup;
    private javax.swing.JLabel jLabel1;
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
                resend.setText("<html><u>Resend email</u></html>");
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                resend.setForeground(new java.awt.Color(0, 204, 204));
                resend.setText("Resend email");
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

        // Email field setup - no center alignment
        
        // Add Enter key listener - will be updated when you add new text fields
        // Remove any existing KeyListeners first to prevent duplicates
        
        // Key listener will be added when you create new text fields
    }

    @Override
    public void addNotify() {
        super.addNotify();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                setupLabelPosition(); // Set label position
                setupButtons(); // Setup buttons when panel is added to container
                
                // Set the user's email dynamically
                if (cephra.Phone.Utilities.AppSessionState.userEmailForReset != null) {
                    cephramail.setText(cephra.Phone.Utilities.AppSessionState.userEmailForReset);
                }
                
                if (code1 != null) {
                    code1.requestFocusInWindow();
                }
                javax.swing.JRootPane root = javax.swing.SwingUtilities.getRootPane(PasswordVerify.this);
                if (root != null && resetsend != null) {
                    root.setDefaultButton(resetsend);
                }
                
                // Show notification with OTP only if the flag is set (coming from PasswordForgot or resending)
                if (cephra.Phone.Utilities.AppSessionState.showOtpNotification) {
                    showOTPNotification(cephra.Database.CephraDB.getGeneratedOTP());
                    // Reset the flag after showing the notification
                    cephra.Phone.Utilities.AppSessionState.showOtpNotification = false;
                }
                
                // Enter key functionality is already set up in setupButtons() method
            }
        });
    }

    public void focusEmailField() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Focus will be set when you add new text fields
            }
        });
    }
    
    // Custom variables
    
    /**
     * Shows OTP notification at the top of the phone screen
     * @param otp The OTP to display
     */
    private void showOTPNotification(String otp) {
        // Get the phone frame
        java.awt.Window[] windows = java.awt.Window.getWindows();
        for (java.awt.Window window : windows) {
            if (window instanceof cephra.Frame.Phone) {
                cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                
                // Hide existing notification if any
                if (notificationPop != null) {
                    notificationPop.hideNotification();
                }
                
                // Create new notification
                notificationPop = new UnifiedNotification();
                notificationPop.setNotificationType(UnifiedNotification.TYPE_OTP, otp, null);
                notificationPop.addToFrame(phoneFrame);
                notificationPop.showNotification();
                break;
            }
        }
    }
   
}
