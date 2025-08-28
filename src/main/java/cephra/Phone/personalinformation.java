
package cephra.Phone;


public class personalinformation extends javax.swing.JPanel {

 
    public personalinformation() {
        initComponents();
        setPreferredSize(new java.awt.Dimension(350, 750));
        setSize(350, 750);
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        charge = new javax.swing.JButton();
        link = new javax.swing.JButton();
        home = new javax.swing.JButton();
        history = new javax.swing.JButton();
        back = new javax.swing.JButton();
        bg = new javax.swing.JLabel();

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Photos/personalinformation.png"))); // NOI18N

        setPreferredSize(new java.awt.Dimension(350, 750));
        setLayout(null);

        charge.setBorderPainted(false);
        charge.setContentAreaFilled(false);
        charge.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chargeActionPerformed(evt);
            }
        });
        add(charge);
        charge.setBounds(50, 693, 30, 30);

        link.setBorderPainted(false);
        link.setContentAreaFilled(false);
        link.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                linkActionPerformed(evt);
            }
        });
        add(link);
        link.setBounds(100, 690, 40, 40);

        home.setBorderPainted(false);
        home.setContentAreaFilled(false);
        home.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                homeActionPerformed(evt);
            }
        });
        add(home);
        home.setBounds(150, 690, 50, 40);

        history.setBorderPainted(false);
        history.setContentAreaFilled(false);
        history.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                historyActionPerformed(evt);
            }
        });
        add(history);
        history.setBounds(210, 690, 50, 40);

        back.setBorderPainted(false);
        back.setContentAreaFilled(false);
        back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backActionPerformed(evt);
            }
        });
        add(back);
        back.setBounds(20, 60, 40, 40);

        bg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Photos/personalinformation.png"))); // NOI18N
        bg.setFocusable(false);
        add(bg);
        bg.setBounds(-10, -60, 400, 890);
    }// </editor-fold>//GEN-END:initComponents

    private void chargeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chargeActionPerformed
                   public void run() {
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frame.Phone) {
                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                        phoneFrame.switchPanel(new cephra.Phone.helpcenter());
                        break;
                    }
                }
            }
        });  // TODO add your handling code here:
    }//GEN-LAST:event_chargeActionPerformed

    private void linkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_linkActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_linkActionPerformed

    private void homeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_homeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_homeActionPerformed

    private void historyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_historyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_historyActionPerformed

    private void backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_backActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton back;
    private javax.swing.JLabel bg;
    private javax.swing.JButton charge;
    private javax.swing.JButton history;
    private javax.swing.JButton home;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton link;
    // End of variables declaration//GEN-END:variables
}
