package cephra.Phone.UserProfile;

import java.awt.*;
import javax.swing.*;
import javax.swing.text.*;

public class Phonelogin extends javax.swing.JPanel {
   private int loginAttempts = 0;
   private final int MAX_ATTEMPTS = 3;
   private Timer cooldownTimer;
   private int cooldownSeconds = 30;
   
   public Phonelogin() {
        initComponents();
        setPreferredSize(new java.awt.Dimension(370, 750));
        setSize(370, 750);
        username.setOpaque(false);
        username.setBackground(new Color(0, 0, 0, 0));
        pass.setOpaque(false);
        pass.setBackground(new Color(0, 0, 0, 0));
        See.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/EyeClose.png")));

        // --- INTEGRATED FILTERS ---
        // Removed username character limit - no longer restricting username length
        ((AbstractDocument) pass.getDocument()).setDocumentFilter(new InputLimitFilter(15, false));
        
        See.setBorderPainted(false);
        See.setOpaque(false);
        See.setContentAreaFilled(false);
        


        // Restore hover underline on Register and Forgot Password
        addUnderlineOnHover(reghere);
        addUnderlineOnHover(forgotpass);
    }
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        See = new javax.swing.JButton();
        reghere = new javax.swing.JButton();
        forgotpass = new javax.swing.JButton();
        pass = new javax.swing.JPasswordField();
        username = new javax.swing.JTextField();
        cooldownLabel = new javax.swing.JLabel();
        loginhome = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(370, 750));
        setPreferredSize(new java.awt.Dimension(370, 750));
        setLayout(null);

        See.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/EyeClose.png"))); // NOI18N
        See.setBorder(null);
        See.setFocusPainted(false);
        See.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SeeActionPerformed(evt);
            }
        });
        add(See);
        See.setBounds(290, 360, 50, 30);

        reghere.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        reghere.setForeground(new java.awt.Color(0, 204, 204));
        reghere.setText("Sign Up");
        reghere.setBorder(null);
        reghere.setBorderPainted(false);
        reghere.setContentAreaFilled(false);
        reghere.setFocusPainted(false);
        reghere.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        reghere.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reghereActionPerformed(evt);
            }
        });
        add(reghere);
        reghere.setBounds(240, 667, 80, 30);

        forgotpass.setForeground(new java.awt.Color(0, 204, 204));
        forgotpass.setText("Forgot password?");
        forgotpass.setBorder(null);
        forgotpass.setBorderPainted(false);
        forgotpass.setContentAreaFilled(false);
        forgotpass.setFocusPainted(false);
        forgotpass.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        forgotpass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                forgotpassActionPerformed(evt);
            }
        });
        add(forgotpass);
        forgotpass.setBounds(230, 390, 120, 30);

        pass.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        pass.setBorder(null);
        pass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passActionPerformed(evt);
            }
        });
        add(pass);
        pass.setBounds(90, 360, 210, 30);

        username.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        username.setBorder(null);
        username.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                usernameActionPerformed(evt);
            }
        });
        add(username);
        username.setBounds(90, 300, 230, 35);
        add(cooldownLabel);
        cooldownLabel.setBounds(60, 440, 160, 20);

        loginhome.setBorder(null);
        loginhome.setBorderPainted(false);
        loginhome.setContentAreaFilled(false);
        loginhome.setFocusPainted(false);
        loginhome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginhomeActionPerformed(evt);
            }
        });
        add(loginhome);
        loginhome.setBounds(60, 430, 290, 40);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/LOGIN.png"))); // NOI18N
        add(jLabel1);
        jLabel1.setBounds(0, 0, 370, 750);
    }// </editor-fold>//GEN-END:initComponents

    // CUSTOM CODE - DO NOT REMOVE - NetBeans will regenerate form code but this method should be preserved
    // Setup label position to prevent NetBeans from changing it
    private void setupLabelPosition() {
        if (jLabel1 != null) {
            jLabel1.setBounds(0, 0, 370, 750);
        }
    }

    private void loginhomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginhomeActionPerformed
        attemptLogin();
    }//GEN-LAST:event_loginhomeActionPerformed

     private void attemptLogin() {
        // Check if in cooldown period
        if (cooldownTimer != null && cooldownTimer.isRunning()) {
            JOptionPane.showMessageDialog(this, "Please wait for the cooldown period to end.", "Login Locked", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String usernameText = username.getText() != null ? username.getText().trim() : "";
        String password = new String(pass.getPassword());

        // Validate login using the CephraDB
        if (cephra.db.CephraDB.validateLogin(usernameText, password)) {
            // Login successful, reset attempts
            loginAttempts = 0;
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    java.awt.Window[] windows = java.awt.Window.getWindows();
                    for (java.awt.Window window : windows) {
                        if (window instanceof cephra.Frame.Phone) {
                            cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                            phoneFrame.switchPanel(new cephra.Phone.Dashboard.Home());
                            break;
                        }
                    }
                }
            });
        } else {
            // Login failed, increment attempts
            loginAttempts++;
            
            if (loginAttempts >= MAX_ATTEMPTS) {
                // Start cooldown timer
                startCooldownTimer();
                JOptionPane.showMessageDialog(this, "Too many failed login attempts. Please wait 30 seconds before trying again.", 
                    "Login Locked", JOptionPane.ERROR_MESSAGE);
            } else {
                // Show warning with remaining attempts
                int remainingAttempts = MAX_ATTEMPTS - loginAttempts;
                javax.swing.JOptionPane.showMessageDialog(this, 
                    "Invalid credentials. You have " + remainingAttempts + " attempts remaining.", 
                    "Login Failed", javax.swing.JOptionPane.WARNING_MESSAGE);
            }
            
            pass.setText(""); // Clear password field
            username.requestFocusInWindow(); // Refocus on username field
        }
    }
    
    private void startCooldownTimer() {
        // Disable login components
        loginhome.setEnabled(false);
        username.setEnabled(false);
        pass.setEnabled(false);
        
        // Reset cooldown seconds
        cooldownSeconds = 30;
        
        // Update and show cooldown label
        cooldownLabel.setText("Cooldown: " + cooldownSeconds + "s");
        cooldownLabel.setVisible(true);
        
        // Create and start the timer
        cooldownTimer = new Timer(1000, e -> {
            cooldownSeconds--;
            cooldownLabel.setText("Cooldown: " + cooldownSeconds + "s");
            
            if (cooldownSeconds <= 0) {
                // Time's up, stop the timer and reset
                ((Timer)e.getSource()).stop();
                loginAttempts = 0;
                cooldownLabel.setVisible(false);
                loginhome.setEnabled(true);
                username.setEnabled(true);
                pass.setEnabled(true);
            }
        });
        
        cooldownTimer.start();
    }

    private void usernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_usernameActionPerformed
        pass.requestFocus();
    }//GEN-LAST:event_usernameActionPerformed

    private void reghereActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reghereActionPerformed
       SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frame.Phone) {
                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                        phoneFrame.switchPanel(new cephra.Phone.UserProfile.Register());
                        break;
                    }
                }
            }
        });
    }//GEN-LAST:event_reghereActionPerformed

    private void forgotpassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_forgotpassActionPerformed
        SwingUtilities.invokeLater(new Runnable() {
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
    }//GEN-LAST:event_forgotpassActionPerformed

    private void passActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passActionPerformed
        // When Enter key is pressed on password field, attempt login
        attemptLogin();
    }//GEN-LAST:event_passActionPerformed

    private void SeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SeeActionPerformed
        
        
        
        
        
         
        
           if(pass.getEchoChar() == 0) {
        pass.setEchoChar('•');
        See.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/EyeClose.png")));
    } else {
        See.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/EyeOpen.png")));
        pass.setEchoChar((char) 0);
    }
        
        
        
        
        
        
        
        
        
    }//GEN-LAST:event_SeeActionPerformed

    @Override
    public void addNotify() {
        super.addNotify();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                setupLabelPosition(); // Set label position
                if (username != null) {
                    username.requestFocusInWindow();
                }
                javax.swing.JRootPane root = javax.swing.SwingUtilities.getRootPane(Phonelogin.this);
                if (root != null && loginhome != null) {
                    root.setDefaultButton(loginhome);
                }
            }
        });
    }

    public void focusUserField() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (username != null) {
                    username.requestFocusInWindow();
                }
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton See;
    private javax.swing.JLabel cooldownLabel;
    private javax.swing.JButton forgotpass;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton loginhome;
    private javax.swing.JPasswordField pass;
    private javax.swing.JButton reghere;
    private javax.swing.JTextField username;
    // End of variables declaration//GEN-END:variables
    
  

    // Add underline on hover for link-like buttons
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

    // Add this inner class at the end of your Phonelogin class (before the last closing brace)
    private static class InputLimitFilter extends DocumentFilter {
        private final int max;
        private final boolean username;

        public InputLimitFilter(int max, boolean username) {
            this.max = max;
            this.username = username;
        }

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (string == null) return;
            if (username && !string.matches("[A-Za-z0-9_]*")) return;
            if ((fb.getDocument().getLength() + string.length()) <= max) {
                super.insertString(fb, offset, string, attr);
            } else {
                java.awt.Toolkit.getDefaultToolkit().beep(); // Beep on max
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text == null) return;
            if (username && !text.matches("[A-Za-z0-9_]*")) return;
            if ((fb.getDocument().getLength() - length + text.length()) <= max) {
                super.replace(fb, offset, length, text, attrs);
            } else {
                java.awt.Toolkit.getDefaultToolkit().beep(); // Beep on max
            }
        }
    }
}
