package cephra.Phone;

import java.awt.Color;
import javax.swing.SwingUtilities;

public class Phonelogin extends javax.swing.JPanel {

    public Phonelogin() {
        initComponents();
        setPreferredSize(new java.awt.Dimension(350, 750));
        setSize(350, 750);
        username.setOpaque(false);
        username.setBackground(new Color(0, 0, 0, 0));
        pass.setOpaque(false);
        pass.setBackground(new Color(0, 0, 0, 0));
    }
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        reghere = new javax.swing.JButton();
        forgotpass = new javax.swing.JButton();
        pass = new javax.swing.JPasswordField();
        username = new javax.swing.JTextField();
        loginhome = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(350, 750));
        setPreferredSize(new java.awt.Dimension(350, 750));
        setLayout(null);

                 reghere.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
         reghere.setForeground(new java.awt.Color(0, 204, 204));
         reghere.setText("Register here");
         reghere.setBorder(null);
         reghere.setBorderPainted(false);
         reghere.setContentAreaFilled(false);
         reghere.setFocusPainted(false);
         reghere.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
         reghere.addMouseListener(new java.awt.event.MouseAdapter() {
             public void mouseEntered(java.awt.event.MouseEvent evt) {
                 reghere.setForeground(new java.awt.Color(0, 255, 255));
                 reghere.setText("<html><u>Register here</u></html>");
             }
             public void mouseExited(java.awt.event.MouseEvent evt) {
                 reghere.setForeground(new java.awt.Color(0, 204, 204));
                 reghere.setText("Register here");
             }
         });
         reghere.addActionListener(new java.awt.event.ActionListener() {
             public void actionPerformed(java.awt.event.ActionEvent evt) {
                 reghereActionPerformed(evt);
             }
         });
        add(reghere);
        reghere.setBounds(190, 610, 120, 40);

                 forgotpass.setForeground(new java.awt.Color(102, 102, 102));
         forgotpass.setText("Forgot password?");
         forgotpass.setBorder(null);
         forgotpass.setBorderPainted(false);
         forgotpass.setContentAreaFilled(false);
         forgotpass.setFocusPainted(false);
         forgotpass.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                   forgotpass.addMouseListener(new java.awt.event.MouseAdapter() {
              public void mouseEntered(java.awt.event.MouseEvent evt) {
                  forgotpass.setText("<html><u>Forgot password?</u></html>");
              }
              public void mouseExited(java.awt.event.MouseEvent evt) {
                  forgotpass.setText("Forgot password?");
              }
          });
         forgotpass.addActionListener(new java.awt.event.ActionListener() {
             public void actionPerformed(java.awt.event.ActionEvent evt) {
                 forgotpassActionPerformed(evt);
             }
         });
        add(forgotpass);
        forgotpass.setBounds(200, 410, 120, 40);

        pass.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        pass.setBorder(null);
        add(pass);
        pass.setBounds(60, 380, 240, 30);

        username.setBorder(null);
        username.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                usernameActionPerformed(evt);
            }
        });
        add(username);
        username.setBounds(60, 330, 240, 40);

        loginhome.setBorder(null);
        loginhome.setBorderPainted(false);
        loginhome.setContentAreaFilled(false);
        loginhome.setFocusPainted(false);
        loginhome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginhomeActionPerformed(evt);
            }
        });
        add(loginhome);
        loginhome.setBounds(80, 570, 220, 40);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Photos/1.png"))); // NOI18N
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

    private void loginhomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginhomeActionPerformed
        attemptLogin();
    }//GEN-LAST:event_loginhomeActionPerformed

    private void attemptLogin() {
        String usernameText = username.getText() != null ? username.getText().trim() : "";
        String password = new String(pass.getPassword());
        if ("admin".equals(usernameText) && "1234".equals(password)) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    java.awt.Window[] windows = java.awt.Window.getWindows();
                    for (java.awt.Window window : windows) {
                        if (window instanceof cephra.Frame.Phone) {
                            cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                            phoneFrame.switchPanel(new cephra.Phone.home());
                            break;
                        }
                    }
                }
            });
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "Invalid credentials (demo: admin / 1234)", "Login Failed", javax.swing.JOptionPane.WARNING_MESSAGE);
            pass.setText(""); // Clear password field
            username.requestFocusInWindow(); // Refocus on username field
        }
    }

    private void usernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_usernameActionPerformed
        pass.requestFocus();
    }//GEN-LAST:event_usernameActionPerformed

    private void reghereActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reghereActionPerformed
       SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frame.Phone) {
                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                        phoneFrame.switchPanel(new cephra.Phone.Register());
                        break;
                    }
                }
            }
        });
    }//GEN-LAST:event_reghereActionPerformed

    private void forgotpassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_forgotpassActionPerformed
        SwingUtilities.invokeLater(new Runnable() {
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
    }//GEN-LAST:event_forgotpassActionPerformed

    @Override
    public void addNotify() {
        super.addNotify();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                setupLabelPosition(); // Set label position
                if (username != null) {
                    username.requestFocusInWindow();
                }
                javax.swing.JRootPane root = javax.swing.SwingUtilities.getRootPane(Phonelogin.this);
                if (root != null && loginhome != null) {
                    root.setDefaultButton(loginhome);
                }
            }
        });
    }

    public void focusUserField() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (username != null) {
                    username.requestFocusInWindow();
                }
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton forgotpass;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton loginhome;
    private javax.swing.JPasswordField pass;
    private javax.swing.JButton reghere;
    private javax.swing.JTextField username;
    // End of variables declaration//GEN-END:variables
}
