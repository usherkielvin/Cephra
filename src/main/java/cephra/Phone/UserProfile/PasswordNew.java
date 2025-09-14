package cephra.Phone.UserProfile;

import javax.swing.SwingUtilities;

public class PasswordNew extends javax.swing.JPanel {

    public PasswordNew() {
        initComponents();
        setPreferredSize(new java.awt.Dimension(370, 750));
        setSize(370, 750);
        pass.setOpaque(false);
        pass.setBackground(new java.awt.Color(0, 0, 0, 0));
        pass1.setOpaque(false);
        pass1.setBackground(new java.awt.Color(0, 0, 0, 0));
        setupLabelPosition(); // Set label position
        setupButtons(); // Call our custom setup method
    }

   
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Back_Button = new javax.swing.JButton();
        pass1 = new javax.swing.JPasswordField();
        pass = new javax.swing.JPasswordField();
        See1 = new javax.swing.JButton();
        See = new javax.swing.JButton();
        Back = new javax.swing.JButton();
        contactsup = new javax.swing.JButton();
        update = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setLayout(null);

        Back_Button.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        Back_Button.setText("back to login");
        Back_Button.setBorder(null);
        Back_Button.setBorderPainted(false);
        Back_Button.setContentAreaFilled(false);
        Back_Button.setFocusPainted(false);
        Back_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Back_ButtonActionPerformed(evt);
            }
        });
        add(Back_Button);
        Back_Button.setBounds(140, 460, 120, 23);

        pass1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        pass1.setBorder(null);
        pass1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pass1ActionPerformed(evt);
            }
        });
        add(pass1);
        pass1.setBounds(90, 350, 220, 35);

        pass.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        pass.setBorder(null);
        pass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passActionPerformed(evt);
            }
        });
        add(pass);
        pass.setBounds(90, 290, 220, 35);

        See1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/EyeClose.png"))); // NOI18N
        See1.setBorderPainted(false);
        See1.setContentAreaFilled(false);
        See1.setFocusPainted(false);
        See1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                See1ActionPerformed(evt);
            }
        });
        add(See1);
        See1.setBounds(300, 350, 30, 40);

        See.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/EyeClose.png"))); // NOI18N
        See.setBorderPainted(false);
        See.setContentAreaFilled(false);
        See.setFocusPainted(false);
        See.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SeeActionPerformed(evt);
            }
        });
        add(See);
        See.setBounds(300, 290, 30, 40);

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

        contactsup.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        contactsup.setForeground(new java.awt.Color(0, 204, 204));
        contactsup.setText("Contact Support");
        contactsup.setBorder(null);
        contactsup.setBorderPainted(false);
        contactsup.setContentAreaFilled(false);
        contactsup.setFocusPainted(false);
        add(contactsup);
        contactsup.setBounds(160, 663, 130, 30);

        update.setBorder(null);
        update.setBorderPainted(false);
        update.setContentAreaFilled(false);
        update.setFocusPainted(false);
        update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateActionPerformed(evt);
            }
        });
        add(update);
        update.setBounds(50, 410, 300, 40);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/newpass.png"))); // NOI18N
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

    // Add button functionality after initComponents to prevent NetBeans from removing it
    private void setupButtons() {
        // Setup Back button (only hover effects, action listener already set in initComponents)
        Back.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Back.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                Back.setForeground(new java.awt.Color(0, 255, 255));
                Back.setText("<html><u>Back</u></html>");
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                Back.setForeground(new java.awt.Color(0, 204, 204));
                Back.setText("Back");
            }
        });

        // Setup Contact Support button (only hover effects, action listener already set in initComponents)
        contactsup.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        contactsup.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                contactsup.setForeground(new java.awt.Color(0, 255, 255));
                contactsup.setText("<html><u>Contact Support</u></html>");
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                contactsup.setForeground(new java.awt.Color(0, 204, 204));
                contactsup.setText("Contact Support");
            }
        });

        // Setup Update button (only hover effects, action listener already set in initComponents)
        update.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        update.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                update.setForeground(new java.awt.Color(0, 255, 255));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                update.setForeground(new java.awt.Color(0, 204, 204));
            }
        });
    }

    @Override
    public void addNotify() {
        super.addNotify();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                setupLabelPosition(); // Set label position
                setupButtons(); // Setup buttons when panel is added to container
                if (pass != null) {
                    pass.requestFocusInWindow();
                }
                javax.swing.JRootPane root = javax.swing.SwingUtilities.getRootPane(PasswordNew.this);
                if (root != null && update != null) {
                    root.setDefaultButton(update);
                }
            }
        });
    }

    private void updateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateActionPerformed
        // Check if passwords match
        String password = new String(pass.getPassword());
        String password2 = new String(pass1.getPassword());

        if (password.equals(password2) && !password.isEmpty()) {
            String userEmail = cephra.Phone.Utilities.AppSessionState.userEmailForReset;
            
            // Check if new password is the same as old password
            String oldPassword = cephra.Database.CephraDB.getUserPasswordByEmail(userEmail);
            if (oldPassword != null && password.equals(oldPassword)) {
                javax.swing.JOptionPane.showMessageDialog(this, 
                    "New password cannot be the same as the old password!", 
                    "Error", 
                    javax.swing.JOptionPane.ERROR_MESSAGE);
                pass.setText("");
                pass1.setText("");
                pass.requestFocusInWindow();
                return;
            }
            
            if (userEmail != null && cephra.Database.CephraDB.updateUserPassword(userEmail, password)) {
                javax.swing.JOptionPane.showMessageDialog(this, "Password changed successfully!", "Success", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                // Reset the email for the next session
                cephra.Phone.Utilities.AppSessionState.userEmailForReset = null;
                // Navigate to Phonelogin after OK is clicked
                javax.swing.SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        java.awt.Window[] windows = java.awt.Window.getWindows();
                        for (java.awt.Window window : windows) {
                            if (window instanceof cephra.Frame.Phone) {
                                cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                                phoneFrame.switchPanel(new cephra.Phone.UserProfile.Phonelogin());
                                break;
                            }
                        }
                    }
                });
            } else {
                javax.swing.JOptionPane.showMessageDialog(this, "An error occurred while updating the password.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "Passwords do not match or are empty!", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            pass.setText("");
            pass1.setText("");
            pass.requestFocusInWindow();
        }
    }//GEN-LAST:event_updateActionPerformed

    private void BackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BackActionPerformed
      
        
          SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    java.awt.Window[] windows = java.awt.Window.getWindows();
                    for (java.awt.Window window : windows) {
                        if (window instanceof cephra.Frame.Phone) {
                            cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                            phoneFrame.switchPanel(new cephra.Phone.UserProfile.Phonelogin());
                            break;
                        }
                    }
                }
         });
        
        
        
    }//GEN-LAST:event_BackActionPerformed

    private void SeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SeeActionPerformed

        if(pass.getEchoChar() == 0) {
            pass.setEchoChar('•');
            See.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/EyeClose.png")));
        } else {
            See.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/EyeOpen.png")));
            pass.setEchoChar((char) 0 );
        }
    }//GEN-LAST:event_SeeActionPerformed

    private void See1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_See1ActionPerformed
        if(pass1.getEchoChar() == 0) {
            pass1.setEchoChar('•');
            See1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/EyeClose.png")));
        } else {
            See1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/EyeOpen.png")));
            pass1.setEchoChar((char) 0 );
        }
    }//GEN-LAST:event_See1ActionPerformed

    private void passActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passActionPerformed
        // When Enter key is pressed on password field, move to next field
        pass1.requestFocusInWindow();
    }//GEN-LAST:event_passActionPerformed

    private void pass1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pass1ActionPerformed
        // When Enter key is pressed on confirm password field, attempt update
        update.doClick();
    }//GEN-LAST:event_pass1ActionPerformed

    private void Back_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Back_ButtonActionPerformed

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frame.Phone) {
                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                        phoneFrame.switchPanel(new cephra.Phone.UserProfile.Phonelogin());
                        break;
                    }
                }
            }
        });

    }//GEN-LAST:event_Back_ButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Back;
    private javax.swing.JButton Back_Button;
    private javax.swing.JButton See;
    private javax.swing.JButton See1;
    private javax.swing.JButton contactsup;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPasswordField pass;
    private javax.swing.JPasswordField pass1;
    private javax.swing.JButton update;
    // End of variables declaration//GEN-END:variables
}
