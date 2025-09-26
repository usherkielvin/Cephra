package cephra.Phone.UserProfile;

import javax.swing.SwingUtilities;

public class PasswordForgot extends javax.swing.JPanel {

  
    public PasswordForgot() {
        initComponents();
        setPreferredSize(new java.awt.Dimension(370, 750));
        setSize(370, 750);
        email.setOpaque(false);
        email.setBackground(new java.awt.Color(0, 0, 0, 0));
        setupLabelPosition();
        
        // Add email prefix functionality
        setupEmailPrefix();
        
        // Add email domain auto-completion
        setupEmailDomainCompletion();

        addUnderlineOnHover(Back_Button);
        addUnderlineOnHover(Contact_Support_Button);
    }

  
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Contact_Support_Button = new javax.swing.JButton();
        Back_Button = new javax.swing.JButton();
        resetsend = new javax.swing.JButton();
        email = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();

        setLayout(null);

        Contact_Support_Button.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Contact_Support_Button.setForeground(new java.awt.Color(0, 204, 204));
        Contact_Support_Button.setText("Contact Support");
        Contact_Support_Button.setBorder(null);
        Contact_Support_Button.setBorderPainted(false);
        Contact_Support_Button.setContentAreaFilled(false);
        Contact_Support_Button.setFocusPainted(false);
        Contact_Support_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Contact_Support_ButtonActionPerformed(evt);
            }
        });
        add(Contact_Support_Button);
        Contact_Support_Button.setBounds(185, 663, 130, 30);

        Back_Button.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        Back_Button.setBorder(null);
        Back_Button.setBorderPainted(false);
        Back_Button.setContentAreaFilled(false);
        Back_Button.setFocusPainted(false);
        Back_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Back_ButtonActionPerformed(evt);
            }
        });
        add(Back_Button);
        Back_Button.setBounds(110, 460, 170, 23);

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
        resetsend.setBounds(60, 410, 290, 40);

        email.setBorder(null);
        email.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailActionPerformed(evt);
            }
        });
        add(email);
        email.setBounds(80, 346, 260, 35);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/PasswordForgot.png"))); // NOI18N
        add(jLabel1);
        jLabel1.setBounds(20, 0, 360, 750);
    }// </editor-fold>//GEN-END:initComponents

    // CUSTOM CODE - DO NOT REMOVE - NetBeans will regenerate form code but this method should be preserved
    // Setup label position to prevent NetBeans from changing it
    private void setupLabelPosition() {
        if (jLabel1 != null) {
            jLabel1.setBounds(2, 0, 398, 750);
        }
    }
    
    // Setup email placeholder text only
    private void setupEmailPrefix() {
        // Set placeholder text
        email.setText("Enter your email");
        email.setForeground(new java.awt.Color(128, 128, 128)); // Gray color for placeholder
        
        // Handle focus events for placeholder only
        email.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (email.getText().equals("Enter your email")) {
                    email.setText("");
                    email.setForeground(new java.awt.Color(0, 0, 0)); // Black color for actual text
                }
            }
            
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                String currentText = email.getText();
                
                if (currentText.isEmpty()) {
                    email.setText("Enter your email");
                    email.setForeground(new java.awt.Color(128, 128, 128)); // Gray color for placeholder
                }
            }
        });
        
        email.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                String currentText = email.getText();
                
                // If placeholder text is still there, clear it
                if (currentText.equals("Enter your email")) {
                    email.setText("");
                    email.setForeground(new java.awt.Color(0, 0, 0)); // Black color for actual text
                }
            }
        });
    }
    
    // Setup email domain auto-completion
    private void setupEmailDomainCompletion() {
        email.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                String text = email.getText();
                int caretPos = email.getCaretPosition();
                
                // Check if user typed @g and suggest gmail.com
                if (text.endsWith("@g") && caretPos == text.length()) {
                    email.setText(text + "mail.com");
                    email.setCaretPosition(email.getText().length()); // Position cursor at end
                }
                // Check if user typed @c and suggest cephra.com
                else if (text.endsWith("@c") && caretPos == text.length()) {
                    email.setText(text + "ephra.com");
                    email.setCaretPosition(email.getText().length()); // Position cursor at end
                }
            }
        });
    }

    private void emailActionPerformed(java.awt.event.ActionEvent evt) {
    resetsendActionPerformed(evt);
    }

    private void resetsendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetsendActionPerformed
        String emailText = email.getText().trim();
        
        // Validate empty email field
        if (emailText.isEmpty() || emailText.equals("Enter your email")) {
            javax.swing.JOptionPane.showMessageDialog(this, "Please enter your email address.", "Empty Field", javax.swing.JOptionPane.WARNING_MESSAGE);
            email.requestFocusInWindow();
            return;
        }
        
        // Validate email format
        if (!emailText.contains("@") || !emailText.contains(".")) {
            javax.swing.JOptionPane.showMessageDialog(this, "Please enter a valid email address.", "Invalid Email", javax.swing.JOptionPane.WARNING_MESSAGE);
            email.requestFocusInWindow();
            return;
        }
        
if (cephra.Database.CephraDB.findUserByEmail(emailText) != null) {
    cephra.Phone.Utilities.AppSessionState.userEmailForReset = emailText; // Add this line
    cephra.Phone.Utilities.AppSessionState.showOtpNotification = true; // Show notification when coming from PasswordForgot
    cephra.Database.CephraDB.generateAndStoreOTP();
        
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frame.Phone) {
                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                        phoneFrame.switchPanel(new cephra.Phone.UserProfile.PasswordVerify());
                        break;
                    }
                }
            }
        });
    } else {
        javax.swing.JOptionPane.showMessageDialog(this, "Email address not found.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        email.setText("");
        email.requestFocusInWindow();
    }
    }//GEN-LAST:event_resetsendActionPerformed

    private void Back_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Back_ButtonActionPerformed
      
        
         SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    java.awt.Window[] windows = java.awt.Window.getWindows();
                    for (java.awt.Window window : windows) {
                        if (window instanceof cephra.Frame.Phone) {
                            cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                            phoneFrame.switchPanel(new cephra.Phone.UserProfile.User_Login());
                            break;
                        }
                    }
                }
         });
        
        
        
        
        
        
        
    }//GEN-LAST:event_Back_ButtonActionPerformed

    private void Contact_Support_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Contact_Support_ButtonActionPerformed
        
            javax.swing.JOptionPane.showMessageDialog(this, "Contact support at: support@cephra.com", "Contact Support", javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_Contact_Support_ButtonActionPerformed

    @Override
    public void addNotify() {
        super.addNotify();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                setupLabelPosition(); // Set label position
                // Removed auto-focus on email field
                javax.swing.JRootPane root = javax.swing.SwingUtilities.getRootPane(PasswordForgot.this);
                if (root != null && resetsend != null) {
                    root.setDefaultButton(resetsend);
                }
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


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Back_Button;
    private javax.swing.JButton Contact_Support_Button;
    private javax.swing.JTextField email;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton resetsend;
    // End of variables declaration//GEN-END:variables

    private void addUnderlineOnHover(final javax.swing.JButton button) {
        if (button == null) return;
        final String baseText = button.getText();
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setText("<html><u>" + baseText + "</u></html>");
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setText(baseText);
            }
        });
    }
}
