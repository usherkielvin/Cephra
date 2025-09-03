package cephra.Admin;

import java.awt.*;
import java.awt.event.*;
import java.awt.font.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.text.*; // Add this import

public class Login extends javax.swing.JPanel {
    private int loginAttempts = 0;
    private final int MAX_ATTEMPTS = 3;
    private Timer cooldownTimer;
    private int cooldownSeconds = 30;

    public Login() {
        initComponents();
        setPreferredSize(new java.awt.Dimension(1000, 750));
        setSize(1000, 750);

        userfield.setOpaque(false);
        userfield.setBackground(new Color(0, 0, 0, 0));

        passfield.setOpaque(false);
        passfield.setBackground(new Color(0, 0, 0, 0));
        passfield.setBorder(null);

        passfield.setEchoChar('•');
        See.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Photos/EyeClose.png")));
        See.setBorderPainted(false);
        See.setOpaque(false);
        See.setContentAreaFilled(false);
        
        // Initialize cooldown label
        cooldownlabel.setForeground(Color.RED);
        cooldownlabel.setVisible(false);

        // --- INTEGRATED FILTERS ---
        ((AbstractDocument) userfield.getDocument()).setDocumentFilter(new InputLimitFilter(15, true));
        ((AbstractDocument) passfield.getDocument()).setDocumentFilter(new InputLimitFilter(15, false));

        // Hover underline and hand cursor for "Forgot Password?"
        if (jButton1 != null) {
            final Font baseFont = jButton1.getFont();
            jButton1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            jButton1.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    Map<TextAttribute, Object> attributes = new HashMap<>(baseFont.getAttributes());
                    attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
                    jButton1.setFont(baseFont.deriveFont(attributes));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    jButton1.setFont(baseFont);
                }
            });
        }
    }

    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        See = new javax.swing.JButton();
        userfield = new javax.swing.JTextField();
        loginbutton = new javax.swing.JButton();
        passfield = new javax.swing.JPasswordField();
        cooldownlabel = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(1000, 750));
        setPreferredSize(new java.awt.Dimension(1000, 750));
        setLayout(null);

        See.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Photos/EyeClose.png"))); // NOI18N
        See.setBorderPainted(false);
        See.setFocusPainted(false);
        See.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SeeActionPerformed(evt);
            }
        });
        add(See);
        See.setBounds(855, 430, 27, 50);

        userfield.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        userfield.setBorder(null);
        userfield.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userfieldActionPerformed(evt);
            }
        });
        add(userfield);
        userfield.setBounds(540, 318, 340, 45);

        loginbutton.setBorder(null);
        loginbutton.setBorderPainted(false);
        loginbutton.setContentAreaFilled(false);
        loginbutton.setFocusPainted(false);
        loginbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginbuttonActionPerformed(evt);
            }
        });
        add(loginbutton);
        loginbutton.setBounds(565, 600, 240, 60);

        passfield.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        passfield.setBorder(null);
        passfield.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passfieldActionPerformed(evt);
            }
        });
        add(passfield);
        passfield.setBounds(540, 433, 320, 45);
        cooldownlabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        cooldownlabel.setForeground(new java.awt.Color(255, 0, 0));
        cooldownlabel.setText("Cooldown");
        add(cooldownlabel);
        cooldownlabel.setBounds(520, 510, 160, 30);

        jButton1.setText("Forgot Password?");
        jButton1.setBorder(null);
        jButton1.setBorderPainted(false);
        jButton1.setContentAreaFilled(false);
        jButton1.setFocusPainted(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        add(jButton1);
        jButton1.setBounds(740, 490, 200, 30);

        jButton2.setBorderPainted(false);
        jButton2.setContentAreaFilled(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        add(jButton2);
        jButton2.setBounds(935, 10, 50, 40);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Photos/LOGIN PANEL.png"))); // NOI18N
        add(jLabel1);
        jLabel1.setBounds(0, 0, 1080, 750);
    }// </editor-fold>//GEN-END:initComponents

    private void passfieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passfieldActionPerformed
        attemptLogin();
    }//GEN-LAST:event_passfieldActionPerformed

    private void userfieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userfieldActionPerformed
    String input = userfield.getText().trim();
    if (!input.matches("^[A-Za-z0-9_]{3,15}$")) {
        JOptionPane.showMessageDialog(this, "Username must be 3-15 characters and only contain letters, numbers, or underscores.");
        userfield.requestFocus();
    } else {
        passfield.requestFocus();
    }
}//GEN-LAST:event_userfieldActionPerformed

    private void loginbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginbuttonActionPerformed
        attemptLogin();
    }//GEN-LAST:event_loginbuttonActionPerformed

    private void SeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SeeActionPerformed
         
        
        if(passfield.getEchoChar() == 0) {
        passfield.setEchoChar('•');
        
        See.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Photos/EyeClose.png")));
                
        } else {
            
            See.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Photos/EyeOpen.png")));
            passfield.setEchoChar((char) 0 );
            
        }
    }//GEN-LAST:event_SeeActionPerformed


    @Override
    public void addNotify() {
        super.addNotify();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (userfield != null) {
                    userfield.requestFocusInWindow();
                }
                javax.swing.JRootPane root = javax.swing.SwingUtilities.getRootPane(Login.this);
                if (root != null && loginbutton != null) {
                    root.setDefaultButton(loginbutton);
                }
            }
        });
    }

    public void focusUserField() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (userfield != null) {
                    userfield.requestFocusInWindow();
                }
            }
        });
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        javax.swing.JOptionPane.showMessageDialog(this, "Please contact the administrator to reset your password.", "Forgot Password", javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
    Window w = SwingUtilities.getWindowAncestor(Login.this);
        if (w instanceof cephra.Frame.Admin) {
            ((cephra.Frame.Admin) w).switchPanel(new Splash());
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void attemptLogin() {
    // Check if in cooldown period
    if (cooldownTimer != null && cooldownTimer.isRunning()) {
        JOptionPane.showMessageDialog(this, "Please wait for the cooldown period to end.", "Login Locked", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    String username = userfield.getText() != null ? userfield.getText().trim() : "";
    String password = new String(passfield.getPassword());

    // Username validation
    if (!username.matches("^[A-Za-z0-9_]{3,15}$")) {
        JOptionPane.showMessageDialog(this, "Username must be 3-15 characters and only contain letters, numbers, or underscores.");
        userfield.requestFocus();
        return;
    }
    // Password validation
    if (password.length() < 3 || password.length() > 15) {
        JOptionPane.showMessageDialog(this, "Password must be 3-15 characters.");
        passfield.requestFocus();
        return;
    }

    // Validate staff login using the database
    if (cephra.CephraDB.validateStaffLogin(username, password)) {
        // Reset login attempts on successful login
        loginAttempts = 0;
        java.awt.Window window = javax.swing.SwingUtilities.getWindowAncestor(this);
        if (window instanceof cephra.Frame.Admin) {
            cephra.Frame.Admin mainFrame = (cephra.Frame.Admin) window;
            // Update the admin frame to store the logged-in username
            mainFrame.setLoggedInUsername(username);
            mainFrame.switchPanel(new cephra.Admin.Dashboard());
        }
    } else {
        // Increment failed login attempts
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
                "Invalid staff credentials. You have " + remainingAttempts + " attempts remaining.", 
                "Login Failed", javax.swing.JOptionPane.WARNING_MESSAGE);
        }
        
        passfield.setText(""); // Clear password field
        userfield.requestFocusInWindow(); // Refocus on username field
    }
}

private void startCooldownTimer() {
    // Disable login components
    loginbutton.setEnabled(false);
    userfield.setEnabled(false);
    passfield.setEnabled(false);
    
    // Reset cooldown seconds
    cooldownSeconds = 30;
    
    // Update and show cooldown label
    cooldownlabel.setText("Cooldown: " + cooldownSeconds + "s");
    cooldownlabel.setVisible(true);
    
    // Create and start the timer
    cooldownTimer = new Timer(1000, e -> {
        cooldownSeconds--;
        cooldownlabel.setText("Cooldown: " + cooldownSeconds + "s");
        
        if (cooldownSeconds <= 0) {
            // Time's up, stop the timer and reset
            ((Timer)e.getSource()).stop();
            loginAttempts = 0;
            cooldownlabel.setVisible(false);
            loginbutton.setEnabled(true);
            userfield.setEnabled(true);
            passfield.setEnabled(true);
        }
    });
    
    cooldownTimer.start();
}


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton See;
    private javax.swing.JLabel cooldownlabel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton loginbutton;
    private javax.swing.JPasswordField passfield;
    private javax.swing.JTextField userfield;
    // End of variables declaration//GEN-END:variables
    
  

    // Add this inner class at the end of your Login class (before the last closing brace)
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
                java.awt.Toolkit.getDefaultToolkit().beep();
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text == null) return;
            if (username && !text.matches("[A-Za-z0-9_]*")) return;
            if ((fb.getDocument().getLength() - length + text.length()) <= max) {
                super.replace(fb, offset, length, text, attrs);
            } else {
                java.awt.Toolkit.getDefaultToolkit().beep();
            }
        }
    }
}
