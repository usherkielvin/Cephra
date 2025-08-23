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

        startbutton = new javax.swing.JButton();
        t = new javax.swing.JLabel();

        setLayout(null);

        startbutton.setBorderPainted(false);
        startbutton.setContentAreaFilled(false);
        startbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startbuttonActionPerformed(evt);
            }
        });
        add(startbutton);
        startbutton.setBounds(50, 663, 270, 50);

        t.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Photos/de final na talaga.gif"))); // NOI18N
        add(t);
        t.setBounds(-15, 0, 398, 750);
    }// </editor-fold>//GEN-END:initComponents

    private void startbuttonActionPerformed(java.awt.event.ActionEvent evt) {
        // Navigate to Phone Login panel
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    if (window instanceof cephra.Frame.Phone) {
                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                        phoneFrame.switchPanel(new cephra.Phone.Phonelogin());
                        break;
                    }
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton startbutton;
    private javax.swing.JLabel t;
    // End of variables declaration//GEN-END:variables
}
