
package cephra.Phone;

public class OTP_forgotpass extends javax.swing.JPanel {

    public OTP_forgotpass() {
        initComponents();
        setPreferredSize(new java.awt.Dimension(350, 750));
        setSize(350, 750);

        setupLabelPosition();
        
        // Display current OTP in the preview label
        otpPreviewLabel.setText(cephra.CephraDB.getGeneratedOTP());
        
        // Auto focus on the email text field
        email.requestFocusInWindow();
    }
       // CUSTOM CODE - DO NOT REMOVE - NetBeans will regenerate form code but this method should be preserved
    // Setup label position to prevent NetBeans from changing it
    private void setupLabelPosition() {
        if (jLabel1 != null) {
            jLabel1.setBounds(-15, 0, 398, 750);
        }
    }


    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        email = new javax.swing.JTextField();
        contactsup = new javax.swing.JButton();
        Back = new javax.swing.JButton();
        cephraemail = new javax.swing.JButton();
        otpPreviewLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setLayout(null);

        email.setBorder(null);
        email.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailActionPerformed(evt);
            }
        });
        
        // Add Enter key listener to email field
        email.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                    // Trigger the same action as clicking Contact Support
                    contactsup.doClick();
                }
            }
        });
        add(email);
        email.setBounds(80, 330, 250, 30);

        contactsup.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        contactsup.setForeground(new java.awt.Color(0, 204, 204));
        contactsup.setText("Contact Support");
        contactsup.setBorder(null);
        contactsup.setBorderPainted(false);
        contactsup.setContentAreaFilled(false);
        contactsup.setFocusPainted(false);
        
        // Add hover effects for Contact Support button
        contactsup.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                contactsup.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 204, 204)));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                contactsup.setBorder(null);
            }
        });
        
        add(contactsup);
        contactsup.setBounds(160, 663, 130, 30);

        Back.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Back.setForeground(new java.awt.Color(0, 204, 204));
        Back.setText("Back");
        Back.setBorder(null);
        Back.setBorderPainted(false);
        Back.setContentAreaFilled(false);
        Back.setFocusPainted(false);
        
        // Add hover effects for Back button
        Back.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                Back.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 204, 204)));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                Back.setBorder(null);
            }
        });
        
        Back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BackActionPerformed(evt);
            }
        });
        add(Back);
        Back.setBounds(120, 630, 120, 23);

        cephraemail.setBorder(null);
        cephraemail.setBorderPainted(false);
        cephraemail.setContentAreaFilled(false);
        cephraemail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cephraemailActionPerformed(evt);
            }
        });
        add(cephraemail);
        cephraemail.setBounds(20, 50, 330, 110);

        otpPreviewLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        otpPreviewLabel.setForeground(java.awt.Color.WHITE);
        otpPreviewLabel.setOpaque(true);
        otpPreviewLabel.setBackground(new java.awt.Color(0, 0, 0, 150));
        add(otpPreviewLabel);
        otpPreviewLabel.setBounds(210, 133, 80, 20);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Photos/emailcheck.png"))); // NOI18N
        add(jLabel1);
        jLabel1.setBounds(0, 0, 370, 750);
    }// </editor-fold>//GEN-END:initComponents

    private void emailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailActionPerformed

    }//GEN-LAST:event_emailActionPerformed

    private void cephraemailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cephraemailActionPerformed
       javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frame.Phone) {
                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                        phoneFrame.switchPanel(new cephra.Phone.EmailApp());
                        break;
                    }
                }
            }
        });
    }//GEN-LAST:event_cephraemailActionPerformed

    private void BackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BackActionPerformed
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
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
    }//GEN-LAST:event_BackActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Back;
    private javax.swing.JButton cephraemail;
    private javax.swing.JButton contactsup;
    private javax.swing.JTextField email;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel otpPreviewLabel;
    // End of variables declaration//GEN-END:variables
    
    // Custom variables
   
}
