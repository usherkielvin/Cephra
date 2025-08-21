package cephra.Frames;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class AdminFrame extends javax.swing.JFrame {

    public AdminFrame() {
        setUndecorated(true);
        initComponents();
        setSize(1000, 750);
        setResizable(false);
        setAppIcon();
        addEscapeKeyListener();
        makeDraggable();
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
    
    private void initComponents() {
        cephra.AdminPanels.Splash splashPanel = new cephra.AdminPanels.Splash();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1000, 750));
        setResizable(false);

        getContentPane().setLayout(new java.awt.BorderLayout());
        getContentPane().add(splashPanel, java.awt.BorderLayout.CENTER);
    }
 public void switchPanel(JPanel newPanel) {
        getContentPane().removeAll();
        getContentPane().add(newPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    } 
 public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AdminFrame mainFrame = new AdminFrame();
            PhoneFrame phoneFrame = new PhoneFrame();
            TVFrame tvFrame = new TVFrame();

            java.awt.Rectangle screenBounds = java.awt.GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration()
                .getBounds();

            mainFrame.setLocation(
                screenBounds.x + screenBounds.width - mainFrame.getWidth(),
                screenBounds.y
            );
            mainFrame.setVisible(true);
            tvFrame.setVisible(true);
            phoneFrame.setLocationRelativeTo(null);
            phoneFrame.setVisible(true);
            phoneFrame.toFront();
            phoneFrame.requestFocus();
        });
    }
}