package cephra.Phone.UserProfile;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JPasswordField;
import javax.swing.SwingUtilities;

public class PasswordNew extends javax.swing.JPanel {

    public PasswordNew() {
        initComponents();
        setPreferredSize(new Dimension(370, 750));
        setSize(370, 750);
        
        setupPasswordFields(); // Setup password field properties
        makeDraggable(); // Make the panel draggable
        setupLabelPosition(); // Fit Label Position
    }

   
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        See2 = new javax.swing.JButton();
        See1 = new javax.swing.JButton();
        confirmpass = new javax.swing.JPasswordField();
        newpass = new javax.swing.JPasswordField();
        resetsend = new javax.swing.JButton();
        contactsup = new javax.swing.JButton();
        bg = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(370, 750));
        setPreferredSize(new java.awt.Dimension(370, 750));
        setLayout(null);

        See2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/EyeClose.png"))); // NOI18N
        See2.setBorder(null);
        See2.setBorderPainted(false);
        See2.setContentAreaFilled(false);
        See2.setFocusPainted(false);
        See2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                See2ActionPerformed(evt);
            }
        });
        add(See2);
        See2.setBounds(280, 400, 50, 30);

        See1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/EyeClose.png"))); // NOI18N
        See1.setBorder(null);
        See1.setBorderPainted(false);
        See1.setContentAreaFilled(false);
        See1.setFocusPainted(false);
        See1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                See1ActionPerformed(evt);
            }
        });
        add(See1);
        See1.setBounds(280, 307, 50, 30);

        confirmpass.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        confirmpass.setBorder(null);
        confirmpass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmpassActionPerformed(evt);
            }
        });
        add(confirmpass);
        confirmpass.setBounds(50, 397, 240, 35);

        newpass.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        newpass.setBorder(null);
        newpass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newpassActionPerformed(evt);
            }
        });
        add(newpass);
        newpass.setBounds(50, 305, 240, 35);

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
        resetsend.setBounds(40, 463, 290, 40);

        contactsup.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
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

        bg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/PasswordNew.png"))); // NOI18N
        add(bg);
        bg.setBounds(0, 0, 370, 750);
    }// </editor-fold>//GEN-END:initComponents

    // Setup password field properties
    private void setupPasswordFields() {
        JPasswordField[] passwordFields = {newpass, confirmpass};
        
        for (JPasswordField field : passwordFields) {
            // Make password fields transparent
            field.setOpaque(false);
            field.setBackground(new Color(0, 0, 0, 0));
        }
    }
    
    // Ensure the background label (PNG) stays positioned correctly
    private void setupLabelPosition() {
        if (bg != null) {
            bg.setBounds(0, 0, 370, 750);
        }
    }

    private void makeDraggable() {
        final Point[] dragPoint = {null};

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                dragPoint[0] = e.getPoint();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (dragPoint[0] != null) {
                    Window window = SwingUtilities.getWindowAncestor(PasswordNew.this);
                    if (window != null) {
                        Point currentLocation = window.getLocation();
                        window.setLocation(
                            currentLocation.x + e.getX() - dragPoint[0].x,
                            currentLocation.y + e.getY() - dragPoint[0].y
                        );
                    }
                }
            }
        });
    }

    @Override
    public void addNotify() {
        super.addNotify();
        SwingUtilities.invokeLater(() -> {
            setupLabelPosition(); // Set label position
            
            if (newpass != null) {
                newpass.requestFocusInWindow();
            }
            javax.swing.JRootPane root = SwingUtilities.getRootPane(PasswordNew.this);
            if (root != null && resetsend != null) {
                root.setDefaultButton(resetsend);
            }
        });
    }

    private void resetsendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetsendActionPerformed
        // Check if passwords match
        String password = new String(newpass.getPassword());
        String password2 = new String(confirmpass.getPassword());

        if (password.equals(password2) && !password.isEmpty()) {
            String userEmail = cephra.Phone.Utilities.AppSessionState.userEmailForReset;
            
            // Check if new password is the same as old password
            String oldPassword = cephra.Database.CephraDB.getUserPasswordByEmail(userEmail);
            if (oldPassword != null && password.equals(oldPassword)) {
                javax.swing.JOptionPane.showMessageDialog(this, 
                    "New password cannot be the same as the old password!", 
                    "Error", 
                    javax.swing.JOptionPane.ERROR_MESSAGE);
                newpass.setText("");
                confirmpass.setText("");
                newpass.requestFocusInWindow();
                return;
            }
            
            if (userEmail != null && cephra.Database.CephraDB.updateUserPassword(userEmail, password)) {
                javax.swing.JOptionPane.showMessageDialog(this, "Password changed successfully!", "Success", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                // Reset the email for the next session
                cephra.Phone.Utilities.AppSessionState.userEmailForReset = null;
                // Navigate to User_Login after OK is clicked
                javax.swing.SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        java.awt.Window[] windows = java.awt.Window.getWindows();
                        for (java.awt.Window window : windows) {
                            if (window instanceof cephra.Frame.Phone) {
                                cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                                phoneFrame.switchPanel(new cephra.Phone.UserProfile.User_Login());
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
            newpass.setText("");
            confirmpass.setText("");
            newpass.requestFocusInWindow();
        }
    }//GEN-LAST:event_resetsendActionPerformed

    private void See1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_See1ActionPerformed
        if(newpass.getEchoChar() == 0) {
            newpass.setEchoChar('•');
            See1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/EyeClose.png")));
        } else {
            See1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/EyeOpen.png")));
            newpass.setEchoChar((char) 0 );
        }
    }//GEN-LAST:event_See1ActionPerformed

    private void See2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_See2ActionPerformed
        if(confirmpass.getEchoChar() == 0) {
            confirmpass.setEchoChar('•');
            See2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/EyeClose.png")));
        } else {
            See2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/EyeOpen.png")));
            confirmpass.setEchoChar((char) 0 );
        }
    }//GEN-LAST:event_See2ActionPerformed

    private void newpassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newpassActionPerformed
        // When Enter key is pressed on password field, move to next field
        confirmpass.requestFocusInWindow();
    }//GEN-LAST:event_newpassActionPerformed

    private void confirmpassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmpassActionPerformed
        // When Enter key is pressed on confirm password field, attempt update
        resetsend.doClick();
    }//GEN-LAST:event_confirmpassActionPerformed

    private void contactsupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_contactsupActionPerformed
        javax.swing.JOptionPane.showMessageDialog(this, "Contact support at: support@cephra.com", "Contact Support", javax.swing.JOptionPane.INFORMATION_MESSAGE);

    }//GEN-LAST:event_contactsupActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton See1;
    private javax.swing.JButton See2;
    private javax.swing.JLabel bg;
    private javax.swing.JPasswordField confirmpass;
    private javax.swing.JButton contactsup;
    private javax.swing.JPasswordField newpass;
    private javax.swing.JButton resetsend;
    // End of variables declaration//GEN-END:variables
}