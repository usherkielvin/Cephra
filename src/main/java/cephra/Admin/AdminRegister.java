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

        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        RegisterBTN = new javax.swing.JButton();
        LoginBTN = new javax.swing.JButton();
        BackBTN = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(1000, 750));
        setLayout(null);

        add(jTextField1);
        jTextField1.setBounds(430, 270, 250, 40);
        add(jTextField2);
        jTextField2.setBounds(720, 270, 250, 40);
        add(jTextField3);
        jTextField3.setBounds(430, 370, 250, 40);
        add(jTextField4);
        jTextField4.setBounds(430, 480, 250, 40);
        add(jTextField5);
        jTextField5.setBounds(430, 580, 250, 40);

        RegisterBTN.setBorderPainted(false);
        RegisterBTN.setContentAreaFilled(false);
        add(RegisterBTN);
        RegisterBTN.setBounds(560, 670, 240, 60);

        LoginBTN.setToolTipText("Go to login page");
        LoginBTN.setBorderPainted(false);
        LoginBTN.setContentAreaFilled(false);
        LoginBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LoginBTNActionPerformed(evt);
            }
        });
        add(LoginBTN);
        LoginBTN.setBounds(870, 20, 110, 30);

        BackBTN.setToolTipText("Return To Admin");
        BackBTN.setBorderPainted(false);
        BackBTN.setContentAreaFilled(false);
        BackBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BackBTNActionPerformed(evt);
            }
        });
        add(BackBTN);
        BackBTN.setBounds(945, 60, 40, 40);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Photos/LOGIN PANEL.png"))); // NOI18N
        add(jLabel1);
        jLabel1.setBounds(0, 0, 1000, 750);
        
        RegisterBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registerButtonActionPerformed(evt);
            }
        });
    }// </editor-fold>//GEN-END:initComponents

	private void registerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_registerButtonActionPerformed
		String name = jTextField1.getText().trim();
		String email = jTextField2.getText().trim();
		String pass = jTextField3.getText().trim();
		String confirm = jTextField4.getText().trim();

		if (name.isEmpty() || email.isEmpty() || pass.isEmpty() || confirm.isEmpty()) {
			javax.swing.JOptionPane.showMessageDialog(this, "Please fill in all fields", "Validation", javax.swing.JOptionPane.WARNING_MESSAGE);
			return;
		}
		if (!pass.equals(confirm)) {
			javax.swing.JOptionPane.showMessageDialog(this, "Passwords do not match", "Validation", javax.swing.JOptionPane.ERROR_MESSAGE);
			return;
		}
		javax.swing.JOptionPane.showMessageDialog(this, "Admin registration successful", "Success", javax.swing.JOptionPane.INFORMATION_MESSAGE);
	}//GEN-LAST:event_registerButtonActionPerformed

	private void LoginBTNActionPerformed(java.awt.event.ActionEvent evt) {
		Window w = SwingUtilities.getWindowAncestor(AdminRegister.this);
		if (w instanceof cephra.Frame.Admin) {
			((cephra.Frame.Admin) w).switchPanel(new cephra.Admin.Login());
		}
	}

	private void BackBTNActionPerformed(java.awt.event.ActionEvent evt) {
		Window w = SwingUtilities.getWindowAncestor(AdminRegister.this);
		if (w instanceof cephra.Frame.Admin) {
			((cephra.Frame.Admin) w).switchPanel(new cephra.Admin.StaffRecord());
		}
	}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BackBTN;
    private javax.swing.JButton LoginBTN;
    private javax.swing.JButton RegisterBTN;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    // End of variables declaration//GEN-END:variables
}
