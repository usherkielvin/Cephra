package cephra.Admin;

import java.awt.Color;
import java.awt.Window;
import javax.swing.SwingUtilities;

public class AdminRegister extends javax.swing.JPanel {

    public AdminRegister() {
        initComponents();
        setPreferredSize(new java.awt.Dimension(1000, 750));
        setSize(1000, 750);
        Firstname.setOpaque(false);
        Firstname.setBackground(new Color(0, 0, 0, 0));  LastName.setOpaque(false);
        LastName.setBackground(new Color(0, 0, 0, 0));
        email.setOpaque(false);
        email.setBackground(new Color(0, 0, 0, 0));
        username.setOpaque(false);
        username.setBackground(new Color(0, 0, 0, 0));
        pass.setOpaque(false);
        pass.setBackground(new Color(0, 0, 0, 0));
        ConPass.setOpaque(false);
        ConPass.setBackground(new Color(0, 0, 0, 0));
        
        
        
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        username = new javax.swing.JTextField();
        email = new javax.swing.JTextField();
        RegisterBTN = new javax.swing.JButton();
        pass = new javax.swing.JPasswordField();
        ConPass = new javax.swing.JPasswordField();
        log = new javax.swing.JButton();
        AdminBTN = new javax.swing.JButton();
        Firstname = new javax.swing.JTextField();
        LastName = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(1000, 750));
        setLayout(null);

