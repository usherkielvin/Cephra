package cephra;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainFrame extends javax.swing.JFrame {

    public MainFrame() {
        initComponents();
        setSize(1000, 750);
        setResizable(false);
        setAppIcon();
        addEscapeKeyListener();
        makeDraggable();
        setUndecorated(true);
        
        adminLoginPanel = new cephra.AdminPanels.AdminLogin();
    }
     private void setAppIcon() {
        java.net.URL iconUrl = getClass().getClassLoader().getResource("cephra/Photos/lod.png");
        if (iconUrl != null) {
            setIconImage(new javax.swing.ImageIcon(iconUrl).getImage());
        }
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
        // </editor-fold>
    
   
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        adminLoginPanel = new cephra.AdminPanels.AdminLogin();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1000, 750));
        setResizable(false);

        // Use BorderLayout and add the admin panel directly
        getContentPane().setLayout(new java.awt.BorderLayout());
        getContentPane().add(adminLoginPanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
 public void switchPanel(JPanel newPanel) {
        getContentPane().removeAll();
        getContentPane().add(newPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    } 
 public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            PhoneFrame phoneFrame = new PhoneFrame();

            // Position the frames
            mainFrame.setLocation(100, 100);
            phoneFrame.setLocation(1200, 100);

            // Show both
            mainFrame.setVisible(true);
            phoneFrame.setVisible(true);
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private cephra.AdminPanels.AdminLogin adminLoginPanel;
    // End of variables declaration//GEN-END:variables
}
