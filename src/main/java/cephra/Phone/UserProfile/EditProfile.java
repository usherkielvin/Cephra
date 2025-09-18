
package cephra.Phone.UserProfile;

import javax.swing.SwingUtilities;


public class EditProfile extends javax.swing.JPanel {

    public EditProfile() {
        initComponents();
    }

   
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fname = new javax.swing.JTextField();
        editpfp = new javax.swing.JButton();
        charge = new javax.swing.JButton();
        linkbutton = new javax.swing.JButton();
        homebutton = new javax.swing.JButton();
        historybutton = new javax.swing.JButton();
        profilebutton = new javax.swing.JButton();
        savechanges = new javax.swing.JButton();
        lname = new javax.swing.JTextField();
        UsernamePhone = new javax.swing.JTextField();
        email = new javax.swing.JTextField();
        bg = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(370, 750));
        setLayout(null);

        fname.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        fname.setBorder(null);
        fname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fnameActionPerformed(evt);
            }
        });
        add(fname);
        fname.setBounds(40, 300, 130, 40);

        editpfp.setBorder(null);
        editpfp.setBorderPainted(false);
        editpfp.setContentAreaFilled(false);
        editpfp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editpfpActionPerformed(evt);
            }
        });
        add(editpfp);
        editpfp.setBounds(200, 210, 50, 40);

        charge.setBorder(null);
        charge.setBorderPainted(false);
        charge.setContentAreaFilled(false);
        charge.setFocusPainted(false);
        charge.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chargeActionPerformed(evt);
            }
        });
        add(charge);
        charge.setBounds(50, 680, 40, 40);

        linkbutton.setBorder(null);
        linkbutton.setBorderPainted(false);
        linkbutton.setContentAreaFilled(false);
        linkbutton.setFocusPainted(false);
        linkbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                linkbuttonActionPerformed(evt);
            }
        });
        add(linkbutton);
        linkbutton.setBounds(110, 680, 40, 40);

        homebutton.setBorder(null);
        homebutton.setBorderPainted(false);
        homebutton.setContentAreaFilled(false);
        homebutton.setFocusPainted(false);
        homebutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                homebuttonActionPerformed(evt);
            }
        });
        add(homebutton);
        homebutton.setBounds(170, 680, 30, 40);

        historybutton.setBorder(null);
        historybutton.setBorderPainted(false);
        historybutton.setContentAreaFilled(false);
        historybutton.setFocusPainted(false);
        historybutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                historybuttonActionPerformed(evt);
            }
        });
        add(historybutton);
        historybutton.setBounds(220, 683, 40, 40);

        profilebutton.setBorder(null);
        profilebutton.setBorderPainted(false);
        profilebutton.setContentAreaFilled(false);
        profilebutton.setFocusPainted(false);
        profilebutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                profilebuttonActionPerformed(evt);
            }
        });
        add(profilebutton);
        profilebutton.setBounds(280, 680, 40, 40);

        savechanges.setBorder(null);
        savechanges.setBorderPainted(false);
        savechanges.setContentAreaFilled(false);
        savechanges.setFocusPainted(false);
        add(savechanges);
        savechanges.setBounds(25, 510, 310, 50);

        lname.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        lname.setBorder(null);
        lname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lnameActionPerformed(evt);
            }
        });
        add(lname);
        lname.setBounds(200, 302, 120, 40);

        UsernamePhone.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        UsernamePhone.setBorder(null);
        UsernamePhone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UsernamePhoneActionPerformed(evt);
            }
        });
        add(UsernamePhone);
        UsernamePhone.setBounds(40, 380, 280, 32);

        email.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        email.setBorder(null);
        email.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailActionPerformed(evt);
            }
        });
        add(email);
        email.setBounds(40, 450, 280, 40);

        bg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/EditProfile.png"))); // NOI18N
        add(bg);
        bg.setBounds(0, 0, 370, 750);
    }// </editor-fold>//GEN-END:initComponents

    private void chargeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chargeActionPerformed
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frame.Phone) {
                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                        phoneFrame.switchPanel(new cephra.Phone.Dashboard.ChargingOption());
                        break;
                    }
                }
            }
        });
    }//GEN-LAST:event_chargeActionPerformed

    private void linkbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_linkbuttonActionPerformed
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frame.Phone) {
                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                        phoneFrame.switchPanel(new cephra.Phone.Dashboard.LinkConnect());
                        break;
                    }
                }
            }
        });
    }//GEN-LAST:event_linkbuttonActionPerformed

    private void homebuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_homebuttonActionPerformed
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frame.Phone) {
                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                        phoneFrame.switchPanel(cephra.Phone.Dashboard.Home.getAppropriateHomePanel());
                        break;
                    }
                }
            }
        });
    }//GEN-LAST:event_homebuttonActionPerformed

    private void historybuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_historybuttonActionPerformed
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frame.Phone) {
                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                        phoneFrame.switchPanel(new cephra.Phone.Dashboard.ChargeHistory());
                        break;
                    }
                }
            }
        });
    }//GEN-LAST:event_historybuttonActionPerformed

    private void profilebuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_profilebuttonActionPerformed
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frame.Phone) {
                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                        phoneFrame.switchPanel(new cephra.Phone.Dashboard.Profile());
                        break;
                    }
                }
            }
        });
    }//GEN-LAST:event_profilebuttonActionPerformed

    private void editpfpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editpfpActionPerformed

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    // Step 1: Select image file using simple drag and drop interface
                    java.io.File selectedFile = cephra.Phone.Utilities.SimpleDragDropSelector.showDialog(EditProfile.this);
                    if (selectedFile == null) {
                        System.out.println("Profile: No image file selected");
                        return; // User cancelled file selection
                    }

                    System.out.println("Profile: Selected image file: " + selectedFile.getName());

                    // Step 2: Open crop dialog
                    java.awt.image.BufferedImage croppedImage = cephra.Phone.Utilities.ImageUtils.openCropDialog(EditProfile.this, selectedFile);
                    if (croppedImage == null) {
                        System.out.println("Profile: Image cropping was cancelled");
                        return; // User cancelled cropping
                    }

                    System.out.println("Profile: Image cropped successfully");

                    // Step 3: Convert to Base64 and save to database
                    String base64Image = cephra.Phone.Utilities.ImageUtils.imageToBase64(croppedImage, "png");
                    if (base64Image == null) {
                        javax.swing.JOptionPane.showMessageDialog(EditProfile.this,
                            "Failed to process the cropped image.",
                            "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Step 4: Save to database
                    String currentUsername = cephra.Database.CephraDB.getCurrentUsername();
                    if (currentUsername == null || currentUsername.trim().isEmpty()) {
                        javax.swing.JOptionPane.showMessageDialog(EditProfile.this,
                            "No user is currently logged in.",
                            "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    boolean saved = cephra.Database.CephraDB.saveUserProfilePicture(currentUsername, base64Image);
                    if (saved) {
                        // Step 5: Profile picture saved successfully

                        javax.swing.JOptionPane.showMessageDialog(EditProfile.this,
                            "Profile picture updated successfully!",
                            "Success", javax.swing.JOptionPane.INFORMATION_MESSAGE);

                        System.out.println("Profile: Profile picture saved successfully for user " + currentUsername);
                    } else {
                        javax.swing.JOptionPane.showMessageDialog(EditProfile.this,
                            "Failed to save profile picture to database.",
                            "Database Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                        System.err.println("Profile: Failed to save profile picture to database");
                    }

                } catch (Exception e) {
                    System.err.println("Profile: Error in addpicActionPerformed: " + e.getMessage());
                    e.printStackTrace();
                    javax.swing.JOptionPane.showMessageDialog(EditProfile.this,
                        "An unexpected error occurred: " + e.getMessage(),
                        "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }//GEN-LAST:event_editpfpActionPerformed

    private void fnameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fnameActionPerformed
        lname.requestFocusInWindow(); // Move focus to lastname field
    }//GEN-LAST:event_fnameActionPerformed

    private void lnameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lnameActionPerformed
        UsernamePhone.requestFocusInWindow(); // Move focus to username field
    }//GEN-LAST:event_lnameActionPerformed

    private void UsernamePhoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UsernamePhoneActionPerformed
        email.requestFocusInWindow(); // Move focus to email field
    }//GEN-LAST:event_UsernamePhoneActionPerformed

    private void emailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailActionPerformed
      
    }//GEN-LAST:event_emailActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField UsernamePhone;
    private javax.swing.JLabel bg;
    private javax.swing.JButton charge;
    private javax.swing.JButton editpfp;
    private javax.swing.JTextField email;
    private javax.swing.JTextField fname;
    private javax.swing.JButton historybutton;
    private javax.swing.JButton homebutton;
    private javax.swing.JButton linkbutton;
    private javax.swing.JTextField lname;
    private javax.swing.JButton profilebutton;
    private javax.swing.JButton savechanges;
    // End of variables declaration//GEN-END:variables
}
