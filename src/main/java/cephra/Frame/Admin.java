package cephra.Frame;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Admin extends javax.swing.JFrame {

    public Admin() {
        setUndecorated(true);
        initComponents();
        setSize(1000, 750);
        setResizable(false);
      
        addEscapeKeyListener();
        makeDraggable();
    }
    

    private void addEscapeKeyListener() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    System.exit(0);
                }
            }
        });
        setFocusable(true);
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
                    Point currentLocation = getLocation();
                    setLocation(
                        currentLocation.x + e.getX() - dragPoint[0].x,
                        currentLocation.y + e.getY() - dragPoint[0].y
                    );
                }
            }
        });
    }
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        cephra.Admin.Splash splashPanel = new cephra.Admin.Splash();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1000, 750));
        setResizable(false);

        getContentPane().setLayout(new java.awt.BorderLayout());
        getContentPane().add(splashPanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    public void switchPanel(JPanel newPanel) {
        getContentPane().removeAll();
        getContentPane().add(newPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}