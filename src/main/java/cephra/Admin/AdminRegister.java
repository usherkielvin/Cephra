package cephra.Admin;

import java.awt.Window;
import javax.swing.SwingUtilities;

public class AdminRegister extends javax.swing.JPanel {

	public AdminRegister() {
		initComponents();
		setPreferredSize(new java.awt.Dimension(1000, 750));
		setSize(1000, 750);
                
                
	}

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        username = new javax.swing.JTextField();
        gender = new javax.swing.JTextField();
        RegisterBTN = new javax.swing.JButton();
        pass = new javax.swing.JPasswordField();
        ConPass = new javax.swing.JPasswordField();
        log = new javax.swing.JButton();
        AdminBTN = new javax.swing.JButton();
        Fullname = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(1000, 750));
        setLayout(null);
        add(username);
        username.setBounds(722, 268, 245, 40);
        add(gender);
        gender.setBounds(433, 373, 245, 38);

        RegisterBTN.setBorderPainted(false);
        RegisterBTN.setContentAreaFilled(false);
        RegisterBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RegisterBTNActionPerformed(evt);
            }
        });
        add(RegisterBTN);
        RegisterBTN.setBounds(560, 670, 240, 60);
        add(pass);
        pass.setBounds(433, 477, 245, 40);
        add(ConPass);
        ConPass.setBounds(433, 581, 245, 40);

        log.setBorderPainted(false);
        log.setContentAreaFilled(false);
        log.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logActionPerformed(evt);
            }
        });
        add(log);
        log.setBounds(870, 20, 120, 30);
        log.getAccessibleContext().setAccessibleName("log");

        AdminBTN.setBorderPainted(false);
        AdminBTN.setContentAreaFilled(false);
        AdminBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AdminBTNActionPerformed(evt);
            }
        });
        add(AdminBTN);
        AdminBTN.setBounds(870, 60, 120, 30);
        add(Fullname);
        Fullname.setBounds(430, 270, 245, 38);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Photos/ADMINregister.png"))); // NOI18N
        add(jLabel1);
        jLabel1.setBounds(0, 0, 1000, 750);
    }// </editor-fold>//GEN-END:initComponents

    private void RegisterBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RegisterBTNActionPerformed
        String name = Fullname.getText().trim();
        String email = username.getText().trim();
        String genderText = gender.getText().trim();
        String password = new String(pass.getPassword()).trim();
        String confirm = new String(ConPass.getPassword()).trim();

        if (name.isEmpty() || email.isEmpty() || genderText.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Please fill in all fields", "Validation", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!password.equals(confirm)) {
            javax.swing.JOptionPane.showMessageDialog(this, "Passwords do not match", "Validation", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }
        javax.swing.JOptionPane.showMessageDialog(this, "Admin registration successful", "Success", javax.swing.JOptionPane.INFORMATION_MESSAGE); 
    }//GEN-LAST:event_RegisterBTNActionPerformed

    private void logActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logActionPerformed
         Window w = SwingUtilities.getWindowAncestor(AdminRegister.this);
        if (w instanceof cephra.Frame.Admin) {
            ((cephra.Frame.Admin) w).switchPanel(new cephra.Admin.Login());
        }

    }//GEN-LAST:event_logActionPerformed

    private void AdminBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AdminBTNActionPerformed
       Window w = SwingUtilities.getWindowAncestor(AdminRegister.this);
        if (w instanceof cephra.Frame.Admin) {
            ((cephra.Frame.Admin) w).switchPanel(new StaffRecord());
        }
    }//GEN-LAST:event_AdminBTNActionPerformed



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AdminBTN;
    private javax.swing.JPasswordField ConPass;
    private javax.swing.JTextField Fullname;
    private javax.swing.JButton RegisterBTN;
    private javax.swing.JTextField gender;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton log;
    private javax.swing.JPasswordField pass;
    private javax.swing.JTextField username;
    // End of variables declaration//GEN-END:variables
}
