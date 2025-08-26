package cephra.Phone;

import javax.swing.SwingUtilities;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.Timer;

public class Transition extends javax.swing.JPanel {
   
    public Transition() {
        initComponents();
        setPreferredSize(new java.awt.Dimension(350, 750));
        setSize(350, 750);
        
        // Make the panel draggable
        makeDraggable();
         new Timer(7000, event -> {
                java.awt.Window w = javax.swing.SwingUtilities.getWindowAncestor(Transition.this);
        if (w instanceof cephra.Frame.Phone) {
            ((cephra.Frame.Phone) w).switchPanel(new cephra.Phone.Phonelogin());
        }
               
             
            }).start();
        
        
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
                    java.awt.Window window = SwingUtilities.getWindowAncestor(Transition.this);
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
        jLabel2 = new javax.swing.JLabel();

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

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Photos/Frame.png"))); // NOI18N
        add(jLabel1);
        jLabel1.setBounds(-30, 10, 398, 750);

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Photos/Transit.gif"))); // NOI18N
        add(jLabel2);
        jLabel2.setBounds(0, 0, 390, 740);
    }// </editor-fold>//GEN-END:initComponents

    private void exitloginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitloginActionPerformed
        java.awt.Window w = javax.swing.SwingUtilities.getWindowAncestor(Transition.this);
        if (w instanceof cephra.Frame.Phone) {
            ((cephra.Frame.Phone) w).switchPanel(new cephra.Phone.Phonelogin());
        }
    }//GEN-LAST:event_exitloginActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton exitlogin;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    // End of variables declaration//GEN-END:variables
}
