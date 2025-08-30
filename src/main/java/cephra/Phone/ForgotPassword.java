
package cephra.Phone;

import javax.swing.SwingUtilities;

public class ForgotPassword extends javax.swing.JPanel {

  
    public ForgotPassword() {
        initComponents();
        setPreferredSize(new java.awt.Dimension(350, 750));
        setSize(350, 750);
        email.setOpaque(false);
        email.setBackground(new java.awt.Color(0, 0, 0, 0));
        setupLabelPosition();
    }

  
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        contactsup = new javax.swing.JButton();
        Back = new javax.swing.JButton();
        resetsend = new javax.swing.JButton();
        email = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();

        setLayout(null);

        contactsup.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        contactsup.setForeground(new java.awt.Color(0, 204, 204));
        contactsup.setText("Contact Support");
        contactsup.setBorder(null);
        contactsup.setBorderPainted(false);
        contactsup.setContentAreaFilled(false);
        contactsup.setFocusPainted(false);
        contactsup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                contactsupActionPerformed(evt);
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
        Back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BackActionPerformed(evt);
            }
        });
        add(Back);
        Back.setBounds(120, 630, 120, 23);

        resetsend.setBorder(null);
        resetsend.setBorderPainted(false);
        resetsend.setContentAreaFilled(false);
        resetsend.setFocusPainted(false);
        resetsend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetsendActionPerformed(evt);
            }
        });
        add(resetsend);
        resetsend.setBounds(50, 440, 270, 50);

        email.setBorder(null);
        email.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailActionPerformed(evt);
            }
        });
        add(email);
        email.setBounds(80, 330, 250, 30);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Photos/forgot-pass.png"))); // NOI18N
        add(jLabel1);
        jLabel1.setBounds(0, 0, 370, 750);
    }// </editor-fold>//GEN-END:initComponents

    // CUSTOM CODE - DO NOT REMOVE - NetBeans will regenerate form code but this method should be preserved
    // Setup label position to prevent NetBeans from changing it
    private void setupLabelPosition() {
        if (jLabel1 != null) {
            jLabel1.setBounds(-15, 0, 398, 750);
        }
    }

    private void emailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailActionPerformed
       
    }//GEN-LAST:event_emailActionPerformed

    private void resetsendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetsendActionPerformed
        String emailText = email.getText().trim();
if (cephra.CephraDB.findUserByEmail(emailText) != null) {
    cephra.Phone.AppSessionState.userEmailForReset = emailText; // Add this line
    cephra.CephraDB.generateAndStoreOTP();
        
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frame.Phone) {
                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                        phoneFrame.switchPanel(new cephra.Phone.EmailPop());
                        break;
                    }
                }
            }
        });
    } else {
        javax.swing.JOptionPane.showMessageDialog(this, "Email address not found.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_resetsendActionPerformed

    private void BackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BackActionPerformed
      
        
         SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    java.awt.Window[] windows = java.awt.Window.getWindows();
                    for (java.awt.Window window : windows) {
                        if (window instanceof cephra.Frame.Phone) {
                            cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                            phoneFrame.switchPanel(new cephra.Phone.Phonelogin());
                            break;
                        }
                    }
                }
         });
        
        
        
        
        
        
        
    }//GEN-LAST:event_BackActionPerformed

    private void contactsupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_contactsupActionPerformed
        
            javax.swing.JOptionPane.showMessageDialog(this, "Contact support at: support@cephra.com", "Contact Support", javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_contactsupActionPerformed

    @Override
    public void addNotify() {
        super.addNotify();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                setupLabelPosition(); // Set label position
                if (email != null) {
                    email.requestFocusInWindow();
                }
                javax.swing.JRootPane root = javax.swing.SwingUtilities.getRootPane(ForgotPassword.this);
                if (root != null && resetsend != null) {
                    root.setDefaultButton(resetsend);
                }
            }
        });
    }

    public void focusEmailField() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (email != null) {
                    email.requestFocusInWindow();
                }
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Back;
    private javax.swing.JButton contactsup;
    private javax.swing.JTextField email;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton resetsend;
    // End of variables declaration//GEN-END:variables
}
