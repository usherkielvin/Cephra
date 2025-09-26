package cephra.Phone.Dashboard;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.SwingUtilities;

public class EditProfile extends javax.swing.JPanel {

    public EditProfile() {
        initComponents();
        setPreferredSize(new java.awt.Dimension(370, 750));
        setSize(370, 750);
      
        makeDraggable();
        
       
        
        // Set the labels
        
        // Load and display current profile picture
        loadCurrentProfilePicture();
    }
    
    /**
     * Loads and displays the current user's profile picture in the edit view
     */
    private void loadCurrentProfilePicture() {
        try {
            String profilePictureBase64 = cephra.Database.CephraDB.getCurrentUserProfilePicture();
            if (profilePictureBase64 != null && !profilePictureBase64.trim().isEmpty()) {
                java.awt.image.BufferedImage profileImage = cephra.Phone.Utilities.ImageUtils.base64ToImage(profilePictureBase64);
                if (profileImage != null) {
                    // Create a circular profile picture that fits the Profile label (110x110)
                    java.awt.image.BufferedImage circularImage = cephra.Phone.Utilities.ImageUtils.createCircularImage(profileImage, 110);
                    
                    if (Profile != null) {
                        Profile.setIcon(new javax.swing.ImageIcon(circularImage));
                        System.out.println("EditProfile: Loaded current profile picture");
                    }
                } else {
                    System.err.println("EditProfile: Failed to decode profile picture from Base64");
                }
            } else {
                // No profile picture set, clear the label
                if (Profile != null) {
                    Profile.setIcon(null);
                }
                System.out.println("EditProfile: No profile picture found for current user");
            }
        } catch (Exception e) {
            System.err.println("EditProfile: Error loading current profile picture: " + e.getMessage());
            e.printStackTrace();
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
                    java.awt.Window window = SwingUtilities.getWindowAncestor(EditProfile.this);
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

    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        historybutton = new javax.swing.JButton();
        homebutton = new javax.swing.JButton();
        linkbutton = new javax.swing.JButton();
        charge = new javax.swing.JButton();
        Profile = new javax.swing.JLabel();
        addpic = new javax.swing.JButton();

        setMaximumSize(new java.awt.Dimension(370, 750));
        setPreferredSize(new java.awt.Dimension(370, 750));
        setLayout(null);

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
        historybutton.setBounds(230, 683, 30, 40);

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
        homebutton.setBounds(170, 680, 40, 40);

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
        linkbutton.setBounds(120, 680, 30, 40);

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
        charge.setBounds(60, 680, 40, 40);
        add(Profile);
        Profile.setBounds(120, 140, 110, 110);

        addpic.setText("Add Photo");
        addpic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addpicActionPerformed(evt);
            }
        });
        add(addpic);
        addpic.setBounds(110, 330, 120, 23);
    }// </editor-fold>//GEN-END:initComponents

    // CUSTOM CODE - DO NOT REMOVE - NetBeans will regenerate form code but this method should be preserved
    // Setup label position to prevent NetBeans from changing it
  

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


    private void addpicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addpicActionPerformed
       
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    // Step 1: Select image file using simple drag and drop interface
                    java.io.File selectedFile = cephra.Phone.Utilities.SimpleDragDropSelector.showDialog(EditProfile.this);
                    if (selectedFile == null) {
                        System.out.println("EditProfile: No image file selected");
                        return; // User cancelled file selection
                    }
                    
                    System.out.println("EditProfile: Selected image file: " + selectedFile.getName());
                    
                    // Step 2: Open crop dialog
                    java.awt.image.BufferedImage croppedImage = cephra.Phone.Utilities.ImageUtils.openCropDialog(EditProfile.this, selectedFile);
                    if (croppedImage == null) {
                        System.out.println("EditProfile: Image cropping was cancelled");
                        return; // User cancelled cropping
                    }
                    
                    System.out.println("EditProfile: Image cropped successfully");
                    
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
                        // Step 5: Update UI to show new profile picture
                        java.awt.image.BufferedImage circularImage = cephra.Phone.Utilities.ImageUtils.createCircularImage(croppedImage, 110);
                        if (Profile != null) {
                            Profile.setIcon(new javax.swing.ImageIcon(circularImage));
                        }
                        
                        javax.swing.JOptionPane.showMessageDialog(EditProfile.this, 
                            "Profile picture updated successfully!", 
                            "Success", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                        
                        System.out.println("EditProfile: Profile picture saved successfully for user " + currentUsername);
                    } else {
                        javax.swing.JOptionPane.showMessageDialog(EditProfile.this, 
                            "Failed to save profile picture to database.", 
                            "Database Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                        System.err.println("EditProfile: Failed to save profile picture to database");
                    }
                    
                } catch (Exception e) {
                    System.err.println("EditProfile: Error in addpicActionPerformed: " + e.getMessage());
                    e.printStackTrace();
                    javax.swing.JOptionPane.showMessageDialog(EditProfile.this, 
                        "An unexpected error occurred: " + e.getMessage(), 
                        "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }//GEN-LAST:event_addpicActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Profile;
    private javax.swing.JButton addpic;
    private javax.swing.JButton charge;
    private javax.swing.JButton historybutton;
    private javax.swing.JButton homebutton;
    private javax.swing.JButton linkbutton;
    // End of variables declaration//GEN-END:variables

    @Override
    public void addNotify() {
        super.addNotify();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
              
              
            }
        });
    }
}
