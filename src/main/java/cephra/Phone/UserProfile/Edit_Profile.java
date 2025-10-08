
package cephra.Phone.UserProfile;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;


public class Edit_Profile extends javax.swing.JPanel {

    public Edit_Profile() {
        initComponents();
         setPreferredSize(new Dimension(370, 750));
        setSize(370, 750);
        
        // Load user data from database and populate fields
        loadUserData();
        
        setupTextFields(); // Setup text field properties
        makeDraggable(); // Make the panel draggable
        setupLabelPosition(); // fit Label Position
        
        // Load and display initial profile picture
        loadInitialProfilePicture();

        
    }
    
    private void loadUserData() {
        try {
            // Get current logged-in user
            String currentUsername = cephra.Database.CephraDB.getCurrentPhoneUsername();
            if (currentUsername != null && !currentUsername.trim().isEmpty()) {
                // Load user data from database
                String firstName = cephra.Database.CephraDB.getCurrentFirstname();
                String lastName = cephra.Database.CephraDB.getCurrentLastname();
                String userEmail = cephra.Database.CephraDB.getCurrentEmail();
                
                // Populate text fields with user data
                if (fname != null) fname.setText(firstName != null ? firstName : "");
                if (lname != null) lname.setText(lastName != null ? lastName : "");
                if (UsernamePhone != null) UsernamePhone.setText(currentUsername != null ? currentUsername : "");
                if (email != null) email.setText(userEmail != null ? userEmail : "");
                
            }
        } catch (Exception e) {
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
                    Window window = SwingUtilities.getWindowAncestor(Edit_Profile.this);
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
      // Setup text field properties
    private void setupTextFields() {
        // Make text fields transparent
        fname.setOpaque(false);
        fname.setBackground(new Color(0, 0, 0, 0));
        email.setOpaque(false);
        email.setBackground(new Color(0, 0, 0, 0));
        lname.setOpaque(false);
        lname.setBackground(new Color(0, 0, 0, 0));
        UsernamePhone.setOpaque(false);
        UsernamePhone.setBackground(new Color(0, 0, 0, 0));
        
        // Add auto-capitalization to name fields
        setupAutoCapitalization(fname);
        setupAutoCapitalization(lname);
        
        // Backspace navigation removed as requested
    }
     // Setup auto-capitalization for text fields
    private void setupAutoCapitalization(JTextField textField) {
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                // Don't process if user is still typing (avoid interfering with spaces)
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    return; // Let space be added naturally
                }
                
                String text = textField.getText();
                if (!text.isEmpty()) {
                    // Capitalize first letter of each word (supports multiple names)
                    String[] words = text.split("\\s+");
                    StringBuilder capitalized = new StringBuilder();
                    for (int i = 0; i < words.length; i++) {
                        if (!words[i].isEmpty()) {
                            // Capitalize first letter of each word
                            capitalized.append(words[i].substring(0, 1).toUpperCase());
                            if (words[i].length() > 1) {
                                // Make rest of the word lowercase
                                capitalized.append(words[i].substring(1).toLowerCase());
                            }
                        }
                        // Add space between words (except after the last word)
                        if (i < words.length - 1) {
                            capitalized.append(" ");
                        }
                    }
                    
                    // Only update if the text has actually changed to avoid cursor jumping
                    String newText = capitalized.toString();
                    if (!newText.equals(text)) {
                        int caretPos = textField.getCaretPosition();
                        textField.setText(newText);
                        // Maintain cursor position or move to end if at end
                        if (caretPos >= newText.length()) {
                            textField.setCaretPosition(newText.length());
                        } else {
                            textField.setCaretPosition(caretPos);
                        }
                    }
                }
            }
        });
    }

    
    // Ensure the background label (PNG) stays positioned correctly
    private void setupLabelPosition() {
        if (bg != null) {
            bg.setBounds(0, 0, 370, 750);
        }
    }
    @Override
    public void addNotify() {
        super.addNotify();
        SwingUtilities.invokeLater(() -> setupLabelPosition());
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
        back = new javax.swing.JButton();
        Frame = new javax.swing.JLabel();
        Profile = new javax.swing.JLabel();
        bg = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(370, 750));
        setLayout(null);

        fname.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        fname.setBorder(null);
        fname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fnameActionPerformed(evt);
            }
        });
        add(fname);
        fname.setBounds(40, 305, 140, 32);

        editpfp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/cam.png"))); // NOI18N
        editpfp.setBorder(null);
        editpfp.setBorderPainted(false);
        editpfp.setContentAreaFilled(false);
        editpfp.setFocusPainted(false);
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
        savechanges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                savechangesActionPerformed(evt);
            }
        });
        add(savechanges);
        savechanges.setBounds(25, 510, 310, 50);

        lname.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        lname.setBorder(null);
        lname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lnameActionPerformed(evt);
            }
        });
        add(lname);
        lname.setBounds(200, 305, 120, 32);

        UsernamePhone.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        UsernamePhone.setBorder(null);
        UsernamePhone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UsernamePhoneActionPerformed(evt);
            }
        });
        add(UsernamePhone);
        UsernamePhone.setBounds(40, 378, 280, 32);

        email.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        email.setBorder(null);
        email.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailActionPerformed(evt);
            }
        });
        add(email);
        email.setBounds(40, 452, 280, 31);

        back.setBorder(null);
        back.setBorderPainted(false);
        back.setContentAreaFilled(false);
        back.setFocusPainted(false);
        back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backActionPerformed(evt);
            }
        });
        add(back);
        back.setBounds(30, 50, 40, 40);

        Frame.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/Profileframe.png"))); // NOI18N
        add(Frame);
        Frame.setBounds(126, 125, 120, 120);
        add(Profile);
        Profile.setBounds(130, 130, 110, 110);

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
                    java.io.File selectedFile = cephra.Phone.Utilities.SimpleDragDropSelector.showDialog(Edit_Profile.this);
                    if (selectedFile == null) {
                        return; // User cancelled file selection
                    }


                    // Step 2: Open crop dialog
                    java.awt.image.BufferedImage croppedImage = cephra.Phone.Utilities.ImageUtils.openCropDialog(Edit_Profile.this, selectedFile);
                    if (croppedImage == null) {
                        return; // User cancelled cropping
                    }


                    // Step 3: Convert to Base64 and save to database
                    String base64Image = cephra.Phone.Utilities.ImageUtils.imageToBase64(croppedImage, "png");
                    if (base64Image == null) {
                        javax.swing.JOptionPane.showMessageDialog(Edit_Profile.this,
                            "Failed to process the cropped image.",
                            "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Step 4: Save to database
                    String currentUsername = cephra.Database.CephraDB.getCurrentUsername();
                    if (currentUsername == null || currentUsername.trim().isEmpty()) {
                        javax.swing.JOptionPane.showMessageDialog(Edit_Profile.this,
                            "No user is currently logged in.",
                            "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    boolean saved = cephra.Database.CephraDB.saveUserProfilePicture(currentUsername, base64Image);
                    if (saved) {
                        // Step 5: Profile picture saved successfully
                        // Update the profile image display immediately
                        updateProfileImageDisplay();
                        
                        javax.swing.JOptionPane.showMessageDialog(Edit_Profile.this,
                            "Profile picture updated successfully!",
                            "Success", javax.swing.JOptionPane.INFORMATION_MESSAGE);

                    } else {
                        javax.swing.JOptionPane.showMessageDialog(Edit_Profile.this,
                            "Failed to save profile picture to database.",
                            "Database Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    javax.swing.JOptionPane.showMessageDialog(Edit_Profile.this,
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

    private void backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backActionPerformed
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
    }//GEN-LAST:event_backActionPerformed

    private void savechangesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_savechangesActionPerformed
        try {
            // Get current user
            String currentUsername = cephra.Database.CephraDB.getCurrentPhoneUsername();
            if (currentUsername == null || currentUsername.trim().isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(this,
                    "No user is currently logged in.",
                    "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Get updated data from text fields
            String newFirstName = fname.getText().trim();
            String newLastName = lname.getText().trim();
            String newEmail = email.getText().trim();
            String newUsername = UsernamePhone.getText().trim();

            // Validate required fields
            if (newFirstName.isEmpty() || newLastName.isEmpty() || newEmail.isEmpty() || newUsername.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(this,
                    "Please fill in all required fields.",
                    "Validation Error", javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Validate email format
            if (!newEmail.contains("@") || !newEmail.contains(".")) {
                javax.swing.JOptionPane.showMessageDialog(this,
                    "Please enter a valid email address.",
                    "Validation Error", javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Check username availability if username is being changed
            if (!newUsername.equals(currentUsername) && !cephra.Database.CephraDB.isUsernameAvailable(newUsername)) {
                javax.swing.JOptionPane.showMessageDialog(this,
                    "Username is already taken. Please choose a different username.",
                    "Validation Error", javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Update user data in database
            boolean success = updateUserProfile(currentUsername, newUsername, newFirstName, newLastName, newEmail);
            
            if (success) {
                javax.swing.JOptionPane.showMessageDialog(this,
                    "Profile updated successfully!",
                    "Success", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            } else {
                javax.swing.JOptionPane.showMessageDialog(this,
                    "Failed to update profile. Please try again.",
                    "Update Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this,
                "An unexpected error occurred while updating profile: " + e.getMessage(),
                "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_savechangesActionPerformed

    private boolean updateUserProfile(String currentUsername, String newUsername, String firstName, String lastName, String email) {
        try {
            // Update user profile in database
            return cephra.Database.CephraDB.updateUserProfile(currentUsername, newUsername, firstName, lastName, email);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Loads and displays the initial profile picture when the panel is created
     */
    private void loadInitialProfilePicture() {
        try {
            String profilePictureBase64 = cephra.Database.CephraDB.getCurrentUserProfilePicture();
            
            if (profilePictureBase64 != null && !profilePictureBase64.trim().isEmpty()) {
                // Handle different profile picture formats
                java.awt.image.BufferedImage profileImage = null;
                
                if (profilePictureBase64.startsWith("data:image")) {
                    // Data URI format: data:image/png;base64,{base64string}
                    int commaIndex = profilePictureBase64.indexOf(',');
                    if (commaIndex != -1) {
                        String base64Data = profilePictureBase64.substring(commaIndex + 1);
                        profileImage = cephra.Phone.Utilities.ImageUtils.base64ToImage(base64Data);
                    }
                } else if (profilePictureBase64.startsWith("iVBORw0KGgo")) {
                    // Raw Base64 format: {base64string}
                    profileImage = cephra.Phone.Utilities.ImageUtils.base64ToImage(profilePictureBase64);
                } else if (profilePictureBase64.endsWith(".jpg") || profilePictureBase64.endsWith(".jpeg") || 
                          profilePictureBase64.endsWith(".png") || profilePictureBase64.endsWith(".gif")) {
                    // File path format: filename.jpg (from web uploads)
                    // Skip loading file path images in Java app - they're for web only
                    System.out.println("Edit_Profile: Skipping file path profile picture: " + profilePictureBase64);
                    profileImage = null;
                } else {
                    // Try as raw Base64 (fallback)
                    profileImage = cephra.Phone.Utilities.ImageUtils.base64ToImage(profilePictureBase64);
                }
                
                if (profileImage != null) {
                    java.awt.image.BufferedImage circularImage = cephra.Phone.Utilities.ImageUtils.createCircularImage(profileImage, 110);
                    Profile.setIcon(new javax.swing.ImageIcon(circularImage));
                } else {
                    // No profile image loaded (could be file path format or invalid data)
                    System.out.println("No profile image to display, using default");
                    setDefaultProfileImage();
                }
            } else {
                System.out.println("No profile picture found, using default");
                setDefaultProfileImage();
            }
        } catch (Exception e) {
            System.err.println("Error loading initial profile picture: " + e.getMessage());
            setDefaultProfileImage();
        }
    }
    
    /**
     * Sets a default profile image when no image is available
     */
    private void setDefaultProfileImage() {
        try {
            // Create a simple default circular image
            java.awt.image.BufferedImage defaultImage = new java.awt.image.BufferedImage(110, 110, java.awt.image.BufferedImage.TYPE_INT_RGB);
            java.awt.Graphics2D g2d = defaultImage.createGraphics();
            g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Fill with light gray background
            g2d.setColor(new java.awt.Color(240, 240, 240));
            g2d.fillOval(0, 0, 110, 110);
            
            // Add a simple user icon outline
            g2d.setColor(new java.awt.Color(180, 180, 180));
            g2d.setStroke(new java.awt.BasicStroke(2));
            g2d.drawOval(35, 25, 40, 40); // Head circle
            g2d.drawArc(20, 70, 70, 50, 0, 180); // Body arc
            
            g2d.dispose();
            
          //  Profile.setIcon(new javax.swing.ImageIcon(defaultImage));
        } catch (Exception e) {
            System.err.println("Error creating default profile image: " + e.getMessage());
        }
    }
    
    /**
     * Updates the profile image display with the latest image from database
     */
    private void updateProfileImageDisplay() {
        try {
            // Get the updated profile picture from database
            String profilePictureBase64 = cephra.Database.CephraDB.getCurrentUserProfilePicture();
            
            if (profilePictureBase64 != null && !profilePictureBase64.trim().isEmpty()) {
                // Extract Base64 part from data URI if it's in data URI format
                String base64Data = profilePictureBase64;
                if (profilePictureBase64.startsWith("data:image")) {
                    int commaIndex = profilePictureBase64.indexOf(',');
                    if (commaIndex != -1) {
                        base64Data = profilePictureBase64.substring(commaIndex + 1);
                    }
                }
                // If it's already raw Base64 (starts with iVBORw0KGgo), use it as is
                // Convert base64 to image
                java.awt.image.BufferedImage profileImage = cephra.Phone.Utilities.ImageUtils.base64ToImage(base64Data);
                
                if (profileImage != null) {
                    // Create circular image for display
                    java.awt.image.BufferedImage circularImage = cephra.Phone.Utilities.ImageUtils.createCircularImage(profileImage, 110);
                    
                    // Update the Profile label with new image
                    SwingUtilities.invokeLater(() -> {
                        Profile.setIcon(new javax.swing.ImageIcon(circularImage));
                        Profile.revalidate();
                        Profile.repaint();
                    });
                    
                    System.out.println("Profile image updated successfully");
                } else {
                    System.err.println("Failed to convert base64 to image");
                }
            } else {
                System.err.println("No profile picture found in database after update");
            }
            
        } catch (Exception e) {
            System.err.println("Error updating profile image display: " + e.getMessage());
            e.printStackTrace();
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Frame;
    private javax.swing.JLabel Profile;
    private javax.swing.JTextField UsernamePhone;
    private javax.swing.JButton back;
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