        username.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        username.setBorder(null);
        username.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                usernameActionPerformed(evt);
            }
        });
        add(username);
        username.setBounds(388, 364, 260, 37);

        email.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        email.setBorder(null);
        email.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailActionPerformed(evt);
            }
        });
        add(email);
        email.setBounds(388, 452, 260, 37);

        RegisterBTN.setBorderPainted(false);
        RegisterBTN.setContentAreaFilled(false);
        RegisterBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RegisterBTNActionPerformed(evt);
            }
        });
        add(RegisterBTN);
        RegisterBTN.setBounds(570, 620, 230, 50);

        pass.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        pass.setBorder(null);
        pass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passActionPerformed(evt);
            }
        });
        add(pass);
        pass.setBounds(700, 276, 220, 37);
        
        // Add show/hide password button for pass field
        showPassBtn = new javax.swing.JButton();
        showPassBtn.setText("👁");
        showPassBtn.setFont(new java.awt.Font("Segoe UI", 0, 16));
        showPassBtn.setBorder(null);
        showPassBtn.setContentAreaFilled(false);
        showPassBtn.setFocusPainted(false);
        showPassBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                togglePasswordVisibility(pass, showPassBtn);
            }
        });
        add(showPassBtn);
        showPassBtn.setBounds(920, 276, 40, 37);

        ConPass.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        ConPass.setBorder(null);
        ConPass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ConPassActionPerformed(evt);
            }
        });
        add(ConPass);
        ConPass.setBounds(700, 362, 220, 38);
        
        // Add show/hide password button for ConPass field
        showConPassBtn = new javax.swing.JButton();
        showConPassBtn.setText("👁");
        showConPassBtn.setFont(new java.awt.Font("Segoe UI", 0, 16));
        showConPassBtn.setBorder(null);
        showConPassBtn.setContentAreaFilled(false);
        showConPassBtn.setFocusPainted(false);
        showConPassBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                togglePasswordVisibility(ConPass, showConPassBtn);
            }
        });
        add(showConPassBtn);
        showConPassBtn.setBounds(920, 362, 40, 38);

        log.setBorderPainted(false);
        log.setContentAreaFilled(false);
        log.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logActionPerformed(evt);
            }
        });
        add(log);
        log.setBounds(870, 30, 110, 30);
        log.getAccessibleContext().setAccessibleName("log");

        AdminBTN.setBorderPainted(false);
        AdminBTN.setContentAreaFilled(false);
        AdminBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AdminBTNActionPerformed(evt);
            }
        });
        add(AdminBTN);
        AdminBTN.setBounds(870, 73, 110, 30);

        Firstname.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        Firstname.setBorder(null);
        Firstname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FirstnameActionPerformed(evt);
            }
        });
        add(Firstname);
        Firstname.setBounds(388, 276, 120, 37);

        LastName.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        LastName.setBorder(null);
        add(LastName);
        LastName.setBounds(533, 276, 120, 37);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/ADMINregister.png"))); // NOI18N
        add(jLabel1);
        jLabel1.setBounds(0, 0, 1000, 750);
    }// </editor-fold>//GEN-END:initComponents

    private void FirstnameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FirstnameActionPerformed
        username.requestFocus();
    }//GEN-LAST:event_FirstnameActionPerformed

    private void usernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_usernameActionPerformed
        email.requestFocus();
    }//GEN-LAST:event_usernameActionPerformed

    private void emailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailActionPerformed
        pass.requestFocus();
    }//GEN-LAST:event_emailActionPerformed

    private void passActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passActionPerformed
        ConPass.requestFocus();
    }//GEN-LAST:event_passActionPerformed

    private void ConPassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ConPassActionPerformed
       
    }//GEN-LAST:event_ConPassActionPerformed

    private void RegisterBTNActionPerformed(java.awt.event.ActionEvent evt) {
        String name = Firstname.getText().trim();
        String usernameVal = username.getText().trim();
        String emailVal = email.getText().trim();
        String password = new String(pass.getPassword()).trim();
        String confirm = new String(ConPass.getPassword()).trim();

        if (name.isEmpty() || usernameVal.isEmpty() || emailVal.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Please fill in all fields", "Validation", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!password.equals(confirm)) {
            javax.swing.JOptionPane.showMessageDialog(this, "Passwords do not match", "Validation", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Basic email format check
        if (!emailVal.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            javax.swing.JOptionPane.showMessageDialog(this, "Please enter a valid email address.", "Validation", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        cephra.Admin.StaffData.addStaff(name, usernameVal, emailVal, password);

        javax.swing.JOptionPane.showMessageDialog(this, "Admin registration successful", "Success", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        Firstname.setText("");
        username.setText("");
        email.setText("");
        pass.setText("");
        ConPass.setText("");
    }

    private void logActionPerformed(java.awt.event.ActionEvent evt) {
         Window w = SwingUtilities.getWindowAncestor(AdminRegister.this);
        if (w instanceof cephra.Frame.Admin) {
            ((cephra.Frame.Admin) w).switchPanel(new cephra.Admin.Login());
        }
    }

    private void AdminBTNActionPerformed(java.awt.event.ActionEvent evt) {
       Window w = SwingUtilities.getWindowAncestor(AdminRegister.this);
        if (w instanceof cephra.Frame.Admin) {
            ((cephra.Frame.Admin) w).switchPanel(new StaffRecord());
        }
    }
    
    /**
     * Toggles password visibility for a password field
     * @param passwordField the password field to toggle
     * @param toggleButton the button that triggers the toggle
     */
    private void togglePasswordVisibility(javax.swing.JPasswordField passwordField, javax.swing.JButton toggleButton) {
        if (passwordField.getEchoChar() == '\u0000') {
            // Currently visible, hide it
            passwordField.setEchoChar('•');
            toggleButton.setText("👁");
        } else {
            // Currently hidden, show it
            passwordField.setEchoChar('\u0000');
            toggleButton.setText("🙈");
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AdminBTN;
    private javax.swing.JPasswordField ConPass;
    private javax.swing.JTextField Firstname;
    private javax.swing.JTextField LastName;
    private javax.swing.JButton RegisterBTN;
    private javax.swing.JTextField email;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton log;
    private javax.swing.JPasswordField pass;
    private javax.swing.JButton showConPassBtn;
    private javax.swing.JButton showPassBtn;
    private javax.swing.JTextField username;
    // End of variables declaration//GEN-END:variables
}
