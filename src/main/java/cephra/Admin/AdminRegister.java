package cephra.Admin;

import java.awt.*;
import javax.swing.*;

public class AdminRegister extends javax.swing.JPanel {

    public AdminRegister() {
        initComponents();
        setPreferredSize(new java.awt.Dimension(1000, 750));
        setSize(1000, 750);
        
        Firstname.setOpaque(false);
        Firstname.setBackground(new Color(0, 0, 0, 0));  
        LastName.setOpaque(false);
        LastName.setBackground(new Color(0, 0, 0, 0));
        email.setOpaque(false);
        email.setBackground(new Color(0, 0, 0, 0));
        username.setOpaque(false);
        username.setBackground(new Color(0, 0, 0, 0));
        pass.setOpaque(false);
        pass.setBackground(new Color(0, 0, 0, 0));
        ConPass.setOpaque(false);
        ConPass.setBackground(new Color(0, 0, 0, 0));     
        setupTextFields();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        See1 = new javax.swing.JButton();
        See = new javax.swing.JButton();
        username = new javax.swing.JTextField();
        email = new javax.swing.JTextField();
        RegisterBTN = new javax.swing.JButton();
        pass = new javax.swing.JPasswordField();
        ConPass = new javax.swing.JPasswordField();
        log = new javax.swing.JButton();
        AdminBTN = new javax.swing.JButton();
        Firstname = new javax.swing.JTextField();
        LastName = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(1000, 750));
        setLayout(null);

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
        See1.setBounds(900, 460, 80, 40);

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
        See.setBounds(610, 460, 60, 40);

