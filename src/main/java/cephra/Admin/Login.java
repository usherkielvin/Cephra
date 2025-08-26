package cephra.Admin;

import java.awt.Color;
import javax.swing.JOptionPane;
import javax.swing.text.*; // Add this import

public class Login extends javax.swing.JPanel {

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
        See.setIcon(new javax.swing.ImageIcon("/cephra/Photos/EyeClose.png")); 
        See.setBorderPainted(false);
        See.setOpaque(false);
        See.setContentAreaFilled(false);

        // --- INTEGRATED FILTERS ---
        ((AbstractDocument) userfield.getDocument()).setDocumentFilter(new InputLimitFilter(15, true));
        ((AbstractDocument) passfield.getDocument()).setDocumentFilter(new InputLimitFilter(15, false));
    }

    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        See = new javax.swing.JButton();
        userfield = new javax.swing.JTextField();
        loginbutton = new javax.swing.JButton();
        passfield = new javax.swing.JPasswordField();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(1000, 750));
        setPreferredSize(new java.awt.Dimension(1000, 750));
        setLayout(null);

        See.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Photos/EyeClose.png"))); // NOI18N
        See.setBorderPainted(false);
        See.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SeeActionPerformed(evt);
            }
        });
        add(See);
        See.setBounds(810, 430, 93, 50);

        userfield.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        userfield.setBorder(null);
        userfield.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userfieldActionPerformed(evt);
            }
        });
        add(userfield);
        userfield.setBounds(540, 320, 340, 40);

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
        passfield.setBounds(540, 435, 270, 40);

        jButton1.setFont(new java.awt.Font("Segoe UI", 0, 17)); // NOI18N
        jButton1.setText("Forgot Password?");
        jButton1.setBorderPainted(false);
        jButton1.setContentAreaFilled(false);
        jButton1.setForeground(new java.awt.Color(102, 102, 102));
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton1.setText("<html><font color='#666666'><u>Forgot Password?</u></font></html>");
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton1.setText("<html><font color='#666666'>Forgot Password?</font></html>");
            }
        });
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        add(jButton1);
        jButton1.setBounds(740, 550, 200, 30);

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
        
        See.setIcon(new javax.swing.ImageIcon("/cephra/Photos/EyeClose.png")); 
                
        } else {
            
            See.setIcon(new javax.swing.ImageIcon("/cephra/Photos/EyeOpen.png")); 
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

    private void attemptLogin() {
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

    if ("admin".equals(username) && "1234".equals(password)) {
        java.awt.Window window = javax.swing.SwingUtilities.getWindowAncestor(this);
        if (window instanceof cephra.Frame.Admin) {
            cephra.Frame.Admin mainFrame = (cephra.Frame.Admin) window;
            mainFrame.switchPanel(new cephra.Admin.Dashboard());
        }
    } else {
        javax.swing.JOptionPane.showMessageDialog(this, "Invalid credentials (demo: admin / 1234)", "Login Failed", javax.swing.JOptionPane.WARNING_MESSAGE);
        passfield.setText(""); // Clear password field
        userfield.requestFocusInWindow(); // Refocus on username field
    }
}


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton See;
    private javax.swing.JButton jButton1;
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
