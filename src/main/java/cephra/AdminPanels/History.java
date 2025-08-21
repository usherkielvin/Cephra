
package cephra.AdminPanels;

import java.awt.Window;
import javax.swing.SwingUtilities;
import cephra.Frames.AdminFrame;

public class History extends javax.swing.JPanel {

    public History() {
        initComponents();
        setPreferredSize(new java.awt.Dimension(1000, 750));
        setSize(1000, 750);
    }

 
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Baybutton = new javax.swing.JButton();
        staffbutton = new javax.swing.JButton();
        businessbutton = new javax.swing.JButton();
        exitlogin = new javax.swing.JButton();
        quebutton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setLayout(null);

        Baybutton.setBorder(null);
        Baybutton.setBorderPainted(false);
        Baybutton.setContentAreaFilled(false);
        Baybutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BaybuttonActionPerformed(evt);
            }
        });
        add(Baybutton);
        Baybutton.setBounds(380, 10, 40, 40);

        staffbutton.setBorder(null);
        staffbutton.setBorderPainted(false);
        staffbutton.setContentAreaFilled(false);
        staffbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                staffbuttonActionPerformed(evt);
            }
        });
        add(staffbutton);
        staffbutton.setBounds(500, 10, 110, 40);

        businessbutton.setBorder(null);
        businessbutton.setBorderPainted(false);
        businessbutton.setContentAreaFilled(false);
        businessbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                businessbuttonActionPerformed(evt);
            }
        });
        add(businessbutton);
        businessbutton.setBounds(610, 10, 140, 40);

        exitlogin.setBorder(null);
        exitlogin.setBorderPainted(false);
        exitlogin.setContentAreaFilled(false);
        exitlogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitloginActionPerformed(evt);
            }
        });
        add(exitlogin);
        exitlogin.setBounds(930, 0, 70, 60);

        quebutton.setBorder(null);
        quebutton.setBorderPainted(false);
        quebutton.setContentAreaFilled(false);
        quebutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quebuttonActionPerformed(evt);
            }
        });
        add(quebutton);
        quebutton.setBounds(270, 10, 100, 40);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Photos/DASHBOARD - HISTORY.png"))); // NOI18N
        add(jLabel1);
        jLabel1.setBounds(0, 0, 1000, 750);
    }// </editor-fold>//GEN-END:initComponents

    private void BaybuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BaybuttonActionPerformed
        Window w = SwingUtilities.getWindowAncestor(History.this);
        if (w instanceof AdminFrame) {
            ((AdminFrame) w).switchPanel(new Bay());
        }
    }//GEN-LAST:event_BaybuttonActionPerformed

    private void businessbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_businessbuttonActionPerformed
        Window w = SwingUtilities.getWindowAncestor(History.this);
        if (w instanceof AdminFrame) {
            ((AdminFrame) w).switchPanel(new AdminDashboard());
        }
    }//GEN-LAST:event_businessbuttonActionPerformed

    private void staffbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_staffbuttonActionPerformed
         Window w = SwingUtilities.getWindowAncestor(History.this);
        if (w instanceof AdminFrame) {
            ((AdminFrame) w).switchPanel(new StaffRecord());
        }
    }//GEN-LAST:event_staffbuttonActionPerformed

    private void quebuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quebuttonActionPerformed
         Window w = SwingUtilities.getWindowAncestor(History.this);
        if (w instanceof AdminFrame) {
            ((AdminFrame) w).switchPanel(new Queue());
        }
    }//GEN-LAST:event_quebuttonActionPerformed

    private void exitloginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitloginActionPerformed
        Window w = SwingUtilities.getWindowAncestor(History.this);
        if (w instanceof AdminFrame) {
            ((AdminFrame) w).switchPanel(new loginpanel());
        }
    }//GEN-LAST:event_exitloginActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Baybutton;
    private javax.swing.JButton businessbutton;
    private javax.swing.JButton exitlogin;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton quebutton;
    private javax.swing.JButton staffbutton;
    // End of variables declaration//GEN-END:variables
}