        username.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        username.setBorder(null);
        username.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                usernameActionPerformed(evt);
            }
        });
        add(username);
        username.setBounds(388, 362, 260, 37);

        email.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        email.setBorder(null);
        email.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailActionPerformed(evt);
            }
        });
        add(email);
        email.setBounds(700, 360, 260, 37);

        RegisterBTN.setBorderPainted(false);
        RegisterBTN.setContentAreaFilled(false);
        RegisterBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RegisterBTNActionPerformed(evt);
            }
        });
        add(RegisterBTN);
        RegisterBTN.setBounds(570, 620, 230, 50);

        pass.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        pass.setBorder(null);
        pass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passActionPerformed(evt);
            }
        });
        add(pass);
        pass.setBounds(390, 460, 260, 37);

        ConPass.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        ConPass.setBorder(null);
        ConPass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ConPassActionPerformed(evt);
            }
        });
        add(ConPass);
        ConPass.setBounds(700, 460, 260, 38);

        log.setBorderPainted(false);
        log.setContentAreaFilled(false);
        log.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logActionPerformed(evt);
            }
        });
        add(log);
        log.setBounds(870, 30, 110, 30);
        log.getAccessibleContext().setAccessibleName("log");

        AdminBTN.setBorderPainted(false);
        AdminBTN.setContentAreaFilled(false);
        AdminBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AdminBTNActionPerformed(evt);
            }
        });
        add(AdminBTN);
        AdminBTN.setBounds(870, 73, 110, 30);

        Firstname.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        Firstname.setBorder(null);
        Firstname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FirstnameActionPerformed(evt);
            }
        });
        add(Firstname);
        Firstname.setBounds(390, 263, 120, 37);

        LastName.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        LastName.setBorder(null);
        LastName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LastNameActionPerformed(evt);
            }
        });
        add(LastName);
        LastName.setBounds(533, 263, 120, 37);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/ADMINregister.png"))); // NOI18N
        add(jLabel1);
        jLabel1.setBounds(0, 0, 1000, 750);
    }// </editor-fold>//GEN-END:initComponents

    private void FirstnameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FirstnameActionPerformed
        LastName.requestFocus();
    }//GEN-LAST:event_FirstnameActionPerformed

    private void LastNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LastNameActionPerformed
        username.requestFocus();
    }//GEN-LAST:event_LastNameActionPerformed

    private void usernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_usernameActionPerformed
        email.requestFocus();
    }//GEN-LAST:event_usernameActionPerformed

    private void emailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailActionPerformed
        pass.requestFocus();
    }//GEN-LAST:event_emailActionPerformed

    private void passActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passActionPerformed
        ConPass.requestFocus();
    }//GEN-LAST:event_passActionPerformed

    private void ConPassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ConPassActionPerformed
       
        // Pressing Enter in confirm password triggers registration
        RegisterBTNActionPerformed(evt);
    }//GEN-LAST:event_ConPassActionPerformed

    private void RegisterBTNActionPerformed(java.awt.event.ActionEvent evt) {
        String firstname = Firstname.getText().trim();
        String lastname = LastName.getText().trim();
        String usernameVal = username.getText().trim();
        String emailVal = email.getText().trim();
        String password = new String(pass.getPassword()).trim();
        String confirm = new String(ConPass.getPassword()).trim();

        if (firstname.isEmpty() || lastname.isEmpty() || usernameVal.isEmpty() || emailVal.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Please fill in all fields", "Validation", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!password.equals(confirm)) {
            javax.swing.JOptionPane.showMessageDialog(this, "Passwords do not match", "Validation", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Basic email format check
        if (!emailVal.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            javax.swing.JOptionPane.showMessageDialog(this, "Please enter a valid email address.", "Validation", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        cephra.Admin.StaffData.addStaff(firstname, lastname, usernameVal, emailVal, password);

        javax.swing.JOptionPane.showMessageDialog(this, "Admin registration successful", "Success", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        Firstname.setText("");
        LastName.setText("");
        username.setText("");
        email.setText("");
        pass.setText("");
        ConPass.setText("");
    }

    private void logActionPerformed(java.awt.event.ActionEvent evt) {
         Window w = SwingUtilities.getWindowAncestor(AdminRegister.this);
        if (w instanceof cephra.Frame.Admin) {
            ((cephra.Frame.Admin) w).switchPanel(new cephra.Admin.Login());
        }
    }

    private void AdminBTNActionPerformed(java.awt.event.ActionEvent evt) {
       Window w = SwingUtilities.getWindowAncestor(AdminRegister.this);
        if (w instanceof cephra.Frame.Admin) {
            ((cephra.Frame.Admin) w).switchPanel(new StaffRecord());
        }
    }
    
    private void SeeActionPerformed(java.awt.event.ActionEvent evt) { 
        togglePasswordVisibility(pass, See);
    }
    
    private void See1ActionPerformed(java.awt.event.ActionEvent evt) {  
        togglePasswordVisibility(ConPass, See1);
    }
    
    @Override
    public void addNotify() {
        super.addNotify();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (Firstname != null) {
                    Firstname.requestFocusInWindow();
                }
            }
        });
    }

    private void setupTextFields() {      
        setupAutoCapitalization(Firstname);
        setupAutoCapitalization(LastName);             
        setupAutoFillUsername();
        setupEmailDomainCompletion();  
        setupBackspaceNavigation();
    }
    
    private void setupAutoCapitalization(javax.swing.JTextField textField) {
        textField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
               
                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_SPACE) {
                    return; 
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
                    
                    
                    String newText = capitalized.toString();
                    if (!newText.equals(text)) {
                        int caretPos = textField.getCaretPosition();
                        textField.setText(newText);
                       
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
    
   
    private void setupAutoFillUsername() {
        // Add focus listener to last name field to auto-fill username
        LastName.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                String firstName = Firstname.getText().trim();
                String lastName = LastName.getText().trim();
                
                if (!firstName.isEmpty() && !lastName.isEmpty()) {
                    // Combine first name + last name in lowercase (removes spaces)
                    String suggestedUsername = (firstName + lastName).replaceAll("\\s+", "").toLowerCase();
                    username.setText(suggestedUsername);
                }
            }
        });
    }
    
    // Setup email domain auto-completion
    private void setupEmailDomainCompletion() {
        email.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
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
        javax.swing.JTextField[] fields = {Firstname, LastName, username, email, pass, ConPass};
        
        for (int i = 0; i < fields.length; i++) {
            final int currentIndex = i;
            final javax.swing.JTextField currentField = fields[i];
            
            currentField.addKeyListener(new java.awt.event.KeyAdapter() {
                @Override
                public void keyPressed(java.awt.event.KeyEvent e) {
                    // Only handle backspace key when field is empty
                    if (e.getKeyCode() == java.awt.event.KeyEvent.VK_BACK_SPACE && currentField.getText().isEmpty()) {
                        // Prevent default backspace behavior and move to previous field
                        e.consume();
                        if (currentIndex > 0) {
                            fields[currentIndex - 1].requestFocusInWindow();
                            // Move cursor to end of previous field
                            javax.swing.SwingUtilities.invokeLater(() -> {
                                fields[currentIndex - 1].setCaretPosition(fields[currentIndex - 1].getText().length());
                            });
                        }
                    }
                }
            });
        }
    }
    
    private void togglePasswordVisibility(javax.swing.JPasswordField passwordField, javax.swing.JButton toggleButton) {
        if (passwordField.getEchoChar() == '\u0000') {
            // Currently visible, hide it
            passwordField.setEchoChar('•');
            toggleButton.setText("👁");
        } else {
            // Currently hidden, show it
            passwordField.setEchoChar('\u0000');
            toggleButton.setText("🙈");
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AdminBTN;
    private javax.swing.JPasswordField ConPass;
    private javax.swing.JTextField Firstname;
    private javax.swing.JTextField LastName;
    private javax.swing.JButton RegisterBTN;
    private javax.swing.JButton See;
    private javax.swing.JButton See1;
    private javax.swing.JTextField email;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton log;
    private javax.swing.JPasswordField pass;
    private javax.swing.JTextField username;
    // End of variables declaration//GEN-END:variables
}
