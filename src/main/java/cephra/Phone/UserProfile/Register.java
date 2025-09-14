package cephra.Phone.UserProfile;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Register extends javax.swing.JPanel {

    public Register() {
        initComponents();
        setPreferredSize(new Dimension(370, 750));
        setSize(370, 750);
     
        setupButtons(); // Setup button hover effects
        setupTextFields(); // Setup text field properties
        makeDraggable(); // Make the panel draggable
        
        // Add action listeners that NetBeans might remove
        lname.addActionListener(evt -> lnameActionPerformed(evt));
        UsernamePhone.addActionListener(evt -> UsernamePhoneActionPerformed(evt));
        
        // Auto-focus on name field
        SwingUtilities.invokeLater(() -> {
            if (fname != null) {
                fname.requestFocusInWindow();
            }
        });
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
                    Window window = SwingUtilities.getWindowAncestor(Register.this);
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

        See = new javax.swing.JButton();
        pass = new javax.swing.JPasswordField();
        email = new javax.swing.JTextField();
        fname = new javax.swing.JTextField();
        register = new javax.swing.JButton();
        termscondition = new javax.swing.JCheckBox();
        loginbutton = new javax.swing.JButton();
        UsernamePhone = new javax.swing.JTextField();
        lname = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(370, 750));
        setPreferredSize(new java.awt.Dimension(370, 750));
        setLayout(null);

        See.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/EyeClose.png"))); // NOI18N
        See.setBorder(null);
        See.setBorderPainted(false);
        See.setContentAreaFilled(false);
        See.setFocusPainted(false);
        See.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SeeActionPerformed(evt);
            }
        });
        add(See);
        See.setBounds(300, 470, 50, 20);

        pass.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        pass.setBorder(null);
        pass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passActionPerformed(evt);
            }
        });
        add(pass);
        pass.setBounds(60, 460, 250, 35);

        email.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        email.setBorder(null);
        email.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailActionPerformed(evt);
            }
        });
        add(email);
        email.setBounds(65, 402, 280, 32);

        fname.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        fname.setBorder(null);
        fname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fnameActionPerformed(evt);
            }
        });
        add(fname);
        fname.setBounds(60, 280, 130, 30);

        register.setBorder(null);
        register.setBorderPainted(false);
        register.setContentAreaFilled(false);
        register.setFocusPainted(false);
        register.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registerActionPerformed(evt);
            }
        });
        add(register);
        register.setBounds(50, 520, 310, 50);

        termscondition.setBackground(new java.awt.Color(255, 255, 255));
        termscondition.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        termscondition.setText("I Agree to the Terms and Conditions");
        termscondition.setBorder(null);
        termscondition.setContentAreaFilled(false);
        termscondition.setFocusPainted(false);
        termscondition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                termsconditionActionPerformed(evt);
            }
        });
        add(termscondition);
        termscondition.setBounds(80, 490, 280, 40);

        loginbutton.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        loginbutton.setForeground(new java.awt.Color(0, 204, 204));
        loginbutton.setText("Sign in here");
        loginbutton.setBorder(null);
        loginbutton.setBorderPainted(false);
        loginbutton.setContentAreaFilled(false);
        loginbutton.setFocusPainted(false);
        loginbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginbuttonActionPerformed(evt);
            }
        });
        add(loginbutton);
        loginbutton.setBounds(220, 670, 90, 50);

        UsernamePhone.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        UsernamePhone.setBorder(null);
        UsernamePhone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UsernamePhoneActionPerformed(evt);
            }
        });
        add(UsernamePhone);
        UsernamePhone.setBounds(45, 340, 280, 32);

        lname.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        lname.setBorder(null);
        lname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lnameActionPerformed(evt);
            }
        });
        add(lname);
        lname.setBounds(200, 279, 120, 32);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/REGISTER.png"))); // NOI18N
        add(jLabel1);
        jLabel1.setBounds(0, -4, 370, 760);
    }// </editor-fold>//GEN-END:initComponents

    // Setup button hover effects
    private void setupButtons() {
        // Setup Register button hover effects
        register.setCursor(new Cursor(Cursor.HAND_CURSOR));
        register.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                register.setForeground(new Color(0, 204, 204));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                register.setForeground(new Color(0, 0, 0));
            }
        });

        // Setup Login button hover effects
        loginbutton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginbutton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginbutton.setForeground(new Color(0, 150, 150)); // Darker cyan on hover
                loginbutton.setText("<html><u>Sign in here</u></html>");
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginbutton.setForeground(new Color(0, 204, 204)); // Original cyan color
                loginbutton.setText("Sign in here");
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
        pass.setOpaque(false);
        pass.setBackground(new Color(0, 0, 0, 0));
        lname.setOpaque(false);
        lname.setBackground(new Color(0, 0, 0, 0));
        UsernamePhone.setOpaque(false);
        UsernamePhone.setBackground(new Color(0, 0, 0, 0));
        
        // Add auto-capitalization to name fields
        setupAutoCapitalization(fname);
        setupAutoCapitalization(lname);
        
        // Add auto-fill functionality
        setupAutoFillUsername();
        setupEmailDomainCompletion();
        
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
    
    // Setup auto-fill username from first name + last name
    private void setupAutoFillUsername() {
        // Add focus listener to last name field to auto-fill username
        lname.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                String firstName = fname.getText().trim();
                String lastName = lname.getText().trim();
                
                if (!firstName.isEmpty() && !lastName.isEmpty()) {
                    // Combine first name + last name in lowercase (removes spaces)
                    String suggestedUsername = (firstName + lastName).replaceAll("\\s+", "").toLowerCase();
                    UsernamePhone.setText(suggestedUsername);
                }
            }
        });
    }
    
    // Setup email domain auto-completion
    private void setupEmailDomainCompletion() {
        email.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String text = email.getText();
                int caretPos = email.getCaretPosition();
                
                // Check if user typed @g and suggest gmail.com
                if (text.endsWith("@g") && caretPos == text.length()) {
                    email.setText(text + "mail.com");
                    email.setCaretPosition(email.getText().length()); // Position cursor at end
                }
                // Check if user typed @c and suggest cephra.com
                else if (text.endsWith("@c") && caretPos == text.length()) {
                    email.setText(text + "ephra.com");
                    email.setCaretPosition(email.getText().length()); // Position cursor at end
                }
            }
        });
    }
    
    // Setup backspace navigation to move focus to previous field
    private void setupBackspaceNavigation() {
        // Define the field order for navigation
        JTextField[] fields = {fname, lname, UsernamePhone, email, pass};
        
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
        if (jLabel1 != null) {
            jLabel1.setBounds(-15, 0, 398, 750);
        }
    }

    @Override
    public void addNotify() {
        super.addNotify();
        SwingUtilities.invokeLater(() -> setupLabelPosition());
    }

    private void loginbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginbuttonActionPerformed
        SwingUtilities.invokeLater(() -> {
            Window[] windows = Window.getWindows();
            for (Window window : windows) {
                if (window instanceof cephra.Frame.Phone) {
                    cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                    phoneFrame.switchPanel(new cephra.Phone.UserProfile.Phonelogin());
                    break;
                }
            }
        });
    }//GEN-LAST:event_loginbuttonActionPerformed

    private void termsconditionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_termsconditionActionPerformed
        if (termscondition.isSelected()) {
            // Show Terms and Conditions in a dialog
            showTermsAndConditions();
        }
    }//GEN-LAST:event_termsconditionActionPerformed

    private void registerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_registerActionPerformed
          // Validate all fields are filled
    String nameText = fname.getText().trim();
    String lastNameText = lname.getText().trim();
    String usernameText = UsernamePhone.getText().trim();
    String emailText = email.getText().trim();
    String passwordText = new String(pass.getPassword()).trim();

    if (nameText.isEmpty() || lastNameText.isEmpty() || usernameText.isEmpty() || emailText.isEmpty() || passwordText.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please fill in all fields!", "Incomplete Form", JOptionPane.WARNING_MESSAGE);
        return;
    }

    // Check if terms and conditions are agreed to
    if (!termscondition.isSelected()) {
        JOptionPane.showMessageDialog(this, "Please agree to the Terms and Conditions before registering!", "Terms and Conditions Required", JOptionPane.WARNING_MESSAGE);
        return;
    }

    // Call the database method to add the new user
    if (cephra.db.CephraDB.addUser(nameText, lastNameText, usernameText, emailText, passwordText)) {
        // Registration successful
        JOptionPane.showMessageDialog(this, "Registration successful!", "Success", JOptionPane.INFORMATION_MESSAGE);

        // Navigate to Phonelogin after OK is clicked
        SwingUtilities.invokeLater(() -> {
            Window[] windows = Window.getWindows();
            for (Window window : windows) {
                if (window instanceof cephra.Frame.Phone) {
                    cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                    phoneFrame.switchPanel(new cephra.Phone.UserProfile.Phonelogin());
                    break;
                }
            }
        });
    } else {
        // Username or email already exists
        JOptionPane.showMessageDialog(this, "Username or email already exists. Please choose a different one.", "Registration Failed", JOptionPane.WARNING_MESSAGE);
        fname.requestFocusInWindow();
    }
    }//GEN-LAST:event_registerActionPerformed

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
        pass.requestFocusInWindow(); // Move focus to password field
    }//GEN-LAST:event_emailActionPerformed

    private void passActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passActionPerformed
        // When Enter key is pressed on password field, trigger registration
        registerActionPerformed(evt);
    }//GEN-LAST:event_passActionPerformed

    private void SeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SeeActionPerformed
        // Toggle password visibility
        if(pass.getEchoChar() == 0) {
            // Currently showing password, hide it
            pass.setEchoChar('•');
            See.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/EyeClose.png")));
        } else {
            // Currently hiding password, show it
            pass.setEchoChar((char) 0);
            See.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/EyeOpen.png")));
        }
    }//GEN-LAST:event_SeeActionPerformed

    private void showTermsAndConditions() {
        String termsText = getTermsAndConditionsText();

        String safeText = termsText
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
        String html = "<html><head><style>" +
                "body{font-family:'Segoe UI',sans-serif;font-size:12px;line-height:1.5;text-align:justify;margin:0;}" +
                ".container{padding:0 4px;}" +
                "</style></head><body><div class='container'>" +
                safeText.replace("\n", "<br/>") +
                "</div></body></html>";

        javax.swing.JEditorPane editorPane = new javax.swing.JEditorPane("text/html", html);
        editorPane.setEditable(false);
        editorPane.setOpaque(false);
        editorPane.setFocusable(false);
        editorPane.setHighlighter(null);
        editorPane.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR));
        if (editorPane.getCaret() != null) {
            editorPane.getCaret().setVisible(false);
            editorPane.getCaret().setSelectionVisible(false);
        }

        javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(editorPane);
        scrollPane.setPreferredSize(new java.awt.Dimension(600, 400));
        scrollPane.setBorder(null);
        scrollPane.setWheelScrollingEnabled(true);
        scrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        java.awt.Window owner = javax.swing.SwingUtilities.getWindowAncestor(this);
        final javax.swing.JDialog dialog = new javax.swing.JDialog(owner, "Terms and Conditions", java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setUndecorated(true); // remove title bar and X
        dialog.setDefaultCloseOperation(javax.swing.JDialog.DO_NOTHING_ON_CLOSE);

        javax.swing.JPanel content = new javax.swing.JPanel(new java.awt.BorderLayout());
        content.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 12, 12, 12));
        content.add(scrollPane, java.awt.BorderLayout.CENTER);

        javax.swing.JButton ok = new javax.swing.JButton("OK");
        ok.setFocusPainted(false);
        ok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dialog.dispose();
            }
        });
        javax.swing.JPanel buttons = new javax.swing.JPanel();
        buttons.add(ok);
        content.add(buttons, java.awt.BorderLayout.SOUTH);

        dialog.setContentPane(content);
        dialog.setSize(320, 600); // fit inside 350x750 phone frame
        if (owner != null) {
            dialog.setLocationRelativeTo(owner); // center within phone frame
            java.awt.Point current = dialog.getLocation();
            dialog.setLocation(current.x - 3, current.y); // shift 3px left
        }
        // Ensure the content starts at the very top
        try {
            editorPane.setCaretPosition(0);
        } catch (Exception ignore) {}
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    editorPane.setCaretPosition(0);
                } catch (Exception ignore) {}
                javax.swing.JViewport vp = scrollPane.getViewport();
                if (vp != null) {
                    vp.setViewPosition(new java.awt.Point(0, 0));
                }
            }
        });
        dialog.setVisible(true);
    }

    private String getTermsAndConditionsText() {
        return "CEPHRA PHONE REGISTRATION\n TERMS AND CONDITIONS\n" +
               "Effective Date: " + java.time.LocalDate.now() + "\n" +
               "Version: 1.0\n\n" +
               "1. ACCEPTANCE OF TERMS\n" +
               "By accessing, registering, or using the Cephra Phone Registration System (\"the Service\"), " +
               "you acknowledge that you have read, understood, and agree to be bound by these Terms and Conditions (\"Terms\"). " +
               "If you do not agree to these Terms, you must not use the Service.\n\n" +
               "2. DEFINITIONS\n" +
               "- \"Cephra,\" \"we,\" \"us,\" \"our\" refers to Cephra and its affiliates\n" +
               "- \"User,\" \"you,\" \"your\" refers to any individual or entity using the Service\n" +
               "- \"Service\" refers to the Cephra Phone Registration System and all related features\n" +
               "- \"Account\" refers to your registered user account within the Service\n\n" +
               "3. ELIGIBILITY\n" +
               "3.1 Age Requirement: You must be at least 18 years old to register for and use the Service.\n" +
               "3.2 Legal Capacity: You must have the legal capacity to enter into binding agreements.\n\n" +
               "4. REGISTRATION AND ACCOUNT CREATION\n" +
               "4.1 Account Information: You must provide accurate, current, and complete information during registration.\n" +
               "4.2 Account Security: You are responsible for maintaining the confidentiality of your login credentials.\n" +
               "4.3 Account Ownership: Each account is personal and non-transferable.\n\n" +
               "5. ACCEPTABLE USE POLICY\n" +
               "You may use the Service for personal registration and account management. " +
               "You agree NOT to use the Service for any illegal or unauthorized purpose.\n\n" +
               "6. PRIVACY AND DATA PROTECTION\n" +
               "We implement appropriate security measures to protect your personal data. " +
               "We retain your personal data for as long as necessary to provide the Service.\n\n" +
               "7. INTELLECTUAL PROPERTY\n" +
               "The Service and all content, features, and functionality are owned by Cephra.\n\n" +
               "8. SERVICE AVAILABILITY\n" +
               "We reserve the right to modify, suspend, or discontinue the Service at any time.\n\n" +
               "9. LIMITATION OF LIABILITY\n" +
               "THE SERVICE IS PROVIDED \"AS IS\" AND \"AS AVAILABLE\" WITHOUT WARRANTIES OF ANY KIND.\n\n" +
               "10. TERMINATION\n" +
               "We may terminate or suspend your account immediately if you violate these Terms.\n\n" +
               "11. GOVERNING LAW AND DISPUTE RESOLUTION\n" +
               "These Terms are governed by the laws of Pasay City, Philippines. " +
               "Any disputes shall be resolved through binding arbitration in Pasay City, Philippines.\n\n" +
               "12. CONTACT INFORMATION\n" +
               "Cephra Support\n" +
               "Email: support@cephra.com\n" +
               "Phone: +63 2 8XXX XXXX\n" +
               "Address: Seaside Boulevard, Mall of Asia Complex, Pasay City, Philippines\n\n" +
               "By checking \"I Agree to the Terms and Conditions,\" you acknowledge that you have read, " +
               "understood, and agree to be bound by these Terms and Conditions.";
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton See;
    private javax.swing.JTextField UsernamePhone;
    private javax.swing.JTextField email;
    private javax.swing.JTextField fname;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTextField lname;
    private javax.swing.JButton loginbutton;
    private javax.swing.JPasswordField pass;
    private javax.swing.JButton register;
    private javax.swing.JCheckBox termscondition;
    // End of variables declaration//GEN-END:variables
}
