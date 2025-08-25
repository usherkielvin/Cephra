/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package cephra.Phone;

/**
 *
 * @author usher
 */
public class NewPassword extends javax.swing.JPanel {

    /**
     * Creates new form NewPassword
     */
    public NewPassword() {
        initComponents();
        setPreferredSize(new java.awt.Dimension(350, 750));
        setSize(350, 750);
        newpass.setOpaque(false);
        newpass.setBackground(new java.awt.Color(0, 0, 0, 0));
        newpass1.setOpaque(false);
        newpass1.setBackground(new java.awt.Color(0, 0, 0, 0));
        setupLabelPosition(); // Set label position
        setupButtons(); // Call our custom setup method
    }

   
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        newpass1 = new javax.swing.JTextField();
        newpass = new javax.swing.JTextField();
        Back = new javax.swing.JButton();
        contactsup = new javax.swing.JButton();
        update = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setLayout(null);

        newpass1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        newpass1.setBorder(null);
        newpass1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newpass1ActionPerformed(evt);
            }
        });
        add(newpass1);
        newpass1.setBounds(40, 380, 290, 40);

        newpass.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        newpass.setBorder(null);
        newpass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newpassActionPerformed(evt);
            }
        });
        add(newpass);
        newpass.setBounds(40, 310, 290, 40);

        Back.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Back.setForeground(new java.awt.Color(0, 204, 204));
        Back.setText("Back");
        Back.setBorder(null);
        Back.setBorderPainted(false);
        Back.setContentAreaFilled(false);
        Back.setFocusPainted(false);
        Back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backActionPerformed(evt);
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
        contactsup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                contactsupActionPerformed(evt);
            }
        });
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
        update.setBounds(50, 440, 270, 50);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Photos/newpass.png"))); // NOI18N
        add(jLabel1);
        // Label position will be set in setupLabelPosition() method
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
                if (newpass != null) {
                    newpass.requestFocusInWindow();
                }
                javax.swing.JRootPane root = javax.swing.SwingUtilities.getRootPane(NewPassword.this);
                if (root != null && update != null) {
                    root.setDefaultButton(update);
                }
            }
        });
    }

    private void updateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateActionPerformed
        // Check if passwords match
        String password1 = newpass.getText();
        String password2 = newpass1.getText();
        
        if (password1.equals(password2) && !password1.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Password changed successfully!", "Success", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            
            // Navigate to Phonelogin after OK is clicked
            javax.swing.SwingUtilities.invokeLater(new Runnable() {
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
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "Passwords do not match or are empty!", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            newpass.setText("");
            newpass1.setText("");
            newpass.requestFocusInWindow();
        }
    }//GEN-LAST:event_updateActionPerformed

    private void newpassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newpassActionPerformed
        newpass1.requestFocusInWindow(); // Move focus to confirm password field
    }//GEN-LAST:event_newpassActionPerformed

    private void newpass1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newpass1ActionPerformed
        updateActionPerformed(evt); // Trigger update when Enter is pressed on confirm password field
    }//GEN-LAST:event_newpass1ActionPerformed

    private void backActionPerformed(java.awt.event.ActionEvent evt) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frame.Phone) {
                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                        phoneFrame.switchPanel(new cephra.Phone.VerificationCode());
                        break;
                    }
                }
            }
        });
    }

    private void contactsupActionPerformed(java.awt.event.ActionEvent evt) {
        javax.swing.JOptionPane.showMessageDialog(this, "Contact support at: support@cephra.com", "Contact Support", javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Back;
    private javax.swing.JButton contactsup;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTextField newpass;
    private javax.swing.JTextField newpass1;
    private javax.swing.JButton update;
    // End of variables declaration//GEN-END:variables
}
