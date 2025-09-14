package cephra.Phone;

import javax.swing.SwingUtilities;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class Loading_Screen extends javax.swing.JPanel {
    public Loading_Screen() {
        initComponents();
        setPreferredSize(new java.awt.Dimension(370, 750));
        setSize(370, 750);
        
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
                    java.awt.Window window = SwingUtilities.getWindowAncestor(Loading_Screen.this);
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

        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        exitlogin = new javax.swing.JButton();

        setMaximumSize(new java.awt.Dimension(350, 740));
        setPreferredSize(new java.awt.Dimension(350, 750));
        setLayout(null);

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/tatatata.png"))); // NOI18N
        add(jLabel2);
        jLabel2.setBounds(100, 120, 160, 60);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Cephra Images/aaaa.png"))); // NOI18N
        add(jLabel1);
        jLabel1.setBounds(0, 0, 370, 750);

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
        exitlogin.setBounds(30, 440, 300, 260);
    }// </editor-fold>//GEN-END:initComponents

    private void exitloginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitloginActionPerformed
        java.awt.Window w = javax.swing.SwingUtilities.getWindowAncestor(Loading_Screen.this);
        if (w instanceof cephra.Frame.Phone) {
            ((cephra.Frame.Phone) w).switchPanel(new cephra.Phone.Utilities.Transition());
        }
    }//GEN-LAST:event_exitloginActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton exitlogin;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    // End of variables declaration//GEN-END:variables
}
