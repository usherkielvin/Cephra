
package cephra.PhonePanels;

import javax.swing.SwingUtilities;


public class link extends javax.swing.JPanel {

    
    public link() {
        initComponents();
        setPreferredSize(new java.awt.Dimension(350, 750));
        setSize(350, 750);
    }


   
    private void initComponents() {

        charge = new javax.swing.JButton();
        historybutton = new javax.swing.JButton();
        profilebutton = new javax.swing.JButton();
        homebutton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setLayout(null);

        charge.setBorderPainted(false);
        charge.setContentAreaFilled(false);
        charge.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chargeActionPerformed(evt);
            }
        });
        add(charge);
        charge.setBounds(40, 670, 50, 60);

        historybutton.setBorderPainted(false);
        historybutton.setContentAreaFilled(false);
        historybutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                historybuttonActionPerformed(evt);
            }
        });
        add(historybutton);
        historybutton.setBounds(230, 653, 50, 100);

        profilebutton.setBorderPainted(false);
        profilebutton.setContentAreaFilled(false);
        profilebutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                profilebuttonActionPerformed(evt);
            }
        });
        add(profilebutton);
        profilebutton.setBounds(290, 660, 80, 70);

        homebutton.setBorderPainted(false);
        homebutton.setContentAreaFilled(false);
        homebutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                homebuttonActionPerformed(evt);
            }
        });
        add(homebutton);
        homebutton.setBounds(170, 680, 50, 40);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Photos/LINKING.png"))); // NOI18N
        add(jLabel1);
        jLabel1.setBounds(-15, 0, 398, 750);
    }// </editor-fold>//GEN-END:initComponents

    private void chargeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chargeActionPerformed
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frames.PhoneFrame) {
                        cephra.Frames.PhoneFrame phoneFrame = (cephra.Frames.PhoneFrame) window;
                        phoneFrame.switchPanel(new cephra.PhonePanels.serviceoffered());
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
                    if (window instanceof cephra.Frames.PhoneFrame) {
                        cephra.Frames.PhoneFrame phoneFrame = (cephra.Frames.PhoneFrame) window;
                        phoneFrame.switchPanel(new cephra.PhonePanels.phonehistory());
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
                    if (window instanceof cephra.Frames.PhoneFrame) {
                        cephra.Frames.PhoneFrame phoneFrame = (cephra.Frames.PhoneFrame) window;
                        phoneFrame.switchPanel(new cephra.PhonePanels.Profile());
                        break;
                    }
                }
            }
        });
    }//GEN-LAST:event_profilebuttonActionPerformed

    private void homebuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_homebuttonActionPerformed
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frames.PhoneFrame) {
                        cephra.Frames.PhoneFrame phoneFrame = (cephra.Frames.PhoneFrame) window;
                        phoneFrame.switchPanel(new cephra.PhonePanels.home());
                        break;
                    }
                }
            }
        });
    }//GEN-LAST:event_homebuttonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton charge;
    private javax.swing.JButton historybutton;
    private javax.swing.JButton homebutton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton profilebutton;
    // End of variables declaration//GEN-END:variables
}
