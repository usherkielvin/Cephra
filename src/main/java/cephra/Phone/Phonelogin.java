package cephra.Phone;

import java.awt.*;
import javax.swing.*;
import javax.swing.text.*; // Add this import

public class Phonelogin extends javax.swing.JPanel {
   private int loginAttempts = 0;
    private final int MAX_ATTEMPTS = 3;
    public Phonelogin() {
        initComponents();
        setPreferredSize(new java.awt.Dimension(350, 750));
        setSize(350, 750);
        username.setOpaque(false);
        username.setBackground(new Color(0, 0, 0, 0));
        pass.setOpaque(false);
        pass.setBackground(new Color(0, 0, 0, 0));
       See.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Photos/EyeClose.png")));

        // --- INTEGRATED FILTERS ---
        ((AbstractDocument) username.getDocument()).setDocumentFilter(new InputLimitFilter(15, true));
        ((AbstractDocument) pass.getDocument()).setDocumentFilter(new InputLimitFilter(15, false));
        
         See.setBorderPainted(false);
        See.setOpaque(false);
        See.setContentAreaFilled(false);
    }
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        See = new javax.swing.JButton();
        reghere = new javax.swing.JButton();
        forgotpass = new javax.swing.JButton();
        pass = new javax.swing.JPasswordField();
        username = new javax.swing.JTextField();
        loginhome = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(350, 750));
        setPreferredSize(new java.awt.Dimension(350, 750));
        setLayout(null);

        See.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Photos/EyeClose.png"))); // NOI18N
        See.setBorder(null);
        See.setOpaque(true);
        See.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SeeActionPerformed(evt);
            }
        });
        add(See);
        See.setBounds(260, 390, 50, 20);

        reghere.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        reghere.setForeground(new java.awt.Color(0, 204, 204));
        reghere.setText("Register here");
        reghere.setBorder(null);
        reghere.setBorderPainted(false);
        reghere.setContentAreaFilled(false);
        reghere.setFocusPainted(false);
        reghere.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reghereActionPerformed(evt);
            }
        });
        add(reghere);
        reghere.setBounds(190, 610, 120, 40);

        forgotpass.setForeground(new java.awt.Color(102, 102, 102));
        forgotpass.setText("Forgot password?");
        forgotpass.setBorder(null);
        forgotpass.setBorderPainted(false);
        forgotpass.setContentAreaFilled(false);
        forgotpass.setFocusPainted(false);
        forgotpass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                forgotpassActionPerformed(evt);
            }
        });
        add(forgotpass);
        forgotpass.setBounds(200, 410, 120, 40);

        pass.setBorder(null);
        pass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passActionPerformed(evt);
            }
        });
        add(pass);
        pass.setBounds(60, 381, 200, 30);

        username.setBorder(null);
        username.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                usernameActionPerformed(evt);
            }
        });
        add(username);
        username.setBounds(60, 334, 240, 30);

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
        loginhome.setBounds(80, 570, 220, 40);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Photos/1.png"))); // NOI18N
        add(jLabel1);
        jLabel1.setBounds(-15, 0, 398, 750);
    }// </editor-fold>//GEN-END:initComponents

    // CUSTOM CODE - DO NOT REMOVE - NetBeans will regenerate form code but this method should be preserved
    // Setup label position to prevent NetBeans from changing it
    private void setupLabelPosition() {
        if (jLabel1 != null) {
            jLabel1.setBounds(-15, 0, 398, 750);
        }
    }

    private void loginhomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginhomeActionPerformed
        attemptLogin();
    }//GEN-LAST:event_loginhomeActionPerformed

     private void attemptLogin() {
        // Check if login attempts are exhausted
        if (loginAttempts >= MAX_ATTEMPTS) {
            javax.swing.JOptionPane.showMessageDialog(this, "Too many failed login attempts. Please use the 'Forgot Password' option.", "Login Disabled", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        String usernameText = username.getText() != null ? username.getText().trim() : "";
        String password = new String(pass.getPassword());

        // Validate login using the CephraDB
        if (cephra.CephraDB.validateLogin(usernameText, password)) {
            // Login successful, reset attempts
            loginAttempts = 0;
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    java.awt.Window[] windows = java.awt.Window.getWindows();
                    for (java.awt.Window window : windows) {
                        if (window instanceof cephra.Frame.Phone) {
                            cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                            phoneFrame.switchPanel(new cephra.Phone.home());
                            break;
                        }
                    }
                }
            });
        } else {
            // Login failed, increment attempts
            loginAttempts++;
            int remainingAttempts = MAX_ATTEMPTS - loginAttempts;
            if (remainingAttempts > 0) {
                javax.swing.JOptionPane.showMessageDialog(this, "Invalid credentials. You have " + remainingAttempts + " attempts remaining.", "Login Failed", javax.swing.JOptionPane.WARNING_MESSAGE);
            } else {
                javax.swing.JOptionPane.showMessageDialog(this, "Invalid credentials. Too many failed attempts. The login button has been disabled.", "Login Disabled", javax.swing.JOptionPane.ERROR_MESSAGE);
                loginhome.setEnabled(false); // Disable the login button
            }
            pass.setText(""); // Clear password field
            username.requestFocusInWindow(); // Refocus on username field
        }
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
                        phoneFrame.switchPanel(new cephra.Phone.Register());
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
                        phoneFrame.switchPanel(new cephra.Phone.ForgotPassword());
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
        See.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Photos/EyeClose.png")));
    } else {
        See.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Photos/EyeOpen.png")));
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
    private javax.swing.JButton forgotpass;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton loginhome;
    private javax.swing.JPasswordField pass;
    private javax.swing.JButton reghere;
    private javax.swing.JTextField username;
    // End of variables declaration//GEN-END:variables

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
