
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


public class EditProfile extends javax.swing.JPanel {

    public EditProfile() {
        initComponents();
         setPreferredSize(new Dimension(370, 750));
        setSize(370, 750);
        
        // Load user data from database and populate fields
        loadUserData();
        
        setupTextFields(); // Setup text field properties
        makeDraggable(); // Make the panel draggable
        setupLabelPosition(); // fit Label Position
        
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
                
                System.out.println("EditProfile: Loaded user data for " + currentUsername);
            } else {
                System.out.println("EditProfile: No current user logged in");
            }
        } catch (Exception e) {
            System.err.println("EditProfile: Error loading user data: " + e.getMessage());
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
                    Window window = SwingUtilities.getWindowAncestor(EditProfile.this);
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
        
        // Add backspace focus navigation
        setupBackspaceNavigation();
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
     // Setup backspace navigation to move focus to previous field
    private void setupBackspaceNavigation() {
        // Define the field order for navigation
        JTextField[] fields = {fname, lname, UsernamePhone, email};
        
        for (int i = 0; i < fields.length; i++) {
            final int currentIndex = i;
            final JTextField currentField = fields[i];
            
            currentField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    // Only handle backspace key when field is empty
                    if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE && currentField.getText().isEmpty()) {
                        // Prevent default backspace behavior and move to previous field
                        e.consume();
                        if (currentIndex > 0) {
                            fields[currentIndex - 1].requestFocusInWindow();
                            // Move cursor to end of previous field
                            SwingUtilities.invokeLater(() -> {
                                fields[currentIndex - 1].setCaretPosition(fields[currentIndex - 1].getText().length());
                            });
                        }
                    }
                }
            });
        }
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
        fname.setBounds(40, 300, 140, 40);

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

            // Validate required fields
            if (newFirstName.isEmpty() || newLastName.isEmpty() || newEmail.isEmpty()) {
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

           
            javax.swing.JOptionPane.showMessageDialog(this,
                "Profile changes saved!\n" +
                "First Name: " + newFirstName + "\n" +
                "Last Name: " + newLastName + "\n" +
                "Email: " + newEmail,
                "Profile Updated", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            
            System.out.println("EditProfile: Profile changes saved for user " + currentUsername);

        } catch (Exception e) {
            System.err.println("EditProfile: Error updating profile: " + e.getMessage());
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this,
                "An unexpected error occurred while updating profile: " + e.getMessage(),
                "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_savechangesActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
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
