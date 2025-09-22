package cephra.Phone.UserProfile;

import java.awt.*;
import java.awt.event.*;
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
        ((AbstractDocument) pass.getDocument()).setDocumentFilter(new InputLimitFilter(15, false));
        See.setBorderPainted(false);
        See.setOpaque(false);
        See.setContentAreaFilled(false);
        addEscapeKeyListener();
        addUnderlineOnHover(reghere);
        addUnderlineOnHover(forgotpass);
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
        See.setBounds(280, 390, 50, 30);

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
        reghere.setBounds(220, 667, 80, 30);

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
        forgotpass.setBounds(210, 425, 120, 30);

        pass.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        pass.setBorder(null);
        pass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passActionPerformed(evt);
            }
        });
        add(pass);
        pass.setBounds(80, 390, 200, 30);

        username.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        username.setBorder(null);
        username.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                usernameActionPerformed(evt);
            }
        });
        add(username);
        username.setBounds(80, 320, 230, 35);
        add(cooldownLabel);
        cooldownLabel.setBounds(40, 400, 160, 20);

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
        loginhome.setBounds(40, 430, 290, 40);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/LOGIN.png"))); // NOI18N
        add(jLabel1);
        jLabel1.setBounds(0, 0, 360, 750);
    }// </editor-fold>//GEN-END:initComponents

    private void setupLabelPosition() {
        if (jLabel1 != null) {
            jLabel1.setBounds(0, 0, 370, 750);
        }
    }

    private void loginhomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginhomeActionPerformed
        attemptLogin();
    }//GEN-LAST:event_loginhomeActionPerformed

    private void attemptLogin() {
        if (cooldownTimer != null && cooldownTimer.isRunning()) {
            JOptionPane.showMessageDialog(this, "Please wait for the cooldown period to end.", "Login Locked", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String usernameText = username.getText() != null ? username.getText().trim() : "";
        String password = new String(pass.getPassword());

        if (usernameText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your username.", "Empty Field", JOptionPane.WARNING_MESSAGE);
            username.requestFocusInWindow();
            return;
        }
        
        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your password.", "Empty Field", JOptionPane.WARNING_MESSAGE);
            pass.requestFocusInWindow();
            return;
        }

        if (cephra.Database.CephraDB.validateLogin(usernameText, password)) {
            loginAttempts = 0;
            cephra.Phone.Utilities.AppState.initializeCarLinkingState();
            
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    java.awt.Window[] windows = java.awt.Window.getWindows();
                    for (java.awt.Window window : windows) {
                        if (window instanceof cephra.Frame.Phone) {
                            cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                            phoneFrame.switchPanel(cephra.Phone.Dashboard.Home.getAppropriateHomePanel());
                            break;
                        }
                    }
                }
            });
        } else {
            loginAttempts++;
            
            if (loginAttempts >= MAX_ATTEMPTS) {
                startCooldownTimer();
                JOptionPane.showMessageDialog(this, "Too many failed login attempts. Please wait 30 seconds before trying again.", 
                    "Login Locked", JOptionPane.ERROR_MESSAGE);
            } else {
                int remainingAttempts = MAX_ATTEMPTS - loginAttempts;
                javax.swing.JOptionPane.showMessageDialog(this, 
                    "Invalid credentials. You have " + remainingAttempts + " attempts remaining.", 
                    "Login Failed", javax.swing.JOptionPane.WARNING_MESSAGE);
            }
            
            pass.setText("");
            username.requestFocusInWindow();
        }
    }

    private void startCooldownTimer() {
        loginhome.setEnabled(false);
        username.setEnabled(false);
        pass.setEnabled(false);
        cooldownSeconds = 30;
        cooldownLabel.setText("Cooldown: " + cooldownSeconds + "s");
        cooldownLabel.setVisible(true);
        
        cooldownTimer = new Timer(1000, e -> {
            cooldownSeconds--;
            cooldownLabel.setText("Cooldown: " + cooldownSeconds + "s");
            
            if (cooldownSeconds <= 0) {
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
        javax.swing.ImageIcon logoIcon = new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/smalllogo.png"));
        int result = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to reset your password?",
            "Confirm Password Reset",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            logoIcon
        );
        
        if (result == JOptionPane.YES_OPTION) {
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
        }
    }//GEN-LAST:event_forgotpassActionPerformed

    private void passActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passActionPerformed
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
