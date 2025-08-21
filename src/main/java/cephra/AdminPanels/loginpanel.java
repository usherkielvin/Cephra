
package cephra.AdminPanels;

import java.awt.Color;

public class loginpanel extends javax.swing.JPanel {

    public loginpanel() {
        initComponents();
         setPreferredSize(new java.awt.Dimension(1000, 750));
        setSize(1000, 750);
        
        userfield.setOpaque(false);
        userfield.setBackground(new Color(0, 0, 0, 0));

        passfield.setOpaque(false);
        passfield.setBackground(new Color(0, 0, 0, 0));
    }

    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        userfield = new javax.swing.JTextField();
        loginbutton = new javax.swing.JButton();
        passfield = new javax.swing.JPasswordField();
        jLabel1 = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(1000, 750));
        setPreferredSize(new java.awt.Dimension(1000, 750));
        setLayout(null);

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
        add(loginbutton);
        loginbutton.setBounds(565, 600, 240, 60);
        loginbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginbuttonActionPerformed(evt);
            }
        });

        passfield.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        passfield.setBorder(null);
        passfield.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passfieldActionPerformed(evt);
            }
        });
        add(passfield);
        passfield.setBounds(540, 440, 320, 40);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Photos/LOGIN PANEL.png"))); // NOI18N
        add(jLabel1);
        jLabel1.setBounds(0, 0, 1080, 750);
        // Keep background behind interactive components
        setComponentZOrder(jLabel1, getComponentCount() - 1);
    }// </editor-fold>//GEN-END:initComponents

    private void passfieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passfieldActionPerformed
        attemptLogin();
    }//GEN-LAST:event_passfieldActionPerformed

    private void userfieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userfieldActionPerformed
        passfield.requestFocus();
    }//GEN-LAST:event_userfieldActionPerformed

    private void loginbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginbuttonActionPerformed
        attemptLogin();
    }//GEN-LAST:event_loginbuttonActionPerformed


    @Override
    public void addNotify() {
        super.addNotify();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (userfield != null) {
                    userfield.requestFocusInWindow();
                }
                javax.swing.JRootPane root = javax.swing.SwingUtilities.getRootPane(loginpanel.this);
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

    private void attemptLogin() {
        String username = userfield.getText() != null ? userfield.getText().trim() : "";
        String password = new String(passfield.getPassword());
        if ("admin".equals(username) && "1234".equals(password)) {
            java.awt.Window window = javax.swing.SwingUtilities.getWindowAncestor(this);
            if (window instanceof cephra.Frames.AdminFrame) {
                cephra.Frames.AdminFrame mainFrame = (cephra.Frames.AdminFrame) window;
                mainFrame.switchPanel(new cephra.AdminPanels.AdminDashboard());
            }
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "Invalid credentials (demo: admin / 1234)", "Login Failed", javax.swing.JOptionPane.WARNING_MESSAGE);
            passfield.setText(""); // Clear password field
            userfield.requestFocusInWindow(); // Refocus on username field
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton loginbutton;
    private javax.swing.JPasswordField passfield;
    private javax.swing.JTextField userfield;
    // End of variables declaration//GEN-END:variables
}
