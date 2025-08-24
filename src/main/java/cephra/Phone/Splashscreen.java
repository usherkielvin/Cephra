package cephra.Phone;

import javax.swing.SwingUtilities;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class Splashscreen extends javax.swing.JPanel {
   
    public Splashscreen() {
        initComponents();
        setPreferredSize(new java.awt.Dimension(350, 750));
        setSize(350, 750);
        
        // Make the panel draggable
        makeDraggable();
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
                    java.awt.Window window = SwingUtilities.getWindowAncestor(Splashscreen.this);
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

        exitlogin = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(350, 750));
        setPreferredSize(new java.awt.Dimension(350, 750));
        setLayout(null);

        exitlogin.setBorder(null);
        exitlogin.setBorderPainted(false);
        exitlogin.setContentAreaFilled(false);
        exitlogin.setFocusPainted(false);
        exitlogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitloginActionPerformed(evt);
            }
        });
        add(exitlogin);
        exitlogin.setBounds(60, 630, 220, 70);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Photos/splash in2.png"))); // NOI18N
        add(jLabel1);
        jLabel1.setBounds(-15, 0, 398, 750);
    }// </editor-fold>//GEN-END:initComponents

    private void exitloginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitloginActionPerformed
        java.awt.Window w = javax.swing.SwingUtilities.getWindowAncestor(Splashscreen.this);
        if (w instanceof cephra.Frame.Phone) {
            ((cephra.Frame.Phone) w).switchPanel(new cephra.Phone.Phonelogin());
        }
    }//GEN-LAST:event_exitloginActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton exitlogin;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}
