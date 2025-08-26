package cephra.Phone;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;

public class Reciept extends javax.swing.JPanel {
  
    public Reciept() {
        initComponents();
         setPreferredSize(new java.awt.Dimension(350, 750));
        setSize(350, 750);
        setupLabelPosition(); // Set label position
        makeDraggable();
    }
     // CUSTOM CODE - DO NOT REMOVE - NetBeans will regenerate form code but this method should be preserved
    // Setup label position to prevent NetBeans from changing it
    private void setupLabelPosition() {
        if (jLabel1 != null) {
            jLabel1.setBounds(-15, 0, 398, 750);
        }
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
                    java.awt.Window window = SwingUtilities.getWindowAncestor(Reciept.this);
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

        share = new javax.swing.JButton();
        Exit = new javax.swing.JButton();
        Download = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setLayout(null);

        share.setBorder(null);
        share.setBorderPainted(false);
        share.setContentAreaFilled(false);
        add(share);
        share.setBounds(180, 680, 130, 40);

        Exit.setBorder(null);
        Exit.setBorderPainted(false);
        Exit.setContentAreaFilled(false);
        Exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExitActionPerformed(evt);
            }
        });
        add(Exit);
        Exit.setBounds(290, 50, 50, 40);

        Download.setBorder(null);
        Download.setBorderPainted(false);
        Download.setContentAreaFilled(false);
        add(Download);
        Download.setBounds(30, 683, 120, 30);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Photos/Receipt.png"))); // NOI18N
        add(jLabel1);
        jLabel1.setBounds(-15, 0, 398, 750);
    }// </editor-fold>//GEN-END:initComponents

    private void ExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExitActionPerformed
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
    }//GEN-LAST:event_ExitActionPerformed

    private void DownloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DownloadActionPerformed
        JOptionPane.showMessageDialog(
            this,
            "Reciept saved to Photos",
            "Saved",
            JOptionPane.INFORMATION_MESSAGE
        );
    }//GEN-LAST:event_DownloadActionPerformed

    private void shareActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_shareActionPerformed
        JOptionPane.showMessageDialog(
            this,
            "Link copied",
            "Shared",
            JOptionPane.INFORMATION_MESSAGE
        );
    }//GEN-LAST:event_shareActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Download;
    private javax.swing.JButton Exit;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton share;
    // End of variables declaration//GEN-END:variables
}
