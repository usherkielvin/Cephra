package cephra.Admin;

import java.awt.*;
import java.awt.event.*;
import java.awt.font.*;
import javax.swing.*;
import javax.swing.text.*;
import java.util.*;
import javax.swing.Timer;

public class Login extends javax.swing.JPanel {
    private int loginAttempts = 0;
    private final int MAX_ATTEMPTS = 3;
    private Timer cooldownTimer;
    private int cooldownSeconds = 30;

    public Login() {
        initComponents();
        setPreferredSize(new java.awt.Dimension(1000, 750));
        setSize(1000, 750);

        // Initialize custom components after NetBeans generated code
        initializeCustomComponents();
    }

    private void initializeCustomComponents() {
        setupFields();
        setupFilters();
        setupForgotPasswordButton();
    }

    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        See = new javax.swing.JButton();
        userfield = new javax.swing.JTextField();
        loginbutton = new javax.swing.JButton();
        passfield = new javax.swing.JPasswordField();
        cooldownlabel = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(1000, 750));
        setPreferredSize(new java.awt.Dimension(1000, 750));
        setLayout(null);

        See.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/EyeClose.png"))); // NOI18N
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

        passfield.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        passfield.setBorder(null);
        passfield.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passfieldActionPerformed(evt);
            }
        });
        add(passfield);
        passfield.setBounds(540, 433, 320, 45);
        add(cooldownlabel);
        cooldownlabel.setBounds(520, 510, 160, 0);

        jButton1.setText("Forgot Password?"); // NOI18N
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

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/LOGIN PANEL.png"))); // NOI18N
        add(jLabel1);
        jLabel1.setBounds(0, 0, 1080, 750);
    }// </editor-fold>//GEN-END:initComponents

    private void passfieldActionPerformed(java.awt.event.ActionEvent evt) {
        attemptLogin();
    }

    private void userfieldActionPerformed(java.awt.event.ActionEvent evt) {
        if (isValidUsername(userfield.getText().trim())) {
            passfield.requestFocus();
        }
    }

    private void loginbuttonActionPerformed(java.awt.event.ActionEvent evt) {
        attemptLogin();
    }

    private void SeeActionPerformed(java.awt.event.ActionEvent evt) {
        if (passfield.getEchoChar() == 0) {
            passfield.setEchoChar('•');
            See.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/EyeClose.png")));
        } else {
            See.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/EyeOpen.png")));
            passfield.setEchoChar((char) 0);
        }
    }


    @Override
    public void addNotify() {
        super.addNotify();
        SwingUtilities.invokeLater(() -> {
            if (userfield != null) {
                userfield.requestFocusInWindow();
            }
            JRootPane root = SwingUtilities.getRootPane(Login.this);
            if (root != null && loginbutton != null) {
                root.setDefaultButton(loginbutton);
            }
        });
    }

    public void focusUserField() {
        SwingUtilities.invokeLater(() -> {
            if (userfield != null) {
                userfield.requestFocusInWindow();
            }
        });
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        JOptionPane.showMessageDialog(this, "Please contact the administrator to reset your password.", "Forgot Password", JOptionPane.INFORMATION_MESSAGE);
    }

    

    private void attemptLogin() {
        if (cooldownTimer != null && cooldownTimer.isRunning()) {
            JOptionPane.showMessageDialog(this, "Please wait for the cooldown period to end.", "Login Locked", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String username = userfield.getText() != null ? userfield.getText().trim() : "";
        String password = new String(passfield.getPassword());

        if (!isValidUsername(username)) {
            userfield.requestFocus();
            return;
        }
        
        if (!isValidPassword(password)) {
            passfield.requestFocus();
            return;
        }

        if (cephra.Database.CephraDB.validateStaffLogin(username, password)) {
            handleSuccessfulLogin(username);
        } else {
            handleFailedLogin();
        }
    }

    private void setupFields() {
        userfield.setOpaque(false);
        userfield.setBackground(new Color(0, 0, 0, 0));
        passfield.setOpaque(false);
        passfield.setBackground(new Color(0, 0, 0, 0));
        passfield.setBorder(null);
        passfield.setEchoChar('•');
        
        See.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/EyeClose.png")));
        See.setBorderPainted(false);
        See.setOpaque(false);
        See.setContentAreaFilled(false);
        
        cooldownlabel.setForeground(Color.RED);
        cooldownlabel.setVisible(false);
    }
    
    private void setupFilters() {
        ((AbstractDocument) userfield.getDocument()).setDocumentFilter(new InputLimitFilter(15, true));
        ((AbstractDocument) passfield.getDocument()).setDocumentFilter(new InputLimitFilter(15, false));
    }
    
    private void setupForgotPasswordButton() {
        if (jButton1 != null) {
            Font baseFont = jButton1.getFont();
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
    
    private boolean isValidUsername(String username) {
        if (!username.matches("^[A-Za-z0-9_]{3,15}$")) {
            JOptionPane.showMessageDialog(this, "Username must be 3-15 characters and only contain letters, numbers, or underscores.");
            return false;
        }
        return true;
    }
    
    private boolean isValidPassword(String password) {
        if (password.length() < 3 || password.length() > 15) {
            JOptionPane.showMessageDialog(this, "Password must be 3-15 characters.");
            return false;
        }
        return true;
    }
    
    private void handleSuccessfulLogin(String username) {
        loginAttempts = 0;
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window instanceof cephra.Frame.Admin) {
            cephra.Frame.Admin mainFrame = (cephra.Frame.Admin) window;
            mainFrame.setLoggedInUsername(username);
            
            if (cephra.Database.CephraDB.isAdminUser(username)) {
                mainFrame.switchPanel(new cephra.Admin.Business_Overview());
            } else {
                mainFrame.switchPanel(new cephra.Admin.BayManagement());
            }
        }
    }
    
    private void handleFailedLogin() {
        loginAttempts++;
        Toolkit.getDefaultToolkit().beep();
        
        if (loginAttempts >= MAX_ATTEMPTS) {
            startCooldownTimer();
            JOptionPane.showMessageDialog(this, "Too many failed login attempts. Please wait 30 seconds before trying again.", 
                "Login Locked", JOptionPane.ERROR_MESSAGE);
        } else {
            int remainingAttempts = MAX_ATTEMPTS - loginAttempts;
            JOptionPane.showMessageDialog(this, 
                "Invalid staff credentials. You have " + remainingAttempts + " attempts remaining.", 
                "Login Failed", JOptionPane.WARNING_MESSAGE);
        }
        
        passfield.setText("");
        userfield.requestFocusInWindow();
    }

    private void startCooldownTimer() {
        loginbutton.setEnabled(false);
        userfield.setEnabled(false);
        passfield.setEnabled(false);
        
        cooldownSeconds = 30;
        cooldownlabel.setText("Cooldown: " + cooldownSeconds + "s");
        cooldownlabel.setVisible(true);
        
        cooldownTimer = new Timer(1000, e -> {
            cooldownSeconds--;
            cooldownlabel.setText("Cooldown: " + cooldownSeconds + "s");
            
            if (cooldownSeconds <= 0) {
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
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton loginbutton;
    private javax.swing.JPasswordField passfield;
    private javax.swing.JTextField userfield;
    // End of variables declaration//GEN-END:variables

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
                Toolkit.getDefaultToolkit().beep();
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text == null) return;
            if (username && !text.matches("[A-Za-z0-9_]*")) return;
            if ((fb.getDocument().getLength() - length + text.length()) <= max) {
                super.replace(fb, offset, length, text, attrs);
            } else {
                Toolkit.getDefaultToolkit().beep();
            }
        }
    }
}