package cephra.Phone;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.SwingUtilities;

public class Register extends javax.swing.JPanel {

    public Register() {
        initComponents();
        setPreferredSize(new java.awt.Dimension(350, 750));
        setSize(350, 750);
     
        setupButtons(); // Setup button hover effects
        setupTextFields(); // Setup text field properties
        makeDraggable();
        
        // Auto-focus on name field
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (name != null) {
                    name.requestFocusInWindow();
                }
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
                    java.awt.Window window = SwingUtilities.getWindowAncestor(Register.this);
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

        confirmpass = new javax.swing.JTextField();
        password = new javax.swing.JTextField();
        email = new javax.swing.JTextField();
        name = new javax.swing.JTextField();
        register = new javax.swing.JButton();
        termscondition = new javax.swing.JCheckBox();
        loginbutton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(350, 750));
        setPreferredSize(new java.awt.Dimension(350, 750));
        setLayout(null);

        confirmpass.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        confirmpass.setBorder(null);
        confirmpass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmpassActionPerformed(evt);
            }
        });
        add(confirmpass);
        confirmpass.setBounds(40, 510, 280, 40);

        password.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        password.setBorder(null);
        password.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passwordActionPerformed(evt);
            }
        });
        add(password);
        password.setBounds(40, 445, 280, 40);

        email.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        email.setBorder(null);
        email.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailActionPerformed(evt);
            }
        });
        add(email);
        email.setBounds(40, 380, 280, 40);

        name.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        name.setBorder(null);
        name.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameActionPerformed(evt);
            }
        });
        add(name);
        name.setBounds(40, 320, 280, 40);

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
        register.setBounds(60, 580, 220, 50);

        termscondition.setBackground(new java.awt.Color(255, 255, 255));
        termscondition.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        termscondition.setForeground(new java.awt.Color(0, 0, 0));
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
        termscondition.setBounds(46, 627, 280, 40);

        loginbutton.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        loginbutton.setForeground(new java.awt.Color(0, 0, 0));
        loginbutton.setText("Login");
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
        loginbutton.setBounds(190, 673, 90, 40);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Photos/REGISTER.png"))); // NOI18N
        add(jLabel1);
        jLabel1.setBounds(0, -4, 370, 760);
    }// </editor-fold>//GEN-END:initComponents

    // Setup button hover effects
    private void setupButtons() {
        // Setup Register button hover effects
        register.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        register.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                register.setForeground(new java.awt.Color(0, 204, 204));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                register.setForeground(new java.awt.Color(0, 0, 0));
            }
        });

        // Setup Login button hover effects
        loginbutton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        loginbutton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginbutton.setForeground(new java.awt.Color(0, 204, 204));
                loginbutton.setText("<html><u>Login</u></html>");
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginbutton.setForeground(new java.awt.Color(51, 51, 51));
                loginbutton.setText("Login");
            }
        });
    }

    // Setup text field properties
    private void setupTextFields() {
        // Make text fields transparent
        name.setOpaque(false);
        name.setBackground(new java.awt.Color(0, 0, 0, 0));
        email.setOpaque(false);
        email.setBackground(new java.awt.Color(0, 0, 0, 0));
        password.setOpaque(false);
        password.setBackground(new java.awt.Color(0, 0, 0, 0));
        confirmpass.setOpaque(false);
        confirmpass.setBackground(new java.awt.Color(0, 0, 0, 0));
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
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                setupLabelPosition();
            }
        });
    }

    private void loginbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginbuttonActionPerformed
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
    }//GEN-LAST:event_loginbuttonActionPerformed

    private void termsconditionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_termsconditionActionPerformed
        if (termscondition.isSelected()) {
            // Show Terms and Conditions in a dialog
            showTermsAndConditions();
        }
    }//GEN-LAST:event_termsconditionActionPerformed

    private void registerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_registerActionPerformed
          // Validate all fields are filled
    String nameText = name.getText().trim();
    String emailText = email.getText().trim();
    String passwordText = password.getText().trim();
    String confirmPassText = confirmpass.getText().trim();

    if (nameText.isEmpty() || emailText.isEmpty() || passwordText.isEmpty() || confirmPassText.isEmpty()) {
        javax.swing.JOptionPane.showMessageDialog(this, "Please fill in all fields!", "Incomplete Form", javax.swing.JOptionPane.WARNING_MESSAGE);
        return;
    }

    // Check if passwords match
    if (!passwordText.equals(confirmPassText)) {
        javax.swing.JOptionPane.showMessageDialog(this, "Passwords do not match!", "Password Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        password.setText("");
        confirmpass.setText("");
        password.requestFocusInWindow();
        return;
    }

    // Check if terms and conditions are agreed to
    if (!termscondition.isSelected()) {
        javax.swing.JOptionPane.showMessageDialog(this, "Please agree to the Terms and Conditions before registering!", "Terms and Conditions Required", javax.swing.JOptionPane.WARNING_MESSAGE);
        return;
    }

    // Call the database method to add the new user
    if (cephra.CephraDB.addUser(nameText, emailText, passwordText)) {
        // Registration successful
        javax.swing.JOptionPane.showMessageDialog(this, "Registration successful!", "Success", javax.swing.JOptionPane.INFORMATION_MESSAGE);

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
        // Username or email already exists
        javax.swing.JOptionPane.showMessageDialog(this, "Username or email already exists. Please choose a different one.", "Registration Failed", javax.swing.JOptionPane.WARNING_MESSAGE);
        name.requestFocusInWindow();
    }
    }//GEN-LAST:event_registerActionPerformed

    private void nameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameActionPerformed
        email.requestFocusInWindow(); // Move focus to email field
    }//GEN-LAST:event_nameActionPerformed

    private void emailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailActionPerformed
        password.requestFocusInWindow(); // Move focus to password field
    }//GEN-LAST:event_emailActionPerformed

    private void passwordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passwordActionPerformed
        confirmpass.requestFocusInWindow(); // Move focus to confirm password field
    }//GEN-LAST:event_passwordActionPerformed

    private void confirmpassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmpassActionPerformed
        registerActionPerformed(evt); // Trigger registration when Enter is pressed on confirm password
    }//GEN-LAST:event_confirmpassActionPerformed

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
    private javax.swing.JTextField confirmpass;
    private javax.swing.JTextField email;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton loginbutton;
    private javax.swing.JTextField name;
    private javax.swing.JTextField password;
    private javax.swing.JButton register;
    private javax.swing.JCheckBox termscondition;
    // End of variables declaration//GEN-END:variables
}

