package cephra.PhonePanels;

import javax.swing.SwingUtilities;

public class Phonelogin extends javax.swing.JPanel {

 
    public Phonelogin() {
        initComponents();
        setPreferredSize(new java.awt.Dimension(350, 750));
        setSize(350, 750);
    }

    
   
    private void initComponents() {

        loginhome = new javax.swing.JButton();
        regbutton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setLayout(null);

        loginhome.setBorder(null);
        loginhome.setBorderPainted(false);
        loginhome.setContentAreaFilled(false);
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
                    if (window instanceof cephra.Frames.PhoneFrame) {
                        cephra.Frames.PhoneFrame phoneFrame = (cephra.Frames.PhoneFrame) window;
                        phoneFrame.switchPanel(new cephra.PhonePanels.Register());
                        break;
                    }
                }
            }
        });     
    }//GEN-LAST:event_regbuttonActionPerformed

    private void loginhomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginhomeActionPerformed
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
    }//GEN-LAST:event_loginhomeActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton loginhome;
    private javax.swing.JButton regbutton;
    // End of variables declaration//GEN-END:variables
}
