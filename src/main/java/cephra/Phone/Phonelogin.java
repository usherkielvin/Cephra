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

        pass = new javax.swing.JPasswordField();
        username = new javax.swing.JTextField();
        loginhome = new javax.swing.JButton();
        regbutton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(350, 750));
        setPreferredSize(new java.awt.Dimension(350, 750));
        setLayout(null);

        pass.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        pass.setBorder(null);
        pass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                attemptLogin();
            }
        });
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

        regbutton.setBorder(null);
        regbutton.setBorderPainted(false);
        regbutton.setContentAreaFilled(false);
        regbutton.setFocusPainted(false);
        regbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                regbuttonActionPerformed(evt);
            }
        });
        add(regbutton);
        regbutton.setBounds(210, 620, 120, 30);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Photos/LOGIN.png"))); // NOI18N
        add(jLabel1);
        jLabel1.setBounds(-15, 0, 398, 750);
    }// </editor-fold>//GEN-END:initComponents

    private void regbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_regbuttonActionPerformed
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
    }//GEN-LAST:event_regbuttonActionPerformed

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

    @Override
    public void addNotify() {
        super.addNotify();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
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
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton loginhome;
    private javax.swing.JPasswordField pass;
    private javax.swing.JButton regbutton;
    private javax.swing.JTextField username;
    // End of variables declaration//GEN-END:variables
}
