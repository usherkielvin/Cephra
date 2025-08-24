/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package cephra.Phone;

/**
 *
 * @author usher
 */
public class VerificationCode extends javax.swing.JPanel {

    /**
     * Creates new form VerificationCode
     */
    public VerificationCode() {
        initComponents();
        setPreferredSize(new java.awt.Dimension(350, 750));
        setSize(350, 750);
        email.setOpaque(false);
        email.setBackground(new java.awt.Color(0, 0, 0, 0));
        setupLabelPosition(); // Set label position
        setupButtons(); // Call our custom setup method
    }

    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        resend = new javax.swing.JButton();
        contactsup = new javax.swing.JButton();
        Back = new javax.swing.JButton();
        email = new javax.swing.JTextField();
        resetsend = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setLayout(null);

        resend.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        resend.setForeground(new java.awt.Color(0, 204, 204));
        resend.setText("Resend");
        resend.setBorder(null);
        resend.setBorderPainted(false);
        resend.setContentAreaFilled(false);
        resend.setFocusPainted(false);
        resend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resendActionPerformed(evt);
            }
        });
        add(resend);
        resend.setBounds(170, 370, 150, 30);

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

        Back.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Back.setForeground(new java.awt.Color(0, 204, 204));
        Back.setText("Back");
        Back.setBorder(null);
        Back.setBorderPainted(false);
        Back.setContentAreaFilled(false);
        Back.setFocusPainted(false);
        add(Back);
        Back.setBounds(120, 630, 120, 23);

        email.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        email.setBorder(null);
        email.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailActionPerformed(evt);
            }
        });
        add(email);
        email.setBounds(50, 340, 250, 30);

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
        resetsend.setBounds(50, 440, 270, 50);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Photos/verifycode.png"))); // NOI18N
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
        // Setup Resend button (only hover effects, action listener already set in initComponents)
        resend.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        resend.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                resend.setForeground(new java.awt.Color(0, 255, 255));
                resend.setText("<html><u>Resend</u></html>");
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                resend.setForeground(new java.awt.Color(0, 204, 204));
                resend.setText("Resend");
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

        // Setup Back button
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
        Back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backActionPerformed(evt);
            }
        });

        // Setup email field center alignment
        email.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    }

    private void emailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailActionPerformed

    }//GEN-LAST:event_emailActionPerformed

    private void resetsendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetsendActionPerformed
        // Handle verification code submission
        javax.swing.JOptionPane.showMessageDialog(this, "Verification code submitted successfully!", "Success", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        
        // Navigate to NewPassword panel after OK is clicked
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frame.Phone) {
                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                        phoneFrame.switchPanel(new cephra.Phone.NewPassword());
                        break;
                    }
                }
            }
        });
    }//GEN-LAST:event_resetsendActionPerformed

    private void resendActionPerformed(java.awt.event.ActionEvent evt) {
        javax.swing.JOptionPane.showMessageDialog(this, 
            "Verification code resent to your email!", 
            "Resent", 
            javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }

    private void backActionPerformed(java.awt.event.ActionEvent evt) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frame.Phone) {
                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                        phoneFrame.switchPanel(new cephra.Phone.ForgotPassword());
                        break;
                    }
                }
            }
        });
    }

    private void contactsupActionPerformed(java.awt.event.ActionEvent evt) {
        javax.swing.JOptionPane.showMessageDialog(this, "Contact support at: support@cephra.com", "Contact Support", javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void addNotify() {
        super.addNotify();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                setupLabelPosition(); // Set label position
                setupButtons(); // Setup buttons when panel is added to container
                if (email != null) {
                    email.requestFocusInWindow();
                }
                javax.swing.JRootPane root = javax.swing.SwingUtilities.getRootPane(VerificationCode.this);
                if (root != null && resetsend != null) {
                    root.setDefaultButton(resetsend);
                }
            }
        });
    }

    public void focusEmailField() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (email != null) {
                    email.requestFocusInWindow();
                }
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Back;
    private javax.swing.JButton contactsup;
    private javax.swing.JTextField email;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton resend;
    private javax.swing.JButton resetsend;
    // End of variables declaration//GEN-END:variables
}
